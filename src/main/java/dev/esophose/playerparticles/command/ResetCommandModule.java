package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class ResetCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        boolean isConsole = pplayer.getPlayer() == null;
        if (isConsole && args.length == 0) {
            localeManager.sendCustomMessage(Bukkit.getConsoleSender(), "&cOnly players can use this command. Did you mean to use '/pp reset [other]'?");
            return;
        }

        if (args.length == 0 || !PlayerParticles.getInstance().getManager(PermissionManager.class).canResetOthers(pplayer)) {
            Integer particleCount = PlayerParticlesAPI.getInstance().resetActivePlayerParticles(pplayer.getPlayer());
            if (particleCount != null)
                localeManager.sendMessage(pplayer, "reset-success", StringPlaceholders.single("amount", particleCount));
        } else {
            PlayerParticlesAPI.getInstance().resetActivePlayerParticles(args[0], success -> {
                if (success) {
                    localeManager.sendMessage(pplayer, "reset-others-success", StringPlaceholders.single("other", args[0]));
                } else {
                    localeManager.sendMessage(pplayer, "reset-others-none", StringPlaceholders.single("other", args[0]));
                }
            });
        }
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        if (args.length == 1 && PlayerParticles.getInstance().getManager(PermissionManager.class).canResetOthers(pplayer)) {
            List<String> replacements = Bukkit.getOnlinePlayers().stream().map(Player::getName).collect(Collectors.toList());
            List<String> suggestions = new ArrayList<>();
            StringUtil.copyPartialMatches(args[0], replacements, suggestions);
            return suggestions;
        }
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "reset";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-reset";
    }

    @Override
    public String getArguments() {
        return "[other]";
    }

    @Override
    public boolean requiresEffectsAndStyles() {
        return false;
    }

    @Override
    public boolean canConsoleExecute() {
        return true;
    }

}
