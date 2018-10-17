package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.DataManager;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleGroup;
import com.esophose.playerparticles.particles.ParticlePair;

public class RemoveCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (args.length == 0) {
            LangManager.sendMessage(pplayer, Lang.REMOVE_NO_ARGS);
            return;
        }
        
        int id = -1;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            LangManager.sendMessage(pplayer, Lang.ID_INVALID);
            return;
        }
        
        if (id <= 0) {
            LangManager.sendMessage(pplayer, Lang.ID_INVALID);
            return;
        }
        
        boolean removed = false;
        ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : activeGroup.getParticles()) {
            if (particle.getId() == id) {
                activeGroup.getParticles().remove(particle);
                removed = true;
                break;
            }
        }
        
        if (!removed) {
            LangManager.sendMessage(pplayer, Lang.ID_UNKNOWN, id);
            return;
        }
        
        DataManager.saveParticleGroup(pplayer.getUniqueId(), activeGroup);
        LangManager.sendMessage(pplayer, Lang.REMOVE_SUCCESS, id);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> matches = new ArrayList<String>();
        List<String> ids = new ArrayList<String>();
        
        for (ParticlePair particles : pplayer.getActiveParticles())
            ids.add(String.valueOf(particles.getId()));
        
        if (args.length == 0) return ids;
        
        StringUtil.copyPartialMatches(args[0], ids, matches);
        return matches;
    }

    public String getName() {
        return "remove";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_REMOVE;
    }

    public String getArguments() {
        return "<id>";
    }

    public boolean requiresEffects() {
        return true;
    }

}
