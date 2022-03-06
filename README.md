# Mouse Wheel Emulator

This project emulates the mouse wheel using mouse movement and keyboard shortcuts (and/or mouse
keyboard shortcuts). You can use it for a tablet to emulate mouse wheel with a pen. This works in
any application and on different platforms (for this purpose, it
uses [this library](https://github.com/kwhat/jnativehook)).

## Features

- [x] You can use **keyboard** and **mouse keyboard shortcuts** together or separately.
- [x] You can configure the mouse movement threshold in pixels to control the scroll sensitivity.
- [x] You can set a **scrolling rule** that defines which axes are used, which direction is scrolled
  up or down, or even scrolling from a point as from the center of circle.

## Running

Run it only on the command line cause it doesn't have any interface. Otherwise, you will have to
terminate the process through the Task Manager.

```bash
java -jar emulator.jar
```

Java 8 or above is required.

## Configuration

You can pass the emulator config path through the arguments:

```bash
java -jar emulator.jar [path to config]
```

The default config is in the `.jar` archive and looks like below. All fields are optional.

Note: see native keycodes for shortcuts
[here](https://github.com/kwhat/jnativehook/blob/2.2/src/main/java/com/github/kwhat/jnativehook/keyboard/NativeKeyEvent.java)
.

```properties
##########################################################################################
# The keys which must be depressed to emulate the mouse wheel
# You can use nothing or keyboard shortcuts like this: "29+56" (LEFT_CTRL+LEFT_ALT)
emulator.keysToPress=29+56
# The mouse buttons which must be depressed to emulate the mouse wheel
# You can use nothing or keyboard shortcuts like this: "1+3" (LMB+RMB)
emulator.buttonsToPress=
# The distance you need to move the cursor to emulate a single mouse scroll (in pixels)
emulator.pixelThresholdToScroll=15
##########################################################################################
# Possible values: [line, point]
# line: Scrolling from the start point in a straight line
# point: Scrolling from the start point as from the center of circle
emulator.scrollRule=line
##########################################################################################
# Emulate mouse wheel while the mouse moves along the X/Y axis
# Possible values: [X, Y, XY]
# X: Scroll horizontally only
# Y: Scroll vertically only
# XY: Scroll diagonally
scrollRule.line.useAxis=XY
# Whether to reverse the X axis
scrollRule.line.reverseX=true
# Whether to reverse the Y axis
scrollRule.line.reverseY=true
##########################################################################################
# Whether to scroll up if the cursor moves to the center
scrollRule.point.insideIsUp=false
```
