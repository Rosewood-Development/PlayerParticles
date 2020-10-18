package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;

public class StylesCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        List<String> styleNames = PlayerParticles.getInstance().getManager(PermissionManager.class).getStyleNamesUserHasPermissionFor(pplayer);
        StringBuilder toSend = new StringBuilder();
        for (String name : styleNames) {
            toSend.append(name).append(", ");
        }
        
        if (toSend.toString().endsWith(", ")) {
            toSend = new StringBuilder(toSend.substring(0, toSend.length() - 2));
        }

        PlayerParticles.getInstance().getManager(LocaleManager.class).sendMessage(pplayer, "style-list", StringPlaceholders.single("styles", toSend.toString()));
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "styles";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-styles";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public boolean requiresEffectsAndStyles() {
        return false;
    }

    @Override
    public boolean canConsoleExecute() {
        return false;
    }

}
