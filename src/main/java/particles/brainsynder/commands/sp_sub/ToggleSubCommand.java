package particles.brainsynder.commands.sp_sub;

import org.bukkit.entity.Player;
import particles.brainsynder.api.User;
import particles.brainsynder.commands.ParticleSub;
import simple.brainsynder.commands.annotations.ICommand;

@ICommand(name = "toggle")
public class ToggleSubCommand extends ParticleSub {
    @Override
    public void run(User user, Player player, String[] args) {
        if (user.getRunnable() == null) {
            player.sendMessage("§cYou have no running particle shape");
            return;
        }

        if (user.isRunning()) {
            user.stop();
            player.sendMessage("§cYou are already running a particle shape");
            return;
        }

        player.sendMessage("§aSuccessfully started your particle effect");
        user.start();
    }
}
