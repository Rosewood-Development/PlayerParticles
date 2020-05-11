package dev.esophose.playerparticles.util;

import dev.esophose.playerparticles.nms.wrapper.ParticleHandler;
import dev.esophose.playerparticles.particles.version.VersionMapping;
import org.bukkit.Bukkit;

public final class NMSUtil {

    private static String cachedVersion = null;
    private static int cachedVersionNumber = -1;

    public static ParticleHandler getHandler(VersionMapping versionMapping) {
        try {
            return (ParticleHandler) Class.forName("dev.esophose.playerparticles.nms." + getVersion() + ".ParticleHandlerImpl").getConstructor(VersionMapping.class).newInstance(versionMapping);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    /**
     * @return The server version
     */
    public static String getVersion() {
        if (cachedVersion == null) {
            String name = Bukkit.getServer().getClass().getPackage().getName();
            cachedVersion = name.substring(name.lastIndexOf('.') + 1);
        }
        return cachedVersion;
    }

    /**
     * @return the server version major release number
     */
    public static int getVersionNumber() {
        if (cachedVersionNumber == -1) {
            String name = getVersion().substring(3);
            cachedVersionNumber = Integer.parseInt(name.substring(0, name.length() - 3));
        }
        return cachedVersionNumber;
    }

}
