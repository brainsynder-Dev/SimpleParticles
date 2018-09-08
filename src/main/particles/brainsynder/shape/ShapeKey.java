package particles.brainsynder.shape;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import particles.brainsynder.api.Shape;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.storage.TriLoc;

public abstract class ShapeKey implements Shape {
    private JSONObject json;
    private TriLoc<Float> offsets;
    private boolean enabled = true;
    private ItemBuilder item;
    private int count = 1, tickSpeed = 1;

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
        offset.put("x", 0F);
        offset.put("y", 0F);
        offset.put("z", 0F);
        json.put("particle_offset", offset);
        init(json);
    }

    /**
     * This constructor is to load saved JSON from the shape file
     */
    public ShapeKey (JSONObject json) {
        this.json = json;
        init(json);
    }

    public void init (JSONObject json) {
        if (json.containsKey("enabled")) enabled = Boolean.valueOf(String.valueOf(json.get("enabled")));
        if (json.containsKey("count")) count = Integer.parseInt(String.valueOf(json.get("count")));
        if (json.containsKey("tick_speed")) tickSpeed = Integer.parseInt(String.valueOf(json.get("tickSpeed")));
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
