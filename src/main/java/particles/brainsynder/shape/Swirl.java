package particles.brainsynder.shape;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.math.MathUtils;

public class Swirl extends ShapeKey {
    private int step = 0;

    public Swirl() {
        super();
    }
    public Swirl(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location location, ParticleMaker maker) {
        runSpeedTest();
        Vector vector = new Vector();
        int particlesPerIteration = 8;
        for (int i = 0; i < particlesPerIteration; i++) {
            step += 1;
            if (step > 400) {
                step = 0;
            }
            int particles = 130;
            float t = 3.1415927F / particles * step;
            float size = 1.0F;
            float r = (float) (Math.sin(t * 2.7182817F * particlesPerIteration / particles) * size);
            float s = r * 3.1415927F * t;

            float xFactor = 1.0F;
            vector.setX(xFactor * r * -Math.cos(s));
            float zFactor = 1.0F;
            vector.setZ(zFactor * r * -Math.sin(s));
            float yOffset = 0.6F;
            float yFactor = 0.6F;
            vector.setY(yFactor * Math.cos(r / 3.1415927F * s) + yOffset);

            double zRotation = 0.0D;
            double yRotation = 0.0D;
            double xRotation = 0.0D;
            MathUtils.rotateVector(vector, xRotation, yRotation, zRotation);
            location.add(vector);
            maker.sendToLocation(location);
            location.subtract(vector);
        }
        runSpeedTest();
    }
}
