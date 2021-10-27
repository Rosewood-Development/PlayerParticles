package dev.esophose.playerparticles.particles;

import java.util.Collections;
import java.util.UUID;
import org.bukkit.command.CommandSender;

public class OtherPPlayer extends PPlayer {
    
    private final CommandSender sender;

    public OtherPPlayer(CommandSender sender) {
        super(UUID.randomUUID(), Collections.emptyMap(), Collections.emptyMap(), false, false);

        this.sender = sender;
    }
    
    public OtherPPlayer(CommandSender sender, PPlayer other) {
        super(other.getUniqueId(), other.getParticleGroups(), other.getFixedParticlesMap(), !other.canSeeParticles(), !other.canSeeOwnParticles());
        
        this.sender = sender;
    }

    /**
     * @return the underlying CommandSender who executed the command
     */
    @Override
    public CommandSender getUnderlyingExecutor() {
        return this.sender;
    }

}
