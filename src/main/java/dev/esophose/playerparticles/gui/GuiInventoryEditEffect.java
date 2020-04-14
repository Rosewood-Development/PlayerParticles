package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.GuiIcon;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.List;
import org.bukkit.Bukkit;

public class GuiInventoryEditEffect extends GuiInventory {

    public GuiInventoryEditEffect(PPlayer pplayer, ParticlePair editingParticle, int pageNumber, List<Runnable> callbackList, int callbackListPosition) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, PlayerParticles.getInstance().getManager(LocaleManager.class).getLocaleMessage("gui-select-effect")));

        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        this.fillBorder(BorderColor.LIGHT_BLUE);

        // Select Effect Buttons
        List<ParticleEffect> effectsUserHasPermissionFor = PlayerParticles.getInstance().getManager(PermissionManager.class).getEffectsUserHasPermissionFor(pplayer);
        int numberOfItems = effectsUserHasPermissionFor.size();
        int itemsPerPage = 28;
        int maxPages = (int) Math.max(1, Math.ceil((double) numberOfItems / itemsPerPage));
        int slot = 10;
        int nextWrap = 17;
        int maxSlot = 43;

        for (int i = (pageNumber - 1) * itemsPerPage; i < numberOfItems; i++) {
            ParticleEffect effect = effectsUserHasPermissionFor.get(i);
            GuiActionButton selectButton = new GuiActionButton(
                    slot,
                    effect.getGuiIconMaterial(),
                    localeManager.getLocaleMessage("gui-color-icon-name") + ParticleUtils.formatName(effect.getName()),
                    new String[]{localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-select-effect-description", StringPlaceholders.single("effect", ParticleUtils.formatName(effect.getName())))},
                    (button, isShiftClick) -> {
                        editingParticle.setEffect(effect);
                        callbackList.get(callbackListPosition + 1).run();
                    });
            this.actionButtons.add(selectButton);

            slot++;
            if (slot == nextWrap) { // Loop around border
                nextWrap += 9;
                slot += 2;
            }
            if (slot > maxSlot) break; // Overflowed the available space
        }

        // Back Button
        GuiActionButton backButton = new GuiActionButton(
                INVENTORY_SIZE - 1,
                GuiIcon.BACK.get(),
                localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-back-button"),
                new String[]{},
                (button, isShiftClick) -> callbackList.get(callbackListPosition - 1).run());
        this.actionButtons.add(backButton);

        // Previous page button
        if (pageNumber != 1) {
            GuiActionButton previousPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 6,
                    GuiIcon.PREVIOUS_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-previous-page-button", StringPlaceholders.builder("start", pageNumber - 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryEditEffect(pplayer, editingParticle, pageNumber - 1, callbackList, callbackListPosition)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    localeManager.getLocaleMessage("gui-color-info") + localeManager.getLocaleMessage("gui-next-page-button", StringPlaceholders.builder("start", pageNumber + 1).addPlaceholder("end", maxPages).build()),
                    new String[]{},
                    (button, isShiftClick) -> guiManager.transition(new GuiInventoryEditEffect(pplayer, editingParticle, pageNumber + 1, callbackList, callbackListPosition)));
            this.actionButtons.add(nextPageButton);
        }

        this.populate();
    }

}
