package io.pgl;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FrameTest {

  private Frame frame;

  @BeforeEach
  void setUp() {
    frame = new Frame(1);
  }

  @Test
  void strike() {
    frame.addThrow(Throw.ALL_PINS_HIT);
    assertTrue(frame.isStrike());
    assertFalse(frame.isSpare());
    assertFalse(frame.hasMoreThrows());
    System.out.println(frame);
  }

  @Test
  void spare() {
    frame.addThrow(new Throw(1));
    frame.addThrow(new Throw(9));
    assertFalse(frame.isStrike());
    assertTrue(frame.isSpare());
    assertFalse(frame.hasMoreThrows());
    System.out.println(frame);
  }

  @Test
  void cantDoMoreThrows() {
    final var oneHitBallThrow = new Throw(1);
    frame.addThrow(oneHitBallThrow);
    assertTrue(frame.hasMoreThrows());
    frame.addThrow(oneHitBallThrow);
    assertFalse(frame.hasMoreThrows());
    assertThrows(IllegalStateException.class, () -> frame.addThrow(oneHitBallThrow));
  }

  @Test
  void cantHitMorePins() {
    frame.addThrow(new Throw(1));
    assertTrue(frame.hasMoreThrows());
    final var allPinsHitThrow = Throw.ALL_PINS_HIT;
    assertThrows(IllegalArgumentException.class, () -> frame.addThrow(allPinsHitThrow));
  }
}
