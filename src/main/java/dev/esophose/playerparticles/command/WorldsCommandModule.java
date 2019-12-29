package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;

public class WorldsCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        if (permissionManager.getDisabledWorlds() == null || permissionManager.getDisabledWorlds().isEmpty()) {
            localeManager.sendMessage(pplayer, "disabled-worlds-none");
            return;
        }

        StringBuilder worlds = new StringBuilder();
        for (String s : permissionManager.getDisabledWorlds()) {
            worlds.append(s).append(", ");
        }
        if (worlds.length() > 2) worlds = new StringBuilder(worlds.substring(0, worlds.length() - 2));

        localeManager.sendCustomMessage(pplayer, localeManager.getLocaleMessage("disabled-worlds") + " " + worlds);
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
