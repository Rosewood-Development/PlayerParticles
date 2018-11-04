package com.esophose.playerparticles.gui;

import org.bukkit.Bukkit;
import org.bukkit.Material;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class GuiInventoryManageParticles extends GuiInventory {

    public GuiInventoryManageParticles(PPlayer pplayer) {
        super(pplayer, Bukkit.createInventory(pplayer.getPlayer(), INVENTORY_SIZE, LangManager.getText(Lang.GUI_MANAGE_YOUR_PARTICLES)));
        
        this.fillBorder(BorderColor.ORANGE);
        
        GuiActionButton testButton = new GuiActionButton(0, new Material[] { Material.BLAZE_POWDER, Material.BLAZE_ROD }, new String[] { "Test 2!", "Look at that!" } , new String[] { "To be continued..." }, (button, isShiftClick) -> {
            GuiHandler.transition(new GuiInventoryDefault(pplayer));
        });
        this.actionButtons.add(testButton);
        
        this.populate();
    }

}
