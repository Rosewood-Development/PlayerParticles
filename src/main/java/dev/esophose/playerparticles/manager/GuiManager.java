package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.gui.GuiInventory;
import dev.esophose.playerparticles.gui.GuiInventoryDefault;
import dev.esophose.playerparticles.gui.GuiInventoryLoadPresetGroups;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.scheduler.BukkitTask;

public class GuiManager extends Manager implements Listener, Runnable {

    private List<GuiInventory> guiInventories = new ArrayList<>();
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

        for (GuiInventory inventory : this.guiInventories) {
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
            this.guiInventories.remove(inventory);
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;
        Player player = (Player)event.getWhoClicked();
        
        GuiInventory inventory = this.getGuiInventory(player);
        if (inventory == null) return;
        
        event.setCancelled(true);
        inventory.onClick(event);
    }

    /**
     * Gets if the GUI is disabled by the server owner or not
     * 
     * @return True if the GUI is disabled
     */
    public boolean isGuiDisabled() {
        return !Setting.GUI_ENABLED.getBoolean();
    }

    /**
     * Forcefully closes all open PlayerParticles GUIs
     * Used for when the plugin unloads so players can't take items from the GUI
     */
    public void forceCloseAllOpenGUIs() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (GuiInventory inventory : this.guiInventories) {
                if (inventory.getPPlayer().getUniqueId().equals(player.getUniqueId()) && inventory.getInventory().equals(player.getOpenInventory().getTopInventory())) {
                    player.closeInventory();
                    break;
                }
            }
        }
        this.guiInventories.clear();
    }
    
    /**
     * Opens the default GUI screen for a player
     * 
     * @param pplayer The PPlayer to open the GUI screen for
     */
    public void openDefault(PPlayer pplayer) {
        this.removeGuiInventory(pplayer);
        
        GuiInventory inventoryToOpen;
        if (!Setting.GUI_PRESETS_ONLY.getBoolean()) {
            inventoryToOpen = new GuiInventoryDefault(pplayer);
        } else {
            inventoryToOpen = new GuiInventoryLoadPresetGroups(pplayer, true);
        }

        this.guiInventories.add(inventoryToOpen);
        pplayer.getPlayer().openInventory(inventoryToOpen.getInventory());
    }
    
    /**
     * Changes the player's inventory to another one
     * 
     * @param nextInventory The GuiInventory to transition to
     */
    public void transition(GuiInventory nextInventory) {
        this.removeGuiInventory(nextInventory.getPPlayer());
        this.guiInventories.add(nextInventory);
        nextInventory.getPPlayer().getPlayer().openInventory(nextInventory.getInventory());
    }
    
    /**
     * Gets a GuiInventory by Player
     * 
     * @param player The Player
     * @return The GuiInventory belonging to the Player, if any
     */
    private GuiInventory getGuiInventory(Player player) {
        for (GuiInventory inventory : this.guiInventories)
            if (inventory.getPPlayer().getUniqueId().equals(player.getUniqueId()))
                return inventory;
        return null;
    }
    
    /**
     * Removes a GuiInventory from guiInventories by a PPlayer
     * 
     * @param pplayer The PPlayer who owns the GuiInventory
     */
    private void removeGuiInventory(PPlayer pplayer) {
        for (GuiInventory inventory : this.guiInventories) {
            if (inventory.getPPlayer().getUniqueId().equals(pplayer.getUniqueId())) {
                this.guiInventories.remove(inventory);
                break;
            }
        }
    }

}
