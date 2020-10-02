package dev.esophose.playerparticles.gui;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.NMSUtil;
import dev.esophose.playerparticles.util.ParticleUtils;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public abstract class GuiInventory {

    public enum BorderColor {
        WHITE(0, "WHITE_STAINED_GLASS_PANE"),
        ORANGE(1, "ORANGE_STAINED_GLASS_PANE"),
        MAGENTA(2, "MAGENTA_STAINED_GLASS_PANE"),
        LIGHT_BLUE(3, "LIGHT_BLUE_STAINED_GLASS_PANE"),
        YELLOW(4, "YELLOW_STAINED_GLASS_PANE"),
        LIME(5, "LIME_STAINED_GLASS_PANE"),
        PINK(6, "PINK_STAINED_GLASS_PANE"),
        GRAY(7, "GRAY_STAINED_GLASS_PANE"),
        LIGHT_GRAY(8, "LIGHT_GRAY_STAINED_GLASS_PANE"),
        CYAN(9, "CYAN_STAINED_GLASS_PANE"),
        PURPLE(10, "PURPLE_STAINED_GLASS_PANE"),
        BLUE(11, "BLUE_STAINED_GLASS_PANE"),
        BROWN(12, "BROWN_STAINED_GLASS_PANE"),
        GREEN(13, "GREEN_STAINED_GLASS_PANE"),
        RED(14, "RED_STAINED_GLASS_PANE"),
        BLACK(15, "BLACK_STAINED_GLASS_PANE");
        
        private short data;
        private Material material;
        
        BorderColor(int data, String materialName) {
            this.data = (short)data;
            this.material = ParticleUtils.closestMatch(materialName);
        }
        
        @SuppressWarnings("deprecation")
        protected ItemStack getIcon() {
            ItemStack borderIcon;
            if (this.material != null) { // Use 1.13 materials
                borderIcon = new ItemStack(this.material, 1);
            } else { // Use < 1.13 data values
                borderIcon = new ItemStack(ParticleUtils.closestMatch("STAINED_GLASS_PANE"), 1, this.data);
            }
            
            ItemMeta meta = borderIcon.getItemMeta();
            if (meta != null) {
                meta.setDisplayName(" ");
                if (NMSUtil.getVersionNumber() > 7)
                    meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS);
                borderIcon.setItemMeta(meta);
            }
            
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
        this.actionButtons = new ArrayList<>();
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
        for (GuiActionButton button : this.actionButtons) {
            this.inventory.setItem(button.getSlot(), button.getIcon(this.pplayer));
        }
    }
    
    /**
     * Ticked at an interval to animate GuiActionButton icons
     */
    public void onTick() {
        for (GuiActionButton button : this.actionButtons) {
            if (button.isTickable()) {
                button.onTick();
                this.inventory.setItem(button.getSlot(), button.getIcon(this.pplayer));
            }
        }
    }
    
    /**
     * Handles clicks of GuiActionButtons
     * 
     * @param event The InventoryClickEvent triggered when the player clicks in a GuiInventory
     */
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || !event.getClickedInventory().equals(this.inventory)) return;
        
        int slot = event.getSlot();
        boolean isShiftClick = event.isShiftClick();
        
        for (GuiActionButton button : this.actionButtons) {
            if (button.getSlot() == slot) {
                button.handleClick(isShiftClick);
                if (Setting.GUI_BUTTON_SOUND.getBoolean() && event.getWhoClicked() instanceof Player) {
                    Player player = (Player) event.getWhoClicked();
                    if (NMSUtil.getVersionNumber() > 8) {
                        player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, 1);
                    } else {
                        player.playSound(player.getLocation(), Sound.valueOf("CLICK"), 0.5f, 1);
                    }
                }
                break;
            }
        }
    }

    public void close() {
        Bukkit.getScheduler().runTask(PlayerParticles.getInstance(), () -> this.pplayer.getPlayer().closeInventory());
    }

}
