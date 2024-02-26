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
    frame.addRoll(Roll.ALL_PINS_HIT);
    assertTrue(frame.isStrike());
    assertFalse(frame.isSpare());
    assertFalse(frame.hasMoreRolls());
    System.out.println(frame);
  }

  @Test
  void spare() {
    frame.addRoll(new Roll(1));
    frame.addRoll(new Roll(9));
    assertFalse(frame.isStrike());
    assertTrue(frame.isSpare());
    assertFalse(frame.hasMoreRolls());
    System.out.println(frame);
  }

  @Test
  void cantDoMoreThrows() {
    final var oneHitBallThrow = new Roll(1);
    frame.addRoll(oneHitBallThrow);
    assertTrue(frame.hasMoreRolls());
    frame.addRoll(oneHitBallThrow);
    assertFalse(frame.hasMoreRolls());
    assertThrows(IllegalStateException.class, () -> frame.addRoll(oneHitBallThrow));
  }

  @Test
  void cantHitMorePins() {
    frame.addRoll(new Roll(1));
    assertTrue(frame.hasMoreRolls());
    final var allPinsHitThrow = Roll.ALL_PINS_HIT;
    assertThrows(IllegalArgumentException.class, () -> frame.addRoll(allPinsHitThrow));
  }
}
