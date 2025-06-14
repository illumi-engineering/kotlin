// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// FILE: Test.java
public class Test {
    interface MyRunnable extends Runnable {}

    public static void foo(MyRunnable r) {}
    public static void foo(Runnable r) {}
}

// FILE: 1.kt
fun main(args: Array<String>) {
    Test.foo {  }
}

/* GENERATED_FIR_TAGS: functionDeclaration, javaFunction, javaType, lambdaLiteral, samConversion */
