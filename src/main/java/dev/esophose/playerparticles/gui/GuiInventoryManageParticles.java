package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.ConfigurationManager.GuiIcon;
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

public class GuiInventoryManageParticles extends GuiInventory {

    public GuiInventoryManageParticles(PPlayer pplayer) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, PlayerParticles.getInstance().getManager(LocaleManager.class).getLocaleMessage("gui-manage-your-particles")));

        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        this.fillBorder(BorderColor.ORANGE);

        // Manage/Delete Particle Buttons
        List<ParticlePair> particles = new ArrayList<>(pplayer.getActiveParticles());
        particles.sort(Comparator.comparingInt(ParticlePair::getId));

        int index = 10;
        int nextWrap = 17;
        int maxIndex = 35;
        for (ParticlePair particle : particles) {
            StringPlaceholders stringPlaceholders = StringPlaceholders.builder("id", particle.getId())
                    .addPlaceholder("effect", ParticleUtils.formatName(particle.getEffect().getName()))
                    .addPlaceholder("style", ParticleUtils.formatName(particle.getStyle().getName()))
                    .addPlaceholder("data", particle.getDataString())
                    .build();
            GuiActionButton selectButton = new GuiActionButton(
                    index,
                    GuiIcon.PARTICLES.get(),
                    localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-particle-name", StringPlaceholders.single("id", particle.getId())),
                    new String[]{
                            localeManager.getLocaleMessage("gui-color-subtext") + localeManager.getLocaleMessage("gui-click-to-edit-particle"),
                            localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-particle-info", stringPlaceholders),
                            localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-shift-click-to-delete")
                    },
                    (button, isShiftClick) -> {
                        if (!isShiftClick) {
                            guiManager.transition(new GuiInventoryEditParticle(pplayer, particle));
                        } else {
                            // Delete particle
                            ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                            activeGroup.getParticles().remove(particle.getId());
                            PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), activeGroup);

                            // Update inventory to reflect deletion
                            this.actionButtons.remove(button);
                            this.inventory.setItem(button.getSlot(), null);
                        }
                    });
            this.actionButtons.add(selectButton);

            index++;
            if (index == nextWrap) { // Loop around border
                nextWrap += 9;
                index += 2;
            }
            if (index > maxIndex) break; // Overflowed the available space
        }

        // Create New Particle Button
        boolean canCreate = pplayer.getActiveParticles().size() < PlayerParticles.getInstance().getManager(PermissionManager.class).getMaxParticlesAllowed(pplayer);
        String lore = localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-create-particle-description");
        GuiActionButton createNewParticle = new GuiActionButton(
                38,
                GuiIcon.CREATE.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-create-particle"),
                canCreate ? new String[]{lore} : new String[]{lore, localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-create-particle-unavailable")},
                (button, isShiftClick) -> {
                    if (!canCreate) return;
                    ParticlePair editingParticle = ParticlePair.getNextDefault(pplayer);
                    List<Runnable> callbacks = new ArrayList<>();
                    callbacks.add(() -> guiManager.transition(new GuiInventoryManageParticles(pplayer)));
                    callbacks.add(() -> guiManager.transition(new GuiInventoryEditEffect(pplayer, editingParticle, 1, callbacks, 1)));
                    callbacks.add(() -> guiManager.transition(new GuiInventoryEditStyle(pplayer, editingParticle, 1, callbacks, 2)));
                    callbacks.add(() -> {
                        if (editingParticle.getEffect().hasProperties()) {
                            guiManager.transition(new GuiInventoryEditData(pplayer, editingParticle, 1, callbacks, 3, null));
                        } else {
                            callbacks.get(4).run();
                        }
                    });
                    callbacks.add(() -> {
                        // Save new particle
                        ParticleGroup group = pplayer.getActiveParticleGroup();
                        group.getParticles().put(editingParticle.getId(), editingParticle);
                        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);

                        // Reopen the manage particle inventory
                        guiManager.transition(new GuiInventoryManageParticles(pplayer));
                    });
                    callbacks.get(1).run();
                });
        this.actionButtons.add(createNewParticle);

        // Reset Particles Button
        GuiActionButton resetParticles = new GuiActionButton(42,
                GuiIcon.RESET.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-reset-particles"),
                new String[]{localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-reset-particles-description")},
                (button, isShiftClick) -> {
                    // Reset particles
                    PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), ParticleGroup.getDefaultGroup());

                    // Reopen this same inventory to refresh it
                    guiManager.transition(new GuiInventoryManageParticles(pplayer));
                });
        this.actionButtons.add(resetParticles);

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
