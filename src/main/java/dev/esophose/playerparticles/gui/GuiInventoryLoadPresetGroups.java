package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.ParticleGroupPresetManager;
import dev.esophose.playerparticles.manager.SettingManager.GuiIcon;
import dev.esophose.playerparticles.manager.SettingManager.Setting;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticleGroupPreset;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.ParticleUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Comparator;
import java.util.List;

public class GuiInventoryLoadPresetGroups extends GuiInventory {

    public GuiInventoryLoadPresetGroups(PPlayer pplayer, boolean isEndPoint) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, LangManager.getText(Lang.GUI_LOAD_A_PRESET_GROUP)));

        DataManager dataManager = PlayerParticles.getInstance().getManager(DataManager.class);

        this.fillBorder(BorderColor.GREEN);

        Player player = pplayer.getPlayer();

        int index = 10;
        int nextWrap = 17;
        int maxIndex = 43;
        List<ParticleGroupPreset> groups = PlayerParticles.getInstance().getManager(ParticleGroupPresetManager.class).getPresetGroupsForPlayer(pplayer.getPlayer());
        for (ParticleGroupPreset group : groups) {
            if (!group.getGroup().canPlayerUse(player))
                continue;

            List<ParticlePair> particles = group.getGroup().getParticles();
            particles.sort(Comparator.comparingInt(ParticlePair::getId));

            String[] lore = new String[particles.size() + 1];
            lore[0] = LangManager.getText(Lang.GUI_COLOR_SUBTEXT) + LangManager.getText(Lang.GUI_CLICK_TO_LOAD, particles.size());
            int i = 1;
            for (ParticlePair particle : particles) {
                lore[i] = LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_PARTICLE_INFO, particle.getId(), ParticleUtils.formatName(particle.getEffect().getName()), ParticleUtils.formatName(particle.getStyle().getName()), particle.getDataString());
                i++;
            }

            // Load Group Buttons
            GuiActionButton groupButton = new GuiActionButton(index, group.getGuiIcon(), LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + group.getDisplayName(), lore, (button, isShiftClick) -> {
                ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                activeGroup.getParticles().clear();
                for (ParticlePair particle : particles) {
                    ParticlePair clonedParticle = particle.clone();
                    clonedParticle.setOwner(pplayer);
                    activeGroup.getParticles().add(clonedParticle);
                }
                dataManager.saveParticleGroup(pplayer.getUniqueId(), activeGroup);

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
                    LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_BACK_BUTTON),
                    new String[]{},
                    (button, isShiftClick) -> GuiManager.transition(new GuiInventoryDefault(pplayer)));
            this.actionButtons.add(backButton);
        } else {
            // Reset Particles Button
            GuiActionButton resetParticles = new GuiActionButton(
                    49,
                    GuiIcon.RESET.get(),
                    LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_RESET_PARTICLES),
                    new String[]{LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_RESET_PARTICLES_DESCRIPTION)},
                    (button, isShiftClick) -> {
                        // Reset particles
                        dataManager.saveParticleGroup(pplayer.getUniqueId(), ParticleGroup.getDefaultGroup());
                    });
            this.actionButtons.add(resetParticles);
        }

        this.populate();
    }

}
