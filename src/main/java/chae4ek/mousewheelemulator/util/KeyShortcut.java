package chae4ek.mousewheelemulator.util;

public class KeyShortcut {

  public static final int MAX_LENGTH = 32; // sizeof(int)

  // regex: [1*00*1*]
  // the first '1's are keys, the last '1's are mouse buttons
  private final int[] keyCombination;
  // the same rule: 0x[1*0*1*]
  private int unpressedKeysMask;

  public KeyShortcut(final int[] nativeKeys, final int[] nativeButtons) {
    if (nativeKeys.length == 0 && nativeButtons.length == 0) {
      throw new IllegalArgumentException("You must define key(s) or/and mouse button(s)");
    }
    if (nativeKeys.length + nativeButtons.length > MAX_LENGTH) {
      throw new IllegalArgumentException("The max length of the keyboard shortcut is 32");
    }

    keyCombination = new int[nativeKeys.length + nativeButtons.length + 1];

    int i = 0;
    for (; i != nativeKeys.length; ++i) {
      if (nativeKeys[i] == 0 || getKeyMask(nativeKeys[i]) != 0)
        throw new IllegalArgumentException("The keys must be different and non-zero");
      keyCombination[i] = nativeKeys[i];
    }
    keyCombination[i] = 0;
    for (int j = 0; ++i < keyCombination.length; ++j) {
      if (nativeButtons[j] == 0 || getButtonMask(nativeButtons[j]) != 0)
        throw new IllegalArgumentException("The buttons must be different and non-zero");
      keyCombination[i] = nativeButtons[j];
    }

    final int keyMask = nativeKeys.length > 0 ? 0x80000000 >> nativeKeys.length - 1 : 0;
    final int buttonMask =
        nativeButtons.length > 0 ? 0xffffffff >>> MAX_LENGTH - nativeButtons.length : 0;
    unpressedKeysMask = keyMask | buttonMask;
  }

  public boolean isKeyPressed(final int nativeKey) {
    return (unpressedKeysMask & getKeyMask(nativeKey)) == 0;
  }

  public boolean isButtonPressed(final int nativeButton) {
    return (unpressedKeysMask & getButtonMask(nativeButton)) == 0;
  }

  public boolean isAllPressed() {
    return unpressedKeysMask == 0;
  }

  public void pressKey(final int nativeKey) {
    unpressedKeysMask &= ~getKeyMask(nativeKey);
  }

  public void releaseKey(final int nativeKey) {
    unpressedKeysMask |= getKeyMask(nativeKey);
  }

  public void pressButton(final int nativeButton) {
    unpressedKeysMask &= ~getButtonMask(nativeButton);
  }

  public void releaseButton(final int nativeButton) {
    unpressedKeysMask |= getButtonMask(nativeButton);
  }

  private int getKeyMask(final int nativeKey) {
    for (int i = 0; keyCombination[i] != 0; ++i)
      if (keyCombination[i] == nativeKey) return 0x80000000 >>> i;
    return 0;
  }

  private int getButtonMask(final int nativeButton) {
    for (int i = keyCombination.length; keyCombination[--i] != 0; )
      if (keyCombination[i] == nativeButton) return 1 << keyCombination.length - ++i;
    return 0;
  }
}
