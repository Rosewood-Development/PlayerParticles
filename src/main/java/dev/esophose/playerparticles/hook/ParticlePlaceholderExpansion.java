package dev.esophose.playerparticles.hook;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Player;

public class ParticlePlaceholderExpansion extends PlaceholderExpansion {

    private final PlayerParticles playerParticles;

    public ParticlePlaceholderExpansion(PlayerParticles playerParticles) {
        this.playerParticles = playerParticles;
    }

    @Override
    public String onPlaceholderRequest(Player p, String placeholder) {
        if (p == null)
            return null;

        PPlayer pplayer = this.playerParticles.getManager(DataManager.class).getPPlayer(p.getUniqueId());
        if (pplayer == null)
            return null;

        switch (placeholder) {
            case "active_amount":
                return String.valueOf(pplayer.getActiveParticles().size());
            case "group_amount":
                return String.valueOf(pplayer.getParticleGroups().size() - 1);
            case "fixed_amount":
                return String.valueOf(pplayer.getFixedParticles().size());
            case "is_moving":
                return String.valueOf(pplayer.isMoving());
            case "is_in_combat":
                return String.valueOf(pplayer.isInCombat());
            case "is_in_allowed_region":
                return String.valueOf(pplayer.isInAllowedRegion());
            case "is_in_limited_region":
                return String.valueOf(pplayer.isInLimitedRegion());
            case "can_see_particles":
                return String.valueOf(pplayer.canSeeParticles());
        }

        if (placeholder.startsWith("has_effect_")) {
            String effectName = placeholder.substring(11);
            return Boolean.toString(pplayer.getActiveParticles().stream().anyMatch(x -> x.getEffect().getName().equalsIgnoreCase(effectName)));
        } else if (placeholder.startsWith("has_style_")) {
            String styleName = placeholder.substring(10);
            return Boolean.toString(pplayer.getActiveParticles().stream().anyMatch(x -> x.getStyle().getName().equalsIgnoreCase(styleName)));
        } else if (placeholder.startsWith("particle_")) {
            ParticlePair particle = pplayer.getActiveParticle(this.parseId(placeholder));
            if (particle == null)
                return "none";

            if (placeholder.startsWith("particle_effect_")) {
                return particle.getEffect().getName();
            } else if (placeholder.startsWith("particle_style_")) {
                return particle.getStyle().getName();
            } else if (placeholder.startsWith("particle_data_")) {
                return ChatColor.stripColor(particle.getDataString());
            }
        }

        return null;
    }

    private int parseId(String placeholder) {
        int lastIndex = placeholder.lastIndexOf('_');
        String number = placeholder.substring(lastIndex + 1);
        try {
            return Integer.parseInt(number);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public String getIdentifier() {
        return PlayerParticles.getInstance().getDescription().getName().toLowerCase();
    }

    @Override
    public String getAuthor() {
        return PlayerParticles.getInstance().getDescription().getAuthors().get(0);
    }

    @Override
    public String getVersion() {
        return PlayerParticles.getInstance().getDescription().getVersion();
    }

}
