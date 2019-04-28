package com.esophose.playerparticles.gui;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.SettingManager.GuiIcon;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.util.NMSUtil;
import com.esophose.playerparticles.util.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

@SuppressWarnings("deprecation")
public class GuiInventoryEditData extends GuiInventory {

    private static List<Material> BLOCK_MATERIALS, ITEM_MATERIALS;
    private static ColorData[] colorMapping;
    private static ColorData[] rainbowColorMapping;
    private static ColorData[] noteColorMapping, noteColorMappingOld;

    static {
        colorMapping = new ColorData[]{
                new ColorData(DyeColor.RED, ParticleUtils.closestMatchWithFallback(false, "RED_DYE", "ROSE_RED"), new OrdinaryColor(255, 0, 0), Lang.GUI_EDIT_DATA_COLOR_RED),
                new ColorData(DyeColor.ORANGE, ParticleUtils.closestMatchWithFallback(false, "ORANGE_DYE"), new OrdinaryColor(255, 140, 0), Lang.GUI_EDIT_DATA_COLOR_ORANGE),
                new ColorData(DyeColor.YELLOW, ParticleUtils.closestMatchWithFallback(false, "YELLOW_DYE", "DANDELION_YELLOW"), new OrdinaryColor(255, 255, 0), Lang.GUI_EDIT_DATA_COLOR_YELLOW),
                new ColorData(DyeColor.LIME, ParticleUtils.closestMatchWithFallback(false, "LIME_DYE"), new OrdinaryColor(50, 205, 50), Lang.GUI_EDIT_DATA_COLOR_LIME_GREEN),
                new ColorData(DyeColor.GREEN, ParticleUtils.closestMatchWithFallback(false, "GREEN_DYE", "CACTUS_GREEN"), new OrdinaryColor(0, 128, 0), Lang.GUI_EDIT_DATA_COLOR_GREEN),
                new ColorData(DyeColor.BLUE, ParticleUtils.closestMatchWithFallback(false, "BLUE_DYE", "LAPIS_LAZULI"), new OrdinaryColor(0, 0, 255), Lang.GUI_EDIT_DATA_COLOR_BLUE),
                new ColorData(DyeColor.CYAN, ParticleUtils.closestMatchWithFallback(false, "CYAN_DYE"), new OrdinaryColor(0, 139, 139), Lang.GUI_EDIT_DATA_COLOR_CYAN),
                new ColorData(DyeColor.LIGHT_BLUE, ParticleUtils.closestMatchWithFallback(false, "LIGHT_BLUE_DYE"), new OrdinaryColor(173, 216, 230), Lang.GUI_EDIT_DATA_COLOR_LIGHT_BLUE),
                new ColorData(DyeColor.PURPLE, ParticleUtils.closestMatchWithFallback(false, "PURPLE_DYE"), new OrdinaryColor(138, 43, 226), Lang.GUI_EDIT_DATA_COLOR_PURPLE),
                new ColorData(DyeColor.MAGENTA, ParticleUtils.closestMatchWithFallback(false, "MAGENTA_DYE"), new OrdinaryColor(202, 31, 123), Lang.GUI_EDIT_DATA_COLOR_MAGENTA),
                new ColorData(DyeColor.PINK, ParticleUtils.closestMatchWithFallback(false, "PINK_DYE"), new OrdinaryColor(255, 182, 193), Lang.GUI_EDIT_DATA_COLOR_PINK),
                new ColorData(DyeColor.BROWN, ParticleUtils.closestMatchWithFallback(false, "BROWN_DYE", "COCOA_BEANS"), new OrdinaryColor(139, 69, 19), Lang.GUI_EDIT_DATA_COLOR_BROWN),
                new ColorData(DyeColor.BLACK, ParticleUtils.closestMatchWithFallback(false, "BLACK_DYE", "INK_SAC"), new OrdinaryColor(0, 0, 0), Lang.GUI_EDIT_DATA_COLOR_BLACK),
                new ColorData(DyeColor.GRAY, ParticleUtils.closestMatchWithFallback(false, "GRAY_DYE"), new OrdinaryColor(128, 128, 128), Lang.GUI_EDIT_DATA_COLOR_GRAY),
                new ColorData(DyeColor.getByDyeData((byte) 7), ParticleUtils.closestMatchWithFallback(false, "LIGHT_GRAY_DYE"), new OrdinaryColor(192, 192, 192), Lang.GUI_EDIT_DATA_COLOR_LIGHT_GRAY),
                new ColorData(DyeColor.WHITE, ParticleUtils.closestMatchWithFallback(false, "WHITE_DYE", "BONE_MEAL"), new OrdinaryColor(255, 255, 255), Lang.GUI_EDIT_DATA_COLOR_WHITE),
        };

        rainbowColorMapping = new ColorData[]{
                colorMapping[0], // Red
                colorMapping[1], // Orange
                colorMapping[2], // Yellow
                colorMapping[3], // Lime
                colorMapping[7], // Light Blue
                colorMapping[6], // Cyan
                colorMapping[5], // Blue
                colorMapping[8], // Purple
                colorMapping[9]  // Magenta
        };

        noteColorMapping = new ColorData[]{
                rainbowColorMapping[3], // 0  Lime
                rainbowColorMapping[3], // 1  Lime
                rainbowColorMapping[2], // 2  Yellow
                rainbowColorMapping[1], // 3  Orange
                rainbowColorMapping[1], // 4  Orange
                rainbowColorMapping[0], // 5  Red
                rainbowColorMapping[0], // 6  Red
                rainbowColorMapping[0], // 7  Red
                rainbowColorMapping[8], // 8  Magenta
                rainbowColorMapping[8], // 9  Magenta
                rainbowColorMapping[7], // 10 Purple
                rainbowColorMapping[7], // 11 Purple
                rainbowColorMapping[7], // 12 Purple
                rainbowColorMapping[6], // 13 Blue
                rainbowColorMapping[6], // 14 Blue
                rainbowColorMapping[6], // 15 Blue
                rainbowColorMapping[6], // 16 Blue
                rainbowColorMapping[5], // 17 Cyan
                rainbowColorMapping[5], // 18 Cyan
                rainbowColorMapping[5], // 20 Lime
                rainbowColorMapping[5], // 21 Lime
                rainbowColorMapping[5], // 22 Lime
                rainbowColorMapping[5], // 23 Lime
                rainbowColorMapping[5]  // 24 Lime
        };

        // Note: Minecraft 1.8 through 1.13 had screwed up note color values, they were thankfully fixed in 1.14
        noteColorMappingOld = new ColorData[]{
                colorMapping[7],  // 0  Light Blue
                colorMapping[7],  // 1  Light Blue
                colorMapping[13], // 2  Gray
                colorMapping[10], // 3  Pink
                colorMapping[9],  // 4  Magenta
                colorMapping[9],  // 5  Magenta
                colorMapping[0],  // 6  Red
                colorMapping[2],  // 7  Yellow
                colorMapping[2],  // 8  Yellow
                colorMapping[14], // 9  Light Gray
                colorMapping[13], // 10 Gray
                colorMapping[6],  // 11 Cyan
                colorMapping[6],  // 12 Cyan
                colorMapping[6],  // 13 Cyan
                colorMapping[5],  // 14 Blue
                colorMapping[8],  // 15 Purple
                colorMapping[8],  // 16 Purple
                colorMapping[8],  // 17 Purple
                colorMapping[13], // 18 Gray
                colorMapping[4],  // 19 Green
                colorMapping[3],  // 20 Lime
                colorMapping[2],  // 21 Yellow
                colorMapping[4],  // 22 Green
                colorMapping[7],  // 23 Light Blue
                colorMapping[7]   // 24 Light Blue
        };

        BLOCK_MATERIALS = new ArrayList<>();
        ITEM_MATERIALS = new ArrayList<>();

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

    public GuiInventoryEditData(PPlayer pplayer, ParticlePair editingParticle, int pageNumber, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, LangManager.getText(Lang.GUI_SELECT_DATA)));

