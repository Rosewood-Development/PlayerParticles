package dev.esophose.playerparticles.particles.spawning;

import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.ParticleColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.particles.spawning.reflective.ReflectionUtils;
import dev.esophose.playerparticles.particles.spawning.reflective.ReflectionUtils.PackageType;
import dev.esophose.playerparticles.particles.spawning.reflective.ReflectiveParticleEffectMapping;
import dev.esophose.playerparticles.util.NMSUtil;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class ReflectiveParticleSpawner extends ParticleSpawner {

    @Override
    public void display(ParticleEffect particleEffect, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner) {
        if (particleEffect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA))
            throw new ParticleDataException("This particle effect requires additional data");

        List<Player> players = getPlayersInRange(center, isLongRange, owner);
        if (players.isEmpty())
            return;

        new ParticlePacket(particleEffect, offsetX, offsetY, offsetZ, speed, amount, true, null).sendTo(center, players);
    }

    @Override
    public void display(ParticleEffect particleEffect, ParticleColor color, Location center, boolean isLongRange, Player owner, float size) {
        if (!particleEffect.hasProperty(ParticleProperty.COLORABLE))
            throw new ParticleColorException("This particle effect is not colorable");

        List<Player> players = getPlayersInRange(center, isLongRange, owner);
        if (players.isEmpty())
            return;

        new ParticlePacket(particleEffect, color, true).sendTo(center, getPlayersInRange(center, isLongRange, owner));
    }

    @Override
    public void display(ParticleEffect particleEffect, Material spawnMaterial, double offsetX, double offsetY, double offsetZ, double speed, int amount, Location center, boolean isLongRange, Player owner) {
        if (!particleEffect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA))
            throw new ParticleDataException("This particle effect does not require additional data");

        List<Player> players = getPlayersInRange(center, isLongRange, owner);
        if (players.isEmpty())
            return;

        new ParticlePacket(particleEffect, offsetX, offsetY, offsetZ, speed, amount, true, spawnMaterial).sendTo(center, getPlayersInRange(center, isLongRange, owner));
    }

    @Override
    public void display(ParticleEffect particleEffect, ColorTransition colorTransition, double offsetX, double offsetY, double offsetZ, int amount, Location center, boolean isLongRange, Player owner,float size) {
        throw new IllegalStateException("This method is unavailable for legacy versions");
    }

    @Override
    public void display(ParticleEffect particleEffect, Vibration vibration, double offsetX, double offsetY, double offsetZ, int amount, Location center, boolean isLongRange, Player owner) {
        throw new IllegalStateException("This method is unavailable for legacy versions");
    }

    /**
     * Represents a particle effect packet with all attributes which is used for
     * sending packets to the players
     * <p>
     * This class is part of the <b>ParticleEffect Library</b> and follows the
     * same usage conditions
     *
     * @author DarkBlade12, Esophose
     * @since 1.5
     */
    public static final class ParticlePacket {
        private static Class<?> enumParticle;
        private static Constructor<?> packetConstructor;
        private static Method getHandle;
        private static Field playerConnection;
        private static Method sendPacket;
        static {
            try {
                if (NMSUtil.getVersionNumber() > 7) {
                    enumParticle = PackageType.MINECRAFT_SERVER.getClass("EnumParticle");
                }
                Class<?> packetClass = PackageType.MINECRAFT_SERVER.getClass(NMSUtil.getVersionNumber() < 7 ? "Packet63WorldParticles" : "PacketPlayOutWorldParticles");
                packetConstructor = ReflectionUtils.getConstructor(packetClass);
                getHandle = ReflectionUtils.getMethod("CraftPlayer", PackageType.CRAFTBUKKIT_ENTITY, "getHandle");
                playerConnection = ReflectionUtils.getField("EntityPlayer", PackageType.MINECRAFT_SERVER, false, "playerConnection");
                sendPacket = ReflectionUtils.getMethod(playerConnection.getType(), "sendPacket", PackageType.MINECRAFT_SERVER.getClass("Packet"));
            } catch (ReflectiveOperationException e) {
                e.printStackTrace();
            }
        }

        private final ParticleEffect effect;
        private float offsetX;
        private final float offsetY;
        private final float offsetZ;
        private final float speed;
        private final int amount;
        private final boolean longDistance;
        private final Material data;
        private Object packet;

        /**
         * Construct a new particle packet
         *
         * @param effect Particle effect
         * @param offsetX Maximum distance particles can fly away from the center on the x-axis
         * @param offsetY Maximum distance particles can fly away from the center on the y-axis
         * @param offsetZ Maximum distance particles can fly away from the center on the z-axis
         * @param speed Display speed of the particles
         * @param amount Amount of particles
         * @param longDistance Indicates whether the maximum distance is increased from 256 to 65536
         * @param data Data of the effect
         * @throws IllegalArgumentException If the speed or amount is lower than 0
         */
        public ParticlePacket(ParticleEffect effect, double offsetX, double offsetY, double offsetZ, double speed, int amount, boolean longDistance, Material data) throws IllegalArgumentException {
            if (speed < 0)
                throw new IllegalArgumentException("The speed is lower than 0");

            if (amount < 0)
                throw new IllegalArgumentException("The amount is lower than 0");

            this.effect = effect;
            this.offsetX = (float) offsetX;
            this.offsetY = (float) offsetY;
            this.offsetZ = (float) offsetZ;
            this.speed = (float) speed;
            this.amount = amount;
            this.longDistance = longDistance;
            this.data = data;
        }

        /**
         * Construct a new particle packet of a single colored particle
         *
         * @param effect Particle effect
         * @param color Color of the particle
         * @param longDistance Indicates whether the maximum distance is increased from 256 to 65536
         */
        public ParticlePacket(ParticleEffect effect, ParticleColor color, boolean longDistance) {
            this(effect, color.getValueX(), color.getValueY(), color.getValueZ(), 1, 0, longDistance, null);

            if (effect == ParticleEffect.DUST && color instanceof OrdinaryColor && ((OrdinaryColor) color).getRed() == 0)
                this.offsetX = Float.MIN_NORMAL;
        }

        /**
         * Initializes {@link #packet} with all set values
         *
         * @param center Center location of the effect
         * @throws PacketInstantiationException If instantion fails due to an unknown error
         */
        private void initializePacket(Location center) throws PacketInstantiationException {
            if (this.packet != null) {
                return;
            }
            try {
                this.packet = packetConstructor.newInstance();
                if (NMSUtil.getVersionNumber() < 8) {
                    String name = ReflectiveParticleEffectMapping.valueOf(this.effect.name()).getName();
                    if (this.data != null) {
                        name += getPacketDataString(this.data);
                    }
                    ReflectionUtils.setValue(this.packet, true, "a", name);
                } else {
                    ReflectionUtils.setValue(this.packet, true, "a", enumParticle.getEnumConstants()[ReflectiveParticleEffectMapping.valueOf(this.effect.name()).getId()]);
                    ReflectionUtils.setValue(this.packet, true, "j", this.longDistance);
                    if (this.data != null) {
                        int[] packetData = getPacketData(this.data);
                        ReflectionUtils.setValue(this.packet, true, "k", this.effect == ParticleEffect.ITEM ? packetData : new int[] { packetData[0] | (packetData[1] << 12) });
                    }
                }
                ReflectionUtils.setValue(this.packet, true, "b", (float) center.getX());
                ReflectionUtils.setValue(this.packet, true, "c", (float) center.getY());
                ReflectionUtils.setValue(this.packet, true, "d", (float) center.getZ());
                ReflectionUtils.setValue(this.packet, true, "e", this.offsetX);
                ReflectionUtils.setValue(this.packet, true, "f", this.offsetY);
                ReflectionUtils.setValue(this.packet, true, "g", this.offsetZ);
                ReflectionUtils.setValue(this.packet, true, "h", this.speed);
                ReflectionUtils.setValue(this.packet, true, "i", this.amount);
            } catch (Exception exception) {
                throw new PacketInstantiationException("Packet instantiation failed", exception);
            }
        }

        /**
         * Sends the packet to a single player and caches it
         *
         * @param center Center location of the effect
         * @param player Receiver of the packet
         * @throws PacketInstantiationException If instantion fails due to an unknown error
         * @throws PacketSendingException If sending fails due to an unknown error
         * @see #initializePacket(Location)
         */
        public void sendTo(Location center, Player player) throws PacketInstantiationException, PacketSendingException {
            this.initializePacket(center);
            try {
                sendPacket.invoke(playerConnection.get(getHandle.invoke(player)), this.packet);
            } catch (Exception exception) {
                throw new PacketSendingException("Failed to send the packet to player '" + player.getName() + "'", exception);
            }
        }

        /**
         * Sends the packet to all players in the list
         *
         * @param center Center location of the effect
         * @param players Receivers of the packet
         * @throws IllegalArgumentException If the player list is empty
         * @see #sendTo(Location center, Player player)
         */
        public void sendTo(Location center, List<Player> players) throws IllegalArgumentException {
            if (players.isEmpty()) {
                throw new IllegalArgumentException("The player list is empty");
            }
            for (Player player : players) {
                this.sendTo(center, player);
            }
        }

        /**
         * Sends the packet to all players in a certain range
         *
         * @param center Center location of the effect
         * @param range Range in which players will receive the packet (Maximum range for particles is usually 16, but it can differ for some types)
         * @throws IllegalArgumentException If the range is lower than 1
         * @see #sendTo(Location center, Player player)
         */
        public void sendTo(Location center, double range) throws IllegalArgumentException {
            if (range < 1) {
                throw new IllegalArgumentException("The range is lower than 1");
            }
            String worldName = center.getWorld().getName();
            double squared = range * range;
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (!player.getWorld().getName().equals(worldName) || player.getLocation().distanceSquared(center) > squared) {
                    continue;
                }
                this.sendTo(center, player);
            }
        }

        private static String getPacketDataString(Material data) {
            int[] packetData = getPacketData(data);
            return "_" + packetData[0] + "_" + packetData[1];
        }

        @SuppressWarnings("deprecation")
        private static int[] getPacketData(Material data) {
            return new int[] { data.getId(), 0 };
        }

        /**
         * Represents a runtime exception that is thrown if packet instantiation fails
         * <p>
         * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
         * </p>
         *
         * @author DarkBlade12
         * @since 1.4
         */
        private static final class PacketInstantiationException extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            /**
             * Construct a new packet instantiation exception
             *
             * @param message Message that will be logged
             * @param cause Cause of the exception
             */
            public PacketInstantiationException(String message, Throwable cause) {
                super(message, cause);
            }
        }

        /**
         * Represents a runtime exception that is thrown if packet sending fails
         * <p>
         * This class is part of the <b>ParticleEffect Library</b> and follows the same usage conditions
         * </p>
         *
         * @author DarkBlade12
         * @since 1.4
         */
        private static final class PacketSendingException extends RuntimeException {
            private static final long serialVersionUID = 3203085387160737484L;

            /**
             * Construct a new packet sending exception
             *
             * @param message Message that will be logged
             * @param cause Cause of the exception
             */
            public PacketSendingException(String message, Throwable cause) {
                super(message, cause);
            }
        }
    }

}
