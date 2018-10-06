package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.util.StringUtil;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;

public class DataCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (args.length > 0) {
            ParticleEffect effect = ParticleEffect.fromName(args[0]);
            if (effect == null) {
                LangManager.sendMessage(pplayer, Lang.EFFECT_INVALID, args[0]);
                return;
            }

            if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                if (effect == ParticleEffect.NOTE) {
                    LangManager.sendMessage(pplayer, Lang.DATA_USAGE_NOTE, effect.getName());
                } else {
                    LangManager.sendMessage(pplayer, Lang.DATA_USAGE_COLOR, effect.getName());
                }
            } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                if (effect == ParticleEffect.ITEM) {
                    LangManager.sendMessage(pplayer, Lang.DATA_USAGE_ITEM, effect.getName());
                } else {
                    LangManager.sendMessage(pplayer, Lang.DATA_USAGE_BLOCK, effect.getName());
                }
            } else {
                LangManager.sendMessage(pplayer, Lang.DATA_USAGE_NONE, effect.getName());
            }
        } else {
            LangManager.sendMessage(pplayer, Lang.COMMAND_DATA_NO_ARGS);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> matches = new ArrayList<String>();
        if (args.length <= 1) {
            if (args.length == 0) matches = PermissionManager.getEffectsUserHasPermissionFor(pplayer.getPlayer());
            else StringUtil.copyPartialMatches(args[0], PermissionManager.getEffectsUserHasPermissionFor(pplayer.getPlayer()), matches);
        }
        return matches;
    }

    public String getName() {
        return "data";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_DATA;
    }

    public String getArguments() {
        return "<effect>";
    }

    public boolean requiresEffects() {
        return true;
    }

}
