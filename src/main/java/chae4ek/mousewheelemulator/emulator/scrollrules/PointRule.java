package chae4ek.mousewheelemulator.emulator.scrollrules;

import chae4ek.mousewheelemulator.util.PropUtils;
import java.util.Properties;

/** Scrolling from the start point as from the center of circle */
public class PointRule implements ScrollRule {

  /** Whether to scroll up if the cursor moves to the center */
  public boolean insideIsUp;

  public PointRule() {}

  public PointRule(final Properties properties) {
    setProperties(properties);
  }

  @Override
  public void setProperties(final Properties properties) {
    insideIsUp = PropUtils.getProp(properties, "scrollRule.point.insideIsUp", insideIsUp);
  }

  @Override
  public int getScrollAmountToStartPoint(
      final int startX, final int startY, int x, int y, final int pixelThresholdToScroll) {
    x -= startX;
    y -= startY;
    final int scrollAmount = (int) (Math.sqrt(x * x + y * y) / pixelThresholdToScroll);
    return insideIsUp ? -scrollAmount : scrollAmount;
  }
}
