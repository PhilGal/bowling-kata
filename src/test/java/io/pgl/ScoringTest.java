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
  void pointsAfterStrike() {
    // Frame 1 -> X
    assertEquals(Score.Points.pending(), player.bowl(Roll.ALL_PINS_HIT));
    // Frame 2 - 9
    player.bowl(new Roll(9));
    assertEquals(Score.Points.pending(), player.getPoints());
    player.bowl(new Roll(0));
    assertEquals(Score.Points.of(28), player.getPoints());
    // Frame 1 -> 19, Frame 2 -> 19 + 9 = 28
  }

  @Test
  void pointsAfterSpare() {
    // Frame 1 -> /
    player.bowl(new Roll(5));
    player.bowl(new Roll(5));
    assertEquals(Score.Points.pending(), player.getPoints());

    // Frame 2
    player.bowl(new Roll(1));
    assertEquals(Score.Points.of(11), player.getPoints());
    player.bowl(new Roll(4));
    assertEquals(Score.Points.of(16), player.getPoints());

    player.bowl(new Roll(6));
    player.bowl(new Roll(4));
    player.bowl(new Roll(1));
    assertEquals(Score.Points.of(27), player.getPoints());
    player.bowl(new Roll(1));
    assertEquals(Score.Points.of(29), player.getPoints());
  }

  @Test
  void totalScore() {
    //Frame 1
    player.bowl(new Roll(10));
    //Frame 2
    player.bowl(new Roll(7));
    player.bowl(new Roll(3));
    //Frame 3
    player.bowl(new Roll(9));
    player.bowl(new Roll(0));
    //Frame 4
    player.bowl(new Roll(10));
    //Frame 5
    player.bowl(new Roll(8));
    player.bowl(new Roll(2));
    //Frame 6
    player.bowl(new Roll(0));
    player.bowl(new Roll(0));
    //Frame 7
    player.bowl(new Roll(10));
    //Frame 8
    player.bowl(new Roll(8));
    player.bowl(new Roll(0));
    //Frame 9
    player.bowl(new Roll(7));
    player.bowl(new Roll(3));
    //Frame 10
    player.bowl(new Roll(10));
    player.bowl(new Roll(9));
    player.bowl(new Roll(1));

    assertEquals(Score.Points.of(144), player.getPoints());

  }

  @Test
  void allStrikes() {
    for (int i = 0; i < 12; i++) {
      player.bowl(new Roll(10));
    }
    assertEquals(Score.Points.of(300), player.getPoints());
  }
}
