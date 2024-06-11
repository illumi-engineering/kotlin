/*
 * Copyright 2010-2024 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.powerassert;

import com.intellij.testFramework.TestDataPath;
import org.jetbrains.kotlin.test.util.KtTestUtil;
import org.jetbrains.kotlin.test.TargetBackend;
import org.jetbrains.kotlin.test.TestMetadata;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.regex.Pattern;

/** This class is generated by {@link org.jetbrains.kotlin.generators.tests.GenerateTestsKt}. DO NOT MODIFY MANUALLY */
@SuppressWarnings("all")
@TestMetadata("plugins/power-assert/testData/codegen")
@TestDataPath("$PROJECT_ROOT")
public class FirLightTreeBlackBoxCodegenTestForPowerAssertGenerated extends AbstractFirLightTreeBlackBoxCodegenTestForPowerAssert {
  @Test
  public void testAllFilesPresentInCodegen() {
    KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
  }

  @Test
  @TestMetadata("CheckCustomMessage.kt")
  public void testCheckCustomMessage() {
    runTest("plugins/power-assert/testData/codegen/CheckCustomMessage.kt");
  }

  @Test
  @TestMetadata("Constants.kt")
  public void testConstants() {
    runTest("plugins/power-assert/testData/codegen/Constants.kt");
  }

  @Test
  @TestMetadata("CustomLocalVariableMessage.kt")
  public void testCustomLocalVariableMessage() {
    runTest("plugins/power-assert/testData/codegen/CustomLocalVariableMessage.kt");
  }

  @Test
  @TestMetadata("CustomMessage.kt")
  public void testCustomMessage() {
    runTest("plugins/power-assert/testData/codegen/CustomMessage.kt");
  }

  @Test
  @TestMetadata("MemberFunctions.kt")
  public void testMemberFunctions() {
    runTest("plugins/power-assert/testData/codegen/MemberFunctions.kt");
  }

  @Test
  @TestMetadata("Multiline.kt")
  public void testMultiline() {
    runTest("plugins/power-assert/testData/codegen/Multiline.kt");
  }

  @Test
  @TestMetadata("RequireCustomMessage.kt")
  public void testRequireCustomMessage() {
    runTest("plugins/power-assert/testData/codegen/RequireCustomMessage.kt");
  }

