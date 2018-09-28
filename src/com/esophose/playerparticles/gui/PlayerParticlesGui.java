package com.esophose.playerparticles.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.particles.ParticleGroup;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;
import com.esophose.playerparticles.util.ParticleUtils;

/**
 * This class provides a collection of static methods for modifying your
 * particle/style/data through the use of a GUI
 */
// TODO: This entire GUI is currently hardwired to only work with the first particle in the player's list, make it with with everything
public class PlayerParticlesGui extends BukkitRunnable implements Listener {

    /**
     * The possible states that the GUI can be in
     * Used to figure out what the InventoryClickEvent should do
     */
    public enum GuiState { // @formatter:off
        DEFAULT, 
        EFFECT, 
        STYLE, 
        DATA
    } // @formatter:on

    public static final String rainbowName = ChatColor.RED + "r" + ChatColor.GOLD + "a" + ChatColor.YELLOW + "i" + ChatColor.GREEN + "n" + ChatColor.AQUA + "b" + ChatColor.BLUE + "o" + ChatColor.LIGHT_PURPLE + "w";

    private static final int INVENTORY_SIZE = 54;
    private static HashMap<UUID, GuiInventory> playerGuiInventories;
    private static boolean guiEnabled;

    /**
     * Cached icons to prevent calling config over and over
     */
    private static Material[] defaultMenuIcons = new Material[3];
    private static HashMap<String, Material> effectIcons;
    private static HashMap<String, Material> styleIcons;

    /**
     * Maps to convert from clicked materials to particle colors
     */
    private static ColorData[] colorMapping;

    /**
     * DyeColor array in the order of the rainbow
     * Color index for the wool color to display next
     */
    private static DyeColor[] rainbowColors;
    private static int rainbowColorsIndex = 0;

    /**
     * Cached material data
     */
    private static final Random RANDOM = new Random();
    private static List<Material> BLOCK_MATERIALS = new ArrayList<Material>();
    private static List<Material> ITEM_MATERIALS = new ArrayList<Material>();

    static { // @formatter:off
    	colorMapping = new ColorData[] {
    		new ColorData(DyeColor.RED, ParticleUtils.closestMatch("ROSE_RED"), new OrdinaryColor(255, 0, 0), ChatColor.RED + "red"),
    		new ColorData(DyeColor.ORANGE, ParticleUtils.closestMatch("ORANGE_DYE"), new OrdinaryColor(255, 140, 0), ChatColor.GOLD + "orange"),
    		new ColorData(DyeColor.YELLOW, ParticleUtils.closestMatch("DANDELION_YELLOW"), new OrdinaryColor(255, 255, 0), ChatColor.YELLOW + "yellow"),
    		new ColorData(DyeColor.LIME, ParticleUtils.closestMatch("LIME_DYE"), new OrdinaryColor(50, 205, 50), ChatColor.GREEN + "lime green"),
    		new ColorData(DyeColor.GREEN, ParticleUtils.closestMatch("CACTUS_GREEN"), new OrdinaryColor(0, 128, 0), ChatColor.DARK_GREEN + "green"),
    		new ColorData(DyeColor.BLUE, ParticleUtils.closestMatch("LAPIZ_LAZULI"), new OrdinaryColor(0, 0, 255), ChatColor.DARK_BLUE + "blue"),
    		new ColorData(DyeColor.CYAN, ParticleUtils.closestMatch("CYAN_DYE"), new OrdinaryColor(0, 139, 139), ChatColor.DARK_AQUA + "cyan"),
    		new ColorData(DyeColor.LIGHT_BLUE, ParticleUtils.closestMatch("LIGHT_BLUE_DYE"), new OrdinaryColor(173, 216, 230), ChatColor.AQUA + "light blue"),
    		new ColorData(DyeColor.PURPLE, ParticleUtils.closestMatch("PURPLE_DYE"), new OrdinaryColor(138, 43, 226), ChatColor.DARK_PURPLE + "purple"),
    		new ColorData(DyeColor.MAGENTA, ParticleUtils.closestMatch("MAGENTA_DYE"), new OrdinaryColor(202, 31, 123), ChatColor.LIGHT_PURPLE + "magenta"),
    		new ColorData(DyeColor.PINK, ParticleUtils.closestMatch("PINK_DYE"), new OrdinaryColor(255, 182, 193), ChatColor.LIGHT_PURPLE + "pink"),
    		new ColorData(DyeColor.BROWN, ParticleUtils.closestMatch("COCOA_BEANS"), new OrdinaryColor(139, 69, 19), ChatColor.GOLD + "brown"),
    		new ColorData(DyeColor.BLACK, ParticleUtils.closestMatch("INK_SAC"), new OrdinaryColor(0, 0, 0), ChatColor.DARK_GRAY + "black"),
    		new ColorData(DyeColor.GRAY, ParticleUtils.closestMatch("GRAY_DYE"), new OrdinaryColor(128, 128, 128), ChatColor.DARK_GRAY + "gray"),
    		new ColorData(DyeColor.getByDyeData((byte)7), ParticleUtils.closestMatch("LIGHT_GRAY_DYE"), new OrdinaryColor(192, 192, 192), ChatColor.GRAY + "light gray"),
    		new ColorData(DyeColor.WHITE, ParticleUtils.closestMatch("BONE_MEAL"), new OrdinaryColor(255, 255, 255), ChatColor.WHITE + "white"),
    	};

        rainbowColors = new DyeColor[] { 
        	DyeColor.RED, 
        	DyeColor.ORANGE, 
        	DyeColor.YELLOW, 
        	DyeColor.LIME, 
        	DyeColor.LIGHT_BLUE, 
        	DyeColor.BLUE, 
        	DyeColor.PURPLE 
        };
        
        Inventory testingInventory = Bukkit.createInventory(null, 9);
        for (Material mat : Material.values()) {
            // Verify an ItemStack of the material can be placed into an inventory. In 1.12 and up this can easily be checked with mat.isItem(), but that doesn't exist pre-1.12
            testingInventory.clear();
            testingInventory.setItem(0, new ItemStack(mat, 1));
            ItemStack itemStack = testingInventory.getItem(0);
            if (itemStack != null) {
                if (mat.isBlock()) {
                    BLOCK_MATERIALS.add(mat);
                } else if (!mat.isBlock()) {
                    ITEM_MATERIALS.add(mat);
                }
            }
        }
    } // @formatter:on

