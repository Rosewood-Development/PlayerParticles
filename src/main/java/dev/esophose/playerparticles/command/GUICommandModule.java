package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;

public class GUICommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        boolean byDefault = false;
        if (args.length > 0 && args[0].equals("_byDefault_")) {
            byDefault = true;
        }

        if (guiManager.isGuiDisabled()) {
            if (byDefault) {
                localeManager.sendMessage(pplayer, "command-error-unknown");
            } else {
                localeManager.sendMessage(pplayer, "gui-disabled");
            }
            return;
        }

        if (!Setting.GUI_PRESETS_ONLY.getBoolean() && PlayerParticles.getInstance().getManager(PermissionManager.class).getEffectNamesUserHasPermissionFor(pplayer.getPlayer()).isEmpty()) {
            if (byDefault) {
                localeManager.sendMessage(pplayer, "command-error-no-effects");
            } else {
                localeManager.sendMessage(pplayer, "command-error-unknown");
            }
            return;
        }

        guiManager.openDefault(pplayer);
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

    public boolean requiresEffectsAndStyles() {
        return false;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
