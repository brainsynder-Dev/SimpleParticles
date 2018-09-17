package particles.brainsynder.shape;

import org.bukkit.Location;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;

import java.util.Arrays;
import java.util.List;

public class OlympicRings extends ShapeKey {
    private int step = 0;
    private float rotation = 0.0F, rotation2 = 0.0F;
    private double radius = 0.4;

    public OlympicRings() {
        super();
    }
    public OlympicRings(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location loc, ParticleMaker maker) {
        runSpeedTest();

        loc.add(0.0D, 0.1, 0.0D);
        for (int i = 0; i < 20; i++) {
            double x = Math.cos(0.785398163397448D * rotation) * radius;
            double z = Math.sin(0.785398163397448D * rotation) * radius;
            loc.add(x, 0.0D, z);

            maker.sendToLocation(loc);
            rotation = ((float) (rotation + 0.3D));
        }

        radius = (radius + 0.3);

        if (radius >= .5) radius = 0.4;
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
