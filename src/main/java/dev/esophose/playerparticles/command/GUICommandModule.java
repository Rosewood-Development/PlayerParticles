package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LangManager;
import dev.esophose.playerparticles.manager.LangManager.Lang;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.manager.SettingManager.Setting;
import dev.esophose.playerparticles.particles.PPlayer;

import java.util.ArrayList;
import java.util.List;

public class GUICommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        boolean byDefault = false;
        if (args.length > 0 && args[0].equals("_byDefault_")) {
            byDefault = true;
        }

        if (GuiManager.isGuiDisabled()) {
            if (byDefault) {
                LangManager.sendMessage(pplayer, Lang.COMMAND_ERROR_UNKNOWN);
            } else {
                LangManager.sendMessage(pplayer, Lang.GUI_DISABLED);
            }
            return;
        }

        if (!Setting.GUI_PRESETS_ONLY.getBoolean() && PlayerParticles.getInstance().getManager(PermissionManager.class).getEffectNamesUserHasPermissionFor(pplayer.getPlayer()).isEmpty()) {
            if (byDefault) {
                LangManager.sendMessage(pplayer, Lang.COMMAND_ERROR_NO_EFFECTS);
            } else {
                LangManager.sendMessage(pplayer, Lang.COMMAND_ERROR_UNKNOWN);
            }
            return;
        }

        GuiManager.openDefault(pplayer);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "gui";
    }

    public String getDescriptionKey() {
        return "command-description-gui";
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
