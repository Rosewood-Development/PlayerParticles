package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.util.StringUtil;

public class ToggleCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        if (args.length == 0) {
            boolean particlesHidden = pplayer.canSeeParticles();
            PlayerParticlesAPI.getInstance().togglePlayerParticleVisibility(pplayer.getPlayer(), particlesHidden);

            if (particlesHidden) {
                localeManager.sendMessage(pplayer, "toggle-off");
            } else {
                localeManager.sendMessage(pplayer, "toggle-on");
            }
        } else if (args[0].equalsIgnoreCase("on")) {
            PlayerParticlesAPI.getInstance().togglePlayerParticleVisibility(pplayer.getPlayer(), false);
            localeManager.sendMessage(pplayer, "toggle-on");
        } else {
            PlayerParticlesAPI.getInstance().togglePlayerParticleVisibility(pplayer.getPlayer(), true);
            localeManager.sendMessage(pplayer, "toggle-off");
        }
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            List<String> possibilities = Arrays.asList("on", "off");
            StringUtil.copyPartialMatches(args[0], possibilities, completions);
        }
        return completions;
    }

    @Override
    public String getName() {
        return "toggle";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-toggle";
    }

    @Override
    public String getArguments() {
        return "[on|off]";
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
