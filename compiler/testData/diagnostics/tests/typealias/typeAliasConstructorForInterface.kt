// RUN_PIPELINE_TILL: FRONTEND
interface IFoo

typealias Test = IFoo

val testAsFunction = <!RESOLUTION_TO_CLASSIFIER!>Test<!>()
val testAsValue = <!NO_COMPANION_OBJECT!>Test<!>

/* GENERATED_FIR_TAGS: interfaceDeclaration, propertyDeclaration, typeAliasDeclaration */
