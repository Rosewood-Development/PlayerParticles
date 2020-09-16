package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.ConfigurationManager.GuiIcon;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

@SuppressWarnings("deprecation")
public class GuiInventoryDefault extends GuiInventory {

    public GuiInventoryDefault(PPlayer pplayer) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, PlayerParticles.getInstance().getManager(LocaleManager.class).getLocaleMessage("gui-playerparticles")));

        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        this.fillBorder(BorderColor.WHITE);

        // PPlayer information icon
        ItemStack headIcon;
        Material playerHead = ParticleUtils.closestMatch("PLAYER_HEAD");
        if (playerHead != null) {
            headIcon = new ItemStack(playerHead, 1);
        } else {
            headIcon = new ItemStack(ParticleUtils.closestMatch("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());
        }

        SkullMeta currentIconMeta = (SkullMeta) headIcon.getItemMeta();
        if (currentIconMeta != null) {
            currentIconMeta.setDisplayName(localeManager.getLocaleMessage("gui-color-icon-name") + pplayer.getPlayer().getName());
            String[] currentIconLore = new String[]{
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-active-particles", StringPlaceholders.single("amount", pplayer.getActiveParticles().size())),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-saved-groups", StringPlaceholders.single("amount", pplayer.getParticleGroups().size() - 1)),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-fixed-effects", StringPlaceholders.single("amount", pplayer.getFixedEffectIds().size())),
                    " ",
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-commands-info")
            };
            currentIconMeta.setLore(GuiActionButton.parseLore(this.pplayer, currentIconLore));
            currentIconMeta.setOwner(pplayer.getPlayer().getName());
            headIcon.setItemMeta(currentIconMeta);
        }

        this.inventory.setItem(13, headIcon);

        // Define what slots to put the icons at based on what other slots are visible
        boolean manageGroupsVisible = PlayerParticles.getInstance().getManager(PermissionManager.class).canPlayerSaveGroups(pplayer);
        boolean loadPresetGroupVisible = !PlayerParticles.getInstance().getManager(ParticleGroupPresetManager.class).getPresetGroupsForPlayer(pplayer).isEmpty();
        int manageParticlesSlot = -1, manageGroupsSlot = -1, loadPresetGroupSlot = -1;

        if (!manageGroupsVisible && !loadPresetGroupVisible) {
            manageParticlesSlot = 22;
        } else if (!manageGroupsVisible) {
            manageParticlesSlot = 21;
            loadPresetGroupSlot = 23;
        } else if (!loadPresetGroupVisible) {
            manageParticlesSlot = 21;
            manageGroupsSlot = 23;
        } else {
            manageParticlesSlot = 20;
            manageGroupsSlot = 22;
            loadPresetGroupSlot = 24;
        }

        // Manage Your Particles button
        GuiActionButton manageYourParticlesButton = new GuiActionButton(
                manageParticlesSlot,
                GuiIcon.PARTICLES.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-manage-your-particles"),
                new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-manage-your-particles-description")},
                (button, isShiftClick) -> guiManager.transition(new GuiInventoryManageParticles(pplayer)));
        this.actionButtons.add(manageYourParticlesButton);

        // Manage Your Groups button
        if (manageGroupsVisible) {
            GuiActionButton manageYourGroupsButton = new GuiActionButton(
                    manageGroupsSlot,
                    GuiIcon.GROUPS.get(),
                    localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-manage-your-groups"),
                    new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-manage-your-groups-description")},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryManageGroups(pplayer, 1)));
            this.actionButtons.add(manageYourGroupsButton);
        }

        // Load Preset Groups
        if (loadPresetGroupVisible) {
            GuiActionButton loadPresetGroups = new GuiActionButton(
                    loadPresetGroupSlot,
                    GuiIcon.PRESET_GROUPS.get(),
                    localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-load-a-preset-group"),
                    new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-load-a-preset-group-description")},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryLoadPresetGroups(pplayer, false, 1)));
            this.actionButtons.add(loadPresetGroups);
        }

        final ParticlePair editingParticle = pplayer.getPrimaryParticle();
        boolean canEditPrimaryStyleAndData = pplayer.getActiveParticle(1) != null;
        boolean doesEffectUseData = editingParticle.getEffect().hasProperty(ParticleProperty.COLORABLE) || editingParticle.getEffect().hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA);

        // Edit Primary Effect
        GuiActionButton editPrimaryEffect = new GuiActionButton(
                38,
                GuiIcon.EDIT_EFFECT.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-edit-primary-effect"),
                new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-edit-primary-effect-description")},
                (button, isShiftClick) -> {
                    List<Runnable> callbacks = new ArrayList<>();
                    callbacks.add(() -> guiManager.transition(new GuiInventoryDefault(pplayer)));
                    callbacks.add(() -> guiManager.transition(new GuiInventoryEditEffect(pplayer, editingParticle, 1, callbacks, 1)));
                    callbacks.add(() -> {
                        ParticleGroup group = pplayer.getActiveParticleGroup();
                        if (canEditPrimaryStyleAndData) {
                            for (ParticlePair particle : group.getParticles().values()) {
                                if (particle.getId() == editingParticle.getId()) {
                                    particle.setEffect(editingParticle.getEffect());
                                    break;
                                }
                            }
                        } else {
                            group.getParticles().put(editingParticle.getId(), editingParticle);
                        }
                        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);

                        guiManager.transition(new GuiInventoryDefault(pplayer));
                    });

                    callbacks.get(1).run();
                });
        this.actionButtons.add(editPrimaryEffect);

        // Edit Primary Style
        String[] editPrimaryStyleLore;
        if (canEditPrimaryStyleAndData) {
            editPrimaryStyleLore = new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-edit-primary-style-description")};
        } else {
            editPrimaryStyleLore = new String[]{
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-edit-primary-style-description"),
                    localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-edit-primary-style-missing-effect")
            };
        }
        GuiActionButton editPrimaryStyle = new GuiActionButton(
                40,
                GuiIcon.EDIT_STYLE.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-edit-primary-style"),
                editPrimaryStyleLore,
                (button, isShiftClick) -> {
                    if (!canEditPrimaryStyleAndData) return;

                    List<Runnable> callbacks = new ArrayList<>();
                    callbacks.add(() -> guiManager.transition(new GuiInventoryDefault(pplayer)));
                    callbacks.add(() -> guiManager.transition(new GuiInventoryEditStyle(pplayer, editingParticle, 1, callbacks, 1)));
                    callbacks.add(() -> {
                        ParticleGroup group = pplayer.getActiveParticleGroup();
                        for (ParticlePair particle : group.getParticles().values()) {
                            if (particle.getId() == editingParticle.getId()) {
                                particle.setStyle(editingParticle.getStyle());
                                break;
                            }
                        }
                        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);

                        guiManager.transition(new GuiInventoryDefault(pplayer));
                    });

                    callbacks.get(1).run();
                });
        this.actionButtons.add(editPrimaryStyle);

        // Edit Primary Data
        String[] editPrimaryDataLore;
        if (canEditPrimaryStyleAndData && doesEffectUseData) {
            editPrimaryDataLore = new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-edit-primary-data-description")};
        } else if (canEditPrimaryStyleAndData) {
            editPrimaryDataLore = new String[]{
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-edit-primary-data-description"),
                    localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-edit-primary-data-unavailable")
            };
        } else {
            editPrimaryDataLore = new String[]{
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-edit-primary-data-description"),
                    localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-edit-primary-data-missing-effect")
            };
        }
        GuiActionButton editPrimaryData = new GuiActionButton(
                42,
                GuiIcon.EDIT_DATA.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-edit-primary-data"),
                editPrimaryDataLore,
                (button, isShiftClick) -> {
                    if (!canEditPrimaryStyleAndData || !doesEffectUseData) return;

                    List<Runnable> callbacks = new ArrayList<>();
                    callbacks.add(() -> guiManager.transition(new GuiInventoryDefault(pplayer)));
                    callbacks.add(() -> guiManager.transition(new GuiInventoryEditData(pplayer, editingParticle, 1, callbacks, 1)));
                    callbacks.add(() -> {
                        ParticleGroup group = pplayer.getActiveParticleGroup();
                        for (ParticlePair particle : group.getParticles().values()) {
                            if (particle.getId() == editingParticle.getId()) {
                                particle.setColor(editingParticle.getColor());
                                particle.setNoteColor(editingParticle.getNoteColor());
                                particle.setItemMaterial(editingParticle.getItemMaterial());
                                particle.setBlockMaterial(editingParticle.getBlockMaterial());
                                break;
                            }
                        }
                        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);

                        guiManager.transition(new GuiInventoryDefault(pplayer));
                    });

                    callbacks.get(1).run();
                });
        this.actionButtons.add(editPrimaryData);

        this.populate();
    }

}
