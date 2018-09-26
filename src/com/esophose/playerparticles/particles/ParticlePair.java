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
	
	private Material itemMaterial;
    private Material blockMaterial;
    private OrdinaryColor color;
    private NoteColor noteColor;
	
	public ParticlePair(UUID ownerUUID, int id, ParticleEffect effect, ParticleStyle style, Material itemMaterial, Material blockMaterial, OrdinaryColor color, NoteColor noteColor) {
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
	}
	
	/**
     * Sets the player's particle effect
     * 
     * @param effect The player's new particle effect
     */
    public void setEffect(ParticleEffect effect) {
        if (effect == null) effect = ParticleEffect.NONE;
        this.effect = effect;
    }

    /**
     * Sets the player's particle style
     * 
     * @param style The player's new particle style
     */
    public void setStyle(ParticleStyle style) {
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
        this.itemMaterial = itemMaterial;
    }

    /**
     * Sets the player's block material
     * 
     * @param blockMaterial The player's new block material
     */
    public void setBlockMaterial(Material blockMaterial) {
        if (blockMaterial == null) blockMaterial = Material.STONE;
        this.blockMaterial = blockMaterial;
    }

    /**
     * Sets the player's color data
     * 
     * @param colorData The player's new color data
     */
    public void setColor(OrdinaryColor colorData) {
        if (colorData == null) colorData = new OrdinaryColor(0, 0, 0);
        this.color = colorData;
    }

    /**
     * Sets the player's note color data
     * 
     * @param noteColorData The player's new note color data
     */
    public void setNoteColor(NoteColor noteColorData) {
        if (noteColorData == null) noteColorData = new NoteColor(0);
        this.noteColor = noteColorData;
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
     * Gets the color the current particle effect will spawn with
     * 
     * @return Gets the ParticleColor the current particle effect will spawn with
     */
    public ParticleColor getSpawnColor() {
        if (this.effect.hasProperty(ParticleProperty.COLORABLE)) {
            if (this.effect == ParticleEffect.NOTE) {
                if (this.noteColor.getValueX() * 24 == 99) {
                    return ParticleManager.getRainbowNoteParticleColor();
                }
                return this.noteColor;
            } else {
                if (this.color.getRed() == 999 && this.color.getGreen() == 999 && this.color.getBlue() == 999) {
                    return ParticleManager.getRainbowParticleColor();
                } else {
                    return this.color;
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
                return this.itemMaterial;
            } else {
                return this.blockMaterial;
            }
        }
        return null;
    }

    /**
     * Gets the current particle data as a string
     * 
     * @return The particle data in a human-readable string
     */
    public String getDataString() {
        if (this.effect == ParticleEffect.BLOCK || this.effect == ParticleEffect.FALLING_DUST) {
            return this.blockMaterial.toString().toLowerCase();
        } else if (this.effect == ParticleEffect.ITEM) {
            return this.itemMaterial.toString().toLowerCase();
        } else if (this.effect.hasProperty(ParticleProperty.COLORABLE)) {
            if (this.effect == ParticleEffect.NOTE) {
                if (this.noteColor.getValueX() * 24 == 99) {
                    return PlayerParticlesGui.rainbowName;
                }
                return "note #" + (int) (this.noteColor.getValueX() * 24);
            } else {
                if (this.color.getRed() == 999 && this.color.getGreen() == 999 && this.color.getBlue() == 999) {
                    return PlayerParticlesGui.rainbowName;
                } else {
                    return ChatColor.RED + "" + this.color.getRed() + " " + ChatColor.GREEN + this.color.getGreen() + " " + ChatColor.AQUA + this.color.getBlue();
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
