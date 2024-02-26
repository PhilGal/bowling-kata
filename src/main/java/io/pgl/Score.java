package io.pgl;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.TreeMap;
import java.util.TreeSet;

public class Score {

  private Points latestPoint = Points.pending();
  private final TreeMap<Integer, Frame> frames = new TreeMap<>(Comparator.naturalOrder());
  private final TreeSet<Integer> pendingFramesIndices = new TreeSet<>(Comparator.naturalOrder());

  public void update(Frame frame) {
    final var frameIndex = frame.getFrameNumber() - 1;
    frames.put(frameIndex, frame);
    final var isPending = frame.isPending();
    if (isPending) {
      frame.setScore(Points.pending());
      pendingFramesIndices.add(frameIndex);
    }
    updatePendingFrames(frame);
    if (!isPending && frame.hasPendingScore()) {
      latestPoint = Points.of(latestPoint.getRawValue() + frame.totalPinsHit());
      frame.setScore(latestPoint);
    }
    System.out.println(frames.values().stream().map(Frame::getScorePoints).toList());
  }

  private void updatePendingFrames(Frame frame) {
    if (frame.getFrameNumber() == 1) {
      return;
    }
    for (Integer pendingFramesIndex : pendingFramesIndices) {
      Frame pendingFrame = frames.get(pendingFramesIndex);
      List<Roll> lastTwoRolls = frames.values().stream()
                                      .filter(f -> f.getFrameNumber() > pendingFrame.getFrameNumber())
                                      .flatMap(f -> f.getRolls().stream())
                                      .limit(2)
                                      .toList();
      if (!pendingFrame.hasPendingScore()) {
        continue;
      }
      if (pendingFrame.isSpare() && lastTwoRolls.size() == 1) {
        updatePendingScore(pendingFramesIndex, lastTwoRolls.get(0).pinsHit(), pendingFrame);
      } else if (pendingFrame.isStrike() && lastTwoRolls.size() == 2) {
        updatePendingScore(pendingFramesIndex, lastTwoRolls.get(0).pinsHit() + lastTwoRolls.get(1).pinsHit(), pendingFrame);
      }
    }
    pendingFramesIndices.removeIf(idx -> frames.get(idx).getScorePoints().isPresent());
  }

  private void updatePendingScore(Integer pendingFramesIndex, int totalPinsHit, Frame pendingFrame) {
    final var score = getPrevValue(pendingFramesIndex);
    final var valuePoint = Points.of(score + totalPinsHit);
    pendingFrame.setScore(valuePoint);
    latestPoint = valuePoint;
  }

  private int getPrevValue(Integer pendingFramesIndex) {
    int prevValue;
    if (pendingFramesIndex >= 1) {
      prevValue = frames.get(pendingFramesIndex - 1).getScorePoints().orElseThrow();
    } else {
      prevValue = 0;
    }
    return pendingFramesIndex == 9 ? prevValue : prevValue + 10;
  }

  public Points latest() {
    return latestPoint;
  }

  @Override
  public String toString() {
    return "Score{" +
        "points =" + latestPoint.value() +
        ", pointsPerFrame=" + frames.values().stream().map(Frame::getScorePoints).toList() +
        '}';
  }

  public void prettyPrint() {
    System.out.println("Bowling Frames Table:");
    System.out.println("Frame\t\tRoll 1\tRoll 2");
    System.out.println("-----------------------------");
    for (Frame frame : frames.values()) {
      final var rolls = frame.getRolls();
      System.out.printf("%d\t\t%s\t%s%n", frame.getFrameNumber(), rolls.size() > 1 ? rolls.get(0).pinsHit() : " ", rolls.size() > 2 ? rolls.get(1).pinsHit() : " ");
    }
  }

  public static class Points implements Comparable<Points> {

    private static final Points PENDING = new Points(null);

    private final Integer value;

    private Points(Integer value) {
      this.value = value;
    }

    public static Points pending() {
      return PENDING;
    }

    public static Points of(Integer value) {
      return new Points(value);
    }

    public Optional<Integer> value() {
      return Optional.ofNullable(value);
    }

    private Integer getRawValue() {
      return value;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      Points points = (Points) o;
      return Objects.equals(value, points.value);
    }

    @Override
    public int hashCode() {
      return Objects.hash(value);
    }

    @Override
    public int compareTo(Points o) {
      return Comparator.nullsFirst(Comparator.comparingInt(Points::getRawValue))
                       .compare(this, o);
    }
  }
}
