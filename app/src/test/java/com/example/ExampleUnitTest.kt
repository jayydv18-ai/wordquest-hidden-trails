package com.example

import com.example.data.LevelRepository
import com.example.game.WordSearchGenerator
import com.example.model.GridPos
import org.junit.Assert.*
import org.junit.Test

class ExampleUnitTest {
  @Test
  fun testLevelRepositoryHas50HandcraftedLevelsAndChapters() {
    assertEquals(50, LevelRepository.levels.size)
    assertEquals(10, LevelRepository.chapters.size)
    
    // Check Level 21
    val level21 = LevelRepository.getLevelById(21)
    assertEquals(21, level21.id)
    assertEquals("BEACH", level21.theme)
    assertEquals(5, level21.chapterId)
    assertEquals(9, level21.words.size)

    // Check Level 50
    val level50 = LevelRepository.getLevelById(50)
    assertEquals(50, level50.id)
    assertEquals("THE FINAL DESTINATION", level50.theme)
    assertEquals(10, level50.chapterId)
    assertEquals(500, level50.rewardCoins)
    assertEquals(5, level50.rewardHints)

    // Check Chapters
    val ch5 = LevelRepository.getChapterForLevel(21)
    assertEquals("Sunset Coast", ch5.subtitle)
    assertEquals(5, ch5.id)

    val ch10 = LevelRepository.getChapterForLevel(50)
    assertEquals("Ancient Kingdom", ch10.subtitle)
    assertEquals(10, ch10.id)
  }

  @Test
  fun testInfiniteLevelGeneration() {
    val level51 = LevelRepository.getLevelById(51)
    assertEquals(51, level51.id)
    assertEquals(100, level51.rewardCoins)
    assertTrue(level51.words.isNotEmpty())

    val level100 = LevelRepository.getLevelById(100)
    assertEquals(100, level100.id)
    assertEquals(100, level100.rewardCoins)
    assertTrue(level100.words.isNotEmpty())

    // Chapter for level 100
    val chapter = LevelRepository.getChapterForLevel(100)
    assertEquals(20, chapter.id) // (100 - 1) / 5 + 1 = 20
    assertEquals(96, chapter.startLevel)
    assertEquals(100, chapter.endLevel)

    // Chunk generation
    val chunk0 = LevelRepository.getLevelsForChunk(0)
    assertEquals(5, chunk0.size)
    assertEquals(1, chunk0.first().id)
    assertEquals(5, chunk0.last().id)

    val chunk10 = LevelRepository.getLevelsForChunk(10)
    assertEquals(5, chunk10.size)
    assertEquals(51, chunk10.first().id)
    assertEquals(55, chunk10.last().id)
  }

  @Test
  fun testWordSearchGenerator() {
    val words = listOf("SUN", "SEA", "SAND")
    val puzzle = WordSearchGenerator.generate(words, 8)
    assertEquals(8, puzzle.gridSize)
    assertEquals(8, puzzle.grid.size)
    assertEquals(3, puzzle.wordPlacements.size)

    // Check line calculation
    val line = WordSearchGenerator.computeLineBetween(GridPos(0, 0), GridPos(0, 2))
    assertNotNull(line)
    assertEquals(3, line?.size)
  }
}
