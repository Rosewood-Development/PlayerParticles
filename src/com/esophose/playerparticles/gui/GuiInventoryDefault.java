package com.esophose.playerparticles.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.SettingManager.GUIIcon;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.util.ParticleUtils;

@SuppressWarnings("deprecation")
public class GuiInventoryDefault extends GuiInventory {

    public GuiInventoryDefault(PPlayer pplayer) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, LangManager.getText(Lang.GUI_PLAYERPARTICLES)));
        
        this.fillBorder(BorderColor.WHITE);
        
        // PPlayer information icon
        ItemStack headIcon;
        Material playerHead = ParticleUtils.closestMatch("PLAYER_HEAD");
        if (playerHead != null) {
            headIcon = new ItemStack(playerHead, 1);
        } else {
            headIcon = new ItemStack(ParticleUtils.closestMatch("SKULL_ITEM"), 1, (short) SkullType.PLAYER.ordinal());
        }

        SkullMeta currentIconMeta = (SkullMeta) headIcon.getItemMeta();
        currentIconMeta.setDisplayName(LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + pplayer.getPlayer().getName());
        String[] currentIconLore = new String[] {
            LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_ACTIVE_PARTICLES, pplayer.getActiveParticles().size()),
            LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SAVED_GROUPS, pplayer.getParticleGroups().size()),
            LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_FIXED_EFFECTS, pplayer.getFixedEffectIds().size())
        };
        currentIconMeta.setLore(Arrays.asList(currentIconLore));
        currentIconMeta.setOwner(pplayer.getPlayer().getName());
        headIcon.setItemMeta(currentIconMeta);
        
        this.inventory.setItem(13, headIcon);
        
        // Manage Your Particles button
        GuiActionButton manageYourParticlesButton = new GuiActionButton(38, 
                                                                        GUIIcon.PARTICLES.get(), 
                                                                        LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_MANAGE_YOUR_PARTICLES), 
                                                                        new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_MANAGE_YOUR_PARTICLES_DESCRIPTION) }, 
                                                                        (button, isShiftClick) -> {
            GuiHandler.transition(new GuiInventoryManageParticles(pplayer));
        });
        this.actionButtons.add(manageYourParticlesButton);
        
        // Manage Your Groups button
        GuiActionButton manageYourGroupsButton = new GuiActionButton(40, 
                                                                     GUIIcon.GROUPS.get(), 
                                                                     LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_MANAGE_YOUR_GROUPS), 
                                                                     new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_MANAGE_YOUR_GROUPS_DESCRIPTION) }, 
                                                                     (button, isShiftClick) -> {
            GuiHandler.transition(new GuiInventoryManageGroups(pplayer));
        });
        this.actionButtons.add(manageYourGroupsButton);
        
        // Load Preset Groups
        GuiActionButton loadPresetGroups = new GuiActionButton(42, 
                                                               GUIIcon.PRESET_GROUPS.get(), 
                                                               LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_LOAD_A_PRESET_GROUP), 
                                                               new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_LOAD_A_PRESET_GROUP_DESCRIPTION) }, 
                                                               (button, isShiftClick) -> {
            GuiHandler.transition(new GuiInventoryLoadPresetGroups(pplayer));
        });
        this.actionButtons.add(loadPresetGroups);
        
        this.populate();
    }

}
