package particles.brainsynder.shape;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;

import java.util.Arrays;
import java.util.List;

public class GroundSpiral extends ShapeKey {
    private int step = 0, points=6,render_times=5;
    private float y = 0.0F;
    private float radius = 1.5F, radiusStored;

    public GroundSpiral() {
        super();
    }
    public GroundSpiral(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location location, ParticleMaker maker) {
        runSpeedTest();
        location.add(0,0.1,0);
        for (int a=0; a<render_times; a++) {
            for (int i = 0; i < points; i++) {
                double incline = 0.06283185307179587D;
                double angle = step * incline + y + i;
                Vector vector = new Vector();
                vector.setX(Math.cos(angle) * radius);
                vector.setZ(Math.sin(angle) * radius);

                maker.sendToLocation(location.add(vector));

                location.subtract(vector);
                if (y < 3.0F) {
                    radius = ((float) (radius - 0.022D));
                    y = ((float) (y + 0.045D));
                } else {
                    y = 0.0F;
                    step = 0;
                    radius = radiusStored;
                }
            }
        }
        runSpeedTest();
    }

    @Override
    public void initDefaults(JSONObject json) {
        json.put("radius", 1.5F); // How wide is the Cone
        json.put("points", 6); // How many "lines" of particles there should be
        json.put("render_times", 2); // How many times it will render the particles per-cycle
    }

    @Override
    public void init(JSONObject json) {
        super.init(json);
        radius = radiusStored = Float.parseFloat(String.valueOf(json.get("radius")));
        points = Integer.parseInt(String.valueOf(json.get("points")));
        render_times = Integer.parseInt(String.valueOf(json.get("render_times")));
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
