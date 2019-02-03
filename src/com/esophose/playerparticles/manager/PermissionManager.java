package com.esophose.playerparticles.manager;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.manager.SettingManager.PSetting;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;

public class PermissionManager {
    
    private static final String PERMISSION_PREFIX = "playerparticles.";
    
    private enum PPermission {
        ALL("*"),
        
        EFFECT_ALL("effect.*"),
        EFFECT("effect"),
        
        STYLE_ALL("style.*"),
        STYLE("style"),
        
        FIXED("fixed"),
        FIXED_UNLIMITED("fixed.unlimited"),
        FIXED_CLEAR("fixed.clear"),
        
        RELOAD("reload"),
        
        PARTICLES_UNLIMITED("particles.unlimited"),
        GROUPS_UNLIMITED("groups.unlimited");
        
        private final String permissionString;
        
        private PPermission(String permissionString) {
            this.permissionString = permissionString;
        }
        
        /**
         * Checks if a Player has a PlayerParticles permission
         * 
         * @param p The Player
         * @return True if the Player has permission
         */
        public boolean check(Player p) {
            String permission = PERMISSION_PREFIX + this.permissionString;
            return p.hasPermission(permission);
        }
        
        /**
         * Checks if a Player has a PlayerParticles permission with a sub-permission
         * 
         * @param p The Player
         * @param subPermission The sub-permission
         * @return True if the Player has permission
         */
        public boolean check(Player p, String subPermission) {
            String permission = PERMISSION_PREFIX + this.permissionString + '.' + subPermission;
            return p.hasPermission(permission);
        }
    }
    
    private PermissionManager() {
        
    }
    
    /**
     * Checks if a player can use /ppo
     * 
     * @param sender The CommandSender to check
     * @return If the player can use /ppo
     */
    public static boolean canOverride(CommandSender sender) {
        if (!(sender instanceof Player)) return true;
        return PPermission.ALL.check((Player)sender);
    }
    
    /**
     * Checks if the given player has reached the max number of particles in their active group
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of particles in their active group
     */
    public static boolean hasPlayerReachedMaxParticles(PPlayer pplayer) {
        if (PPermission.ALL.check(pplayer.getPlayer())) return false;
        if (PPermission.PARTICLES_UNLIMITED.check(pplayer.getPlayer())) return false;
        return pplayer.getActiveParticles().size() >= PSetting.MAX_PARTICLES.getInt();
    }
    
    /**
     * Checks if the given player has reached the max number of saved particle groups
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of saved particle groups
     */
    public static boolean hasPlayerReachedMaxGroups(PPlayer pplayer) {
        if (PPermission.ALL.check(pplayer.getPlayer())) return false;
        if (PPermission.GROUPS_UNLIMITED.check(pplayer.getPlayer())) return false;
        return pplayer.getParticleGroups().size() - 1 >= PSetting.MAX_GROUPS.getInt();
    }
    
    /**
     * Checks if the given player is able to save groups
     * 
     * @param pplayer The player to check
     * @return If the player has permission to save groups
     */
    public static boolean canPlayerSaveGroups(PPlayer pplayer) {
        if (PPermission.ALL.check(pplayer.getPlayer())) return true;
        if (PPermission.GROUPS_UNLIMITED.check(pplayer.getPlayer())) return true;
        return PSetting.MAX_GROUPS.getInt() != 0;
    }
    
    /**
     * Checks if the given player has reached the max number of fixed effects
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of fixed effects
     */
    public static boolean hasPlayerReachedMaxFixedEffects(PPlayer pplayer) {
        if (PPermission.ALL.check(pplayer.getPlayer())) return false;
        if (PPermission.FIXED_UNLIMITED.check(pplayer.getPlayer())) return false;
        return pplayer.getFixedEffectIds().size() >= PSetting.MAX_FIXED_EFFECTS.getInt();
    }

    /**
     * Gets the max distance a fixed effect can be created from the player
     * 
     * @return The max distance a fixed effect can be created from the player
     */
    public static int getMaxFixedEffectCreationDistance() {
        return PSetting.MAX_FIXED_EFFECT_CREATION_DISTANCE.getInt();
    }
    
    /**
     * Gets the maximum number of particles a player is allowed to use
     * 
     * @param player The player to check
     * @return The maximum number of particles based on the config.yml value, or unlimited
     */
    public static int getMaxParticlesAllowed(Player player) {
        if (PPermission.ALL.check(player) || PPermission.PARTICLES_UNLIMITED.check(player)) return Integer.MAX_VALUE;
        return PSetting.MAX_PARTICLES.getInt();
    }

