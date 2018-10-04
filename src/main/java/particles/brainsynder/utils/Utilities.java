package particles.brainsynder.utils;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import particles.brainsynder.SimpleParticles;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.nms.IParticleSender;
import simple.brainsynder.reflection.FieldAccessor;

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

    public static String toString (Color color) {
        return color.getRed()+","+color.getGreen()+","+color.getBlue();
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

    public static JSONObject getDefaults () {
        JSONObject json = new JSONObject();
        json.put("enabled", true);
        json.put("count", 1);
        json.put("tick_speed", 1);
        return json;
    }

    public static Vector getBackVector(Location loc) {
        final float newZ = (float) (loc.getZ() + (1 * Math.sin(Math.toRadians(loc.getYaw() + 90))));
        final float newX = (float) (loc.getX() + (1 * Math.cos(Math.toRadians(loc.getYaw() + 90))));
        return new Vector(newX - loc.getX(), 0, newZ - loc.getZ());
    }

    public static ParticleMaker fromJSON (JSONObject json) {
        if (!json.containsKey("particle")) throw new NullPointerException("Missing 'particle' type");
        ParticleMaker.Particle particle = ParticleMaker.Particle.getByName(getString(json, "particle"));
        ParticleMaker maker = new ParticleMaker(particle);

        if (json.containsKey("count")) maker.setCount(Integer.parseInt(getString(json, "count")));
        if (json.containsKey("speed")) maker.setSpeed(Double.parseDouble(getString(json, "speed")));
        if (json.containsKey("offset")) {
            JSONObject offset = (JSONObject) json.get("offset");
            double x = Double.parseDouble(getString(offset, "x", "0"));
            double y = Double.parseDouble(getString(offset, "y", "0"));
            double z = Double.parseDouble(getString(offset, "z", "0"));
            maker.setOffset(x, y, z);
        }
        if (json.containsKey("dust")) {
            JSONObject dust = (JSONObject) json.get("dust");
            Color color = formatColor(getString(dust, "color", "255,0,0"));
            float size = Float.parseFloat(getString(dust, "size", "1.0"));
            maker.setDustOptions(new IParticleSender.DustOptions(color, size));
        }
        if (json.containsKey("item")) {
            JSONObject item = (JSONObject) json.get("item");
            ItemBuilder builder = ItemBuilder.fromJSON(item);
            maker.setData(builder.build());
        }

        return maker;
    }

    public static JSONObject toJSON (ParticleMaker maker) {
        JSONObject json = new JSONObject();
        json.put("particle", getObject(ParticleMaker.Particle.class, maker, "type").getName());
        if (maker.getCount() != 1) json.put("count", maker.getCount());
        if (maker.getSpeed() != 0.0D) json.put("speed", maker.getSpeed());

        double offsetX = maker.getOffsetX();
        double offsetY = maker.getOffsetY();
        double offsetZ = maker.getOffsetZ();
        if ((offsetX != 0.0D) || (offsetY != 0.0D) || (offsetZ != 0.0D)) {
            JSONObject offset = new JSONObject();
            offset.put("x", offsetX);
            offset.put("y", offsetY);
            offset.put("z", offsetZ);
            json.put("offset", offset);
        }

        IParticleSender.DustOptions dust = getObject(IParticleSender.DustOptions.class, maker, "dustOptions");
        if (dust != null) {
            JSONObject redstone = new JSONObject();
            redstone.put("color", toString(dust.getColor()));
            if (dust.getSize() != 1.0) redstone.put("size", dust.getSize());
            json.put("dust", redstone);
        }

        ItemStack item = getObject(ItemStack.class, maker, "data");
        if (item != null) json.put("item", ItemBuilder.fromItem(item).toJSON());

        return json;
    }

    public static <T>T getObject(Class<T> target, Object instance, String name) {
        FieldAccessor<T> field = FieldAccessor.getField(instance.getClass(), name, target);
        return field.get(instance);
    }
    public static String getString(JSONObject json, String key) {
        return getString(json, key, "");
    }
    public static String getString(JSONObject json, String key, String fallback) {
        if (!json.containsKey(key)) {
            if (fallback.isEmpty()) throw new NullPointerException("Could not find '"+key+"' in json");
            return fallback;
        }
        return String.valueOf(json.get(key));
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
