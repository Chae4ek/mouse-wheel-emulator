package chae4ek.mousewheelemulator.util;

import java.util.Properties;

public class PropUtils {

  public static int getProp(final Properties properties, final String key, final int defaultValue) {
    return Integer.parseInt(properties.getProperty(key, Integer.toString(defaultValue)));
  }

  public static boolean getProp(
      final Properties properties, final String key, final boolean defaultValue) {
    return Boolean.parseBoolean(properties.getProperty(key, Boolean.toString(defaultValue)));
  }

  public static <T extends Enum<T>> T getProp(
      final Properties properties,
      final String key,
      final T defaultValue,
      final Class<T> enumClass) {
    final String value = properties.getProperty(key, defaultValue.name());
    return Enum.valueOf(enumClass, value);
  }
}
