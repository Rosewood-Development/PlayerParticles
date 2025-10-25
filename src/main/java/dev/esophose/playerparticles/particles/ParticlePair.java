package dev.esophose.playerparticles.particles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleDataType;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.ParticleColor;
import dev.esophose.playerparticles.particles.data.TransparentColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.styles.DefaultStyles;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;

public class ParticlePair {

    private static final ParticlePair DEFAULT = new ParticlePair(
            null,
            -1,
            ParticleEffect.FLAME,
            DefaultStyles.NORMAL,
            ParticleUtils.closestMatchWithFallback(true, "IRON_SHOVEL", "IRON_SPADE"),
            Material.STONE,
            new OrdinaryColor(0, 0, 0),
            new NoteColor(0),
            new ColorTransition(new OrdinaryColor(0, 0, 0), new OrdinaryColor(255, 255, 255)),
            new Vibration(20)
    );

    private UUID ownerUUID;
    private final int id;

    private ParticleEffect effect;
    private ParticleStyle style;

    private Material itemMaterial;
    private Material blockMaterial;
    private OrdinaryColor color;
    private NoteColor noteColor;
    private ColorTransition colorTransition;
    private Vibration vibration;

    public ParticlePair(UUID ownerUUID, int id, ParticleEffect effect, ParticleStyle style, Material itemMaterial, Material blockMaterial, OrdinaryColor color, NoteColor noteColor, ColorTransition colorTransition, Vibration vibration) {
        this.ownerUUID = ownerUUID;
        this.id = id;

        this.effect = effect;
        this.style = style;

        this.setEffect(effect);
        this.setStyle(style);
        this.setItemMaterial(itemMaterial);
        this.setBlockMaterial(blockMaterial);
        this.setColor(color);
        this.setNoteColor(noteColor);
        this.setColorTransition(colorTransition);
        this.setVibration(vibration);
    }

    @Deprecated
    public ParticlePair(UUID ownerUUID, int id, ParticleEffect effect, ParticleStyle style, Material itemMaterial, Material blockMaterial, OrdinaryColor color, NoteColor noteColor) {
        this(ownerUUID, id, effect, style, itemMaterial, blockMaterial, color, noteColor, null, null);
    }

    /**
     * Updates the particle's owner if it hasn't been set yet
     *
     * @param pplayer The new owner
     */
    public void setOwner(PPlayer pplayer) {
        if (this.ownerUUID == null)
            this.ownerUUID = pplayer.getUniqueId();
    }

    /**
     * Sets the player's particle effect
     *
     * @param effect The player's new particle effect
     */
    public void setEffect(ParticleEffect effect) {
        if (effect == null) {
            this.effect = DEFAULT.getEffect();
        } else {
            this.effect = effect;
        }
    }

    /**
     * Sets the player's particle style
     *
     * @param style The player's new particle style
     */
    public void setStyle(ParticleStyle style) {
        if (style == null) {
            this.style = DEFAULT.getStyle();
        } else {
            this.style = style;
        }
    }

    /**
     * Sets the player's item material
     *
     * @param itemMaterial The player's new item material
     */
    public void setItemMaterial(Material itemMaterial) {
        if (itemMaterial == null || itemMaterial.isBlock()) {
            this.itemMaterial = DEFAULT.getItemMaterial();
        } else {
            this.itemMaterial = itemMaterial;
        }
    }

    /**
     * Sets the player's block material
     *
     * @param blockMaterial The player's new block material
     */
    public void setBlockMaterial(Material blockMaterial) {
        if (blockMaterial == null || !blockMaterial.isBlock()) {
            this.blockMaterial = DEFAULT.getBlockMaterial();
        } else {
            this.blockMaterial = blockMaterial;
        }
    }

    /**
     * Sets the player's color data
     *
     * @param colorData The player's new color data
     */
    public void setColor(OrdinaryColor colorData) {
        if (colorData == null) {
            this.color = DEFAULT.getColor();
        } else {
            this.color = colorData;
            if (this.colorTransition != null)
                this.colorTransition = new ColorTransition(this.color, this.colorTransition.getEndColor());
        }
    }

    /**
     * Sets the player's note color data
     *
     * @param noteColorData The player's new note color data
     */
    public void setNoteColor(NoteColor noteColorData) {
        if (noteColorData == null) {
            this.noteColor = DEFAULT.getNoteColor();
        } else {
            this.noteColor = noteColorData;
        }
    }

    /**
     * Sets the player's color transition data
     *
     * @param colorTransitionData The player's new color transition data
     */
    public void setColorTransition(ColorTransition colorTransitionData) {
        if (colorTransitionData == null) {
            this.colorTransition = new ColorTransition(this.getColor(), DEFAULT.getColorTransition().getEndColor());
        } else {
            this.color = colorTransitionData.getStartColor();
            this.colorTransition = colorTransitionData;
        }
    }

    /**
     * Sets the player's vibration data
     *
     * @param vibrationData The player's new vibration data
     */
    public void setVibration(Vibration vibrationData) {
        if (vibrationData == null) {
            this.vibration = DEFAULT.getVibration();
        } else {
            this.vibration = vibrationData;
        }
    }

