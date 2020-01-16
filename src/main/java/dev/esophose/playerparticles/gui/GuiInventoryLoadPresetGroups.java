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
import dev.esophose.playerparticles.particles.ParticleGroupPreset;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GuiInventoryLoadPresetGroups extends GuiInventory {

    public GuiInventoryLoadPresetGroups(PPlayer pplayer, boolean isEndPoint) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, PlayerParticles.getInstance().getManager(LocaleManager.class).getLocaleMessage("gui-load-a-preset-group")));

        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        this.fillBorder(BorderColor.GREEN);

        Player player = pplayer.getPlayer();

        int index = 10;
        int nextWrap = 17;
        int maxIndex = 43;
        List<ParticleGroupPreset> groups = PlayerParticles.getInstance().getManager(ParticleGroupPresetManager.class).getPresetGroupsForPlayer(pplayer);
        for (ParticleGroupPreset group : groups) {
            if (!group.canPlayerUse(pplayer))
                continue;

            List<ParticlePair> particles = new ArrayList<>(group.getGroup().getParticles().values());
            particles.sort(Comparator.comparingInt(ParticlePair::getId));

            String[] lore = new String[particles.size() + 1];
            lore[0] = localeManager.getLocaleMessage("gui-color-subtext") + localeManager.getLocaleMessage("gui-click-to-load", StringPlaceholders.single("amount", particles.size()));
            int i = 1;
            for (ParticlePair particle : particles) {
                StringPlaceholders stringPlaceholders = StringPlaceholders.builder("id", particle.getId())
                        .addPlaceholder("effect", ParticleUtils.formatName(particle.getEffect().getName()))
                        .addPlaceholder("style", ParticleUtils.formatName(particle.getStyle().getName()))
                        .addPlaceholder("data", particle.getDataString())
                        .build();
                lore[i] = localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-particle-info", stringPlaceholders);
                i++;
            }

            // Load Group Buttons
            GuiActionButton groupButton = new GuiActionButton(index, group.getGuiIcon(), localeManager.getLocaleMessage("gui-color-icon-name") + group.getDisplayName(), lore, (button, isShiftClick) -> {
                ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                activeGroup.getParticles().clear();
                for (ParticlePair particle : particles) {
                    ParticlePair clonedParticle = particle.clone();
                    clonedParticle.setOwner(pplayer);
                    activeGroup.getParticles().put(clonedParticle.getId(), clonedParticle);
                }
                PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), activeGroup);

                if (Setting.GUI_CLOSE_AFTER_GROUP_SELECTED.getBoolean()) {
                    pplayer.getPlayer().closeInventory();
                }
            });
            this.actionButtons.add(groupButton);

            index++;
            if (index == nextWrap) { // Loop around border
                nextWrap += 9;
                index += 2;
            }
            if (index > maxIndex) break; // Overflowed the available space
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
                    49,
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