    /**
     * Checks if a world is disabled for particles to spawn in
     * 
     * @param world The world name to check
     * @return True if the world is disabled
     */
    public static boolean isWorldDisabled(String world) {
        return getDisabledWorlds().contains(world);
    }

    /**
     * Gets all the worlds that are disabled
     * 
     * @return All world names that are disabled
     */
    public static List<String> getDisabledWorlds() {
        return PSetting.DISABLED_WORLDS.getStringList();
    }

    /**
     * Checks if a player has permission to use an effect
     * 
     * @param player The player to check the permission for
     * @param effect The effect to check
     * @return True if the player has permission to use the effect
     */
    public static boolean hasEffectPermission(Player player, ParticleEffect effect) {
        if (PPermission.ALL.check(player) || PPermission.EFFECT_ALL.check(player)) return true;
        return PPermission.EFFECT.check(player, effect.getName());
    }

    /**
     * Checks if a player has permission to use a style
     * Always returns true for 'normal', a player needs at least one style to apply particles
     * 
     * @param player The player to check the permission for
     * @param style The style to check
     * @return If the player has permission to use the style
     */
    public static boolean hasStylePermission(Player player, ParticleStyle style) {
        if (style == DefaultStyles.NORMAL) return true;
        if (PPermission.ALL.check(player) || PPermission.STYLE_ALL.check(player)) return true;
        return PPermission.STYLE.check(player, style.getName());
    }

    /**
     * Gets a String List of all effect names a player has permission for
     * 
     * @param p The player to get effect names for
     * @return A String List of all effect names the given player has permission for
     */
    public static List<String> getEffectNamesUserHasPermissionFor(Player p) {
        List<String> list = new ArrayList<String>();
        for (ParticleEffect pe : ParticleEffect.getSupportedEffects())
            if (hasEffectPermission(p, pe)) 
                list.add(pe.getName());
        return list;
    }
    
    /**
     * Gets a String List of all style names a player has permission for
     * 
     * @param p The player to get style names for
     * @return A String List of all style names the given player has permission for
     */
    public static List<String> getStyleNamesUserHasPermissionFor(Player p) {
        List<String> list = new ArrayList<String>();
        for (ParticleStyle ps : ParticleStyleManager.getStyles())
            if (hasStylePermission(p, ps)) 
                list.add(ps.getName());
        return list;
    }

    /**
     * Gets a String List of all fixable style names a player has permission for
     * 
     * @param p The player to get style names for
     * @return A String List of all fixable style names the given player has permission for
     */
    public static List<String> getFixableStyleNamesUserHasPermissionFor(Player p) {
        List<String> list = new ArrayList<String>();
        for (ParticleStyle ps : ParticleStyleManager.getStyles())
            if (ps.canBeFixed() && hasStylePermission(p, ps)) 
                list.add(ps.getName());
        return list;
    }
    
    /**
     * Gets a List of all effects a player has permission for
     * 
     * @param p The player to get effects for
     * @return A List of all effects the given player has permission for
     */
    public static List<ParticleEffect> getEffectsUserHasPermissionFor(Player p) {
        List<ParticleEffect> list = new ArrayList<ParticleEffect>();
        for (ParticleEffect pe : ParticleEffect.getSupportedEffects())
            if (hasEffectPermission(p, pe)) 
                list.add(pe);
        return list;
    }

    /**
     * Gets a List of all styles a player has permission for
     * 
     * @param p The player to get styles for
     * @return A List of all styles the given player has permission for
     */
    public static List<ParticleStyle> getStylesUserHasPermissionFor(Player p) {
        List<ParticleStyle> list = new ArrayList<ParticleStyle>();
        for (ParticleStyle ps : ParticleStyleManager.getStyles())
            if (hasStylePermission(p, ps)) 
                list.add(ps);
        return list;
    }

    /**
     * Checks if a player has permission to created fixed effects
     * 
     * @param player The player to check the permission for
     * @return True if the player has permission
     */
    public static boolean canUseFixedEffects(Player player) {
        return PPermission.ALL.check(player) || PPermission.FIXED.check(player);
    }
    
    /**
     * Checks if a player has permission to clear fixed effects
     * 
     * @param player The player to check the permission for
     * @return True if the player has permission to use /pp fixed clear
     */
    public static boolean canClearFixedEffects(Player player) {
        return PPermission.ALL.check(player) || PPermission.FIXED_CLEAR.check(player);
    }
    
    /**
     * Checks if a player has permission to use /pp reload
     * 
     * @param player The player to check the permission for
     * @return True if the player has permission to reload the plugin's settings
     */
    public static boolean canReloadPlugin(Player player) {
        return PPermission.ALL.check(player) || PPermission.RELOAD.check(player);
    }

}
