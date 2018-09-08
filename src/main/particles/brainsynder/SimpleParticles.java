package particles.brainsynder;

import org.bukkit.plugin.java.JavaPlugin;

public class SimpleParticles extends JavaPlugin {

    private static SimpleParticles instance;

    @Override
    public void onEnable() {
        instance = this;
    }

    public static SimpleParticles getInstance() {
        return instance;
    }
}
