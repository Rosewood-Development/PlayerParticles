package com.esophose.playerparticles.gui;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.SettingManager.GuiIcon;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.util.ParticleUtils;

@SuppressWarnings("deprecation")
public class GuiInventoryEditData extends GuiInventory {
    
    private static Random RANDOM = new Random();
    private static List<Material> BLOCK_MATERIALS, ITEM_MATERIALS;
    private static ColorData[] colorMapping;
    private static ColorData[] rainbowColorMapping;
    private static ColorData[] noteColorMapping;
    
    static {
        colorMapping = new ColorData[] {
            new ColorData(DyeColor.RED, ParticleUtils.closestMatch("ROSE_RED"), new OrdinaryColor(255, 0, 0), Lang.GUI_EDIT_DATA_COLOR_RED),
            new ColorData(DyeColor.ORANGE, ParticleUtils.closestMatch("ORANGE_DYE"), new OrdinaryColor(255, 140, 0), Lang.GUI_EDIT_DATA_COLOR_ORANGE),
            new ColorData(DyeColor.YELLOW, ParticleUtils.closestMatch("DANDELION_YELLOW"), new OrdinaryColor(255, 255, 0), Lang.GUI_EDIT_DATA_COLOR_YELLOW),
            new ColorData(DyeColor.LIME, ParticleUtils.closestMatch("LIME_DYE"), new OrdinaryColor(50, 205, 50), Lang.GUI_EDIT_DATA_COLOR_LIME_GREEN),
            new ColorData(DyeColor.GREEN, ParticleUtils.closestMatch("CACTUS_GREEN"), new OrdinaryColor(0, 128, 0), Lang.GUI_EDIT_DATA_COLOR_GREEN),
            new ColorData(DyeColor.BLUE, ParticleUtils.closestMatch("LAPIS_LAZULI"), new OrdinaryColor(0, 0, 255), Lang.GUI_EDIT_DATA_COLOR_BLUE),
            new ColorData(DyeColor.CYAN, ParticleUtils.closestMatch("CYAN_DYE"), new OrdinaryColor(0, 139, 139), Lang.GUI_EDIT_DATA_COLOR_CYAN),
            new ColorData(DyeColor.LIGHT_BLUE, ParticleUtils.closestMatch("LIGHT_BLUE_DYE"), new OrdinaryColor(173, 216, 230), Lang.GUI_EDIT_DATA_COLOR_LIGHT_BLUE),
            new ColorData(DyeColor.PURPLE, ParticleUtils.closestMatch("PURPLE_DYE"), new OrdinaryColor(138, 43, 226), Lang.GUI_EDIT_DATA_COLOR_PURPLE),
            new ColorData(DyeColor.MAGENTA, ParticleUtils.closestMatch("MAGENTA_DYE"), new OrdinaryColor(202, 31, 123), Lang.GUI_EDIT_DATA_COLOR_MAGENTA),
            new ColorData(DyeColor.PINK, ParticleUtils.closestMatch("PINK_DYE"), new OrdinaryColor(255, 182, 193), Lang.GUI_EDIT_DATA_COLOR_PINK),
            new ColorData(DyeColor.BROWN, ParticleUtils.closestMatch("COCOA_BEANS"), new OrdinaryColor(139, 69, 19), Lang.GUI_EDIT_DATA_COLOR_BROWN),
            new ColorData(DyeColor.BLACK, ParticleUtils.closestMatch("INK_SAC"), new OrdinaryColor(0, 0, 0), Lang.GUI_EDIT_DATA_COLOR_BLACK),
            new ColorData(DyeColor.GRAY, ParticleUtils.closestMatch("GRAY_DYE"), new OrdinaryColor(128, 128, 128), Lang.GUI_EDIT_DATA_COLOR_GRAY),
            new ColorData(DyeColor.getByDyeData((byte)7), ParticleUtils.closestMatch("LIGHT_GRAY_DYE"), new OrdinaryColor(192, 192, 192), Lang.GUI_EDIT_DATA_COLOR_LIGHT_GRAY),
            new ColorData(DyeColor.WHITE, ParticleUtils.closestMatch("BONE_MEAL"), new OrdinaryColor(255, 255, 255), Lang.GUI_EDIT_DATA_COLOR_WHITE),
        };

        rainbowColorMapping = new ColorData[] { 
            colorMapping[0], // Red
            colorMapping[1], // Orange
            colorMapping[2], // Yellow
            colorMapping[3], // Lime
            colorMapping[7], // Light Blue
            colorMapping[5], // Blue
            colorMapping[8]  // Purple
        };
        
        // Note: This is supposed to be a rainbow but there's actually a bug in Minecraft since 1.8 that makes a bunch of them gray
        noteColorMapping = new ColorData[] {
            colorMapping[7],  // Light Blue
            colorMapping[7],  // Light Blue
            colorMapping[13], // Gray
            colorMapping[10], // Pink
            colorMapping[9],  // Magenta
            colorMapping[9],  // Magenta
            colorMapping[0],  // Red
            colorMapping[2],  // Yellow
            colorMapping[2],  // Yellow
            colorMapping[14], // Light Gray
            colorMapping[13], // Gray
            colorMapping[6],  // Cyan
            colorMapping[6],  // Cyan
            colorMapping[6],  // Cyan
            colorMapping[5],  // Blue
            colorMapping[8],  // Purple
            colorMapping[8],  // Purple
            colorMapping[8],  // Purple
            colorMapping[13], // Gray
            colorMapping[4],  // Green
            colorMapping[3],  // Lime
            colorMapping[2],  // Yellow
            colorMapping[4],  // Green
            colorMapping[7],  // Light Blue
            colorMapping[7]   // Light Blue
        };
        
        BLOCK_MATERIALS = new ArrayList<Material>();
        ITEM_MATERIALS = new ArrayList<Material>();
        
        Inventory tempInventory = Bukkit.createInventory(null, 9);
        for (Material mat : Material.values()) {
            // Verify an ItemStack of the material can be placed into an inventory. In 1.12 and up this can easily be checked with mat.isItem(), but that doesn't exist pre-1.12
            tempInventory.clear();
            tempInventory.setItem(0, new ItemStack(mat, 1));
            ItemStack itemStack = tempInventory.getItem(0);
            if (itemStack != null) {
                if (mat.isBlock()) {
                    BLOCK_MATERIALS.add(mat);
                } else if (!mat.isBlock()) {
                    ITEM_MATERIALS.add(mat);
                }
            }
        }
    }

