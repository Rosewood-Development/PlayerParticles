package dev.esophose.playerparticles.hook;

import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import java.util.List;
import java.util.Set;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.codemc.worldguardwrapper.WorldGuardWrapper;
import org.codemc.worldguardwrapper.region.IWrappedRegion;

public class WorldGuardHook {

    private static Boolean enabled;
    private static WorldGuardWrapper worldGuardWrapper;

    /**
     * @return true if WorldGuard is enabled, otherwise false
     */
    public static boolean enabled() {
        if (enabled != null)
            return enabled;

        enabled = Bukkit.getPluginManager().getPlugin("WorldGuard") != null;
        if (enabled)
            worldGuardWrapper = WorldGuardWrapper.getInstance();

        return enabled;
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

        List<String> disallowedRegionIds = Setting.WORLDGUARD_DISALLOWED_REGIONS.getStringList();
        if (regions.stream().map(IWrappedRegion::getId).anyMatch(disallowedRegionIds::contains))
            return false;

        if (Setting.WORLDGUARD_USE_ALLOWED_REGIONS.getBoolean()) {
            List<String> allowedRegionIds = Setting.WORLDGUARD_ALLOWED_REGIONS.getStringList();
            return regions.stream().map(IWrappedRegion::getId).anyMatch(allowedRegionIds::contains);
        }

        return true;
    }

}