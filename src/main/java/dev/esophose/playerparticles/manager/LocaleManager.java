package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.hook.PlaceholderAPIHook;
import dev.esophose.playerparticles.locale.EnglishLocale;
import dev.esophose.playerparticles.locale.FrenchLocale;
import dev.esophose.playerparticles.locale.GermanLocale;
import dev.esophose.playerparticles.locale.Locale;
import dev.esophose.playerparticles.locale.PolishLocale;
import dev.esophose.playerparticles.locale.RussianLocale;
import dev.esophose.playerparticles.locale.SimplifiedChineseLocale;
import dev.esophose.playerparticles.locale.SpanishLocale;
import dev.esophose.playerparticles.locale.VietnameseLocale;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.HexUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LocaleManager extends Manager {

    private CommentedFileConfiguration locale;

    public LocaleManager(PlayerParticles playerParticles) {
        super(playerParticles);
    }

    /**
     * Creates a .lang file if one doesn't exist
     * Cross merges values between files into the .lang file, the .lang values take priority
     *
     * @param locale The Locale to register
     */
    private void registerLocale(Locale locale) {
        File file = new File(this.playerParticles.getDataFolder() + "/locale", locale.getLocaleName() + ".lang");
        boolean newFile = false;
        if (!file.exists()) {
            try {
                file.createNewFile();
                newFile = true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        boolean changed = false;
        CommentedFileConfiguration configuration = CommentedFileConfiguration.loadConfiguration(this.playerParticles, file);
        if (newFile) {
            configuration.addComments(locale.getLocaleName() + " translation by " + locale.getTranslatorName());
            Map<String, String> defaultLocaleStrings = locale.getDefaultLocaleStrings();
            for (String key : defaultLocaleStrings.keySet()) {
                String value = defaultLocaleStrings.get(key);
                if (key.startsWith("#")) {
                    configuration.addComments(value);
                } else {
                    configuration.set(key, value);
                }
            }
            changed = true;
        } else {
            Map<String, String> defaultLocaleStrings = locale.getDefaultLocaleStrings();
            for (String key : defaultLocaleStrings.keySet()) {
                if (key.startsWith("#"))
                    continue;

                String value = defaultLocaleStrings.get(key);
                if (!configuration.contains(key)) {
                    configuration.set(key, value);
                    changed = true;
                }
            }
        }

        if (changed)
            configuration.save();
    }

    @Override
    public void reload() {
        File localeDirectory = new File(this.playerParticles.getDataFolder(), "locale");
        if (!localeDirectory.exists())
            localeDirectory.mkdirs();

        this.registerLocale(new EnglishLocale());
        this.registerLocale(new FrenchLocale());
        this.registerLocale(new GermanLocale());
        this.registerLocale(new PolishLocale());
        this.registerLocale(new RussianLocale());
        this.registerLocale(new SimplifiedChineseLocale());
        this.registerLocale(new SpanishLocale());
        this.registerLocale(new VietnameseLocale());

        File targetLocaleFile = new File(this.playerParticles.getDataFolder() + "/locale", Setting.LOCALE.getString() + ".lang");
        if (!targetLocaleFile.exists()) {
            targetLocaleFile = new File(this.playerParticles.getDataFolder() + "/locale", "en_US.lang");
            this.playerParticles.getLogger().severe("File " + targetLocaleFile.getName() + " does not exist. Defaulting to en_US.lang");
        }

        this.locale = CommentedFileConfiguration.loadConfiguration(this.playerParticles, targetLocaleFile);
    }

    @Override
    public void disable() {

    }

    public String getLocaleMessage(String messageKey) {
        return this.getLocaleMessage(messageKey, StringPlaceholders.empty());
    }

    public String getLocaleMessage(String messageKey, StringPlaceholders stringPlaceholders) {
        String message = this.locale.getString(messageKey);
        if (message == null)
            return ChatColor.RED + "Missing message in locale file: " + messageKey;
        return HexUtils.colorify(stringPlaceholders.apply(message));
    }

    /**
     * Sends a message to a CommandSender with the prefix with placeholders applied
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        if (!Setting.MESSAGES_ENABLED.getBoolean())
            return;

        String prefix = "";
        if (Setting.USE_MESSAGE_PREFIX.getBoolean())
            prefix = this.getLocaleMessage("prefix");

        this.sendParsedMessage(sender, prefix + this.getLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a PPlayer with the prefix with placeholders applied
     *
     * @param pplayer The PPlayer to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendMessage(PPlayer pplayer, String messageKey, StringPlaceholders stringPlaceholders) {
        if (!Setting.MESSAGES_ENABLED.getBoolean())
            return;

        String prefix = "";
        if (Setting.USE_MESSAGE_PREFIX.getBoolean())
            prefix = this.getLocaleMessage("prefix");

        this.sendParsedMessage(pplayer.getUnderlyingExecutor(), prefix + this.getLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a CommandSender with the prefix
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendMessage(CommandSender sender, String messageKey) {
        this.sendMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a message to a PPlayer with the prefix
     *
     * @param pplayer The PPlayer to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendMessage(PPlayer pplayer, String messageKey) {
        this.sendMessage(pplayer, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a message to a CommandSender with placeholders applied
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendSimpleMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        if (!Setting.MESSAGES_ENABLED.getBoolean())
            return;

        this.sendParsedMessage(sender, this.getLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a PPlayer with placeholders applied
     *
     * @param pplayer The PPlayer to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendSimpleMessage(PPlayer pplayer, String messageKey, StringPlaceholders stringPlaceholders) {
        if (!Setting.MESSAGES_ENABLED.getBoolean())
            return;

        this.sendParsedMessage(pplayer.getUnderlyingExecutor(), this.getLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a CommandSender
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendSimpleMessage(CommandSender sender, String messageKey) {
        this.sendSimpleMessage(sender, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a message to a PPlayer
     *
     * @param pplayer The PPlayer to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendSimpleMessage(PPlayer pplayer, String messageKey) {
        this.sendSimpleMessage(pplayer, messageKey, StringPlaceholders.empty());
    }

    /**
     * Sends a custom message to a CommandSender
     *
     * @param sender The CommandSender to send to
     * @param message The message to send
     */
    public void sendCustomMessage(CommandSender sender, String message) {
        if (!Setting.MESSAGES_ENABLED.getBoolean())
            return;

        this.sendParsedMessage(sender, message);
    }

    /**
     * Sends a custom message to a PPlayer
     *
     * @param pplayer The PPlayer to send to
     * @param message The message to send
     */
    public void sendCustomMessage(PPlayer pplayer, String message) {
        this.sendCustomMessage(pplayer.getUnderlyingExecutor(), message);
    }

    /**
     * Replaces PlaceholderAPI placeholders if PlaceholderAPI is enabled
     *
     * @param sender The Player to replace with
     * @param message The message
     * @return A placeholder-replaced message
     */
    private String parsePlaceholders(CommandSender sender, String message) {
        if (sender instanceof Player)
            return PlaceholderAPIHook.applyPlaceholders((Player) sender, message);
        return message;
    }

    /**
     * Sends a message with placeholders and colors parsed to a CommandSender
     *
     * @param sender The sender to send the message to
     * @param message The message
     */
    private void sendParsedMessage(CommandSender sender, String message) {
        HexUtils.sendMessage(sender, this.parsePlaceholders(sender, message));
    }

}
