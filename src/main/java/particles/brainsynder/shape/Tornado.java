package particles.brainsynder.shape;

import org.bukkit.Location;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;

public class Tornado extends ShapeKey {
    private float width = 0.0F;

    public Tornado() {
        super();
    }
    public Tornado(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location loc, ParticleMaker maker) {
        runSpeedTest();
        Location location1 = loc.clone();
        location1.add(0.0, 2.0, 0.0);
        location1.add(Math.cos(0.7853981633974483D * width) * 0.2D, 0.0, Math.sin(0.7853981633974483D * width) * 0.2D);

        width = (width + 0.2F);
        Location location2 = loc.clone();
        location2.add(0.0, 2.3, 0.0);
        location2.add(Math.cos(0.7853981633974483D * width) * 0.4D, 0.0, Math.sin(0.7853981633974483D * width) * 0.4D);

        Location location3 = loc.clone();
        location3.add(0.0, 2.6, 0.0);
        location3.add(Math.cos(0.7853981633974483D * width) * 0.6D, 0.0, Math.sin(0.7853981633974483D * width) * 0.6D);

        maker.sendToLocation(location1);
        maker.sendToLocation(location2);
        maker.sendToLocation(location3);
        runSpeedTest();
    }
}
