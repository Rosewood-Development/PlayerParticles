package dev.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;

public class EffectsCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        Player p = pplayer.getPlayer();

        List<String> effectList = PermissionManager.getEffectNamesUserHasPermissionFor(p);
        if (effectList.isEmpty()) {
            LangManager.sendMessage(pplayer, Lang.EFFECT_LIST_EMPTY);
            return;
        }

        StringBuilder toSend = new StringBuilder();
        for (String name : effectList) {
            toSend.append(name).append(", ");
        }
        
        if (toSend.toString().endsWith(", ")) {
            toSend = new StringBuilder(toSend.substring(0, toSend.length() - 2));
        }

        LangManager.sendMessage(pplayer, Lang.EFFECT_LIST, toSend.toString());
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
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

    public boolean canConsoleExecute() {
        return false;
    }

}
