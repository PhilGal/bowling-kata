package io.pgl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

class Player implements Comparable<String> {

  private final List<Frame> frames = new ArrayList<>(10);
  private final Score score = new Score(frames);
  private final String name;
  private int currentFrameIdx;

  public Player(String name) {
    this.name = name;
    currentFrameIdx = nextFrame();
  }

  /**
   * Bowl-Roll a ball
   * @param roll to bowl
   * @return this player
   */
  public Player bowl(Roll roll) {
    final var currentFrame = currentFrame();
    currentFrame.addRoll(roll);
    score.update(currentFrame);
    if (!currentFrame.hasMoreRolls()) {
      this.currentFrameIdx = nextFrame();
    }
    return this;
  }

  public boolean hasMoreRolls() {
    return currentFrame().getFrameNumber() != Game.MAX_FRAMES || currentFrame().hasPendingScore();
  }

  public Score.Points getPoints() {
    score.prettyPrint();
    return score.latest();
  }

  private int nextFrame() {
    if (frames.size() == Game.MAX_FRAMES && currentFrame().getScorePoints().isPresent()) {
      System.out.println("Game has been finished!");
      return currentFrameIdx;
    }
    final var frame = new Frame(frames.size() + 1);
    frames.add(frame);
    return frames.size() - 1;
  }

  private Frame currentFrame() {
    return frames.get(currentFrameIdx);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Player player = (Player) o;
    return Objects.equals(name, player.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(name);
  }

  @Override
  public int compareTo(String o) {
    return Comparator.<String>naturalOrder().compare(this.name, o);
  }
}
