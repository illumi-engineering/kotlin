plugins {
    kotlin("jvm")
    id("jps-compatible")
}

project.configureJvmToolchain(JdkMajorVersion.JDK_1_6)

dependencies {
    api(project(":core:compiler.common"))
    api(project(":core:util.runtime"))
    api(kotlinStdlib())
    api(project(":kotlin-annotations-jvm"))
    kotlinCompilerClasspath(project(":libraries:tools:stdlib-compiler-classpath"))
}

sourceSets {
    "main" { projectDefault() }
    "test" {}
}
