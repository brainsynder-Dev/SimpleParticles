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
import java.util.ArrayList;
import java.util.List;

public class ShapeManager {
    private ObjectPager<Shape> shapes;

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

        List<Shape> shapeList = new ArrayList<>();
        for (Shape shape : register) {
            if (!json.containsKey(shape.getName())) {
                json.put(shape.getName(), shape.getJson());
                shapeList.add(shape);
            }else{
                if (shape.isEnabled()) shapeList.add(shape.newInstance());
            }
        }

        Gson g = (new GsonBuilder()).disableHtmlEscaping().setPrettyPrinting().create();
        String prettyJsonString = g.toJson(json.toJSONString());
        try {
            OutputStreamWriter fw = new OutputStreamWriter(new FileOutputStream(file), Charsets.UTF_8);
            try {
                fw.write(prettyJsonString);
            } finally {
                fw.flush();
                fw.close();
            }
        }catch (Exception ignored){}

        shapes = new ObjectPager<>(28, shapeList);
    }

    public Shape getShape (String name) {
        for (Shape shape : shapes) {
            if (shape.getName().equalsIgnoreCase(name)) return shape;
        }
        return null;
    }

    public ObjectPager<Shape> getShapes() {
        return shapes;
    }
}
