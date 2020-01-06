package dev.esophose.playerparticles.api;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import dev.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.ParticleStyle;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public final class PlayerParticlesAPI {

    private static PlayerParticlesAPI INSTANCE;

    private PlayerParticles playerParticles;

    private PlayerParticlesAPI() {
        this.playerParticles = PlayerParticles.getInstance();
    }

    public static PlayerParticlesAPI getInstance() {
        if (INSTANCE == null)
            INSTANCE = new PlayerParticlesAPI();
        return INSTANCE;
    }

    @Nullable
    public PPlayer getPPlayer(@NotNull UUID uuid) {
        Objects.requireNonNull(uuid);

        return this.playerParticles.getManager(DataManager.class).getPPlayer(uuid);
    }

    @Nullable
    public PPlayer getPPlayer(@NotNull Player player) {
        return this.getPPlayer(player.getUniqueId());
    }

    //region Manage Active Player Particles
    public void addActivePlayerParticle(@NotNull Player player, @NotNull ParticlePair particle) {
        Objects.requireNonNull(particle);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        ParticleGroup particleGroup = pplayer.getActiveParticleGroup();
        if (particleGroup.getParticles().containsKey(particle.getId()))
            throw new IllegalArgumentException("A particle with the id " + particle.getId() + " already exists");

        pplayer.getActiveParticleGroup().getParticles().put(particle.getId(), particle);
        dataManager.saveParticleGroup(player.getUniqueId(), pplayer.getActiveParticleGroup());
    }

    public void editActivePlayerParticle(@NotNull Player player, int id, @NotNull ParticleEffect effect) {
        Objects.requireNonNull(effect);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleGroup group = this.validateActivePlayerParticle(player, id);
        if (group != null) {
            group.getParticles().get(id).setEffect(effect);
            dataManager.saveParticleGroup(player.getUniqueId(), group);
        }
    }

    public void editActivePlayerParticle(@NotNull Player player, int id, @NotNull ParticleStyle style) {
        Objects.requireNonNull(style);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleGroup group = this.validateActivePlayerParticle(player, id);
        if (group != null) {
            group.getParticles().get(id).setStyle(style);
            dataManager.saveParticleGroup(player.getUniqueId(), group);
        }
    }

    public void editActivePlayerParticle(@NotNull Player player, int id, @NotNull OrdinaryColor color) {
        Objects.requireNonNull(color);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleGroup group = this.validateActivePlayerParticle(player, id);
        if (group != null) {
            group.getParticles().get(id).setColor(color);
            dataManager.saveParticleGroup(player.getUniqueId(), group);
        }
    }

    public void editActivePlayerParticle(@NotNull Player player, int id, @NotNull NoteColor noteColor) {
        Objects.requireNonNull(noteColor);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleGroup group = this.validateActivePlayerParticle(player, id);
        if (group != null) {
            group.getParticles().get(id).setNoteColor(noteColor);
            dataManager.saveParticleGroup(player.getUniqueId(), group);
        }
    }

    public void editActivePlayerParticle(@NotNull Player player, int id, @NotNull Material material) {
        Objects.requireNonNull(material);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleGroup group = this.validateActivePlayerParticle(player, id);
        if (group != null) {
            if (material.isBlock()) {
                group.getParticles().get(id).setBlockMaterial(material);
            } else {
                group.getParticles().get(id).setItemMaterial(material);
            }
            dataManager.saveParticleGroup(player.getUniqueId(), group);
        }
    }

    public void removeActivePlayerParticle(@NotNull Player player, int id) {
        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleGroup group = this.validateActivePlayerParticle(player, id);
        if (group != null) {
            group.getParticles().remove(id);
            dataManager.saveParticleGroup(player.getUniqueId(), group);
        }
    }

    public void removeActivePlayerParticle(@NotNull Player player, @NotNull ParticleEffect effect) {
        Objects.requireNonNull(effect);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        ParticleGroup group = pplayer.getActiveParticleGroup();
        group.getParticles().values().removeIf(x -> x.getEffect().equals(effect));
        dataManager.saveParticleGroup(player.getUniqueId(), group);
    }

    public void removeActivePlayerParticle(@NotNull Player player, @NotNull ParticleStyle style) {
        Objects.requireNonNull(style);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        ParticleGroup group = pplayer.getActiveParticleGroup();
        group.getParticles().values().removeIf(x -> x.getStyle().equals(style));
        dataManager.saveParticleGroup(player.getUniqueId(), group);
    }

    private ParticleGroup validateActivePlayerParticle(Player player, int id) {
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return null;

        ParticleGroup particleGroup = pplayer.getActiveParticleGroup();
        if (!particleGroup.getParticles().containsKey(id))
            throw new IllegalArgumentException("No particle exists with the id " + id);

        return particleGroup;
    }
    //endregion

}
