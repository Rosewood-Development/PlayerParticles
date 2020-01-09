package dev.esophose.playerparticles.api;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.GuiManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import dev.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.ParticleStyle;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The API for the PlayerParticles plugin.
 * Used to manipulate a player's particles and data.
 *
 * Note: This API will bypass all permissions and does not send any messages.
 *       Any changes made through the API will be saved to the database automatically.
 */
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

    //region Get PPlayer
    @Nullable
    public PPlayer getPPlayer(@NotNull UUID uuid) {
        Objects.requireNonNull(uuid);

        return this.playerParticles.getManager(DataManager.class).getPPlayer(uuid);
    }

    @Nullable
    public PPlayer getPPlayer(@NotNull Player player) {
        Objects.requireNonNull(player);

        return this.getPPlayer(player.getUniqueId());
    }
    //endregion

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

    public void addActivePlayerParticle(@NotNull Player player, @NotNull ParticleEffect effect, @NotNull ParticleStyle style) {
        this.addActivePlayerParticle(player, effect, style, null, null, null);
    }

    public void addActivePlayerParticle(@NotNull Player player, @NotNull ParticleEffect effect, @NotNull ParticleStyle style, @NotNull OrdinaryColor colorData) {
        this.addActivePlayerParticle(player, effect, style, colorData, null, null);
    }

    public void addActivePlayerParticle(@NotNull Player player, @NotNull ParticleEffect effect, @NotNull ParticleStyle style, @NotNull NoteColor noteColorData) {
        this.addActivePlayerParticle(player, effect, style, null, noteColorData, null);
    }

    public void addActivePlayerParticle(@NotNull Player player, @NotNull ParticleEffect effect, @NotNull ParticleStyle style, @NotNull Material materialData) {
        this.addActivePlayerParticle(player, effect, style, null, null, materialData);
    }

    private void addActivePlayerParticle(@NotNull Player player, @NotNull ParticleEffect effect, @NotNull ParticleStyle style, @Nullable OrdinaryColor colorData, @Nullable NoteColor noteColorData, @Nullable Material materialData) {
        Objects.requireNonNull(effect);
        Objects.requireNonNull(style);

        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        Material itemMaterialData = null;
        Material blockMaterialData = null;
        if (materialData != null) {
            if (materialData.isBlock()) {
                blockMaterialData = materialData;
            } else {
                itemMaterialData = materialData;
            }
        }

        ParticlePair particle = new ParticlePair(player.getUniqueId(), pplayer.getNextActiveParticleId(), effect, style, itemMaterialData, blockMaterialData, colorData, noteColorData);
        this.addActivePlayerParticle(player, particle);
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

    public void editActivePlayerParticle(@NotNull Player player, int id, @NotNull OrdinaryColor colorData) {
        Objects.requireNonNull(colorData);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleGroup group = this.validateActivePlayerParticle(player, id);
        if (group != null) {
            group.getParticles().get(id).setColor(colorData);
            dataManager.saveParticleGroup(player.getUniqueId(), group);
        }
    }

    public void editActivePlayerParticle(@NotNull Player player, int id, @NotNull NoteColor noteColorData) {
        Objects.requireNonNull(noteColorData);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleGroup group = this.validateActivePlayerParticle(player, id);
        if (group != null) {
            group.getParticles().get(id).setNoteColor(noteColorData);
            dataManager.saveParticleGroup(player.getUniqueId(), group);
        }
    }

    public void editActivePlayerParticle(@NotNull Player player, int id, @NotNull Material materialData) {
        Objects.requireNonNull(materialData);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleGroup group = this.validateActivePlayerParticle(player, id);
        if (group != null) {
            if (materialData.isBlock()) {
                group.getParticles().get(id).setBlockMaterial(materialData);
            } else {
                group.getParticles().get(id).setItemMaterial(materialData);
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

    @Nullable
    private ParticleGroup validateActivePlayerParticle(@NotNull Player player, int id) {
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return null;

        ParticleGroup particleGroup = pplayer.getActiveParticleGroup();
        if (!particleGroup.getParticles().containsKey(id))
            throw new IllegalArgumentException("No particle exists with the id " + id);

        return particleGroup;
    }

    public void resetActivePlayerParticles(@NotNull Player player) {
        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        pplayer.getActiveParticleGroup().getParticles().clear();
        dataManager.saveParticleGroup(pplayer.getUniqueId(), pplayer.getActiveParticleGroup());
    }

    @NotNull
    public Collection<ParticlePair> getActivePlayerParticles(@NotNull Player player) {
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return new ArrayList<>();

        return pplayer.getActiveParticles();
    }

    @Nullable
    public ParticlePair getActivePlayerParticle(@NotNull Player player, int id) {
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return null;

        return pplayer.getActiveParticle(id);
    }
    //endregion

    //region Manage Player Particle Groups
    public void savePlayerParticleGroup(@NotNull Player player, @NotNull ParticleGroup particleGroup) {
        Objects.requireNonNull(particleGroup);

        if (particleGroup.getParticles().isEmpty() && !particleGroup.getName().equals(ParticleGroup.DEFAULT_NAME))
            throw new IllegalArgumentException("Cannot save an empty ParticleGroup");

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        pplayer.getParticleGroups().put(particleGroup.getName().toLowerCase(), particleGroup);
        dataManager.saveParticleGroup(player.getUniqueId(), particleGroup);
    }

    public void savePlayerParticleGroup(@NotNull Player player, @NotNull String groupName, @NotNull Collection<ParticlePair> particles) {
        Objects.requireNonNull(groupName);
        Objects.requireNonNull(particles);

        Map<Integer, ParticlePair> mappedParticles = new HashMap<>();
        particles.forEach(x -> mappedParticles.put(x.getId(), x));
        ParticleGroup particleGroup = new ParticleGroup(groupName.toLowerCase(), mappedParticles);
        this.savePlayerParticleGroup(player, particleGroup);
    }

    public void removePlayerParticleGroup(@NotNull Player player, @NotNull String groupName) {
        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        pplayer.getParticleGroups().remove(groupName.toLowerCase());
        dataManager.removeParticleGroup(player.getUniqueId(), groupName);
    }

    public void removePlayerParticleGroup(@NotNull Player player, @NotNull ParticleGroup particleGroup) {
        Objects.requireNonNull(particleGroup);

        this.removePlayerParticleGroup(player, particleGroup.getName());
    }

    @NotNull
    public Collection<ParticleGroup> getPlayerParticleGroups(@NotNull Player player) {
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return new ArrayList<>();

        return pplayer.getParticleGroups().values();
    }
    //endregion

    //region Fixed Effect Management
    public void createFixedParticleEffect(@NotNull Player player, @NotNull Location location, @NotNull ParticlePair particle) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(location.getWorld());
        Objects.requireNonNull(particle);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        FixedParticleEffect fixedEffect = new FixedParticleEffect(player.getUniqueId(), pplayer.getNextFixedEffectId(), location, particle);
        pplayer.addFixedEffect(fixedEffect);
        dataManager.saveFixedEffect(fixedEffect);
    }

    public void createFixedParticleEffect(@NotNull Player player, @NotNull Location location, @NotNull ParticleEffect effect, @NotNull ParticleStyle style) {
        this.createFixedParticleEffect(player, location, effect, style, null, null, null);
    }

    public void createFixedParticleEffect(@NotNull Player player, @NotNull Location location, @NotNull ParticleEffect effect, @NotNull ParticleStyle style, @NotNull OrdinaryColor colorData) {
        this.createFixedParticleEffect(player, location, effect, style, colorData, null, null);
    }

    public void createFixedParticleEffect(@NotNull Player player, @NotNull Location location, @NotNull ParticleEffect effect, @NotNull ParticleStyle style, @NotNull NoteColor noteColorData) {
        this.createFixedParticleEffect(player, location, effect, style, null, noteColorData, null);
    }

    public void createFixedParticleEffect(@NotNull Player player, @NotNull Location location, @NotNull ParticleEffect effect, @NotNull ParticleStyle style, @NotNull Material materialData) {
        this.createFixedParticleEffect(player, location, effect, style, null, null, materialData);
    }

    private void createFixedParticleEffect(@NotNull Player player, @NotNull Location location, @NotNull ParticleEffect effect, @NotNull ParticleStyle style, @Nullable OrdinaryColor colorData, @Nullable NoteColor noteColorData, @Nullable Material materialData) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(location.getWorld());
        Objects.requireNonNull(effect);
        Objects.requireNonNull(style);

        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        Material itemMaterialData = null;
        Material blockMaterialData = null;
        if (materialData != null) {
            if (materialData.isBlock()) {
                blockMaterialData = materialData;
            } else {
                itemMaterialData = materialData;
            }
        }

        ParticlePair particle = new ParticlePair(player.getUniqueId(), 1, effect, style, itemMaterialData, blockMaterialData, colorData, noteColorData);
        this.createFixedParticleEffect(player, location, particle);
    }

    public void editFixedParticleEffect(@NotNull Player player, @NotNull FixedParticleEffect fixedEffect) {
        Objects.requireNonNull(fixedEffect);

        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        if (this.validateFixedParticleEffect(player, fixedEffect.getId()) != null) {
            pplayer.removeFixedEffect(fixedEffect.getId());
            pplayer.addFixedEffect(fixedEffect);
            dataManager.updateFixedEffect(fixedEffect);
        }
    }

    public void editFixedParticleEffect(@NotNull Player player, int id, @NotNull Location location) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(location.getWorld());

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        FixedParticleEffect fixedEffect = this.validateFixedParticleEffect(player, id);
        if (fixedEffect != null) {
            fixedEffect.setCoordinates(location.getX(), location.getY(), location.getZ());
            dataManager.saveFixedEffect(fixedEffect);
        }
    }

    public void editFixedParticleEffect(@NotNull Player player, int id, @NotNull ParticleEffect effect) {
        Objects.requireNonNull(effect);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        FixedParticleEffect fixedEffect = this.validateFixedParticleEffect(player, id);
        if (fixedEffect != null) {
            fixedEffect.getParticlePair().setEffect(effect);
            dataManager.saveFixedEffect(fixedEffect);
        }
    }

    public void editFixedParticleEffect(@NotNull Player player, int id, @NotNull ParticleStyle style) {
        Objects.requireNonNull(style);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        FixedParticleEffect fixedEffect = this.validateFixedParticleEffect(player, id);
        if (fixedEffect != null) {
            fixedEffect.getParticlePair().setStyle(style);
            dataManager.saveFixedEffect(fixedEffect);
        }
    }

    public void editFixedParticleEffect(@NotNull Player player, int id, @NotNull OrdinaryColor colorData) {
        Objects.requireNonNull(colorData);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        FixedParticleEffect fixedEffect = this.validateFixedParticleEffect(player, id);
        if (fixedEffect != null) {
            fixedEffect.getParticlePair().setColor(colorData);
            dataManager.saveFixedEffect(fixedEffect);
        }
    }

    public void editFixedParticleEffect(@NotNull Player player, int id, @NotNull NoteColor noteColorData) {
        Objects.requireNonNull(noteColorData);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        FixedParticleEffect fixedEffect = this.validateFixedParticleEffect(player, id);
        if (fixedEffect != null) {
            fixedEffect.getParticlePair().setNoteColor(noteColorData);
            dataManager.saveFixedEffect(fixedEffect);
        }
    }

    public void editFixedParticleEffect(@NotNull Player player, int id, @NotNull Material materialData) {
        Objects.requireNonNull(materialData);

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        FixedParticleEffect fixedEffect = this.validateFixedParticleEffect(player, id);
        if (fixedEffect != null) {
            if (materialData.isBlock()) {
                fixedEffect.getParticlePair().setBlockMaterial(materialData);
            } else {
                fixedEffect.getParticlePair().setItemMaterial(materialData);
            }
            dataManager.saveFixedEffect(fixedEffect);
        }
    }

    public void removeFixedEffect(@NotNull Player player, int id) {
        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        FixedParticleEffect fixedEffect = this.validateFixedParticleEffect(player, id);
        if (fixedEffect != null) {
            PPlayer pplayer = this.getPPlayer(player);
            if (pplayer == null)
                return;

            pplayer.removeFixedEffect(id);
            dataManager.removeFixedEffect(player.getUniqueId(), fixedEffect.getId());
        }
    }

    /**
     * Removes fixed effects within a given radius of a location
     *
     * @param location The location to search around
     * @param radius The radius to remove
     * @return The number of fixed effects that were removed
     */
    public int removeFixedEffectsInRange(@NotNull Location location, double radius) {
        Objects.requireNonNull(location);
        Objects.requireNonNull(location.getWorld());

        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        ParticleManager particleManager = this.playerParticles.getManager(ParticleManager.class);

        int removed = 0;
        for (PPlayer pplayer : particleManager.getPPlayers()) {
            for (FixedParticleEffect fixedEffect : pplayer.getFixedParticles()) {
                if (fixedEffect.getLocation().getWorld() == location.getWorld() && fixedEffect.getLocation().distance(location) <= radius) {
                    pplayer.removeFixedEffect(fixedEffect.getId());
                    dataManager.removeFixedEffect(pplayer.getUniqueId(), fixedEffect.getId());
                    removed++;
                }
            }
        }

        return removed;
    }

    @Nullable
    private FixedParticleEffect validateFixedParticleEffect(@NotNull Player player, int id) {
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return null;

        FixedParticleEffect fixedEffect = pplayer.getFixedEffectById(id);
        if (fixedEffect == null)
            throw new IllegalArgumentException("No fixed effect exists with the id " + id);

        return fixedEffect;
    }

    @Nullable
    public FixedParticleEffect getFixedParticleEffect(@NotNull Player player, int id) {
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return null;

        return pplayer.getFixedEffectById(id);
    }

    @NotNull
    public Collection<FixedParticleEffect> getFixedParticleEffects(@NotNull Player player) {
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return new ArrayList<>();

        return pplayer.getFixedParticlesMap().values();
    }
    //endregion

    //region GUI Management
    public void openParticlesGui(@NotNull Player player) {
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        this.playerParticles.getManager(GuiManager.class).openDefault(pplayer);
    }
    //endregion

    //region Player Settings
    public void togglePlayerParticleVisibility(@NotNull Player player, boolean particlesHidden) {
        DataManager dataManager = this.playerParticles.getManager(DataManager.class);
        PPlayer pplayer = this.getPPlayer(player);
        if (pplayer == null)
            return;

        pplayer.setParticlesHidden(particlesHidden);
        dataManager.updateSettingParticlesHidden(player.getUniqueId(), particlesHidden);
    }
    //endregion

    //region Registering Custom Styles
    public void registerParticleStyle(@NotNull ParticleStyle particleStyle) {
        Objects.requireNonNull(particleStyle);

        this.playerParticles.getManager(ParticleStyleManager.class).registerStyle(particleStyle);
    }

    public void registerEventParticleStyle(@NotNull ParticleStyle particleStyle) {
        Objects.requireNonNull(particleStyle);

        this.playerParticles.getManager(ParticleStyleManager.class).registerEventStyle(particleStyle);
    }
    //endregion

}
