package chae4ek.mousewheelemulator.emulator.scrollrules;

import chae4ek.mousewheelemulator.util.PropUtils;
import java.util.Properties;

/** Scrolling from the start point in a straight line */
public class LineRule implements ScrollRule {

  /** Emulate mouse wheel while the mouse moves along the X/Y axis */
  public Axis useAxis = Axis.XY;
  /** Whether to reverse the X axis */
  public boolean reverseX;
  /** Whether to reverse the Y axis */
  public boolean reverseY;

  public LineRule() {}

  public LineRule(final Properties properties) {
    setProperties(properties);
  }

  @Override
  public void setProperties(final Properties properties) {
    useAxis = PropUtils.getProp(properties, "scrollRule.line.useAxis", useAxis, Axis.class);
    reverseX = PropUtils.getProp(properties, "scrollRule.line.reverseX", reverseX);
    reverseY = PropUtils.getProp(properties, "scrollRule.line.reverseY", reverseY);
  }

  @Override
  public int getScrollAmountToStartPoint(
      final int startX, final int startY, int x, int y, final int pixelThresholdToScroll) {
    switch (useAxis) {
      case X:
        return (reverseX ? (startX - x) : (x - startX)) / pixelThresholdToScroll;
      case Y:
        return (reverseY ? (y - startY) : (startY - y)) / pixelThresholdToScroll;
      case XY:
        x = reverseX ? (startX - x) : (x - startX);
        y = reverseY ? (y - startY) : (startY - y);
        return (x + y) / pixelThresholdToScroll;
      default:
        throw new IllegalStateException("You must define which axis to use");
    }
  }

  public enum Axis {
    /** Scroll horizontally only */
    X,
    /** Scroll vertically only */
    Y,
    /** Scroll diagonally */
    XY
  }
}
