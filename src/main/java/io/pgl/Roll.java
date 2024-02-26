package io.pgl;

public record Roll(int pinsHit) {

  public static final Roll ALL_PINS_HIT = new Roll(Game.MAX_PINS);

  public Roll {
    if (pinsHit < 0 || pinsHit > Game.MAX_PINS) {
      throw new IllegalArgumentException("invalid number of pins: " + pinsHit);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(pinsHit);
  }
}
