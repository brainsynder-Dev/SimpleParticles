package particles.brainsynder.shape;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import simple.brainsynder.api.ItemBuilder;
import simple.brainsynder.api.ParticleMaker;

public class Circle extends ShapeKey {
    private float pos = 0.0F;

    @Override
    public ItemBuilder getDefaultItem() {
        return new ItemBuilder(Material.DIAMOND).withName("&aCircle");
    }

    @Override
    public void run(Player player, ParticleMaker maker) {
        Location loc = player.getLocation ();
        loc.add(0.0D, 1.5D, 0.0D);
        loc.add(Math.cos(0.785398163397448D * pos) * 0.4D, 0.0D, Math.sin(0.785398163397448D * pos) * 0.4D);

        maker.sendToLocation(loc.add(0.0D, 1.0D, 0.0D));

        pos = ((float) (pos + 0.3D));
    }
}
