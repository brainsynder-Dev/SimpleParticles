package particles.brainsynder;

import org.bukkit.plugin.java.JavaPlugin;
import particles.brainsynder.config.Configuration;

public class SimpleParticles extends JavaPlugin {

    private Configuration config;

    @Override
    public void onEnable() {
        loadConfiguration();
    }

    private void loadConfiguration() {
        config = new Configuration(this, getDataFolder().getName(), "config.yml");
    }

    public Configuration getConfiguration() {
        return config;
    }
}
