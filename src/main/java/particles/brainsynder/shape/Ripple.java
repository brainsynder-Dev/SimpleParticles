package particles.brainsynder.shape;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.math.MathUtils;

import java.util.Arrays;
import java.util.List;

public class Ripple extends ShapeKey {
    private int step = 0;

    public Ripple() {
        super();
    }
    public Ripple(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location location, ParticleMaker maker) {
        runSpeedTest();

        location.subtract(0,0.8,0);
        Vector vector = new Vector();
        for (int i = 0; i < 15; ++i) {
            ++step;
            float t = 3.1415927F / (float) 150 * (float) step;
            float r = MathUtils.sin(t * 2.7182817F * (float) 15 / (float) 120) * 1.5F;
            float s = r * 3.1415927F * t;
            vector.setX(1.0F * r * -MathUtils.cos(s));
            vector.setZ(1.0F * r * -MathUtils.sin(s));
            vector.setY(1.0F);
            MathUtils.rotateVector(vector, 0.0D, 0.0D, 0.0);
            location.add(vector);
            maker.sendToLocation(location);
            location.subtract(vector);
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
