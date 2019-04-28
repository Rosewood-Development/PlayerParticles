package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.PPlayer;

public class WorldsCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (PermissionManager.getDisabledWorlds() == null || PermissionManager.getDisabledWorlds().isEmpty()) {
            LangManager.sendMessage(pplayer, Lang.DISABLED_WORLDS_NONE);
            return;
        }

        StringBuilder worlds = new StringBuilder();
        for (String s : PermissionManager.getDisabledWorlds()) {
            worlds.append(s).append(", ");
        }
        if (worlds.length() > 2) worlds = new StringBuilder(worlds.substring(0, worlds.length() - 2));

        LangManager.sendCustomMessage(pplayer, LangManager.getText(Lang.DISABLED_WORLDS) + " " + worlds);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "worlds";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_WORLDS;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

}
