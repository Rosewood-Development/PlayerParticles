package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.Settings;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.util.StringUtil;

public class GUICommandModule implements CommandModule {

    private static final List<String> GUI_TYPES = Arrays.asList("presets", "groups", "particles");

    @Override
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
        if (!Settings.GUI_PRESETS_ONLY.get() && (Settings.GUI_REQUIRE_EFFECTS_AND_STYLES.get() && !hasEffectsAndStyles)) {
            if (openedFromGuiCommand) {
                localeManager.sendMessage(pplayer, "command-error-missing-effects-or-styles");
            } else {
                localeManager.sendMessage(pplayer, "command-error-unknown");
            }
            return;
        }

        if (args.length == 0) {
            guiManager.openDefault(pplayer);
            return;
        }

        String guiName = args[0].toLowerCase();
        if (GUI_TYPES.contains(guiName) && permissionManager.canOpenGui(pplayer, guiName)) {
            switch (guiName) {
                case "presets":
                    guiManager.openPresetGroups(pplayer);
                    return;
                case "groups":
                    guiManager.openGroups(pplayer);
                    return;
                case "particles":
                    guiManager.openParticles(pplayer);
                    return;
            }
        }

        guiManager.openDefault(pplayer);
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> possibilities = GUI_TYPES.stream()
                    .filter(x -> permissionManager.canOpenGui(pplayer, x))
                    .collect(Collectors.toList());
            StringUtil.copyPartialMatches(args[0], possibilities, completions);
        }
        return completions;
    }

    @Override
    public String getName() {
        return "gui";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-gui";
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
