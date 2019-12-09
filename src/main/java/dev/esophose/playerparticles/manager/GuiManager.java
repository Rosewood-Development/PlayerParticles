package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.gui.GuiInventory;
import dev.esophose.playerparticles.gui.GuiInventoryDefault;
import dev.esophose.playerparticles.gui.GuiInventoryLoadPresetGroups;
import dev.esophose.playerparticles.manager.SettingManager.Setting;
import dev.esophose.playerparticles.particles.PPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitTask;

import java.util.ArrayList;
import java.util.List;

// TODO: Remove static guiInventories and subsequent methods
public class GuiManager extends Manager implements Listener, Runnable {

    private static List<GuiInventory> guiInventories = new ArrayList<>();
    private BukkitTask guiTask = null;

    public GuiManager(PlayerParticles playerParticles) {
        super(playerParticles);

        Bukkit.getPluginManager().registerEvents(this, this.playerParticles);
    }

    @Override
    public void reload() {
        if (this.guiTask != null)
            this.guiTask.cancel();
        this.guiTask = Bukkit.getScheduler().runTaskTimer(this.playerParticles, this, 0, 10);
    }

    @Override
    public void disable() {
        this.forceCloseAllOpenGUIs();
    }

    /**
     * Ticks GuiInventories
     * Removes entries from playerGuiInventories if the player no longer has the inventory open or is offline
     */
    public void run() {
        List<GuiInventory> toRemoveList = new ArrayList<>();

        for (GuiInventory inventory : guiInventories) {
            PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(inventory.getPPlayer().getUniqueId());
            if (pplayer == null) {
                toRemoveList.add(inventory);
                continue;
            }

            Player player = Bukkit.getPlayer(inventory.getPPlayer().getUniqueId());
            if (player == null) {
                toRemoveList.add(inventory);
                continue;
            }
            
            if (!inventory.getInventory().equals(player.getOpenInventory().getTopInventory())) {
                toRemoveList.add(inventory);
                continue;
            }
            
            inventory.onTick();
        }

        for (GuiInventory inventory : toRemoveList)
            guiInventories.remove(inventory);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player)event.getWhoClicked();
        
        GuiInventory inventory = getGuiInventory(player);
        if (inventory == null) return;
        
        event.setCancelled(true);
        inventory.onClick(event);
    }

    /**
     * Gets if the GUI is disabled by the server owner or not
     * 
     * @return True if the GUI is disabled
     */
    public static boolean isGuiDisabled() {
        return !Setting.GUI_ENABLED.getBoolean();
    }

    /**
     * Forcefully closes all open PlayerParticles GUIs
     * Used for when the plugin unloads so players can't take items from the GUI
     */
    public void forceCloseAllOpenGUIs() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (GuiInventory inventory : guiInventories) {
                if (inventory.getPPlayer().getUniqueId().equals(player.getUniqueId()) && inventory.getInventory().equals(player.getOpenInventory().getTopInventory())) {
                    player.closeInventory();
                    break;
                }
            }
        }
        guiInventories.clear();
    }
    
    /**
     * Opens the default GUI screen for a player
     * 
     * @param pplayer The PPlayer to open the GUI screen for
     */
    public static void openDefault(PPlayer pplayer) {
        removeGuiInventory(pplayer);
        
        GuiInventory inventoryToOpen;
        if (!Setting.GUI_PRESETS_ONLY.getBoolean()) {
            inventoryToOpen = new GuiInventoryDefault(pplayer);
        } else {
            inventoryToOpen = new GuiInventoryLoadPresetGroups(pplayer, true);
        }
        
        guiInventories.add(inventoryToOpen);
        pplayer.getPlayer().openInventory(inventoryToOpen.getInventory());
    }
    
    /**
     * Changes the player's inventory to another one
     * 
     * @param nextInventory The GuiInventory to transition to
     */
    public static void transition(GuiInventory nextInventory) {
        removeGuiInventory(nextInventory.getPPlayer());
        guiInventories.add(nextInventory);
        nextInventory.getPPlayer().getPlayer().openInventory(nextInventory.getInventory());
    }
    
    /**
     * Gets a GuiInventory by Player
     * 
     * @param player The Player
     * @return The GuiInventory belonging to the Player, if any
     */
    private static GuiInventory getGuiInventory(Player player) {
        for (GuiInventory inventory : guiInventories)
            if (inventory.getPPlayer().getUniqueId().equals(player.getUniqueId()))
                return inventory;
        return null;
    }
    
    /**
     * Removes a GuiInventory from guiInventories by a PPlayer
     * 
     * @param pplayer The PPlayer who owns the GuiInventory
     */
    private static void removeGuiInventory(PPlayer pplayer) {
        for (GuiInventory inventory : guiInventories) {
            if (inventory.getPPlayer().getUniqueId().equals(pplayer.getUniqueId())) {
                guiInventories.remove(inventory);
                break;
            }
        }
    }

}
