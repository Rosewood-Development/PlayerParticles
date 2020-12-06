package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.gui.hook.PlayerChatHook;
import dev.esophose.playerparticles.gui.hook.PlayerChatHookData;
import dev.esophose.playerparticles.manager.ConfigurationManager.GuiIcon;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.NMSUtil;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;

public class GuiInventoryManageGroups extends GuiInventory {

    public GuiInventoryManageGroups(PPlayer pplayer, int pageNumber) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, PlayerParticles.getInstance().getManager(LocaleManager.class).getLocaleMessage("gui-manage-your-groups")));

        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        this.fillBorder(BorderColor.BROWN);

        List<ParticleGroup> groups = pplayer.getParticleGroups().values().stream()
                .filter(x -> !x.getName().equals(ParticleGroup.DEFAULT_NAME))
                .sorted(Comparator.comparing(ParticleGroup::getName))
                .collect(Collectors.toList());

        int numberOfItems = groups.size();
        int itemsPerPage = 28;
        int maxPages = (int) Math.max(1, Math.ceil((double) numberOfItems / itemsPerPage));
        int slot = 10;
        int nextWrap = 17;
        int maxIndex = 43;

        for (int i = (pageNumber - 1) * itemsPerPage; i < numberOfItems; i++) {
            ParticleGroup group = groups.get(i);
            if (group.getName().equals(ParticleGroup.DEFAULT_NAME))
                continue;

            List<ParticlePair> particles = new ArrayList<>(group.getParticles().values());
            particles.sort(Comparator.comparingInt(ParticlePair::getId));

            String[] lore = new String[particles.size() + 2];
            lore[0] = localeManager.getLocaleMessage("gui-color-subtext") + localeManager.getLocaleMessage("gui-click-to-load", StringPlaceholders.single("amount", particles.size()));
            int n = 1;
            for (ParticlePair particle : particles) {
                StringPlaceholders stringPlaceholders = StringPlaceholders.builder("id", particle.getId())
                        .addPlaceholder("effect", ParticleUtils.formatName(particle.getEffect().getName()))
                        .addPlaceholder("style", ParticleUtils.formatName(particle.getStyle().getName()))
                        .addPlaceholder("data", particle.getDataString())
                        .build();
                lore[n] = localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-particle-info", stringPlaceholders);
                n++;
            }
            lore[n] = localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-shift-click-to-delete");

            // Load Group Buttons
            GuiActionButton groupButton = new GuiActionButton(
                    slot,
                    GuiIcon.GROUPS.get(),
                    localeManager.getLocaleMessage("gui-color-icon-name") + group.getName(),
                    lore,
                    (button, isShiftClick) -> {
                        if (isShiftClick) {
                            PlayerParticlesAPI.getInstance().removePlayerParticleGroup(pplayer.getPlayer(), group.getName());

                            this.actionButtons.remove(button);
                            this.inventory.setItem(button.getSlot(), null);
                        } else {
                            ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                            activeGroup.getParticles().clear();
                            for (ParticlePair particle : particles)
                                activeGroup.getParticles().put(particle.getId(), particle.clone());
                            PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), activeGroup);

                            if (Setting.GUI_CLOSE_AFTER_GROUP_SELECTED.getBoolean())
                                this.close();
                        }
                    });
            this.actionButtons.add(groupButton);

            slot++;
            if (slot == nextWrap) { // Loop around border
                nextWrap += 9;
                slot += 2;
            }
            if (slot > maxIndex) break; // Overflowed the available space
        }

        // Previous page button
        if (pageNumber != 1) {
            GuiActionButton previousPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 6,
                    GuiIcon.PREVIOUS_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-previous-page-button", StringPlaceholders.builder("start", pageNumber - 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryManageGroups(pplayer, pageNumber - 1)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-next-page-button", StringPlaceholders.builder("start", pageNumber + 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryManageGroups(pplayer, pageNumber + 1)));
            this.actionButtons.add(nextPageButton);
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
                INVENTORY_SIZE - 5,
                GuiIcon.CREATE.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-save-group"),
                lore,
                (button, isShiftClick) -> {
                    if (hasReachedMax || !hasParticles)
                        return;

                    if (!Setting.GUI_GROUP_CREATION_BUNGEE_SUPPORT.getBoolean()) {
                        PlayerChatHook.addHook(new PlayerChatHookData(pplayer.getUniqueId(), 15, (textEntered) -> {
                            if (textEntered != null && !textEntered.equalsIgnoreCase("cancel")) {
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
                                    Map<Integer, ParticlePair> particles = new ConcurrentHashMap<>();
                                    for (ParticlePair particle : pplayer.getActiveParticles())
                                        particles.put(particle.getId(), particle.clone()); // Make sure the ParticlePairs aren't the same references in both the active and saved group
                                    group = new ParticleGroup(groupName, particles);
                                } else {
                                    groupUpdated = true;
                                }

                                // Apply changes and notify player
                                PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);
                                if (groupUpdated) {
                                    localeManager.sendMessage(pplayer, "group-save-success-overwrite", StringPlaceholders.single("name", groupName));
                                } else {
                                    localeManager.sendMessage(pplayer, "group-save-success", StringPlaceholders.single("name", groupName));
                                }

                            }
                            guiManager.transition(new GuiInventoryManageGroups(pplayer, pageNumber));
                        }));
                    } else {
                        if (NMSUtil.getVersionNumber() >= 8) {
                            String message = localeManager.getLocaleMessage("gui-save-group-chat-message");
                            String prefix = "";
                            if (Setting.USE_MESSAGE_PREFIX.getBoolean())
                                prefix = localeManager.getLocaleMessage("prefix");

                            TextComponent component = new TextComponent(TextComponent.fromLegacyText(prefix + message));
                            component.setClickEvent(new ClickEvent(Action.SUGGEST_COMMAND, "/pp group save "));
                            pplayer.getPlayer().spigot().sendMessage(component);
                        } else {
                            localeManager.sendMessage(pplayer, "gui-save-group-chat-message");
                        }
                    }

                    this.close();
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
