package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.ConfigurationManager.GuiIcon;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.preset.ParticleGroupPreset;
import dev.esophose.playerparticles.particles.preset.ParticleGroupPresetPage;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import org.bukkit.Bukkit;

public class GuiInventoryLoadPresetGroups extends GuiInventory {

    public GuiInventoryLoadPresetGroups(PPlayer pplayer, boolean isEndPoint, int pageNumber) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, PlayerParticles.getInstance().getManager(ParticleGroupPresetManager.class).getPresetGroupPages(pplayer).get(pageNumber).getTitle()));

        PlayerParticles playerParticles = PlayerParticles.getInstance();
        ParticleGroupPresetManager presetManager = playerParticles.getManager(ParticleGroupPresetManager.class);
        LocaleManager localeManager = playerParticles.getManager(LocaleManager.class);
        GuiManager guiManager = playerParticles.getManager(GuiManager.class);

        this.fillBorder(BorderColor.GREEN);

        ParticleGroupPresetPage pageInfo = presetManager.getPresetGroupPages(pplayer).get(pageNumber);
        Map<Integer, BorderColor> extraBorder = pageInfo.getExtraBorder();

        int maxPages = presetManager.getMaxPageNumber(pplayer);

        // Fill borders
        extraBorder.forEach((item, color) -> this.inventory.setItem(item, color.getIcon()));

        // Fill presets
        for (ParticleGroupPreset group : pageInfo.getPresets()) {
            if (!group.canPlayerUse(pplayer))
                continue;

            int slot = group.getGuiSlot();
            List<ParticlePair> particles = new ArrayList<>(group.getGroup().getParticles().values());
            particles.sort(Comparator.comparingInt(ParticlePair::getId));

            List<String> lore = new ArrayList<>(group.getLore());
            if (!Setting.GUI_PRESETS_HIDE_PARTICLES_DESCRIPTIONS.getBoolean()) {
                lore.add(localeManager.getLocaleMessage("gui-color-subtext") + localeManager.getLocaleMessage("gui-click-to-load", StringPlaceholders.single("amount", particles.size())));
                for (ParticlePair particle : particles) {
                    StringPlaceholders stringPlaceholders = StringPlaceholders.builder("id", particle.getId())
                            .addPlaceholder("effect", ParticleUtils.formatName(particle.getEffect().getName()))
                            .addPlaceholder("style", ParticleUtils.formatName(particle.getStyle().getName()))
                            .addPlaceholder("data", particle.getDataString())
                            .build();
                    lore.add(localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-particle-info", stringPlaceholders));
                }
            }

            // Load Group Buttons
            GuiActionButton groupButton = new GuiActionButton(slot, group.getGuiIcon(), localeManager.getLocaleMessage("gui-color-icon-name") + group.getDisplayName(), lore.toArray(new String[0]), (button, isShiftClick) -> {
                ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                activeGroup.getParticles().clear();
                for (ParticlePair particle : particles) {
                    ParticlePair clonedParticle = particle.clone();
                    clonedParticle.setOwner(pplayer);
                    activeGroup.getParticles().put(clonedParticle.getId(), clonedParticle);
                }
                PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), activeGroup);

                if (Setting.GUI_CLOSE_AFTER_GROUP_SELECTED.getBoolean())
                    this.close();
            });
            this.actionButtons.add(groupButton);
        }

        // Previous page button
        if (pageNumber != 1) {
            GuiActionButton previousPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 6,
                    GuiIcon.PREVIOUS_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-previous-page-button", StringPlaceholders.builder("start", pageNumber - 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryLoadPresetGroups(pplayer, isEndPoint, pageNumber - 1)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-next-page-button", StringPlaceholders.builder("start", pageNumber + 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryLoadPresetGroups(pplayer, isEndPoint, pageNumber + 1)));
            this.actionButtons.add(nextPageButton);
        }

        if (!isEndPoint) {
            // Back Button
            GuiActionButton backButton = new GuiActionButton(
                    INVENTORY_SIZE - 1, GuiIcon.BACK.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-back-button"),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryDefault(pplayer)));
            this.actionButtons.add(backButton);
        } else {
            // Reset Particles Button
            GuiActionButton resetParticles = new GuiActionButton(
                    INVENTORY_SIZE - 5,
                    GuiIcon.RESET.get(),
                    localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-reset-particles"),
                    new String[]{localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-reset-particles-description")},
                    (button, isShiftClick) -> {
                        // Reset particles
                        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), ParticleGroup.getDefaultGroup());
                    });
            this.actionButtons.add(resetParticles);
        }

        this.populate();
    }

}
