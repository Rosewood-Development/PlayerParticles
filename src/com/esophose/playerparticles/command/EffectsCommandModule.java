package com.esophose.playerparticles.command;

import java.util.List;

import org.bukkit.entity.Player;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;

public class EffectsCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        Player p = pplayer.getPlayer();

        List<String> effectList = PermissionManager.getEffectsUserHasPermissionFor(p);
        if (effectList.size() == 1) {
            LangManager.sendMessage(p, Lang.EFFECT_LIST, effectList.get(0));
            return;
        }

        String toSend = "";
        for (ParticleEffect effect : ParticleEffect.getSupportedEffects()) {
            if (PermissionManager.hasEffectPermission(p, effect)) {
                toSend += effect.getName() + ", ";
                continue;
            }
        }
        if (toSend.endsWith(", ")) {
            toSend = toSend.substring(0, toSend.length() - 2);
        }

        LangManager.sendMessage(p, Lang.EFFECT_LIST, toSend);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
    }

    public String getName() {
        return "effects";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_EFFECTS;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

}