  @Test
  @TestMetadata("Transformations.kt")
  public void testTransformations() {
    runTest("plugins/power-assert/testData/codegen/Transformations.kt");
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/arithmetic")
  @TestDataPath("$PROJECT_ROOT")
  public class Arithmetic {
    @Test
    public void testAllFilesPresentInArithmetic() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/arithmetic"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("InlineAddition.kt")
    public void testInlineAddition() {
      runTest("plugins/power-assert/testData/codegen/arithmetic/InlineAddition.kt");
    }

    @Test
    @TestMetadata("InlineDivision.kt")
    public void testInlineDivision() {
      runTest("plugins/power-assert/testData/codegen/arithmetic/InlineDivision.kt");
    }

    @Test
    @TestMetadata("InlineMultiplication.kt")
    public void testInlineMultiplication() {
      runTest("plugins/power-assert/testData/codegen/arithmetic/InlineMultiplication.kt");
    }

    @Test
    @TestMetadata("InlinePostfixDecrement.kt")
    public void testInlinePostfixDecrement() {
      runTest("plugins/power-assert/testData/codegen/arithmetic/InlinePostfixDecrement.kt");
    }

    @Test
    @TestMetadata("InlinePostfixIncrement.kt")
    public void testInlinePostfixIncrement() {
      runTest("plugins/power-assert/testData/codegen/arithmetic/InlinePostfixIncrement.kt");
    }

    @Test
    @TestMetadata("InlinePrefixDecrement.kt")
    public void testInlinePrefixDecrement() {
      runTest("plugins/power-assert/testData/codegen/arithmetic/InlinePrefixDecrement.kt");
    }

    @Test
    @TestMetadata("InlinePrefixIncrement.kt")
    public void testInlinePrefixIncrement() {
      runTest("plugins/power-assert/testData/codegen/arithmetic/InlinePrefixIncrement.kt");
    }

    @Test
    @TestMetadata("InlineSubtraction.kt")
    public void testInlineSubtraction() {
      runTest("plugins/power-assert/testData/codegen/arithmetic/InlineSubtraction.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/boolean")
  @TestDataPath("$PROJECT_ROOT")
  public class Boolean {
    @Test
    public void testAllFilesPresentInBoolean() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/boolean"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("BooleanAnd.kt")
    public void testBooleanAnd() {
      runTest("plugins/power-assert/testData/codegen/boolean/BooleanAnd.kt");
    }

    @Test
    @TestMetadata("BooleanMixWithAndFirst.kt")
    public void testBooleanMixWithAndFirst() {
      runTest("plugins/power-assert/testData/codegen/boolean/BooleanMixWithAndFirst.kt");
    }

    @Test
    @TestMetadata("BooleanMixWithAndLast.kt")
    public void testBooleanMixWithAndLast() {
      runTest("plugins/power-assert/testData/codegen/boolean/BooleanMixWithAndLast.kt");
    }

    @Test
    @TestMetadata("BooleanMixWithOrFirst.kt")
    public void testBooleanMixWithOrFirst() {
      runTest("plugins/power-assert/testData/codegen/boolean/BooleanMixWithOrFirst.kt");
    }

    @Test
    @TestMetadata("BooleanMixWithOrLast.kt")
    public void testBooleanMixWithOrLast() {
      runTest("plugins/power-assert/testData/codegen/boolean/BooleanMixWithOrLast.kt");
    }

    @Test
    @TestMetadata("BooleanOr.kt")
    public void testBooleanOr() {
      runTest("plugins/power-assert/testData/codegen/boolean/BooleanOr.kt");
    }

    @Test
    @TestMetadata("BooleanShortCircuit.kt")
    public void testBooleanShortCircuit() {
      runTest("plugins/power-assert/testData/codegen/boolean/BooleanShortCircuit.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/cast")
  @TestDataPath("$PROJECT_ROOT")
  public class Cast {
    @Test
    public void testAllFilesPresentInCast() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/cast"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("InstanceEquals.kt")
    public void testInstanceEquals() {
      runTest("plugins/power-assert/testData/codegen/cast/InstanceEquals.kt");
    }

    @Test
    @TestMetadata("InstanceNotEquals.kt")
    public void testInstanceNotEquals() {
      runTest("plugins/power-assert/testData/codegen/cast/InstanceNotEquals.kt");
    }

    @Test
    @TestMetadata("SmartCast.kt")
    public void testSmartCast() {
      runTest("plugins/power-assert/testData/codegen/cast/SmartCast.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/dbg")
  @TestDataPath("$PROJECT_ROOT")
  public class Dbg {
    @Test
    public void testAllFilesPresentInDbg() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/dbg"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("DebugEntryFunction.kt")
    public void testDebugEntryFunction() {
      runTest("plugins/power-assert/testData/codegen/dbg/DebugEntryFunction.kt");
    }

    @Test
    @TestMetadata("DebugEntryFunctionComplexBooleans.kt")
    public void testDebugEntryFunctionComplexBooleans() {
      runTest("plugins/power-assert/testData/codegen/dbg/DebugEntryFunctionComplexBooleans.kt");
    }

    @Test
    @TestMetadata("DebugEntryFunctionMessage.kt")
    public void testDebugEntryFunctionMessage() {
      runTest("plugins/power-assert/testData/codegen/dbg/DebugEntryFunctionMessage.kt");
    }

    @Test
    @TestMetadata("DebugEntryFunctionMessageComplexBooleans.kt")
    public void testDebugEntryFunctionMessageComplexBooleans() {
      runTest("plugins/power-assert/testData/codegen/dbg/DebugEntryFunctionMessageComplexBooleans.kt");
    }

    @Test
    @TestMetadata("DebugFunction.kt")
    public void testDebugFunction() {
      runTest("plugins/power-assert/testData/codegen/dbg/DebugFunction.kt");
    }

    @Test
    @TestMetadata("DebugFunctionMessage.kt")
    public void testDebugFunctionMessage() {
      runTest("plugins/power-assert/testData/codegen/dbg/DebugFunctionMessage.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/expressions")
  @TestDataPath("$PROJECT_ROOT")
  public class Expressions {
    @Test
    public void testAllFilesPresentInExpressions() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/expressions"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("ElvisOperator.kt")
    public void testElvisOperator() {
      runTest("plugins/power-assert/testData/codegen/expressions/ElvisOperator.kt");
    }

    @Test
    @TestMetadata("IfExpression.kt")
    public void testIfExpression() {
      runTest("plugins/power-assert/testData/codegen/expressions/IfExpression.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/infix")
  @TestDataPath("$PROJECT_ROOT")
  public class Infix {
    @Test
    public void testAllFilesPresentInInfix() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/infix"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("DispatchInfix.kt")
    public void testDispatchInfix() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchInfix.kt");
    }

    @Test
    @TestMetadata("DispatchInfixComplexReceiver.kt")
    public void testDispatchInfixComplexReceiver() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchInfixComplexReceiver.kt");
    }

    @Test
    @TestMetadata("DispatchInfixConstantParameter.kt")
    public void testDispatchInfixConstantParameter() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchInfixConstantParameter.kt");
    }

    @Test
    @TestMetadata("DispatchInfixConstantReceiver.kt")
    public void testDispatchInfixConstantReceiver() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchInfixConstantReceiver.kt");
    }

    @Test
    @TestMetadata("DispatchInfixOnlyConstants.kt")
    public void testDispatchInfixOnlyConstants() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchInfixOnlyConstants.kt");
    }

    @Test
    @TestMetadata("DispatchNonInfix.kt")
    public void testDispatchNonInfix() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchNonInfix.kt");
    }

    @Test
    @TestMetadata("DispatchNonInfixComplexReceiver.kt")
    public void testDispatchNonInfixComplexReceiver() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchNonInfixComplexReceiver.kt");
    }

    @Test
    @TestMetadata("DispatchNonInfixConstantParameter.kt")
    public void testDispatchNonInfixConstantParameter() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchNonInfixConstantParameter.kt");
    }

    @Test
    @TestMetadata("DispatchNonInfixConstantReceiver.kt")
    public void testDispatchNonInfixConstantReceiver() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchNonInfixConstantReceiver.kt");
    }

    @Test
    @TestMetadata("DispatchNonInfixOnlyConstants.kt")
    public void testDispatchNonInfixOnlyConstants() {
      runTest("plugins/power-assert/testData/codegen/infix/DispatchNonInfixOnlyConstants.kt");
    }

    @Test
    @TestMetadata("ExtensionInfix.kt")
    public void testExtensionInfix() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionInfix.kt");
    }

    @Test
    @TestMetadata("ExtensionInfixComplexReceiver.kt")
    public void testExtensionInfixComplexReceiver() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionInfixComplexReceiver.kt");
    }

    @Test
    @TestMetadata("ExtensionInfixConstantParameter.kt")
    public void testExtensionInfixConstantParameter() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionInfixConstantParameter.kt");
    }

