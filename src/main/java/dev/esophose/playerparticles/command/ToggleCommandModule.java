package dev.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.particles.PPlayer;

public class ToggleCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        boolean canSee = pplayer.canSeeParticles();
        DataManager.updateSettingParticlesHidden(pplayer.getUniqueId(), canSee);
        
        if (canSee) {
            LangManager.sendMessage(pplayer, Lang.TOGGLE_OFF);
        } else {
            LangManager.sendMessage(pplayer, Lang.TOGGLE_ON);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "toggle";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_TOGGLE;
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
