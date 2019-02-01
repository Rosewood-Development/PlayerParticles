package com.esophose.playerparticles.gui;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Bukkit;

import com.esophose.playerparticles.gui.hook.PlayerChatHook;
import com.esophose.playerparticles.gui.hook.PlayerChatHookData;
import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.SettingManager.GuiIcon;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleGroup;
import com.esophose.playerparticles.particles.ParticlePair;
import com.esophose.playerparticles.util.ParticleUtils;

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
                lore[i] = LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_PARTICLE_INFO, particle.getId(), ParticleUtils.formatName(particle.getEffect().getName()), ParticleUtils.formatName(particle.getStyle().getName()), particle.getDataString());
                i++;
            }
            lore[i] = LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_SHIFT_CLICK_TO_DELETE);
            
            // Load Group Buttons
            GuiActionButton groupButton = new GuiActionButton(index, GuiIcon.GROUPS.get(), LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + group.getName(), lore, (button, isShiftClick) -> {
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
        
        boolean hasReachedMax = PermissionManager.hasPlayerReachedMaxGroups(pplayer);
        boolean hasParticles = !pplayer.getActiveParticles().isEmpty();
        String[] lore;
        if (hasReachedMax) {
            lore = new String[] { 
                LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SAVE_GROUP_DESCRIPTION), 
                LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_SAVE_GROUP_FULL)
            };
        } else if (!hasParticles) {
            lore = new String[] { 
                LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SAVE_GROUP_DESCRIPTION), 
                LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_SAVE_GROUP_NO_PARTICLES)
            };
        } else {
            lore = new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SAVE_GROUP_DESCRIPTION) };
        }
        
        // Save Group Button
        GuiActionButton saveGroupButton = new GuiActionButton(40, 
                                                              GuiIcon.CREATE.get(), 
                                                              LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_SAVE_GROUP),
                                                              lore,
                                                              (button, isShiftClick) -> {
                                                                  if (hasReachedMax || !hasParticles) return;
                                                                  
                                                                  PlayerChatHook.addHook(new PlayerChatHookData(pplayer.getUniqueId(), 15, (textEntered) -> {
                                                                      if (textEntered == null || textEntered.equalsIgnoreCase("cancel")) {
                                                                          GuiHandler.transition(new GuiInventoryManageGroups(pplayer));
                                                                      } else {
                                                                          String groupName = textEntered.split(" ")[0];
                                                                          
                                                                          // Check that the groupName isn't the reserved name
                                                                          if (groupName.equalsIgnoreCase(ParticleGroup.DEFAULT_NAME)) {
                                                                              LangManager.sendMessage(pplayer, Lang.GROUP_RESERVED);
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
                                                                          DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
                                                                          if (groupUpdated) {
                                                                              LangManager.sendMessage(pplayer, Lang.GROUP_SAVE_SUCCESS_OVERWRITE, groupName);
                                                                          } else {
                                                                              LangManager.sendMessage(pplayer, Lang.GROUP_SAVE_SUCCESS, groupName);
                                                                          }
                                                                          
                                                                          GuiHandler.transition(new GuiInventoryManageGroups(pplayer));
                                                                      }
                                                                  }));
                                                                  pplayer.getPlayer().closeInventory();
                                                              });
        this.actionButtons.add(saveGroupButton);
        
        // Back Button
        GuiActionButton backButton = new GuiActionButton(INVENTORY_SIZE - 1, GuiIcon.BACK.get(), LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_BACK_BUTTON), new String[] {}, (button, isShiftClick) -> {
            GuiHandler.transition(new GuiInventoryDefault(pplayer));
        });
        this.actionButtons.add(backButton);
        
        this.populate();
    }

}
