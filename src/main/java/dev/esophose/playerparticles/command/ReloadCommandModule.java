package dev.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;

public class ReloadCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (PlayerParticles.getInstance().getManager(PermissionManager.class).canReloadPlugin(pplayer.getMessageDestination())) {
            PlayerParticles.getInstance().reload();
            LangManager.sendMessage(pplayer, Lang.RELOAD_SUCCESS);
            PlayerParticles.getInstance().getLogger().info("Reloaded configuration.");
        } else {
            LangManager.sendMessage(pplayer, Lang.RELOAD_NO_PERMISSION);
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

    public boolean requiresEffects() {
        return false;
    }

    public boolean canConsoleExecute() {
        return true;
    }

}
