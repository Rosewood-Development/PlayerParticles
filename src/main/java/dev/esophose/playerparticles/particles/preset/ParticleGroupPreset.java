package dev.esophose.playerparticles.particles.preset;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleGroup;
import java.util.List;
import org.bukkit.Material;

public class ParticleGroupPreset {

    private final String displayName;
    private final Material guiIcon;
    private final int guiSlot;
    private final List<String> lore;
    private final String permission;
    private final boolean allowPermissionOverride;
    private final ParticleGroup group;
    
    public ParticleGroupPreset(String displayName, Material guiIcon, int guiSlot, List<String> lore, String permission, boolean allowPermissionOverride, ParticleGroup group) {
        this.displayName = displayName;
        this.guiIcon = guiIcon;
        this.guiSlot = guiSlot;
        this.lore = lore;
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
     * Gets the GUI slot for this particle group
     *
     * @return The GUI slot for this particle group
     */
    public int getGuiSlot() {
        return this.guiSlot;
    }

    /**
     * Gets the GUI lore for this particle group
     *
     * @return The GUI lore for this particle group
     */
    public List<String> getLore() {
        return this.lore;
    }
    
    /**
     * Checks if a player has permission to use this particle group
     * 
     * @param player The player to check
     * @return True if the player has permission
     */
    public boolean canPlayerUse(PPlayer player) {
        // If this particle group has a permission, does the player have it?
        if (!this.permission.isEmpty() && !PlayerParticles.getInstance().getManager(PermissionManager.class).hasPermission(player, this.permission))
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
