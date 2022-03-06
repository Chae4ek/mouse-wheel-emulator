package chae4ek.mousewheelemulator.emulator;

import chae4ek.mousewheelemulator.emulator.scrollrules.LineRule;
import chae4ek.mousewheelemulator.emulator.scrollrules.PointRule;
import chae4ek.mousewheelemulator.emulator.scrollrules.ScrollRule;
import chae4ek.mousewheelemulator.util.PropUtils;
import java.util.Properties;

public class EmulatorConfig {

  /** The keys which must be depressed to emulate the mouse wheel */
  public int[] keysToPress;
  /** The mouse buttons which must be depressed to emulate the mouse wheel */
  public int[] buttonsToPress;

  /** The distance you need to move the cursor to emulate a single mouse scroll (in pixels) */
  public int pixelThresholdToScroll = 15;

  public ScrollRule scrollRule = new LineRule();

  public EmulatorConfig() {
    keysToPress = new int[] {29, 56};
    buttonsToPress = new int[] {};
  }

  public EmulatorConfig(final Properties properties) {
    setProperties(properties);
  }

  public void setProperties(final Properties properties) {
    final String[] keys = properties.getProperty("emulator.keysToPress", "").split("[+]");
    final String[] buttons = properties.getProperty("emulator.buttonsToPress", "1").split("[+]");
    if (!keys[0].equals("")) {
      keysToPress = new int[keys.length];
      for (int i = 0; i < keys.length; ++i) keysToPress[i] = Integer.parseInt(keys[i]);
    } else keysToPress = new int[] {};
    if (!buttons[0].equals("")) {
      buttonsToPress = new int[buttons.length];
      for (int i = 0; i < buttons.length; ++i) buttonsToPress[i] = Integer.parseInt(buttons[i]);
    } else buttonsToPress = new int[] {};

    pixelThresholdToScroll =
        PropUtils.getProp(properties, "emulator.pixelThresholdToScroll", pixelThresholdToScroll);

    final String scrollRule = properties.getProperty("emulator.scrollRule", "line");
    if (scrollRule.equalsIgnoreCase("line")) this.scrollRule.setProperties(properties);
    if (scrollRule.equalsIgnoreCase("point")) this.scrollRule = new PointRule(properties);
  }
}
