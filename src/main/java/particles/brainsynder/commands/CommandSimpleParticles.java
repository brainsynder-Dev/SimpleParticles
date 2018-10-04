package particles.brainsynder.commands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import particles.brainsynder.SimpleParticles;
import particles.brainsynder.api.Shape;
import particles.brainsynder.cape.CapeMaker;
import particles.brainsynder.commands.sp_sub.ClearSubCommand;
import particles.brainsynder.commands.sp_sub.GhostTestCMD;
import particles.brainsynder.commands.sp_sub.SetSubCommand;
import particles.brainsynder.commands.sp_sub.ToggleSubCommand;
import simple.brainsynder.api.ParticleMaker;
import simple.brainsynder.commands.ParentCommand;
import simple.brainsynder.commands.annotations.ICommand;
import simple.brainsynder.utils.ServerVersion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ICommand(
        name = "simpleparticles",
        alias = {"sp", "particles"}
)
public class CommandSimpleParticles extends ParentCommand {
    private SimpleParticles simpleParticles;
    public CommandSimpleParticles (SimpleParticles particles) {
        this.simpleParticles = particles;
        registerSub(new ClearSubCommand());
        registerSub(new SetSubCommand(this));
        registerSub(new GhostTestCMD(this));
        registerSub(new ToggleSubCommand());
    }

    @Override
    public void run(CommandSender sender, String[] args) {
        sendHelp(sender, false);
    }

    public List<String> getShapes () {
        List<String> list = new ArrayList<>();
        for (Shape type : simpleParticles.getShapeManager().getShapes()) {
            if (type == null) continue;
            if (!type.isEnabled()) continue;
            list.add(type.getName());
        }
        for (CapeMaker type : simpleParticles.getCustomManager().getCapes()) {
            if (type == null) continue;
            if (!type.isEnabled()) continue;
            list.add(type.getName());
        }
        return list;
    }

    public SimpleParticles getSimpleParticles() {
        return simpleParticles;
    }

    public List<String> getParticles() {
        List<String> list = new ArrayList<>();
        for (ParticleMaker.Particle type : ParticleMaker.Particle.values()) {
            if (!type.isCompatable()) continue;
            list.add(type.fetchName());
        }
        return list;
    }

    public List<String> getColors () {
        return Arrays.asList(
                "255,255,255", // WHITE
                "192,192,192", // SILVER
                "128,128,128", // GRAY
                "0,0,0", // BLACK
                "255,0,0", // RED
                "128,0,0", // MAROON
                "255,255,0", // YELLOW
                "128,128,0", // OLIVE
                "0,255,0", // LIME
                "0,128,0", // GREEN
                "0,255,255", // AQUA
                "0,128,128", // TEAL
                "0,0,255", // BLUE
                "0,0,128", // NAVY
                "255,0,255", // FUCHSIA
                "128,0,128", // PURPLE
                "255,165,0"  // ORANGE
        );
    }

    public List<String> getMaterials () {
        List<String> list = new ArrayList<>();
        for (Material type : Material.values()) {
            if (type.name().startsWith("LEGACY_")
                    && (ServerVersion.getVersion().getIntVersion() >= ServerVersion.v1_13_R1.getIntVersion())) continue;
            list.add(type.name());
        }
        return list;
    }
}
