package particles.brainsynder.shape;

import org.bukkit.Location;
import org.json.simple.JSONObject;
import simple.brainsynder.api.ParticleMaker;

public class Orbit extends ShapeKey {
    private double step = 0;

    public Orbit() {
        super();
    }

    public Orbit(JSONObject json) {
        super(json);
    }

    @Override
    public void run(Location location, ParticleMaker maker) {
        runSpeedTest();
        location.add(0,0.1,0);
        double rad = Math.toRadians(step);
        double radius = 2.0;
        double cos = Math.cos(rad) * radius;
        double sin = Math.sin(rad) * radius;
        double tan = Math.tanh(rad) * radius;
        double cosin = Math.cos(rad) / Math.sin(rad) * radius;
        Location nv = location.clone().add(cos, 0, sin);
        Location so = location.clone().subtract(cos, 0, sin);
        Location no = location.clone().add(sin, 0, cos);
        Location sv = location.clone().subtract(sin, 0, cos);
        Location e1 = location.clone().add(tan, 0, cosin);
        Location e2 = location.clone().add(cosin, 0, tan);
        Location e3 = location.clone().subtract(tan, 0, cosin);
        Location e4 = location.clone().subtract(cosin, 0, tan);

        maker.sendToLocation(nv);
        maker.sendToLocation(so);
        maker.sendToLocation(no);
        maker.sendToLocation(sv);
        maker.sendToLocation(e1);
        maker.sendToLocation(e2);
        maker.sendToLocation(e3);
        maker.sendToLocation(e4);

        if (step == 360) step = 0;
        step++;
        runSpeedTest();
    }
}
