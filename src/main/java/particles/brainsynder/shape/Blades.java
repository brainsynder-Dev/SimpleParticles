package particles.brainsynder.shape;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;

public class Blades extends ShapeKey {
    private double rotation = 0.0D;
    int tries = 0;

    public Blades() {
        super();
    }
    public Blades(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location loc, ParticleMaker maker) {
        runSpeedTest();
        loc.add(0,0.1,0);

        // Helicopter blades
        for (double y = 5; y >= 0; y -= 0.07) {
            double radius = (y / 3);

            double angle = (3*y) * 5 + rotation;
            Vector v = new Vector();
            v.setX(Math.cos(angle) * radius);
            v.setZ(Math.sin(angle) * radius);

            loc.add(v);
            maker.sendToLocation(loc);
            loc.subtract(v);
        }
        rotation += 0.02D;
        runSpeedTest();
    }
}