    /**
     * Get the UUID of the PPlayer that owns this ParticlePair
     *
     * @return The owner's UUID
     */
    public UUID getOwnerUniqueId() {
        return this.ownerUUID;
    }

    /**
     * Get the id of this particle
     *
     * @return The id of this particle
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get the ParticleEffect that this ParticlePair represents
     *
     * @return The effect
     */
    public ParticleEffect getEffect() {
        return this.effect;
    }

    /**
     * Get the ParticleStyle that this ParticlePair represents
     *
     * @return The style
     */
    public ParticleStyle getStyle() {
        return this.style;
    }

    /**
     * Get the item Material this particle uses
     *
     * @return The item Material
     */
    public Material getItemMaterial() {
        return this.itemMaterial;
    }

    /**
     * Get the block Material this particle uses
     *
     * @return The block Material
     */
    public Material getBlockMaterial() {
        return this.blockMaterial;
    }

    /**
     * Get the color this particle uses
     *
     * @return The color
     */
    public OrdinaryColor getColor() {
        return this.color;
    }

    /**
     * Get the note color this particle uses
     *
     * @return The note color
     */
    public NoteColor getNoteColor() {
        return this.noteColor;
    }

    /**
     * Get the color transition this particle uses
     *
     * @return The color transition
     */
    public ColorTransition getColorTransition() {
        return this.colorTransition;
    }

    /**
     * Get the vibration this particle uses
     *
     * @return The vibration
     */
    public Vibration getVibration() {
        return this.vibration;
    }

