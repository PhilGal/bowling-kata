package io.pgl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class Frame implements Comparable<Frame> {

  private final int frameNumber;
  private final List<Roll> rolls = new ArrayList<>(3);
  private int pinsLeft = Game.MAX_PINS;
  private boolean strike;
  private boolean spare;

  private Score.Points score = Score.Points.pending();

  public Frame(int frameNumber) {
    if (frameNumber > Game.MAX_FRAMES) {
      throw new IllegalArgumentException("Too many frames");
    }
    this.frameNumber = frameNumber;
  }

  void addRoll(Roll ballRoll) {
    if (!isLastFrame() && countRolls() == 2 || isLastFrame() && (isStrike() || isSpare()) && countRolls() == 3) {
      throw new IllegalStateException("Too many throws");
    }
    if (!isLastFrame() && totalPinsHit() + ballRoll.pinsHit() > Game.MAX_PINS) {
      throw new IllegalArgumentException("Too many pins hit @_@");
    }

    this.rolls.add(ballRoll);
    this.pinsLeft -= ballRoll.pinsHit();
    // all pins hit
    if (!isOpen()) {
      this.strike = countRolls() == 1;
      this.spare = countRolls() == 2;
    }
  }

  public boolean hasMoreRolls() {
    if (!isLastFrame()) {
      return !(isStrike() || isSpare()) && isOpen() && countRolls() < 2;
    } else {
      return (isOpen() && countRolls() < 2) || (isSpare() || isStrike()) && countRolls() < 3;
    }
  }

  public boolean isSpare() {
    return spare;
  }

  public boolean isStrike() {
    return strike;
  }

  public int totalPinsHit() {
    int result = 0;
    for (Roll roll : rolls) {
      result += roll.pinsHit();
    }
    return result;
  }

  public int getFrameNumber() {
    return frameNumber;
  }

  public List<Roll> getRolls() {
    return rolls;
  }

  public int countRolls() {
    return rolls.size();
  }

  public Optional<Integer> getScorePoints() {
    return score.value();
  }

  public boolean hasPendingScore() {
    return getScorePoints().isEmpty();
  }

  public boolean isPending() {
    if (frameNumber != Game.MAX_FRAMES) {
      return isSpare() || isStrike() || hasMoreRolls();
    } else {
      return hasMoreRolls();
    }
  }

  public void setScore(Score.Points score) {
    this.score = score;
  }

  private boolean isOpen() {
    return pinsLeft != 0;
  }

  private boolean isLastFrame() {
    return frameNumber == Game.MAX_FRAMES;
  }

  @Override
  public String toString() {
    return "Frame{" +
        "frameNumber=" + frameNumber +
        ", madeThrows=" + rolls +
        '}';
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Frame frame = (Frame) o;
    return frameNumber == frame.frameNumber;
  }

  @Override
  public int hashCode() {
    return Objects.hash(frameNumber);
  }

  @Override
  public int compareTo(Frame o) {
    return Comparator.comparingInt(Frame::getFrameNumber).compare(this, o);
  }
}
