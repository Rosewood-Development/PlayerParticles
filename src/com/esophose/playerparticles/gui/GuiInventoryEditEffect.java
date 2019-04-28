package com.esophose.playerparticles.gui;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.manager.SettingManager.GuiIcon;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.util.ParticleUtils;
import org.bukkit.Bukkit;

import java.util.List;

public class GuiInventoryEditEffect extends GuiInventory {

    public GuiInventoryEditEffect(PPlayer pplayer, ParticlePair editingParticle, int pageNumber, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, LangManager.getText(Lang.GUI_SELECT_EFFECT)));

        this.fillBorder(BorderColor.LIGHT_BLUE);

        // Select Effect Buttons
        List<ParticleEffect> effectsUserHasPermissionFor = PermissionManager.getEffectsUserHasPermissionFor(pplayer.getPlayer());
        int numberOfItems = effectsUserHasPermissionFor.size();
        int maxPages = (int) Math.floor(numberOfItems / 28.0);
        int itemsPerPage = 28;
        int slot = 10;
        int nextWrap = 17;
        int maxSlot = 43;

        for (int i = (pageNumber - 1) * itemsPerPage; i < numberOfItems; i++) {
            ParticleEffect effect = effectsUserHasPermissionFor.get(i);
            GuiActionButton selectButton = new GuiActionButton(
                    slot,
                    GuiIcon.EFFECT.get(effect.getName()),
                    LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + ParticleUtils.formatName(effect.getName()),
                    new String[]{LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_EFFECT_DESCRIPTION, ParticleUtils.formatName(effect.getName()))},
                    (button, isShiftClick) -> {
                        editingParticle.setEffect(effect);
                        callbackList.get(callbackListPosition + 1).execute();
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
                LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_BACK_BUTTON),
                new String[]{},
                (button, isShiftClick) -> callbackList.get(callbackListPosition - 1).execute());
        this.actionButtons.add(backButton);

        // Previous page button
        if (pageNumber != 1) {
            GuiActionButton previousPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 6,
                    GuiIcon.PREVIOUS_PAGE.get(),
                    LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_PREVIOUS_PAGE_BUTTON, pageNumber - 1, maxPages),
                    new String[]{},
                    (button, isShiftClick) -> GuiHandler.transition(new GuiInventoryEditEffect(pplayer, editingParticle, pageNumber - 1, callbackList, callbackListPosition)));
            this.actionButtons.add(previousPageButton);
        }

        // Next page button
        if (pageNumber != maxPages) {
            GuiActionButton nextPageButton = new GuiActionButton(
                    INVENTORY_SIZE - 4,
                    GuiIcon.NEXT_PAGE.get(),
                    LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_NEXT_PAGE_BUTTON, pageNumber + 1, maxPages),
                    new String[]{},
                    (button, isShiftClick) -> GuiHandler.transition(new GuiInventoryEditEffect(pplayer, editingParticle, pageNumber + 1, callbackList, callbackListPosition)));
            this.actionButtons.add(nextPageButton);
        }

        this.populate();
    }

}
