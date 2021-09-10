package dev.esophose.playerparticles.particles.spawning;

import dev.esophose.playerparticles.manager.ConfigurationManager.Setting;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.ParticleColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.util.NMSUtil;
import dev.esophose.playerparticles.util.CavesAndCliffsUtil;
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

        for (Player player : getPlayersInRange(center, isLongRange, owner))
            player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed);
    }

    @Override
    public void display(ParticleEffect particleEffect, ParticleColor color, Location center, boolean isLongRange, Player owner, float size) {
        if (!particleEffect.hasProperty(ParticleProperty.COLORABLE))
            throw new ParticleColorException("This particle effect is not colorable");

        if (particleEffect == ParticleEffect.DUST && NMSUtil.getVersionNumber() >= 13) { // DUST uses a special data object for spawning in 1.13+
            OrdinaryColor dustColor = (OrdinaryColor) color;
            DustOptions dustOptions = new DustOptions(dustColor.toSpigot(), size > 0 ? size : Setting.DUST_SIZE.getFloat());
            for (Player player : getPlayersInRange(center, isLongRange, owner))
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), 1, 0, 0, 0, 0, dustOptions);
        } else {
            for (Player player : getPlayersInRange(center, isLongRange, owner)) {
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

        for (Player player : getPlayersInRange(center, isLongRange, owner))
            player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed, extraData);
    }

    @Override
    public void display(ParticleEffect particleEffect, ColorTransition colorTransition, double offsetX, double offsetY, double offsetZ, int amount, Location center, boolean isLongRange, Player owner, float size) {
        if (NMSUtil.getVersionNumber() < 17)
            return;

        if (!particleEffect.hasProperty(ParticleProperty.COLORABLE_TRANSITION))
            throw new ParticleDataException("This particle effect does not require additional data");

        org.bukkit.Particle.DustTransition dustTransition = new org.bukkit.Particle.DustTransition(colorTransition.getStartColor().toSpigot(), colorTransition.getEndColor().toSpigot(), size > 0 ? size : Setting.DUST_SIZE.getFloat());
        for (Player player : getPlayersInRange(center, isLongRange, owner))
            player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, dustTransition);
    }

    @Override
    public void display(ParticleEffect particleEffect, Vibration vibration, double offsetX, double offsetY, double offsetZ, int amount, Location center, boolean isLongRange, Player owner) {
        if (NMSUtil.getVersionNumber() < 17)
            return;

        CavesAndCliffsUtil.spawnParticles(particleEffect, vibration, offsetX, offsetY, offsetZ, amount, center, isLongRange, owner);
    }

}
