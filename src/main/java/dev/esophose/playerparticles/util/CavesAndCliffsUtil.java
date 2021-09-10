package dev.esophose.playerparticles.util;

import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.particles.spawning.ParticleSpawner;
import dev.esophose.playerparticles.particles.spawning.SpigotParticleSpawner;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * For some reason the Classloader absolutely needs this to not be loaded for versions lower than 1.17.
 * It really likes complaining about org.bukkit.Vibration$Destination being missing even before it's accessed.
 * Edit: It also hates BlockDataMeta in 1.12 and below, so now that's in here too and the class is renamed to CavesAndCliffsUtil
 */
public class CavesAndCliffsUtil {

    public static void spawnParticles(ParticleEffect particleEffect, Vibration vibration, double offsetX, double offsetY, double offsetZ, int amount, Location center, boolean isLongRange, Player owner) {
        if (!particleEffect.hasProperty(ParticleEffect.ParticleProperty.VIBRATION))
            throw new ParticleSpawner.ParticleDataException("This particle effect does not require additional data");

        org.bukkit.Vibration data = vibration.getVibration() != null ? vibration.getVibration() : new org.bukkit.Vibration(center, new org.bukkit.Vibration.Destination.BlockDestination(center), vibration.getDuration());
        for (Player player : SpigotParticleSpawner.getPlayersInRange(center, isLongRange, owner))
            player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, data);
    }

    public static void setLightLevel(ItemMeta itemMeta, int level) {
        if (itemMeta instanceof BlockDataMeta) {
            BlockDataMeta blockDataMeta = (BlockDataMeta) itemMeta;
            Light lightData = (Light) Material.LIGHT.createBlockData();
            lightData.setLevel(level);
            blockDataMeta.setBlockData(lightData);
        }
    }

}
