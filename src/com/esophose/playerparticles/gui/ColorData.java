package com.esophose.playerparticles.gui;

import org.bukkit.DyeColor;
import org.bukkit.Material;

import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;

public class ColorData {
	private DyeColor dyeColor;
	private Material material;
	private OrdinaryColor ordinaryColor;
	private String name;
	
	public ColorData(DyeColor dyeColor, Material material, OrdinaryColor ordinaryColor, String name) {
		this.dyeColor = dyeColor;
		this.material = material;
		this.ordinaryColor = ordinaryColor;
		this.name = name;
	}
	
	public DyeColor getDyeColor() {
		return this.dyeColor;
	}
	
	public Material getMaterial() {
		return this.material;
	}
	
	public OrdinaryColor getOrdinaryColor() {
		return this.ordinaryColor;
	}
	
	public String getName() {
		return this.name;
	}
}