package io.pgl;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class Frame implements Comparable<Frame> {

  private final int frameNumber;
  private final LinkedList<Throw> madeThrows = new LinkedList<>();

  private int pinsLeft = Game.MAX_PINS;
  private boolean strike;
  private boolean spare;

  private Score.FramePoint score = Score.FramePoint.pending();

  public Frame(int frameNumber) {
    if (frameNumber > Game.MAX_FRAMES) {
      throw new IllegalArgumentException("Too many frames");
    }
    this.frameNumber = frameNumber;
  }

  void addThrow(Throw ballThrow) {
    if (!isLastFrame() && countThrows() == 2 || isLastFrame() && (isStrike() || isSpare()) && countThrows() == 3) {
      throw new IllegalStateException("Too many throws");
    }
    if (!isLastFrame() && totalPinsHit() + ballThrow.pinsHit() > Game.MAX_PINS) {
      throw new IllegalArgumentException("Too many pins hit @_@");
    }

    this.madeThrows.add(ballThrow);
    this.pinsLeft -= ballThrow.pinsHit();
    // all pins hit
    if (!isOpen()) {
      this.strike = countThrows() == 1;
      this.spare = countThrows() == 2;
    }
  }

  public boolean hasMoreThrows() {
    if (!isLastFrame()) {
      return !(isStrike() || isSpare()) && isOpen() && countThrows() < 2;
    } else {
      return (isOpen() && countThrows() < 2) || (isSpare() || isStrike()) && countThrows() < 3;
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
    for (Throw madeThrow : madeThrows) {
      result += madeThrow.pinsHit();
    }
    return result;
  }

  public int getFrameNumber() {
    return frameNumber;
  }

  public List<Throw> getThrows() {
    return madeThrows;
  }

  public int countThrows() {
    return madeThrows.size();
  }

  public Score.FramePoint getScore() {
    return score;
  }

  public boolean hasPendingScore() {
    return score instanceof Score.PendingPoint;
  }

  public boolean isPending() {
    if ( frameNumber != Game.MAX_FRAMES) {
      return isSpare() || isStrike() || hasMoreThrows();
    } else {
      return hasMoreThrows();
    }
  }

  public void setScore(Score.FramePoint score) {
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
        ", madeThrows=" + madeThrows +
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
