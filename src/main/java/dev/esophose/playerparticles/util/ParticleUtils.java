package dev.esophose.playerparticles.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;

public class ParticleUtils {
    
    private static List<String> blockMaterials, itemMaterials;
    
    static {
        blockMaterials = new ArrayList<String>();
        itemMaterials = new ArrayList<String>();
        
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
    public static Material closestMatch(String input) { // @formatter:off
        if (input == null) return null;
        for (Material material : Material.values()) // First check for exact matches
            if (material.name().equalsIgnoreCase(input) ||
                material.toString().equalsIgnoreCase(input)) return material;
        for (Material material : Material.values()) // Then check for partial matches
            if (material.name().toLowerCase().contains(input.toLowerCase()) || 
                material.toString().toLowerCase().contains(input.toLowerCase()) ||
                material.name().replaceAll("_", "").toLowerCase().contains(input.toLowerCase()) || 
                material.toString().replaceAll("_", "").toLowerCase().contains(input.toLowerCase())) 
                return material; 
        return null;
    } // @formatter:on

    /**
     * Finds a block/item as a material from a list of possible strings
     * Contains a fallback to the barrier icon just in case
     * 
     * @param barrierFallback If the material should fall back to barrier
     * @param input A list of material names
     * @return The first matching material
     */
    public static Material closestMatchWithFallback(boolean barrierFallback, String... input) {
        Material mat = null;
        for (String name : input) {
            mat = closestMatch(name);
            if (mat != null)
                return mat;
        }
        if (barrierFallback)
            mat = Material.BARRIER;
        return mat;
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
        String[] words = string.split("_");
        String result = "";
        for (String word : words) 
            result += Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase() + " ";
        return result;
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

}
