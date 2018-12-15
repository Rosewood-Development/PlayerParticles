package com.esophose.playerparticles.particles;

import org.bukkit.command.CommandSender;

public class OtherPPlayer extends PPlayer {
    
    private CommandSender sender;
    
    public OtherPPlayer(CommandSender sender, PPlayer other) {
        super(other.getUniqueId(), other.getParticleGroups(), other.getFixedParticles(), !other.canSeeParticles());
        
        this.sender = sender;
    }
    
    public CommandSender getMessageDestination() {
        return this.sender;
    }

}
