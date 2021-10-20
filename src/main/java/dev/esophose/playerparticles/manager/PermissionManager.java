package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.OtherPPlayer;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.styles.ParticleStyle;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permissible;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionAttachmentInfo;
import org.bukkit.plugin.PluginManager;

public class PermissionManager extends Manager {
    
    private static final String PERMISSION_PREFIX = "playerparticles.";

    private enum PPermission {
        EFFECT("effect"),
        STYLE("style"),
        
        FIXED("fixed"),
        FIXED_MAX("fixed.max"),
        FIXED_UNLIMITED("fixed.unlimited"),
        FIXED_CLEAR("fixed.clear"),
        FIXED_TELEPORT("fixed.teleport"),

        RELOAD("reload"),
        OVERRIDE("override"),
        RESET_OTHERS("reset.others"),

        GUI("gui"),

        PARTICLES_MAX("particles.max"),
        PARTICLES_UNLIMITED("particles.unlimited"),

        GROUPS_MAX("groups.max"),
        GROUPS_UNLIMITED("groups.unlimited"),

        WORLDGUARD_BYPASS("worldguard.bypass");
        
        private final String permissionString;
        
        PPermission(String permissionString) {
            this.permissionString = permissionString;
        }
        
        /**
         * Checks if a Permissible has a PlayerParticles permission
         * 
         * @param p The Permissible
         * @return True if the Player has permission
         */
        public boolean check(Permissible p) {
            String permission = PERMISSION_PREFIX + this.permissionString;
            return p.hasPermission(permission);
        }
        
        /**
         * Checks if a Permissible has a PlayerParticles permission with a sub-permission
         * 
         * @param p The Permissible
         * @param subPermission The sub-permission
         * @return True if the Player has permission
         */
        public boolean check(Permissible p, String subPermission) {
            String permission = PERMISSION_PREFIX + this.permissionString + '.' + subPermission;
            return p.hasPermission(permission);
        }

        @Override
        public String toString() {
            return PERMISSION_PREFIX + this.permissionString;
        }
    }
    
    public PermissionManager(PlayerParticles playerParticles) {
        super(playerParticles);

        Bukkit.getScheduler().runTaskLater(playerParticles, () -> {
            try {
                // Register plugin permissions to Bukkit
                PluginManager pluginManager = Bukkit.getPluginManager();

                Set<Permission> allPermissions = new HashSet<>();

                // Effects
                Map<String, Boolean> effectPermissions = new HashMap<>();
                for (ParticleEffect effect : ParticleEffect.values()) {
                    if (!effect.isSupported())
                        continue;

                    Permission permission = new Permission("playerparticles.effect." + effect.getInternalName());
                    pluginManager.addPermission(permission);
                    effectPermissions.put(permission.getName(), true);
                }

                // Effects Wildcard
                allPermissions.add(new Permission("playerparticles.effect.*", effectPermissions));

                // Styles
                Map<String, Boolean> stylePermissions = new HashMap<>();
                for (ParticleStyle style : this.playerParticles.getManager(ParticleStyleManager.class).getStylesWithDisabled()) {
                    Permission permission = new Permission("playerparticles.style." + style.getInternalName());
                    pluginManager.addPermission(permission);
                    stylePermissions.put(permission.getName(), true);
                }

                // Styles Wildcard
                allPermissions.add(new Permission("playerparticles.style.*", stylePermissions));

                // Fixed
                pluginManager.addPermission(new Permission("playerparticles.fixed"));
                pluginManager.addPermission(new Permission("playerparticles.fixed.max"));
                pluginManager.addPermission(new Permission("playerparticles.fixed.unlimited"));
                pluginManager.addPermission(new Permission("playerparticles.fixed.clear"));
                pluginManager.addPermission(new Permission("playerparticles.fixed.teleport"));

                // Misc
                pluginManager.addPermission(new Permission("playerparticles.reload"));
                pluginManager.addPermission(new Permission("playerparticles.override"));
                pluginManager.addPermission(new Permission("playerparticles.reset.others"));
                pluginManager.addPermission(new Permission("playerparticles.gui"));

                pluginManager.addPermission(new Permission("playerparticles.particles.max"));
                pluginManager.addPermission(new Permission("playerparticles.particles.unlimited"));

                pluginManager.addPermission(new Permission("playerparticles.groups.max"));
                pluginManager.addPermission(new Permission("playerparticles.groups.unlimited"));

                pluginManager.addPermission(new Permission("playerparticles.worldguard.bypass"));

                // Register all non-child permissions
                Map<String, Boolean> childPermissions = new HashMap<>();
                for (Permission permission : allPermissions) {
                    pluginManager.addPermission(permission);
                    childPermissions.put(permission.getName(), true);
                }

                // Register all permissions as a child to the global plugin permission
                pluginManager.addPermission(new Permission("playerparticles.*", childPermissions));
            } catch (Exception e) {
                playerParticles.getLogger().warning("Failed to register permissions dynamically. Did you load PlayerParticles through means other than a restart or reload?");
            }
        }, 2L);
    }

