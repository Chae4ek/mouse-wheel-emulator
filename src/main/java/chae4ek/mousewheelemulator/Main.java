package chae4ek.mousewheelemulator;

import chae4ek.mousewheelemulator.emulator.EmulatorConfig;
import chae4ek.mousewheelemulator.emulator.MouseWheelEmulator;
import com.github.kwhat.jnativehook.GlobalScreen;
import com.github.kwhat.jnativehook.NativeHookException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

  public static void main(final String[] args) {
    System.out.println("You can pass the emulator config path through the arguments:");
    System.out.println("java -jar emulator.jar [path to config]");
    System.out.println("Example: java -jar emulator.jar ./emulator-config.conf");
    System.out.println("The next arguments will be ignored");

    final Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
    logger.setUseParentHandlers(false);
    logger.setLevel(Level.OFF);

    try {
      startEmulator(args);
    } catch (final NativeHookException e) {
      System.err.println("Error starting the emulator");
      e.printStackTrace();
      System.exit(1);
    }
  }

  private static void startEmulator(final String[] args) throws NativeHookException {
    GlobalScreen.registerNativeHook();

    final EmulatorConfig config = getConfig(args.length > 0 ? args[0] : null);
    final MouseWheelEmulator emulator = new MouseWheelEmulator(config);

    GlobalScreen.addNativeKeyListener(emulator);
    GlobalScreen.addNativeMouseListener(emulator);
    GlobalScreen.addNativeMouseMotionListener(emulator);
  }

  private static EmulatorConfig getConfig(final String configPath) {
    final File file = configPath == null ? null : new File(configPath);
    try (final InputStream inputStream =
        file != null && file.exists() && file.canRead()
            ? new FileInputStream(file)
            : Main.class.getResourceAsStream("/emulator-config.conf")) {
      final Properties properties = new Properties();
      properties.load(inputStream);
      return new EmulatorConfig(properties);
    } catch (final IOException e) {
      System.err.println("Error parsing the emulator-config.conf. Loading the default settings");
      e.printStackTrace();
      return new EmulatorConfig();
    }
  }
}
