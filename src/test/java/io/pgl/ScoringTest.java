package io.pgl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ScoringTest {

  private Player player;

  @BeforeEach
  void setUp() {
    this.player = new Player("pgl");
  }

  @Test
  void totalScore() {
    System.gc();
    Runtime runtime = Runtime.getRuntime();
    final var usedMemoryBefore = runtime.totalMemory() - runtime.freeMemory();
    //Frame 1
    player.bowl(Roll.STRIKE)
        //Frame 2
        .bowl(new Roll(7))
        .bowl(new Roll(3))
        //Frame 3
        .bowl(new Roll(9))
        .bowl(new Roll(0))
        //Frame 4
        .bowl(Roll.STRIKE)
        //Frame 5
        .bowl(new Roll(8))
        .bowl(new Roll(2))
        //Frame 6
        .bowl(new Roll(0))
        .bowl(new Roll(0))
        //Frame 7
        .bowl(Roll.STRIKE)
        //Frame 8
        .bowl(new Roll(8))
        .bowl(new Roll(0))
        //Frame 9
        .bowl(new Roll(7))
        .bowl(new Roll(3))
        //Frame 10
        .bowl(Roll.STRIKE)
        .bowl(new Roll(9))
        .bowl(new Roll(1));

    assertEquals(Score.Points.of(144), player.getPoints());
    final var usedMemoryAfter = runtime.totalMemory() - runtime.freeMemory();
    System.out.println("Memory increased: " + (usedMemoryAfter - usedMemoryBefore));
  }

  @Test
  void allStrikes() {
    while (player.hasMoreRolls()) {
      player.bowl(Roll.STRIKE);
    }
    assertEquals(Score.Points.of(300), player.getPoints());
  }
}
