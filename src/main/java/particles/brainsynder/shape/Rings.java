package particles.brainsynder.shape;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.math.MathUtils;

public class Rings extends ShapeKey {
    private int step = 0;

    public Rings() {
        super();
    }
    public Rings(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location location, ParticleMaker maker) {
        runSpeedTest();
        location.add(0,0.6,0);
        for (int i = 0; i < 2; i++) {
            double inc = 6.283185307179586D / (10 * 20);
            double toAdd = 0.0D;
            if (i == 1) toAdd = 3.5D;
            double angle = (step * inc + toAdd);
            Vector v = new Vector();
            v.setX(Math.cos(angle));
            v.setZ(Math.sin(angle));
            if (i == 0) {
                MathUtils.rotateAroundAxisZ(v, 10.0D);
            } else {
                MathUtils.rotateAroundAxisZ(v, 100.0D);
            }
            location.add(v);
            maker.sendToLocation(location);
            location.subtract(v);
        }
        step += 3.0F;
        runSpeedTest();
    }
}
