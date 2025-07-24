package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import org.bukkit.command.CommandSender;

public class LocaleManager extends AbstractLocaleManager {

    public LocaleManager(RosePlugin playerParticles) {
        super(playerParticles);
    }

    @Override
    protected void handleMessage(CommandSender sender, String message) {
        if (dev.esophose.playerparticles.config.Settings.MESSAGES_ENABLED.get())
            super.handleMessage(sender, message);
    }

    /**
     * Sends a message to a PPlayer with the prefix with placeholders applied
     *
     * @param pplayer            The PPlayer to send to
     * @param messageKey         The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendMessage(PPlayer pplayer, String messageKey, StringPlaceholders stringPlaceholders) {
        String prefix = this.getLocaleMessage("prefix");
        String message = this.getLocaleMessage(messageKey, stringPlaceholders);
        if (!message.isEmpty())
            this.sendUnparsedMessage(pplayer.getUnderlyingExecutor(), prefix + message);
    }

    /**
     * Sends a message to a PPlayer with the prefix
     *
     * @param pplayer    The PPlayer to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendMessage(PPlayer pplayer, String messageKey) {
        this.sendMessage(pplayer.getUnderlyingExecutor(), messageKey);
    }

    /**
     * Sends a message to a PPlayer without the prefix
     *
     * @param pplayer    The PPlayer to send to
     * @param messageKey The message to send
     */
    public void sendSimpleMessage(PPlayer pplayer, String messageKey) {
        this.sendSimpleMessage(pplayer.getUnderlyingExecutor(), messageKey);
    }

    /**
     * Sends a message to a PPlayer without the prefix with placeholders applied
     *
     * @param pplayer            The PPlayer to send to
     * @param messageKey         The message to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendSimpleMessage(PPlayer pplayer, String messageKey, StringPlaceholders stringPlaceholders) {
        this.sendSimpleMessage(pplayer.getUnderlyingExecutor(), messageKey, stringPlaceholders);
    }

}
