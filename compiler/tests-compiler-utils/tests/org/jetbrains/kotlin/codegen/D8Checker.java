/*
 * Copyright 2010-2020 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.codegen;

import com.android.tools.r8.*;
import com.android.tools.r8.origin.PathOrigin;
import com.android.tools.r8.utils.ExceptionDiagnostic;
import kotlin.ExceptionsKt;
import kotlin.Pair;
import org.jetbrains.kotlin.backend.common.output.OutputFile;
import org.jetbrains.kotlin.test.KtAssert;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

public class D8Checker {

    public static final boolean RUN_D8_CHECKER = !Boolean.valueOf(System.getProperty("kotlin.test.box.d8.disable"));

    private D8Checker() {
    }

    public static void check(ClassFileFactory outputFiles) {
        if (!RUN_D8_CHECKER) return;
        runD8(builder -> addOutputFiles(builder, outputFiles));
    }

    private static void addOutputFiles(D8Command.Builder builder, ClassFileFactory outputFiles) {
        for (OutputFile file : ClassFileUtilsKt.getClassFiles(outputFiles)) {
            byte[] bytes = file.asByteArray();
            builder.addClassProgramData(bytes, new PathOrigin(Paths.get(file.getRelativePath())));
        }
    }

    public static void checkFilesWithD8(Collection<Pair<byte[], String>> classFiles) {
        if (!RUN_D8_CHECKER) return;
        runD8(builder -> classFiles.forEach(
                pair -> builder.addClassProgramData(pair.getFirst(), new PathOrigin(Paths.get(pair.getSecond())))
        ));
    }

    // Compilation with D8 should proceed with no output. There should be no info, warnings, or errors.
    static class TestDiagnosticsHandler implements DiagnosticsHandler {
        private static String renderDiagnostic(Diagnostic diagnostic) {
            StringBuilder sb = new StringBuilder();
            sb.append(diagnostic.getDiagnosticMessage());
            Throwable cause = (diagnostic instanceof ExceptionDiagnostic) ? ((ExceptionDiagnostic) diagnostic).getCause() : null;
            if (cause != null) {
                sb.append("\n");
                sb.append(ExceptionsKt.stackTraceToString(cause));
            }
            return sb.toString();
        }

        @Override
        public void error(Diagnostic diagnostic) {
            KtAssert.fail("D8 dexing error: " + renderDiagnostic(diagnostic));
        }

        @Override
        public void warning(Diagnostic diagnostic) {
            KtAssert.fail("D8 dexing warning: " + renderDiagnostic(diagnostic));
        }

        @Override
        public void info(Diagnostic diagnostic) {
            KtAssert.fail("D8 dexing info: " + renderDiagnostic(diagnostic));
        }
    }

    private static void runD8(Consumer<D8Command.Builder> addInput) {
        ProgramConsumer ignoreOutputConsumer = new DexIndexedConsumer.ForwardingConsumer(null);
        D8Command.Builder builder = D8Command.builder(new TestDiagnosticsHandler())
                .setMinApiLevel(30)
                .setMode(CompilationMode.DEBUG)
                .setProgramConsumer(ignoreOutputConsumer);
        addInput.accept(builder);
        try {
            D8.run(builder.build(), Executors.newSingleThreadExecutor());
        }
        catch (CompilationFailedException e) {
            KtAssert.fail(generateExceptionMessage(e));
        }
    }

    private static String generateExceptionMessage(Throwable e) {
        StringWriter writer = new StringWriter();
        try (PrintWriter printWriter = new PrintWriter(writer)) {
            e.printStackTrace(printWriter);
            return writer.toString();
        }
    }
}
