package particles.brainsynder.shape;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.math.MathUtils;

import java.util.Arrays;
import java.util.List;

public class Field extends ShapeKey {
    private int step = 0;

    public Field() {
        super();
    }
    public Field(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location location, ParticleMaker maker) {
        runSpeedTest();
        Vector vector = new Vector();
        for (int a = 0; a < 2; a++) {
            for (int i = 0; i < 10; i++) {
                this.step += 1;
                float t = 3.1415927F / 120 * this.step;
                float r = (float) (Math.sin(t) * 1.0F);
                float s = r * 3.1415927F * t;
                vector.setX(1.4F * r * Math.cos(s));
                vector.setY(1.4F * Math.cos(t) + 1.0F);
                vector.setZ(1.4F * r * Math.sin(s));
                MathUtils.rotateVector (vector, 0.0, 0.0, 0.0);
                maker.sendToLocation(location.add (vector));
                location.subtract (vector);
            }
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
