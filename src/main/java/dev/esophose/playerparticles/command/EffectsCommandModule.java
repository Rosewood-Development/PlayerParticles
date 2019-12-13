package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.Player;

public class EffectsCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        Player p = pplayer.getPlayer();

        List<String> effectList = PlayerParticles.getInstance().getManager(PermissionManager.class).getEffectNamesUserHasPermissionFor(p);
        if (effectList.isEmpty()) {
            localeManager.sendMessage(pplayer, "effect-list-empty");
            return;
        }

        StringBuilder toSend = new StringBuilder();
        for (String name : effectList) {
            toSend.append(name).append(", ");
        }
        
        if (toSend.toString().endsWith(", ")) {
            toSend = new StringBuilder(toSend.substring(0, toSend.length() - 2));
        }

        localeManager.sendMessage(pplayer, "effect-list", StringPlaceholders.single("effects", toSend.toString()));
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "effects";
    }

    public String getDescriptionKey() {
        return "command-description-effects";
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
