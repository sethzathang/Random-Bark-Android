package com.sz.randombark

import com.sz.randombark.common.utils.extractAndFormatDogBreed
import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    @Test
    fun testExtractAndFormatDogBreedWithValidData() {
        val input = "www.dog.api/breeds/german-shepherd/"
        val expected = "German Shepherd"
        val actual = input.extractAndFormatDogBreed()
        assertEquals(expected, actual)
    }

    @Test
    fun testExtractAndFormatDogBreedWithInvalidData() {
        val input = "www.dog.api/noBreedTest"
        val expected = "Breed Name Unavailable"
        val actual = input.extractAndFormatDogBreed()
        assertEquals(expected, actual)
    }
}