package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.GuiIcon;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.util.CavesAndCliffsUtil;
import dev.esophose.playerparticles.util.NMSUtil;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

@SuppressWarnings("deprecation")
public class GuiInventoryEditData extends GuiInventory {

    private static ColorData[] colorMapping;
    private static ColorData[] rainbowColorMapping;
    private static ColorData[] noteColorMapping, noteColorMappingOld;

    static {
        colorMapping = new ColorData[]{
                new ColorData(DyeColor.RED, ParticleUtils.closestMatchWithFallback(false, "RED_DYE", "ROSE_RED"), new OrdinaryColor(255, 0, 0), "gui-edit-data-color-red"),
                new ColorData(DyeColor.ORANGE, ParticleUtils.closestMatchWithFallback(false, "ORANGE_DYE"), new OrdinaryColor(255, 140, 0), "gui-edit-data-color-orange"),
                new ColorData(DyeColor.YELLOW, ParticleUtils.closestMatchWithFallback(false, "YELLOW_DYE", "DANDELION_YELLOW"), new OrdinaryColor(255, 255, 0), "gui-edit-data-color-yellow"),
                new ColorData(DyeColor.LIME, ParticleUtils.closestMatchWithFallback(false, "LIME_DYE"), new OrdinaryColor(50, 205, 50), "gui-edit-data-color-lime-green"),
                new ColorData(DyeColor.GREEN, ParticleUtils.closestMatchWithFallback(false, "GREEN_DYE", "CACTUS_GREEN"), new OrdinaryColor(0, 128, 0), "gui-edit-data-color-green"),
                new ColorData(DyeColor.BLUE, ParticleUtils.closestMatchWithFallback(false, "BLUE_DYE", "LAPIS_LAZULI"), new OrdinaryColor(0, 0, 255), "gui-edit-data-color-blue"),
                new ColorData(DyeColor.CYAN, ParticleUtils.closestMatchWithFallback(false, "CYAN_DYE"), new OrdinaryColor(0, 139, 139), "gui-edit-data-color-cyan"),
                new ColorData(DyeColor.LIGHT_BLUE, ParticleUtils.closestMatchWithFallback(false, "LIGHT_BLUE_DYE"), new OrdinaryColor(173, 216, 230), "gui-edit-data-color-light-blue"),
                new ColorData(DyeColor.PURPLE, ParticleUtils.closestMatchWithFallback(false, "PURPLE_DYE"), new OrdinaryColor(138, 43, 226), "gui-edit-data-color-purple"),
                new ColorData(DyeColor.MAGENTA, ParticleUtils.closestMatchWithFallback(false, "MAGENTA_DYE"), new OrdinaryColor(202, 31, 123), "gui-edit-data-color-magenta"),
                new ColorData(DyeColor.PINK, ParticleUtils.closestMatchWithFallback(false, "PINK_DYE"), new OrdinaryColor(255, 182, 193), "gui-edit-data-color-pink"),
                new ColorData(DyeColor.BROWN, ParticleUtils.closestMatchWithFallback(false, "BROWN_DYE", "COCOA_BEANS"), new OrdinaryColor(139, 69, 19), "gui-edit-data-color-brown"),
                new ColorData(DyeColor.BLACK, ParticleUtils.closestMatchWithFallback(false, "BLACK_DYE", "INK_SAC"), new OrdinaryColor(0, 0, 0), "gui-edit-data-color-black"),
                new ColorData(DyeColor.GRAY, ParticleUtils.closestMatchWithFallback(false, "GRAY_DYE"), new OrdinaryColor(128, 128, 128), "gui-edit-data-color-gray"),
                new ColorData(DyeColor.getByDyeData((byte) 7), ParticleUtils.closestMatchWithFallback(false, "LIGHT_GRAY_DYE"), new OrdinaryColor(192, 192, 192), "gui-edit-data-color-light-gray"),
                new ColorData(DyeColor.WHITE, ParticleUtils.closestMatchWithFallback(false, "WHITE_DYE", "BONE_MEAL"), new OrdinaryColor(255, 255, 255), "gui-edit-data-color-white"),
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
                rainbowColorMapping[5], // 19 Cyan
                rainbowColorMapping[3], // 20 Lime
                rainbowColorMapping[3], // 21 Lime
                rainbowColorMapping[3], // 22 Lime
                rainbowColorMapping[3], // 23 Lime
                rainbowColorMapping[3]  // 24 Lime
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
    }

    public GuiInventoryEditData(PPlayer pplayer, ParticlePair editingParticle, int pageNumber, List<Runnable> callbackList, int callbackListPosition, OrdinaryColor startColor) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, PlayerParticles.getInstance().getManager(LocaleManager.class).getLocaleMessage("gui-select-data")));

        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        this.fillBorder(BorderColor.MAGENTA);

        ParticleEffect pe = editingParticle.getEffect();
        if (pe.hasProperty(ParticleProperty.COLORABLE)) {
            if (pe == ParticleEffect.NOTE) { // Note data
                this.populateNoteData(editingParticle, pageNumber, callbackList, callbackListPosition);
            } else { // Color data
                this.populateColorData(editingParticle, callbackList, callbackListPosition);
            }
        } else if (pe.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            if (pe == ParticleEffect.ITEM) { // Item data
                this.populateItemData(editingParticle, pageNumber, callbackList, callbackListPosition);
            } else { // Block data
                this.populateBlockData(editingParticle, pageNumber, callbackList, callbackListPosition);
            }
        } else if (pe.hasProperty(ParticleProperty.COLORABLE_TRANSITION)) {
            this.populateColorTransitionData(editingParticle, callbackList, callbackListPosition, startColor);
        } else if (pe.hasProperty(ParticleProperty.VIBRATION)) {
            this.populateVibrationData(editingParticle, callbackList, callbackListPosition);
        }

        // Back Button
        GuiActionButton backButton = new GuiActionButton(
                INVENTORY_SIZE - 1,
                GuiIcon.BACK.get(),
                localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-back-button"),
                new String[]{},
                (button, isShiftClick) -> callbackList.get(callbackListPosition - 1).run());
        this.actionButtons.add(backButton);

        this.populate();
    }

    /**
     * Populates the Inventory with available color data options
     *
     * @param editingParticle      The ParticlePair that's being edited
     * @param callbackList         The List of GuiInventoryEditFinishedCallbacks
     * @param callbackListPosition The index of the callbackList we're currently at
     */
    private void populateColorData(ParticlePair editingParticle, List<Runnable> callbackList, int callbackListPosition) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        int index = 10;
        int nextWrap = 17;
        for (ColorData colorData : colorMapping) {
            String formattedDisplayColor = ChatColor.RED.toString() + colorData.getOrdinaryColor().getRed() + " " + ChatColor.GREEN + colorData.getOrdinaryColor().getGreen() + " " + ChatColor.AQUA + colorData.getOrdinaryColor().getBlue();

            // Color Data Buttons
            GuiActionButton setColorButton = new GuiActionButton(
                    index,
                    colorData,
                    colorData.getName(),
                    new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", formattedDisplayColor))},
                    (button, isShiftClick) -> {
                        editingParticle.setColor(colorData.getOrdinaryColor());
                        callbackList.get(callbackListPosition + 1).run();
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
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("rainbow"),
                new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", localeManager.getLocaleMessage("rainbow")))},
                (button, isShiftClick) -> {
                    editingParticle.setColor(OrdinaryColor.RAINBOW);
                    callbackList.get(callbackListPosition + 1).run();
                });
        this.actionButtons.add(setRainbowColorButton);

        // Random Color Data Button
        List<ColorData> randomizedColorsList = Arrays.asList(colorMapping.clone());
        Collections.shuffle(randomizedColorsList);
        ColorData[] randomizedColors = new ColorData[randomizedColorsList.size()];
        randomizedColors = randomizedColorsList.toArray(randomizedColors);
        GuiActionButton setRandomColorButton = new GuiActionButton(41,
                randomizedColors,
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("random"),
                new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", localeManager.getLocaleMessage("random")))},
                (button, isShiftClick) -> {
                    editingParticle.setColor(OrdinaryColor.RANDOM);
                    callbackList.get(callbackListPosition + 1).run();
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
    private void populateNoteData(ParticlePair editingParticle, int pageNumber, List<Runnable> callbackList, int callbackListPosition) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        int numberOfItems = noteColorMapping.length;
        int itemsPerPage = 14;
        int maxPages = (int) Math.ceil((double) numberOfItems / itemsPerPage);
        int slot = 10;
        int nextWrap = 17;
        int maxSlot = 25;

        for (int i = (pageNumber - 1) * itemsPerPage; i < numberOfItems; i++) {
            ColorData colorData = NMSUtil.getVersionNumber() > 13 ? noteColorMapping[i] : noteColorMappingOld[i];
            String formattedDisplayName = localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-select-data-note", StringPlaceholders.single("note", i)) + " (" + colorData.getName() + localeManager.getLocaleMessage("gui-color-icon-name") + ")";
            String formattedDescription = localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", localeManager.getLocaleMessage("gui-select-data-note", StringPlaceholders.single("note", i))));

            // Note Color Buttons
            int noteIndex = i;
            GuiActionButton setColorButton = new GuiActionButton(
                    slot,
                    colorData,
                    formattedDisplayName,
                    new String[]{formattedDescription},
                    (button, isShiftClick) -> {
                        editingParticle.setNoteColor(new NoteColor(noteIndex));
                        callbackList.get(callbackListPosition + 1).run();
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
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("rainbow"),
                new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", localeManager.getLocaleMessage("rainbow")))},
                (button, isShiftClick) -> {
                    editingParticle.setNoteColor(NoteColor.RAINBOW);
                    callbackList.get(callbackListPosition + 1).run();
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
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("random"),
                new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", localeManager.getLocaleMessage("random")))},
                (button, isShiftClick) -> {
                    editingParticle.setNoteColor(NoteColor.RANDOM);
                    callbackList.get(callbackListPosition + 1).run();
                });
        this.actionButtons.add(setRandomColorButton);

        // Previous page button
        if (pageNumber != 1) {
            GuiActionButton previousPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 6,
                    GuiIcon.PREVIOUS_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-previous-page-button", StringPlaceholders.builder("start", pageNumber - 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber - 1, callbackList, callbackListPosition, null)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-next-page-button", StringPlaceholders.builder("start", pageNumber + 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber + 1, callbackList, callbackListPosition, null)));
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
    private void populateItemData(ParticlePair editingParticle, int pageNumber, List<Runnable> callbackList, int callbackListPosition) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        int numberOfItems = ParticleUtils.ITEM_MATERIALS.size();
        int itemsPerPage = 28;
        int maxPages = (int) Math.ceil((double) numberOfItems / itemsPerPage);
        int slot = 10;
        int nextWrap = 17;
        int maxSlot = 43;

        for (int i = (pageNumber - 1) * itemsPerPage; i < numberOfItems; i++) {
            // Item Data Button
            Material material = ParticleUtils.ITEM_MATERIALS.get(i);
            GuiActionButton setRainbowColorButton = new GuiActionButton(
                    slot,
                    material,
                    localeManager.getLocaleMessage("gui-color-icon-name") + material.name().toLowerCase(),
                    new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", material.name().toLowerCase()))},
                    (button, isShiftClick) -> {
                        editingParticle.setItemMaterial(material);
                        callbackList.get(callbackListPosition + 1).run();
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
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-previous-page-button", StringPlaceholders.builder("start", pageNumber - 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber - 1, callbackList, callbackListPosition, null)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-next-page-button", StringPlaceholders.builder("start", pageNumber + 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber + 1, callbackList, callbackListPosition, null)));
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
    private void populateBlockData(ParticlePair editingParticle, int pageNumber, List<Runnable> callbackList, int callbackListPosition) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        int numberOfItems = ParticleUtils.BLOCK_MATERIALS.size();
        int itemsPerPage = 28;
        int maxPages = (int) Math.ceil((double) numberOfItems / itemsPerPage);
        int slot = 10;
        int nextWrap = 17;
        int maxSlot = 43;

        for (int i = (pageNumber - 1) * itemsPerPage; i < numberOfItems; i++) {
            // Item Data Button
            Material material = ParticleUtils.BLOCK_MATERIALS.get(i);
            GuiActionButton setRainbowColorButton = new GuiActionButton(
                    slot,
                    material,
                    localeManager.getLocaleMessage("gui-color-icon-name") + material.name().toLowerCase(),
                    new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", material.name().toLowerCase()))},
                    (button, isShiftClick) -> {
                        editingParticle.setBlockMaterial(material);
                        callbackList.get(callbackListPosition + 1).run();
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
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-previous-page-button", StringPlaceholders.builder("start", pageNumber - 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber - 1, callbackList, callbackListPosition, null)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-next-page-button", StringPlaceholders.builder("start", pageNumber + 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryEditData(this.pplayer, editingParticle, pageNumber + 1, callbackList, callbackListPosition, null)));
            this.actionButtons.add(nextPageButton);
        }
    }

    /**
     * Populates the Inventory with available color transition data options.
     * This is a copy of the Color data options, but it will be displayed twice.
     *
     * @param editingParticle      The ParticlePair that's being edited
     * @param callbackList         The List of GuiInventoryEditFinishedCallbacks
     * @param callbackListPosition The index of the callbackList we're currently at
     * @param startColor           The start color of the color transition, will be null if no start color has been selected yet
     */
    private void populateColorTransitionData(ParticlePair editingParticle, List<Runnable> callbackList, int callbackListPosition, OrdinaryColor startColor) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        int index = 10;
        int nextWrap = 17;
        for (ColorData colorData : colorMapping) {
            String formattedDisplayColor = ChatColor.RED.toString() + colorData.getOrdinaryColor().getRed() + " " + ChatColor.GREEN + colorData.getOrdinaryColor().getGreen() + " " + ChatColor.AQUA + colorData.getOrdinaryColor().getBlue();

            // Color Data Buttons
            GuiActionButton setColorButton = new GuiActionButton(
                    index,
                    colorData,
                    colorData.getName(),
                    new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", formattedDisplayColor))},
                    (button, isShiftClick) -> {
                        if (startColor == null) {
                            PlayerParticles.getInstance().getManager(GuiManager.class).transition(new GuiInventoryEditData(this.pplayer, editingParticle, 1, callbackList, callbackListPosition, colorData.getOrdinaryColor()));
                        } else {
                            editingParticle.setColorTransition(new ColorTransition(startColor, colorData.getOrdinaryColor()));
                            callbackList.get(callbackListPosition + 1).run();
                        }
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
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("rainbow"),
                new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", localeManager.getLocaleMessage("rainbow")))},
                (button, isShiftClick) -> {
                    if (startColor == null) {
                        PlayerParticles.getInstance().getManager(GuiManager.class).transition(new GuiInventoryEditData(this.pplayer, editingParticle, 1, callbackList, callbackListPosition, OrdinaryColor.RAINBOW));
                    } else {
                        editingParticle.setColorTransition(new ColorTransition(startColor, OrdinaryColor.RAINBOW));
                        callbackList.get(callbackListPosition + 1).run();
                    }
                });
        this.actionButtons.add(setRainbowColorButton);

        // Random Color Data Button
        List<ColorData> randomizedColorsList = Arrays.asList(colorMapping.clone());
        Collections.shuffle(randomizedColorsList);
        ColorData[] randomizedColors = new ColorData[randomizedColorsList.size()];
        randomizedColors = randomizedColorsList.toArray(randomizedColors);
        GuiActionButton setRandomColorButton = new GuiActionButton(41,
                randomizedColors,
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("random"),
                new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", localeManager.getLocaleMessage("random")))},
                (button, isShiftClick) -> {
                    if (startColor == null) {
                        PlayerParticles.getInstance().getManager(GuiManager.class).transition(new GuiInventoryEditData(this.pplayer, editingParticle, 1, callbackList, callbackListPosition, OrdinaryColor.RANDOM));
                    } else {
                        editingParticle.setColorTransition(new ColorTransition(startColor, OrdinaryColor.RANDOM));
                        callbackList.get(callbackListPosition + 1).run();
                    }
                });
        this.actionButtons.add(setRandomColorButton);

        // Which particle are we selecting?
        // Display a light with either the number 1 or 2
        ItemStack light = new ItemStack(Material.LIGHT);
        ItemMeta itemMeta = light.getItemMeta();
        if (itemMeta != null) {
            CavesAndCliffsUtil.setLightLevel(itemMeta, startColor == null ? 1 : 2);
            itemMeta.setDisplayName(localeManager.getLocaleMessage("gui-select-data-color-transition-" + (startColor == null ? "start" : "end")));
            light.setItemMeta(itemMeta);
        }

        this.inventory.setItem(4, light);
    }

    /**
     * Populates the Inventory with available vibration data options
     *
     * @param editingParticle      The ParticlePair that's being edited
     * @param callbackList         The List of GuiInventoryEditFinishedCallbacks
     * @param callbackListPosition The index of the callbackList we're currently at
     */
    private void populateVibrationData(ParticlePair editingParticle, List<Runnable> callbackList, int callbackListPosition) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        int slot = 21;
        for (int i = 1; i <= 6; i++) {
            int vibration = i * 10;
            String formattedDisplayName = localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-select-data-vibration", StringPlaceholders.single("ticks", vibration));
            String formattedDescription = localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-data-description", StringPlaceholders.single("data", localeManager.getLocaleMessage("gui-select-data-vibration", StringPlaceholders.single("ticks", vibration))));

            // Vibration Buttons
            GuiActionButton setColorButton = new GuiActionButton(
                    slot,
                    Material.SCULK_SENSOR,
                    formattedDisplayName,
                    new String[]{formattedDescription},
                    (button, isShiftClick) -> {
                        editingParticle.setVibration(new Vibration(vibration));
                        callbackList.get(callbackListPosition + 1).run();
                    });
            this.actionButtons.add(setColorButton);

            if (slot++ == 23) // Loop around
                slot = 30;
        }
    }

    /**
     * A data class used for storing information about the color data
     */
    protected static class ColorData {
        private final DyeColor dyeColor;
        private final Material material;
        private final OrdinaryColor ordinaryColor;
        private final String nameKey;

        public ColorData(DyeColor dyeColor, Material material, OrdinaryColor ordinaryColor, String nameKey) {
            this.dyeColor = dyeColor;
            this.material = material;
            this.ordinaryColor = ordinaryColor;
            this.nameKey = nameKey;
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
            return PlayerParticles.getInstance().getManager(LocaleManager.class).getLocaleMessage(this.nameKey);
        }
    }

}
