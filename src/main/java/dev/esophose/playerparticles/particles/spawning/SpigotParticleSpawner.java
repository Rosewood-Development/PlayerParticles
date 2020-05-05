package dev.esophose.playerparticles.particles.spawning;

import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.ParticleColor;
import dev.esophose.playerparticles.util.NMSUtil;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle.DustOptions;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

public class SpigotParticleSpawner extends ParticleSpawner {

    @Override
    public void display(ParticleEffect particleEffect, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner) {
        if (particleEffect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA))
            throw new ParticleDataException("This particle effect requires additional data");

        for (Player player : this.getPlayersInRange(center, isLongRange, owner))
            player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed);
    }

    @Override
    public void display(ParticleEffect particleEffect, ParticleColor color, Location center, boolean isLongRange, Player owner) {
        if (!particleEffect.hasProperty(ParticleProperty.COLORABLE))
            throw new ParticleColorException("This particle effect is not colorable");

        if (particleEffect == ParticleEffect.DUST && NMSUtil.getVersionNumber() >= 13) { // DUST uses a special data object for spawning in 1.13+
            OrdinaryColor dustColor = (OrdinaryColor) color;
            DustOptions dustOptions = new DustOptions(Color.fromRGB(dustColor.getRed(), dustColor.getGreen(), dustColor.getBlue()), Setting.DUST_SIZE.getFloat());
            for (Player player : this.getPlayersInRange(center, isLongRange, owner))
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), 1, 0, 0, 0, 0, dustOptions);
        } else {
            for (Player player : this.getPlayersInRange(center, isLongRange, owner)) {
                // Minecraft clients require that you pass a non-zero value if the Red value should be zero
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), 0, particleEffect == ParticleEffect.DUST && color.getValueX() == 0 ? Float.MIN_VALUE : color.getValueX(), color.getValueY(), color.getValueZ(), 1);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void display(ParticleEffect particleEffect, Material spawnMaterial, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner) {
        if (!particleEffect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA))
            throw new ParticleDataException("This particle effect does not require additional data");

        Object extraData = null;
        if (particleEffect.getSpigotEnum().getDataType().getTypeName().equals("org.bukkit.block.data.BlockData")) {
            extraData = spawnMaterial.createBlockData();
        } else if (particleEffect.getSpigotEnum().getDataType() == ItemStack.class) {
            extraData = new ItemStack(spawnMaterial);
        } else if (particleEffect.getSpigotEnum().getDataType() == MaterialData.class) {
            extraData = new MaterialData(spawnMaterial); // Deprecated, only used in versions < 1.13
        }

        for (Player player : this.getPlayersInRange(center, isLongRange, owner))
            player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed, extraData);
    }

}
