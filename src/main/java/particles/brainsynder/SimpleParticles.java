package particles.brainsynder;

import org.bukkit.plugin.java.JavaPlugin;
import particles.brainsynder.commands.CommandSimpleParticles;
import particles.brainsynder.config.Configuration;
import particles.brainsynder.shape.ShapeManager;
import simple.brainsynder.commands.CommandRegistry;

public class SimpleParticles extends JavaPlugin {

    private boolean outputTimes = false;
    private Configuration config;
    private ShapeManager shapeManager;

    @Override
    public void onEnable() {
        loadConfiguration();
        shapeManager = new ShapeManager();

        try {
            CommandRegistry<SimpleParticles> registry = new CommandRegistry<>(this);
            registry.register(new CommandSimpleParticles(this));
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisable() {
        shapeManager.unload();
        shapeManager = null;
    }

    private void loadConfiguration() {
        config = new Configuration(this, getDataFolder().getName(), "config.yml");
    }

    public Configuration getConfiguration() {
        return config;
    }

    public ShapeManager getShapeManager() {
        return shapeManager;
    }

    public boolean canOutputTimes() {
        return outputTimes;
    }

    public void setOutputTimes(boolean outputTimes) {
        this.outputTimes = outputTimes;
    }
}
