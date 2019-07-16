package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class VersionCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LangManager.sendCustomMessage(pplayer, ChatColor.YELLOW + "Running PlayerParticles " + ChatColor.AQUA + "v" + PlayerParticles.getPlugin().getDescription().getVersion());
        LangManager.sendCustomMessage(pplayer, ChatColor.YELLOW + "Plugin created by: " + ChatColor.AQUA + "Esophose");
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "version";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_VERSION;
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