    @Override
    public void reload() {

    }

    @Override
    public void disable() {

    }

    /**
     * Checks if the given player has the given permission
     *
     * @param pplayer The player to check
     * @param permission The permission to check
     * @return true if the player has the permission, otherwise false
     */
    public boolean hasPermission(PPlayer pplayer, String permission) {
        return pplayer.getUnderlyingExecutor().hasPermission(permission);
    }

    /**
     * Checks if the given player has reached the max number of particles in their active group
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of particles in their active group
     */
    public boolean hasPlayerReachedMaxParticles(PPlayer pplayer) {
        if (PPermission.PARTICLES_UNLIMITED.check(pplayer.getUnderlyingExecutor()))
            return false;

        PPlayer executor = this.getUnderlyingExecutorAsPPlayer(pplayer);
        if (executor != pplayer)
            return false;

        return pplayer.getActiveParticles().size() >= this.getPermissionAmount(pplayer.getUnderlyingExecutor(), PPermission.PARTICLES_MAX, Setting.MAX_PARTICLES.getInt());
    }
    
    /**
     * Checks if the given player has reached the max number of saved particle groups
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of saved particle groups
     */
    public boolean hasPlayerReachedMaxGroups(PPlayer pplayer) {
        if (PPermission.GROUPS_UNLIMITED.check(pplayer.getUnderlyingExecutor()))
            return false;

        PPlayer executor = this.getUnderlyingExecutorAsPPlayer(pplayer);
        if (executor != pplayer)
            return false;

        return executor.getParticleGroups().size() - 1 >= this.getPermissionAmount(pplayer.getUnderlyingExecutor(), PPermission.GROUPS_MAX, Setting.MAX_GROUPS.getInt());
    }
    
    /**
     * Checks if the given player is able to save groups
     * 
     * @param pplayer The player to check
     * @return If the player has permission to save groups
     */
    public boolean canPlayerSaveGroups(PPlayer pplayer) {
        if (PPermission.GROUPS_UNLIMITED.check(pplayer.getUnderlyingExecutor()))
            return true;

        return this.getPermissionAmount(pplayer.getUnderlyingExecutor(), PPermission.GROUPS_MAX, Setting.MAX_GROUPS.getInt()) != 0;
    }
    
    /**
     * Checks if the given player has reached the max number of fixed effects
     * 
     * @param pplayer The player to check
     * @return If the player has reached the max number of fixed effects
     */
    public boolean hasPlayerReachedMaxFixedEffects(PPlayer pplayer) {
        if (PPermission.FIXED_UNLIMITED.check(pplayer.getUnderlyingExecutor()))
            return false;

        PPlayer executor = this.getUnderlyingExecutorAsPPlayer(pplayer);
        if (executor != pplayer)
            return false;

        return pplayer.getFixedEffectIds().size() >= this.getPermissionAmount(pplayer.getUnderlyingExecutor(), PPermission.FIXED_MAX, Setting.MAX_FIXED_EFFECTS.getInt());
    }

    /**
     * Gets the max distance a fixed effect can be created from the player
     * 
     * @return The max distance a fixed effect can be created from the player
     */
    public int getMaxFixedEffectCreationDistance() {
        return Setting.MAX_FIXED_EFFECT_CREATION_DISTANCE.getInt();
    }
    
