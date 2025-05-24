package dev.esophose.playerparticles.hook;

import dev.esophose.playerparticles.config.Settings;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
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
    @SuppressWarnings("unchecked")
    public static boolean isInAllowedRegion(Location location) {
        if (!enabled())
            return true;

        List<IWrappedRegion> regions = worldGuardWrapper.getRegions(location).stream()
                .sorted(Comparator.comparing(IWrappedRegion::getPriority))
                .collect(Collectors.toList());

        // Get the "player-particles" flag.
        // This will use the region priority to determine which one takes precedence.
        if (flagPlayerParticles != null) {
            for (IWrappedRegion region : regions) {
                Optional<WrappedState> flagState = region.getFlag(flagPlayerParticles);
                if (flagState.isPresent()) {
                    Object value = flagState.get();
                    // Fix a weird mismatch where the type in the compiler does not match the runtime type
                    if (value instanceof WrappedState && value == WrappedState.DENY) {
                        return false;
                    } else if (value instanceof Optional && ((Optional<WrappedState>) value).get() == WrappedState.DENY) {
                        return false;
                    }
                }
            }
        }

        // Legacy blocking by region name.
        List<String> disallowedRegionIds = Settings.WORLDGUARD_DISALLOWED_REGIONS.get();
        if (regions.stream().map(IWrappedRegion::getId).anyMatch(disallowedRegionIds::contains))
            return false;

        if (!Settings.WORLDGUARD_USE_ALLOWED_REGIONS.get())
            return true;

        List<String> allowedRegionIds = Settings.WORLDGUARD_ALLOWED_REGIONS.get();
        return regions.stream().map(IWrappedRegion::getId).anyMatch(allowedRegionIds::contains);
    }

}