    /**
     * Initializes all the static values for this class
     */
    public static void setup() {
        FileConfiguration config = PlayerParticles.getPlugin().getConfig();

        guiEnabled = config.getBoolean("gui-enabled");
        if (!guiEnabled) return;

        playerGuiInventories = new HashMap<UUID, GuiInventory>();
        effectIcons = new HashMap<String, Material>();
        styleIcons = new HashMap<String, Material>();

        defaultMenuIcons[0] = ParticleUtils.closestMatchWithFallback(config.getString("gui-icon.main-menu.EFFECT"));
        defaultMenuIcons[1] = ParticleUtils.closestMatchWithFallback(config.getString("gui-icon.main-menu.STYLE"));
        defaultMenuIcons[2] = ParticleUtils.closestMatchWithFallback(config.getString("gui-icon.main-menu.DATA"));

        // Grab a different effect icon set based on if the Minecraft version is >= 1.13 or not
        String legacy;
        try {
            Particle.valueOf("NAUTILUS");
            legacy = "";
        } catch (Exception ex) {
            legacy = "-legacy";
        }

        for (ParticleEffect effect : ParticleEffect.getSupportedEffects()) {
            String effectName = effect.getName().toUpperCase();
            Material iconMaterial = ParticleUtils.closestMatchWithFallback(config.getString("gui-icon.effect" + legacy + "." + effectName));
            effectIcons.put(effectName, iconMaterial);
        }

        for (ParticleStyle style : ParticleStyleManager.getStyles()) {
            String styleName = style.getName().toUpperCase();
            Material iconMaterial = ParticleUtils.closestMatchWithFallback(config.getString("gui-icon.style" + legacy + "." + styleName));
            styleIcons.put(styleName, iconMaterial);
        }

        new PlayerParticlesGui().runTaskTimer(PlayerParticles.getPlugin(), 0, 10);
    }

