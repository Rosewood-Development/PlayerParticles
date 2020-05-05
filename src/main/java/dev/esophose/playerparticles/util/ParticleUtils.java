package dev.esophose.playerparticles.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public final class ParticleUtils {

    public final static Material FALLBACK_MATERIAL;
    private static List<String> blockMaterials, itemMaterials;
    
    static {
        if (NMSUtil.getVersionNumber() > 7) {
            FALLBACK_MATERIAL = Material.BARRIER;
        } else {
            FALLBACK_MATERIAL = Material.BEDROCK;
        }

        blockMaterials = new ArrayList<>();
        itemMaterials = new ArrayList<>();
        
        for (Material mat : Material.values()) {
            if (mat.isBlock()) {
                blockMaterials.add(mat.name().toLowerCase());
            } else {
                itemMaterials.add(mat.name().toLowerCase());
            }
        }
    }
    
    private ParticleUtils() {

    }

    /**
     * Finds a block/item as a material from a string
     * 
     * @param input The material name as a string
     * @return The material from the string
     */
    public static Material closestMatch(String input) {
        if (input == null || input.trim().isEmpty())
            return null;

        return Material.matchMaterial(input.toUpperCase());
    }

    /**
     * Finds a block/item as a material from a list of possible strings
     * Contains a fallback to the barrier icon just in case
     * 
     * @param fallback If the material should fall back to barrier
     * @param input A list of material names
     * @return The first matching material
     */
    public static Material closestMatchWithFallback(boolean fallback, String... input) {
        for (String name : input) {
            Material mat = closestMatch(name);
            if (mat != null)
                return mat;
        }

        if (fallback)
            return FALLBACK_MATERIAL;

        return null;
    }

    /**
     * Gets all block materials in the game
     * 
     * @return A List of all block material names available in the game
     */
    public static List<String> getAllBlockMaterials() {
        return blockMaterials;
    }

    /**
     * Gets all item materials in the game
     * 
     * @return A List of all item material names available in the game
     */
    public static List<String> getAllItemMaterials() {
        return itemMaterials;
    }
    
    /**
     * Formats a string from the format "word_word" to "Word Word"
     * 
     * @param string The input string
     * @return The input string but formatted with each word capitalized
     */
    public static String formatName(String string) {
        return WordUtils.capitalizeFully(string.replaceAll("_", " "));
    }

    /**
     * Gets the smallest positive integer from an array
     * 
     * @param n The array containing non-available integers
     * @return The smallest positive integer not in the given array
     */
    public static int getSmallestPositiveInt(int[] n) {
        for (int i = 0; i < n.length; ++i) {
            while (n[i] != i + 1) {
                if (n[i] <= 0 || n[i] > n.length || n[i] == n[n[i] - 1]) break;
                int temp = n[i];
                n[i] = n[temp - 1];
                n[temp - 1] = temp;
            }
        }
        for (int i = 0; i < n.length; ++i)
            if (n[i] != i + 1) 
                return i + 1;
        return n.length + 1;
    }

    public static boolean containsConfigSpecialCharacters(String string) {
        for (char c : string.toCharArray()) {
            // Range taken from SnakeYAML's Emitter.java
            if (!(c == '\n' || (0x20 <= c && c <= 0x7E)) &&
                    (c == 0x85 || (c >= 0xA0 && c <= 0xD7FF)
                    || (c >= 0xE000 && c <= 0xFFFD)
                    || (c >= 0x10000 && c <= 0x10FFFF))) {
                return true;
            }
        }
        return false;
    }

}