    @Test
    @TestMetadata("ExtensionInfixConstantReceiver.kt")
    public void testExtensionInfixConstantReceiver() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionInfixConstantReceiver.kt");
    }

    @Test
    @TestMetadata("ExtensionInfixOnlyConstants.kt")
    public void testExtensionInfixOnlyConstants() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionInfixOnlyConstants.kt");
    }

    @Test
    @TestMetadata("ExtensionNonInfix.kt")
    public void testExtensionNonInfix() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionNonInfix.kt");
    }

    @Test
    @TestMetadata("ExtensionNonInfixComplexReceiver.kt")
    public void testExtensionNonInfixComplexReceiver() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionNonInfixComplexReceiver.kt");
    }

    @Test
    @TestMetadata("ExtensionNonInfixConstantParameter.kt")
    public void testExtensionNonInfixConstantParameter() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionNonInfixConstantParameter.kt");
    }

    @Test
    @TestMetadata("ExtensionNonInfixConstantReceiver.kt")
    public void testExtensionNonInfixConstantReceiver() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionNonInfixConstantReceiver.kt");
    }

    @Test
    @TestMetadata("ExtensionNonInfixOnlyConstants.kt")
    public void testExtensionNonInfixOnlyConstants() {
      runTest("plugins/power-assert/testData/codegen/infix/ExtensionNonInfixOnlyConstants.kt");
    }

    @Test
    @TestMetadata("RegexFunction.kt")
    public void testRegexFunction() {
      runTest("plugins/power-assert/testData/codegen/infix/RegexFunction.kt");
    }

    @Test
    @TestMetadata("RegexInfix.kt")
    public void testRegexInfix() {
      runTest("plugins/power-assert/testData/codegen/infix/RegexInfix.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/junit5")
  @TestDataPath("$PROJECT_ROOT")
  public class Junit5 {
    @Test
    public void testAllFilesPresentInJunit5() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/junit5"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("JunitAssertTrue.kt")
    public void testJunitAssertTrue() {
      runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrue.kt");
    }

    @Test
    @TestMetadata("JunitAssertTrueMessage.kt")
    public void testJunitAssertTrueMessage() {
      runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrueMessage.kt");
    }

    @Test
    @TestMetadata("JunitAssertTrueMessageSupplier.kt")
    public void testJunitAssertTrueMessageSupplier() {
      runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrueMessageSupplier.kt");
    }

    @Test
    @TestMetadata("JunitAssertTrueMessageSupplierVariable.kt")
    public void testJunitAssertTrueMessageSupplierVariable() {
      runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrueMessageSupplierVariable.kt");
    }

    @Test
    @TestMetadata("JunitAssertTrueMessageVariable.kt")
    public void testJunitAssertTrueMessageVariable() {
      runTest("plugins/power-assert/testData/codegen/junit5/JunitAssertTrueMessageVariable.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/kotlin-test")
  @TestDataPath("$PROJECT_ROOT")
  public class Kotlin_test {
    @Test
    public void testAllFilesPresentInKotlin_test() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/kotlin-test"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("AssertEquals.kt")
    public void testAssertEquals() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertEquals.kt");
    }

    @Test
    @TestMetadata("AssertEqualsMessage.kt")
    public void testAssertEqualsMessage() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertEqualsMessage.kt");
    }

    @Test
    @TestMetadata("AssertEqualsMessageVariable.kt")
    public void testAssertEqualsMessageVariable() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertEqualsMessageVariable.kt");
    }

    @Test
    @TestMetadata("AssertFalse.kt")
    public void testAssertFalse() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertFalse.kt");
    }

    @Test
    @TestMetadata("AssertFalseMessage.kt")
    public void testAssertFalseMessage() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertFalseMessage.kt");
    }

    @Test
    @TestMetadata("AssertFalseMessageVariable.kt")
    public void testAssertFalseMessageVariable() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertFalseMessageVariable.kt");
    }

    @Test
    @TestMetadata("AssertNotNull.kt")
    public void testAssertNotNull() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertNotNull.kt");
    }

    @Test
    @TestMetadata("AssertNotNullMessage.kt")
    public void testAssertNotNullMessage() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertNotNullMessage.kt");
    }

    @Test
    @TestMetadata("AssertNotNullMessageVariable.kt")
    public void testAssertNotNullMessageVariable() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertNotNullMessageVariable.kt");
    }

    @Test
    @TestMetadata("AssertTrue.kt")
    public void testAssertTrue() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertTrue.kt");
    }

    @Test
    @TestMetadata("AssertTrueMessage.kt")
    public void testAssertTrueMessage() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertTrueMessage.kt");
    }

    @Test
    @TestMetadata("AssertTrueMessageVariable.kt")
    public void testAssertTrueMessageVariable() {
      runTest("plugins/power-assert/testData/codegen/kotlin-test/AssertTrueMessageVariable.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/lambda")
  @TestDataPath("$PROJECT_ROOT")
  public class Lambda {
    @Test
    public void testAllFilesPresentInLambda() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/lambda"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("AnonymousObject.kt")
    public void testAnonymousObject() {
      runTest("plugins/power-assert/testData/codegen/lambda/AnonymousObject.kt");
    }

    @Test
    @TestMetadata("ListOperationsAssert.kt")
    public void testListOperationsAssert() {
      runTest("plugins/power-assert/testData/codegen/lambda/ListOperationsAssert.kt");
    }

    @Test
    @TestMetadata("ListOperationsRequire.kt")
    public void testListOperationsRequire() {
      runTest("plugins/power-assert/testData/codegen/lambda/ListOperationsRequire.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/nullsafe")
  @TestDataPath("$PROJECT_ROOT")
  public class Nullsafe {
    @Test
    public void testAllFilesPresentInNullsafe() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/nullsafe"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("ConditionalAccess.kt")
    public void testConditionalAccess() {
      runTest("plugins/power-assert/testData/codegen/nullsafe/ConditionalAccess.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/operator")
  @TestDataPath("$PROJECT_ROOT")
  public class Operator {
    @Test
    public void testAllFilesPresentInOperator() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/operator"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("ContainsFunction.kt")
    public void testContainsFunction() {
      runTest("plugins/power-assert/testData/codegen/operator/ContainsFunction.kt");
    }

    @Test
    @TestMetadata("ContainsOperator.kt")
    public void testContainsOperator() {
      runTest("plugins/power-assert/testData/codegen/operator/ContainsOperator.kt");
    }

    @Test
    @TestMetadata("ExcleqOperator.kt")
    public void testExcleqOperator() {
      runTest("plugins/power-assert/testData/codegen/operator/ExcleqOperator.kt");
    }

    @Test
    @TestMetadata("ExcleqeqOperator.kt")
    public void testExcleqeqOperator() {
      runTest("plugins/power-assert/testData/codegen/operator/ExcleqeqOperator.kt");
    }

    @Test
    @TestMetadata("NegativeContainsFunction.kt")
    public void testNegativeContainsFunction() {
      runTest("plugins/power-assert/testData/codegen/operator/NegativeContainsFunction.kt");
    }

    @Test
    @TestMetadata("NegativeContainsOperator.kt")
    public void testNegativeContainsOperator() {
      runTest("plugins/power-assert/testData/codegen/operator/NegativeContainsOperator.kt");
    }
  }

  @Nested
  @TestMetadata("plugins/power-assert/testData/codegen/parameters")
  @TestDataPath("$PROJECT_ROOT")
  public class Parameters {
    @Test
    public void testAllFilesPresentInParameters() {
      KtTestUtil.assertAllTestsPresentByMetadataWithExcluded(this.getClass(), new File("plugins/power-assert/testData/codegen/parameters"), Pattern.compile("^(.+)\\.kt$"), Pattern.compile("^(.+)\\.fir\\.kts?$"), TargetBackend.JVM_IR, true);
    }

    @Test
    @TestMetadata("VarargParameter.kt")
    public void testVarargParameter() {
      runTest("plugins/power-assert/testData/codegen/parameters/VarargParameter.kt");
    }
  }
}
