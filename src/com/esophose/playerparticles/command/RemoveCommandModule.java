package com.esophose.playerparticles.command;

import java.util.List;

import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class RemoveCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {

    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
    }

    public String getName() {
        return "remove";
    }

    public Lang getDescription() {
        return Lang.REMOVE_COMMAND_DESCRIPTION;
    }

    public String getArguments() {
        return "<id>";
    }

    public boolean requiresEffects() {
        return true;
    }

}
