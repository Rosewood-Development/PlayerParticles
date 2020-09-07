package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

public class GUICommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        this.onCommandExecute(pplayer, true);
    }

    public void onCommandExecute(PPlayer pplayer, boolean openedFromGuiCommand) {
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

        Bukkit.getScheduler().runTask(PlayerParticles.getInstance(), () -> guiManager.openDefault(pplayer));
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
