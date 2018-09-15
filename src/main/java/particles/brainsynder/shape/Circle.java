package particles.brainsynder.shape;

import org.bukkit.Location;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;

public class Circle extends ShapeKey {
    private float rotation = 0.0F;

    public Circle () {
        super();
    }
    public Circle (JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location loc, ParticleMaker maker) {
        runSpeedTest();
        loc.add(0.0D, 1.5D, 0.0D);
        double radius = 0.4;
        double x = Math.cos(0.785398163397448D * rotation) * radius;
        double z = Math.sin(0.785398163397448D * rotation) * radius;
        loc.add(x, 0.0D, z);

        maker.sendToLocation(loc.add(0.0D, 1.0D, 0.0D));

        rotation = ((float) (rotation + 0.3D));
        runSpeedTest();
    }
}
