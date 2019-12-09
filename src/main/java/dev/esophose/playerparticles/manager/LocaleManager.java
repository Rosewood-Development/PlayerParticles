package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.locale.EnglishLocale;
import dev.esophose.playerparticles.locale.FrenchLocale;
import dev.esophose.playerparticles.locale.GermanLocale;
import dev.esophose.playerparticles.locale.Locale;
import dev.esophose.playerparticles.locale.RussianLocale;
import dev.esophose.playerparticles.locale.VietnameseLocale;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

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

        CommentedFileConfiguration configuration = CommentedFileConfiguration.loadConfiguration(this.playerParticles, file);
        if (newFile)
            configuration.addComments(locale.getLocaleName() + " translation by " + locale.getTranslatorName());

        Map<String, String> defaultLocaleStrings = locale.getDefaultLocaleStrings();
        for (String key : defaultLocaleStrings.keySet()) {
            String value = defaultLocaleStrings.get(key);
            if (!configuration.contains(key))
                configuration.set(key, value);
        }

        configuration.save(true);
    }

    @Override
    public void reload() {
        if (!this.playerParticles.getDataFolder().exists())
            this.playerParticles.getDataFolder().mkdirs();

        this.registerLocale(new EnglishLocale());
        this.registerLocale(new FrenchLocale());
        this.registerLocale(new GermanLocale());
        this.registerLocale(new RussianLocale());
        this.registerLocale(new VietnameseLocale());

        File targetLocaleFile = new File(Setting.LOCALE.getString() + ".lang");
        if (!targetLocaleFile.exists()) {
            targetLocaleFile = new File("en_US.lang");
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
            return "null";
        return ChatColor.translateAlternateColorCodes('&', stringPlaceholders.apply(message));
    }

    /**
     * Sends a message to a CommandSender with the prefix with placeholders applied
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendPrefixedMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        sender.sendMessage(this.getLocaleMessage("prefix") + this.getLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a CommandSender with the prefix
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendPrefixedMessage(CommandSender sender, String messageKey) {
        this.sendPrefixedMessage(sender, messageKey, new StringPlaceholders());
    }

    /**
     * Sends a message to a CommandSender with placeholders applied
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     * @param stringPlaceholders The placeholders to apply
     */
    public void sendMessage(CommandSender sender, String messageKey, StringPlaceholders stringPlaceholders) {
        sender.sendMessage(this.getLocaleMessage(messageKey, stringPlaceholders));
    }

    /**
     * Sends a message to a CommandSender
     *
     * @param sender The CommandSender to send to
     * @param messageKey The message key of the Locale to send
     */
    public void sendMessage(CommandSender sender, String messageKey) {
        this.sendMessage(sender, messageKey, StringPlaceholders.empty());
    }

}
