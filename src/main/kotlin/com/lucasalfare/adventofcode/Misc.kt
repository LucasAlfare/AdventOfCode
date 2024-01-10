package com.lucasalfare.adventofcode

/**
 * A utility class for loading resources from the classpath.
 */
object ResourceLoader {
  private val classLoader = {}.javaClass.classLoader

  /**
   * Loads the content of a resource file as a string.
   *
   * @param pathname The path to the resource file.
   * @return The content of the resource file as a string.
   * @throws IllegalArgumentException if the specified resource is not found.
   */
  fun loadToString(pathname: String): String {
    // Retrieve the input stream for the specified resource.
    val inputStream = classLoader.getResourceAsStream(pathname)

    // Ensure that the input stream is not null.
    requireNotNull(inputStream) { "Resource not found: $pathname" }

    // Read the content of the resource as bytes and convert it to a string.
    return String(inputStream.readAllBytes())
  }
}