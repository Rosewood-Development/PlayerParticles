package dev.esophose.playerparticles.particles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.PermissionManager;
import org.bukkit.Material;

public class ParticleGroupPreset {

    private String displayName;
    private Material guiIcon;
    private String permission;
    private boolean allowPermissionOverride;
    private ParticleGroup group;
    
    public ParticleGroupPreset(String displayName, Material guiIcon, String permission, boolean allowPermissionOverride, ParticleGroup group) {
        this.displayName = displayName;
        this.guiIcon = guiIcon;
        this.permission = permission;
        this.allowPermissionOverride = allowPermissionOverride;
        this.group = group;
    }
    
    /**
     * Gets the display name for this preset group for the GUI
     * 
     * @return The display name of the preset group
     */
    public String getDisplayName() {
        return this.displayName;
    }
    
    /**
     * Gets the GUI icon for this particle group
     * 
     * @return The GUI icon for this particle group
     */
    public Material getGuiIcon() {
        return this.guiIcon;
    }
    
    /**
     * Checks if a player has permission to use this particle group
     * 
     * @param player The player to check
     * @return True if the player has permission
     */
    public boolean canPlayerUse(PPlayer player) {
        // If this particle group has a permission, does the player have it?
        if (!this.permission.isEmpty() && PlayerParticles.getInstance().getManager(PermissionManager.class).hasPermission(player, this.permission))
            return false;
        
        // If allowPermissionOverride is true, always let the player apply this group
        if (this.allowPermissionOverride) 
            return true;
        
        return this.group.canPlayerUse(player);
    }
    
    /**
     * Gets the underlying ParticleGroup
     * 
     * @return The underlying ParticleGroup
     */
    public ParticleGroup getGroup() {
        return this.group;
    }

}
