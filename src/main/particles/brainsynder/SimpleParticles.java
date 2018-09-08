package particles.brainsynder;

import org.bukkit.plugin.java.JavaPlugin;
import particles.brainsynder.config.Configuration;

public class SimpleParticles extends JavaPlugin {

    private Configuration config;
    private static SimpleParticles instance;

    @Override
    public void onEnable() {
        instance = this;
        loadConfiguration();
    }

    private void loadConfiguration() {
        config = new Configuration(this, getDataFolder().getName(), "config.yml");
    }

    public static SimpleParticles getInstance() {
        return instance;
    }

    public Configuration getConfiguration() {
        return config;
    }
}
