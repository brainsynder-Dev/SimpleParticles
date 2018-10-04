package particles.brainsynder.shape;

import org.apache.commons.lang.WordUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import particles.brainsynder.api.Shape;
import particles.brainsynder.utils.Utilities;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.storage.TriLoc;

public abstract class ShapeKey implements Shape {
    private JSONObject json;
    private TriLoc<Float> offsets;
    private boolean enabled = true;
    private ItemBuilder item;
    private int count = 1, tickSpeed = 1, tested = 0, counter = 0;

    /**
     * Loads default JSON data...
     */
    public ShapeKey () {
        json = new JSONObject();
        json.put("enabled", true);
        json.put("count", 1);
        json.put("tick_speed", 1);
        json.put("item", getDefaultItem().toJSON());

        JSONObject offset = new JSONObject();
        offset.put("x", 0);
        offset.put("y", 0);
        offset.put("z", 0);
        json.put("particle_offset", offset);
        initDefaults(json);
        init(json);
    }

    /**
     * This constructor is to load saved JSON from the shape file
     */
    public ShapeKey (JSONObject json) {
        this(json, false);
    }
    public ShapeKey (JSONObject json, boolean overrideDefaults) {
        if (!json.containsKey("enabled")) json.put("enabled", true);
        if (!json.containsKey("count")) json.put("count", 1);
        if (!json.containsKey("tick_speed")) json.put("tick_speed", 1);
        if (!json.containsKey("item")) json.put("item", getDefaultItem().toJSON());

        if (overrideDefaults) initDefaults(json);
        this.json = json;
        init(json);
    }

    public void init (JSONObject json) {
        if (json.containsKey("enabled")) enabled = Boolean.valueOf(String.valueOf(json.get("enabled")));
        if (json.containsKey("count")) count = Integer.parseInt(String.valueOf(json.get("count")));
        if (json.containsKey("tick_speed")) tickSpeed = Integer.parseInt(String.valueOf(json.get("tick_speed")));
        if (json.containsKey("particle_offset")) {
            JSONObject offset = (JSONObject) json.get("particle_offset");
            float offsetX=0, offsetY=0, offsetZ=0;

            if (offset.containsKey("x")) offsetX = Float.parseFloat(String.valueOf(offset.get("x")));
            if (offset.containsKey("y")) offsetY = Float.parseFloat(String.valueOf(offset.get("y")));
            if (offset.containsKey("z")) offsetZ = Float.parseFloat(String.valueOf(offset.get("z")));

            offsets = new TriLoc<>(offsetX, offsetY, offsetZ);
        }
        if (json.containsKey("item")) {
            item = ItemBuilder.fromJSON((JSONObject) json.get("item"));
        }else{
            this.json.put("item", getDefaultItem().toJSON());
            item = getDefaultItem();
        }
    }

    void runSpeedTest() {
        if (tested >= 10) return;
        Utilities.findDelay(getClass(), "shape");
        tested++;
    }

    void output(String message) {
        if (counter == 0) {
            System.out.println(message);
            counter = 6;
            return;
        }

        counter--;
    }

    public void initDefaults (JSONObject json){}

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.DIAMOND).withName("&a"+ WordUtils.capitalize(getName()));
    }

    @Override
    public JSONObject getJson() {
        return json;
    }

    @Override
    public float getOffsetX() {
        return offsets.getX();
    }

    @Override
    public float getOffsetY() {
        return offsets.getY();
    }

    @Override
    public float getOffsetZ() {
        return offsets.getZ();
    }

    @Override
    public int getParticleCount() {
        return count;
    }

    @Override
    public int getTickSpeed() {
        return tickSpeed;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean hasPermission(Player player) {
        return player.hasPermission(getPermission());
    }

    @Override
    public ItemBuilder getItem() {
        return item;
    }
}
