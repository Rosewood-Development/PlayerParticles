package dev.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.particles.PPlayer;

public class VersionCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LangManager.sendCustomMessage(pplayer, ChatColor.YELLOW + "Running PlayerParticles " + ChatColor.AQUA + "v" + PlayerParticles.getInstance().getDescription().getVersion());
        LangManager.sendCustomMessage(pplayer, ChatColor.YELLOW + "Plugin created by: " + ChatColor.AQUA + "Esophose");
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "version";
    }

    public String getDescriptionKey() {
        return "command-description-version";
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
