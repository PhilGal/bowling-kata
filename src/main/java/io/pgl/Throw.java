package io.pgl;

public record Throw(int pinsHit) {

  public static final Throw ALL_PINS_HIT = new Throw(Game.MAX_PINS);

  public Throw {
    if (pinsHit < 0 || pinsHit > Game.MAX_PINS) {
      throw new IllegalArgumentException("invalid number of pins: " + pinsHit);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(pinsHit);
  }
}
