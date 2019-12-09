package dev.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import dev.esophose.playerparticles.PlayerParticles;
import org.bukkit.entity.Player;

import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;

public class StylesCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        Player p = pplayer.getPlayer();

        List<String> styleNames = PlayerParticles.getInstance().getManager(PermissionManager.class).getStyleNamesUserHasPermissionFor(p);
        StringBuilder toSend = new StringBuilder();
        for (String name : styleNames) {
            toSend.append(name).append(", ");
        }
        
        if (toSend.toString().endsWith(", ")) {
            toSend = new StringBuilder(toSend.substring(0, toSend.length() - 2));
        }

        LangManager.sendMessage(pplayer, Lang.STYLE_LIST, toSend.toString());
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "styles";
    }

    public String getDescriptionKey() {
        return "command-description-styles";
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
