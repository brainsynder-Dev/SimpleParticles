package particles.brainsynder.config;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import particles.brainsynder.SimpleParticles;

import java.util.ArrayList;
import java.util.List;

public class JoinItem extends ItemStack {

    public JoinItem() {
        // This should be changed to the new code... aka ItemBuilder
        Configuration config = SimpleParticles.getPlugin(SimpleParticles.class).getConfiguration();
        this.setType(Material.getMaterial(config.getString("join-item.item.material")));
        setDurability((short) config.getInt("join-item.item.data")); // Another way of setting data for older versions
        ItemMeta meta = getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', config.getString("join-item.item.display-name")));
        List<String> lore = new ArrayList<>(); // To translate color codes
        for (String s : config.getStringList("join-item.item.lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', s));
        }
        meta.setLore(lore);
        setItemMeta(meta);
    }
}
