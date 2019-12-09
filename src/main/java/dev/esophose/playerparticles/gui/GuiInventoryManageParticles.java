package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.manager.SettingManager.GuiIcon;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.ParticleUtils;
import org.bukkit.Bukkit;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GuiInventoryManageParticles extends GuiInventory {

    public GuiInventoryManageParticles(PPlayer pplayer) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, LangManager.getText(Lang.GUI_MANAGE_YOUR_PARTICLES)));

        DataManager dataManager = PlayerParticles.getInstance().getManager(DataManager.class);

        this.fillBorder(BorderColor.ORANGE);

        // Manage/Delete Particle Buttons
        List<ParticlePair> particles = pplayer.getActiveParticles();
        particles.sort(Comparator.comparingInt(ParticlePair::getId));

        int index = 10;
        int nextWrap = 17;
        int maxIndex = 35;
        for (ParticlePair particle : particles) {
            GuiActionButton selectButton = new GuiActionButton(
                    index,
                    GuiIcon.PARTICLES.get(),
                    LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_PARTICLE_NAME, particle.getId()),
                    new String[]{
                            LangManager.getText(Lang.GUI_COLOR_SUBTEXT) + LangManager.getText(Lang.GUI_CLICK_TO_EDIT_PARTICLE, particles.size()),
                            LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_PARTICLE_INFO, particle.getId(), ParticleUtils.formatName(particle.getEffect().getName()), ParticleUtils.formatName(particle.getStyle().getName()), particle.getDataString()),
                            LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_SHIFT_CLICK_TO_DELETE)
                    },
                    (button, isShiftClick) -> {
                        if (!isShiftClick) {
                            GuiManager.transition(new GuiInventoryEditParticle(pplayer, particle));
                        } else {
                            // Delete particle
                            ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                            for (ParticlePair pp : activeGroup.getParticles()) {
                                if (pp.getId() == particle.getId()) {
                                    activeGroup.getParticles().remove(pp);
                                    break;
                                }
                            }
                            dataManager.saveParticleGroup(pplayer.getUniqueId(), activeGroup);

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
        boolean canCreate = pplayer.getActiveParticles().size() < PlayerParticles.getInstance().getManager(PermissionManager.class).getMaxParticlesAllowed(pplayer.getPlayer());
        String lore = LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_CREATE_PARTICLE_DESCRIPTION);
        GuiActionButton createNewParticle = new GuiActionButton(
                38,
                GuiIcon.CREATE.get(),
                LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_CREATE_PARTICLE),
                canCreate ? new String[]{lore} : new String[]{lore, LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_CREATE_PARTICLE_UNAVAILABLE)},
                (button, isShiftClick) -> {
                    if (!canCreate) return;
                    ParticlePair editingParticle = ParticlePair.getNextDefault(pplayer);
                    List<GuiInventoryEditFinishedCallback> callbacks = new ArrayList<>();
                    callbacks.add(() -> GuiManager.transition(new GuiInventoryManageParticles(pplayer)));
                    callbacks.add(() -> GuiManager.transition(new GuiInventoryEditEffect(pplayer, editingParticle, 1, callbacks, 1)));
                    callbacks.add(() -> GuiManager.transition(new GuiInventoryEditStyle(pplayer, editingParticle, 1, callbacks, 2)));
                    callbacks.add(() -> {
                        if (editingParticle.getEffect().hasProperty(ParticleProperty.COLORABLE) || editingParticle.getEffect().hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                            GuiManager.transition(new GuiInventoryEditData(pplayer, editingParticle, 1, callbacks, 3));
                        } else {
                            callbacks.get(4).execute();
                        }
                    });
                    callbacks.add(() -> {
                        // Save new particle
                        ParticleGroup group = pplayer.getActiveParticleGroup();
                        group.getParticles().add(editingParticle);
                        dataManager.saveParticleGroup(pplayer.getUniqueId(), group);

                        // Reopen the manage particle inventory
                        GuiManager.transition(new GuiInventoryManageParticles(pplayer));
                    });
                    callbacks.get(1).execute();
                });
        this.actionButtons.add(createNewParticle);

        // Reset Particles Button
        GuiActionButton resetParticles = new GuiActionButton(42,
                GuiIcon.RESET.get(),
                LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_RESET_PARTICLES),
                new String[]{LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_RESET_PARTICLES_DESCRIPTION)},
                (button, isShiftClick) -> {
                    // Reset particles
                    dataManager.saveParticleGroup(pplayer.getUniqueId(), ParticleGroup.getDefaultGroup());

                    // Reopen this same inventory to refresh it
                    GuiManager.transition(new GuiInventoryManageParticles(pplayer));
                });
        this.actionButtons.add(resetParticles);

        // Back Button
        GuiActionButton backButton = new GuiActionButton(
                INVENTORY_SIZE - 1,
                GuiIcon.BACK.get(),
                LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_BACK_BUTTON),
                new String[]{},
                (button, isShiftClick) -> GuiManager.transition(new GuiInventoryDefault(pplayer)));
        this.actionButtons.add(backButton);

        this.populate();
    }

}
