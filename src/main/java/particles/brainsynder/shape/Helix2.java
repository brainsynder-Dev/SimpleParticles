package particles.brainsynder.shape;

import org.bukkit.Location;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;

import java.util.Arrays;
import java.util.List;

public class Helix2 extends ShapeKey {
    private double i = 0.0D;

    public Helix2() {
        super();
    }
    public Helix2(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location loc, ParticleMaker maker) {
        runSpeedTest();

        double radius = 2;

        for (double y = 5; y >= 0; y -= 0.07) {
            radius = (y / 3);
            double x = radius * Math.cos(3 * y);
            double z = radius * Math.sin(3 * y);

            double y2 = 5 - y;

            Location loc2 = new Location(loc.getWorld(), loc.getX() + x, loc.getY() + y2, loc.getZ() + z);
            maker.sendToLocation(loc2);
        }
        runSpeedTest();
    }

    @Override
    public List<ParticleMaker.Particle> blockedParticles() {
        return Arrays.asList(
                ParticleMaker.Particle.BARRIER,
                ParticleMaker.Particle.DAMAGE_INDICATOR,
                ParticleMaker.Particle.DRAGON_BREATH,
                ParticleMaker.Particle.DOLPHIN,
                ParticleMaker.Particle.DRIP_LAVA,
                ParticleMaker.Particle.DRIP_WATER,
                ParticleMaker.Particle.ENCHANTMENT_TABLE,
                ParticleMaker.Particle.END_ROD,
                ParticleMaker.Particle.EXPLOSION_HUGE,
                ParticleMaker.Particle.EXPLOSION_LARGE,
                ParticleMaker.Particle.FIREWORKS_SPARK,
                ParticleMaker.Particle.LAVA,
                ParticleMaker.Particle.NAUTILUS,
                ParticleMaker.Particle.PORTAL,
                ParticleMaker.Particle.SQUID_INK
        );
    }
}
