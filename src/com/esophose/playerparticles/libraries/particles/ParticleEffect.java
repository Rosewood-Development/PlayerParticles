/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2014 Maxim Roncace
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.esophose.playerparticles.libraries.particles;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

/**
 * Particle effects utility library
 * @author Maxim Roncace
 * @version 0.1.0
 */

/**
 * Slightly modified to suit the needs
 * of the plugin
 * @author Esophose
 */

@SuppressWarnings({ "unchecked", "rawtypes" })
public class ParticleEffect {

	private static Class<?> packetClass = null;
	private static Constructor<?> packetConstructor = null;
	private static Field[] fields = null;
	private static boolean netty = true;
	private static Field player_connection = null;
	private static Method player_sendPacket = null;
	private static HashMap<Class<? extends Entity>, Method> handles = new HashMap<Class<? extends Entity>, Method>();

	private static boolean newParticlePacketConstructor = false;
	private static Class<Enum> enumParticle = null;

	private ParticleType type;
	private double speed;
	private int count;
	private double offsetX, offsetY, offsetZ;

	private static boolean compatible = true;

	static {
		String vString = getVersion().replace("v", "");
		double v = 0;
		if (!vString.isEmpty()){
			String[] array = vString.split("_");
			v = Integer.parseInt(array[1]); // Take the second value to fix MC 1.10 looking like 1.1 and failing to be greater than 1.7
		}
		try {
			if (v < 7) { // Maintain support for versions below 1.6 (Probably doesn't even work)
				netty = false;
				packetClass = getNmsClass("Packet63WorldParticles");
				packetConstructor = packetClass.getConstructor();
				fields = packetClass.getDeclaredFields();
			} else { // Use the greater than 1.7 particle packet class
				packetClass = getNmsClass("PacketPlayOutWorldParticles");
				if (v < 8){
					packetConstructor = packetClass.getConstructor(String.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class);
				} else { // use the new constructor for 1.8
					newParticlePacketConstructor = true;
					enumParticle = (Class<Enum>)getNmsClass("EnumParticle");
					packetConstructor = packetClass.getDeclaredConstructor(enumParticle, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int[].class);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Bukkit.getLogger().severe("[PlayerParticles] Failed to initialize NMS components! This occurs if you are running the plugin on a version of Minecraft that is not supported!");
			compatible = false;
		}
	}

	/**
	 * Constructs a new particle effect for use.
	 * <p>
	 *     Note: different values for speed and radius may hav;e different effects
	 *     depending on the particle's type.
	 * </p>
	 * @param type the particle type
	 * @param speed the speed of the particles
	 * @param count the number of particles to spawn
	 * @param radius the radius of the particles
	 */
	public ParticleEffect(ParticleType type, double offsetX, double offsetY, double offsetZ, double speed, int count){
		this.type = type;
		this.speed = speed;
		this.count = count;
		this.offsetX = offsetX;
		this.offsetY = offsetY;
		this.offsetZ = offsetZ;
	}
	
	/**
	 * Gets the name of this effect
	 * @return The name of this effect
	 */
	public String getName() {
		return type.name;
	}

	/**
	 * Gets the speed of the particles in this effect
	 * @return The speed of the particles in this effect
	 */
	public double getSpeed(){
		return speed;
	}

	/**
	 * Retrieves the number of particles spawned by the effect
	 * @return The number of particles spawned by the effect
	 */
	public int getCount(){
		return count;
	}

	/**
	 * Gets the offsetX of the particle effect
	 * @return The offsetX of the particle effect
	 */
	public double getOffsetX(){
		return offsetX;
	}
	
	/**
	 * Gets the offsetY of the particle effect
	 * @return The offsetY of the particle effect
	 */
	public double getOffsetY(){
		return offsetY;
	}
	
	/**
	 * Gets the offsetZ of the particle effect
	 * @return The offsetZ of the particle effect
	 */
	public double getOffsetZ(){
		return offsetZ;
	}

	/**
	 * Send a particle effect to all players
	 * @param location The location to send the effect to
	 */
	public void display(Location location){
		try {
			Object packet = createPacket(location);
			for (Player player : Bukkit.getOnlinePlayers()){
				if(player.getLocation().getWorld().getName().equals(location.getWorld().getName())) // Patch a bug where particles will be shown in all worlds
					sendPacket(player, packet);
			}
		}
		catch (Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * Constructs a new particle packet
	 * @param location the location to spawn the particle effect at
	 * @return the constructed packet
	 */
	private Object createPacket(Location location){
		try {
			if (this.count <= 0){
				this.count = 1;
			}
			Object packet;
			if (netty){
				if (newParticlePacketConstructor){
					Object particleType = enumParticle.getEnumConstants()[type.getId()];
					packet = packetConstructor.newInstance(particleType,
							true, (float)location.getX(), (float)location.getY(), (float)location.getZ(),
							(float)this.offsetX, (float)this.offsetY, (float)this.offsetZ,
							(float)this.speed, this.count, new int[0]);
				}
				else {
					packet = packetConstructor.newInstance(type.getName(),
							(float)location.getX(), (float)location.getY(), (float)location.getZ(),
							(float)this.offsetX, (float)this.offsetY, (float)this.offsetZ,
							(float)this.speed, this.count);
				}
			}
			else {
				packet = packetConstructor.newInstance();
				for (Field f : fields){
					f.setAccessible(true);
					if (f.getName().equals("a"))
						f.set(packet, type.getName());
					else if (f.getName().equals("b"))
						f.set(packet, (float)location.getX());
					else if (f.getName().equals("c"))
						f.set(packet, (float)location.getY());
					else if (f.getName().equals("d"))
						f.set(packet, (float)location.getZ());
					else if (f.getName().equals("e"))
						f.set(packet, this.offsetX);
					else if (f.getName().equals("f"))
						f.set(packet, this.offsetY);
					else if (f.getName().equals("g"))
						f.set(packet, this.offsetZ);
					else if (f.getName().equals("h"))
						f.set(packet, this.speed);
					else if (f.getName().equals("i"))
						f.set(packet, this.count);
				}
			}
			return packet;
		}
		catch (IllegalAccessException ex){
			ex.printStackTrace();
			Bukkit.getLogger().severe("[PlayerParticles] Failed to construct particle effect packet!");
		}
		catch (InstantiationException ex){
			ex.printStackTrace();
			Bukkit.getLogger().severe("[PlayerParticles] Failed to construct particle effect packet!");
		}
		catch (InvocationTargetException ex){
			ex.printStackTrace();
			Bukkit.getLogger().severe("[PlayerParticles] Failed to construct particle effect packet!");
		}
		return null;
	}

	/**
	 * Sends a packet to a player.
	 * <p>
	 *     Note: this method is <strong>not typesafe</strong>!
	 * </p>
	 * @param p the player to send a packet to
	 * @param packet the packet to send
	 * @throws IllegalArgumentException if <code>packet</code> is not of a proper type
	 */
	private static void sendPacket(Player p, Object packet) throws IllegalArgumentException {
		try {
			if (player_connection == null){
				player_connection = getHandle(p).getClass().getField("playerConnection");
				for (Method m : player_connection.get(getHandle(p)).getClass().getMethods()){
					if (m.getName().equalsIgnoreCase("sendPacket")){
						player_sendPacket = m;
					}
				}
			}
			player_sendPacket.invoke(player_connection.get(getHandle(p)), packet);
		}
		catch (IllegalAccessException ex){
			ex.printStackTrace();
			Bukkit.getLogger().severe("[PlayerParticles] Failed to send packet!");
		}
		catch (InvocationTargetException ex){
			ex.printStackTrace();
			Bukkit.getLogger().severe("[PlayerParticles] Failed to send packet!");
		}
		catch (NoSuchFieldException ex){
			ex.printStackTrace();
			Bukkit.getLogger().severe("[PlayerParticles] Failed to send packet!");
		}
	}

	/**
	 * Gets the NMS handle of the given {@link Entity}.
	 * @param entity the entity get the handle of
	 * @return the entity's NMS handle
	 */
	private static Object getHandle(Entity entity){
		try {
			if (handles.get(entity.getClass()) != null)
				return handles.get(entity.getClass()).invoke(entity);
			else {
				Method entity_getHandle = entity.getClass().getMethod("getHandle");
				handles.put(entity.getClass(), entity_getHandle);
				return entity_getHandle.invoke(entity);
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	/**
	 * Gets the NMS class by the given name.
	 * @param name the name of the NMS class to get
	 * @return the NMS class of the given name
	 */
	private static Class<?> getNmsClass(String name){
		String version = getVersion();
		String className = "net.minecraft.server." + version + name;
		Class<?> clazz = null;
		try {
			clazz = Class.forName(className);
		}
		catch (ClassNotFoundException ex){
			ex.printStackTrace();
			Bukkit.getLogger().severe("[PlayerParticles] Failed to load NMS class " + name + "!");
		}
		return clazz;
	}

	/**
	 * Determines the version string used by Craftbukkit's safeguard (e.g. 1_7_R4).
	 * @return the version string used by Craftbukkit's safeguard
	 */
	private static String getVersion(){
		String[] array = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",");
		if (array.length == 4)
			return array[3] + ".";
		return "";
	}

	/**
	 * Gets whether PlayerParticles is compatible with the server software.
	 * @return whether PlayerParticles is compatible with the server software.
	 */
	public static boolean isCompatible(){
		return compatible;
	}

	/**
	 * Enum representing valid particle types in Minecraft 1.10 minus a few which are too similar or too spammy
	 */
	public enum ParticleType {

		EXPLODE("explode", 0, 17),
		LARGE_EXPLODE("largeexplode", 1, 1),
		HUGE_EXPLOSION("hugeexplosion", 2, 0),
		FIREWORKS_SPARK("fireworksSpark", 3, 2),
		BUBBLE("bubble", 4, 3),
		WAKE("wake", 6, -1),
		SUSPENDED("suspended", 7, 4),
		DEPTH_SUSPEND("depthsuspend", 8, 5),
		CRIT("crit", 9, 7),
		MAGIC_CRIT("magicCrit", 10, 8),
		SMOKE("smoke", 11, -1),
		LARGE_SMOKE("largesmoke", 12, 22),
		SPELL("spell", 13, 11),
		INSTANT_SPELL("instantSpell", 14, 12),
		MOB_SPELL("mobSpell", 15, 9),
		MOB_SPELL_AMBIENT("mobSpellAmbient", 16, 10),
		WITCH_MAGIC("witchMagic", 17, 13),
		DRIP_WATER("dripWater", 18, 27),
		DRIP_LAVA("dripLava", 19, 28),
		ANGRY_VILLAGER("angryVillager", 20, 31),
		HAPPY_VILLAGER("happyVillager", 21, 32),
		NOTE("note", 23, 24),
		PORTAL("portal", 24, 15),
		ENCHANTMENT_TABLE("enchantmenttable", 25, 16),
		FLAME("flame", 26, 18),
		LAVA("lava", 27, 19),
		FOOTSTEP("footstep", 28, 20),
		CLOUD("cloud", 29, 23),
		RED_DUST("reddust", 30, 24),
		SNOWBALL_POOF("snowballpoof", 31, 25),
		SNOW_SHOVEL("snowshovel", 32, 28),
		SLIME("slime", 33, 29),
		HEART("heart", 34, 30),
		BARRIER("barrier", 35, -1),
		DROPLET("droplet", 39, -1),
		DRAGON_BREATH("dragonbreath", 42, -1),
		END_ROD("endRod", 43, -1),
		DAMAGE_INDICATOR("damageIndicator", 44, -1),
		SWEEP_ATTACK("sweepAttack", 45, -1),
		RAINBOW("rainbow", 30, -1);

		private String name;
		private int id;
		private int legacyId;

		ParticleType(String name, int id, int legacyId){
			this.name = name;
			this.id = id;
			this.legacyId = legacyId;
		}

		/**
		 * Gets the name of the particle effect
		 *
		 * @return The name of the particle effect
		 */
		public String getName(){
			return name;
		}

		/**
		 * Gets the ID of the particle effect
		 *
		 * @return The ID of the particle effect
		 */
		int getId(){
			return id;
		}

		/**
		 * Gets the legacy ID (pre-1.8) of the particle effect
		 *
		 * @return the legacy ID of the particle effect (or -1 if introduced after 1.7)
		 */
		int getLegacyId(){
			return legacyId;
		}
	}

}