package dev.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import dev.esophose.playerparticles.PlayerParticles;
import org.bukkit.util.StringUtil;

import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;

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
            LangManager.sendMessage(pplayer, Lang.DATA_NO_ARGS);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        List<String> matches = new ArrayList<>();
        if (args.length <= 1) {
            if (args.length == 0) {
                matches = permissionManager.getEffectNamesUserHasPermissionFor(pplayer.getPlayer());
            } else {
                StringUtil.copyPartialMatches(args[0], permissionManager.getEffectNamesUserHasPermissionFor(pplayer.getPlayer()), matches);
            }
        }
        return matches;
    }

    public String getName() {
        return "data";
    }

    public String getDescriptionKey() {
        return "command-description-data";
    }

    public String getArguments() {
        return "<effect>";
    }

    public boolean requiresEffects() {
        return true;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
