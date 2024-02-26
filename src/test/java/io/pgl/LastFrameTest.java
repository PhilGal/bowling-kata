package io.pgl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class LastFrameTest {

  private Frame lastFrame;

  @BeforeEach
  void setUp() {
    lastFrame = new Frame(Game.MAX_FRAMES);
    assertTrue(lastFrame.hasMoreRolls());
  }

  @Test
  void lastFrameNormal() {
    lastFrame.addRoll(new Roll(0));
    lastFrame.addRoll(new Roll(3));
    assertFalse(lastFrame.hasMoreRolls());
  }

  @Test
  void lastFrameStrike_Has2MoreThrows() {
    lastFrame.addRoll(Roll.ALL_PINS_HIT);
    assertTrue(lastFrame.isStrike());
    //two more throws in the last frames after the Strike
    assertTrue(lastFrame.hasMoreRolls());
    lastFrame.addRoll(new Roll(3));
    lastFrame.addRoll(new Roll(5));
    assertNoMoreThrows();
  }

  @Test
  void lastFrameSpare_Has1MoreThrow() {
    final var halfPinsHitRoll = new Roll(Game.MAX_PINS / 2);
    lastFrame.addRoll(halfPinsHitRoll);
    lastFrame.addRoll(halfPinsHitRoll);
    assertFalse(lastFrame.isStrike());
    assertTrue(lastFrame.isSpare());
    //one more throw in the last frame after the Spare
    assertTrue(lastFrame.hasMoreRolls());
    lastFrame.addRoll(halfPinsHitRoll);
    assertNoMoreThrows();
  }

  private void assertNoMoreThrows() {
    assertFalse(lastFrame.hasMoreRolls());
    final var illegalBallRoll = new Roll(1);
    assertThrows(IllegalStateException.class, () -> lastFrame.addRoll(illegalBallRoll));
  }
}
