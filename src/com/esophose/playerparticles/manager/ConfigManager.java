/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles.manager;

import java.io.File;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.ParticleCreator;
import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.library.ParticleEffect;
import com.esophose.playerparticles.library.ParticleEffect.BlockData;
import com.esophose.playerparticles.library.ParticleEffect.ItemData;
import com.esophose.playerparticles.library.ParticleEffect.NoteColor;
import com.esophose.playerparticles.library.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;

public class ConfigManager {

	/**
	 * The instance of the ConfigManager used for effect data
	 */
	private static ConfigManager instance = new ConfigManager("playerData");
	/**
	 * The file the data is located in for the instance
	 */
	private File file;
	/**
	 * The configuration used to edit the .yaml file
	 */
	private FileConfiguration config;

	/**
	 * @return The instance of the config for effects
	 */
	public static ConfigManager getInstance() {
		return instance;
	}

	/**
	 * @param fileName The name of the file
	 */
	private ConfigManager(String fileName) {
		if (!PlayerParticles.getPlugin().getDataFolder().exists()) PlayerParticles.getPlugin().getDataFolder().mkdir();

		file = new File(PlayerParticles.getPlugin().getDataFolder(), fileName + ".yml");

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		config = YamlConfiguration.loadConfiguration(file);
	}

