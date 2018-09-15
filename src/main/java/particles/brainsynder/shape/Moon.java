package particles.brainsynder.shape;

import org.bukkit.Location;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;

import java.util.Arrays;
import java.util.List;

public class Moon extends ShapeKey {
    private double step = 0;
    private double pos = 0;

    public Moon() {
        super();
    }
    public Moon(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location loc, ParticleMaker maker) {
        runSpeedTest();
        loc.add(0,0.1,1.3);

        for (int i = 0; i < 10; i++) {
            if (step < 2 * Math.PI) {
                step += 0.39 / 2;
            } else {
                step = 0;
            }

            if (pos < 2 * Math.PI) {
                pos += 0.39 / 2;
            }else {
                pos = 0;
            }

            double x1 = Math.cos((step * Math.PI) / 6) * 3;
            double x2 = Math.cos((step == 0 ? 6 : step - 0.39 / 2) * Math.PI) / 6 * 3;
            double z1 = Math.sin((step * Math.PI) / 6) * 3;
            double z2 = Math.sin((step == 0 ? 6 : step - 0.39 / 2) * Math.PI) / 6 * 3;

            Location part1 = loc.clone().add(x1, 0, z2);
            Location part2 = loc.clone().subtract(x2, 0, z1);
            Location part3 = loc.clone().add(x2, 0, z2);
            Location part4 = loc.clone().subtract(x1, 0, z1);

            maker.sendToLocation(part1);
            maker.sendToLocation(part2);
            maker.sendToLocation(part3);
            maker.sendToLocation(part4);
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