    /**
     * Gets the maximum number of particles a player is allowed to use
     * 
     * @param pplayer The pplayer to check
     * @return The maximum number of particles based on the config.yml value, or unlimited
     */
    public int getMaxParticlesAllowed(PPlayer pplayer) {
        if (PPermission.PARTICLES_UNLIMITED.check(pplayer.getUnderlyingExecutor()))
            return Integer.MAX_VALUE;

        PPlayer executor = this.getUnderlyingExecutorAsPPlayer(pplayer);
        if (executor != pplayer)
            return Integer.MAX_VALUE;

        return this.getPermissionAmount(pplayer.getUnderlyingExecutor(), PPermission.PARTICLES_MAX, Setting.MAX_PARTICLES.getInt());
    }

    /**
     * Checks if a world is enabled for particles to spawn in
     * 
     * @param world The world name to check
     * @return True if the world is disabled
     */
    public boolean isWorldEnabled(String world) {
        return !this.getDisabledWorlds().contains(world);
    }

    /**
     * Gets all the worlds that are disabled
     * 
     * @return All world names that are disabled
     */
    public List<String> getDisabledWorlds() {
        return Setting.DISABLED_WORLDS.getStringList();
    }

    /**
     * Checks if a player can reset another offline player's particles
     *
     * @param player The player to check the permission for
     * @return True if the player has permission, otherwise false
     */
    public boolean canResetOthers(PPlayer player) {
        return PPermission.RESET_OTHERS.check(player.getUnderlyingExecutor());
    }

    /**
     * Checks if a player has permission to use an effect
     * 
     * @param player The player to check the permission for
     * @param effect The effect to check
     * @return True if the player has permission to use the effect
     */
    public boolean hasEffectPermission(PPlayer player, ParticleEffect effect) {
        return PPermission.EFFECT.check(player.getUnderlyingExecutor(), effect.getInternalName());
    }

    /**
     * Checks if a player has permission to use a style
     * Always returns true for 'normal', a player needs at least one style to apply particles
     * 
     * @param player The player to check the permission for
     * @param style The style to check
     * @return If the player has permission to use the style
     */
    public boolean hasStylePermission(PPlayer player, ParticleStyle style) {
        return PPermission.STYLE.check(player.getUnderlyingExecutor(), style.getInternalName());
    }

    /**
     * Gets a String List of all effect names a player has permission for
     * 
     * @param p The player to get effect names for
     * @return A String List of all effect names the given player has permission for
     */
    public List<String> getEffectNamesUserHasPermissionFor(PPlayer p) {
        List<String> list = new ArrayList<>();
        for (ParticleEffect pe : ParticleEffect.getEnabledEffects())
            if (this.hasEffectPermission(p, pe))
                list.add(pe.getName());
        return list;
    }
    
    /**
     * Gets a String List of all style names a player has permission for
     * 
     * @param p The player to get style names for
     * @return A String List of all style names the given player has permission for
     */
    public List<String> getStyleNamesUserHasPermissionFor(PPlayer p) {
        List<String> list = new ArrayList<>();
        for (ParticleStyle ps : this.playerParticles.getManager(ParticleStyleManager.class).getStyles())
            if (this.hasStylePermission(p, ps))
                list.add(ps.getName());
        return list;
    }

    /**
     * Gets a String List of all fixable style names a player has permission for
     * 
     * @param p The player to get style names for
     * @return A String List of all fixable style names the given player has permission for
     */
    public List<String> getFixableStyleNamesUserHasPermissionFor(PPlayer p) {
        List<String> list = new ArrayList<>();
        for (ParticleStyle ps : this.playerParticles.getManager(ParticleStyleManager.class).getStyles())
            if (ps.canBeFixed() && this.hasStylePermission(p, ps))
                list.add(ps.getName());
        return list;
    }
    
    /**
     * Gets a List of all effects a player has permission for
     * 
     * @param p The player to get effects for
     * @return A List of all effects the given player has permission for
     */
    public List<ParticleEffect> getEffectsUserHasPermissionFor(PPlayer p) {
        List<ParticleEffect> list = new ArrayList<>();
        for (ParticleEffect pe : ParticleEffect.getEnabledEffects())
            if (this.hasEffectPermission(p, pe))
                list.add(pe);
        return list;
    }

