package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.gui.hook.PlayerChatHook;
import dev.esophose.playerparticles.gui.hook.PlayerChatHookData;
import dev.esophose.playerparticles.manager.ConfigurationManager.GuiIcon;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.bukkit.Bukkit;

public class GuiInventoryManageGroups extends GuiInventory {

    public GuiInventoryManageGroups(PPlayer pplayer) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, PlayerParticles.getInstance().getManager(LocaleManager.class).getLocaleMessage("gui-manage-your-groups")));

        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        DataManager dataManager = PlayerParticles.getInstance().getManager(DataManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        this.fillBorder(BorderColor.BROWN);

        int index = 10;
        int nextWrap = 17;
        int maxIndex = 35;
        List<ParticleGroup> groups = pplayer.getParticleGroups();
        groups.sort(Comparator.comparing(ParticleGroup::getName));

        for (ParticleGroup group : groups) {
            if (group.getName().equals(ParticleGroup.DEFAULT_NAME)) continue;

            List<ParticlePair> particles = group.getParticles();
            particles.sort(Comparator.comparingInt(ParticlePair::getId));

            String[] lore = new String[particles.size() + 2];
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
            lore[i] = localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-shift-click-to-delete");

            // Load Group Buttons
            GuiActionButton groupButton = new GuiActionButton(
                    index,
                    GuiIcon.GROUPS.get(),
                    localeManager.getLocaleMessage("gui-color-icon-name") + group.getName(),
                    lore,
                    (button, isShiftClick) -> {
                        if (isShiftClick) {
                            dataManager.removeParticleGroup(pplayer.getUniqueId(), group);

                            this.actionButtons.remove(button);
                            this.inventory.setItem(button.getSlot(), null);
                        } else {
                            ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                            activeGroup.getParticles().clear();
                            for (ParticlePair particle : particles)
                                activeGroup.getParticles().add(particle.clone());
                            dataManager.saveParticleGroup(pplayer.getUniqueId(), activeGroup);

                            if (Setting.GUI_CLOSE_AFTER_GROUP_SELECTED.getBoolean()) {
                                pplayer.getPlayer().closeInventory();
                            }
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

        boolean hasReachedMax = PlayerParticles.getInstance().getManager(PermissionManager.class).hasPlayerReachedMaxGroups(pplayer);
        boolean hasParticles = !pplayer.getActiveParticles().isEmpty();
        String[] lore;
        if (hasReachedMax) {
            lore = new String[]{
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-save-group-description"),
                    localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-save-group-full")
            };
        } else if (!hasParticles) {
            lore = new String[]{
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-save-group-description"),
                    localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-save-group-no-particles")
            };
        } else {
            lore = new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-save-group-description")};
        }

        // Save Group Button
        GuiActionButton saveGroupButton = new GuiActionButton(
                40,
                GuiIcon.CREATE.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-save-group"),
                lore,
                (button, isShiftClick) -> {
                    if (hasReachedMax || !hasParticles) return;

                    PlayerChatHook.addHook(new PlayerChatHookData(pplayer.getUniqueId(), 15, (textEntered) -> {
                        if (textEntered == null || textEntered.equalsIgnoreCase("cancel")) {
                            guiManager.transition(new GuiInventoryManageGroups(pplayer));
                        } else {
                            String groupName = textEntered.split(" ")[0];

                            // Check that the groupName isn't the reserved name
                            if (groupName.equalsIgnoreCase(ParticleGroup.DEFAULT_NAME)) {
                                localeManager.sendMessage(pplayer, "group-reserved");
                                return;
                            }

                            // The database column can only hold up to 100 characters, cut it off there
                            if (groupName.length() >= 100) {
                                groupName = groupName.substring(0, 100);
                            }

                            // Use the existing group if available, otherwise create a new one
                            ParticleGroup group = pplayer.getParticleGroupByName(groupName);
                            boolean groupUpdated = false;
                            if (group == null) {
                                List<ParticlePair> particles = new ArrayList<ParticlePair>();
                                for (ParticlePair particle : pplayer.getActiveParticles())
                                    particles.add(particle.clone()); // Make sure the ParticlePairs aren't the same references in both the active and saved group
                                group = new ParticleGroup(groupName, particles);
                            } else {
                                groupUpdated = true;
                            }

                            // Apply changes and notify player
                            dataManager.saveParticleGroup(pplayer.getUniqueId(), group);
                            if (groupUpdated) {
                                localeManager.sendMessage(pplayer, "group-save-success-overwrite", StringPlaceholders.single("name", groupName));
                            } else {
                                localeManager.sendMessage(pplayer, "group-save-success", StringPlaceholders.single("name", groupName));
                            }

                            guiManager.transition(new GuiInventoryManageGroups(pplayer));
                        }
                    }));
                    pplayer.getPlayer().closeInventory();
                });
        this.actionButtons.add(saveGroupButton);

        // Back Button
        GuiActionButton backButton = new GuiActionButton(
                INVENTORY_SIZE - 1,
                GuiIcon.BACK.get(),
                localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-back-button"),
                new String[]{},
                (button, isShiftClick) -> guiManager.transition(new GuiInventoryDefault(pplayer)));
        this.actionButtons.add(backButton);

        this.populate();
    }

}
