package com.sz.randombark.common.utils

/**
 * Find and extract the dog breed name between "breeds/" and the next "/" in the given image URL.
 * If no match is found, it returns "Breed Information Unavailable".
 * If the breed name contains a dash, it splits the name into separate words,
 * capitalizes the first letter of each word, and joins them with a space.
 *
 * @return The formatted dog breed name
 */
fun String.extractAndFormatDogBreed(): String {
    val pattern = """breeds/([^/]+)/""".toRegex()
    val result = pattern.find(input = this)?.groups?.get(1)?.value ?: "Breed Name Unavailable"
    return result.split("-").joinToString(" ") { word ->
        word.replaceFirstChar { data ->
            if (data.isLowerCase()) {
                data.titlecase()
            } else {
                data.toString()
            }
        }
    }
}