package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.bukkit.util.StringUtil;

public class GUICommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        this.onCommandExecute(pplayer, args, true);
    }

    public void onCommandExecute(PPlayer pplayer, String[] args, boolean openedFromGuiCommand) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        GuiManager guiManager = PlayerParticles.getInstance().getManager(GuiManager.class);

        if (!permissionManager.canOpenGui(pplayer)) {
            localeManager.sendMessage(pplayer, "gui-no-permission");
            return;
        }

        if (guiManager.isGuiDisabled()) {
            if (openedFromGuiCommand) {
                localeManager.sendMessage(pplayer, "gui-disabled");
            } else {
                localeManager.sendMessage(pplayer, "command-error-unknown");
            }
            return;
        }

        boolean hasEffectsAndStyles = !permissionManager.getEffectsUserHasPermissionFor(pplayer).isEmpty() && !permissionManager.getStylesUserHasPermissionFor(pplayer).isEmpty();
        if (!Setting.GUI_PRESETS_ONLY.getBoolean() && (Setting.GUI_REQUIRE_EFFECTS_AND_STYLES.getBoolean() && !hasEffectsAndStyles)) {
            if (openedFromGuiCommand) {
                localeManager.sendMessage(pplayer, "command-error-missing-effects-or-styles");
            } else {
                localeManager.sendMessage(pplayer, "command-error-unknown");
            }
            return;
        }

        if (args.length > 0 && args[0].equalsIgnoreCase("presets")) {
            guiManager.openPresetGroups(pplayer);
        } else {
            guiManager.openDefault(pplayer);
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> possibilities = Collections.singletonList("presets");
            StringUtil.copyPartialMatches(args[0], possibilities, completions);
        }
        return completions;
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
