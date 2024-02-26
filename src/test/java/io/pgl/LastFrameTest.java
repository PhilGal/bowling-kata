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
    assertTrue(lastFrame.hasMoreThrows());
  }

  @Test
  void lastFrameNormal() {
    lastFrame.addThrow(new Throw(0));
    lastFrame.addThrow(new Throw(3));
    assertFalse(lastFrame.hasMoreThrows());
  }

  @Test
  void lastFrameStrike_Has2MoreThrows() {
    lastFrame.addThrow(Throw.ALL_PINS_HIT);
    assertTrue(lastFrame.isStrike());
    //two more throws in the last frames after the Strike
    assertTrue(lastFrame.hasMoreThrows());
    lastFrame.addThrow(new Throw(3));
    lastFrame.addThrow(new Throw(5));
    assertNoMoreThrows();
  }

  @Test
  void lastFrameSpare_Has1MoreThrow() {
    final var halfPinsHitThrow = new Throw(Game.MAX_PINS / 2);
    lastFrame.addThrow(halfPinsHitThrow);
    lastFrame.addThrow(halfPinsHitThrow);
    assertFalse(lastFrame.isStrike());
    assertTrue(lastFrame.isSpare());
    //one more throw in the last frame after the Spare
    assertTrue(lastFrame.hasMoreThrows());
    lastFrame.addThrow(halfPinsHitThrow);
    assertNoMoreThrows();
  }

  private void assertNoMoreThrows() {
    assertFalse(lastFrame.hasMoreThrows());
    final var illegalBallThrow = new Throw(1);
    assertThrows(IllegalStateException.class, () -> lastFrame.addThrow(illegalBallThrow));
  }
}
