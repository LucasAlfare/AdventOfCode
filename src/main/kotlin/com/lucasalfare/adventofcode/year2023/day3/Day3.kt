package com.lucasalfare.adventofcode.year2023.day3

import com.lucasalfare.adventofcode.ResourceLoader

/**
 * https://adventofcode.com/2023/day/3
 */
fun main() {
  val myInput = ResourceLoader.loadToString("2023/day3")

  val part1Result = gearRatiosPart1(myInput)
  println("Result of part 1: [$part1Result]")

  // TODO: solve part 2
}

/**
 * We turn the input string into a matrix.
 *
 * Doing this we can iterate on it using coordinate values (x and y).
 *
 *
 */
fun gearRatiosPart1(input: String): Int {
  // "dimensions" of the input string is fixed [140x140]
  val inputWidth = 140 + 1 // <-- here we sum 1 to make possible recognize extra "dot" below
  val inputHeight = 140

  /*
  we map (transform) each line of the input in itself
  with an extra "dot" at the end.

  Also, this is a matrix with inverted coordinate access
  system: the first index leads to the line (vertical)
  and the second to the actual line character (horizontal).
   */
  val inputAsMatrix = input.lines().map { "$it." }

  var numericBuffer = "" // <-- used to store the parsing numbers
  var shouldStoreBuffer = false // <-- indicates if we should save the parsed number
  val validNumbers = mutableListOf<Int>() // <-- data source to store the valid numbers

  /*
  nested loops to iterate over the created 2D matrix.

  We need first iterate over the "y" dimension due to this
  value increase only after the "x", making the coordinates
  run in the direction: TopLeft -> BottomRight
   */
  for (y in 0..<inputHeight) {
    for (x in 0..<inputWidth) {

      /*
      the actual character of the coordinate.
      we access it with inverted coordinates,
      due our matrix form.
       */
      val currentChar = inputAsMatrix[y][x]
      if (currentChar.isDigit()) { // <-- first checks if is a digit
        numericBuffer += currentChar // <-- if is a digit, always buffer it

        /*
        we need to loop over the "neighbors" of the current coordinate.
        this can be done by using two nested loops, each one going from
        "-1" to "1".
        this will generate factors to sum up to the actual coordinate and
        leads to a neighbor.

        also, we only loop the neighs if we didn't it before.
        this can be checked through the [shouldStoreBuffer] variable.
         */
        if (!shouldStoreBuffer) {
          for (yy in -1..1) {
            for (xx in -1..1) {

              /*
              we ignore the case "x+0, y+0"
              this is the current coordinate,
              instead of a neighbor of itself
               */
              if (xx != 0 || yy != 0) {
                val neighCoordX = x + xx
                val neighCoordY = y + yy

                /*
                just checks if the actual neighbor coordinate is inside
                the bounds of our matrix.

                this is checked using a custom helper function below.
                 */
                if (inBounds(
                    neighCoordX, neighCoordY,
                    inputWidth - 1, inputHeight - 1
                  )
                ) {
                  val neigh = inputAsMatrix[neighCoordY][neighCoordX]

                  /*
                  checks if the neighbor is a character
                  that we are searching.

                  if so, we flag that the current numeric
                  buffer should be stored later.
                   */
                  if (neigh in "@#$*+-=/%&") {
                    shouldStoreBuffer = true
                  }
                }
              }
            }
          }
        }
      } else { // <-- here we are not facing a number anymore
        if (shouldStoreBuffer) { // <-- checks if this was previously set to [true]
          validNumbers += numericBuffer.toInt() // <-- stores the actual buffer
        }

        /*
        we always clear the buffers and flags
        when we are not facing a number anymore
         */
        numericBuffer = "" // <-- clears the buffer
        shouldStoreBuffer = false // <-- resets buffer storing flag
      }
    }
  }

  return validNumbers.sum() // <-- just sum up all numbers inside the valid numbers
}

/**
 * This function is used to check if the passed coordinate is inside
 * the bounds of the passed [width] and [height].
 */
fun inBounds(x: Int, y: Int, width: Int, height: Int) =
  (x in 0..width) && (y in 0..height)