package particles.brainsynder.commands.sp_sub;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import particles.brainsynder.SimpleParticles;
import particles.brainsynder.api.Shape;
import particles.brainsynder.api.User;
import particles.brainsynder.cape.CapeMaker;
import particles.brainsynder.commands.CommandSimpleParticles;
import particles.brainsynder.commands.ParticleSub;
import particles.brainsynder.utils.Utilities;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.nms.IParticleSender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ICommand(
        name = "ghost",
        description = "Temporarily Sets a shape to run on an entity (EG: ArmorStand)",
        usage = "/simpleparticles ghost <shape> <particle> [data]"
)
public class GhostTestCMD extends ParticleSub {
    private CommandSimpleParticles particles;
    private static Map<String, BukkitRunnable> map = new HashMap<>();

    public GhostTestCMD(CommandSimpleParticles particles) {
        this.particles = particles;
        registerCompletion(1, particles.getShapes());
        registerCompletion(2, particles.getParticles());
        registerCompletion(3, (commandSender, list, s) -> {
            if (s.equalsIgnoreCase(ParticleMaker.Particle.BLOCK_DUST.fetchName())
                    || s.equalsIgnoreCase(ParticleMaker.Particle.BLOCK_CRACK.fetchName())
                    || s.equalsIgnoreCase(ParticleMaker.Particle.ITEM_CRACK.fetchName())
                    || s.equalsIgnoreCase(ParticleMaker.Particle.ITEM_TAKE.fetchName())) {
                list.addAll(particles.getMaterials());
                return true;
            }

            if (s.equalsIgnoreCase(ParticleMaker.Particle.REDSTONE.fetchName())) {
                list.addAll(particles.getColors());
                return true;
            }
            return false;
        });
    }

    @Override
    public void run(User user, Player player, String[] args) {
        if (args.length == 0) {
            sendUsage(player);
            return;
        }
        SimpleParticles core = particles.getSimpleParticles();
        Shape shape = core.getShapeManager().getShape(args[0]);
        if (shape == null) shape = core.getCustomManager().getCape(messageMaker(args, 0));
        if (shape == null) return;

        if (shape instanceof CapeMaker) {
            Entity entity = getEntity(player);
            if (entity == null) {
                player.sendMessage("§cYou are not looking at a block that has an entity on it");
                return;
            }
            String uuid = entity.getUniqueId().toString();
            if (map.containsKey(uuid)) {
                map.get(uuid).cancel();
                map.remove(uuid);
            }

            Shape finalShape = shape;
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!map.containsKey(uuid)) {
                        cancel();
                        return;
                    }

                    if (entity.isDead() || (!entity.isValid())) {
                        cancel();
                        return;
                    }

                    finalShape.run(entity.getLocation().clone());
                }
            };
            map.put(uuid, runnable);
            runnable.runTaskTimer(SimpleParticles.getProvidingPlugin(SimpleParticles.class), 0, shape.getTickSpeed());

            return;
        }
        if (args.length == 1) {
            sendUsage(player);
            return;
        }
        ParticleMaker.Particle particle = ParticleMaker.Particle.getByName(args[1]);
        if (particle == ParticleMaker.Particle.UNKNOWN) return;
        if (shape.blockedParticles().contains(particle)) {
            player.sendMessage("§cThis particle is not allowed to work with this effect/shape");
            return;
        }

        ParticleMaker maker = new ParticleMaker(particle, shape.getParticleCount(), shape.getOffsetX(), shape.getOffsetY(), shape.getOffsetZ());

        if (particle == ParticleMaker.Particle.BLOCK_DUST
                || particle == ParticleMaker.Particle.BLOCK_CRACK
                || particle == ParticleMaker.Particle.ITEM_CRACK
                || particle == ParticleMaker.Particle.ITEM_TAKE) {
            Material material = Material.STONE;

            if (args.length != 2) {
                material = Material.getMaterial(args[2]);
            }

            maker.setData(material);
        } else if (particle == ParticleMaker.Particle.REDSTONE) {
            Color color = Color.RED;

            if (args.length != 2) {
                color = Utilities.formatColor(args[2]);
            }

            maker.setDustOptions(new IParticleSender.DustOptions(color, 1F));
        }

        Entity entity = getEntity(player);
        if (entity == null) {
            player.sendMessage("§cYou are not looking at a block that has an entity on it");
            return;
        }
        String uuid = entity.getUniqueId().toString();
        if (map.containsKey(uuid)) {
            map.get(uuid).cancel();
            map.remove(uuid);
        }

        Shape finalShape1 = shape;
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                if (!map.containsKey(uuid)) {
                    cancel();
                    return;
                }

                if (entity.isDead() || (!entity.isValid())) {
                    cancel();
                    return;
                }

                finalShape1.run(entity.getLocation().clone(), maker);
                finalShape1.run(entity.getLocation().clone());
            }
        };
        map.put(uuid, runnable);
        runnable.runTaskTimer(SimpleParticles.getProvidingPlugin(SimpleParticles.class), 0, shape.getTickSpeed());
    }

    private Entity getEntity(Player player) {
        Block target = getTargetBlock(player);
        for (Entity e : target.getWorld().getNearbyEntities(target.getLocation(), 10, 10, 10)) {
            if (e instanceof Player) continue;
            if ((e.getLocation().getBlockX() == target.getLocation().getBlockX())
                    && (e.getLocation().getBlockZ() == target.getLocation().getBlockZ())) {
                return e;
            }
        }

        return null;
    }

    private Block getTargetBlock(Player player) {
        List<Block> lastTwoTargetBlocks = player.getLastTwoTargetBlocks(null, 100);
        return lastTwoTargetBlocks.get(1);
    }

    @Override
    public boolean canExecute(CommandSender sender) {
        return sender.hasPermission("simpleparticles.command.ghost");
    }
}
