package dev.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleGroup;

public class ResetCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        int particleCount = pplayer.getActiveParticles().size();
        PlayerParticles.getInstance().getManager(DataManager.class).saveParticleGroup(pplayer.getUniqueId(), ParticleGroup.getDefaultGroup());
        LangManager.sendMessage(pplayer, Lang.RESET_SUCCESS, particleCount);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "reset";
    }

    public String getDescriptionKey() {
        return "command-description-reset";
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
