// RUN_PIPELINE_TILL: BACKEND
// FULL_JDK

fun foo(x: Comparator<in CharSequence>) {}


fun main() {
    foo { x, y ->
        x.length - y.length
    }
}

/* GENERATED_FIR_TAGS: additiveExpression, flexibleType, functionDeclaration, inProjection, lambdaLiteral, samConversion */
