/**
 * Copyright Esophose 2018
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import com.esophose.playerparticles.gui.PlayerParticlesGui;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.BlockData;
import com.esophose.playerparticles.particles.ParticleEffect.ItemData;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleData;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.util.ParticleUtils;

public class PPlayer {

    /**
     * The UUID of the player
     */
    private final UUID playerUUID;

    /**
     * The effect and style the player is using
     */
    private ParticleEffect particleEffect;
    private ParticleStyle particleStyle;

    /**
     * All data used to display custom particles
     */
    private ItemData particleItemData;
    private BlockData particleBlockData;
    private OrdinaryColor particleColorData;
    private NoteColor particleNoteColorData;

    /**
     * Constructs a new PPlayer
     * 
     * @param uuid The player UUID
     * @param effect The player's effect
     * @param style The player's style
     * @param itemData The player's item data
     * @param blockData The player's block data
     * @param colorData The player's color data
     * @param noteColorData The player's note color data
     */
    public PPlayer(UUID uuid, ParticleEffect effect, ParticleStyle style, ItemData itemData, BlockData blockData, OrdinaryColor colorData, NoteColor noteColorData) {
        this.playerUUID = uuid;
        this.setParticleEffect(effect);
        this.setParticleStyle(style);
        this.setItemData(itemData);
        this.setBlockData(blockData);
        this.setColorData(colorData);
        this.setNoteColorData(noteColorData);
    }

    /**
     * Gets the player's UUID
     * 
     * @return The player's UUID
     */
    public UUID getUniqueId() {
        return this.playerUUID;
    }

    /**
     * Gets the Player from their UUID
     * 
     * @return The player if they are online, null if they are offline
     */
    public Player getPlayer() {
        return Bukkit.getPlayer(this.playerUUID);
    }

    /**
     * Gets the player's particle effect
     * 
     * @return The player's particle effect
     */
    public ParticleEffect getParticleEffect() {
        return this.particleEffect;
    }

    /**
     * Gets the player's particle style
     * 
     * @return The player's particle style
     */
    public ParticleStyle getParticleStyle() {
        if (this.particleStyle == null) return DefaultStyles.NONE;
        return this.particleStyle;
    }

    /**
     * Gets the player's item data
     * 
     * @return The player's item data
     */
    public ItemData getItemData() {
        return this.particleItemData;
    }

    /**
     * Gets the player's block data
     * 
     * @return The player's block data
     */
    public BlockData getBlockData() {
        return this.particleBlockData;
    }

    /**
     * Gets the player's color data
     * 
     * @return The player's color data
     */
    public OrdinaryColor getColorData() {
        return this.particleColorData;
    }

    /**
     * Gets the player's note color data
     * 
     * @return The player's note color data
     */
    public NoteColor getNoteColorData() {
        return this.particleNoteColorData;
    }

    /**
     * Sets the player's particle effect
     * 
     * @param effect The player's new particle effect
     */
    public void setParticleEffect(ParticleEffect effect) {
        if (effect == null) effect = ParticleEffect.NONE;
        this.particleEffect = effect;
    }

    /**
     * Sets the player's particle style
     * 
     * @param style The player's new particle style
     */
    public void setParticleStyle(ParticleStyle style) {
        if (style == null) style = DefaultStyles.NONE;
        this.particleStyle = style;
    }

    /**
     * Sets the player's item data
     * 
     * @param itemData The player's new item data
     */
    public void setItemData(ItemData itemData) {
        if (itemData == null) itemData = new ItemData(ParticleUtils.closestMatchWithFallback("IRON_SHOVEL", "IRON_SPADE"), (byte)0);
        this.particleItemData = itemData;
    }

    /**
     * Sets the player's block data
     * 
     * @param blockData The player's new block data
     */
    public void setBlockData(BlockData blockData) {
        if (blockData == null) blockData = new BlockData(Material.STONE, (byte) 0);
        this.particleBlockData = blockData;
    }

    /**
     * Sets the player's color data
     * 
     * @param colorData The player's new color data
     */
    public void setColorData(OrdinaryColor colorData) {
        if (colorData == null) colorData = new OrdinaryColor(0, 0, 0);
        this.particleColorData = colorData;
    }

    /**
     * Sets the player's note color data
     * 
     * @param noteColorData The player's new note color data
     */
    public void setNoteColorData(NoteColor noteColorData) {
        if (noteColorData == null) noteColorData = new NoteColor(0);
        this.particleNoteColorData = noteColorData;
    }

    /**
     * Gets the data the current particle effect will spawn with
     * 
     * @return The ParticleData the current particle effect requires
     */
    public ParticleData getParticleSpawnData() {
        if (this.particleEffect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
            if (this.particleEffect == ParticleEffect.ITEM_CRACK) {
                return this.particleItemData;
            } else {
                return particleBlockData;
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
     * Gets the current particle data as a string
     * 
     * @return The particle data in a human-readable string
     */
    public String getParticleDataString() {
        if (this.particleEffect == ParticleEffect.BLOCK_CRACK || this.particleEffect == ParticleEffect.BLOCK_DUST || this.particleEffect == ParticleEffect.FALLING_DUST) {
            return particleBlockData.getMaterial().toString().toLowerCase() + ":" + particleBlockData.getData();
        } else if (this.particleEffect == ParticleEffect.ITEM_CRACK) {
            return particleItemData.getMaterial().toString().toLowerCase() + ":" + particleItemData.getData();
        } else if (this.particleEffect.hasProperty(ParticleProperty.COLORABLE)) {
            if (this.particleEffect == ParticleEffect.NOTE) {
                if (this.particleNoteColorData.getValueX() * 24 == 99) {
                    return PlayerParticlesGui.rainbowName;
                }
                return "note #" + (int) (this.particleNoteColorData.getValueX() * 24);
            } else {
                if (this.particleColorData.getRed() == 999 && this.particleColorData.getGreen() == 999 && this.particleColorData.getBlue() == 999) {
                    return PlayerParticlesGui.rainbowName;
                } else {
                    return ChatColor.RED + "" + this.particleColorData.getRed() + " " + ChatColor.GREEN + this.particleColorData.getGreen() + " " + ChatColor.AQUA + this.particleColorData.getBlue();
                }
            }
        }
        return "none";
    }

    /**
     * Gets a default PPlayer
     * Used for when a new PPlayer is being created
     * 
     * @param playerUUID The player's UUID
     * @return A default PPlayer
     */
    public static PPlayer getNewPPlayer(UUID playerUUID) {
        ParticleEffect particleEffect = ParticleEffect.NONE;
        ParticleStyle particleStyle = DefaultStyles.NONE;
        ItemData particleItemData = new ItemData(ParticleUtils.closestMatchWithFallback("IRON_SHOVEL", "IRON_SPADE"), (byte) 0);
        BlockData particleBlockData = new BlockData(Material.STONE, (byte) 0);
        OrdinaryColor particleColorData = new OrdinaryColor(0, 0, 0);
        NoteColor particleNoteColorData = new NoteColor(0);

        return new PPlayer(playerUUID, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData);
    }

}
