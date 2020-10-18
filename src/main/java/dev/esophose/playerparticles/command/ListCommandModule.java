package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ListCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        List<ParticlePair> particles = new ArrayList<>(pplayer.getActiveParticles());
        particles.sort(Comparator.comparingInt(ParticlePair::getId));
        
        if (particles.isEmpty()) {
            localeManager.sendMessage(pplayer, "list-none");
            return;
        }

        localeManager.sendMessage(pplayer, "list-you-have");
        for (ParticlePair particle : particles) {
            StringPlaceholders stringPlaceholders = StringPlaceholders.builder("id", particle.getId())
                    .addPlaceholder("effect", particle.getEffect().getName())
                    .addPlaceholder("style", particle.getStyle().getName())
                    .addPlaceholder("data", particle.getDataString())
                    .build();
            localeManager.sendMessage(pplayer, "list-output", stringPlaceholders);
        }
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-list";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public boolean requiresEffectsAndStyles() {
        return true;
    }

    @Override
    public boolean canConsoleExecute() {
        return false;
    }

}
