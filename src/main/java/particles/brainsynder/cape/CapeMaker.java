package particles.brainsynder.cape;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import particles.brainsynder.shape.ShapeKey;
import particles.brainsynder.utils.CustomParseException;
import particles.brainsynder.utils.Utilities;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.math.MathUtils;
import simple.brainsynder.nms.IParticleSender;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CapeMaker extends ShapeKey {
    private Map<String, ParticleMaker> particleKeys;
    private Map<String, Double> offsetMods;
    private List<List<CapePos>> shape;
    private int length;
    private double yOffset;
    private boolean angled = false;
    private String name;

    public CapeMaker() {
        this(Utilities.getDefaults());
    }

    public CapeMaker(JSONObject json) {
        this(json, Utilities.getDefaults().toJSONString().equals(json.toJSONString()));
    }

    public CapeMaker(JSONObject json, boolean loadDefaults) {
        super(json, loadDefaults);
    }

    @Override
    public void initDefaults(JSONObject json) {
        if (json.containsKey("cape")) {
            name = Utilities.getString(((JSONObject)json.get("cape")), "name");
        }else {
            JSONObject config = new JSONObject();
            JSONObject keys = new JSONObject();
            keys.put("R", Utilities.toJSON(new ParticleMaker(ParticleMaker.Particle.REDSTONE).setDustOptions(new IParticleSender.DustOptions(Color.RED, 1))));
            config.put("keys", keys);

            JSONArray section = new JSONArray();
            for (int i = 0; i < 8; i++) {
                JSONArray row = new JSONArray();
                for (int i1 = 0; i1 < 5; i1++) row.add("R");
                section.add(row);
            }
            config.put("shape", section);
            config.put("angled", false);
            config.put("yOffset", 1.5);

            json.put("cape", config);
        }
        super.initDefaults(json);
    }

    @Override
    public void init(JSONObject jsonO) {
        super.init(jsonO);
        particleKeys = new HashMap<>();
        offsetMods = new HashMap<>();
        shape = new ArrayList<>();

        if (!jsonO.containsKey("cape")) throw new NullPointerException("Can't be used, as it is missing the 'cape' section");
        JSONObject json = (JSONObject) jsonO.get("cape");
        name = Utilities.getString(json, "name", "capemaker");
        if (!json.containsKey("keys")) throw new NullPointerException("Can't be used, as it is missing 'keys'");
        if (!json.containsKey("shape"))
            throw new NullPointerException("Can't be used, as it is missing a 'shape'");
        angled = Boolean.parseBoolean(Utilities.getString(json, "angled", "false"));
        yOffset = Double.parseDouble(Utilities.getString(json, "yOffset", "1.5"));
        System.out.println(name+"|Angle: "+ angled);
        System.out.println(name+"|yOffset: "+yOffset);
        JSONObject keys = (JSONObject) json.get("keys");
        keys.keySet().forEach(key -> {
            String string = (String) key;
            JSONObject value = (JSONObject) keys.getOrDefault(key, new JSONObject());
            if (value.isEmpty()) {
                particleKeys.put(string, null);
            } else {
                try {
                    ParticleMaker maker = Utilities.fromJSON(value);
                    particleKeys.put(string, maker);
                } catch (Exception e) {
                    try {
                        ParticleMaker maker = Utilities.fromJSON(value);
                        particleKeys.put(string, maker);
                    } catch (Exception e2) {
                        System.out.println("json: "+json.toJSONString());
                        e2.printStackTrace();
                    }
                }
            }
        });
        if (json.containsKey("key_offsets")) {
            JSONObject offsets = (JSONObject) json.get("key_offsets");
            offsets.keySet().forEach(key -> {
                String string = (String) key;
                double value = Double.parseDouble(Utilities.getString(offsets, string));
                if ((value != 0.0D) && (particleKeys.containsKey(string))) offsetMods.put(string, value);
            });
        }

        JSONArray shapes = (JSONArray) json.get("shape");
        int rowNum = 1;
        if (shapes.isEmpty()) throw new NullPointerException("Can't be used [it is missing a 'shape']");

        for (Object o : shapes) {
            try {
                JSONArray row = (JSONArray) o;
                if (row.isEmpty()) throw new CustomParseException("Row(" + rowNum + ") is empty");
                if ((rowNum != 1) && (row.size() != length))
                    throw new CustomParseException("Row(" + rowNum + ") is not the same length as previous rows");
                List<CapePos> rowList = new ArrayList<>();
                int finalRowNum = rowNum;
                row.forEach(o1 -> {
                    List<String> characters = new ArrayList<>();
                    if (o1 instanceof JSONArray) {
                        ((JSONArray)o1).forEach(o2 -> {
                            String key = String.valueOf(o2);
                            if ((!key.equals("air")) && (!particleKeys.containsKey(key))) {
                                try {
                                    throw new CustomParseException("Row(" + finalRowNum + ") Key("+key+") is not defined");
                                } catch (CustomParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            characters.add(key);
                        });
                    }else{
                        String key = String.valueOf(o1);
                        if ((!key.equals("air")) && (!particleKeys.containsKey(key))) {
                            try {
                                throw new CustomParseException("Row(" + finalRowNum + ") Key("+key+") is not defined");
                            } catch (CustomParseException e) {
                                System.out.println("json: "+json.toJSONString());
                                e.printStackTrace();
                            }
                            return;
                        }
                        characters.add(key);
                    }
                    rowList.add(new CapePos (characters));
                });
                shape.add(rowList);
                if (rowNum == 1) length = row.size();
                rowNum++;
            } catch (CustomParseException e) {
                System.out.println("json: "+json.toJSONString());
                e.printStackTrace();
            }
        }
    }

    public Map<String, ParticleMaker> getParticleKeys() {
        return particleKeys;
    }

    public List<List<CapePos>> getShape() {
        return shape;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void run(Location loc) {
        Location location = loc.clone();
        double space = 0.2D;
        double defX = location.getX() - space * (double) length / 2.0D + space / 2.0D;
        double x = defX;
        double y = (location.getY() + yOffset);
        double angle = (double) (-((location.getYaw() + 180.0F) / 60.0F));
        angle += location.getYaw() < -180.0F ? 3.25D : 2.985D;

        for (int i = 0; i < getShape().size(); i++) {
            for (CapePos capePos : getShape().get(i)) {
                for (String character : capePos.getCharacters()) {
                    ParticleMaker maker = getParticleKeys().getOrDefault(character, null);
                    Location target = location.clone();
                    target.setX(x);
                    target.setY(y);
                    Vector rotation = target.toVector().subtract(location.toVector());
                    Vector back = Utilities.getBackVector(location);

                    rotation = MathUtils.rotateAroundAxisY(rotation, angle);
                    double iT = (double) i / 10.0D; // Used to make the cape angled
                    double offset = -0.2D;
                    if (offsetMods.containsKey(character)) offset = (offset+(-offsetMods.get(character)));
                    if (angled) offset = (offset-iT);
                    back.setY(0).multiply(offset);

                    location.add(rotation);
                    location.add(back);

                    if (!character.equals("air")) maker.sendToLocation(location);

                    location.subtract(rotation);
                    location.subtract(back);
                }
                x += space;
            }
            y -= space;
            x = defX;
        }
    }
}
