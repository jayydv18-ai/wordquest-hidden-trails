package com.example.game

import com.example.model.GridPos
import com.example.model.WordPlacement
import kotlin.random.Random

data class GeneratedPuzzle(
    val gridSize: Int,
    val grid: List<List<Char>>,
    val wordPlacements: List<WordPlacement>
)

object WordSearchGenerator {

    private val DIRECTIONS = listOf(
        Pair(0, 1),   // Horizontal Left -> Right
        Pair(0, -1),  // Horizontal Right -> Left
        Pair(1, 0),   // Vertical Top -> Bottom
        Pair(-1, 0),  // Vertical Bottom -> Top
        Pair(1, 1),   // Diagonal Down -> Right
        Pair(1, -1),  // Diagonal Down -> Left
        Pair(-1, 1),  // Diagonal Up -> Right
        Pair(-1, -1)  // Diagonal Up -> Left
    )

    // Weighted letter frequencies for natural English puzzle fillers
    private const val FILLER_POOL = "AAAAABBCCCCDDDEEEEEEEEFFGGHHHIIIIIJKLLLLMMNNNNOOOOOPPQRRRRSSSSTTTTTUUUUVWXYZ"

    fun generate(words: List<String>, requestedGridSize: Int): GeneratedPuzzle {
        val cleanWords = words.map { it.uppercase().trim() }.filter { it.isNotEmpty() }
        val maxWordLen = cleanWords.maxOfOrNull { it.length } ?: 6
        val actualGridSize = maxOf(requestedGridSize, maxWordLen)

        // Try up to 40 attempts to place all words cleanly
        for (attempt in 0 until 40) {
            val grid = Array(actualGridSize) { CharArray(actualGridSize) { ' ' } }
            val placements = mutableListOf<WordPlacement>()
            var allPlaced = true

            // Sort longer words first for easier placement
            val sortedWords = cleanWords.sortedByDescending { it.length }

            for ((colorIdx, word) in sortedWords.withIndex()) {
                val placement = tryPlaceWord(word, grid, actualGridSize, colorIdx)
                if (placement != null) {
                    placements.add(placement)
                    for (i in word.indices) {
                        val pos = placement.positions[i]
                        grid[pos.row][pos.col] = word[i]
                    }
                } else {
                    allPlaced = false
                    break
                }
            }

            if (allPlaced) {
                // Fill remaining empty cells with realistic random letters
                for (r in 0 until actualGridSize) {
                    for (c in 0 until actualGridSize) {
                        if (grid[r][c] == ' ') {
                            grid[r][c] = FILLER_POOL[Random.nextInt(FILLER_POOL.length)]
                        }
                    }
                }

                // Restore original word order
                val orderedPlacements = cleanWords.mapNotNull { target ->
                    placements.find { it.word == target }
                }

                val gridList = grid.map { it.toList() }
                return GeneratedPuzzle(
                    gridSize = actualGridSize,
                    grid = gridList,
                    wordPlacements = orderedPlacements
                )
            }
        }

        // Fallback: guaranteed placement in simple horizontal lines if complex placement times out
        val fallbackGrid = Array(actualGridSize) { CharArray(actualGridSize) { ' ' } }
        val fallbackPlacements = mutableListOf<WordPlacement>()
        var row = 0
        for ((idx, word) in cleanWords.withIndex()) {
            val r = row % actualGridSize
            val colStart = (actualGridSize - word.length).coerceAtLeast(0) / 2
            val positions = mutableListOf<GridPos>()
            for (i in word.indices) {
                val c = (colStart + i) % actualGridSize
                fallbackGrid[r][c] = word[i]
                positions.add(GridPos(r, c))
            }
            fallbackPlacements.add(WordPlacement(word, positions, idx % 8))
            row++
        }

        for (r in 0 until actualGridSize) {
            for (c in 0 until actualGridSize) {
                if (fallbackGrid[r][c] == ' ') {
                    fallbackGrid[r][c] = FILLER_POOL[Random.nextInt(FILLER_POOL.length)]
                }
            }
        }

        return GeneratedPuzzle(
            gridSize = actualGridSize,
            grid = fallbackGrid.map { it.toList() },
            wordPlacements = fallbackPlacements
        )
    }

    private fun tryPlaceWord(
        word: String,
        grid: Array<CharArray>,
        gridSize: Int,
        colorIndex: Int
    ): WordPlacement? {
        val shuffledDirections = DIRECTIONS.shuffled()
        val allPositions = mutableListOf<Pair<Int, Int>>()
        for (r in 0 until gridSize) {
            for (c in 0 until gridSize) {
                allPositions.add(Pair(r, c))
            }
        }
        allPositions.shuffle()

        for ((startR, startC) in allPositions) {
            for ((dRow, dCol) in shuffledDirections) {
                val endR = startR + dRow * (word.length - 1)
                val endCol = startC + dCol * (word.length - 1)

                if (endR in 0 until gridSize && endCol in 0 until gridSize) {
                    var canPlace = true
                    val coords = mutableListOf<GridPos>()

                    for (i in word.indices) {
                        val currR = startR + dRow * i
                        val currC = startC + dCol * i
                        val existingChar = grid[currR][currC]

                        if (existingChar != ' ' && existingChar != word[i]) {
                            canPlace = false
                            break
                        }
                        coords.add(GridPos(currR, currC))
                    }

                    if (canPlace) {
                        return WordPlacement(
                            word = word,
                            positions = coords,
                            colorIndex = colorIndex % 8
                        )
                    }
                }
            }
        }
        return null
    }

    /**
     * Compute a straight line of GridPos between two endpoints if they lie on a straight
     * horizontal, vertical, or 45-degree diagonal line.
     */
    fun computeLineBetween(start: GridPos, end: GridPos): List<GridPos>? {
        val dRow = end.row - start.row
        val dCol = end.col - start.col

        val stepRow = when {
            dRow > 0 -> 1
            dRow < 0 -> -1
            else -> 0
        }
        val stepCol = when {
            dCol > 0 -> 1
            dCol < 0 -> -1
            else -> 0
        }

        val absDRow = kotlin.math.abs(dRow)
        val absDCol = kotlin.math.abs(dCol)

        // Must be horizontal, vertical, or 45-degree diagonal
        val isValidLine = (absDRow == 0 && absDCol > 0) ||
                (absDCol == 0 && absDRow > 0) ||
                (absDRow == absDCol && absDRow > 0)

        if (!isValidLine) {
            return null
        }

        val steps = maxOf(absDRow, absDCol)
        val list = mutableListOf<GridPos>()
        for (i in 0..steps) {
            list.add(GridPos(start.row + stepRow * i, start.col + stepCol * i))
        }
        return list
    }
}
