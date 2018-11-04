package com.esophose.playerparticles.gui;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.util.ParticleUtils;

public abstract class GuiInventory {
    
    protected enum BorderColor {
        WHITE(DyeColor.WHITE, "WHITE_STAINED_GLASS_PANE"),
        ORANGE(DyeColor.ORANGE, "ORANGE_STAINED_GLASS_PANE"),
        RED(DyeColor.RED, "RED_STAINED_GLASS_PANE"),
        BROWN(DyeColor.BROWN, "BROWN_STAINED_GLASS_PANE"),
        GREEN(DyeColor.GREEN, "GREEN_STAINED_GLASS_PANE");
        
        private DyeColor dyeColor;
        private Material material;
        
        private BorderColor(DyeColor dyeColor, String materialName) {
            this.dyeColor = dyeColor;
            this.material = ParticleUtils.closestMatch(materialName);
        }
        
        @SuppressWarnings("deprecation")
        protected ItemStack getIcon() {
            ItemStack borderIcon;
            if (this.material != null) { // Use 1.13 materials
                borderIcon = new ItemStack(this.material, 1);
            } else { // Use < 1.13 dye colors
                borderIcon = new Dye(this.dyeColor).toItemStack(1);
            }
            
            ItemMeta meta = borderIcon.getItemMeta();
            meta.setDisplayName(" ");
            meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS);
            borderIcon.setItemMeta(meta);
            
            return borderIcon;
        }
    }
    
    protected static final int INVENTORY_SIZE = 54;
    protected PPlayer pplayer;
    protected Inventory inventory;
    protected List<GuiActionButton> actionButtons;
    
    public GuiInventory(PPlayer pplayer, Inventory inventory) {
        this.pplayer = pplayer;
        this.inventory = inventory;
        this.actionButtons = new ArrayList<GuiActionButton>();
    }
    
    /**
     * Gets the PPlayer this GuiInventory is managing
     * 
     * @return The PPlayer this GuiInventory is managing
     */
    public PPlayer getPPlayer() {
        return this.pplayer;
    }
    
    /**
     * Gets the Inventory this GuiInventory is managing
     * 
     * @return The Inventory this GuiInventory is managing
     */
    public Inventory getInventory() {
        return this.inventory;
    }
    
    /**
     * Fills the border of the inventory with a given color
     * 
     * @param borderColor The color of the border
     */
    protected void fillBorder(BorderColor borderColor) {
        ItemStack itemStack = borderColor.getIcon();
        
        // Top
        for (int i = 0; i < 9; i++)
            this.inventory.setItem(i, itemStack);
        
        // Bottom
        for (int i = INVENTORY_SIZE - 9; i < INVENTORY_SIZE; i++)
            this.inventory.setItem(i, itemStack);
        
        // Left
        for (int i = 0; i < INVENTORY_SIZE; i += 9)
            this.inventory.setItem(i, itemStack);
        
        // Right
        for (int i = 8; i < INVENTORY_SIZE; i += 9)
            this.inventory.setItem(i, itemStack);
    }
    
    /**
     * Populates the Inventory with the contents of actionButtons
     */
    protected void populate() {
        for (GuiActionButton button : actionButtons) {
            this.inventory.setItem(button.getSlot(), button.getIcon());
        }
    }
    
    /**
     * Ticked at an interval to animate GuiActionButton icons
     */
    public void onTick() {
        for (GuiActionButton button : this.actionButtons) {
            if (button.isTickable()) {
                button.onTick();
                this.inventory.setItem(button.getSlot(), button.getIcon());
            }
        }
    }
    
    /**
     * Handles clicks of GuiActionButtons
     * 
     * @param event The InventoryClickEvent triggered when the player clicks a GuiActionButton
     */
    public void onClick(InventoryClickEvent event) {
        int slot = event.getSlot();
        boolean isShiftClick = event.isShiftClick();
        
        for (GuiActionButton button : this.actionButtons) {
            if (button.getSlot() == slot) {
                button.handleClick(button, isShiftClick);
                break;
            }
        }
    }

}
