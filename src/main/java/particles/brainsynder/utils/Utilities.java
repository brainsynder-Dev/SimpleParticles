package particles.brainsynder.utils;

import org.bukkit.Color;

import java.util.Arrays;

public class Utilities {
    public static Color hex2Rgb(String colorStr) {
        return Color.fromBGR(
                Integer.valueOf(colorStr.substring(1, 3), 16),
                Integer.valueOf(colorStr.substring(3, 5), 16),
                Integer.valueOf(colorStr.substring(5, 7), 16)
        );
    }

    public static Color formatColor (String color) {
        if (color.startsWith("#")) return hex2Rgb(color.replace("#", ""));
        if (!color.contains(",")) return Color.WHITE;

        String[] array = color.split(",");
        if (Arrays.asList(array).size() > 3) return Color.WHITE;
        if (Arrays.asList(array).size() < 3) return Color.WHITE;

        try {
            int r = Integer.parseInt(array[0]);
            int g = Integer.parseInt(array[1]);
            int b = Integer.parseInt(array[2]);

            return Color.fromBGR(r, g, b);
        } catch (Exception ignored) {
        }
        return Color.WHITE;
    }
}
