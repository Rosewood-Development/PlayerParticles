package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.PPlayer;

public class ReloadCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (PermissionManager.canReloadPlugin(pplayer.getPlayer())) {
            ((PlayerParticles)PlayerParticles.getPlugin()).reload(false);
            LangManager.sendMessage(pplayer, Lang.RELOAD_SUCCESS);
        } else {
            LangManager.sendMessage(pplayer, Lang.RELOAD_NO_PERMISSION);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<String>();
    }

    public String getName() {
        return "reload";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_RELOAD;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

}
