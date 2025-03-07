/*
 * Copyright 2010-2019 JetBrains s.r.o. and Kotlin Programming Language contributors.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the license/LICENSE.txt file.
 */

package org.jetbrains.kotlin.kapt.base.test.incremental

import org.jetbrains.kotlin.kapt.base.incremental.RuntimeProcType
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.File

class IsolationgIncrementalProcessorTest : AbstractTestWithGeneratedSourcesDir() {
    @Test
    fun testDependenciesRecorded() {
        val srcFiles = listOf("User.java", "Address.java", "Observable.java").map { File(TEST_DATA_DIR, it) }
        val isolating = SimpleProcessor().toIsolating()
        runAnnotationProcessing(srcFiles, listOf(isolating), generatedSources)

        assertEquals(RuntimeProcType.ISOLATING, isolating.getRuntimeType())

        assertEquals(
            mapOf(
                generatedSources.resolve("test/UserGenerated.java") to "test.User",
                generatedSources.resolve("test/AddressGenerated.java") to "test.Address"
            ),
            isolating.getGeneratedToSources()
        )
    }

    @Test
    fun testNoSourcesToProcess() {
        val srcFiles = listOf("Observable.java").map { File(TEST_DATA_DIR, it) }
        val isolating = SimpleProcessor().toIsolating()
        runAnnotationProcessing(srcFiles, listOf(isolating), generatedSources)

        assertEquals(RuntimeProcType.ISOLATING, isolating.getRuntimeType())
        assertEquals(emptyMap<File, String?>(), isolating.getGeneratedToSources())
    }

    @Test
    fun testGeneratingSourcesClassesResources() {
        val srcFiles = listOf("User.java", "Address.java", "Observable.java").map { File(TEST_DATA_DIR, it) }
        val isolating = SimpleCreatingClassFilesAndResources().toIsolating()
        runAnnotationProcessing(srcFiles, listOf(isolating), generatedSources)

        assertEquals(RuntimeProcType.ISOLATING, isolating.getRuntimeType())

        assertEquals(
            mapOf(
                generatedSources.resolve("test/UserGenerated.java") to "test.User",
                generatedSources.resolve("test/UserGeneratedClass.class") to "test.User",
                generatedSources.resolve("test/UserGeneratedResource") to "test.User",
                generatedSources.resolve("test/AddressGenerated.java") to "test.Address",
                generatedSources.resolve("test/AddressGeneratedClass.class") to "test.Address",
                generatedSources.resolve("test/AddressGeneratedResource") to "test.Address"
            ),
            isolating.getGeneratedToSources()
        )
    }

    @Test
    fun testWrongOriginElement() {
        val srcFiles = listOf("User.java", "Address.java", "Observable.java").map { File(TEST_DATA_DIR, it) }
        val isolating = SimpleProcessor(wrongOrigin = true).toIsolating()
        runAnnotationProcessing(srcFiles, listOf(isolating), generatedSources)

        assertEquals(RuntimeProcType.NON_INCREMENTAL, isolating.getRuntimeType())
        assertEquals(emptyMap<File, String?>(), isolating.getGeneratedToSources())
    }

    @Test
    fun testTwoIsolating() {
        val srcFiles = listOf("User.java", "Address.java", "Observable.java").map { File(TEST_DATA_DIR, it) }
        val isolating = listOf(
            SimpleProcessor().toIsolating(),
            SimpleProcessor(generatedSuffix = "Two").toIsolating()
        )
        runAnnotationProcessing(srcFiles, isolating, generatedSources)

        isolating.forEach { assertEquals(RuntimeProcType.ISOLATING, it.getRuntimeType()) }
        assertEquals(
            mapOf(
                generatedSources.resolve("test/UserGenerated.java") to "test.User",
                generatedSources.resolve("test/AddressGenerated.java") to "test.Address"
            ), isolating[0].getGeneratedToSources()
        )

        assertEquals(
            mapOf(
                generatedSources.resolve("test/UserGeneratedTwo.java") to "test.User",
                generatedSources.resolve("test/AddressGeneratedTwo.java") to "test.Address"
            ), isolating[1].getGeneratedToSources()
        )
    }

    @Test
    fun testIsolatingWithMultipleOriginatingElements() {
        val srcFiles = listOf("User.java", "Observable.java").map { File(TEST_DATA_DIR, it) }
        val isolating = listOf(ReportTwoOriginElements().toIsolating())
        runAnnotationProcessing(srcFiles, isolating, generatedSources)

        assertEquals(
            mapOf(
                generatedSources.resolve("test/UserGenerated.java") to "test.User"
            ), isolating[0].getGeneratedToSources()
        )
    }
}
