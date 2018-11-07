package com.esophose.playerparticles.gui;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.Dye;

import com.esophose.playerparticles.gui.GuiInventoryEditData.ColorData;

public class GuiActionButton {

    private int slot;
    private Material[] icons;
    private ColorData[] colors;
    private String name;
    private String[] lore;
    private GuiActionButtonClickCallback onClick;
    private int iconIndex;
    
    /**
     * Constructor for creating animated icons
     * 
     * @param slot The slot ID of the inventory
     * @param icons The Materials that this icon will cycle through
     * @param name The name that this icon will use
     * @param lore The lore of this icon
     * @param onClick The callback to execute when this button is clicked
     */
    public GuiActionButton(int slot, Material[] icons, String name, String[] lore, GuiActionButtonClickCallback onClick) {
        this.slot = slot;
        this.icons = icons;
        this.colors = null;
        this.name = name;
        this.lore = lore;
        this.onClick = onClick;
    }
    
    /**
     * Constructor for creating non-animated icons
     * 
     * @param slot The slot ID of the inventory
     * @param icons The Materials that this icon will use
     * @param name The name that this icon will use
     * @param lore The lore of this icon
     * @param onClick The callback to execute when this button is clicked
     */
    public GuiActionButton(int slot, Material icon, String name, String[] lore, GuiActionButtonClickCallback onClick) {
        this(slot, new Material[] { icon }, name, lore, onClick);
    }
    
    /**
     * Constructor for creating animated icons using dyes
     * 
     * @param slot The slot ID of the inventory
     * @param iconColors The ColorData that this icon will cycle through
     * @param name The name that this icon will use
     * @param lore The lore of this icon
     * @param onClick The callback to execute when this button is clicked
     */
    public GuiActionButton(int slot, ColorData[] iconColors, String name, String[] lore, GuiActionButtonClickCallback onClick) {
        this.slot = slot;
        this.icons = null;
        this.colors = iconColors;
        this.name = name;
        this.lore = lore;
        this.onClick = onClick;
    }
    
    /**
     * Constructor for creating non-animated icons using dyes
     * 
     * @param slot The slot ID of the inventory
     * @param iconColor The ColorData that this icon will use
     * @param name The name that this icon will use
     * @param lore The lore of this icon
     * @param onClick The callback to execute when this button is clicked
     */
    public GuiActionButton(int slot, ColorData iconColor, String name, String[] lore, GuiActionButtonClickCallback onClick) {
        this(slot, new ColorData[] { iconColor }, name, lore, onClick);
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
    @SuppressWarnings("deprecation")
    public ItemStack getIcon() {
        ItemStack itemStack;
        if (this.icons != null) {
            itemStack = new ItemStack(this.icons[this.iconIndex]);
        } else {
            if (this.colors[0].getMaterial() != null) { // Use Materials
                itemStack = new ItemStack(this.colors[this.iconIndex].getMaterial());
            } else { // Use Dyes
                itemStack = new Dye(this.colors[this.iconIndex].getDyeColor()).toItemStack(1);
            }
        }
        
        ItemMeta itemMeta = itemStack.getItemMeta();
        
        itemMeta.setDisplayName(this.name);
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
        this.iconIndex = (this.iconIndex + 1) % (this.icons != null ? this.icons.length : this.colors.length);
    }
    
    /**
     * Checks if this GuiActionButton has more than one icon/name that it can cycle through
     * 
     * @return If this GuiActionButton has more than one icon/name
     */
    public boolean isTickable() {
        return this.icons != null ? this.icons.length > 1 : this.colors.length > 1;
    }
    
    /**
     * Allows button click callbacks as parameters
     */
    @FunctionalInterface
    public static interface GuiActionButtonClickCallback {
        public void execute(GuiActionButton button, boolean isShiftClick);
    }

}
