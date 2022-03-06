package chae4ek.mousewheelemulator.emulator.scrollrules;

import java.util.Properties;

public interface ScrollRule {

  int getScrollAmountToStartPoint(int startX, int startY, int x, int y, int pixelThresholdToScroll);

  default void setProperties(final Properties properties) {}
}
