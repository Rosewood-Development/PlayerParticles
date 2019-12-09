package dev.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;

public class WorldsCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        if (permissionManager.getDisabledWorlds() == null || permissionManager.getDisabledWorlds().isEmpty()) {
            LangManager.sendMessage(pplayer, Lang.DISABLED_WORLDS_NONE);
            return;
        }

        StringBuilder worlds = new StringBuilder();
        for (String s : permissionManager.getDisabledWorlds()) {
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

    public String getDescriptionKey() {
        return "command-description-worlds";
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
