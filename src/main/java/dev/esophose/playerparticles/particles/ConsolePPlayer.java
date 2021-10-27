package dev.esophose.playerparticles.particles;

import java.util.Map;
import java.util.UUID;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ConsolePPlayer extends PPlayer {

    private static final UUID uuid = UUID.fromString("ffffffff-ffff-ffff-ffff-ffffffffffff");

    public ConsolePPlayer(Map<String, ParticleGroup> particleGroups, Map<Integer, FixedParticleEffect> fixedParticles) {
        super(uuid, particleGroups, fixedParticles, false, false);
    }

    /**
     * @return the underlying ConsoleCommandSender who executed the command
     */
    @Override
    public CommandSender getUnderlyingExecutor() {
        return Bukkit.getConsoleSender();
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    public static UUID getUUID() {
        return uuid;
    }

}
