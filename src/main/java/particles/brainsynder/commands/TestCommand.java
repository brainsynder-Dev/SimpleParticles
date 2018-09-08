package particles.brainsynder.commands;

import org.bukkit.Color;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import particles.brainsynder.SimpleParticles;
import particles.brainsynder.api.Shape;
import particles.brainsynder.api.User;
import particles.brainsynder.utils.Utilities;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.commands.ParentCommand;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.nms.IParticleSender;

@ICommand(name = "sp_test")
public class TestCommand extends ParentCommand {
    @Override
    public void run(CommandSender sender, String[] args) {
        SimpleParticles particles = SimpleParticles.getPlugin(SimpleParticles.class);
        Shape shape = particles.getShapeManager().getShape("circle");
        User user = User.getUser((Player) sender);
        user.setShape(shape);
        Color color = Color.RED;

        if (args.length != 0) {
            color = Utilities.formatColor(args[0]);
        }

        if (user.getRunnable() == null) {
            sender.sendMessage("§7Test(Circle) has been: §e§lINITIALIZED");
            user.initShape(ParticleMaker.Particle.REDSTONE, null, new IParticleSender.DustOptions(color, 1.5F));
        } else {
            if (user.isRunning()) {
                user.stop();
                sender.sendMessage("§7Test(Circle) has been: §c§lDISABLED");
                return;
            }
            sender.sendMessage("§7Test(Circle) has been: §a§lENABLED");
            user.start();
        }
    }
}
