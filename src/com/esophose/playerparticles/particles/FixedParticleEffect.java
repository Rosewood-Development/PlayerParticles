/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.particles;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.manager.ConfigManager;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.particles.ParticleEffect.BlockData;
import com.esophose.playerparticles.particles.ParticleEffect.ItemData;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleData;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class FixedParticleEffect {

    /**
     * The UUID of the player who owns this effect
     */
    private UUID pplayerUUID;

    /**
     * The ID of this effect, unique to the owner's UUID
     */
    private int id;

    /**
     * The location for this effect to be displayed
     */
    private Location location;

    /**
     * The effect and style this effect uses
     */
    private ParticleEffect particleEffect;
    private ParticleStyle particleStyle;

    /**
     * The data this effect uses
     */
    private ItemData particleItemData;
    private BlockData particleBlockData;
    private OrdinaryColor particleColorData;
    private NoteColor particleNoteColorData;

    /**
     * Constructs a new FixedParticleEffect
     * FixedParticleEffects can NOT use custom handled styles
     * 
     * @param pplayerUUID The UUID of the player who owns the effect
     * @param id The id this effect has, unique to the owner pplayer
     * @param worldName The world name this effect will be displayed in
     * @param xPos The X position in the world
     * @param yPos The Y position in the world
     * @param zPos The Z position in the world
     * @param particleEffect The particle effect to use
     * @param particleStyle The particle style to use
     * @param itemData The item data for the effect
     * @param blockData The block data for the effect
     * @param colorData The color data for the effect
     * @param noteColorData The note color data for the effect
     */
    public FixedParticleEffect(UUID pplayerUUID, int id, String worldName, double xPos, double yPos, double zPos, ParticleEffect particleEffect, ParticleStyle particleStyle, ItemData itemData, BlockData blockData, OrdinaryColor colorData, NoteColor noteColorData) {
        this.pplayerUUID = pplayerUUID;
        this.id = id;

        this.particleEffect = particleEffect;
        this.particleStyle = particleStyle;

        this.particleItemData = itemData;
        this.particleBlockData = blockData;
        this.particleColorData = colorData;
        this.particleNoteColorData = noteColorData;

        PPlayer owner = ConfigManager.getInstance().getPPlayer(this.pplayerUUID, true);

        // Check nulls, if any are null set them to the PPlayer's values
        if (this.particleItemData == null) this.particleItemData = owner.getItemData();
        if (this.particleBlockData == null) this.particleBlockData = owner.getBlockData();
        if (this.particleColorData == null) this.particleColorData = owner.getColorData();
        if (this.particleNoteColorData == null) this.particleNoteColorData = owner.getNoteColorData();

        World world = Bukkit.getWorld(worldName);
        if (world == null) { // Default to the first world in case it doesn't exist
            world = Bukkit.getWorlds().get(0); // All servers will have at least one world
        }

        this.location = new Location(world, xPos, yPos, zPos);
    }

    /**
     * Gets the owner of the effect's UUID
     * 
     * @return The owner of the effect's UUID
     */
    public UUID getOwnerUniqueId() {
        return this.pplayerUUID;
    }

    /**
     * Gets the id unique to the owner's UUID
     * 
     * @return This effect's id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets the particle effect used for this effect
     * 
     * @return The particle effect used for this effect
     */
    public ParticleEffect getParticleEffect() {
        return this.particleEffect;
    }

    /**
     * Gets the particle style used for this effect
     * 
     * @return The particle style used for this effect
     */
    public ParticleStyle getParticleStyle() {
        return this.particleStyle;
    }

    /**
     * Gets the effect's item data
     * 
     * @return The effect's item data
     */
    public ItemData getItemData() {
        return this.particleItemData;
    }

    /**
     * Gets the effect's block data
     * 
     * @return The effect's block data
     */
    public BlockData getBlockData() {
        return this.particleBlockData;
    }

    /**
     * Gets the effect's color data
     * 
     * @return The effect's color data
     */
    public OrdinaryColor getColorData() {
        return this.particleColorData;
    }

    /**
     * Gets the effect's note color data
     * 
     * @return The effect's note color data
     */
    public NoteColor getNoteColorData() {
        return this.particleNoteColorData;
    }

    /**
     * Gets the data the current particle effect will spawn with
     * 
     * @return The ParticleData the current particle effect requires
     */
    public ParticleData getParticleSpawnData() {
        if (this.particleEffect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
            if (this.particleEffect == ParticleEffect.BLOCK_CRACK || this.particleEffect == ParticleEffect.BLOCK_DUST || this.particleEffect == ParticleEffect.FALLING_DUST) {
                return particleBlockData;
            } else if (this.particleEffect == ParticleEffect.ITEM_CRACK) {
                return this.particleItemData;
            }
        }
        return null;
    }

    /**
     * Gets the color the current particle effect will spawn with
     * 
     * @return Gets the ParticleColor the current particle effect will spawn with
     */
    public ParticleColor getParticleSpawnColor() {
        if (this.particleEffect.hasProperty(ParticleProperty.COLORABLE)) {
            if (this.particleEffect == ParticleEffect.NOTE) {
                if (this.particleNoteColorData.getValueX() * 24 == 99) {
                    return ParticleManager.getRainbowNoteParticleColor();
                }
                return this.particleNoteColorData;
            } else {
                if (this.particleColorData.getRed() == 999 && this.particleColorData.getGreen() == 999 && this.particleColorData.getBlue() == 999) {
                    return ParticleManager.getRainbowParticleColor();
                } else {
                    return this.particleColorData;
                }
            }
        }
        return null;
    }

    /**
     * Gets the particle data as a string
     * 
     * @return A string of the current effect's data
     */
    public String getParticleDataString() {
        if (this.particleEffect.hasProperty(ParticleProperty.COLORABLE)) {
            if (this.particleEffect == ParticleEffect.NOTE) {
                if (this.particleNoteColorData.getValueX() * 24 == 99) {
                    return "rainbow";
                } else {
                    return (this.particleNoteColorData.getValueX() * 24) + "";
                }
            } else {
                if (this.particleColorData.getRed() == 999 && this.particleColorData.getGreen() == 999 && this.particleColorData.getBlue() == 999) {
                    return "rainbow";
                } else {
                    return this.particleColorData.getRed() + " " + this.particleColorData.getGreen() + " " + this.particleColorData.getBlue();
                }
            }
        } else if (this.particleEffect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
            if (this.particleEffect == ParticleEffect.BLOCK_CRACK || this.particleEffect == ParticleEffect.BLOCK_DUST || this.particleEffect == ParticleEffect.FALLING_DUST) {
                return this.particleBlockData.getMaterial() + " " + this.particleBlockData.getData();
            } else if (this.particleEffect == ParticleEffect.ITEM_CRACK) {
                return this.particleItemData.getMaterial() + " " + this.particleItemData.getData();
            }
        }

        return "None";
    }

    /**
     * Gets the location this effect will be displayed at
     * 
     * @return The effect's location
     */
    public Location getLocation() {
        return this.location.clone();
    }

}
