package dev.esophose.playerparticles.hook;

import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.flag.IWrappedFlag;
import org.codemc.worldguardwrapper.flag.WrappedState;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class WorldGuardHook {

    private static WorldGuardWrapper worldGuardWrapper;
    private static IWrappedFlag<WrappedState> flagPlayerParticles;

    /**
     * Initializes the WorldGuard hook.
     * Must be called during onLoad, or else WorldGuard prevents flag registration.
     */
    public static void initialize() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null)
            return;
        
        worldGuardWrapper = WorldGuardWrapper.getInstance();
        flagPlayerParticles = worldGuardWrapper.registerFlag("player-particles", WrappedState.class, WrappedState.ALLOW).orElse(null);
    }
    
    /**
     * @return true if WorldGuard is enabled, otherwise false
     */
    public static boolean enabled() {
        return worldGuardWrapper != null;
    }

    /**
     * Checks if a location is in a region that allows particles to spawn
     *
     * @param location The location to check
     * @return true if the location is in an allowed region, otherwise false
     */
    public static boolean isInAllowedRegion(Location location) {
        if (!enabled())
            return true;

        Set<IWrappedRegion> regions = worldGuardWrapper.getRegions(location);

        // Get the "player-particles" flag.
        // This will use the region priority to determine which one takes precedence.
        if (flagPlayerParticles != null) {
            Optional<WrappedState> flagState = regions.stream()
                    .sorted(Comparator.comparingInt(IWrappedRegion::getPriority))
                    .map(region -> region.getFlag(flagPlayerParticles))
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .findFirst();

            if (flagState.isPresent())
                return flagState.get() == WrappedState.ALLOW;
        }

        // Legacy blocking by region name.
        List<String> disallowedRegionIds = Setting.WORLDGUARD_DISALLOWED_REGIONS.getStringList();
        if (regions.stream().map(IWrappedRegion::getId).anyMatch(disallowedRegionIds::contains))
            return false;

        if (!Setting.WORLDGUARD_USE_ALLOWED_REGIONS.getBoolean())
            return true;

        List<String> allowedRegionIds = Setting.WORLDGUARD_ALLOWED_REGIONS.getStringList();
        return regions.stream().map(IWrappedRegion::getId).anyMatch(allowedRegionIds::contains);
    }

}
