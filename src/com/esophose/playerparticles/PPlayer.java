package com.esophose.playerparticles;

import java.util.UUID;

import org.bukkit.Material;

import com.esophose.playerparticles.library.ParticleEffect;
import com.esophose.playerparticles.library.ParticleEffect.BlockData;
import com.esophose.playerparticles.library.ParticleEffect.ItemData;
import com.esophose.playerparticles.library.ParticleEffect.NoteColor;
import com.esophose.playerparticles.library.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.library.ParticleEffect.ParticleColor;
import com.esophose.playerparticles.library.ParticleEffect.ParticleData;
import com.esophose.playerparticles.library.ParticleEffect.ParticleProperty;
import com.esophose.playerparticles.styles.DefaultStyles;
import com.esophose.playerparticles.styles.api.ParticleStyle;

public class PPlayer {

	private final UUID playerUUID;

	private ParticleEffect particleEffect;
	private ParticleStyle particleStyle;

	private ItemData particleItemData;
	private BlockData particleBlockData;
	private OrdinaryColor particleColorData;
	private NoteColor particleNoteColorData;

	public PPlayer(UUID uuid, ParticleEffect effect, ParticleStyle style, ItemData itemData, BlockData blockData, OrdinaryColor colorData, NoteColor noteColorData) {
		this.playerUUID = uuid;
		this.particleEffect = effect;
		this.particleStyle = style;
		this.particleItemData = itemData;
		this.particleBlockData = blockData;
		this.particleColorData = colorData;
		this.particleNoteColorData = noteColorData;
	}
	
	public UUID getUniqueId() {
		return this.playerUUID;
	}

	public ParticleEffect getParticleEffect() {
		return this.particleEffect;
	}

	public ParticleStyle getParticleStyle() {
		return this.particleStyle;
	}

	public ItemData getItemData() {
		return this.particleItemData;
	}

	public BlockData getBlockData() {
		return this.particleBlockData;
	}

	public OrdinaryColor getColorData() {
		return this.particleColorData;
	}

	public NoteColor getNoteColorData() {
		return this.particleNoteColorData;
	}

	public void setParticleEffect(ParticleEffect effect) {
		this.particleEffect = effect;
	}

	public void setParticleStyle(ParticleStyle style) {
		this.particleStyle = style;
	}
	
	public void setItemData(ItemData itemData) {
		this.particleItemData = itemData;
	}
	
	public void setBlockData(BlockData blockData) {
		this.particleBlockData = blockData;
	}
	
	public void setColorData(OrdinaryColor colorData) {
		this.particleColorData = colorData;
	}
	
	public void setNoteColorData(NoteColor noteColorData) {
		this.particleNoteColorData = noteColorData;
	}

	public ParticleData getParticleSpawnData() {
		if (particleEffect.hasProperty(ParticleProperty.REQUIRES_DATA)) {
			if (particleEffect == ParticleEffect.BLOCK_CRACK || particleEffect == ParticleEffect.BLOCK_DUST || particleEffect == ParticleEffect.FALLING_DUST) {
				return particleBlockData;
			} else if (particleEffect == ParticleEffect.ITEM_CRACK) {
				return particleItemData;
			}
		}
		return null;
	}

	public ParticleColor getParticleSpawnColor() {
		if (particleEffect.hasProperty(ParticleProperty.COLORABLE)) {
			if (particleEffect == ParticleEffect.NOTE) {
				return particleNoteColorData;
			} else return particleColorData;
		}
		return null;
	}

	public static PPlayer getNewPPlayer(UUID playerUUID) {
		ParticleEffect particleEffect = ParticleEffect.NONE;
		ParticleStyle particleStyle = DefaultStyles.NONE;
		ItemData particleItemData = new ItemData(Material.IRON_SPADE, (byte) 0);
		BlockData particleBlockData = new BlockData(Material.STONE, (byte) 0);
		OrdinaryColor particleColorData = new OrdinaryColor(0, 0, 0);
		NoteColor particleNoteColorData = new NoteColor(0);

		return new PPlayer(playerUUID, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData);
	}

}
