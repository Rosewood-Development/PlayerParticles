package com.esophose.playerparticles.gui.hook;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class PlayerChatHook extends BukkitRunnable implements Listener {

    private static Set<PlayerChatHookData> hooks;
    private static BukkitTask hookTask = null;

    /**
     * Initializes all the static values for this class
     */
    public static void setup() {
        hooks = new HashSet<>();
        if (hookTask != null)
            hookTask.cancel();
        hookTask = new PlayerChatHook().runTaskTimer(PlayerParticles.getPlugin(), 0, 20);
    }
    
    /**
     * Called when a player sends a message in chat
     * 
     * @param event The AsyncPlayerChatEvent
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        for (PlayerChatHookData hook : hooks) {
            if (hook.getPlayerUUID().equals(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
                hook.triggerCallback(event.getMessage());
                hooks.remove(hook);
                return;
            }
        }
    }
    
    /**
     * Ticked every second to decrease the seconds remaining on each hook
     */
    public void run() {
        Set<PlayerChatHookData> hooksToRemove = new HashSet<>();
        
        for (PlayerChatHookData hook : hooks) {
            hook.decrementHookLength();
            if (hook.timedOut()) {
                hook.triggerCallback(null);
                hooksToRemove.add(hook);
                continue;
            }
            
            Player player = Bukkit.getPlayer(hook.getPlayerUUID());
            if (player == null) {
                hooksToRemove.remove(hook);
            } else {
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(LangManager.getText(Lang.GUI_SAVE_GROUP_HOTBAR_MESSAGE, hook.getTimeRemaining())));
            }
        }
        
        for (PlayerChatHookData hookToRemove : hooksToRemove) 
            hooks.remove(hookToRemove);
    }
    
    /**
     * Adds a player chat hook
     * 
     * @param newHook The new hook to add
     */
    public static void addHook(PlayerChatHookData newHook) {
        for (PlayerChatHookData hook : hooks) {
            if (hook.getPlayerUUID().equals(newHook.getPlayerUUID())) {
                hooks.remove(hook);
                break;
            }
        }
        hooks.add(newHook);
    }

}
