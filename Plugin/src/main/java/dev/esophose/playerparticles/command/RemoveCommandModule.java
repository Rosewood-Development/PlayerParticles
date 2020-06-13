package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.apache.commons.lang.StringUtils;
import org.bukkit.util.StringUtil;

public class RemoveCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        if (args.length == 0) {
            localeManager.sendMessage(pplayer, "remove-no-args");
            return;
        }
        
        if (StringUtils.isNumeric(args[0])) { // Removing by ID
            int id;
            try {
                id = Integer.parseInt(args[0]);
            } catch (Exception ex) {
                localeManager.sendMessage(pplayer, "id-invalid");
                return;
            }

            if (id <= 0) {
                localeManager.sendMessage(pplayer, "id-invalid");
                return;
            }

            ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
            boolean removed = activeGroup.getParticles().remove(id) != null;

            if (!removed) {
                localeManager.sendMessage(pplayer, "id-unknown", StringPlaceholders.single("id", id));
                return;
            }

            PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), activeGroup);
            localeManager.sendMessage(pplayer, "remove-id-success", StringPlaceholders.single("id", id));
        } else { // Removing by effect/style name
            ParticleEffect effect = particleManager.getEffectFromName(args[0]);
            ParticleStyle style = ParticleStyle.fromName(args[0]);

            boolean removed = false;

            if (effect != null) {
                Set<Integer> toRemove = new HashSet<>();
                ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                for (int id : activeGroup.getParticles().keySet())
                    if (activeGroup.getParticles().get(id).getEffect() == effect)
                        toRemove.add(id);
                for (int id : toRemove)
                    activeGroup.getParticles().remove(id);

                if (toRemove.size() > 0) {
                    PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), activeGroup);
                    localeManager.sendMessage(pplayer, "remove-effect-success", StringPlaceholders.builder("amount", toRemove.size()).addPlaceholder("effect", particleManager.getEffectSettings(effect).getName()).build());
                    removed = true;
                } else if (style == null) {
                    localeManager.sendMessage(pplayer, "remove-effect-none", StringPlaceholders.single("effect", particleManager.getEffectSettings(effect).getName()));
                    return;
                }
            }

            if (style != null) {
                Set<Integer> toRemove = new HashSet<>();
                ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
                for (int id : activeGroup.getParticles().keySet())
                    if (activeGroup.getParticles().get(id).getStyle() == style)
                        toRemove.add(id);
                for (int id : toRemove)
                    activeGroup.getParticles().remove(id);

                if (toRemove.size() > 0) {
                    PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), activeGroup);
                    localeManager.sendMessage(pplayer, "remove-style-success", StringPlaceholders.builder("amount", toRemove.size()).addPlaceholder("style", style.getName()).build());
                    removed = true;
                } else if (effect == null) {
                    localeManager.sendMessage(pplayer, "remove-style-none", StringPlaceholders.single("style", style.getName()));
                    return;
                }
            }

            if (!removed) {
                if (effect != null && style != null) {
                    localeManager.sendMessage(pplayer, "remove-effect-style-none", StringPlaceholders.single("name", style.getName()));
                } else {
                    localeManager.sendMessage(pplayer, "remove-unknown", StringPlaceholders.single("name", args[0]));
                }
            }
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        List<String> matches = new ArrayList<>();
        Set<String> removeBy = new HashSet<>();

        for (ParticlePair particle : pplayer.getActiveParticles()) {
            removeBy.add(String.valueOf(particle.getId()));
            removeBy.add(particleManager.getEffectSettings(particle.getEffect()).getName());
            removeBy.add(particle.getStyle().getName());
        }

        if (args.length == 0) return new ArrayList<>(removeBy);

        StringUtil.copyPartialMatches(args[0], removeBy, matches);
        return matches;
    }

    public String getName() {
        return "remove";
    }

    public String getDescriptionKey() {
        return "command-description-remove";
    }

    public String getArguments() {
        return "<ID>|<effect>|<style>";
    }

    public boolean requiresEffectsAndStyles() {
        return false;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
