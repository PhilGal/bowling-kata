package io.pgl;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public class Score {

  private ValuePoint lastValuePoint = FramePoint.value(0);
  private final Map<Integer, Frame> frames = new TreeMap<>(Comparator.naturalOrder());
  private final Set<Integer> pendingFramesIndices = new TreeSet<>(Comparator.naturalOrder());

  public void update(Frame frame) {
    //>>> this is needed still, but better
    final var frameNumber = frame.getFrameNumber();
    final var frameIndex = frameNumber - 1;
    frames.put(frameIndex, frame);
    final var isPending = frame.isPending();
    if (isPending) {
      frame.setScore(FramePoint.pending());
      pendingFramesIndices.add(frameIndex);
    }
    updatePendingFrames(frame);
    if (!isPending && frame.hasPendingScore()) {
      lastValuePoint = FramePoint.value(lastValuePoint.value() + frame.totalPinsHit());
      frame.setScore(lastValuePoint);
    }
    System.out.println(frames.values().stream().map(Frame::getScore).toList());
  }

  private void updatePendingFrames(Frame frame) {
    if (frame.getFrameNumber() == 1) {
      return;
    }
    for (Integer pendingFramesIndex : pendingFramesIndices) {
      Frame pendingFrame = frames.get(pendingFramesIndex);
      List<Throw> lastTwoThrows = frames.values().stream()
                                        .filter(f -> f.getFrameNumber() > pendingFrame.getFrameNumber())
                                        .flatMap(f -> f.getThrows().stream())
                                        .limit(2)
                                        .toList();
      if (!pendingFrame.hasPendingScore()) {
        continue;
      }
      if (pendingFrame.isSpare() && lastTwoThrows.size() == 1) {
        updatePendingScore(pendingFramesIndex, lastTwoThrows.get(0).pinsHit(), pendingFrame);
      } else if (pendingFrame.isStrike() && lastTwoThrows.size() == 2) {
        updatePendingScore(pendingFramesIndex, lastTwoThrows.get(0).pinsHit() + lastTwoThrows.get(1).pinsHit(), pendingFrame);
      }

    }
    pendingFramesIndices.removeIf(idx -> frames.get(idx).getScore() instanceof ValuePoint);
  }

  private void updatePendingScore(Integer pendingFramesIndex, int totalPinsHit, Frame pendingFrame) {
    final var score = getPrevValue(pendingFramesIndex);
    final var valuePoint = FramePoint.value(score + totalPinsHit);
    pendingFrame.setScore(valuePoint);
    lastValuePoint = valuePoint;
  }

  private int getPrevValue(Integer pendingFramesIndex) {
    int prevValue;
    if (pendingFramesIndex >= 1) {
      prevValue = ((ValuePoint) frames.get(pendingFramesIndex - 1).getScore()).value();
    } else {
      prevValue = 0;
    }
    return pendingFramesIndex == 9 ? prevValue : prevValue + 10;
  }

  public FramePoint getPoints() {
    return frames.values().stream().map(Frame::getScore)
                 .max(Comparator.naturalOrder())
                 .orElseGet(FramePoint::pending);

  }

  @Override
  public String toString() {
    return "Score{" +
        "points =" + lastValuePoint.value() +
        ", pointsPerFrame=" + frames.values().stream().map(Frame::getScore).toList() +
        '}';
  }

  public interface FramePoint extends Comparable<FramePoint> {

    PendingPoint PENDING_PONT = new PendingPoint();

    static PendingPoint pending() {
      return PENDING_PONT;
    }

    static ValuePoint value(int value) {
      return new ValuePoint(value);
    }
  }

  public static class PendingPoint implements FramePoint {

    @Override
    public String toString() {
      return "Pending";
    }

    @Override
    public int compareTo(FramePoint o) {
      return -1;
    }
  }

  public record ValuePoint(int value) implements FramePoint, Comparable<FramePoint> {

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ValuePoint that = (ValuePoint) o;
      return value == that.value;
    }

    @Override
    public int compareTo(FramePoint o) {
      if (o instanceof PendingPoint) {
        return 1;
      }
      return Comparator.comparingInt(ValuePoint::value).compare(this, (ValuePoint) o);
    }
  }
}
