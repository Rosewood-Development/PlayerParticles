package com.esophose.playerparticles.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.manager.SettingManager.GuiIcon;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.particles.ParticleGroup;
import com.esophose.playerparticles.particles.ParticlePair;
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
            LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_SAVED_GROUPS, pplayer.getParticleGroups().size() - 1),
            LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_FIXED_EFFECTS, pplayer.getFixedEffectIds().size()),
            " ",
            LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_COMMANDS_INFO)
        };
        currentIconMeta.setLore(GuiActionButton.parseLore(currentIconLore));
        currentIconMeta.setOwner(pplayer.getPlayer().getName());
        headIcon.setItemMeta(currentIconMeta);
        
        this.inventory.setItem(13, headIcon);
        
        // Define what slots to put the icons at based on what other slots are visible
        boolean manageGroupsVisible = PermissionManager.canPlayerSaveGroups(pplayer);
        boolean loadPresetGroupVisible = !ParticleGroup.getPresetGroupsForPlayer(pplayer.getPlayer()).isEmpty();
        int manageParticlesSlot = -1, manageGroupsSlot = -1, loadPresetGroupSlot = -1;
        
        if (!manageGroupsVisible && !loadPresetGroupVisible) {
            manageParticlesSlot = 22;
        } else if (!manageGroupsVisible) {
            manageParticlesSlot = 21;
            loadPresetGroupSlot = 23;
        } else if (!loadPresetGroupVisible) {
            manageParticlesSlot = 21;
            manageGroupsSlot = 23;
        } else {
            manageParticlesSlot = 20;
            manageGroupsSlot = 22;
            loadPresetGroupSlot = 24;
        }
        
        // Manage Your Particles button
        GuiActionButton manageYourParticlesButton = new GuiActionButton(manageParticlesSlot, 
                                                                        GuiIcon.PARTICLES.get(), 
                                                                        LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_MANAGE_YOUR_PARTICLES), 
                                                                        new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_MANAGE_YOUR_PARTICLES_DESCRIPTION) }, 
                                                                        (button, isShiftClick) -> {
                                                                            GuiHandler.transition(new GuiInventoryManageParticles(pplayer));
                                                                        });
        this.actionButtons.add(manageYourParticlesButton);
        
        // Manage Your Groups button
        if (manageGroupsVisible) {
            GuiActionButton manageYourGroupsButton = new GuiActionButton(manageGroupsSlot, 
                                                                         GuiIcon.GROUPS.get(), 
                                                                         LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_MANAGE_YOUR_GROUPS), 
                                                                         new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_MANAGE_YOUR_GROUPS_DESCRIPTION) }, 
                                                                         (button, isShiftClick) -> {
                                                                             GuiHandler.transition(new GuiInventoryManageGroups(pplayer));
                                                                         });
            this.actionButtons.add(manageYourGroupsButton);
        }
        
        // Load Preset Groups
        if (loadPresetGroupVisible) {
            GuiActionButton loadPresetGroups = new GuiActionButton(loadPresetGroupSlot, 
                                                                   GuiIcon.PRESET_GROUPS.get(), 
                                                                   LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_LOAD_A_PRESET_GROUP), 
                                                                   new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_LOAD_A_PRESET_GROUP_DESCRIPTION) }, 
                                                                   (button, isShiftClick) -> {
                                                                       GuiHandler.transition(new GuiInventoryLoadPresetGroups(pplayer));
                                                                   });
            this.actionButtons.add(loadPresetGroups);
        }
        
        final ParticlePair editingParticle = pplayer.getPrimaryParticle();
        boolean canEditPrimaryStyleAndData = pplayer.getActiveParticle(1) != null;
        boolean doesEffectUseData = editingParticle.getEffect().hasProperty(ParticleProperty.COLORABLE) || editingParticle.getEffect().hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA);
        
        // Edit Primary Effect
        GuiActionButton editPrimaryEffect = new GuiActionButton(38,
                                                                GuiIcon.EDIT_EFFECT.get(),
                                                                LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_EFFECT),
                                                                new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_EFFECT_DESCRIPTION) },
                                                                (button, isShiftClick) -> {
                                                                    List<GuiInventoryEditFinishedCallback> callbacks = new ArrayList<GuiInventoryEditFinishedCallback>();
                                                                    callbacks.add(() -> GuiHandler.transition(new GuiInventoryDefault(pplayer)));
                                                                    callbacks.add(() -> GuiHandler.transition(new GuiInventoryEditEffect(pplayer, editingParticle, callbacks, 1)));
                                                                    callbacks.add(() -> {
                                                                        ParticleGroup group = pplayer.getActiveParticleGroup();
                                                                        if (canEditPrimaryStyleAndData) {
                                                                            for (ParticlePair particle : group.getParticles()) {
                                                                                if (particle.getId() == editingParticle.getId()) {
                                                                                    particle.setEffect(editingParticle.getEffect());
                                                                                    break;
                                                                                }
                                                                            }
                                                                        } else {
                                                                            group.getParticles().add(editingParticle);
                                                                        }
                                                                        DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
                                                                        
                                                                        GuiHandler.transition(new GuiInventoryDefault(pplayer));
                                                                    });
                                                                    
                                                                    callbacks.get(1).execute();
                                                                });
        this.actionButtons.add(editPrimaryEffect);
        
        // Edit Primary Style
        String[] editPrimaryStyleLore;
        if (canEditPrimaryStyleAndData) {
            editPrimaryStyleLore = new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_STYLE_DESCRIPTION) };
        } else {
            editPrimaryStyleLore = new String[] {
                LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_STYLE_DESCRIPTION),
                LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_STYLE_MISSING_EFFECT)
            };
        }
        GuiActionButton editPrimaryStyle = new GuiActionButton(40,
                                                               GuiIcon.EDIT_STYLE.get(),
                                                               LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_STYLE),
                                                               editPrimaryStyleLore,
                                                               (button, isShiftClick) -> {
                                                                   if (!canEditPrimaryStyleAndData) return;
                                                                   
                                                                   List<GuiInventoryEditFinishedCallback> callbacks = new ArrayList<GuiInventoryEditFinishedCallback>();
                                                                   callbacks.add(() -> GuiHandler.transition(new GuiInventoryDefault(pplayer)));
                                                                   callbacks.add(() -> GuiHandler.transition(new GuiInventoryEditStyle(pplayer, editingParticle, callbacks, 1)));
                                                                   callbacks.add(() -> {
                                                                       ParticleGroup group = pplayer.getActiveParticleGroup();
                                                                       for (ParticlePair particle : group.getParticles()) {
                                                                           if (particle.getId() == editingParticle.getId()) {
                                                                               particle.setStyle(editingParticle.getStyle());
                                                                               break;
                                                                           }
                                                                       }
                                                                       DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
                                                                       
                                                                       GuiHandler.transition(new GuiInventoryDefault(pplayer));
                                                                   });
                                                                   
                                                                   callbacks.get(1).execute();
                                                               });
        this.actionButtons.add(editPrimaryStyle);
        
        // Edit Primary Data
        String[] editPrimaryDataLore;
        if (canEditPrimaryStyleAndData && doesEffectUseData) {
            editPrimaryDataLore = new String[] { LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_DATA_DESCRIPTION) };
        } else if (canEditPrimaryStyleAndData) {
            editPrimaryDataLore = new String[] { 
                LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_DATA_DESCRIPTION),
                LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_DATA_UNAVAILABLE)
            };
        } else {
            editPrimaryDataLore = new String[] { 
                LangManager.getText(Lang.GUI_COLOR_INFO) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_DATA_DESCRIPTION),
                LangManager.getText(Lang.GUI_COLOR_UNAVAILABLE) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_DATA_MISSING_EFFECT)
            };
        }
        GuiActionButton editPrimaryData = new GuiActionButton(42,
                                                              GuiIcon.EDIT_DATA.get(),
                                                              LangManager.getText(Lang.GUI_COLOR_ICON_NAME) + LangManager.getText(Lang.GUI_EDIT_PRIMARY_DATA),
                                                              editPrimaryDataLore,
                                                              (button, isShiftClick) -> {
                                                                  if (!canEditPrimaryStyleAndData || !doesEffectUseData) return;
                                                                  
                                                                  List<GuiInventoryEditFinishedCallback> callbacks = new ArrayList<GuiInventoryEditFinishedCallback>();
                                                                  callbacks.add(() -> GuiHandler.transition(new GuiInventoryDefault(pplayer)));
                                                                  callbacks.add(() -> GuiHandler.transition(new GuiInventoryEditData(pplayer, editingParticle, callbacks, 1)));
                                                                  callbacks.add(() -> {
                                                                      ParticleGroup group = pplayer.getActiveParticleGroup();
                                                                      for (ParticlePair particle : group.getParticles()) {
                                                                          if (particle.getId() == editingParticle.getId()) {
                                                                              particle.setColor(editingParticle.getColor());
                                                                              particle.setNoteColor(editingParticle.getNoteColor());
                                                                              particle.setItemMaterial(editingParticle.getItemMaterial());
                                                                              particle.setBlockMaterial(editingParticle.getBlockMaterial());
                                                                              break;
                                                                          }
                                                                      }
                                                                      DataManager.saveParticleGroup(pplayer.getUniqueId(), group);
                                                                      
                                                                      GuiHandler.transition(new GuiInventoryDefault(pplayer));
                                                                  });
                                                                  
                                                                  callbacks.get(1).execute();
                                                              });
        this.actionButtons.add(editPrimaryData);
        
        this.populate();
    }

}
