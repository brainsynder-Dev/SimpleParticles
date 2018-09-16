package particles.brainsynder.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import particles.brainsynder.SimpleParticles;
import simple.brainsynder.api.ItemBuilder;

import java.util.ArrayList;
import java.util.List;

public class JoinItem extends ItemBuilder {

    public JoinItem() {
        super(Material.getMaterial(SimpleParticles.getPlugin(SimpleParticles.class).getConfiguration().getString("join-item.item.material")));

        // This should be changed to the new code... aka ItemBuilder
        // Nailed that.
        Configuration config = SimpleParticles.getPlugin(SimpleParticles.class).getConfiguration();
        withData((short) config.getInt("join-item.item.data"));
        withName(ChatColor.translateAlternateColorCodes('&', config.getString("join-item.item.display-name")));
        List<String> lore = new ArrayList<>(); // To translate color codes
        for (String s : config.getStringList("join-item.item.lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        withLore(lore);
    }
}
