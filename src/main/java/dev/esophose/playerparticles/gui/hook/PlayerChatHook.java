package dev.esophose.playerparticles.gui.hook;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.Settings;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.rosewood.rosegarden.utils.NMSUtil;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PlayerChatHook implements Listener {

    private static PlayerChatHook INSTANCE;

    private final Set<PlayerChatHookData> hooks;

    private PlayerChatHook() {
        this.hooks = Collections.synchronizedSet(new HashSet<>());
        PlayerParticles.getInstance().getScheduler().runTaskTimer(this::run, 0, 20);
        Bukkit.getPluginManager().registerEvents(this, PlayerParticles.getInstance());
    }

    public static PlayerChatHook getInstance() {
        if (INSTANCE == null)
            INSTANCE = new PlayerChatHook();
        return INSTANCE;
    }

    /**
     * Called when a player sends a message in chat
     *
     * @param event The AsyncPlayerChatEvent
     */
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        for (PlayerChatHookData hook : this.hooks) {
            if (hook.getPlayerUUID().equals(event.getPlayer().getUniqueId())) {
                event.setCancelled(true);
                this.hooks.remove(hook);
                hook.triggerCallback(event.getMessage());
                return;
            }
        }
    }

    /**
     * Ticked every second to decrease the seconds remaining on each hook
     */
    public void run() {
        Set<PlayerChatHookData> hooksToRemove = new HashSet<>();

        for (PlayerChatHookData hook : this.hooks) {
            if (hook.timedOut()) {
                hook.triggerCallback(null);
                hooksToRemove.add(hook);
                continue;
            }

            Player player = Bukkit.getPlayer(hook.getPlayerUUID());
            if (player == null) {
                hooksToRemove.remove(hook);
            } else {
                LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
                String message = localeManager.getLocaleMessage("gui-save-group-hotbar-message", StringPlaceholders.of("seconds", hook.getTimeRemaining()));

                if (NMSUtil.getVersionNumber() >= 11) {
                    switch (Settings.GUI_GROUP_CREATION_MESSAGE_DISPLAY_AREA.get().toUpperCase()) {
                        case "ACTION_BAR":
                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(localeManager.getLocaleMessage("gui-save-group-hotbar-message", StringPlaceholders.of("seconds", hook.getTimeRemaining()))));
                            break;
                        case "TITLE":
                            player.sendTitle("", message, 5, 40, 10);
                            break;
                        default:
                            if (hook.getMaxHookLength() == hook.getTimeRemaining())
                                player.sendMessage(message);
                            break;
                    }
                } else {
                    if (hook.getMaxHookLength() == hook.getTimeRemaining())
                        player.sendMessage(message);
                }
            }

            hook.decrementHookLength();
        }

        for (PlayerChatHookData hookToRemove : hooksToRemove)
            this.hooks.remove(hookToRemove);
    }

    /**
     * Adds a player chat hook
     *
     * @param newHook The new hook to add
     */
    public void addHook(PlayerChatHookData newHook) {
        for (PlayerChatHookData hook : this.hooks) {
            if (hook.getPlayerUUID().equals(newHook.getPlayerUUID())) {
                this.hooks.remove(hook);
                break;
            }
        }
        this.hooks.add(newHook);
    }

}
