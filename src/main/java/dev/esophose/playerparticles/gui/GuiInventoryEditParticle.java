package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.ConfigurationManager.GuiIcon;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

public class GuiInventoryEditParticle extends GuiInventory {

    public GuiInventoryEditParticle(PPlayer pplayer, ParticlePair editingParticle) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, PlayerParticles.getInstance().getManager(LocaleManager.class).getLocaleMessage("gui-editing-particle", StringPlaceholders.single("id", editingParticle.getId()))));

        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        this.fillBorder(BorderColor.RED);

        // Particle Info Icon
        StringPlaceholders stringPlaceholders = StringPlaceholders.builder("id", editingParticle.getId())
                .addPlaceholder("effect", editingParticle.getEffect().getName())
                .addPlaceholder("style", editingParticle.getStyle().getName())
                .addPlaceholder("data", editingParticle.getDataString())
                .build();
        String particleInfo = localeManager.getLocaleMessage("gui-particle-info", stringPlaceholders);
        GuiActionButton particleInfoIcon = new GuiActionButton(
                13,
                GuiIcon.PARTICLES.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-particle-name", StringPlaceholders.single("id", editingParticle.getId())),
                new String[]{localeManager.getLocaleMessage("gui-color-info") + particleInfo},
                (button, isShiftClick) -> { });
        this.actionButtons.add(particleInfoIcon);

        // Edit Effect Button
        GuiActionButton editEffectButton = new GuiActionButton(
                38,
                GuiIcon.EDIT_EFFECT.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-edit-effect"),
                new String[]{localeManager.getLocaleMessage("gui-color-subtext") + localeManager.getLocaleMessage("gui-edit-effect-description")},
                (button, isShiftClick) -> {
                    List<Runnable> callbacks = new ArrayList<>();
                    callbacks.add(() -> guiManager.transition(new GuiInventoryEditParticle(pplayer, editingParticle)));
                    callbacks.add(() -> guiManager.transition(new GuiInventoryEditEffect(pplayer, editingParticle, 1, callbacks, 1)));
                    callbacks.add(() -> {
                        ParticleGroup group = pplayer.getActiveParticleGroup();
                        for (ParticlePair particle : group.getParticles().values()) {
                            if (particle.getId() == editingParticle.getId()) {
                                particle.setEffect(editingParticle.getEffect());
                                break;
                            }
                        }
                        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);

                        guiManager.transition(new GuiInventoryEditParticle(pplayer, editingParticle));
                    });

                    callbacks.get(1).run();
                });
        this.actionButtons.add(editEffectButton);

        // Edit Style Button
        GuiActionButton editStyleButton = new GuiActionButton(40,
                GuiIcon.EDIT_STYLE.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-edit-style"),
                new String[]{localeManager.getLocaleMessage("gui-color-subtext") + localeManager.getLocaleMessage("gui-edit-style-description")},
                (button, isShiftClick) -> {
                    List<Runnable> callbacks = new ArrayList<>();
                    callbacks.add(() -> guiManager.transition(new GuiInventoryEditParticle(pplayer, editingParticle)));
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

                        guiManager.transition(new GuiInventoryEditParticle(pplayer, editingParticle));
                    });

                    callbacks.get(1).run();
                });
        this.actionButtons.add(editStyleButton);

        // Edit Data Button
        boolean usesData = editingParticle.getEffect().hasProperties();
        GuiActionButton editDataButton = new GuiActionButton(42,
                GuiIcon.EDIT_DATA.get(),
                localeManager.getLocaleMessage("gui-color-icon-name") + localeManager.getLocaleMessage("gui-edit-data"),
                usesData ? new String[]{localeManager.getLocaleMessage("gui-color-subtext") + localeManager.getLocaleMessage("gui-edit-data-description")} :
                        new String[]{localeManager.getLocaleMessage("gui-color-unavailable") + localeManager.getLocaleMessage("gui-edit-data-unavailable")},
                (button, isShiftClick) -> {
                    if (usesData) {
                        List<Runnable> callbacks = new ArrayList<>();
                        callbacks.add(() -> guiManager.transition(new GuiInventoryEditParticle(pplayer, editingParticle)));
                        callbacks.add(() -> guiManager.transition(new GuiInventoryEditData(pplayer, editingParticle, 1, callbacks, 1, null)));
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

                            guiManager.transition(new GuiInventoryEditParticle(pplayer, editingParticle));
                        });

                        callbacks.get(1).run();
                    }
                });
        this.actionButtons.add(editDataButton);

        // Back Button
        GuiActionButton backButton = new GuiActionButton(
                INVENTORY_SIZE - 1,
                GuiIcon.BACK.get(),
                localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-back-button"),
                new String[]{},
                (button, isShiftClick) -> guiManager.transition(new GuiInventoryManageParticles(pplayer)));
        this.actionButtons.add(backButton);

        this.populate();
    }

}
