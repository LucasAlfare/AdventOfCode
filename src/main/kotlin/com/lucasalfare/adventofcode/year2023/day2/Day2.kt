package com.lucasalfare.adventofcode.year2023.day2

import com.lucasalfare.adventofcode.ResourceLoader

/**
 * https://adventofcode.com/2023/day/2
 */
fun main() {
  val cubeConundrumInput = ResourceLoader.loadToString("2023/day2")
  println("ResultOfPart1={${cubeConundrumPart1(input = cubeConundrumInput, maxRed = 12, maxGreen = 13, maxBlue = 14)}}")
  println("ResultOfPart2={${cubeConundrumPart2(input = cubeConundrumInput)}}")
}

fun cubeConundrumPart1(input: String, maxRed: Int, maxGreen: Int, maxBlue: Int): Int {
  val finalSum = inputToElfGames(input) // <-- parses the input to a known data structure
    .filter { (it.isPossible(maxRed, maxGreen, maxBlue)) } // <-- filter only possible games
    .sumOf { it.id } // <-- get the sum of all values called "id" from the filtered games

  return finalSum
}

fun cubeConundrumPart2(input: String): Int {
  val elfGames = inputToElfGames(input) // <-- parses the input to a known data structure
  var sum = 0
  elfGames.forEach { // <-- iterates over all the games
    val currentPower = it.power() // <-- retrieves the current game "power" multiplier factor
    if (currentPower != 0) // <-- if the power is not ZERO, then sums it to the final sum
      sum += currentPower
  }

  return sum
}

/**
 * Data class used to represent a game entry.
 *
 * In the raw input string, a game looks like:
 *
 * "Game 11: 6 blue, 3 green, 8 red; 6 blue, 4 green; 1 red, 3 green, 4 blue"
 */
data class ElfGame(var id: Int = -1, val rounds: MutableList<Round> = mutableListOf()) {

  /**
   * One game is possible ONLY when ALL of its rounds INDIVIDUALLY are possible.
   *
   * If one of its rounds is not possible, then the entire game is not possible.
   */
  fun isPossible(maxRed: Int = 0, maxGreen: Int = 0, maxBlue: Int = 0) =
    rounds.find { !it.isPossible(maxRed, maxGreen, maxBlue) } == null

  fun power() =
    rounds.maxOf { it.nRed } * rounds.maxOf { it.nGreen } * rounds.maxOf { it.nBLue }
}

/**
 * A data class used to represent a single round/set entry of an elf game.
 *
 * In the raw input string, a round looks like:
 *
 * "1 green, 10 blue, 13 red"
 */
data class Round(var nRed: Int = 0, var nGreen: Int = 0, var nBLue: Int = 0) {

  /**
   * A round is possible ONLY if its amounts of colored cubes are less or equals then the checking values.
   */
  fun isPossible(maxRed: Int, maxGreen: Int, maxBlue: Int) = nRed <= maxRed && nGreen <= maxGreen && nBLue <= maxBlue
}

fun inputToElfGames(input: String): List<ElfGame> {
  val elfGames = mutableListOf<ElfGame>() // <-- all the parsed games will be stored in this collection

  input
    .lines() // <-- iterates over the lines of the input
    .forEach { line ->
      val tmpElfGame = ElfGame()
      var tmpRound = Round()

      var defaultBuffer = ""
      var numericBuffer = ""

      "$line;" // <-- appends a semicolon in the end of all the lines, in order to parse the last round/set
        .forEach { c ->
          if (c != ' ' && c != ',') { // <-- ignores blank spaces and commas
            if (c.isDigit()) {
              /*
              when numeric buffer is updated, we don't clear the default buffer
              because we will need to know its value (in this point, a "color" name).
               */
              numericBuffer += c
            } else if (c == ':') { // <-- colon means we reached the end of parsing of an ID
              tmpElfGame.id = numericBuffer.toInt()
              numericBuffer = ""
              defaultBuffer = ""
            } else if (c == ';') { // <-- semicolon marks the end of a round, just stores it
              tmpElfGame.rounds += Round(tmpRound.nRed, tmpRound.nGreen, tmpRound.nBLue)
              tmpRound = Round()
              defaultBuffer = ""
              numericBuffer = ""
            } else { // <-- here we are parsing "words"
              defaultBuffer += c

              when (defaultBuffer) {
                "red" -> {
                  tmpRound.nRed = numericBuffer.toInt()
                  numericBuffer = ""
                  defaultBuffer = ""
                }

                "green" -> {
                  tmpRound.nGreen = numericBuffer.toInt()
                  numericBuffer = ""
                  defaultBuffer = ""
                }

                "blue" -> {
                  tmpRound.nBLue = numericBuffer.toInt()
                  numericBuffer = ""
                  defaultBuffer = ""
                }
              }
            }
          }
        }

      /*
      for some reason that I didn't search, the code was adding
      "empty" [ElfGame] objects. Here we are able to detect those
      by checking their ID.
       */
      if (tmpElfGame.id != -1) elfGames += tmpElfGame
    }

  return elfGames
}