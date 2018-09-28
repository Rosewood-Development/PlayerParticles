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

    /**
     * Get the DyeColor
     * 
     * @return The DyeColor
     */
    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    /**
     * Get the Material representing this color
     * 
     * @return The Material
     */
    public Material getMaterial() {
        return this.material;
    }

    /**
     * Get the OrdinaryColor representing this color
     * 
     * @return The OrdinaryColor
     */
    public OrdinaryColor getOrdinaryColor() {
        return this.ordinaryColor;
    }

    /**
     * Get the name of this color
     * 
     * @return The name of this color
     */
    public String getName() {
        return this.name;
    }
}
