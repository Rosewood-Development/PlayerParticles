package com.esophose.playerparticles.gui;

import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.SettingManager.GUIIcon;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleGroup;
import com.esophose.playerparticles.particles.ParticlePair;

public class GuiInventoryManageGroups extends GuiInventory {

    public GuiInventoryManageGroups(PPlayer pplayer) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, LangManager.getText(Lang.GUI_MANAGE_YOUR_GROUPS)));
        
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
            lore[0] = LangManager.getText(Lang.GUI_COLOR_SUBTEXT) + LangManager.getText(Lang.GUI_CLICK_TO_LOAD, particles.size());
            int i = 1;
            for (ParticlePair particle : particles) {
                lore[i] = LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_PARTICLE_INFO, particle.getId(), particle.getEffect().getName(), particle.getStyle().getName(), particle.getDataString());
                i++;
            }
            lore[i] = LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_SHIFT_CLICK_TO_DELETE);
            
            // Load Group Buttons
            GuiActionButton groupButton = new GuiActionButton(index, GUIIcon.GROUPS.get(), LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + group.getName(), lore, (button, isShiftClick) -> {
                if (isShiftClick) {
                    DataManager.removeParticleGroup(pplayer.getUniqueId(), group);
                    
                    this.actionButtons.remove(button);
                    this.inventory.setItem(button.getSlot(), null);
                } else {
                    ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                    activeGroup.getParticles().clear();
                    for (ParticlePair particle : particles)
                        activeGroup.getParticles().add(particle.clone());
                    DataManager.saveParticleGroup(pplayer.getUniqueId(), activeGroup);
                    
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
        
        // Save Group Button
        GuiActionButton saveGroupButton = new GuiActionButton(40, 
                                                              GUIIcon.CREATE.get(), 
                                                              LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_SAVE_GROUP),
                                                              new String[] { 
                                                                  LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SAVE_GROUP_DESCRIPTION),
                                                                  LangManager.getText(Lang.GUI_COLOR_SUBTEXT) + LangManager.getText(Lang.GUI_SAVE_GROUP_DESCRIPTION_2),
                                                              },
                                                              (button, isShiftClick) -> {}); // Does nothing on click
        this.actionButtons.add(saveGroupButton);
        
        // Back Button
        GuiActionButton backButton = new GuiActionButton(INVENTORY_SIZE - 1, GUIIcon.BACK.get(), LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_BACK_BUTTON), new String[] {}, (button, isShiftClick) -> {
            GuiHandler.transition(new GuiInventoryDefault(pplayer));
        });
        this.actionButtons.add(backButton);
        
        this.populate();
    }

}
