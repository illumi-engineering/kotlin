// RUN_PIPELINE_TILL: BACKEND
// ISSUE: KT-38288

open class A<K : Any>

class G : A<G.Key<*>>() {
    class Key<T : Any> {}
}

/* GENERATED_FIR_TAGS: classDeclaration, nestedClass, starProjection, typeConstraint, typeParameter */
