package com.esophose.playerparticles.command;

import java.util.List;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;

public class DataCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (args.length > 0) {
            ParticleEffect effect = ParticleEffect.fromName(args[0]);

            if ((!effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA) && !effect.hasProperty(ParticleProperty.COLORABLE)) || args.length == 0) {
                if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                    if (effect == ParticleEffect.NOTE) {
                        LangManager.sendMessage(pplayer, Lang.DATA_USAGE, "note");
                        LangManager.sendCustomMessage(pplayer, LangManager.getText(Lang.USAGE) + " " + LangManager.getText(Lang.NOTE_DATA_USAGE));
                    } else {
                        LangManager.sendMessage(pplayer, Lang.DATA_USAGE, "color");
                        LangManager.sendCustomMessage(pplayer, LangManager.getText(Lang.USAGE) + " " + LangManager.getText(Lang.COLOR_DATA_USAGE));
                    }
                } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                    if (effect == ParticleEffect.ITEM) {
                        LangManager.sendMessage(pplayer, Lang.DATA_USAGE, "item");
                        LangManager.sendCustomMessage(pplayer, LangManager.getText(Lang.USAGE) + " " + LangManager.getText(Lang.ITEM_DATA_USAGE));
                    } else {
                        LangManager.sendMessage(pplayer, Lang.DATA_USAGE, "block");
                        LangManager.sendCustomMessage(pplayer, LangManager.getText(Lang.USAGE) + " " + LangManager.getText(Lang.BLOCK_DATA_USAGE));
                    }
                } else {
                    LangManager.sendMessage(pplayer, Lang.NO_DATA_USAGE);
                }
            }
        } else {
            LangManager.sendMessage(pplayer, Lang.INVALID_EFFECT);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
    }

    public String getName() {
        return "data";
    }

    public Lang getDescription() {
        return Lang.DATA_COMMAND_DESCRIPTION;
    }

    public String getArguments() {
        return "<effect>";
    }

    public boolean requiresEffects() {
        return true;
    }

}
