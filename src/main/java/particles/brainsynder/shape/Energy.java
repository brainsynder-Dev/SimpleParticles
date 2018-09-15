package particles.brainsynder.shape;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.math.MathUtils;

import java.util.Arrays;
import java.util.List;

public class Energy extends ShapeKey {
    private int step = 0;
    private float y = 0.0F;
    private float radius = 0.1F;
    private int points = 40;
    private double angularVelocityX = 0.07853981633974483D;
    private double angularVelocityY = 0.07853981633974483D;
    private double angularVelocityZ = 0.07853981633974483D;

    public Energy() {
        super();
    }
    public Energy(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location loc, ParticleMaker maker) {
        runSpeedTest();
        double xRotation = step * angularVelocityX;
        double yRotation = step * angularVelocityY;
        double zRotation = step * angularVelocityZ;

        Location location = loc.add(0.0, 0.5, 0.0);
        for (int point = 0; point < points; point++) {
            double x = Math.cos(point * 3.141592653589793D / 3.0D) * radius;
            double z = Math.sin(point * 3.141592653589793D / 3.0D) * radius;
            double y = Math.sin(point * 3.141592653589793D / 2.0D) * radius;

            Vector vector = new Vector(x, y, z);
            MathUtils.rotateAroundAxisX(vector, -yRotation);
            MathUtils.rotateAroundAxisY(vector, -xRotation);
            MathUtils.rotateAroundAxisZ(vector, zRotation);

            location.add(vector);
            maker.sendToLocation(location.add(vector));
            location.subtract(vector);
        }

        if (step % 2 == 0) {
            radius -= 0.1F;
            if (radius < 0.1F) radius = 2.0F;
        }
        step += 1;
        runSpeedTest();
    }

    @Override
    public void initDefaults(JSONObject json) {
        json.put("radius", 1.5F); // How wide is the Cone
        json.put("points", 40); // How many "lines" of particles there should be
        json.put("render_times", 6); // How many times it will render the particles per-cycle
    }

    @Override
    public void init(JSONObject json) {
        super.init(json);
        points = Integer.parseInt(String.valueOf(json.get("points")));
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
