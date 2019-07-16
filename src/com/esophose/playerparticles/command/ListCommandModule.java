package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticlePair;

public class ListCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        List<ParticlePair> particles = pplayer.getActiveParticles();
        particles.sort(Comparator.comparingInt(ParticlePair::getId));
        
        if (particles.isEmpty()) {
            LangManager.sendMessage(pplayer, Lang.LIST_NONE);
            return;
        }
        
        LangManager.sendMessage(pplayer, Lang.LIST_YOU_HAVE);
        for (ParticlePair particle : particles) {
            int id = particle.getId();
            String effect = particle.getEffect().getName();
            String style = particle.getStyle().getName();
            String data = particle.getDataString();
            LangManager.sendMessage(pplayer, Lang.LIST_OUTPUT, id, effect, style, data);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "list";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_LIST;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return true;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
