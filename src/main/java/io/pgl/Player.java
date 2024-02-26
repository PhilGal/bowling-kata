package io.pgl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

class Player implements Comparable<String> {

  private final String name;
  private final List<Frame> frames = new ArrayList<>(10);
  private Frame currentFrame;
  private final Score score = new Score();

  public Player(String name) {
    this.name = name;
    currentFrame = nextFrame();
  }

  /**
   * Bowl-Roll a ball
   * @param roll to bowl
   * @return this player
   */
  public Player bowl(Roll roll) {
    this.currentFrame.addRoll(roll);
    score.update(currentFrame);
    if (!currentFrame.hasMoreRolls()) {
      this.currentFrame = nextFrame();
    }
    return this;
  }

  public boolean isFrameCompleted() {
    return !currentFrame.hasPendingScore();
  }

  public boolean hasMoreRolls() {
    return currentFrame.getFrameNumber() != Game.MAX_FRAMES || currentFrame.hasPendingScore();
  }

  public Score.Points getPoints() {
    score.prettyPrint();
    return score.latest();
  }

  private Frame nextFrame() {
    if (currentFrame != null && currentFrame.getScorePoints().isPresent() && currentFrame.getFrameNumber() == 10) {
      System.out.println("Game has been finished!");
      return currentFrame;
    }
    final var frame = new Frame(frames.size() + 1);
    frames.add(frame);
    return frame;
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
