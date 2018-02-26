/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.Dye;
import org.bukkit.material.Wool;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.library.ParticleEffect;
import com.esophose.playerparticles.library.ParticleEffect.BlockData;
import com.esophose.playerparticles.library.ParticleEffect.ItemData;
import com.esophose.playerparticles.library.ParticleEffect.NoteColor;
import com.esophose.playerparticles.library.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.library.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.MessageManager.MessageType;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;
import com.esophose.playerparticles.util.ParticleUtils;

/**
 * This class provides a collection of static methods for modifying your
 * particle/style/data through the use of a GUI
 */
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
    private static LinkedHashMap<DyeColor, OrdinaryColor> colorMapping;
    private static String[] colorMappingNames;

    /**
     * DyeColor array in the order of the rainbow
     * Color index for the wool color to display next
     */
    private static DyeColor[] rainbowColors;
    private static int rainbowColorsIndex = 0;

    /**
     * 28 of each block/item Materials for block/item data
     */
    private static Material[] blockMaterials;
    private static Material[] itemMaterials;

    static { // @formatter:off
        colorMapping = new LinkedHashMap<DyeColor, OrdinaryColor>();

        colorMapping.put(DyeColor.RED, new OrdinaryColor(255, 0, 0));
        colorMapping.put(DyeColor.ORANGE, new OrdinaryColor(255, 140, 0));
        colorMapping.put(DyeColor.YELLOW, new OrdinaryColor(255, 255, 0));
        colorMapping.put(DyeColor.LIME, new OrdinaryColor(50, 205, 50));
        colorMapping.put(DyeColor.GREEN, new OrdinaryColor(0, 128, 0));
        colorMapping.put(DyeColor.BLUE, new OrdinaryColor(0, 0, 255));
        colorMapping.put(DyeColor.CYAN, new OrdinaryColor(0, 139, 139));
        colorMapping.put(DyeColor.LIGHT_BLUE, new OrdinaryColor(173, 216, 230));
        colorMapping.put(DyeColor.PURPLE, new OrdinaryColor(138, 43, 226));
        colorMapping.put(DyeColor.MAGENTA, new OrdinaryColor(202, 31, 123));
        colorMapping.put(DyeColor.PINK, new OrdinaryColor(255, 182, 193));
        colorMapping.put(DyeColor.BROWN, new OrdinaryColor(139, 69, 19));
        colorMapping.put(DyeColor.BLACK, new OrdinaryColor(0, 0, 0));
        colorMapping.put(DyeColor.GRAY, new OrdinaryColor(128, 128, 128));
        colorMapping.put(DyeColor.SILVER, new OrdinaryColor(192, 192, 192));
        colorMapping.put(DyeColor.WHITE, new OrdinaryColor(255, 255, 255));

        colorMappingNames = new String[] { 
            ChatColor.RED + "red", 
            ChatColor.GOLD + "orange", 
            ChatColor.YELLOW + "yellow", 
            ChatColor.GREEN + "lime green", 
            ChatColor.DARK_GREEN + "green", 
            ChatColor.DARK_BLUE + "blue", 
            ChatColor.DARK_AQUA + "cyan", 
            ChatColor.AQUA + "light blue", 
            ChatColor.DARK_PURPLE + "purple", 
            ChatColor.LIGHT_PURPLE + "magenta", 
            ChatColor.LIGHT_PURPLE + "pink", 
            ChatColor.GOLD + "brown", 
            ChatColor.DARK_GRAY + "black", 
            ChatColor.DARK_GRAY + "gray", 
            ChatColor.GRAY + "light gray", 
            ChatColor.WHITE + "white" 
        };

        rainbowColors = new DyeColor[] { DyeColor.RED, DyeColor.ORANGE, DyeColor.YELLOW, DyeColor.LIME, DyeColor.LIGHT_BLUE, DyeColor.BLUE, DyeColor.PURPLE };

        blockMaterials = new Material[] {
            Material.STONE,
            Material.GRASS,
            Material.TNT,
            Material.COBBLESTONE,
            Material.WOOD,
            Material.BEDROCK,
            Material.SAND,
            Material.LOG,
            Material.SPONGE,
            Material.GLASS,
            Material.WOOL,
            Material.IRON_BLOCK,
            Material.GOLD_BLOCK,
            Material.DIAMOND_BLOCK,
            Material.EMERALD_BLOCK,
            Material.COAL_BLOCK,
            Material.REDSTONE_BLOCK,
            Material.BOOKSHELF,
            Material.ICE,
            Material.CLAY,
            Material.PUMPKIN,
            Material.MELON_BLOCK,
            Material.NETHERRACK,
            Material.SOUL_SAND,
            Material.GLOWSTONE,
            Material.NETHER_BRICK,
            Material.ENDER_STONE,
            Material.PRISMARINE
        };

        itemMaterials = new Material[] {
            Material.COAL,
            Material.IRON_INGOT,
            Material.GOLD_INGOT,
            Material.REDSTONE,
            Material.EMERALD,
            Material.QUARTZ,
            Material.CLAY_BRICK,
            Material.GLOWSTONE_DUST,
            Material.SUGAR_CANE,
            Material.FLINT,
            Material.POTATO_ITEM,
            Material.CARROT_ITEM,
            Material.SNOW_BALL,
            Material.BONE,
            Material.ENDER_PEARL,
            Material.BLAZE_POWDER,
            Material.NETHER_STALK,
            Material.FIREBALL,
            Material.CHORUS_FRUIT,
            Material.PRISMARINE_CRYSTALS,
            Material.SULPHUR,
            Material.APPLE,
            Material.MELON,
            Material.COOKIE,
            Material.IRON_SPADE,
            Material.COMPASS,
            Material.WATCH,
            Material.NAME_TAG
        };
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

        defaultMenuIcons[0] = ParticleUtils.closestMatch(config.getString("gui-icon.main-menu.EFFECT"));
        defaultMenuIcons[1] = ParticleUtils.closestMatch(config.getString("gui-icon.main-menu.STYLE"));
        defaultMenuIcons[2] = ParticleUtils.closestMatch(config.getString("gui-icon.main-menu.DATA"));
        for (int i = 0; i < defaultMenuIcons.length; i++)
            if (defaultMenuIcons[i] == null) defaultMenuIcons[i] = Material.BARRIER;

        for (ParticleEffect effect : ParticleEffect.values()) {
            String effectName = effect.name();
            Material iconMaterial = ParticleUtils.closestMatch(config.getString("gui-icon.effect." + effectName));
            if (iconMaterial == null) iconMaterial = Material.BARRIER; // Missing icon or invalid? Replace it with a barrier instead to fail safety.
            effectIcons.put(effectName, iconMaterial);
        }

        for (ParticleStyle style : ParticleStyleManager.getStyles()) {
            String styleName = style.getName().toUpperCase();
            Material iconMaterial = ParticleUtils.closestMatch(config.getString("gui-icon.style." + styleName));
            if (iconMaterial == null) iconMaterial = Material.BARRIER; // Missing icon or invalid? Replace it with a barrier instead to fail safety.
            styleIcons.put(styleName, iconMaterial);
        }

        new PlayerParticlesGui().runTaskTimer(PlayerParticles.getPlugin(), 0, 10);
    }

    /**
     * Updates all color/note data inventories to display the rainbow icon
     * Removes any players who are no longer online from playerGuiInventories
     */
    public void run() {
        List<UUID> toRemoveList = new ArrayList<UUID>();

        for (Map.Entry<UUID, GuiInventory> entry : playerGuiInventories.entrySet()) {
            PPlayer pplayer = ConfigManager.getInstance().getPPlayer(entry.getKey(), false);
            Player player = Bukkit.getPlayer(pplayer.getUniqueId());

            if (player == null) {
                toRemoveList.add(pplayer.getUniqueId());
                continue;
            }

            GuiInventory guiInventory = entry.getValue();
            Inventory inventory = guiInventory.getInventory();

            if (player.getOpenInventory().getTopInventory().equals(inventory) && guiInventory.getGuiState() == GuiState.DATA && pplayer.getParticleEffect().hasProperty(ParticleProperty.COLORABLE)) {
                ItemStack rainbowIcon;
                if (pplayer.getParticleEffect() != ParticleEffect.NOTE) {
                    rainbowIcon = getItemForRainbowColorData(pplayer.getColorData(), rainbowColors[rainbowColorsIndex]);
                } else {
                    rainbowIcon = getItemForRainbowNoteData(pplayer.getNoteColorData(), rainbowColors[rainbowColorsIndex]);
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
     * Changes the GUI to the indicated state
     * If the GUI isn't open yet, it gets opened
     * 
     * @param p The pplayer
     * @param state The new state
     */
    public static void changeState(PPlayer p, GuiState state) {
        Player player = p.getPlayer();

        if (PermissionManager.getEffectsUserHasPermissionFor(player).size() == 1 || PermissionManager.getStylesUserHasPermissionFor(player).size() == 1 || (state == GuiState.DATA && p.getParticleSpawnData() == null && p.getParticleSpawnColor() == null)) return;

        // Update the state and create an inventory for the player if one isn't already open for them
        // If they have the wrong inventory open for some reason, create a new one and open it for them
        if (playerGuiInventories.containsKey(p.getUniqueId())) {
            GuiInventory guiInventory = playerGuiInventories.get(p.getUniqueId());
            guiInventory.setGuiState(state);
            if (player.getOpenInventory().getTopInventory() != guiInventory.getInventory()) {
                Inventory ppInventory = Bukkit.createInventory(null, INVENTORY_SIZE, "PlayerParticles");
                player.openInventory(ppInventory);
                guiInventory.setInventory(ppInventory);
            }
        } else {
            Inventory ppInventory = Bukkit.createInventory(null, INVENTORY_SIZE, "PlayerParticles");
            player.openInventory(ppInventory);
            playerGuiInventories.put(p.getUniqueId(), new GuiInventory(ppInventory, state));
        }

        switch (state) {
        case DEFAULT:
            populateDefault(p);
            break;
        case EFFECT:
            populateEffect(p);
            break;
        case STYLE:
            populateStyle(p);
            break;
        case DATA:
            populateData(p);
            break;
        }
    }

    /**
     * Opens the menu to go to other menus: effect, style, data
     * 
     * @param p The PPlayer
     */
    private static void populateDefault(PPlayer p) {
        Player player = p.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        inventory.clear(); // Make sure the inventory is always empty before we start adding items

        ItemStack currentIcon = new ItemStack(Material.SKULL_ITEM, 1, (short) SkullType.PLAYER.ordinal());
        SkullMeta currentIconMeta = (SkullMeta) currentIcon.getItemMeta();
        currentIconMeta.setDisplayName(ChatColor.GREEN + player.getName());
        String[] currentIconLore = new String[3];
        currentIconLore[0] = ChatColor.YELLOW + "Effect: " + ChatColor.AQUA + p.getParticleEffect().getName();
        currentIconLore[1] = ChatColor.YELLOW + "Style: " + ChatColor.AQUA + p.getParticleStyle().getName();
        currentIconLore[2] = ChatColor.YELLOW + "Active Data: " + ChatColor.AQUA + p.getParticleDataString();
        currentIconMeta.setLore(Arrays.asList(currentIconLore));
        currentIconMeta.setOwner(player.getName());
        currentIcon.setItemMeta(currentIconMeta);

        ItemStack effectIcon = new ItemStack(defaultMenuIcons[0], 1);
        ItemMeta effectIconMeta = effectIcon.getItemMeta();
        effectIconMeta.setDisplayName(ChatColor.GREEN + "Effect");
        effectIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SET_YOUR.getMessageReplaced("effect")));
        if (PermissionManager.getEffectsUserHasPermissionFor(player).size() == 1) { // Always has access to NONE
            effectIconMeta.setLore(Arrays.asList(MessageType.GUI_NO_ACCESS_TO.getMessageReplaced("effects")));
        }
        effectIcon.setItemMeta(effectIconMeta);

        ItemStack styleIcon = new ItemStack(defaultMenuIcons[1], 1);
        ItemMeta styleIconMeta = styleIcon.getItemMeta();
        styleIconMeta.setDisplayName(ChatColor.GREEN + "Style");
        styleIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SET_YOUR.getMessageReplaced("style")));
        if (PermissionManager.getStylesUserHasPermissionFor(player).size() == 1) { // Always has access to NONE
            styleIconMeta.setLore(Arrays.asList(MessageType.GUI_NO_ACCESS_TO.getMessageReplaced("styles")));
        }
        styleIcon.setItemMeta(styleIconMeta);

        ItemStack dataIcon = new ItemStack(defaultMenuIcons[2], 1);
        ItemMeta dataIconMeta = dataIcon.getItemMeta();
        dataIconMeta.setDisplayName(ChatColor.GREEN + "Data");
        ParticleEffect pe = p.getParticleEffect();
        String dataType = "data";
        if (pe.hasProperty(ParticleProperty.COLORABLE)) if (pe == ParticleEffect.NOTE) dataType = "note " + dataType;
        else dataType = "color " + dataType;
        else if (pe.hasProperty(ParticleProperty.REQUIRES_DATA)) if (pe == ParticleEffect.ITEM_CRACK) dataType = "item " + dataType;
        else dataType = "block " + dataType;
        dataIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SET_YOUR.getMessageReplaced(dataType)));
        if (p.getParticleSpawnData() == null && p.getParticleSpawnColor() == null) {
            dataIconMeta.setLore(Arrays.asList(MessageType.GUI_NO_DATA.getMessage()));
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
     * @param p The PPlayer
     */
    private static void populateEffect(PPlayer p) {
        Player player = p.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        inventory.clear(); // Make sure the inventory is always empty before we start adding items

        List<String> effectsUserHasPermissionFor = PermissionManager.getEffectsUserHasPermissionFor(player);
        for (int i = 0; i < effectsUserHasPermissionFor.size(); i++) {
            String s = effectsUserHasPermissionFor.get(i);
            ParticleEffect effect = ParticleManager.effectFromString(s);
            inventory.setItem(i, getItemForEffect(effect, effect == p.getParticleEffect()));
        }

        inventory.setItem(INVENTORY_SIZE - 1, getItemForBack());
    }

    /**
     * Opens the menu that allows you to select a style you have permission for
     * 
     * @param p The PPlayer
     */
    private static void populateStyle(PPlayer p) {
        Player player = p.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        inventory.clear(); // Make sure the inventory is always empty before we start adding items

        List<String> stylesUserHasPermissionFor = PermissionManager.getStylesUserHasPermissionFor(player);
        for (int i = 0; i < stylesUserHasPermissionFor.size(); i++) {
            String s = stylesUserHasPermissionFor.get(i);
            ParticleStyle style = ParticleStyleManager.styleFromString(s);
            inventory.setItem(i, getItemForStyle(style, style == p.getParticleStyle()));
        }

        inventory.setItem(INVENTORY_SIZE - 1, getItemForBack());
    }

    /**
     * Opens the menu that allows you to select some preset data
     * 
     * @param p The PPlayer
     */
    private static void populateData(PPlayer p) {
        Player player = p.getPlayer();
        Inventory inventory = player.getOpenInventory().getTopInventory();

        inventory.clear(); // Make sure the inventory is always empty before we start adding items

        // There are a lot of for loops here, somebody submit a pull request if you have a better way of doing this
        ParticleEffect pe = p.getParticleEffect();
        if (pe.hasProperty(ParticleProperty.COLORABLE)) {
            if (pe == ParticleEffect.NOTE) { // Note data
                NoteColor currentNote = p.getNoteColorData();
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

                inventory.setItem(40, getItemForRainbowNoteData(p.getNoteColorData(), rainbowColors[rainbowColorsIndex]));
            } else { // Color data
                OrdinaryColor currentColor = p.getColorData();
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

                inventory.setItem(40, getItemForRainbowColorData(p.getColorData(), rainbowColors[rainbowColorsIndex]));
            }
        } else if (pe.hasProperty(ParticleProperty.REQUIRES_DATA)) {
            if (pe == ParticleEffect.ITEM_CRACK) { // Item data
                Material currentItemMaterial = p.getItemData().getMaterial();
                int itemMaterialIndex = 0;
                for (int i = 10; i <= 16; i++) { // Top row
                    inventory.setItem(i, getItemForMaterialData(currentItemMaterial, "item", itemMaterials[itemMaterialIndex]));
                    itemMaterialIndex++;
                }
                for (int i = 19; i <= 25; i++) { // Middle 1 row
                    inventory.setItem(i, getItemForMaterialData(currentItemMaterial, "item", itemMaterials[itemMaterialIndex]));
                    itemMaterialIndex++;
                }
                for (int i = 28; i <= 34; i++) { // Middle 2 row
                    inventory.setItem(i, getItemForMaterialData(currentItemMaterial, "item", itemMaterials[itemMaterialIndex]));
                    itemMaterialIndex++;
                }
                for (int i = 37; i <= 43; i++) { // Bottom row
                    inventory.setItem(i, getItemForMaterialData(currentItemMaterial, "item", itemMaterials[itemMaterialIndex]));
                    itemMaterialIndex++;
                }
            } else { // Block data
                Material currentBlockMaterial = p.getBlockData().getMaterial();
                int blockMaterialIndex = 0;
                for (int i = 10; i <= 16; i++) { // Top row
                    inventory.setItem(i, getItemForMaterialData(currentBlockMaterial, "block", blockMaterials[blockMaterialIndex]));
                    blockMaterialIndex++;
                }
                for (int i = 19; i <= 25; i++) { // Middle 1 row
                    inventory.setItem(i, getItemForMaterialData(currentBlockMaterial, "block", blockMaterials[blockMaterialIndex]));
                    blockMaterialIndex++;
                }
                for (int i = 28; i <= 34; i++) { // Middle 2 row
                    inventory.setItem(i, getItemForMaterialData(currentBlockMaterial, "block", blockMaterials[blockMaterialIndex]));
                    blockMaterialIndex++;
                }
                for (int i = 37; i <= 43; i++) { // Bottom row
                    inventory.setItem(i, getItemForMaterialData(currentBlockMaterial, "block", blockMaterials[blockMaterialIndex]));
                    blockMaterialIndex++;
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
        if (!(e.getWhoClicked() instanceof Player)) return; // Not sure if I actually have to check this

        Player player = (Player) e.getWhoClicked();
        GuiInventory guiInventory = playerGuiInventories.get(player.getUniqueId());

        if (guiInventory == null || !guiInventory.getInventory().equals(e.getView().getTopInventory())) return; // Make sure it is the right inventory

        PPlayer pplayer = ConfigManager.getInstance().getPPlayer(player.getUniqueId(), false);

        e.setCancelled(true);

        ItemStack clicked = e.getCurrentItem();
        if (clicked == null || clicked.getType() == Material.AIR) return; // Clicked on an empty slot, do nothing

        // Check back button. This is common for most menus
        if (clicked.getItemMeta().getDisplayName().equals(MessageType.GUI_BACK_BUTTON.getMessage())) {
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
            ConfigManager.getInstance().savePPlayer(pplayer.getUniqueId(), ParticleManager.effectFromString(name));
            changeState(pplayer, GuiState.DEFAULT);
            break;
        case STYLE:
            ConfigManager.getInstance().savePPlayer(pplayer.getUniqueId(), ParticleStyleManager.styleFromString(name));
            changeState(pplayer, GuiState.DEFAULT);
            break;
        case DATA:
            ParticleEffect pe = pplayer.getParticleEffect();
            if (pe.hasProperty(ParticleProperty.COLORABLE)) {
                if (pe == ParticleEffect.NOTE) {
                    if (clicked.getItemMeta().getDisplayName().equals(rainbowName)) {
                        ConfigManager.getInstance().savePPlayer(pplayer.getUniqueId(), new NoteColor(99));
                    } else {
                        int note = Integer.parseInt(ChatColor.stripColor(clicked.getItemMeta().getDisplayName()).substring(6));
                        ConfigManager.getInstance().savePPlayer(pplayer.getUniqueId(), new NoteColor(note));
                    }
                } else {
                    if (clicked.getItemMeta().getDisplayName().equals(rainbowName)) {
                        ConfigManager.getInstance().savePPlayer(pplayer.getUniqueId(), new OrdinaryColor(999, 999, 999));
                    } else {
                        for (int i = 0; i < colorMappingNames.length; i++) {
                            if (clicked.getItemMeta().getDisplayName().equals(colorMappingNames[i])) {
                                ConfigManager.getInstance().savePPlayer(pplayer.getUniqueId(), getColorMappingEntry(i).getValue());
                            }
                        }
                    }
                }
            } else if (pe.hasProperty(ParticleProperty.REQUIRES_DATA)) {
                Material clickedMaterial = clicked.getType(); // All preset materials have a data value of 0
                if (pe == ParticleEffect.ITEM_CRACK) {
                    ConfigManager.getInstance().savePPlayer(pplayer.getUniqueId(), new ItemData(clickedMaterial, (byte) 0));
                } else {
                    ConfigManager.getInstance().savePPlayer(pplayer.getUniqueId(), new BlockData(clickedMaterial, (byte) 0));
                }
            }

            changeState(pplayer, GuiState.DEFAULT);
            break;
        }
    }

    /**
     * Gets a DyeColor, OrdinaryColor pair from the colorMapping LinkedHashSet
     * 
     * @param colorIndex The index to get the pair from
     * @return A DyeColor, OrdinaryColor pair
     */
    @SuppressWarnings("unchecked")
    private static Map.Entry<DyeColor, OrdinaryColor> getColorMappingEntry(int colorIndex) {
        Set<Map.Entry<DyeColor, OrdinaryColor>> mapSet = colorMapping.entrySet();
        return (Map.Entry<DyeColor, OrdinaryColor>) mapSet.toArray()[colorIndex];
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

        iconMeta.setDisplayName(MessageType.GUI_ICON_NAME_COLOR.getMessage() + effect.getName());
        if (!isActive) {
            iconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("effect") + effect.getName()));
        } else {
            iconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("effect") + effect.getName(), MessageType.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("effect")));
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

        iconMeta.setDisplayName(MessageType.GUI_ICON_NAME_COLOR.getMessage() + style.getName());
        if (!isActive) {
            iconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("style") + style.getName()));
        } else {
            iconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("style") + style.getName(), MessageType.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("style")));
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

        materialIconMeta.setDisplayName(MessageType.GUI_ICON_NAME_COLOR.getMessage() + material.name().toLowerCase());
        if (currentMaterial != material) {
            materialIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced(dataType + " data") + material.name().toLowerCase()));
        } else {
            materialIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced(dataType + " data") + material.name().toLowerCase(), MessageType.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced(dataType + " data")));
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
        Map.Entry<DyeColor, OrdinaryColor> colorMappingEntry = getColorMappingEntry(colorIndex);
        DyeColor dyeColor = colorMappingEntry.getKey();
        OrdinaryColor displayColor = colorMappingEntry.getValue();
        String formattedDisplayColor = ChatColor.RED.toString() + displayColor.getRed() + " " + ChatColor.GREEN + displayColor.getGreen() + " " + ChatColor.AQUA + displayColor.getBlue();

        ItemStack colorIcon = new Dye(dyeColor).toItemStack(1);
        ItemMeta colorIconMeta = colorIcon.getItemMeta();

        colorIconMeta.setDisplayName(colorMappingNames[colorIndex]);
        if (!currentColor.equals(displayColor)) {
            colorIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("color data") + formattedDisplayColor));
        } else {
            colorIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("color data") + formattedDisplayColor, MessageType.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("color data")));
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

        noteIconMeta.setDisplayName(MessageType.GUI_ICON_NAME_COLOR.getMessage() + "note #" + noteIndex);
        if (currentNote.getValueX() * 24 != noteIndex) {
            noteIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("note data") + "note #" + noteIndex));
        } else {
            noteIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("note data") + "note #" + noteIndex, MessageType.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("note data")));
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
        ItemStack rainbowIcon = new Wool(dyeColor).toItemStack(1);
        ItemMeta rainbowIconMeta = rainbowIcon.getItemMeta();

        rainbowIconMeta.setDisplayName(rainbowName);
        if (currentColor.getRed() == 999 && currentColor.getGreen() == 999 && currentColor.getBlue() == 999) {
            rainbowIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("color data") + rainbowName, MessageType.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("color data")));
            rainbowIconMeta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
            rainbowIconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            rainbowIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("color data") + rainbowName));
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
        ItemStack rainbowIcon = new Wool(dyeColor).toItemStack(1);
        ItemMeta rainbowIconMeta = rainbowIcon.getItemMeta();

        rainbowIconMeta.setDisplayName(rainbowName);
        if (currentColor.getValueX() * 24 == 99) {
            rainbowIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("note data") + rainbowName, MessageType.GUI_ICON_CURRENT_ACTIVE.getMessageReplaced("note data")));
            rainbowIconMeta.addEnchant(Enchantment.ARROW_INFINITE, -1, true);
            rainbowIconMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        } else {
            rainbowIconMeta.setLore(Arrays.asList(MessageType.GUI_ICON_SETS_TO.getMessageReplaced("note data") + rainbowName));
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
        iconMeta.setDisplayName(MessageType.GUI_BACK_BUTTON.getMessage());
        icon.setItemMeta(iconMeta);

        return icon;
    }

}
