package particles.brainsynder.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import particles.brainsynder.api.User;
import simple.brainsynder.commands.SubCommand;

public abstract class ParticleSub extends SubCommand {
    public abstract void run (User user, Player player, String[] args);

    @Override
    public void run(CommandSender sender, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("You must be a player to run this command.");
            return;
        }
        Player player = (Player)sender;
        User user = User.getUser(player);
        run (user, player, args);
    }
}
