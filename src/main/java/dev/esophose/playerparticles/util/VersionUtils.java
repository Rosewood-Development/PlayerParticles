package dev.esophose.playerparticles.util;

import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.particles.spawning.SpigotParticleSpawner;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle.Spell;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.BlockDataMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * For some reason the Classloader absolutely needs this to not be loaded for versions lower than 1.17.
 * It really likes complaining about org.bukkit.Vibration$Destination being missing even before it's accessed.
 * Edit: It also hates BlockDataMeta in 1.12 and below, so now that's in here too and the class is renamed to CavesAndCliffsUtil
 * Edit: Back again in 1.21.10 where Spell was added for effect and instant_effect, adding it here just in case and renamed to VersionUtils.
 */
public class VersionUtils {

    public static void spawnVibrationParticles(ParticleEffect particleEffect, Vibration vibration, double offsetX, double offsetY, double offsetZ, int amount, Location center, boolean isLongRange, Player owner) {
        org.bukkit.Vibration data = vibration.getVibration() != null ? vibration.getVibration() : new org.bukkit.Vibration(center, new org.bukkit.Vibration.Destination.BlockDestination(center), vibration.getDuration());
        for (Player player : SpigotParticleSpawner.getPlayersInRange(center, isLongRange, owner))
            player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, data);
    }

    public static void spawnSpellParticles(ParticleEffect particleEffect, Location center, OrdinaryColor ordinaryColor, boolean isLongRange, Player owner) {
        Color color = ordinaryColor.toSpigot();
        Spell spell = new Spell(color, 1.0F);
        for (Player player : SpigotParticleSpawner.getPlayersInRange(center, isLongRange, owner))
            player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), 1, 0, 0, 0, 0, spell);
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
