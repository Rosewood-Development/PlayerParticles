package dev.esophose.playerparticles.util;

import org.bukkit.Bukkit;

public final class NMSUtil {

    private static String cachedVersion = null;
    private static int cachedVersionNumber = -1;

    /**
     * Gets the server version
     *
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
     * Gets the server version major release number
     *
     * @return The server version major release number
     */
    public static int getVersionNumber() {
        if (cachedVersionNumber == -1) {
            String name = getVersion().substring(3);
            cachedVersionNumber = Integer.parseInt(name.substring(0, name.length() - 3));
        }
        return cachedVersionNumber;
    }

    /**
     * @return true if the server is running Spigot or a fork, false otherwise
     */
    public static boolean isSpigot() {
        try {
            Class.forName("org.spigotmc.SpigotConfig");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

}
