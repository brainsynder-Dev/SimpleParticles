package particles.brainsynder.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Color;
import particles.brainsynder.SimpleParticles;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utilities {
    private static Map<String, Long> startTimeMap = new HashMap<>();

    public static Color hex2Rgb(String colorStr) {
        return Color.fromRGB(
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

            return Color.fromRGB(r, g, b);
        } catch (Exception ignored) {
        }
        return Color.WHITE;
    }

    // These methods are mostly for when I create new shapes... to test the process time
    public static long findDelay(Class clazz, String taskName) {
        return findDelay(clazz, taskName, true);
    }

    public static long findDelay(Class clazz, String taskName, boolean output) {
        String key = clazz.getSimpleName() + " - " + taskName;
        if (startTimeMap.containsKey(key)) {
            long start = startTimeMap.get(key);
            long end = System.nanoTime();
            long diff = (end - start) / 1000000;
            if (SimpleParticles.getPlugin(SimpleParticles.class).canOutputTimes() && output)
                System.out.println(key + " -   Took: " + diff + "ms");
            startTimeMap.remove(key);
            return diff;
        }
        startTimeMap.put(key, System.nanoTime());
        return 0;
    }

    public static String saveTextToHastebin(String text) {
        try {
            String url = "https://hastebin.com/documents";
            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(text);
            wr.flush();
            wr.close();

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) response.append(inputLine);
            in.close();

            JsonElement json = new JsonParser().parse(response.toString());
            if (!json.isJsonObject()) throw new IOException("Cannot parse JSON");
            return "https://hastebin.com/" + json.getAsJsonObject().get("key").getAsString();
        } catch (IOException ignored) {
        }
        return null;
    }

    public static String formatTime (long milliseconds) {
        long time = milliseconds/1000L;
        long minutes = (time / 60 % 60);
        long seconds = (time % 60);

        if (seconds <= 0) return milliseconds+"ms";
        String formatted = "";
        if (minutes > 0) formatted += (minutes+"m ");
        formatted += (seconds+"s ");
        return formatted;
    }

    public static int calculateAverage(List<Integer> marks) {
        Integer sum = 0;
        if(!marks.isEmpty()) {
            for (Integer mark : marks) {
                sum += mark;
            }
            return (int) (sum.doubleValue() / marks.size());
        }
        return sum;
    }
}
