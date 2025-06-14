// RUN_PIPELINE_TILL: BACKEND
// FIR_IDENTICAL

@RequiresOptIn
annotation class Boom

@RequiresOptIn
annotation class Boom2

@SubclassOptInRequired(Boom::class)
open class B {
    @SubclassOptInRequired(Boom2::class)
    open inner class C
}


fun test() {
    with(B()) {
        @OptIn(Boom2::class)
        class Local : B.C() {}
    }
}

/* GENERATED_FIR_TAGS: annotationDeclaration, classDeclaration, classReference, functionDeclaration, inner,
lambdaLiteral, localClass */
