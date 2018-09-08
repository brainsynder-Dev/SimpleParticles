package particles.brainsynder.api;

import org.bukkit.craftbukkit.libs.jline.internal.Nullable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import particles.brainsynder.SimpleParticles;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.nms.IParticleSender;
import simple.brainsynder.reflection.FieldAccessor;

import java.util.HashMap;
import java.util.Map;

public class User {
    private static Map<String, User> users = new HashMap<>();

    public static User getUser (Player player) {
        if (users.containsKey(player.getUniqueId().toString())) return users.get(player.getUniqueId().toString());
        User user = new User (player);
        users.put(player.getUniqueId().toString(), user);
        return users.get(player.getUniqueId().toString());
    }

    private ParticleMaker maker;
    private ParticleMaker.Particle particle;
    private Shape shape;
    private ItemStack item;
    private boolean running = false;
    private IParticleSender.DustOptions dustOptions;
    private BukkitRunnable runnable;
    private Player player;

    public User (Player player){
        this.player = player;
    }

    /**
     * Initializes the shape so it has less stuff to run each time
     */
    public void initShape (ParticleMaker.Particle particle) {
        initShape(particle, null, null);
    }
    public void initShape (ParticleMaker.Particle particle, @Nullable ItemStack item, @Nullable IParticleSender.DustOptions dustOptions) {
        if (particle == null) throw new NullPointerException("Particle can not be null");
        if (shape == null) throw new NullPointerException("Shape can not be null");

        this.particle = particle;
        this.item = item;
        this.dustOptions = dustOptions;

        maker = new ParticleMaker(particle, shape.getParticleCount(), shape.getOffsetX(), shape.getOffsetY(), shape.getOffsetZ());
        if (item != null) maker.setData(item);
        if (dustOptions != null) maker.setDustOptions(dustOptions);
        if (runnable != null) runnable.cancel();
        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                shape.run(player, maker);
            }
        };
    }

    public void start () {
        if (shape == null) return;
        if (runnable == null) return;
        running = true;
        runnable.runTaskTimer(SimpleParticles.getProvidingPlugin(SimpleParticles.class), 0, shape.getTickSpeed());
    }

    public void stop () {
        if (runnable == null) return;
        running = false;
        runnable.cancel();

        FieldAccessor<BukkitTask> task = FieldAccessor.getField(BukkitRunnable.class, "task", BukkitTask.class);
        task.set(runnable, null);
    }

    public void clear () {
        if (runnable != null) runnable.cancel();
        runnable = null;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public @Nullable BukkitRunnable getRunnable() {
        return runnable;
    }
    public ParticleMaker.Particle getParticle() {
        return particle;
    }
    public Shape getShape() {
        return shape;
    }
    public @Nullable IParticleSender.DustOptions getDustOptions() {
        return dustOptions;
    }
    public @Nullable ItemStack getItem() {
        return item;
    }
    public Player getPlayer() {
        return player;
    }
    public ParticleMaker getMaker() {
        return maker;
    }
    public boolean isRunning() {
        return running;
    }
}
