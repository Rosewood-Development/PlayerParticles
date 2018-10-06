package com.esophose.playerparticles.command;

import java.util.List;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleGroup;

public class ResetCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        int particleCount = pplayer.getActiveParticles().size();
        DataManager.saveParticleGroup(pplayer.getUniqueId(), ParticleGroup.getDefaultGroup());
        LangManager.sendMessage(pplayer, Lang.RESET_SUCCESS, particleCount);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
    }

    public String getName() {
        return "reset";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_RESET;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

}
