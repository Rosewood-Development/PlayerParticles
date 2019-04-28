package com.esophose.playerparticles.util;

import org.bukkit.Bukkit;

public class NMSUtil {

    /**
     * Gets the server version
     *
     * @return The server version
     */
    public static String getVersion() {
        String name = Bukkit.getServer().getClass().getPackage().getName();
        return name.substring(name.lastIndexOf('.') + 1) + ".";
    }

    /**
     * Gets the server version major release number
     *
     * @return The server version major release number
     */
    public static int getVersionNumber() {
        String name = getVersion().substring(3);
        return Integer.valueOf(name.substring(0, name.length() - 4));
    }

}
