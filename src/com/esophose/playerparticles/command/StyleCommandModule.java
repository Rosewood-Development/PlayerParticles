package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;

public class StyleCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LangManager.sendMessage(pplayer, Lang.COMMAND_DESCRIPTION_STYLE);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<String>();
    }

    public String getName() {
        return "style";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_STYLE;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return true;
    }

}
