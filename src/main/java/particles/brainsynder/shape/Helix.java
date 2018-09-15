package particles.brainsynder.shape;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;

import java.util.Arrays;
import java.util.List;

public class Helix extends ShapeKey {
    private double i = 0.0D;
    private int inc = 4;

    public Helix() {
        super();
    }
    public Helix(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location location, ParticleMaker maker) {
        runSpeedTest();
        Location location2 = location.clone();
        double radius = 1.1D;
        double particles = 100.0D;

        for (int step = 0; step < 28; step += 1) {
            double incline = 6.283185307179586D / particles;
            double angle = (double) (step*inc) * incline + this.i;
            Vector v = new Vector();
            v.setX(Math.cos(angle) * radius);
            v.setZ(Math.sin(angle) * radius);
            location.add(v);
            maker.sendToLocation(location);
            location.subtract(v);
            location.add(0.0D, 0.12D, 0.0D);


            angle = angle + 3.5D;
            Vector v2 = new Vector();
            v2.setX(Math.cos(angle) * radius);
            v2.setZ(Math.sin(angle) * radius);
            location2.add(v2);
            maker.sendToLocation(location2);
            location2.subtract(v2);
            location2.add(0.0D, 0.12D, 0.0D);

            radius -= 0.04399999976158142D;
        }

        this.i += 0.05D;
        runSpeedTest();
    }

    @Override
    public void init(JSONObject json) {
        super.init(json);
        inc = Integer.parseInt(String.valueOf(json.get("incline_angle")));
    }

    @Override
    public void initDefaults(JSONObject json) {
        json.put("incline_angle", 4);
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