    /**
     * Gets the color the current particle effect will spawn with
     *
     * @return The ParticleColor the current particle effect will spawn with
     */
    public ParticleColor getSpawnColor(ParticleEffect effect) {
        if (this.effect.getDataType() != ParticleDataType.COLORABLE && this.effect.getDataType() != ParticleDataType.COLORABLE_TRANSPARENCY)
            return null;

        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);
        if (this.effect == ParticleEffect.NOTE) {
            if (this.noteColor.equals(NoteColor.RAINBOW)) {
                return particleManager.getRainbowNoteParticleColor();
            } else if (this.noteColor.equals(NoteColor.RANDOM)) {
                return particleManager.getRandomNoteParticleColor();
            }
            return this.noteColor;
        } else {
            boolean transparent = effect.getDataType() == ParticleDataType.COLORABLE_TRANSPARENCY;
            if (this.color.equals(OrdinaryColor.RAINBOW)) {
                if (transparent) {
                    return new TransparentColor(particleManager.getRainbowParticleColor(), 255);
                } else {
                    return particleManager.getRainbowParticleColor();
                }
            } else if (this.color.equals(OrdinaryColor.RANDOM)) {
                if (transparent) {
                    return new TransparentColor(particleManager.getRandomParticleColor(), 255);
                } else {
                    return particleManager.getRandomParticleColor();
                }
            }
            return this.color;
        }
    }

    /**
     * Gets the color transition the current particle effect will spawn with
     *
     * @return The ColorTransition the current particle effect will spawn with
     */
    public ColorTransition getSpawnColorTransition() {
        if (this.effect.getDataType() != ParticleDataType.COLORABLE_TRANSITION)
            return null;

        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);
        OrdinaryColor startColor, endColor;
        if (this.colorTransition.getStartColor().equals(OrdinaryColor.RAINBOW)) {
            startColor = particleManager.getRainbowParticleColor();
        } else if (this.colorTransition.getStartColor().equals(OrdinaryColor.RANDOM)) {
            startColor = particleManager.getRandomParticleColor();
        } else {
            startColor = this.colorTransition.getStartColor();
        }

        if (this.colorTransition.getEndColor().equals(OrdinaryColor.RAINBOW)) {
            endColor = particleManager.getShiftedRainbowParticleColor();
        } else if (this.colorTransition.getEndColor().equals(OrdinaryColor.RANDOM)) {
            endColor = particleManager.getRandomParticleColor();
        } else {
            endColor = this.colorTransition.getEndColor();
        }

        return new ColorTransition(startColor, endColor);
    }

    /**
     * Gets the material the current particle effect will spawn with
     *
     * @return The Material the current particle effect requires
     */
    public Material getSpawnMaterial() {
        if (this.effect.getDataType() == ParticleDataType.BLOCK) {
            return this.blockMaterial;
        } else if (this.effect.getDataType() == ParticleDataType.ITEM) {
            return this.itemMaterial;
        }
        return null;
    }

    /**
     * Gets the current particle data as a string
     *
     * @return The particle data in a human-readable string
     */
    public String getDataString() {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        switch (this.effect.getDataType()) {
            case BLOCK:
            case ITEM:
                return this.getSpawnMaterial().toString().toLowerCase();
            case COLORABLE:
                if (this.effect == ParticleEffect.NOTE) {
                    if (this.noteColor.equals(NoteColor.RAINBOW)) {
                        return localeManager.getLocaleMessage("rainbow");
                    } else if (this.noteColor.equals(NoteColor.RANDOM)) {
                        return localeManager.getLocaleMessage("random");
                    }
                    return localeManager.getLocaleMessage("gui-select-data-note", StringPlaceholders.of("note", this.noteColor.getNote()));
                } else {
                    if (this.color.equals(OrdinaryColor.RAINBOW)) {
                        return localeManager.getLocaleMessage("rainbow");
                    } else if (this.color.equals(OrdinaryColor.RANDOM)) {
                        return localeManager.getLocaleMessage("random");
                    } else {
                        return ChatColor.AQUA + "#" + ChatColor.AQUA + ParticleUtils.rgbToHex(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
                    }
                }
            case COLORABLE_TRANSPARENCY:
                if (this.color.equals(OrdinaryColor.RAINBOW)) {
                    return localeManager.getLocaleMessage("rainbow");
                } else if (this.color.equals(OrdinaryColor.RANDOM)) {
                    return localeManager.getLocaleMessage("random");
                } else {
                    if (this.color instanceof TransparentColor) {
                        return ChatColor.AQUA + "#" + ChatColor.AQUA + ParticleUtils.rgbaToHex(this.color.getRed(), this.color.getGreen(), this.color.getBlue(), ((TransparentColor) this.color).getAlpha());
                    } else {
                        return ChatColor.AQUA + "#" + ChatColor.AQUA + ParticleUtils.rgbToHex(this.color.getRed(), this.color.getGreen(), this.color.getBlue());
                    }
                }
            case COLORABLE_TRANSITION:
                String start, end;
                if (this.colorTransition.getStartColor().equals(OrdinaryColor.RAINBOW)) {
                    start = localeManager.getLocaleMessage("rainbow");
                } else if (this.colorTransition.getStartColor().equals(OrdinaryColor.RANDOM)) {
                    start = localeManager.getLocaleMessage("random");
                } else {
                    start = "#" + ChatColor.AQUA + ParticleUtils.rgbToHex(this.colorTransition.getStartColor().getRed(), this.colorTransition.getStartColor().getGreen(), this.colorTransition.getStartColor().getBlue());
                }

                if (this.colorTransition.getEndColor().equals(OrdinaryColor.RAINBOW)) {
                    end = localeManager.getLocaleMessage("rainbow");
                } else if (this.colorTransition.getEndColor().equals(OrdinaryColor.RANDOM)) {
                    end = localeManager.getLocaleMessage("random");
                } else {
                    end = "#" + ChatColor.AQUA + ParticleUtils.rgbToHex(this.colorTransition.getEndColor().getRed(), this.colorTransition.getEndColor().getGreen(), this.colorTransition.getEndColor().getBlue());
                }

                return ChatColor.AQUA + start + " " + ChatColor.AQUA + end;
            case VIBRATION:
                return String.valueOf(this.vibration.getDuration());
            default:
                return localeManager.getLocaleMessage("gui-data-none");
        }
    }

    public boolean isHiddenWhenLimited() {
        return this.effect.isHiddenWhenLimited() || this.style.isHiddenWhenLimited();
    }

    /**
     * @return a copy of this ParticlePair
     */
    public ParticlePair clone() {
        return new ParticlePair(this.ownerUUID, this.id, this.effect, this.style, this.itemMaterial, this.blockMaterial, this.color, this.noteColor, this.colorTransition, this.vibration);
    }

    /**
     * Gets a copy of this ParticlePair with a specified ID
     *
     * @param id the new ID of the ParticlePair
     * @return a copy of this ParticlePair with a different ID
     */
    public ParticlePair clone(int id) {
        return new ParticlePair(this.ownerUUID, id, this.effect, this.style, this.itemMaterial, this.blockMaterial, this.color, this.noteColor, this.colorTransition, this.vibration);
    }

    /**
     * Gets a ParticlePair for a PPlayer with the default values applied
     *
     * @param pplayer The PPlayer that will own this ParticlePair
     * @return A ParticlePair with default values
     */
    public static ParticlePair getNextDefault(PPlayer pplayer) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        List<ParticleEffect> effects;
        List<ParticleStyle> styles;
        if (pplayer.getPlayer() == null) {
            effects = Collections.emptyList();
            styles = Collections.emptyList();
        } else {
            effects = permissionManager.getEffectsUserHasPermissionFor(pplayer);
            styles = permissionManager.getStylesUserHasPermissionFor(pplayer);
        }

        return new ParticlePair(pplayer.getUniqueId(),
                pplayer.getNextActiveParticleId(),
                effects.stream().findFirst().orElse(ParticleEffect.FLAME),
                styles.stream().findFirst().orElse(DefaultStyles.NORMAL),
                ParticleUtils.closestMatchWithFallback(true, "IRON_SHOVEL", "IRON_SPADE"),
                Material.STONE,
                new OrdinaryColor(0, 0, 0),
                new NoteColor(0),
                new ColorTransition(new OrdinaryColor(0, 0, 0), new OrdinaryColor(255, 255, 255)),
                new Vibration(20)
        );
    }

}
