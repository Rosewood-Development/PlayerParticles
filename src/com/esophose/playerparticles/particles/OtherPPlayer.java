package com.esophose.playerparticles.particles;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.UUID;

public class OtherPPlayer extends PPlayer {
    
    private CommandSender sender;

    public OtherPPlayer(CommandSender sender) {
        super(UUID.randomUUID(), new ArrayList<>(), new ArrayList<>(), false);

        this.sender = sender;
    }
    
    public OtherPPlayer(CommandSender sender, PPlayer other) {
        super(other.getUniqueId(), other.getParticleGroups(), other.getFixedParticles(), !other.canSeeParticles());
        
        this.sender = sender;
    }
    
    public CommandSender getMessageDestination() {
        return this.sender;
    }

}
