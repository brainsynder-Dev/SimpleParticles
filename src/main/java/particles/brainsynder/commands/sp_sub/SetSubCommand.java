package particles.brainsynder.commands.sp_sub;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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

@ICommand(
        name = "set",
        usage = "/simpleparticles set <shape> <particle> [data]"
)
public class SetSubCommand extends ParticleSub {
    private CommandSimpleParticles particles;
    public SetSubCommand (CommandSimpleParticles particles) {
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

            if (s.equalsIgnoreCase(ParticleMaker.Particle.REDSTONE.fetchName())){
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
            user.setShape(shape);
            if (user.isRunning()) user.stop();
            user.initShape(ParticleMaker.Particle.REDSTONE, null, null);
            user.start();
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
        user.setShape(shape);
        if (user.isRunning()) user.stop();

        if (particle == ParticleMaker.Particle.BLOCK_DUST
                || particle == ParticleMaker.Particle.BLOCK_CRACK
                || particle == ParticleMaker.Particle.ITEM_CRACK
                || particle == ParticleMaker.Particle.ITEM_TAKE) {
            Material material = Material.STONE;

            if (args.length != 2) {
                material = Material.getMaterial(args[2]);
            }

            user.initShape(particle, new ItemStack(material), null);
            user.start();
            return;
        }

        if (particle == ParticleMaker.Particle.REDSTONE) {
            Color color = Color.RED;

            if (args.length != 2) {
                color = Utilities.formatColor(args[2]);
            }

            user.initShape(particle, null, new IParticleSender.DustOptions(color, 1.0F));
            user.start();
            return;
        }
        user.initShape(particle, null, null);
        user.start();
    }
}
