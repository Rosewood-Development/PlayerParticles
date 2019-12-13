package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;

public class ToggleCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        boolean canSee = pplayer.canSeeParticles();
        PlayerParticles.getInstance().getManager(DataManager.class).updateSettingParticlesHidden(pplayer.getUniqueId(), canSee);
        
        if (canSee) {
            localeManager.sendMessage(pplayer, "toggle-off");
        } else {
            localeManager.sendMessage(pplayer, "toggle-on");
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "toggle";
    }

    public String getDescriptionKey() {
        return "command-description-toggle";
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
