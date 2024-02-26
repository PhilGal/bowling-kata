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
    assertEquals(Score.FramePoint.pending(), player.bowl(Throw.ALL_PINS_HIT));
    // Frame 2 - 9
    player.bowl(new Throw(9));
    assertEquals(Score.FramePoint.pending(), player.getPoints());
    player.bowl(new Throw(0));
    assertEquals(Score.FramePoint.value(28), player.getPoints());
    // Frame 1 -> 19, Frame 2 -> 19 + 9 = 28
  }

  @Test
  void pointsAfterSpare() {
    // Frame 1 -> /
    player.bowl(new Throw(5));
    player.bowl(new Throw(5));
    assertEquals(Score.FramePoint.pending(), player.getPoints());

    // Frame 2
    player.bowl(new Throw(1));
    assertEquals(Score.FramePoint.value(11), player.getPoints());
    player.bowl(new Throw(4));
    assertEquals(Score.FramePoint.value(16), player.getPoints());

    player.bowl(new Throw(6));
    player.bowl(new Throw(4));
    player.bowl(new Throw(1));
    assertEquals(Score.FramePoint.value(27), player.getPoints());
    player.bowl(new Throw(1));
    assertEquals(Score.FramePoint.value(29), player.getPoints());
  }

  @Test
  void totalScore() {
    //Frame 1
    player.bowl(new Throw(10));
    //Frame 2
    player.bowl(new Throw(7));
    player.bowl(new Throw(3));
    //Frame 3
    player.bowl(new Throw(9));
    player.bowl(new Throw(0));
    //Frame 4
    player.bowl(new Throw(10));
    //Frame 5
    player.bowl(new Throw(8));
    player.bowl(new Throw(2));
    //Frame 6
    player.bowl(new Throw(0));
    player.bowl(new Throw(0));
    //Frame 7
    player.bowl(new Throw(10));
    //Frame 8
    player.bowl(new Throw(8));
    player.bowl(new Throw(0));
    //Frame 9
    player.bowl(new Throw(7));
    player.bowl(new Throw(3));
    //Frame 10
    player.bowl(new Throw(10));
    player.bowl(new Throw(9));
    player.bowl(new Throw(1));

    assertEquals("144", player.getPoints().toString());
  }

  @Test
  void allStrikes() {
    for (int i = 0; i < 12; i++) {
      player.bowl(new Throw(10));
    }
    assertEquals(Score.FramePoint.value(300), player.getPoints());
  }
}
