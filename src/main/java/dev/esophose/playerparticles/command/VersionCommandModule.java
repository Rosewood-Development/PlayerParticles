package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;
import net.md_5.bungee.api.ChatColor;

public class VersionCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        localeManager.sendCustomMessage(pplayer, ChatColor.YELLOW + "Running PlayerParticles " + ChatColor.AQUA + "v" + PlayerParticles.getInstance().getDescription().getVersion());
        localeManager.sendCustomMessage(pplayer, ChatColor.YELLOW + "Plugin created by: " + ChatColor.AQUA + "Esophose");
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "version";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-version";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public boolean requiresEffectsAndStyles() {
        return false;
    }

    @Override
    public boolean canConsoleExecute() {
        return true;
    }

}
