package com.esophose.playerparticles.particles;

import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;

import com.esophose.playerparticles.gui.PlayerParticlesGui;
import com.esophose.playerparticles.manager.ParticleManager;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleColor;
import com.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.util.ParticleUtils;

public class ParticlePair {
	
	private UUID ownerUUID;
	private int id;

	private ParticleEffect effect;
	private ParticleStyle style;
	
	private Material particleItemMaterial;
    private Material particleBlockMaterial;
    private OrdinaryColor particleColorData;
    private NoteColor particleNoteColorData;
	
	public ParticlePair(UUID ownerUUID, int id, ParticleEffect effect, ParticleStyle style, Material itemMaterial, Material blockMaterial, OrdinaryColor colorData, NoteColor noteColorData) {
		this.ownerUUID = ownerUUID;
		this.id = id;
		
		this.effect = effect;
		this.style = style;
		
		this.setParticleEffect(effect);
        this.setParticleStyle(style);
        this.setItemMaterial(itemMaterial);
        this.setBlockData(blockMaterial);
        this.setColorData(colorData);
        this.setNoteColorData(noteColorData);
	}
	
	/**
     * Sets the player's particle effect
     * 
     * @param effect The player's new particle effect
     */
    public void setParticleEffect(ParticleEffect effect) {
        if (effect == null) effect = ParticleEffect.NONE;
        this.effect = effect;
    }

    /**
     * Sets the player's particle style
     * 
     * @param style The player's new particle style
     */
    public void setParticleStyle(ParticleStyle style) {
        if (style == null) style = DefaultStyles.NONE;
        this.style = style;
    }

    /**
     * Sets the player's item material
     * 
     * @param itemMaterial The player's new item material
     */
    public void setItemMaterial(Material itemMaterial) {
        if (itemMaterial == null || itemMaterial.isBlock()) itemMaterial = ParticleUtils.closestMatchWithFallback("IRON_SHOVEL", "IRON_SPADE");
        this.particleItemMaterial = itemMaterial;
    }

    /**
     * Sets the player's block material
     * 
     * @param blockMaterial The player's new block material
     */
    public void setBlockData(Material blockMaterial) {
        if (blockMaterial == null) blockMaterial = Material.STONE;
        this.particleBlockMaterial = blockMaterial;
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
     * Gets the color the current particle effect will spawn with
     * 
     * @return Gets the ParticleColor the current particle effect will spawn with
     */
    public ParticleColor getSpawnColor() {
        if (this.effect.hasProperty(ParticleProperty.COLORABLE)) {
            if (this.effect == ParticleEffect.NOTE) {
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
     * Gets the material the current particle effect will spawn with
     * 
     * @return The Material the current particle effect requires
     */
    public Material getSpawnMaterial() {
        if (this.effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            if (this.effect == ParticleEffect.ITEM) {
                return this.particleItemMaterial;
            } else {
                return this.particleBlockMaterial;
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
        if (this.effect == ParticleEffect.BLOCK || this.effect == ParticleEffect.FALLING_DUST) {
            return this.particleBlockMaterial.toString().toLowerCase();
        } else if (this.effect == ParticleEffect.ITEM) {
            return this.particleItemMaterial.toString().toLowerCase();
        } else if (this.effect.hasProperty(ParticleProperty.COLORABLE)) {
            if (this.effect == ParticleEffect.NOTE) {
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
     * Gets a ParticlePair with the default values applied
     * 
     * @return A ParticlePair with default values
     */
    public static ParticlePair getDefault() {
    	return new ParticlePair(null, // @formatter:off
    							-1, 
    							ParticleEffect.NONE, 
    							DefaultStyles.NONE, 
    							ParticleUtils.closestMatchWithFallback("IRON_SHOVEL", "IRON_SPADE"), 
    							Material.STONE, 
    							new OrdinaryColor(0, 0, 0), 
    							new NoteColor(0)); // @formatter:on
    }
	
}
