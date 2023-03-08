package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.locale.EnglishLocale;
import dev.esophose.playerparticles.locale.FrenchLocale;
import dev.esophose.playerparticles.locale.GermanLocale;
import dev.esophose.playerparticles.locale.PolishLocale;
import dev.esophose.playerparticles.locale.RussianLocale;
import dev.esophose.playerparticles.locale.SimplifiedChineseLocale;
import dev.esophose.playerparticles.locale.SpanishLocale;
import dev.esophose.playerparticles.locale.VietnameseLocale;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.rosewood.rosegarden.RosePlugin;
import dev.rosewood.rosegarden.locale.Locale;
import dev.rosewood.rosegarden.manager.AbstractLocaleManager;
import dev.rosewood.rosegarden.utils.StringPlaceholders;

import java.util.Arrays;
import java.util.List;

public class LocaleManager extends AbstractLocaleManager {

    public LocaleManager(RosePlugin playerParticles) {
        super(playerParticles);
    }

    /**
     * Sends a message to a PPlayer with the prefix with placeholders applied
     *
     * @param pplayer            The PPlayer to send to
     * @param messageKey         The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendMessage(PPlayer pplayer, String messageKey, StringPlaceholders stringPlaceholders) {
        if (Setting.MESSAGES_ENABLED.getBoolean())
            this.sendMessage(pplayer.getUnderlyingExecutor(), messageKey, stringPlaceholders);
    }

    /**
     * Sends a message to a PPlayer with the prefix
     *
     * @param pplayer    The PPlayer to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendMessage(PPlayer pplayer, String messageKey) {
        if (Setting.MESSAGES_ENABLED.getBoolean())
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

    @Override
    public List<Locale> getLocales() {
        return Arrays.asList(
                new EnglishLocale(),
                new FrenchLocale(),
                new GermanLocale(),
                new PolishLocale(),
                new RussianLocale(),
                new SimplifiedChineseLocale(),
                new SpanishLocale(),
                new VietnameseLocale()
        );
    }


}
