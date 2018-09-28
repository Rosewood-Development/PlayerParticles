package com.esophose.playerparticles.command;

import java.util.List;

import org.bukkit.entity.Player;

import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.particles.PPlayer;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;

public class StylesCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        Player p = pplayer.getPlayer();

        if (PermissionManager.getStylesUserHasPermissionFor(p).size() == 1) {
            LangManager.sendMessage(pplayer, Lang.NO_STYLES);
            return;
        }

        String toSend = Lang.USE.get() + " ";
        for (ParticleStyle style : ParticleStyleManager.getStyles()) {
            if (PermissionManager.hasStylePermission(p, style)) {
                toSend += style.getName();
                toSend += ", ";
            }
        }
        if (toSend.endsWith(", ")) {
            toSend = toSend.substring(0, toSend.length() - 2);
        }

        LangManager.sendCustomMessage(p, toSend);
        LangManager.sendCustomMessage(p, Lang.USAGE.get() + " " + Lang.STYLE_USAGE.get());
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return null;
    }

    public String getName() {
        return "styles";
    }

    public String getDescription() {
        return Lang.STYLES_COMMAND_DESCRIPTION.get();
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return false;
    }

}
