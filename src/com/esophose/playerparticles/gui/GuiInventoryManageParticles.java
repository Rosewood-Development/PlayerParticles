package com.esophose.playerparticles.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.esophose.playerparticles.particles.PPlayer;

public class GuiInventoryManageParticles extends GuiInventory {

    public GuiInventoryManageParticles(PPlayer pplayer) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE));
        
        this.fillBorder(BorderColor.ORANGE);
        
        GuiActionButton button = new GuiActionButton(0, new Material[] { Material.BLAZE_POWDER, Material.BLAZE_ROD }, new String[] { "Test 2!", "Look at that!" } , new String[] { "To be continued..." }, true, (isShiftClick) -> {
            GuiHandler.transition(new GuiInventoryDefault(pplayer));
        });
        this.actionButtons.add(button);
        
        this.populate();
    }

}
