package particles.brainsynder.commands.sp_sub;

import org.bukkit.entity.Player;
import particles.brainsynder.api.User;
import particles.brainsynder.commands.ParticleSub;
import simple.brainsynder.commands.annotations.ICommand;

@ICommand(name = "clear")
public class ClearSubCommand extends ParticleSub {
    @Override
    public void run(User user, Player player, String[] args) {
        if (user.getRunnable() == null) {
            player.sendMessage("§cYou have no running particle shape");
            return;
        }

        player.sendMessage("§aYour current shape/particle data was cleared");
        user.clear();
    }
}