    public GuiInventoryEditData(PPlayer pplayer, ParticlePair editingParticle, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, LangManager.getText(Lang.GUI_SELECT_DATA)));
        
        ParticleEffect pe = editingParticle.getEffect();
        if (pe.hasProperty(ParticleProperty.COLORABLE)) {
            if (pe == ParticleEffect.NOTE) { // Note data
                this.populateNoteData(editingParticle, callbackList, callbackListPosition);
            } else { // Color data
                this.populateColorData(editingParticle, callbackList, callbackListPosition);
            }
        } else if (pe.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            if (pe == ParticleEffect.ITEM) { // Item data
                this.populateItemData(editingParticle, callbackList, callbackListPosition);
            } else { // Block data
                this.populateBlockData(editingParticle, callbackList, callbackListPosition);
            }
        }
        
        // Back Button
        GuiActionButton backButton = new GuiActionButton(INVENTORY_SIZE - 1, GuiIcon.BACK.get(), LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_BACK_BUTTON), new String[] {}, (button, isShiftClick) -> {
            callbackList.get(callbackListPosition - 1).execute();
        });
        this.actionButtons.add(backButton);
        
        this.populate();
    }
    
    private void populateColorData(ParticlePair editingParticle, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        int index = 10;
        int nextWrap = 17;
        for (int i = 0; i < colorMapping.length; i++) {
            ColorData colorData = colorMapping[i];
            String formattedDisplayColor = ChatColor.RED.toString() + colorData.getOrdinaryColor().getRed() + " " + ChatColor.GREEN + colorData.getOrdinaryColor().getGreen() + " " + ChatColor.AQUA + colorData.getOrdinaryColor().getBlue();

            // Color Data Buttons
            GuiActionButton setColorButton = new GuiActionButton(index,
                                                                 colorData,
                                                                 colorData.getName(),
                                                                 new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, formattedDisplayColor) },
                                                                 (button, isShiftClick) -> {
                                                                     editingParticle.setColor(colorData.getOrdinaryColor());
                                                                     callbackList.get(callbackListPosition + 1).execute();
                                                                 });
            this.actionButtons.add(setColorButton);
            
            index++;
            if (index == nextWrap) { // Loop around border
                nextWrap += 9;
                index += 2; 
            }
        }
        
        // Rainbow Color Data Button
        GuiActionButton setRainbowColorButton = new GuiActionButton(40,
                                                                    rainbowColorMapping,
                                                                    LangManager.getText(Lang.RAINBOW),
                                                                    new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, LangManager.getText(Lang.RAINBOW)) },
                                                                    (button, isShiftClick) -> {
                                                                        editingParticle.setColor(new OrdinaryColor(999, 999, 999));
                                                                        callbackList.get(callbackListPosition + 1).execute();
                                                                    });
        this.actionButtons.add(setRainbowColorButton);
    }
    
    private void populateNoteData(ParticlePair editingParticle, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        for (int i = 0; i < noteColorMapping.length; i++) {
            ColorData colorData = noteColorMapping[i];
            String formattedDisplayName = LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_SELECT_DATA_NOTE, i) + " (" + colorData.getName() + LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + ")";
            String formattedDescription = LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, LangManager.getText(Lang.GUI_SELECT_DATA_NOTE, i));

            // Note Color Buttons
            int noteIndex = i;
            GuiActionButton setColorButton = new GuiActionButton(i,
                                                                 colorData,
                                                                 formattedDisplayName,
                                                                 new String[] { formattedDescription },
                                                                 (button, isShiftClick) -> {
                                                                     editingParticle.setNoteColor(new NoteColor(noteIndex));
                                                                     callbackList.get(callbackListPosition + 1).execute();
                                                                 });
            this.actionButtons.add(setColorButton);
        }
        
        // Rainbow Color Data Button
        GuiActionButton setRainbowColorButton = new GuiActionButton(40,
                                                                    rainbowColorMapping,
                                                                    LangManager.getText(Lang.RAINBOW),
                                                                    new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, LangManager.getText(Lang.RAINBOW)) },
                                                                    (button, isShiftClick) -> {
                                                                        editingParticle.setNoteColor(new NoteColor(99));
                                                                        callbackList.get(callbackListPosition + 1).execute();
                                                                    });
        this.actionButtons.add(setRainbowColorButton);
    }
    
    private void populateItemData(ParticlePair editingParticle, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        List<Material> materialBag = new ArrayList<Material>();
        while (materialBag.size() < 36) { // Grab 36 random materials that are an item
            Material randomMaterial = ITEM_MATERIALS.get(RANDOM.nextInt(ITEM_MATERIALS.size()));
            if (!materialBag.contains(randomMaterial)) materialBag.add(randomMaterial);
        }
        
        for (int i = 0; i < materialBag.size(); i++) {
            // Item Data Button
            Material material = materialBag.get(i);
            GuiActionButton setRainbowColorButton = new GuiActionButton(i,
                                                                        material,
                                                                        LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + material.name().toLowerCase(),
                                                                        new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, material.name().toLowerCase()) },
                                                                        (button, isShiftClick) -> {
                                                                            editingParticle.setItemMaterial(material);
                                                                            callbackList.get(callbackListPosition + 1).execute();
                                                                        });
            this.actionButtons.add(setRainbowColorButton);
        }
        
        // Randomize Button, re-randomizes the icons
        GuiActionButton randomizeButton = new GuiActionButton(45, 
                                                              GuiIcon.RANDOMIZE.get(), 
                                                              LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_SELECT_DATA_RANDOMIZE_ITEMS), 
                                                              new String[] { LangManager.getText(Lang.GUI_COLOR_SUBTEXT) + LangManager.getText(Lang.GUI_SELECT_DATA_RANDOMIZE_ITEMS_DESCRIPTION) },
                                                              (block, isShiftClick) -> {
                                                                  callbackList.get(callbackListPosition).execute(); // Just reopen the same inventory
                                                              });
        this.actionButtons.add(randomizeButton);
    }
    
    private void populateBlockData(ParticlePair editingParticle, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        List<Material> materialBag = new ArrayList<Material>();
        while (materialBag.size() < 36) { // Grab 36 random materials that are an item
            Material randomMaterial = BLOCK_MATERIALS.get(RANDOM.nextInt(BLOCK_MATERIALS.size()));
            if (!materialBag.contains(randomMaterial)) materialBag.add(randomMaterial);
        }
        
        for (int i = 0; i < materialBag.size(); i++) {
            // Item Data Button
            Material material = materialBag.get(i);
            GuiActionButton setRainbowColorButton = new GuiActionButton(i,
                                                                        material,
                                                                        LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + material.name().toLowerCase(),
                                                                        new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, material.name().toLowerCase()) },
                                                                        (button, isShiftClick) -> {
                                                                            editingParticle.setBlockMaterial(material);
                                                                            callbackList.get(callbackListPosition + 1).execute();
                                                                        });
            this.actionButtons.add(setRainbowColorButton);
        }
        
        // Randomize Button, re-randomizes the icons
        GuiActionButton randomizeButton = new GuiActionButton(45, 
                                                              GuiIcon.RANDOMIZE.get(), 
                                                              LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_SELECT_DATA_RANDOMIZE_BLOCKS), 
                                                              new String[] { LangManager.getText(Lang.GUI_COLOR_SUBTEXT) + LangManager.getText(Lang.GUI_SELECT_DATA_RANDOMIZE_BLOCKS_DESCRIPTION) },
                                                              (block, isShiftClick) -> {
                                                                  callbackList.get(callbackListPosition).execute(); // Just reopen the same inventory
                                                              });
        this.actionButtons.add(randomizeButton);
    }
    
    /**
     * A data class used for storing information about the color data
     */
    protected static class ColorData {
        private DyeColor dyeColor;
        private Material material;
        private OrdinaryColor ordinaryColor;
        private Lang name;
        
        public ColorData(DyeColor dyeColor, Material material, OrdinaryColor ordinaryColor, Lang name) {
            this.dyeColor = dyeColor;
            this.material = material;
            this.ordinaryColor = ordinaryColor;
            this.name = name;
        }
        
        /**
         * Get the DyeColor 
         * 
         * @return The DyeColor
         */
        public DyeColor getDyeColor() {
            return this.dyeColor;
        }
        
        /**
         * Get the Material representing this color
         * 
         * @return The Material
         */
        public Material getMaterial() {
            return this.material;
        }
        
        /**
         * Get the OrdinaryColor representing this color
         * 
         * @return The OrdinaryColor
         */
        public OrdinaryColor getOrdinaryColor() {
            return this.ordinaryColor;
        }
        
        /**
         * Get the name of this color
         * 
         * @return The name of this color
         */
        public String getName() {
            return LangManager.getText(this.name);
        }
    }

}
