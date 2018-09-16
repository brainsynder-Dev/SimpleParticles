package particles.brainsynder.config;

import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import simple.brainsynder.files.FileMaker;

import java.util.Arrays;

public class Configuration extends FileMaker {
    public Configuration(Plugin plugin, String directory, String fileName) {
        super(plugin, directory, fileName);
    }

    public void loadDefaults() {
        setDefault("join-item.enabled", true);
        setDefault("join-item.can-not-drop", true);
        setDefault("join-item.can-not-move", true);
        setDefault("join-item.inventories-slot", 1);
        setDefault("join-item.item.material", Material.BOOK.name());
        setDefault("join-item.item.data", 0);
        setDefault("join-item.item.display-name", "&eSimpleParticles &6&l>> &7Selector");
        setDefault("join-item.item.lore", Arrays.asList("&eChange me", "&ein config.yml."));
        setDefault("lores.particles", true);
        setDefault("lores.shapes", true);
        setDefault("website-link.enabled", true);
        setDefault("website-link.account", "https://brainsynder.us/cape/save/0");
    }
}
