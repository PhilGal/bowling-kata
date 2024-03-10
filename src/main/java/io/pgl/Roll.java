package io.pgl;

record Roll(int pinsHit) {

  public static final Roll STRIKE = new Roll(Game.MAX_PINS);

  public Roll {
    System.out.println(pinsHit + " pins hit!");
    if (pinsHit < 0 || pinsHit > Game.MAX_PINS) {
      throw new IllegalArgumentException("invalid number of pins: " + pinsHit);
    }
  }

  @Override
  public String toString() {
    return String.valueOf(pinsHit);
  }
}
