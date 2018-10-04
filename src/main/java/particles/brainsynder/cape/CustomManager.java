package particles.brainsynder.cape;

import com.google.common.base.Charsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import particles.brainsynder.SimpleParticles;
import simple.brainsynder.utils.ObjectPager;

import java.io.*;
import java.text.Collator;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class CustomManager {
    private ObjectPager<CapeMaker> shapes;
    private List<CapeMaker> shapeList = new ArrayList<>();

    public CustomManager() {
        File file = new File (SimpleParticles.getPlugin(SimpleParticles.class).getDataFolder(), "Custom.json");
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

        List<CapeMaker> register = new ArrayList<>();
        try {
            register.add(new CapeMaker());
        }catch (Exception e){
            e.printStackTrace();
        }

        for (Object o : json.keySet()) {
            String key = String.valueOf(o);
            JSONObject shapeJSON = (JSONObject)json.get(key);
            if (shapeJSON.containsKey("cape")) {
                try {
                    register.add(new CapeMaker(shapeJSON, true));
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }

        for (CapeMaker shape : register) {
            if (!json.containsKey(shape.getName())) {
                json.put(shape.getName(), shape.getJson());
                shapeList.add(shape);
            }else{
                if (shape.isEnabled()) {
                    CapeMaker newShape = (CapeMaker) shape.newInstance((JSONObject) json.get(shape.getName()));
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

    public CapeMaker getCape (String name) {
        for (CapeMaker shape : shapes) {
            if (shape.getName().equalsIgnoreCase(name)) return (CapeMaker) shape.newInstance(shape.getJson());
        }
        return null;
    }

    public ObjectPager<CapeMaker> getCapes() {
        if (shapes == null) shapes = new ObjectPager<>(28, shapeList);
        return shapes;
    }
}
