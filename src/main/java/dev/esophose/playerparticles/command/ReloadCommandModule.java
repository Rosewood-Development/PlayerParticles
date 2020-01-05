package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;

public class ReloadCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        if (PlayerParticles.getInstance().getManager(PermissionManager.class).canReloadPlugin(pplayer.getMessageDestination())) {
            PlayerParticles.getInstance().reload();
            localeManager.sendMessage(pplayer, "reload-success");
            PlayerParticles.getInstance().getLogger().info("Reloaded configuration.");
        } else {
            localeManager.sendMessage(pplayer, "reload-no-permission");
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "reload";
    }

    public String getDescriptionKey() {
        return "command-description-reload";
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffectsAndStyles() {
        return false;
    }

    public boolean canConsoleExecute() {
        return true;
    }

}
