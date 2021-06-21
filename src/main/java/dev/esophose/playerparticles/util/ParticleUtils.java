package dev.esophose.playerparticles.util;

import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang.WordUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;

public final class ParticleUtils {

    public static final Material FALLBACK_MATERIAL;
    public static final List<Material> BLOCK_MATERIALS, ITEM_MATERIALS;
    public static final List<String> BLOCK_MATERIALS_STRING, ITEM_MATERIALS_STRING;
    private static Method LivingEntity_getTargetBlock;
    
    static {
        if (NMSUtil.getVersionNumber() > 7) {
            FALLBACK_MATERIAL = Material.BARRIER;
        } else {
            FALLBACK_MATERIAL = Material.BEDROCK;
        }

        BLOCK_MATERIALS = new ArrayList<>();
        ITEM_MATERIALS = new ArrayList<>();

        Inventory tempInventory = Bukkit.createInventory(null, 9);
        for (Material mat : Material.values()) {
            // Verify an ItemStack of the material can be placed into an inventory. In 1.12 and up this can easily be checked with mat.isItem(), but that doesn't exist pre-1.12
            tempInventory.clear();
            tempInventory.setItem(0, new ItemStack(mat, 1));
            ItemStack itemStack = tempInventory.getItem(0);
            if (itemStack != null) {
                if (mat.isBlock())
                    BLOCK_MATERIALS.add(mat);
                ITEM_MATERIALS.add(mat); // Items allow both items and blocks to be selected
            }
        }

        BLOCK_MATERIALS.sort(Comparator.comparing(Enum::name));
        ITEM_MATERIALS.sort(Comparator.comparing(Enum::name));

        BLOCK_MATERIALS_STRING = BLOCK_MATERIALS.stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toList());
        ITEM_MATERIALS_STRING = ITEM_MATERIALS.stream().map(Enum::name).map(String::toLowerCase).collect(Collectors.toList());

        if (NMSUtil.getVersionNumber() < 8) {
            try {
                LivingEntity_getTargetBlock = LivingEntity.class.getDeclaredMethod("getTargetBlock", HashSet.class, int.class);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
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

    public static boolean isPlayerVisible(Player player) {
        return Setting.DISPLAY_WHEN_INVISIBLE.getBoolean() || ((NMSUtil.getVersionNumber() <= 8 || player.getGameMode() != GameMode.SPECTATOR)
                && !player.hasPotionEffect(PotionEffectType.INVISIBILITY));
    }

    public static String rgbToHex(int r, int g, int b) {
        return String.format("%02x%02x%02x", r, g, b);
    }

    public static Block getTargetBlock(Player player) {
        if (NMSUtil.getVersionNumber() > 7) {
            return player.getTargetBlock((Set<Material>) null, 8); // Need the Set<Material> cast for 1.9 support
        } else {
            try {
                return (Block) LivingEntity_getTargetBlock.invoke(player, null, 8);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                return player.getLocation().getBlock();
            }
        }
    }

}
