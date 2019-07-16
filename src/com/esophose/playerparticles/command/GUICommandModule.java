package com.esophose.playerparticles.command;

import java.util.ArrayList;
import java.util.List;

import com.esophose.playerparticles.gui.GuiHandler;
import com.esophose.playerparticles.manager.LangManager;
import com.esophose.playerparticles.manager.LangManager.Lang;
import com.esophose.playerparticles.manager.ParticleGroupPresetManager;
import com.esophose.playerparticles.manager.SettingManager.PSetting;
import com.esophose.playerparticles.manager.PermissionManager;
import com.esophose.playerparticles.particles.PPlayer;

public class GUICommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        boolean byDefault = false;
        if (args.length > 0 && args[0].equals("_byDefault_")) {
            byDefault = true;
        }

        if (GuiHandler.isGuiDisabled()) {
            if (byDefault) {
                LangManager.sendMessage(pplayer, Lang.COMMAND_ERROR_UNKNOWN);
            } else {
                LangManager.sendMessage(pplayer, Lang.GUI_DISABLED);
            }
            return;
        }
        
        if (PSetting.GUI_PRESETS_ONLY.getBoolean() && ParticleGroupPresetManager.getPresetGroupsForPlayer(pplayer.getPlayer()).isEmpty()) {
            return;
        } else if (PermissionManager.getEffectNamesUserHasPermissionFor(pplayer.getPlayer()).isEmpty()) {
            if (byDefault) {
                LangManager.sendMessage(pplayer, Lang.COMMAND_ERROR_UNKNOWN);
            } else {
                LangManager.sendMessage(pplayer, Lang.COMMAND_ERROR_NO_EFFECTS);
            }
            return;
        }

        GuiHandler.openDefault(pplayer);
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    public String getName() {
        return "gui";
    }

    public Lang getDescription() {
        return Lang.COMMAND_DESCRIPTION_GUI;
    }

    public String getArguments() {
        return "";
    }

    public boolean requiresEffects() {
        return true;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
