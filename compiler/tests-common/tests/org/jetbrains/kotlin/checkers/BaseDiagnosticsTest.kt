/*
 * Copyright 2010-2022 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.checkers

import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Condition
import com.intellij.openapi.util.Conditions
import com.intellij.openapi.util.TextRange
import com.intellij.util.containers.ContainerUtil
import org.jetbrains.kotlin.checkers.BaseDiagnosticsTest.TestFile
import org.jetbrains.kotlin.checkers.BaseDiagnosticsTest.TestModule
import org.jetbrains.kotlin.checkers.diagnostics.ActualDiagnostic
import org.jetbrains.kotlin.checkers.diagnostics.PositionalTextDiagnostic
import org.jetbrains.kotlin.checkers.diagnostics.TextDiagnostic
import org.jetbrains.kotlin.checkers.diagnostics.factories.DebugInfoDiagnosticFactory0
import org.jetbrains.kotlin.checkers.diagnostics.factories.SyntaxErrorDiagnosticFactory
import org.jetbrains.kotlin.checkers.utils.CheckerTestUtil
import org.jetbrains.kotlin.checkers.utils.DiagnosticsRenderingConfiguration
import org.jetbrains.kotlin.cli.jvm.compiler.KotlinCoreEnvironment
import org.jetbrains.kotlin.config.*
import org.jetbrains.kotlin.descriptors.DeclarationDescriptor
import org.jetbrains.kotlin.descriptors.impl.ModuleDescriptorImpl
import org.jetbrains.kotlin.diagnostics.*
import org.jetbrains.kotlin.load.java.InternalFlexibleTypeTransformer
import org.jetbrains.kotlin.name.FqName
import org.jetbrains.kotlin.platform.CommonPlatforms
import org.jetbrains.kotlin.platform.TargetPlatform
import org.jetbrains.kotlin.platform.js.JsPlatforms
import org.jetbrains.kotlin.platform.jvm.JvmPlatforms
import org.jetbrains.kotlin.platform.konan.NativePlatforms
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.resolve.BindingContext
import org.jetbrains.kotlin.resolve.calls.smartcasts.DataFlowValueFactoryImpl
import org.jetbrains.kotlin.test.Directives
import org.jetbrains.kotlin.test.InTextDirectivesUtils.isDirectiveDefined
import org.jetbrains.kotlin.test.KotlinBaseTest
import org.jetbrains.kotlin.test.util.KtTestUtil
import org.jetbrains.kotlin.utils.addIfNotNull
import org.junit.Assert
import java.io.File
import java.util.regex.Pattern
import kotlin.reflect.jvm.javaField

internal const val JVM_TARGET = "JVM_TARGET"

abstract class BaseDiagnosticsTest : KotlinMultiFileTestWithJava<TestModule, TestFile>() {
    protected lateinit var environment: KotlinCoreEnvironment

    protected val project: Project
        get() = environment.project

    override fun tearDown() {
        this::environment.javaField!![this] = null
        super.tearDown()
    }

    override fun createTestModule(
        name: String,
        dependencies: List<String>,
        friends: List<String>
    ): TestModule =
        TestModule(name, dependencies, friends)

    override fun createTestFile(module: TestModule?, fileName: String, text: String, directives: Directives): TestFile =
        TestFile(module, fileName, text, directives)

    fun doMultiFileTest(
        wholeFile: File,
        files: List<TestFile>,
        additionalClasspath: File? = null,
        usePsiClassFilesReading: Boolean = true,
        excludeNonTypeUseJetbrainsAnnotations: Boolean = false
    ) {
        environment =
            createEnvironment(wholeFile, files, additionalClasspath, usePsiClassFilesReading, excludeNonTypeUseJetbrainsAnnotations)
        //after environment initialization cause of `tearDown` logic, maybe it's obsolete
        if (shouldSkipTest(wholeFile, files)) {
            println("${wholeFile.name} test is skipped")
            return
        }
        setupEnvironment(environment)
        analyzeAndCheck(wholeFile, files)
    }

    override fun doMultiFileTest(wholeFile: File, files: List<TestFile>) {
        doMultiFileTest(wholeFile, files, null)
    }

    protected open fun shouldSkipTest(wholeFile: File, files: List<TestFile>): Boolean = false

    protected abstract fun analyzeAndCheck(testDataFile: File, files: List<TestFile>)

    protected open fun getKtFiles(testFiles: List<TestFile>, includeExtras: Boolean): List<KtFile> {
        var declareFlexibleType = false
        var declareCheckType = false
        val ktFiles = arrayListOf<KtFile>()
        for (testFile in testFiles) {
            ktFiles.addIfNotNull(testFile.ktFile)
            declareFlexibleType = declareFlexibleType or testFile.declareFlexibleType
            declareCheckType = declareCheckType or testFile.declareCheckType
        }

        if (includeExtras) {
            if (declareFlexibleType) {
                ktFiles.add(
                    KtTestUtil.createFile(
                        "EXPLICIT_FLEXIBLE_TYPES.kt",
                        EXPLICIT_FLEXIBLE_TYPES_DECLARATIONS,
                        project
                    )
                )
            }
            if (declareCheckType) {
                val checkTypeDeclarations = File("$HELPERS_PATH/types/checkType.kt").readText()

                ktFiles.add(
                    KtTestUtil.createFile(
                        "CHECK_TYPE.kt",
                        checkTypeDeclarations,
                        project
                    )
                )
            }
        }

        return ktFiles
    }

    class TestModule(name: String, dependencies: List<String>, friends: List<String>) :
        KotlinBaseTest.TestModule(name, dependencies, friends) {
        lateinit var languageVersionSettings: LanguageVersionSettings
    }

    inner class TestFile(
        val module: TestModule?,
        val fileName: String,
        textWithMarkers: String,
        directives: Directives
    ) : KotlinBaseTest.TestFile(fileName, textWithMarkers, directives) {
        val diagnosedRanges: MutableList<DiagnosedRange> = mutableListOf()
        val diagnosedRangesToDiagnosticNames: MutableMap<IntRange, MutableSet<String>> = mutableMapOf()
        val actualDiagnostics: MutableList<ActualDiagnostic> = mutableListOf()
        val expectedText: String
        val clearText: String
        private val createKtFile: Lazy<KtFile?>
        private val whatDiagnosticsToConsider: Condition<Diagnostic>
        val customLanguageVersionSettings: LanguageVersionSettings?
        val jvmTarget: JvmTarget?
        val declareCheckType: Boolean = CHECK_TYPE_DIRECTIVE in directives
        val declareFlexibleType: Boolean
        private val markDynamicCalls: Boolean
        val dynamicCallDescriptors: MutableList<DeclarationDescriptor> = mutableListOf()
        val withNewInferenceDirective: Boolean
        val newInferenceEnabled: Boolean
        val renderDiagnosticMessages: Boolean
        val renderDiagnosticsFullText: Boolean

        init {
            this.whatDiagnosticsToConsider = parseDiagnosticFilterDirective(directives, declareCheckType)
            this.customLanguageVersionSettings = parseLanguageVersionSettings(directives)
            this.jvmTarget = parseJvmTarget(directives)
            this.declareFlexibleType = EXPLICIT_FLEXIBLE_TYPES_DIRECTIVE in directives
            this.markDynamicCalls = MARK_DYNAMIC_CALLS_DIRECTIVE in directives
            this.withNewInferenceDirective = WITH_NEW_INFERENCE_DIRECTIVE in directives
            this.newInferenceEnabled =
                customLanguageVersionSettings?.supportsFeature(LanguageFeature.NewInference) ?: shouldUseNewInferenceForTests()
            if (fileName.endsWith(".java")) {
                // TODO: check there are no syntax errors in .java sources
                this.createKtFile = lazyOf(null)
                this.clearText = textWithMarkers
                this.expectedText = this.clearText
            } else {
                this.expectedText = textWithMarkers
                this.clearText =
                    CheckerTestUtil.parseDiagnosedRanges(addExtras(expectedText), diagnosedRanges, diagnosedRangesToDiagnosticNames)
                this.createKtFile = lazy { TestCheckerUtil.createCheckAndReturnPsiFile(fileName, clearText, project) }
            }
            this.renderDiagnosticMessages = RENDER_DIAGNOSTICS_MESSAGES in directives
            this.renderDiagnosticsFullText = RENDER_DIAGNOSTICS_FULL_TEXT in directives
        }

        val ktFile: KtFile? by createKtFile

        private val imports: String
            get() = buildString {
                // Line separator is "\n" intentionally here (see DocumentImpl.assertValidSeparators)
                if (declareFlexibleType) {
                    append(EXPLICIT_FLEXIBLE_TYPES_IMPORT + "\n")
                }
            }

        private val extras: String
            get() = "/*extras*/\n$imports/*extras*/\n\n"

        fun addExtras(text: String): String =
            addImports(text, extras)

        fun stripExtras(actualText: StringBuilder) {
            val extras = extras
            val start = actualText.indexOf(extras)
            if (start >= 0) {
                actualText.delete(start, start + extras.length)
            }
        }

        private fun addImports(text: String, imports: String): String {
            var result = text
            val pattern = Pattern.compile("^package [.\\w\\d]*\n", Pattern.MULTILINE)
            val matcher = pattern.matcher(result)
            result = if (matcher.find()) {
                // add imports after the package directive
                result.substring(0, matcher.end()) + imports + result.substring(matcher.end())
            } else {
                // add imports at the beginning
                imports + result
            }
            return result
        }

        private fun shouldUseNewInferenceForTests(): Boolean {
            if (System.getProperty("kotlin.ni") == "true") return true
            return LanguageVersionSettingsImpl.DEFAULT.supportsFeature(LanguageFeature.NewInference)
        }

        fun getActualText(
            bindingContext: BindingContext,
            implementingModulesBindings: List<Pair<TargetPlatform, BindingContext>>,
            actualText: StringBuilder,
            languageVersionSettings: LanguageVersionSettings,
            moduleDescriptor: ModuleDescriptorImpl
        ): Boolean {
            val ktFile = this.ktFile
            if (ktFile == null) {
                // TODO: check java files too
                actualText.append(this.clearText)
                return true
            }

            if (ktFile.name.endsWith("CoroutineUtil.kt") && ktFile.packageFqName == FqName("helpers")) return true

            val ok = booleanArrayOf(true)
            val withNewInference = newInferenceEnabled && withNewInferenceDirective && !USE_OLD_INFERENCE_DIAGNOSTICS_FOR_NI
            val diagnostics = CheckerTestUtil.getDiagnosticsIncludingSyntaxErrors(
                bindingContext,
                implementingModulesBindings,
                ktFile,
                markDynamicCalls,
                dynamicCallDescriptors,
                DiagnosticsRenderingConfiguration(platform = null, withNewInference, languageVersionSettings),
                DataFlowValueFactoryImpl(languageVersionSettings),
                moduleDescriptor,
                this.diagnosedRangesToDiagnosticNames
            )
            val filteredDiagnostics = ContainerUtil.filter(diagnostics) {
                whatDiagnosticsToConsider.value(it.diagnostic)
            }

            filteredDiagnostics.map { it.diagnostic }.forEach { diagnostic ->
                val diagnosticElementTextRange = diagnostic.psiElement.textRange
                diagnostic.textRanges.forEach {
                    check(diagnosticElementTextRange.contains(it)) {
                        "Annotation API violation:" +
                                " diagnostic text range $it has to be in range of" +
                                " diagnostic element ${diagnostic.psiElement} '${diagnostic.psiElement.text}'" +
                                " (factory ${diagnostic.factory.name}): $diagnosticElementTextRange"
                    }
                }
            }

            actualDiagnostics.addAll(filteredDiagnostics)

            val uncheckedDiagnostics = mutableListOf<PositionalTextDiagnostic>()
            val inferenceCompatibilityOfTest = asInferenceCompatibility(withNewInference)
            val invertedInferenceCompatibilityOfTest = asInferenceCompatibility(!withNewInference)

            val diagnosticToExpectedDiagnostic =
                CheckerTestUtil.diagnosticsDiff(diagnosedRanges, filteredDiagnostics, object : DiagnosticDiffCallbacks {
                    override fun missingDiagnostic(diagnostic: TextDiagnostic, expectedStart: Int, expectedEnd: Int) {
                        if (withNewInferenceDirective && diagnostic.inferenceCompatibility != inferenceCompatibilityOfTest) {
                            updateUncheckedDiagnostics(diagnostic, expectedStart, expectedEnd)
                            return
                        }

                        val message = "Missing " + diagnostic.description + PsiDiagnosticUtils.atLocation(
                            ktFile,
                            TextRange(expectedStart, expectedEnd)
                        )
                        System.err.println(message)
                        ok[0] = false
                    }

                    override fun wrongParametersDiagnostic(
                        expectedDiagnostic: TextDiagnostic,
                        actualDiagnostic: TextDiagnostic,
                        start: Int,
                        end: Int
                    ) {
                        val message = "Parameters of diagnostic not equal at position " +
                                PsiDiagnosticUtils.atLocation(ktFile, TextRange(start, end)) +
                                ". Expected: ${expectedDiagnostic.asString()}, actual: $actualDiagnostic"
                        System.err.println(message)
                        ok[0] = false
                    }

                    override fun unexpectedDiagnostic(diagnostic: TextDiagnostic, actualStart: Int, actualEnd: Int) {
                        if (withNewInferenceDirective && diagnostic.inferenceCompatibility != inferenceCompatibilityOfTest) {
                            updateUncheckedDiagnostics(diagnostic, actualStart, actualEnd)
                            return
                        }

                        val message = "Unexpected ${diagnostic.description}${
                            PsiDiagnosticUtils.atLocation(
                                ktFile,
                                TextRange(actualStart, actualEnd)
                            )
                        }"
                        System.err.println(message)
                        ok[0] = false
                    }

                    fun updateUncheckedDiagnostics(diagnostic: TextDiagnostic, start: Int, end: Int) {
                        diagnostic.enhanceInferenceCompatibility(invertedInferenceCompatibilityOfTest)
                        uncheckedDiagnostics.add(PositionalTextDiagnostic(diagnostic, start, end))
                    }
                })

            actualText.append(
                CheckerTestUtil.addDiagnosticMarkersToText(
                    ktFile,
                    filteredDiagnostics,
                    diagnosticToExpectedDiagnostic,
                    { file -> file.text },
                    uncheckedDiagnostics,
                    withNewInferenceDirective,
                    renderDiagnosticMessages
                )
            )

            stripExtras(actualText)

            return ok[0]
        }

        private fun asInferenceCompatibility(isNewInference: Boolean): TextDiagnostic.InferenceCompatibility {
            return if (isNewInference)
                TextDiagnostic.InferenceCompatibility.NEW
            else
                TextDiagnostic.InferenceCompatibility.OLD
        }

        override fun toString(): String = ktFile?.name ?: "Java file"
    }

    companion object {
        private const val HELPERS_PATH = "./compiler/testData/diagnostics/helpers"
        val DIAGNOSTICS_DIRECTIVE = "DIAGNOSTICS"
        val DIAGNOSTICS_PATTERN: Pattern = Pattern.compile("([+\\-!])(\\w+)\\s*")
        val DIAGNOSTICS_TO_INCLUDE_ANYWAY: Set<DiagnosticFactory<*>> = setOf(
            Errors.UNRESOLVED_REFERENCE,
            Errors.UNRESOLVED_REFERENCE_WRONG_RECEIVER,
            SyntaxErrorDiagnosticFactory.INSTANCE,
            DebugInfoDiagnosticFactory0.ELEMENT_WITH_ERROR_TYPE,
            DebugInfoDiagnosticFactory0.MISSING_UNRESOLVED,
            DebugInfoDiagnosticFactory0.UNRESOLVED_WITH_TARGET
        )

        val DEFAULT_DIAGNOSTIC_TESTS_FEATURES = mapOf(
            LanguageFeature.Coroutines to LanguageFeature.State.ENABLED
        )

        val CHECK_TYPE_DIRECTIVE = "CHECK_TYPE"

        val EXPLICIT_FLEXIBLE_TYPES_DIRECTIVE = "EXPLICIT_FLEXIBLE_TYPES"
        val EXPLICIT_FLEXIBLE_PACKAGE = InternalFlexibleTypeTransformer.FLEXIBLE_TYPE_CLASSIFIER.packageFqName.asString()
        val EXPLICIT_FLEXIBLE_CLASS_NAME = InternalFlexibleTypeTransformer.FLEXIBLE_TYPE_CLASSIFIER.relativeClassName.asString()
        private val EXPLICIT_FLEXIBLE_TYPES_DECLARATIONS = "\npackage " + EXPLICIT_FLEXIBLE_PACKAGE +
                "\npublic class " + EXPLICIT_FLEXIBLE_CLASS_NAME + "<L, U>"
        private val EXPLICIT_FLEXIBLE_TYPES_IMPORT = "import $EXPLICIT_FLEXIBLE_PACKAGE.$EXPLICIT_FLEXIBLE_CLASS_NAME"

        val MARK_DYNAMIC_CALLS_DIRECTIVE = "MARK_DYNAMIC_CALLS"

        val WITH_NEW_INFERENCE_DIRECTIVE = "WITH_NEW_INFERENCE"

        // Change it to "true" to load diagnostics for old inference to test new inference (ignore diagnostics with <NI; prefix)
        val USE_OLD_INFERENCE_DIAGNOSTICS_FOR_NI = false

        val RENDER_DIAGNOSTICS_MESSAGES = "RENDER_DIAGNOSTICS_MESSAGES"

        val RENDER_DIAGNOSTICS_FULL_TEXT = "RENDER_DIAGNOSTICS_FULL_TEXT"

        val DIAGNOSTIC_IN_TESTDATA_PATTERN = Regex("<!>|<!(.*?(\\(\".*?\"\\)|\\(\\))??)+(?<!<)!>")
        val SPEC_LINKED_TESTDATA_PATTERN =
            Regex("""\/\*\s+? \* KOTLIN (PSI|DIAGNOSTICS|CODEGEN BOX) SPEC TEST \((POSITIVE|NEGATIVE)\)\n([\s\S]*?\n)\s+\*\/\n""")

        val SPEC_NOT_LINED_TESTDATA_PATTERN =
            Regex("""\/\*\s+? \* KOTLIN (PSI|DIAGNOSTICS|CODEGEN BOX) NOT LINKED SPEC TEST \((POSITIVE|NEGATIVE)\)\n([\s\S]*?\n)\s+\*\/\n""")


        fun parseDiagnosticFilterDirective(
            directiveMap: Directives,
            allowUnderscoreUsage: Boolean
        ): Condition<Diagnostic> {
            val directives = directiveMap[DIAGNOSTICS_DIRECTIVE]
            val initialCondition =
                if (allowUnderscoreUsage)
                    Condition<Diagnostic> { it.factory.name != "UNDERSCORE_USAGE_WITHOUT_BACKTICKS" }
                else
                    Conditions.alwaysTrue()

            if (directives == null) {
                // If "!API_VERSION" is present, disable the NEWER_VERSION_IN_SINCE_KOTLIN diagnostic.
                // Otherwise it would be reported in any non-trivial test on the @SinceKotlin value.
                if (API_VERSION_DIRECTIVE in directiveMap) {
                    return Conditions.and(initialCondition, Condition { diagnostic ->
                        diagnostic.factory !== Errors.NEWER_VERSION_IN_SINCE_KOTLIN
                    })
                }
                return initialCondition
            }

            var condition = initialCondition
            val matcher = DIAGNOSTICS_PATTERN.matcher(directives)
            if (!matcher.find()) {
                Assert.fail(
                    "Wrong syntax in the '// $DIAGNOSTICS_DIRECTIVE: ...' directive:\n" +
                            "found: '$directives'\n" +
                            "Must be '([+-!]DIAGNOSTIC_FACTORY_NAME|ERROR|WARNING|INFO)+'\n" +
                            "where '+' means 'include'\n" +
                            "      '-' means 'exclude'\n" +
                            "      '!' means 'exclude everything but this'\n" +
                            "directives are applied in the order of appearance, i.e. !FOO +BAR means include only FOO and BAR"
                )
            }

            var first = true
            do {
                val operation = matcher.group(1)
                val name = matcher.group(2)

                val newCondition: Condition<Diagnostic> =
                    if (name in setOf("ERROR", "WARNING", "INFO")) {
                        Condition { diagnostic -> diagnostic.severity == Severity.valueOf(name) }
                    } else {
                        Condition { diagnostic -> name == diagnostic.factory.name }
                    }

                when (operation) {
                    "!" -> {
                        if (!first) {
                            Assert.fail(
                                "'$operation$name' appears in a position rather than the first one, " +
                                        "which effectively cancels all the previous filters in this directive"
                            )
                        }
                        condition = newCondition
                    }

                    "+" -> condition = Conditions.or(condition, newCondition)
                    "-" -> condition = Conditions.and(condition, Conditions.not(newCondition))
                }
                first = false
            } while (matcher.find())

            // We always include UNRESOLVED_REFERENCE and SYNTAX_ERROR because they are too likely to indicate erroneous test data
            return Conditions.or(
                condition,
                Condition { diagnostic -> diagnostic.factory in DIAGNOSTICS_TO_INCLUDE_ANYWAY }
            )
        }

        fun isJavacSkipTest(wholeFile: File): Boolean {
            val testDataFileText = wholeFile.readText()
            if (isDirectiveDefined(testDataFileText, "// JAVAC_SKIP")) {
                return true
            }
            return false
        }

        //TODO: merge with isJavacSkipTest
        fun isSkipJavacTest(wholeFile: File): Boolean {
            val testDataFileText = wholeFile.readText()
            if (isDirectiveDefined(testDataFileText, "// SKIP_JAVAC")) {
                return true
            }
            return false
        }
    }

    private fun parseJvmTarget(directiveMap: Directives) = directiveMap[JVM_TARGET]?.let { JvmTarget.fromString(it) }

    protected fun parseModulePlatformByName(moduleName: String): TargetPlatform? {
        val nameSuffix = moduleName.substringAfterLast("-", "").uppercase()
        return when {
            nameSuffix == "COMMON" -> CommonPlatforms.defaultCommonPlatform
            nameSuffix == "JVM" -> JvmPlatforms.unspecifiedJvmPlatform // TODO(dsavvinov): determine JvmTarget precisely
            nameSuffix == "JS" -> JsPlatforms.defaultJsPlatform
            nameSuffix == "NATIVE" -> NativePlatforms.unspecifiedNativePlatform
            nameSuffix.isEmpty() -> null // TODO(dsavvinov): this leads to 'null'-platform in ModuleDescriptor
            else -> throw IllegalStateException("Can't determine platform by name $nameSuffix")
        }
    }

}
