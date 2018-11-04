package com.esophose.playerparticles.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class GuiActionButton {

    private int slot;
    private Material[] icons;
    private String[] names;
    private String[] lore;
    private GuiActionButtonClickCallback onClick;
    private int iconIndex;
    
    public GuiActionButton(int slot, Material[] icons, String[] names, String[] lore, GuiActionButtonClickCallback onClick) {
        this.slot = slot;
        this.icons = icons;
        this.names = names;
        this.lore = lore;
        this.onClick = onClick;
        
        if (icons.length != names.length)
            throw new IllegalArgumentException("icons and names must have the same length!");
    }
    
    public GuiActionButton(int slot, Material icon, String name, String[] lore, GuiActionButtonClickCallback onClick) {
        this(slot, new Material[] { icon }, new String[] { name }, lore, onClick);
    }
    
    /**
     * Gets the slot this GuiActionButton occupies in the GuiInventory
     * 
     * @return The slot this GuiActionButton occupies in the GuiInventory
     */
    public int getSlot() {
        return this.slot;
    }
    
    /**
     * Gets the ItemStack icon that goes into the GUI
     * 
     * @return The icon ItemStack for the GUI
     */
    public ItemStack getIcon() {
        ItemStack itemStack = new ItemStack(icons[this.iconIndex]);
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(this.names[this.iconIndex]);
        itemMeta.setLore(Arrays.asList(this.lore));
        itemMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
        
        itemStack.setItemMeta(itemMeta);
        
        return itemStack;
    }
    
    /**
     * Executes the onClick callback passed in the constructor
     * 
     * @param isShiftClick If the player was holding shift when they clicked
     */
    public void handleClick(GuiActionButton button, boolean isShiftClick) {
        if (this.onClick != null)
            this.onClick.execute(this, isShiftClick);
    }
    
    /**
     * Ticked at an interval to allow the icon/name to update
     */
    public void onTick() {
        this.iconIndex = (this.iconIndex + 1) % this.names.length;
    }
    
    /**
     * Checks if this GuiActionButton has more than one icon/name that it can cycle through
     * 
     * @return If this GuiActionButton has more than one icon/name
     */
    public boolean isTickable() {
        return this.icons.length > 1;
    }
    
    /**
     * Allows button click callbacks as parameters
     */
    @FunctionalInterface
    public static interface GuiActionButtonClickCallback {
        public void execute(GuiActionButton button, boolean isShiftClick);
    }

}
