package com.esophose.playerparticles.command;

import java.util.List;

import org.bukkit.ChatColor;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class VersionCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LangManager.sendCustomMessage(pplayer, ChatColor.GOLD + "Running PlayerParticles v" + PlayerParticles.getPlugin().getDescription().getVersion());
        LangManager.sendCustomMessage(pplayer, ChatColor.GOLD + "Plugin created by: Esophose");
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
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

}
