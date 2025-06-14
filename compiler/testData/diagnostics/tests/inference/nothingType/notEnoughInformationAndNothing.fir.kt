// RUN_PIPELINE_TILL: FRONTEND
// DIAGNOSTICS: -UNUSED_VARIABLE -UNUSED_PARAMETER
// CHECK_TYPE
// COMPARE_WITH_LIGHT_TREE

class Inv<I>
fun <T> materialize(): Inv<T> = TODO()
fun <K> id(arg: K) = arg
fun <S> select(vararg args: S): S = TODO()

fun test1(b: Boolean?) {
    val v = when(b) {
        true -> materialize()
        false -> null
        null -> materialize<String>()
    }
    v checkType { _<Inv<String>?>() }
}

fun test2() {
    <!CANNOT_INFER_PARAMETER_TYPE!>select<!>(
        <!CANNOT_INFER_PARAMETER_TYPE!>materialize<!>()
    )
    select(materialize(), materialize<String>())
    select(materialize(), null, Inv<String>())
    select(
        <!CANNOT_INFER_PARAMETER_TYPE!>materialize<!>(),
        null
    )
    <!CANNOT_INFER_PARAMETER_TYPE!>select<!>(
        <!CANNOT_INFER_PARAMETER_TYPE!>materialize<!>(),
        <!CANNOT_INFER_PARAMETER_TYPE!>materialize<!>()
    )
    select(
        <!CANNOT_INFER_PARAMETER_TYPE!>materialize<!>(),
        <!CANNOT_INFER_PARAMETER_TYPE!>materialize<!>(),
        null
    )
}

/* GENERATED_FIR_TAGS: classDeclaration, equalityExpression, funWithExtensionReceiver, functionDeclaration,
functionalType, infix, lambdaLiteral, localProperty, nullableType, propertyDeclaration, smartcast, starProjection,
typeParameter, typeWithExtension, vararg, whenExpression, whenWithSubject */
