package particles.brainsynder.api;

import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.api.ParticleMaker;

import java.lang.reflect.Constructor;

public interface Shape {

    default float getOffsetX(){
        return 0.0F;
    }
    default float getOffsetY(){
        return 0.0F;
    }
    default float getOffsetZ(){
        return 0.0F;
    }

    boolean isEnabled();
    boolean hasPermission(Player player);

    @Deprecated ItemBuilder getDefaultItem ();
    ItemBuilder getItem ();
    JSONObject getJson();
    default String getName() {
        return getClass().getSimpleName().toLowerCase();
    }
    default String getPermission() {
        return "simpleparticles.shape."+getName();
    }

    default int getParticleCount() {
        return 1;
    }
    default int getTickSpeed() {
        return 1;
    }

    default void run(Player player, ParticleMaker particle) {}

    default Shape newInstance () {
        try {
            Constructor constructor = getClass().getConstructor(JSONObject.class);
            return (Shape) constructor.newInstance(getJson());
        }catch (Exception e){
            return null;
        }
    }
}
