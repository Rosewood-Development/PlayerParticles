package com.esophose.playerparticles.gui;

import java.util.List;

import org.bukkit.Bukkit;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.manager.SettingManager.GuiIcon;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.util.ParticleUtils;

public class GuiInventoryEditEffect extends GuiInventory {

    public GuiInventoryEditEffect(PPlayer pplayer, ParticlePair editingParticle, List<GuiInventoryEditFinishedCallback> callbackList, int callbackListPosition) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, LangManager.getText(Lang.GUI_SELECT_EFFECT)));
        
        // Select Effect Buttons
        List<ParticleEffect> effectsUserHasPermissionFor = PermissionManager.getEffectsUserHasPermissionFor(pplayer.getPlayer());
        for (int i = 0; i < effectsUserHasPermissionFor.size(); i++) {
            ParticleEffect effect = effectsUserHasPermissionFor.get(i);
            GuiActionButton selectButton = new GuiActionButton(i, 
                                                               GuiIcon.EFFECT.get(effect.getName()), 
                                                               LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + ParticleUtils.formatName(effect.getName()),
                                                               new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SELECT_EFFECT_DESCRIPTION, ParticleUtils.formatName(effect.getName())) },
                                                               (button, isShiftClick) -> {
                                                                   editingParticle.setEffect(effect);
                                                                   callbackList.get(callbackListPosition + 1).execute();
                                                               });
            this.actionButtons.add(selectButton);
        }
        
        // Back Button
        GuiActionButton backButton = new GuiActionButton(INVENTORY_SIZE - 1, GuiIcon.BACK.get(), LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_BACK_BUTTON), new String[] {}, (button, isShiftClick) -> {
            callbackList.get(callbackListPosition - 1).execute();
        });
        this.actionButtons.add(backButton);
        
        this.populate();
    }

}
