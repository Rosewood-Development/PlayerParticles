package com.esophose.playerparticles.gui;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.util.ParticleUtils;

@SuppressWarnings("deprecation")
public class GuiInventoryDefault extends GuiInventory {

    public GuiInventoryDefault(PPlayer pplayer) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, "PlayerParticles"));
        
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
        currentIconMeta.setDisplayName(ChatColor.GREEN + pplayer.getPlayer().getName());
        String[] currentIconLore = new String[] {
            ChatColor.YELLOW + "Active Particles: " + ChatColor.AQUA + pplayer.getActiveParticles().size(),
            ChatColor.YELLOW + "Saved Groups: " + ChatColor.AQUA + pplayer.getParticleGroups().size(),
            ChatColor.YELLOW + "Fixed Effects: " + ChatColor.AQUA + pplayer.getFixedEffectIds().size()
        };
        currentIconMeta.setLore(Arrays.asList(currentIconLore));
        currentIconMeta.setOwner(pplayer.getPlayer().getName());
        headIcon.setItemMeta(currentIconMeta);
        
        this.inventory.setItem(13, headIcon);
        
        // Manage Your Particles button
        GuiActionButton manageYourParticlesButton = new GuiActionButton(38, Material.BLAZE_POWDER, ChatColor.GREEN + "Manage Your Particles", new String[] { ChatColor.YELLOW + "Create, edit, and delete your particles" }, false, (isShiftClick) -> {
            GuiHandler.transition(new GuiInventoryManageParticles(pplayer));
        });
        this.actionButtons.add(manageYourParticlesButton);
        
        // Manage Your Groups button
        GuiActionButton manageYourGroupsButton = new GuiActionButton(40, Material.CHEST, ChatColor.GREEN + "Manage Your Groups", new String[] { ChatColor.YELLOW + "Create, delete, and load particle groups" }, false, (isShiftClick) -> {
            // transition to GuiInventoryManageGroups
        });
        this.actionButtons.add(manageYourGroupsButton);
        
        // Load Preset Groups
        GuiActionButton loadPresetGroups = new GuiActionButton(42, Material.ENDER_CHEST, ChatColor.GREEN + "Load Preset Groups", new String[] { ChatColor.YELLOW + "Load pre-existing particle groups" }, false, (isShiftClick) -> {
            // transition to GuiInventoryPresetGroups
        });
        this.actionButtons.add(loadPresetGroups);
        
        this.populate();
    }

}
