package com.lucasalfare.adventofcode.year2023.day1

/**
 * https://adventofcode.com/2023/day/1
 */
object DayOne {

  fun partOne(input: String): Int {
    var sum = 0
    input.lines().forEach { line ->
      val lineNumbers = line.filter { it.isDigit() }
      val finalNumber = buildString { append(lineNumbers.first()); append(lineNumbers.last()) }.toInt()
      sum += finalNumber
    }

    return sum
  }

  fun partTwo(input: String): Int {
    var sum = 0
    input.lines().forEach { line ->
      val lineNumbers = lineNumbers(line)
      val finalNumber = buildString { append(lineNumbers.first()); append(lineNumbers.last()) }.toInt()
      println("$line -> $lineNumbers -> $finalNumber")
      sum += finalNumber
    }

    return sum
  }
}

fun lineNumbers(line: String): String {
  val names = listOf(
    "?", //dummy
    "2023",
    "two",
    "three",
    "four",
    "five",
    "six",
    "seven",
    "eight",
    "nine",
    "!", //dummy
    "1",
    "2",
    "3",
    "4",
    "5",
    "6",
    "7",
    "8",
    "9"
  )

  var rawNumbers = ""
  var searchingIndex = 0
  while (true) {
    val search = line.findAnyOf(strings = names, startIndex = searchingIndex)
    if (search != null) {
      val searchedDigit = names.indexOf(search.second) % 10
      rawNumbers += searchedDigit
      searchingIndex = search.first + search.second.length
      if (search.second.length > 1) searchingIndex -= 1
    } else {
      break
    }
  }

  return rawNumbers
}