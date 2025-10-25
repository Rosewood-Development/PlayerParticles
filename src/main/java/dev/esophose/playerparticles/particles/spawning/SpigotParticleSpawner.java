package dev.esophose.playerparticles.particles.spawning;

import dev.esophose.playerparticles.config.Settings;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleDataType;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.ParticleColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.util.VersionUtils;
import dev.rosewood.rosegarden.utils.NMSUtil;
import java.lang.reflect.Constructor;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle.DustOptions;
import org.bukkit.Particle.Trail;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;
import org.bukkit.util.Vector;

public class SpigotParticleSpawner extends ParticleSpawner {

    @Override
    public void display(ParticleEffect particleEffect, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner) {
        if (particleEffect.getDataType() != ParticleDataType.NONE)
            throw new ParticleDataException("This particle effect requires additional data");

        if (particleEffect == ParticleEffect.SCULK_CHARGE) {
            for (Player player : getPlayersInRange(center, isLongRange, owner))
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed, 0F);
        } else if (particleEffect == ParticleEffect.SHRIEK) {
            for (Player player : getPlayersInRange(center, isLongRange, owner))
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed, 0);
        } else if ((NMSUtil.getVersionNumber() > 21 || (NMSUtil.getVersionNumber() == 21 && NMSUtil.getMinorVersionNumber() >= 9) && particleEffect == ParticleEffect.DRAGON_BREATH)) {
            for (Player player : getPlayersInRange(center, isLongRange, owner))
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed, 1.0F);
        } else {
            for (Player player : getPlayersInRange(center, isLongRange, owner))
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, speed);
        }
    }

    @Override
    public void display(ParticleEffect particleEffect, ParticleColor color, Location center, boolean isLongRange, Player owner, float size) {
        if (particleEffect.getDataType() != ParticleDataType.COLORABLE && particleEffect.getDataType() != ParticleDataType.COLORABLE_TRANSPARENCY)
            throw new ParticleColorException("This particle effect is not colorable");

        if (particleEffect == ParticleEffect.DUST && NMSUtil.getVersionNumber() >= 13) { // DUST uses a special data object for spawning in 1.13+
            OrdinaryColor dustColor = (OrdinaryColor) color;
            DustOptions dustOptions = new DustOptions(dustColor.toSpigot(), size > 0 ? size : Settings.DUST_SIZE.get());
            for (Player player : getPlayersInRange(center, isLongRange, owner))
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), 1, 0, 0, 0, 0, dustOptions);
        } else if (particleEffect.getDataType() == ParticleDataType.COLORABLE_TRANSPARENCY) {
            OrdinaryColor ordinaryColor = (OrdinaryColor) color;
            for (Player player : getPlayersInRange(center, isLongRange, owner))
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), 1, 0, 0, 0, 0, ordinaryColor.toSpigot());
        } else if (particleEffect == ParticleEffect.TRAIL) {
            OrdinaryColor ordinaryColor = (OrdinaryColor) color;
            Location target;
            if (owner != null && Settings.TRAIL_MOVE_TO_PLAYER.get()) {
                target = owner.getLocation().clone().add(0, 1, 0);
            } else {
                target = center.clone().add(0, -1, 0).add(Vector.getRandom().subtract(new Vector(0.5, 0.5, 0.5)).multiply(0.2));
            }
            Object trailData = createTrailData(target, ordinaryColor.toSpigot());
            for (Player player : getPlayersInRange(center, isLongRange, owner))
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), 1, 0, 0, 0, 0, trailData, true);
        } if (particleEffect == ParticleEffect.INSTANT_EFFECT && NMSUtil.getVersionNumber() > 21 || (NMSUtil.getVersionNumber() == 21 && NMSUtil.getMinorVersionNumber() >= 9)) {
            VersionUtils.spawnSpellParticles(particleEffect, center, (OrdinaryColor) color, isLongRange, owner); // INSTANT_EFFECT uses a Spell object for spawning in 1.21.9+
        } else if (particleEffect == ParticleEffect.ENTITY_EFFECT && (NMSUtil.getVersionNumber() > 20 || (NMSUtil.getVersionNumber() == 20 && NMSUtil.getMinorVersionNumber() >= 5))) {
            for (Player player : getPlayersInRange(center, isLongRange, owner)) // ENTITY_EFFECT uses a Color object for spawning in 1.20.5+
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), 0, 0, 0, 0, 1, color.toSpigot());
        } else {
            for (Player player : getPlayersInRange(center, isLongRange, owner)) {
                // Minecraft clients require that you pass a non-zero value if the red value should be exactly zero for dust
                player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), 0, particleEffect == ParticleEffect.DUST && color.getValueX() == 0 ? Float.MIN_VALUE : color.getValueX(), color.getValueY(), color.getValueZ(), 1);
            }
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public void display(ParticleEffect particleEffect, Material spawnMaterial, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner) {
        if (particleEffect.getDataType() != ParticleDataType.BLOCK && particleEffect.getDataType() != ParticleDataType.ITEM)
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

        if (particleEffect.getDataType() != ParticleDataType.COLORABLE_TRANSITION)
            throw new ParticleDataException("This particle effect does not require additional data");

        org.bukkit.Particle.DustTransition dustTransition = new org.bukkit.Particle.DustTransition(colorTransition.getStartColor().toSpigot(), colorTransition.getEndColor().toSpigot(), size > 0 ? size : Settings.DUST_SIZE.get());
        for (Player player : getPlayersInRange(center, isLongRange, owner))
            player.spawnParticle(particleEffect.getSpigotEnum(), center.getX(), center.getY(), center.getZ(), amount, offsetX, offsetY, offsetZ, dustTransition);
    }

    @Override
    public void display(ParticleEffect particleEffect, Vibration vibration, double offsetX, double offsetY, double offsetZ, int amount, Location center, boolean isLongRange, Player owner) {
        if (NMSUtil.getVersionNumber() < 17)
            return;

        if (particleEffect.getDataType() != ParticleEffect.ParticleDataType.VIBRATION)
            throw new ParticleSpawner.ParticleDataException("This particle effect does not require additional data");

        VersionUtils.spawnVibrationParticles(particleEffect, vibration, offsetX, offsetY, offsetZ, amount, center, isLongRange, owner);
    }

    private static Constructor<?> trailDataConstructor;
    private static Object createTrailData(Location location, Color color) {
        if (NMSUtil.getVersionNumber() > 21 || (NMSUtil.getVersionNumber() == 21 && NMSUtil.getMinorVersionNumber() >= 4)) {
            return new Trail(location, color, ThreadLocalRandom.current().nextInt(15, 40));
        } else {
            try {
                if (trailDataConstructor == null)
                    trailDataConstructor = Class.forName("org.bukkit.Particle$TargetColor").getConstructor(Location.class, Color.class);
                return trailDataConstructor.newInstance(location, color);
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
                throw new RuntimeException("The Trail effect is not supported on this server version", e);
            }
        }
    }

}
