package dev.esophose.playerparticles.hook;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ParticlePlaceholderExpansion extends PlaceholderExpansion {

    private PlayerParticles playerParticles;

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

        if (placeholder.equals("active_amount")) {
            return String.valueOf(pplayer.getActiveParticles().size());
        } else if (placeholder.equals("group_amount")) {
            return String.valueOf(pplayer.getParticleGroups().size());
        } else if (placeholder.equals("fixed_amount")) {
            return String.valueOf(pplayer.getFixedParticles().size());
        } else if (placeholder.equals("is_moving")) {
            return String.valueOf(pplayer.isMoving());
        } else if (placeholder.equals("can_see_particles")) {
            return String.valueOf(pplayer.canSeeParticles());
        }

        if (placeholder.startsWith("particle_")) {
            ParticlePair particle = pplayer.getActiveParticle(this.parseId(placeholder));
            if (particle == null)
                return null;

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
