// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL
// FILE: Derived.java
import kotlin.jvm.functions.Function0;
import org.jetbrains.annotations.NotNull;

public class Derived implements Base {
    @Override
    public <V> void foo(@NotNull Function0<? extends V> compute) {}
}

// FILE: main.kt
interface Base {
    fun <V: Any> foo(compute: () -> V?)
}

fun main() {
    Derived().foo<String> { "" }
}

/* GENERATED_FIR_TAGS: flexibleType, functionDeclaration, functionalType, interfaceDeclaration, javaFunction, javaType,
lambdaLiteral, nullableType, stringLiteral, typeConstraint, typeParameter */
