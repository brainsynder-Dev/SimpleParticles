package particles.brainsynder.shape;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import particles.brainsynder.SimpleParticles;
import particles.brainsynder.api.Shape;
import simple.brainsynder.utils.ObjectPager;

import java.io.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ShapeManager {
    private ObjectPager<Shape> shapes;
    private List<Shape> shapeList = new ArrayList<>();

    public ShapeManager () {
        File file = new File (SimpleParticles.getPlugin(SimpleParticles.class).getDataFolder(), "Shapes.json");
        if (!file.exists()) {
            try {
                OutputStreamWriter pw = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
                pw.write("{");
                pw.write("}");
                pw.flush();
                pw.close();
            } catch (IOException ignored) {}
        }
        JSONParser parser = new JSONParser();
        JSONObject json = new JSONObject();
        try {
            json = (JSONObject)parser.parse(new InputStreamReader(new FileInputStream(file), Charsets.UTF_8));
        }catch (Exception ignored){}

        List<Shape> register = new ArrayList<>();
        register.add(new Circle());
        register.add(new Blades());
        register.add(new Cone());
        register.add(new Energy());
        register.add(new Field());
        register.add(new GroundSpiral());
        register.add(new Helix());
        register.add(new Helix2());
        register.add(new Moon());
        register.add(new Orbit());
        register.add(new Rings());
        register.add(new Ripple());
        register.add(new Swirl());
        register.add(new StarBurst());
        register.add(new Test());
        register.add(new Tornado());
        register.add(new ScatterSpiral());
        register.add(new WaterRipple());

        for (Shape shape : register) {
            if (!json.containsKey(shape.getName())) {
                json.put(shape.getName(), shape.getJson());
                shapeList.add(shape);
            }else{
                if (shape.isEnabled()) {
                    Shape newShape = shape.newInstance((JSONObject) json.get(shape.getName()));
                    if (newShape == null) {
                        System.out.println("Could not load shape: "+shape.getName());
                        continue;
                    }
                    shapeList.add(newShape);
                }
            }
        }

        try {
            TreeMap<String, Object> toSave = new TreeMap<>(Collator.getInstance());

            for (Object obj : json.entrySet()) {
                Map.Entry<String, Object> stringObjectEntry = (Map.Entry) obj;
                Object o = stringObjectEntry.getValue();
                toSave.put(stringObjectEntry.getKey(), o);
            }
            Gson g = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
            String prettyJsonString = g.toJson(toSave);
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
            try {
                fw.write(prettyJsonString.replace("\u0026", "&"));
            } finally {
                fw.flush();
                fw.close();
            }
        } catch (Exception ignored) {}

        shapes = new ObjectPager<>(28, shapeList);
    }

    public void unload () {
        shapes = null;
        shapeList = null;
    }

    public Shape getShape (String name) {
        for (Shape shape : shapes) {
            if (shape.getName().equalsIgnoreCase(name)) return shape.newInstance(shape.getJson());
        }
        return null;
    }

    public ObjectPager<Shape> getShapes() {
        if (shapes == null) shapes = new ObjectPager<>(28, shapeList);
        return shapes;
    }
}
