package dev.esophose.playerparticles.hook;

import java.util.ArrayList;
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
    private static IWrappedFlag<WrappedState> flagPlayerParticlesLimited;

    /**
     * Initializes the WorldGuard hook.
     * Must be called during onLoad, or else WorldGuard prevents flag registration.
     */
    public static void initialize() {
        if (Bukkit.getPluginManager().getPlugin("WorldGuard") == null)
            return;
        
        worldGuardWrapper = WorldGuardWrapper.getInstance();
        flagPlayerParticles = worldGuardWrapper.registerFlag("player-particles", WrappedState.class, WrappedState.ALLOW).orElse(null);
        flagPlayerParticlesLimited = worldGuardWrapper.registerFlag("player-particles-limited", WrappedState.class, WrappedState.ALLOW).orElse(null);
    }
    
    /**
     * @return true if WorldGuard is enabled, otherwise false
     */
    public static boolean enabled() {
        return worldGuardWrapper != null;
    }

    /**
     * Result of a combined region status check.
     * Avoids calling getRegions() twice for each player.
     */
    public static class RegionStatus {
        public final boolean allowed;
        public final boolean limited;

        public RegionStatus(boolean allowed, boolean limited) {
            this.allowed = allowed;
            this.limited = limited;
        }
    }

    /**
     * Checks both allowed and limited region flags in a single pass.
     * This is more efficient than calling isInAllowedRegion() and isInLimitedRegion() separately
     * because it only fetches and sorts regions once.
     *
     * @param location The location to check
     * @return A RegionStatus containing both allowed and limited states
     */
    @SuppressWarnings("unchecked")
    public static RegionStatus getRegionStatuses(Location location) {
        boolean allowed = true;
        boolean limited = false;

        if (!enabled())
            return new RegionStatus(allowed, limited);

        // Only fetch regions if at least one flag is registered
        if (flagPlayerParticles == null && flagPlayerParticlesLimited == null)
            return new RegionStatus(allowed, limited);

        // Fetch regions once and sort without streams to avoid unnecessary allocations
        Set<IWrappedRegion> regionSet = worldGuardWrapper.getRegions(location);
        List<IWrappedRegion> regions = new ArrayList<>(regionSet);
        regions.sort(Comparator.comparingInt(IWrappedRegion::getPriority));

        // Check "player-particles" flag
        if (flagPlayerParticles != null) {
            for (IWrappedRegion region : regions) {
                Optional<WrappedState> flagState = region.getFlag(flagPlayerParticles);
                if (flagState.isPresent()) {
                    Object value = flagState.get();
                    if (value instanceof WrappedState && value == WrappedState.DENY) {
                        allowed = false;
                        break;
                    } else if (value instanceof Optional && ((Optional<WrappedState>) value).get() == WrappedState.DENY) {
                        allowed = false;
                        break;
                    }
                }
            }
        }

        // Check "player-particles-limited" flag
        if (flagPlayerParticlesLimited != null) {
            for (IWrappedRegion region : regions) {
                Optional<WrappedState> flagState = region.getFlag(flagPlayerParticlesLimited);
                if (flagState.isPresent()) {
                    Object value = flagState.get();
                    if (value instanceof WrappedState && value == WrappedState.DENY) {
                        limited = true;
                        break;
                    } else if (value instanceof Optional && ((Optional<WrappedState>) value).get() == WrappedState.DENY) {
                        limited = true;
                        break;
                    }
                }
            }
        }

        return new RegionStatus(allowed, limited);
    }

    /**
     * Checks if a location is in a region that allows particles to spawn
     *
     * @param location The location to check
     * @return true if the location is in an allowed region, otherwise false
     */
    public static boolean isInAllowedRegion(Location location) {
        return getRegionStatuses(location).allowed;
    }

    /**
     * Checks if a location is in a region that has limited particles allowed
     *
     * @param location The location to check
     * @return true if the location only allows limited particles, otherwise false
     */
    public static boolean isInLimitedRegion(Location location) {
        return getRegionStatuses(location).limited;
    }

}