        this.fillBorder(BorderColor.MAGENTA);

        ParticleEffect pe = editingParticle.getEffect();
        if (pe.hasProperty(ParticleProperty.COLORABLE)) {
            if (pe == ParticleEffect.NOTE) { // Note data
                this.populateNoteData(editingParticle, pageNumber, callbackList, callbackListPosition);
            } else { // Color data
                this.populateColorData(editingParticle, pageNumber, callbackList, callbackListPosition);
            }
        } else if (pe.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            if (pe == ParticleEffect.ITEM) { // Item data
                this.populateItemData(editingParticle, pageNumber, callbackList, callbackListPosition);
            } else { // Block data
                this.populateBlockData(editingParticle, pageNumber, callbackList, callbackListPosition);
            }
        }

        // Back Button
        GuiActionButton backButton = new GuiActionButton(
                INVENTORY_SIZE - 1,
                GuiIcon.BACK.get(),
                LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_BACK_BUTTON),
                new String[]{},
                (button, isShiftClick) -> callbackList.get(callbackListPosition - 1).execute());
        this.actionButtons.add(backButton);

        this.populate();
    }

    /**
     * Populates the Inventory with available color data options
     *
     * @param editingParticle      The ParticlePair that's being edited
     * @param pageNumber           The current page number
     * @param callbackList         The List of GuiInventoryEditFinishedCallbacks
     * @param callbackListPosition The index of the callbackList we're currently at
     */
    private void populateColorData(ParticlePair editingParticle, int pageNumber, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        int index = 10;
        int nextWrap = 17;
        for (ColorData colorData : colorMapping) {
            String formattedDisplayColor = ChatColor.RED.toString() + colorData.getOrdinaryColor().getRed() + " " + ChatColor.GREEN + colorData.getOrdinaryColor().getGreen() + " " + ChatColor.AQUA + colorData.getOrdinaryColor().getBlue();

            // Color Data Buttons
            GuiActionButton setColorButton = new GuiActionButton(
                    index,
                    colorData,
                    colorData.getName(),
                    new String[]{LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, formattedDisplayColor)},
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
        GuiActionButton setRainbowColorButton = new GuiActionButton(
                39,
                rainbowColorMapping,
                LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.RAINBOW),
                new String[]{LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, LangManager.getText(Lang.RAINBOW))},
                (button, isShiftClick) -> {
                    editingParticle.setColor(new OrdinaryColor(999, 999, 999));
                    callbackList.get(callbackListPosition + 1).execute();
                });
        this.actionButtons.add(setRainbowColorButton);

        // Rainbow Color Data Button
        List<ColorData> randomizedColorsList = Arrays.asList(colorMapping.clone());
        Collections.shuffle(randomizedColorsList);
        ColorData[] randomizedColors = new ColorData[randomizedColorsList.size()];
        randomizedColors = randomizedColorsList.toArray(randomizedColors);
        GuiActionButton setRandomColorButton = new GuiActionButton(41,
                randomizedColors,
                LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.RANDOM),
                new String[]{LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, LangManager.getText(Lang.RANDOM))},
                (button, isShiftClick) -> {
                    editingParticle.setColor(new OrdinaryColor(998, 998, 998));
                    callbackList.get(callbackListPosition + 1).execute();
                });
        this.actionButtons.add(setRandomColorButton);
    }

    /**
     * Populates the Inventory with available note data options
     *
     * @param editingParticle      The ParticlePair that's being edited
     * @param callbackList         The List of GuiInventoryEditFinishedCallbacks
     * @param pageNumber           The current page number
     * @param callbackListPosition The index of the callbackList we're currently at
     */
    private void populateNoteData(ParticlePair editingParticle, int pageNumber, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        int numberOfItems = noteColorMapping.length;
        int maxPages = (int) Math.floor(numberOfItems / 28.0);
        int itemsPerPage = 14;
        int slot = 10;
        int nextWrap = 17;
        int maxSlot = 43;

        for (int i = (pageNumber - 1) * itemsPerPage; i < numberOfItems; i++) {
            ColorData colorData = NMSUtil.getVersionNumber() > 13 ? noteColorMapping[i] : noteColorMappingOld[i];
            String formattedDisplayName = LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_SELECT_DATA_NOTE, i) + " (" + colorData.getName() + LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + ")";
            String formattedDescription = LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, LangManager.getText(Lang.GUI_SELECT_DATA_NOTE, i));

            // Note Color Buttons
            int noteIndex = i;
            GuiActionButton setColorButton = new GuiActionButton(
                    slot,
                    colorData,
                    formattedDisplayName,
                    new String[]{formattedDescription},
                    (button, isShiftClick) -> {
                        editingParticle.setNoteColor(new NoteColor(noteIndex));
                        callbackList.get(callbackListPosition + 1).execute();
                    });
            this.actionButtons.add(setColorButton);

            slot++;
            if (slot == nextWrap) { // Loop around border
                nextWrap += 9;
                slot += 2;
            }
            if (slot > maxSlot) break; // Overflowed the available space
        }

        // Rainbow Note Data Button
        GuiActionButton setRainbowColorButton = new GuiActionButton(
                39,
                rainbowColorMapping,
                LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.RAINBOW),
                new String[]{LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, LangManager.getText(Lang.RAINBOW))},
                (button, isShiftClick) -> {
                    editingParticle.setNoteColor(new NoteColor(99));
                    callbackList.get(callbackListPosition + 1).execute();
                });
        this.actionButtons.add(setRainbowColorButton);

        // Rainbow Note Data Button
        List<ColorData> randomizedColorsList = Arrays.asList(colorMapping.clone());
        Collections.shuffle(randomizedColorsList);
        ColorData[] randomizedColors = new ColorData[randomizedColorsList.size()];
        randomizedColors = randomizedColorsList.toArray(randomizedColors);
        GuiActionButton setRandomColorButton = new GuiActionButton(
                41,
                randomizedColors,
                LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.RANDOM),
                new String[]{LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, LangManager.getText(Lang.RANDOM))},
                (button, isShiftClick) -> {
                    editingParticle.setNoteColor(new NoteColor(98));
                    callbackList.get(callbackListPosition + 1).execute();
                });
        this.actionButtons.add(setRandomColorButton);

        // Previous page button
        if (pageNumber != 1) {
            GuiActionButton previousPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 6,
                    GuiIcon.PREVIOUS_PAGE.get(),
                    LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_PREVIOUS_PAGE_BUTTON, pageNumber - 1, maxPages),
                    new String[]{},
                    (button, isShiftClick) -> GuiHandler.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber - 1, callbackList, callbackListPosition)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_NEXT_PAGE_BUTTON, pageNumber + 1, maxPages),
                    new String[]{},
                    (button, isShiftClick) -> GuiHandler.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber + 1, callbackList, callbackListPosition)));
            this.actionButtons.add(nextPageButton);
        }
    }

    /**
     * Populates the Inventory with available item data options
     *
     * @param editingParticle      The ParticlePair that's being edited
     * @param callbackList         The List of GuiInventoryEditFinishedCallbacks
     * @param pageNumber           The current page number
     * @param callbackListPosition The index of the callbackList we're currently at
     */
    private void populateItemData(ParticlePair editingParticle, int pageNumber, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        int numberOfItems = ITEM_MATERIALS.size();
        int maxPages = (int) Math.floor(numberOfItems / 28.0);
        int itemsPerPage = 28;
        int slot = 10;
        int nextWrap = 17;
        int maxSlot = 43;

        for (int i = (pageNumber - 1) * itemsPerPage; i < numberOfItems; i++) {
            // Item Data Button
            Material material = ITEM_MATERIALS.get(i);
            GuiActionButton setRainbowColorButton = new GuiActionButton(
                    slot,
                    material,
                    LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + material.name().toLowerCase(),
                    new String[]{LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, material.name().toLowerCase())},
                    (button, isShiftClick) -> {
                        editingParticle.setItemMaterial(material);
                        callbackList.get(callbackListPosition + 1).execute();
                    });
            this.actionButtons.add(setRainbowColorButton);

            slot++;
            if (slot == nextWrap) { // Loop around border
                nextWrap += 9;
                slot += 2;
            }
            if (slot > maxSlot) break; // Overflowed the available space
        }

        // Previous page button
        if (pageNumber != 1) {
            GuiActionButton previousPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 6,
                    GuiIcon.PREVIOUS_PAGE.get(),
                    LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_PREVIOUS_PAGE_BUTTON, pageNumber - 1, maxPages),
                    new String[]{},
                    (button, isShiftClick) -> GuiHandler.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber - 1, callbackList, callbackListPosition)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_NEXT_PAGE_BUTTON, pageNumber + 1, maxPages),
                    new String[]{},
                    (button, isShiftClick) -> GuiHandler.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber + 1, callbackList, callbackListPosition)));
            this.actionButtons.add(nextPageButton);
        }
    }

    /**
     * Populates the Inventory with available block data options
     *
     * @param editingParticle      The ParticlePair that's being edited
     * @param callbackList         The List of GuiInventoryEditFinishedCallbacks
     * @param pageNumber           The current page number
     * @param callbackListPosition The index of the callbackList we're currently at
     */
    private void populateBlockData(ParticlePair editingParticle, int pageNumber, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        int numberOfItems = BLOCK_MATERIALS.size();
        int maxPages = (int) Math.floor(numberOfItems / 28.0);
        int itemsPerPage = 28;
        int slot = 10;
        int nextWrap = 17;
        int maxSlot = 43;

        for (int i = (pageNumber - 1) * itemsPerPage; i < numberOfItems; i++) {
            // Item Data Button
            Material material = BLOCK_MATERIALS.get(i);
            GuiActionButton setRainbowColorButton = new GuiActionButton(
                    slot,
                    material,
                    LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + material.name().toLowerCase(),
                    new String[]{LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_DATA_DESCRIPTION, material.name().toLowerCase())},
                    (button, isShiftClick) -> {
                        editingParticle.setBlockMaterial(material);
                        callbackList.get(callbackListPosition + 1).execute();
                    });
            this.actionButtons.add(setRainbowColorButton);

            slot++;
            if (slot == nextWrap) { // Loop around border
                nextWrap += 9;
                slot += 2;
            }
            if (slot > maxSlot) break; // Overflowed the available space
        }

        // Previous page button
        if (pageNumber != 1) {
            GuiActionButton previousPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 6,
                    GuiIcon.PREVIOUS_PAGE.get(),
                    LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_PREVIOUS_PAGE_BUTTON, pageNumber - 1, maxPages),
                    new String[]{},
                    (button, isShiftClick) -> GuiHandler.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber - 1, callbackList, callbackListPosition)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_NEXT_PAGE_BUTTON, pageNumber + 1, maxPages),
                    new String[]{},
                    (button, isShiftClick) -> GuiHandler.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber + 1, callbackList, callbackListPosition)));
            this.actionButtons.add(nextPageButton);
        }
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