    /**
     * Gets a List of all styles a player has permission for
     * 
     * @param p The player to get styles for
     * @return A List of all styles the given player has permission for
     */
    public List<ParticleStyle> getStylesUserHasPermissionFor(PPlayer p) {
        List<ParticleStyle> list = new ArrayList<>();
        for (ParticleStyle ps : this.playerParticles.getManager(ParticleStyleManager.class).getStyles())
            if (this.hasStylePermission(p, ps))
                list.add(ps);
        return list;
    }

    /**
     * Checks if a player has permission to created fixed effects
     * 
     * @param player The player to check the permission for
     * @return True if the player has permission
     */
    public boolean canUseFixedEffects(PPlayer player) {
        return PPermission.FIXED.check(player.getUnderlyingExecutor());
    }
    
    /**
     * Checks if a player has permission to clear fixed effects
     * 
     * @param player The player to check the permission for
     * @return True if the player has permission to use /pp fixed clear
     */
    public boolean canClearFixedEffects(PPlayer player) {
        return PPermission.FIXED_CLEAR.check(player.getUnderlyingExecutor());
    }

    /**
     * Checks if a player has permission to teleport to fixed effects
     *
     * @param player The player to check the permission for
     * @return True if the player has permission to use /pp fixed teleport
     */
    public boolean canTeleportToFixedEffects(PPlayer player) {
        return PPermission.FIXED_TELEPORT.check(player.getUnderlyingExecutor());
    }

    /**
     * Checks if a player has permission to open the GUI
     *
     * @param player The player to check the permission for
     * @return True if the player has permission to open the GUI
     */
    public boolean canOpenGui(PPlayer player) {
        return !Setting.GUI_REQUIRE_PERMISSION.getBoolean() || PPermission.GUI.check(player.getUnderlyingExecutor());
    }
    
    /**
     * Checks if a player has permission to use /pp reload
     * 
     * @param sender The sender to check the permission for
     * @return True if the sender has permission to reload the plugin's settings
     */
    public boolean canReloadPlugin(CommandSender sender) {
        return PPermission.RELOAD.check(sender);
    }

    /**
     * Checks if a player can use /ppo
     *
     * @param sender The CommandSender to check
     * @return If the sender can use /ppo
     */
    public boolean canOverride(CommandSender sender) {
        if (this.isConsole(sender))
            return true;

        return PPermission.OVERRIDE.check(sender);
    }

    /**
     * Checks if a player has the WorldGuard bypass permission
     *
     * @param player The Player to check
     * @return If the player has the WorldGuard bypass permission
     */
    public boolean hasWorldGuardBypass(Player player) {
        return PPermission.WORLDGUARD_BYPASS.check(player);
    }

    private boolean isConsole(PPlayer pplayer) {
        return this.isConsole(pplayer.getUnderlyingExecutor());
    }

    private boolean isConsole(CommandSender sender) {
        return sender instanceof ConsoleCommandSender;
    }

    private PPlayer getUnderlyingExecutorAsPPlayer(PPlayer pplayer) {
        if (pplayer instanceof OtherPPlayer) {
            OtherPPlayer other = (OtherPPlayer) pplayer;
            CommandSender executor = other.getUnderlyingExecutor();
            if (this.isConsole(executor))
                return null;
            Player executingPlayer = (Player) executor;
            return this.playerParticles.getManager(DataManager.class).getPPlayer(executingPlayer.getUniqueId());
        }
        return pplayer;
    }

    private int getPermissionAmount(Permissible permissible, PPermission permission, int lowerBound) {
        int amount = lowerBound;
        for (PermissionAttachmentInfo info : permissible.getEffectivePermissions()) {
            String target = info.getPermission().toLowerCase();
            if (target.startsWith(permission.toString()) && info.getValue()) {
                try {
                    amount = Math.max(amount, Integer.parseInt(target.substring(target.lastIndexOf('.') + 1)));
                } catch (NumberFormatException ignored) { }
            }
        }
        return amount;
    }

}
