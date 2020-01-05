package dev.esophose.playerparticles.particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import org.bukkit.command.CommandSender;

public class OtherPPlayer extends PPlayer {
    
    private CommandSender sender;

    public OtherPPlayer(CommandSender sender) {
        super(UUID.randomUUID(), new HashMap<>(), new HashMap<>(), false);

        this.sender = sender;
    }
    
    public OtherPPlayer(CommandSender sender, PPlayer other) {
        super(other.getUniqueId(), other.getParticleGroups(), other.getFixedParticlesMap(), !other.canSeeParticles());
        
        this.sender = sender;
    }

    /**
     * @return The CommandSender where messages should be sent to
     */
    public CommandSender getMessageDestination() {
        return this.sender;
    }

}