    // TODO: Delete these specialized getter methods once the new GUI is finished
    private static ParticleEffect getPPlayerEffect(PPlayer pplayer) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        if (particles.isEmpty()) return ParticleEffect.NONE;
        return particles.get(0).getEffect();
    }

    private static ParticleStyle getPPlayerStyle(PPlayer pplayer) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        if (particles.isEmpty()) return DefaultStyles.NONE;
        return particles.get(0).getStyle();
    }

    private static Material getPPlayerItemMaterial(PPlayer pplayer) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        if (particles.isEmpty()) return ParticlePair.getDefault().getItemMaterial();
        return particles.get(0).getItemMaterial();
    }

    private static Material getPPlayerBlockMaterial(PPlayer pplayer) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        if (particles.isEmpty()) return ParticlePair.getDefault().getBlockMaterial();
        return particles.get(0).getBlockMaterial();
    }

    private static OrdinaryColor getPPlayerColor(PPlayer pplayer) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        if (particles.isEmpty()) return ParticlePair.getDefault().getColor();
        return particles.get(0).getColor();
    }

    private static NoteColor getPPlayerNoteColor(PPlayer pplayer) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        if (particles.isEmpty()) return ParticlePair.getDefault().getNoteColor();
        return particles.get(0).getNoteColor();
    }

    private static Material getPPlayerSpawnMaterial(PPlayer pplayer) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        if (particles.isEmpty()) return ParticlePair.getDefault().getSpawnMaterial();
        return particles.get(0).getSpawnMaterial();
    }

    private static ParticleColor getPPlayerSpawnColor(PPlayer pplayer) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        if (particles.isEmpty()) return ParticlePair.getDefault().getSpawnColor();
        return particles.get(0).getSpawnColor();
    }

    private static String getPPlayerDataString(PPlayer pplayer) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        if (particles.isEmpty()) return ParticlePair.getDefault().getDataString();
        return particles.get(0).getDataString();
    }

    private static void setPPlayerEffect(PPlayer pplayer, ParticleEffect effect) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        ParticlePair particle;
        if (particles.isEmpty()) particle = ParticlePair.getDefault(pplayer.getUniqueId());
        else particle = particles.get(0);

        particle.setEffect(effect);
        particles.clear();
        particles.add(particle);

        ParticleGroup group = new ParticleGroup(null, particles);
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
    }

    private static void setPPlayerStyle(PPlayer pplayer, ParticleStyle style) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        ParticlePair particle;
        if (particles.isEmpty()) particle = ParticlePair.getDefault(pplayer.getUniqueId());
        else particle = particles.get(0);

        particle.setStyle(style);
        particles.clear();
        particles.add(particle);

        ParticleGroup group = new ParticleGroup(null, particles);
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
    }

    private static void setPPlayerItemMaterial(PPlayer pplayer, Material material) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        ParticlePair particle;
        if (particles.isEmpty()) particle = ParticlePair.getDefault(pplayer.getUniqueId());
        else particle = particles.get(0);

        particle.setItemMaterial(material);
        particles.clear();
        particles.add(particle);

        ParticleGroup group = new ParticleGroup(null, particles);
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
    }

    private static void setPPlayerBlockMaterial(PPlayer pplayer, Material material) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        ParticlePair particle;
        if (particles.isEmpty()) particle = ParticlePair.getDefault(pplayer.getUniqueId());
        else particle = particles.get(0);

        particle.setBlockMaterial(material);
        particles.clear();
        particles.add(particle);

        ParticleGroup group = new ParticleGroup(null, particles);
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
    }

    private static void setPPlayerColor(PPlayer pplayer, OrdinaryColor color) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        ParticlePair particle;
        if (particles.isEmpty()) particle = ParticlePair.getDefault(pplayer.getUniqueId());
        else particle = particles.get(0);

        particle.setColor(color);
        particles.clear();
        particles.add(particle);

        ParticleGroup group = new ParticleGroup(null, particles);
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
    }

    private static void setPPlayerNoteColor(PPlayer pplayer, NoteColor noteColor) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        ParticlePair particle;
        if (particles.isEmpty()) particle = ParticlePair.getDefault(pplayer.getUniqueId());
        else particle = particles.get(0);

        particle.setNoteColor(noteColor);
        particles.clear();
        particles.add(particle);

        ParticleGroup group = new ParticleGroup(null, particles);
        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
    }

    /**
     * Updates all color/note data inventories to display the rainbow icon
     * Removes any players who are no longer online from playerGuiInventoriesd
     */
    public void run() {
        List<UUID> toRemoveList = new ArrayList<UUID>();

        for (Map.Entry<UUID, GuiInventory> entry : playerGuiInventories.entrySet()) {
            UUID playerUUID = entry.getKey();
            PPlayer pplayer = DataManager.getPPlayer(playerUUID);
            if (pplayer == null) {
                toRemoveList.add(playerUUID);
                continue;
            }

            Player player = Bukkit.getPlayer(playerUUID);
            if (player == null) {
                toRemoveList.add(playerUUID);
                continue;
            }

            GuiInventory guiInventory = entry.getValue();
            Inventory inventory = guiInventory.getInventory();

            if (player.getOpenInventory().getTopInventory().equals(inventory) && guiInventory.getGuiState() == GuiState.DATA && getPPlayerEffect(pplayer).hasProperty(ParticleProperty.COLORABLE)) {
                ItemStack rainbowIcon;
                if (getPPlayerEffect(pplayer) != ParticleEffect.NOTE) {
                    rainbowIcon = getItemForRainbowColorData(getPPlayerColor(pplayer), rainbowColors[rainbowColorsIndex]);
                } else {
                    rainbowIcon = getItemForRainbowNoteData(getPPlayerNoteColor(pplayer), rainbowColors[rainbowColorsIndex]);
                }
                inventory.setItem(40, rainbowIcon);
            }
        }

        for (UUID uuid : toRemoveList)
            playerGuiInventories.remove(uuid);

        rainbowColorsIndex++;
        rainbowColorsIndex %= rainbowColors.length;
    }

    /**
     * Gets if the GUI is disabled by the server owner or not
     * 
     * @return True if the GUI is disabled
     */
    public static boolean isGuiDisabled() {
        return !guiEnabled;
    }

    /**
     * Forcefully closes all open PlayerParticles GUIs
     * Used for when the plugin unloads so players can't take items from the GUI
     */
    public static void forceCloseAllOpenGUIs() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            InventoryView openInventory = p.getOpenInventory();
            if (openInventory == null) continue;
            Inventory topInventory = openInventory.getTopInventory();
            if (topInventory == null) continue;

            // Check if any of the inventories are the one the user has open, close if true
            for (GuiInventory guiInventory : playerGuiInventories.values()) {
                if (topInventory.equals(guiInventory.getInventory())) {
                    p.closeInventory();
                    break;
                }
            }
        }
    }

    /**
     * Changes the GUI to the indicated state
     * If the GUI isn't open yet, it gets opened
     * 
     * @param pplayer The pplayer
     * @param state The new state
     */
    public static void changeState(PPlayer pplayer, GuiState state) {
        Player player = pplayer.getPlayer();

        if ((state == GuiState.EFFECT && PermissionManager.getEffectsUserHasPermissionFor(player).size() == 1) || (state == GuiState.STYLE && PermissionManager.getStylesUserHasPermissionFor(player).size() == 1) || (state == GuiState.DATA && getPPlayerSpawnMaterial(pplayer) == null && getPPlayerSpawnColor(pplayer) == null)) return;

        // Update the state and create an inventory for the player if one isn't already open for them
        // If they have the wrong inventory open for some reason, create a new one and open it for them
        if (playerGuiInventories.containsKey(pplayer.getUniqueId())) {
            GuiInventory guiInventory = playerGuiInventories.get(pplayer.getUniqueId());
            guiInventory.setGuiState(state);
            if (!player.getOpenInventory().getTopInventory().equals(guiInventory.getInventory())) {
                player.openInventory(guiInventory.getInventory());
            }
        } else {
            Inventory ppInventory = Bukkit.createInventory(null, INVENTORY_SIZE, "PlayerParticles");
            player.openInventory(ppInventory);
            playerGuiInventories.put(pplayer.getUniqueId(), new GuiInventory(ppInventory, state));
        }

        switch (state) {
        case DEFAULT:
            populateDefault(pplayer);
            break;
        case EFFECT:
            populateEffect(pplayer);
            break;
        case STYLE:
            populateStyle(pplayer);
            break;
        case DATA:
            populateData(pplayer);
            break;
        }
    }

    /**
     * Opens the menu to go to other menus: effect, style, data
     * 
     * @param pplayer The PPlayer
     */
    private static void populateDefault(PPlayer pplayer) {
        Player player = pplayer.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        inventory.clear(); // Make sure the inventory is always empty before we start adding items

        ItemStack currentIcon;
        Material playerHead = ParticleUtils.closestMatch("PLAYER_HEAD");
        if (playerHead != null) {
            currentIcon = new ItemStack(playerHead, 1);
        } else {
            currentIcon = new ItemStack(ParticleUtils.closestMatch("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());
        }

        SkullMeta currentIconMeta = (SkullMeta) currentIcon.getItemMeta();
        currentIconMeta.setDisplayName(ChatColor.GREEN + player.getName());
        String[] currentIconLore = new String[3];
        currentIconLore[0] = ChatColor.YELLOW + "Effect: " + ChatColor.AQUA + getPPlayerEffect(pplayer).getName();
        currentIconLore[1] = ChatColor.YELLOW + "Style: " + ChatColor.AQUA + getPPlayerStyle(pplayer).getName();
        currentIconLore[2] = ChatColor.YELLOW + "Active Data: " + ChatColor.AQUA + getPPlayerDataString(pplayer);
        currentIconMeta.setLore(Arrays.asList(currentIconLore));
        currentIconMeta.setOwner(player.getName());
        // currentIconMeta.setOwningPlayer(Bukkit.getOfflinePlayer(player.getUniqueId())); // This doesn't exist in 1.9
        currentIcon.setItemMeta(currentIconMeta);

        ItemStack effectIcon = new ItemStack(defaultMenuIcons[0], 1);
        ItemMeta effectIconMeta = effectIcon.getItemMeta();
        effectIconMeta.setDisplayName(ChatColor.GREEN + "Effect");
        effectIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SET_YOUR.getMessageReplaced("effect")));
        if (PermissionManager.getEffectsUserHasPermissionFor(player).size() == 1) { // Always has access to NONE
            effectIconMeta.setLore(Arrays.asList(Lang.GUI_NO_ACCESS_TO.getMessageReplaced("effects")));
        }
        effectIcon.setItemMeta(effectIconMeta);

        ItemStack styleIcon = new ItemStack(defaultMenuIcons[1], 1);
        ItemMeta styleIconMeta = styleIcon.getItemMeta();
        styleIconMeta.setDisplayName(ChatColor.GREEN + "Style");
        styleIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SET_YOUR.getMessageReplaced("style")));
        if (PermissionManager.getStylesUserHasPermissionFor(player).size() == 1) { // Always has access to NONE
            styleIconMeta.setLore(Arrays.asList(Lang.GUI_NO_ACCESS_TO.getMessageReplaced("styles")));
        }
        styleIcon.setItemMeta(styleIconMeta);

        ItemStack dataIcon = new ItemStack(defaultMenuIcons[2], 1);
        ItemMeta dataIconMeta = dataIcon.getItemMeta();
        dataIconMeta.setDisplayName(ChatColor.GREEN + "Data");
        ParticleEffect pe = getPPlayerEffect(pplayer);
        String dataType = "data";
        if (pe.hasProperty(ParticleProperty.COLORABLE)) // @formatter:off
            if (pe == ParticleEffect.NOTE) dataType = "note " + dataType;
                else dataType = "color " + dataType;
            else if (pe.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) if (pe == ParticleEffect.ITEM) dataType = "item " + dataType;
        else dataType = "block " + dataType; // @formatter:on
        dataIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SET_YOUR.getMessageReplaced(dataType)));
        if (getPPlayerSpawnMaterial(pplayer) == null && getPPlayerSpawnColor(pplayer) == null) {
            dataIconMeta.setLore(Arrays.asList(Lang.GUI_NO_DATA.get()));
        }
        dataIcon.setItemMeta(dataIconMeta);

        inventory.setItem(13, currentIcon);
        inventory.setItem(38, effectIcon);
        inventory.setItem(40, styleIcon);
        inventory.setItem(42, dataIcon);
    }

    /**
     * Opens the menu that allows you to select an effect you have permission for
     * 
     * @param pplayer The PPlayer
     */
    private static void populateEffect(PPlayer pplayer) {
        Player player = pplayer.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();
        inventory.clear(); // Make sure the inventory is always empty before we start adding items

        List<String> effectsUserHasPermissionFor = PermissionManager.getEffectsUserHasPermissionFor(player);
        for (int i = 0; i < effectsUserHasPermissionFor.size(); i++) {
            String s = effectsUserHasPermissionFor.get(i);
            ParticleEffect effect = ParticleManager.effectFromString(s);
            inventory.setItem(i, getItemForEffect(effect, effect == getPPlayerEffect(pplayer)));
        }

        inventory.setItem(INVENTORY_SIZE - 1, getItemForBack());
    }

    /**
     * Opens the menu that allows you to select a style you have permission for
     * 
     * @param pplayer The PPlayer
     */
    private static void populateStyle(PPlayer pplayer) {
        Player player = pplayer.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        inventory.clear(); // Make sure the inventory is always empty before we start adding items

        List<String> stylesUserHasPermissionFor = PermissionManager.getStylesUserHasPermissionFor(player);
        for (int i = 0; i < stylesUserHasPermissionFor.size(); i++) {
            String s = stylesUserHasPermissionFor.get(i);
            ParticleStyle style = ParticleStyleManager.styleFromString(s);
            inventory.setItem(i, getItemForStyle(style, style == getPPlayerStyle(pplayer)));
        }

        inventory.setItem(INVENTORY_SIZE - 1, getItemForBack());
    }

    /**
     * Opens the menu that allows you to select some preset data
     * 
     * @param pplayer The PPlayer
     */
    private static void populateData(PPlayer pplayer) {
        Player player = pplayer.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        inventory.clear(); // Make sure the inventory is always empty before we start adding items

        // There are a lot of for loops here, somebody submit a pull request if you have a better way of doing this
        ParticleEffect pe = getPPlayerEffect(pplayer);
        if (pe.hasProperty(ParticleProperty.COLORABLE)) {
            if (pe == ParticleEffect.NOTE) { // Note data
                NoteColor currentNote = getPPlayerNoteColor(pplayer);
                int noteIndex = 0;
                for (int i = 1; i <= 7; i++) { // Top row
                    inventory.setItem(i, getItemForNoteData(currentNote, noteIndex));
                    noteIndex++;
                }
                for (int i = 10; i <= 16; i++) { // Middle 1 row
                    inventory.setItem(i, getItemForNoteData(currentNote, noteIndex));
                    noteIndex++;
                }
                for (int i = 19; i <= 25; i++) { // Middle 2 row
                    inventory.setItem(i, getItemForNoteData(currentNote, noteIndex));
                    noteIndex++;
                }
                for (int i = 28; i <= 30; i++) { // Bottom row
                    inventory.setItem(i, getItemForNoteData(currentNote, noteIndex));
                    noteIndex++;
                }

                inventory.setItem(40, getItemForRainbowNoteData(getPPlayerNoteColor(pplayer), rainbowColors[rainbowColorsIndex]));
            } else { // Color data
                OrdinaryColor currentColor = getPPlayerColor(pplayer);
                int colorIndex = 0;
                for (int i = 10; i <= 16; i++) { // Top row
                    inventory.setItem(i, getItemForColorData(currentColor, colorIndex));
                    colorIndex++;
                }
                for (int i = 19; i <= 25; i++) { // Middle row
                    inventory.setItem(i, getItemForColorData(currentColor, colorIndex));
                    colorIndex++;
                }
                for (int i = 28; i <= 29; i++) { // Bottom row
                    inventory.setItem(i, getItemForColorData(currentColor, colorIndex));
                    colorIndex++;
                }

                inventory.setItem(40, getItemForRainbowColorData(getPPlayerColor(pplayer), rainbowColors[rainbowColorsIndex]));
            }
        } else if (pe.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            List<Material> materialBag = new ArrayList<Material>();
            int materialIndex = 0;

            if (pe == ParticleEffect.ITEM) { // Item data
                Material currentItemMaterial = getPPlayerItemMaterial(pplayer);

                while (materialBag.size() < 28) { // Grab 28 random materials that are an item
                    Material randomMaterial = ITEM_MATERIALS.get(RANDOM.nextInt(ITEM_MATERIALS.size()));
                    if (!materialBag.contains(randomMaterial)) materialBag.add(randomMaterial);
                }

                for (int i = 10; i <= 16; i++) { // Top row
                    inventory.setItem(i, getItemForMaterialData(currentItemMaterial, "item", materialBag.get(materialIndex)));
                    materialIndex++;
                }
                for (int i = 19; i <= 25; i++) { // Middle 1 row
                    inventory.setItem(i, getItemForMaterialData(currentItemMaterial, "item", materialBag.get(materialIndex)));
                    materialIndex++;
                }
                for (int i = 28; i <= 34; i++) { // Middle 2 row
                    inventory.setItem(i, getItemForMaterialData(currentItemMaterial, "item", materialBag.get(materialIndex)));
                    materialIndex++;
                }
                for (int i = 37; i <= 43; i++) { // Bottom row
                    inventory.setItem(i, getItemForMaterialData(currentItemMaterial, "item", materialBag.get(materialIndex)));
                    materialIndex++;
                }
            } else { // Block data
                Material currentBlockMaterial = getPPlayerBlockMaterial(pplayer);

                while (materialBag.size() < 28) { // Grab 28 random materials that are an item
                    Material randomMaterial = BLOCK_MATERIALS.get(RANDOM.nextInt(BLOCK_MATERIALS.size()));
                    if (!materialBag.contains(randomMaterial)) materialBag.add(randomMaterial);
                }

                for (int i = 10; i <= 16; i++) { // Top row
                    inventory.setItem(i, getItemForMaterialData(currentBlockMaterial, "block", materialBag.get(materialIndex)));
                    materialIndex++;
                }
                for (int i = 19; i <= 25; i++) { // Middle 1 row
                    inventory.setItem(i, getItemForMaterialData(currentBlockMaterial, "block", materialBag.get(materialIndex)));
                    materialIndex++;
                }
                for (int i = 28; i <= 34; i++) { // Middle 2 row
                    inventory.setItem(i, getItemForMaterialData(currentBlockMaterial, "block", materialBag.get(materialIndex)));
                    materialIndex++;
                }
                for (int i = 37; i <= 43; i++) { // Bottom row
                    inventory.setItem(i, getItemForMaterialData(currentBlockMaterial, "block", materialBag.get(materialIndex)));
                    materialIndex++;
                }
            }
        }

        inventory.setItem(INVENTORY_SIZE - 1, getItemForBack());
    }

    /**
     * Called whenever something is clicked in an inventory
     * Will return immediately if not the particlesInventory
     * 
     * @param e The InventoryClickEvent fired from this event
     */
    @EventHandler
    public void onInventoryInteract(InventoryClickEvent e) {
        if (isGuiDisabled()) return; // Don't worry about processing anything if the GUI is disabled

        if (!(e.getWhoClicked() instanceof Player)) return; // Not sure if I actually have to check this

        Player player = (Player) e.getWhoClicked();
        GuiInventory guiInventory = playerGuiInventories.get(player.getUniqueId());

        if (guiInventory == null || !guiInventory.getInventory().equals(e.getView().getTopInventory())) return; // Make sure it is the right inventory

        e.setCancelled(true); // In the PlayerParticles GUI, can't let them take anything out

        if (!guiInventory.getInventory().equals(e.getClickedInventory())) return; // Clicked bottom inventory

        PPlayer pplayer = DataManager.getPPlayer(player.getUniqueId());
        if (pplayer == null) {
            player.closeInventory();
            return;
        }

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return; // Clicked on an empty slot, do nothing

        // Check back button. This is common for most menus
        if (clicked.getItemMeta().getDisplayName().equals(Lang.GUI_BACK_BUTTON.get())) {
            changeState(pplayer, GuiState.DEFAULT);
            return;
        }

        String name = ChatColor.stripColor(clicked.getItemMeta().getDisplayName());
        switch (guiInventory.getGuiState()) {
        case DEFAULT:
            if (name.equals("Effect")) {
                changeState(pplayer, GuiState.EFFECT);
            } else if (name.equals("Style")) {
                changeState(pplayer, GuiState.STYLE);
            } else if (name.equals("Data")) {
                changeState(pplayer, GuiState.DATA);
            }
            break;
        case EFFECT:
            setPPlayerEffect(pplayer, ParticleManager.effectFromString(name));
            changeState(pplayer, GuiState.DEFAULT);
            break;
        case STYLE:
            setPPlayerStyle(pplayer, ParticleStyleManager.styleFromString(name));
            changeState(pplayer, GuiState.DEFAULT);
            break;
        case DATA:
            ParticleEffect pe = getPPlayerEffect(pplayer);
            if (pe.hasProperty(ParticleProperty.COLORABLE)) {
                if (pe == ParticleEffect.NOTE) {
                    if (clicked.getItemMeta().getDisplayName().equals(rainbowName)) {
                        setPPlayerNoteColor(pplayer, new NoteColor(99));
                    } else {
                        int note = Integer.parseInt(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).substring(6));
                        setPPlayerNoteColor(pplayer, new NoteColor(note));
                    }
                } else {
                    if (clicked.getItemMeta().getDisplayName().equals(rainbowName)) {
                        setPPlayerColor(pplayer, new OrdinaryColor(999, 999, 999));
                    } else {
                        for (int i = 0; i < colorMapping.length; i++) {
                            if (clicked.getItemMeta().getDisplayName().equals(colorMapping[i].getName())) {
                                setPPlayerColor(pplayer, colorMapping[i].getOrdinaryColor());
                            }
                        }
                    }
                }
            } else if (pe.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                Material clickedMaterial = clicked.getType(); // All preset materials have a data value of 0
                if (pe == ParticleEffect.ITEM) {
                    setPPlayerItemMaterial(pplayer, clickedMaterial);
                } else {
                    setPPlayerBlockMaterial(pplayer, clickedMaterial);
                }
            }

            changeState(pplayer, GuiState.DEFAULT);
            break;
        }
    }

    /**
     * Gets the icon for a given particle effect from the config
     * 
     * @param effect The effect
     * @return An ItemStack formatted to be displayed in the GUI
     */
    private static ItemStack getItemForEffect(ParticleEffect effect, boolean isActive) {
        ItemStack icon = new ItemStack(effectIcons.get(effect.name()), 1);
        ItemMeta iconMeta = icon.getItemMeta();

        iconMeta.setDisplayName(Lang.GUI_ICON_NAME_COLOR.get() + effect.getName());
        if (!isActive) {
            iconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("effect") + effect.getName()));
        } else {
            iconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("effect") + effect.getName(), Lang.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("effect")));
            iconMeta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
            iconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        icon.setItemMeta(iconMeta);

        return icon;
    }

    /**
     * Gets the icon for a given particle style from the config
     * 
     * @param style The style
     * @return An ItemStack formatted to be displayed in the GUI
     */
    private static ItemStack getItemForStyle(ParticleStyle style, boolean isActive) {
        ItemStack icon = new ItemStack(styleIcons.get(style.getName().toUpperCase()), 1);
        ItemMeta iconMeta = icon.getItemMeta();

        iconMeta.setDisplayName(Lang.GUI_ICON_NAME_COLOR.get() + style.getName());
        if (!isActive) {
            iconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("style") + style.getName()));
        } else {
            iconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("style") + style.getName(), Lang.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("style")));
            iconMeta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
            iconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        icon.setItemMeta(iconMeta);

        return icon;
    }

    /**
     * Gets the icon for a given material to be used for data
     * 
     * @param currentMaterial What material the player is currently using
     * @param dataType What type of data this is (block/item)
     * @param material The material to use for the icon
     * @return An ItemStack formatted to be displayed in the GUI
     */
    private static ItemStack getItemForMaterialData(Material currentMaterial, String dataType, Material material) {
        ItemStack materialIcon = new ItemStack(material, 1);
        ItemMeta materialIconMeta = materialIcon.getItemMeta();

        materialIconMeta.setDisplayName(Lang.GUI_ICON_NAME_COLOR.get() + material.name().toLowerCase());
        if (currentMaterial != material) {
            materialIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced(dataType + " data") + material.name().toLowerCase()));
        } else {
            materialIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced(dataType + " data") + material.name().toLowerCase(), Lang.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced(dataType + " data")));
            materialIconMeta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
            materialIconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        materialIcon.setItemMeta(materialIconMeta);

        return materialIcon;
    }

    /**
     * Gets the icon for a color to be used for data
     * 
     * @param currentColor What color the player is currently using
     * @param colorIndex What color to use
     * @return An ItemStack formatted to be displayed in the GUI
     */
    private static ItemStack getItemForColorData(OrdinaryColor currentColor, int colorIndex) {
        ColorData colorData = colorMapping[colorIndex];
        String formattedDisplayColor = ChatColor.RED.toString() + colorData.getOrdinaryColor().getRed() + " " + ChatColor.GREEN + colorData.getOrdinaryColor().getGreen() + " " + ChatColor.AQUA + colorData.getOrdinaryColor().getBlue();

        ItemStack colorIcon;
        if (colorData.getMaterial() != null) { // Use 1.13 materials
            colorIcon = new ItemStack(colorData.getMaterial());
        } else { // Use < 1.13 dye colors
            colorIcon = new Dye(colorData.getDyeColor()).toItemStack(1);
        }
        ItemMeta colorIconMeta = colorIcon.getItemMeta();

        colorIconMeta.setDisplayName(colorData.getName());
        if (!currentColor.equals(colorData.getOrdinaryColor())) {
            colorIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("color data") + formattedDisplayColor));
        } else {
            colorIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("color data") + formattedDisplayColor, Lang.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("color data")));
            colorIconMeta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
            colorIconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        colorIcon.setItemMeta(colorIconMeta);

        return colorIcon;
    }

    /**
     * Gets the icon for a note to be used for data
     * 
     * @param currentNote What note the player is currently using
     * @param noteIndex What note to use
     * @return An ItemStack formatted to be displayed in the GUI
     */
    private static ItemStack getItemForNoteData(NoteColor currentNote, int noteIndex) {
        ItemStack noteIcon = new ItemStack(Material.NOTE_BLOCK, noteIndex + 1);
        ItemMeta noteIconMeta = noteIcon.getItemMeta();

        noteIconMeta.setDisplayName(Lang.GUI_ICON_NAME_COLOR.get() + "note #" + noteIndex);
        if (currentNote.getValueX() * 24 != noteIndex) {
            noteIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("note data") + "note #" + noteIndex));
        } else {
            noteIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("note data") + "note #" + noteIndex, Lang.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("note data")));
            noteIconMeta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
            noteIconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        }
        noteIcon.setItemMeta(noteIconMeta);

        return noteIcon;
    }

    /**
     * Gets the icon for rainbow color/note data
     * 
     * @return An ItemStack formatted to be displayed in the GUI
     */
    private static ItemStack getItemForRainbowColorData(OrdinaryColor currentColor, DyeColor dyeColor) {
        ColorData colorData = getColorDataFromOrdinaryColor(dyeColor);
        ItemStack rainbowIcon;
        if (colorData.getMaterial() != null) { // Use 1.13 materials
            rainbowIcon = new ItemStack(colorData.getMaterial());
        } else { // Use < 1.13 dye colors
            rainbowIcon = new Dye(colorData.getDyeColor()).toItemStack(1);
        }
        ItemMeta rainbowIconMeta = rainbowIcon.getItemMeta();

        rainbowIconMeta.setDisplayName(rainbowName);
        if (currentColor.getRed() == 999 && currentColor.getGreen() == 999 && currentColor.getBlue() == 999) {
            rainbowIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("color data") + rainbowName, Lang.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("color data")));
            rainbowIconMeta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
            rainbowIconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            rainbowIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("color data") + rainbowName));
        }
        rainbowIcon.setItemMeta(rainbowIconMeta);

        return rainbowIcon;
    }

    /**
     * Gets the icon for rainbow color/note data
     * 
     * @return An ItemStack formatted to be displayed in the GUI
     */
    private static ItemStack getItemForRainbowNoteData(NoteColor currentColor, DyeColor dyeColor) {
        ColorData colorData = getColorDataFromOrdinaryColor(dyeColor);
        ItemStack rainbowIcon;
        if (colorData.getMaterial() != null) { // Use 1.13 materials
            rainbowIcon = new ItemStack(colorData.getMaterial());
        } else { // Use < 1.13 dye colors
            rainbowIcon = new Dye(colorData.getDyeColor()).toItemStack(1);
        }
        ItemMeta rainbowIconMeta = rainbowIcon.getItemMeta();

        rainbowIconMeta.setDisplayName(rainbowName);
        if (currentColor.getValueX() * 24 == 99) {
            rainbowIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("note data") + rainbowName, Lang.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("note data")));
            rainbowIconMeta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
            rainbowIconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            rainbowIconMeta.setLore(Arrays.asList(Lang.GUI_ICON_SETS_TO.getMessageReplaced("note data") + rainbowName));
        }
        rainbowIcon.setItemMeta(rainbowIconMeta);

        return rainbowIcon;
    }

    /**
     * Gets the icon used to return to the previous menu
     * 
     * @return An ItemStack formatted to be displayed in the GUI
     */
    private static ItemStack getItemForBack() {
        ItemStack icon = new ItemStack(Material.ARROW, 1);
        ItemMeta iconMeta = icon.getItemMeta();
        iconMeta.setDisplayName(Lang.GUI_BACK_BUTTON.get());
        icon.setItemMeta(iconMeta);

        return icon;
    }

    /**
     * Gets a ColorData object from its DyeColor
     * 
     * @param color The DyeColor
     * @return The found ColorData object, null if not found
     */
    private static ColorData getColorDataFromOrdinaryColor(DyeColor color) {
        for (ColorData colorData : colorMapping)
            if (colorData.getDyeColor().equals(color)) return colorData;
        return null;
    }

}
