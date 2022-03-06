package chae4ek.mousewheelemulator.emulator;

import chae4ek.mousewheelemulator.emulator.scrollrules.ScrollRule;
import chae4ek.mousewheelemulator.util.KeyShortcut;
import com.github.kwhat.jnativehook.keyboard.NativeKeyEvent;
import com.github.kwhat.jnativehook.keyboard.NativeKeyListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseEvent;
import com.github.kwhat.jnativehook.mouse.NativeMouseInputListener;
import com.github.kwhat.jnativehook.mouse.NativeMouseWheelListener;
import java.awt.AWTException;
import java.awt.Robot;

public class MouseWheelEmulator
    implements NativeMouseWheelListener, NativeKeyListener, NativeMouseInputListener {

  /**
   * @deprecated to remove
   */
  @Deprecated private static final Robot robot;

  static {
    try {
      robot = new Robot();
    } catch (final AWTException e) {
      e.printStackTrace();
      throw new RuntimeException("Robot is not supported");
    }
  }

  private KeyShortcut keyShortcut;

  private boolean emulateWheel;
  private int pixelThresholdToScroll;

  private int startX;
  private int startY;
  private int lastX;
  private int lastY;

  private ScrollRule scrollRule;

  public MouseWheelEmulator(final EmulatorConfig config) {
    setConfig(config);
  }

  public void setConfig(final EmulatorConfig config) {
    keyShortcut = new KeyShortcut(config.keysToPress, config.buttonsToPress);
    setPixelThresholdToScroll(config.pixelThresholdToScroll);
    setScrollRule(config.scrollRule);
  }

  public void setScrollRule(final ScrollRule scrollRule) {
    this.scrollRule = scrollRule;
  }

  public void setPixelThresholdToScroll(final int pixelThresholdToScroll) {
    if (pixelThresholdToScroll <= 0) {
      throw new IllegalArgumentException("Pixel threshold to scroll must be > 0");
    }
    this.pixelThresholdToScroll = pixelThresholdToScroll;
  }

  @Override
  public void nativeKeyTyped(final NativeKeyEvent nativeEvent) {}

  @Override
  public void nativeKeyPressed(final NativeKeyEvent nativeEvent) {
    keyShortcut.pressKey(nativeEvent.getKeyCode());
    assertEmulation();
  }

  @Override
  public void nativeKeyReleased(final NativeKeyEvent nativeEvent) {
    keyShortcut.releaseKey(nativeEvent.getKeyCode());
    assertEmulation();
  }

  @Override
  public void nativeMouseClicked(final NativeMouseEvent nativeEvent) {}

  @Override
  public void nativeMousePressed(final NativeMouseEvent nativeEvent) {
    keyShortcut.pressButton(nativeEvent.getButton());
    assertEmulation();
  }

  @Override
  public void nativeMouseReleased(final NativeMouseEvent nativeEvent) {
    keyShortcut.releaseButton(nativeEvent.getButton());
    assertEmulation();
  }

  @Override
  public void nativeMouseMoved(final NativeMouseEvent nativeEvent) {
    emulateWheel(nativeEvent);
  }

  @Override
  public void nativeMouseDragged(final NativeMouseEvent nativeEvent) {
    emulateWheel(nativeEvent);
  }

  private void assertEmulation() {
    if (keyShortcut.isAllPressed()) {
      if (!emulateWheel) {
        emulateWheel = true;
        startX = lastX;
        startY = lastY;
      }
    } else emulateWheel = false;
  }

  private void emulateWheel(final NativeMouseEvent nativeEvent) {
    final int x = nativeEvent.getX();
    final int y = nativeEvent.getY();
    if (emulateWheel) {
      final int diffLast =
          scrollRule.getScrollAmountToStartPoint(
              startX, startY, lastX, lastY, pixelThresholdToScroll);
      final int diffNew =
          scrollRule.getScrollAmountToStartPoint(startX, startY, x, y, pixelThresholdToScroll);
      if (diffLast != diffNew) {
        final int amount = diffLast - diffNew;
        robot.mouseWheel(amount);
        // TODO: it doesn't work correctly on Linux
        //  issue: https://github.com/kwhat/jnativehook/issues/397
        /*final NativeMouseWheelEvent event =
            new NativeMouseWheelEvent(
                NativeMouseEvent.NATIVE_MOUSE_WHEEL,
                amount > 0 ? NativeMouseEvent.BUTTON5_MASK : NativeMouseEvent.BUTTON4_MASK,
                startX,
                startY,
                1,
                NativeMouseWheelEvent.WHEEL_UNIT_SCROLL,
                1,
                amount > 0 ? 1 : -1,
                NativeMouseWheelEvent.WHEEL_VERTICAL_DIRECTION);
        GlobalScreen.postNativeEvent(event);*/
      }
    }
    lastX = x;
    lastY = y;
  }
}