	/**
	 * Saves the playerData.yml file to disk
	 */
	private void save() {
		try {
			config.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Gets a player from the save data, creates one if it doesn't exist and adds it to the list
	 */
	public PPlayer getPPlayer(UUID playerUUID) {
		for (PPlayer pp : ParticleCreator.particlePlayers) {
			if (pp.getUniqueId() == playerUUID) return pp;
		}

		PPlayer pplayer = buildPPlayer(playerUUID);
		ParticleCreator.particlePlayers.add(pplayer);
		return pplayer;
	}

	/**
	 * Gets a PPlayer matching the UUID given
	 * One will be created if it doesn't exist
	 * 
	 * @param playerUUID The UUID to match the PPlayer to
	 * @return
	 */
	private PPlayer buildPPlayer(UUID playerUUID) {
		if (!PlayerParticles.useMySQL) {
			if (config.getString(playerUUID.toString() + ".style.name") != null) {
				ConfigurationSection section = config.getConfigurationSection(playerUUID.toString());
				ConfigurationSection effectSection = section.getConfigurationSection("effect");
				ConfigurationSection styleSection = section.getConfigurationSection("style");
				ConfigurationSection itemDataSection = section.getConfigurationSection("itemData");
				ConfigurationSection blockDataSection = section.getConfigurationSection("blockData");
				ConfigurationSection colorDataSection = section.getConfigurationSection("colorData");
				ConfigurationSection noteColorDataSection = section.getConfigurationSection("noteColorData");

				ParticleEffect particleEffect = ParticleEffect.fromName(effectSection.getString("name"));
				ParticleStyle particleStyle = ParticleStyleManager.styleFromString(styleSection.getString("name"));
				ItemData particleItemData = new ItemData(Material.matchMaterial(itemDataSection.getString("material")), (byte) itemDataSection.getInt("data"));
				BlockData particleBlockData = new BlockData(Material.matchMaterial(blockDataSection.getString("material")), (byte) blockDataSection.getInt("data"));
				OrdinaryColor particleColorData = new OrdinaryColor(Color.fromRGB(colorDataSection.getInt("r"), colorDataSection.getInt("g"), colorDataSection.getInt("b")));
				NoteColor particleNoteColorData = new NoteColor(noteColorDataSection.getInt("note"));

				return new PPlayer(playerUUID, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData);
			} else {
				PPlayer pplayer = PPlayer.getNewPPlayer(playerUUID);
				saveNewPPlayer(pplayer);
				return pplayer;
			}
		} else {
			String id = playerUUID.toString(); // @formatter:off
			try (ResultSet res = PlayerParticles.mySQL.querySQL("SELECT * FROM pp_users u " + 
																"JOIN pp_data_item i ON u.player_uuid = i.player_uuid " + 
																"JOIN pp_data_block b ON u.player_uuid = b.player_uuid " +
																"JOIN pp_data_color c ON u.player_uuid = c.player_uuid " +
																"JOIN pp_data_note n ON u.player_uuid = n.player_uuid " +
																"WHERE u.player_uuid = '" + id + "'")) { // @formatter:on

				if (res.next()) {
					ParticleEffect particleEffect = ParticleEffect.fromName(res.getString("u.effect"));
					ParticleStyle particleStyle = ParticleStyleManager.styleFromString(res.getString("u.style"));
					ItemData particleItemData = new ItemData(Material.matchMaterial(res.getString("i.material")), res.getByte("i.data"));
					BlockData particleBlockData = new BlockData(Material.matchMaterial(res.getString("b.material")), res.getByte("b.data"));
					OrdinaryColor particleColorData = new OrdinaryColor(Color.fromRGB(res.getInt("c.r"), res.getInt("c.g"), res.getInt("c.b")));
					NoteColor particleNoteColorData = new NoteColor(res.getByte("n.note"));

					return new PPlayer(playerUUID, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData);
				} else {
					PPlayer pplayer = PPlayer.getNewPPlayer(playerUUID);
					saveNewPPlayer(pplayer);
					return pplayer;
				}
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		// This should only be returned if there is a database or config error
		return null;
	}

	/**
	 * Saves a new PPlayer to the database or the file
	 * 
	 * @param pplayer The PPlayer to save
	 */
	public void saveNewPPlayer(PPlayer pplayer) {
		if (!PlayerParticles.useMySQL) {
			if (!config.isConfigurationSection(pplayer.getUniqueId().toString())) {
				String id = pplayer.getUniqueId().toString();
				config.createSection(id);
				config.createSection(id + ".effect");
				config.createSection(id + ".style");
				config.createSection(id + ".itemData");
				config.createSection(id + ".blockData");
				config.createSection(id + ".colorData");
				config.createSection(id + ".noteColorData");
			}

			ConfigurationSection section = config.getConfigurationSection(pplayer.getUniqueId().toString());
			ConfigurationSection effectSection = section.getConfigurationSection("effect");
			ConfigurationSection styleSection = section.getConfigurationSection("style");
			ConfigurationSection itemDataSection = section.getConfigurationSection("itemData");
			ConfigurationSection blockDataSection = section.getConfigurationSection("blockData");
			ConfigurationSection colorDataSection = section.getConfigurationSection("colorData");
			ConfigurationSection noteColorDataSection = section.getConfigurationSection("noteColorData");

			effectSection.set("name", pplayer.getParticleEffect().getName());
			styleSection.set("name", pplayer.getParticleStyle().getName());
			itemDataSection.set("material", pplayer.getItemData().getMaterial().name());
			itemDataSection.set("data", pplayer.getItemData().getData());
			blockDataSection.set("material", pplayer.getBlockData().getMaterial().name());
			blockDataSection.set("data", pplayer.getBlockData().getData());
			colorDataSection.set("r", pplayer.getColorData().getRed());
			colorDataSection.set("g", pplayer.getColorData().getGreen());
			colorDataSection.set("b", pplayer.getColorData().getBlue());
			noteColorDataSection.set("note", (byte) (pplayer.getNoteColorData().getValueX() * 24));

			save();
		} else {
			try (ResultSet res = PlayerParticles.mySQL.querySQL("SELECT * FROM pp_users WHERE player_uuid = '" + pplayer.getUniqueId() + "'")) {
				if (res.next()) {
					throw new RuntimeException("The user " + pplayer.getUniqueId() + " is already in the database. They can not be added.");
				} else { // @formatter:off
					PlayerParticles.mySQL.updateSQL("INSERT INTO pp_users (player_uuid, effect, style) VALUES (" +
													"'" + pplayer.getUniqueId().toString() + "', " +
													"'" + pplayer.getParticleEffect().getName() + "', " +
													"'" + pplayer.getParticleStyle().getName() + "'" +
													"); " +
													"INSERT INTO pp_data_item (player_uuid, material, data) VALUES (" +
													"'" + pplayer.getUniqueId().toString() + "', " +
													"'" + pplayer.getItemData().getMaterial().name() + "', " +
													      pplayer.getItemData().getData() + 
													"); " +
													"INSERT INTO pp_data_block (player_uuid, material, data) VALUES (" +
													"'" + pplayer.getUniqueId().toString() + "', " +
													"'" + pplayer.getBlockData().getMaterial().name() + "', " +
												          pplayer.getBlockData().getData() + 
												    "); " +
												    "INSERT INTO pp_data_color (player_uuid, r, g, b) VALUES (" +
												    "'" + pplayer.getUniqueId().toString() + "', " +
												    	  pplayer.getColorData().getRed() + ", " +
												    	  pplayer.getColorData().getGreen() + ", " +
												    	  pplayer.getColorData().getBlue() + 
												    "); " +
												    "INSERT INTO pp_data_note (player_uuid, note) VALUES (" +
												    "'" + pplayer.getUniqueId().toString() + "', " +
												    	  (byte) (pplayer.getNoteColorData().getValueX() * 24) +
												    ");"
												    );
				} // @formatter:on
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}

		ParticleCreator.updateIfContains(pplayer); // Update the player in case this is a /pp reset
	}

	/**
	 * Resets all saved information about a PPlayer
	 * This should be made into a single batch query in the future
	 * 
	 * @param playerUUID
	 */
	public void resetPPlayer(UUID playerUUID) {
		PPlayer pplayer = PPlayer.getNewPPlayer(playerUUID);
		savePPlayer(playerUUID, pplayer.getParticleEffect());
		savePPlayer(playerUUID, pplayer.getParticleStyle());
		savePPlayer(playerUUID, pplayer.getItemData());
		savePPlayer(playerUUID, pplayer.getBlockData());
		savePPlayer(playerUUID, pplayer.getColorData());
		savePPlayer(playerUUID, pplayer.getNoteColorData());
	}

	/**
	 * Saves the effect to the player's save file or database entry
	 * 
	 * @param playerUUID The UUID of the player
	 * @param particleStyle The effect that is being saved
	 */
	public void savePPlayer(UUID playerUUID, ParticleEffect particleEffect) {
		if (!PlayerParticles.useMySQL) {
			ConfigurationSection section = config.getConfigurationSection(playerUUID.toString() + ".effect");
			section.set("name", particleEffect.getName());
			save();
		} else {
			try {
				PlayerParticles.mySQL.updateSQL("UPDATE pp_users SET effect = '" + particleEffect.getName() + "' WHERE player_uuid = '" + playerUUID + "';");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		getPPlayer(playerUUID).setParticleEffect(particleEffect);
	}

	/**
	 * Saves the style to the player's save file or database entry
	 * 
	 * @param playerUUID The UUID of the player
	 * @param particleStyle The style that is being saved
	 */
	public void savePPlayer(UUID playerUUID, ParticleStyle particleStyle) {
		if (!PlayerParticles.useMySQL) {
			ConfigurationSection section = config.getConfigurationSection(playerUUID.toString() + ".style");
			section.set("name", particleStyle.getName());
			save();
		} else {
			try {
				PlayerParticles.mySQL.updateSQL("UPDATE pp_users SET style = '" + particleStyle.getName() + "' WHERE player_uuid = '" + playerUUID + "';");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		getPPlayer(playerUUID).setParticleStyle(particleStyle);
	}

	/**
	 * Saves the item data to the player's save file or database entry
	 * 
	 * @param playerUUID The UUID of the player
	 * @param particleItemData The data that is being saved
	 */
	public void savePPlayer(UUID playerUUID, ItemData particleItemData) {
		if (!PlayerParticles.useMySQL) {
			ConfigurationSection section = config.getConfigurationSection(playerUUID.toString() + ".itemData");
			section.set("material", particleItemData.getMaterial().name());
			section.set("data", particleItemData.getData());
			save();
		} else {
			try {
				PlayerParticles.mySQL.updateSQL("UPDATE pp_data_item SET material = '" + particleItemData.getMaterial().name() + "', data = '" + particleItemData.getData() + "' WHERE player_uuid = '" + playerUUID + "';");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		getPPlayer(playerUUID).setItemData(particleItemData);
	}

	/**
	 * Saves the block data to the player's save file or database entry
	 * 
	 * @param playerUUID The UUID of the player
	 * @param particleBlockData The data that is being saved
	 */
	public void savePPlayer(UUID playerUUID, BlockData particleBlockData) {
		if (!PlayerParticles.useMySQL) {
			ConfigurationSection section = config.getConfigurationSection(playerUUID.toString() + ".blockData");
			section.set("material", particleBlockData.getMaterial().name());
			section.set("data", particleBlockData.getData());
			save();
		} else {
			try {
				PlayerParticles.mySQL.updateSQL("UPDATE pp_data_block SET material = '" + particleBlockData.getMaterial().name() + "', data = '" + particleBlockData.getData() + "' WHERE player_uuid = '" + playerUUID + "';");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		getPPlayer(playerUUID).setBlockData(particleBlockData);
	}

	/**
	 * Saves the color data to the player's save file or database entry
	 * 
	 * @param playerUUID The UUID of the player
	 * @param particleColorData The data that is being saved
	 */
	public void savePPlayer(UUID playerUUID, OrdinaryColor particleColorData) {
		if (!PlayerParticles.useMySQL) {
			ConfigurationSection section = config.getConfigurationSection(playerUUID.toString() + ".colorData");
			section.set("r", particleColorData.getRed());
			section.set("g", particleColorData.getGreen());
			section.set("b", particleColorData.getBlue());
			save();
		} else {
			try {
				PlayerParticles.mySQL.updateSQL("UPDATE pp_data_color SET r = " + particleColorData.getRed() + ", g = " + particleColorData.getGreen() + ", b = " + particleColorData.getBlue() + " WHERE player_uuid = '" + playerUUID + "';");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		getPPlayer(playerUUID).setColorData(particleColorData);
	}

	/**
	 * Saves the note color data to the player's save file or database entry
	 * 
	 * @param playerUUID The UUID of the player
	 * @param particleNoteColorData The data that is being saved
	 */
	public void savePPlayer(UUID playerUUID, NoteColor particleNoteColorData) {
		if (!PlayerParticles.useMySQL) {
			ConfigurationSection section = config.getConfigurationSection(playerUUID.toString() + ".noteColorData");
			section.set("note", (byte) (particleNoteColorData.getValueX() * 24));
			save();
		} else {
			try {
				PlayerParticles.mySQL.updateSQL("UPDATE pp_data_note SET note = " + (byte) (particleNoteColorData.getValueX() * 24) + " WHERE player_uuid = '" + playerUUID + "';");
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
			}
		}
		getPPlayer(playerUUID).setNoteColorData(particleNoteColorData);
	}

	/**
	 * Checks if a world is disabled for particles to spawn in
	 * 
	 * @param world The world name to check
	 * @return True if the world is disabled
	 */
	public boolean isWorldDisabled(String world) {
		return getDisabledWorlds().contains(world);
	}

	/**
	 * Gets all the worlds that are disabled
	 * 
	 * @return All world names that are disabled
	 */
	@SuppressWarnings("unchecked")
	public ArrayList<String> getDisabledWorlds() {
		if (PlayerParticles.getPlugin().getConfig().get("disabled-worlds") != null) {
			return ((ArrayList<String>) PlayerParticles.getPlugin().getConfig().get("disabled-worlds"));
		} else return null;
	}

}
