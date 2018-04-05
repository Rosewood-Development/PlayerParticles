/**
 * Copyright Esophose 2018
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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import com.esophose.playerparticles.PPlayer;
import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.particles.FixedParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.particles.ParticleEffect.BlockData;
import com.esophose.playerparticles.particles.ParticleEffect.ItemData;
import com.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import com.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import com.esophose.playerparticles.styles.api.ParticleStyle;
import com.esophose.playerparticles.styles.api.ParticleStyleManager;
import com.esophose.playerparticles.util.ParticleUtils;

public class ConfigManager {

    /**
     * The instance of the ConfigManager used for effect data
     */
    private static ConfigManager instance = new ConfigManager("playerData");
    /**
     * The file the data is located in for the instance
     */
    private File playerDataYamlFile;
    /**
     * The configuration used to edit the .yaml file
     */
    private FileConfiguration playerDataYaml;

    /**
     * The disabled worlds cached for quick access
     */
    private List<String> disabledWorlds = null;

    /**
     * The max number of fixed effects a player can have, defined in the config
     */
    private int maxFixedEffects = -1;

    /**
     * The max distance a fixed effect can be created relative to the player
     */
    private int maxFixedEffectCreationDistance = -1;

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
        if (!PlayerParticles.useMySQL) { // Don't bother creating the playerData.yml file if we aren't going to use it
            if (!PlayerParticles.getPlugin().getDataFolder().exists()) PlayerParticles.getPlugin().getDataFolder().mkdir();

            playerDataYamlFile = new File(PlayerParticles.getPlugin().getDataFolder(), fileName + ".yml");

            if (!playerDataYamlFile.exists()) {
                try {
                    playerDataYamlFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            playerDataYaml = YamlConfiguration.loadConfiguration(playerDataYamlFile);
        }
    }

    /**
     * Saves the playerData.yml file to disk
     */
    private void save() {
        try {
            playerDataYaml.save(playerDataYamlFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Gets a PPlayer from cache
     * This method should be used over the other one unless you absolutely need the PPlayer and you don't care about waiting
     * You should always check for a null result when using this method
     * 
     * @param playerUUID The PPlayer to get
     * @return The PPlayer from cache
     */
    public PPlayer getPPlayer(UUID playerUUID) {
        for (PPlayer pp : ParticleManager.particlePlayers) 
            if (pp.getUniqueId() == playerUUID) 
                return pp;
        
        return null;
    }

    /**
     * Gets a player from the save data, creates one if it doesn't exist and caches it
     * 
     * @param playerUUID The pplayer to get
     * @param callback The callback to execute with the found pplayer, or a newly generated one 
     */
    public void getPPlayer(UUID playerUUID, ConfigurationCallback<PPlayer> callback) {
        // Try to get them from cache first
        PPlayer fromCache = getPPlayer(playerUUID);
        if (fromCache != null) {
            callback.execute(fromCache);
            return;
        }
        
        // Either get an existing one from the database, or create a new one
        buildPPlayer(playerUUID, true, (pplayer) -> {
            ParticleManager.particlePlayers.add(pplayer);
            callback.execute(pplayer);
        });
    }

    /**
     * Gets a PPlayer matching the UUID given
     * If createIfNotFound is true, one will be created if it doesn't exist
     * 
     * @param playerUUID The UUID to match the PPlayer to
     * @param createIfNotFound If true, creates a new PPlayer if the requested one doesn't exist
     * @param callback The callback to execute with the built PPlayer
     */
    private void buildPPlayer(UUID playerUUID, boolean createIfNotFound, ConfigurationCallback<PPlayer> callback) {
        if (!PlayerParticles.useMySQL) {
            if (playerDataYaml.contains(playerUUID.toString())) {
                ConfigurationSection section = playerDataYaml.getConfigurationSection(playerUUID.toString());
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
                OrdinaryColor particleColorData = new OrdinaryColor(colorDataSection.getInt("r"), colorDataSection.getInt("g"), colorDataSection.getInt("b"));
                NoteColor particleNoteColorData = new NoteColor(noteColorDataSection.getInt("note"));

                callback.execute(new PPlayer(playerUUID, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData));
                return;
            }
            
            if (createIfNotFound) {
                // Didn't find an existing PPlayer, create and return a new one
                PPlayer pplayer = PPlayer.getNewPPlayer(playerUUID);
                saveNewPPlayer(pplayer);
                callback.execute(pplayer);
            }
        } else {
            async(() -> {
                String id = playerUUID.toString(); // @formatter:off
                PlayerParticles.mySQL.connect((connection) -> {
                    String query = "SELECT * FROM pp_users u " + 
                                   "JOIN pp_data_item i ON u.player_uuid = i.uuid " + 
                                   "JOIN pp_data_block b ON u.player_uuid = b.uuid " +
                                   "JOIN pp_data_color c ON u.player_uuid = c.uuid " +
                                   "JOIN pp_data_note n ON u.player_uuid = n.uuid " +
                                   "WHERE u.player_uuid = '" + id + "'";
                    
                    try (Statement statement = connection.createStatement();
                         ResultSet res = statement.executeQuery(query)) {
                        
                        if (res.next()) {
                            ParticleEffect particleEffect = ParticleEffect.fromName(res.getString("u.effect"));
                            ParticleStyle particleStyle = ParticleStyleManager.styleFromString(res.getString("u.style"));
                            ItemData particleItemData = new ItemData(Material.matchMaterial(res.getString("i.material")), res.getByte("i.data"));
                            BlockData particleBlockData = new BlockData(Material.matchMaterial(res.getString("b.material")), res.getByte("b.data"));
                            OrdinaryColor particleColorData = new OrdinaryColor(res.getInt("c.r"), res.getInt("c.g"), res.getInt("c.b"));
                            NoteColor particleNoteColorData = new NoteColor(res.getByte("n.note"));

                            sync(() -> callback.execute(new PPlayer(playerUUID, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData)));
                            return;
                        }
                        
                        if (createIfNotFound) { // Didn't find an existing PPlayer, create and return a new one
                            PPlayer pplayer = PPlayer.getNewPPlayer(playerUUID);
                            saveNewPPlayer(pplayer);
                            sync(() -> callback.execute(pplayer));
                        }
                    }
                });
            });
        }
    }

    /**
     * Saves a new PPlayer to the database or the file
     * 
     * @param pplayer The PPlayer to save
     */
    public void saveNewPPlayer(PPlayer pplayer) {
        if (!PlayerParticles.useMySQL) {
            if (!playerDataYaml.isConfigurationSection(pplayer.getUniqueId().toString())) {
                String id = pplayer.getUniqueId().toString();
                playerDataYaml.createSection(id);
                playerDataYaml.createSection(id + ".effect");
                playerDataYaml.createSection(id + ".style");
                playerDataYaml.createSection(id + ".itemData");
                playerDataYaml.createSection(id + ".blockData");
                playerDataYaml.createSection(id + ".colorData");
                playerDataYaml.createSection(id + ".noteColorData");
            }

            ConfigurationSection section = playerDataYaml.getConfigurationSection(pplayer.getUniqueId().toString());
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
            async(() -> {
                PlayerParticles.mySQL.connect((connection) -> {
                    try (Statement statement = connection.createStatement();
                         ResultSet res = statement.executeQuery("SELECT * FROM pp_users WHERE player_uuid = '" + pplayer.getUniqueId() + "'")) {
                        
                        if (!res.next()) {
                            PlayerParticles.mySQL.updateSQL("INSERT INTO pp_users (player_uuid, effect, style) VALUES (" +
                                                            "'" + pplayer.getUniqueId().toString() + "', " +
                                                            "'" + pplayer.getParticleEffect().getName() + "', " +
                                                            "'" + pplayer.getParticleStyle().getName() + "'" +
                                                            "); " +
                                                            "INSERT INTO pp_data_item (uuid, material, data) VALUES (" +
                                                            "'" + pplayer.getUniqueId().toString() + "', " +
                                                            "'" + pplayer.getItemData().getMaterial().name() + "', " +
                                                            pplayer.getItemData().getData() + 
                                                            "); " +
                                                            "INSERT INTO pp_data_block (uuid, material, data) VALUES (" +
                                                            "'" + pplayer.getUniqueId().toString() + "', " +
                                                            "'" + pplayer.getBlockData().getMaterial().name() + "', " +
                                                            pplayer.getBlockData().getData() + 
                                                            "); " +
                                                            "INSERT INTO pp_data_color (uuid, r, g, b) VALUES (" +
                                                            "'" + pplayer.getUniqueId().toString() + "', " +
                                                            pplayer.getColorData().getRed() + ", " +
                                                            pplayer.getColorData().getGreen() + ", " +
                                                            pplayer.getColorData().getBlue() + 
                                                            "); " +
                                                            "INSERT INTO pp_data_note (uuid, note) VALUES (" +
                                                            "'" + pplayer.getUniqueId().toString() + "', " +
                                                            (byte) (pplayer.getNoteColorData().getValueX() * 24) +
                                                            ");"
                                                            );
                        } else {
                            throw new RuntimeException("The user " + pplayer.getUniqueId() + " is already in the database. They can not be added.");
                        }
                    }
                });
            });
        }

        ParticleManager.updateIfContains(pplayer); // Update the player in case this is a /pp reset
    }
    
    /**
     * Loads a PPlayer and caches it
     * 
     * @param playerUUID The pplayer to load
     */
    public void loadPPlayer(UUID playerUUID) {
        for (PPlayer pplayer : ParticleManager.particlePlayers)
            if (pplayer.getUniqueId() == playerUUID)
                return;
        
        buildPPlayer(playerUUID, false, (pplayer) -> { 
            ParticleManager.particlePlayers.add(pplayer);
        });
    }

    /**
     * Resets all saved information about a PPlayer
     * This should be made into a single batch query in the future
     * 
     * @param playerUUID The pplayer to reset
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
     * @param particleEffect The effect that is being saved
     */
    public void savePPlayer(UUID playerUUID, ParticleEffect particleEffect) {
        getPPlayer(playerUUID, (pplayer) -> {
            if (!PlayerParticles.useMySQL) {
                ConfigurationSection section = playerDataYaml.getConfigurationSection(playerUUID.toString() + ".effect");
                section.set("name", particleEffect.getName());
                save();
            } else {
                async(() -> {
                    try {
                        PlayerParticles.mySQL.updateSQL("UPDATE pp_users SET effect = '" + particleEffect.getName() + "' WHERE player_uuid = '" + playerUUID + "';");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        
            pplayer.setParticleEffect(particleEffect);
        });
    }

    /**
     * Saves the style to the player's save file or database entry
     * 
     * @param playerUUID The UUID of the player
     * @param particleStyle The style that is being saved
     */
    public void savePPlayer(UUID playerUUID, ParticleStyle particleStyle) {
        getPPlayer(playerUUID, (pplayer) -> {
            if (!PlayerParticles.useMySQL) {
                ConfigurationSection section = playerDataYaml.getConfigurationSection(playerUUID.toString() + ".style");
                section.set("name", particleStyle.getName());
                save();
            } else {
                async(() -> {
                    try {
                        PlayerParticles.mySQL.updateSQL("UPDATE pp_users SET style = '" + particleStyle.getName() + "' WHERE player_uuid = '" + playerUUID + "';");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
            
            pplayer.setParticleStyle(particleStyle);
        });
    }

    /**
     * Saves the item data to the player's save file or database entry
     * 
     * @param playerUUID The UUID of the player
     * @param particleItemData The data that is being saved
     */
    public void savePPlayer(UUID playerUUID, ItemData particleItemData) {
        getPPlayer(playerUUID, (pplayer) -> {
            if (!PlayerParticles.useMySQL) {
                ConfigurationSection section = playerDataYaml.getConfigurationSection(playerUUID.toString() + ".itemData");
                section.set("material", particleItemData.getMaterial().name());
                section.set("data", particleItemData.getData());
                save();
            } else {
                async(() -> {
                    try {
                        PlayerParticles.mySQL.updateSQL("UPDATE pp_data_item SET material = '" + particleItemData.getMaterial().name() + "', data = '" + particleItemData.getData() + "' WHERE uuid = '" + playerUUID + "';");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        
            pplayer.setItemData(particleItemData);
        });
    }

    /**
     * Saves the block data to the player's save file or database entry
     * 
     * @param playerUUID The UUID of the player
     * @param particleBlockData The data that is being saved
     */
    public void savePPlayer(UUID playerUUID, BlockData particleBlockData) {
        getPPlayer(playerUUID, (pplayer) -> {
            if (!PlayerParticles.useMySQL) {
                ConfigurationSection section = playerDataYaml.getConfigurationSection(playerUUID.toString() + ".blockData");
                section.set("material", particleBlockData.getMaterial().name());
                section.set("data", particleBlockData.getData());
                save();
            } else {
                async(() -> {
                    try {
                        PlayerParticles.mySQL.updateSQL("UPDATE pp_data_block SET material = '" + particleBlockData.getMaterial().name() + "', data = '" + particleBlockData.getData() + "' WHERE uuid = '" + playerUUID + "';");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        
            pplayer.setBlockData(particleBlockData);
        });
    }

    /**
     * Saves the color data to the player's save file or database entry
     * 
     * @param playerUUID The UUID of the player
     * @param particleColorData The data that is being saved
     */
    public void savePPlayer(UUID playerUUID, OrdinaryColor particleColorData) {
        getPPlayer(playerUUID, (pplayer) -> {
            if (!PlayerParticles.useMySQL) {
                ConfigurationSection section = playerDataYaml.getConfigurationSection(playerUUID.toString() + ".colorData");
                section.set("r", particleColorData.getRed());
                section.set("g", particleColorData.getGreen());
                section.set("b", particleColorData.getBlue());
                save();
            } else {
                async(() -> {
                    try {
                        PlayerParticles.mySQL.updateSQL("UPDATE pp_data_color SET r = " + particleColorData.getRed() + ", g = " + particleColorData.getGreen() + ", b = " + particleColorData.getBlue() + " WHERE uuid = '" + playerUUID + "';");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        
            pplayer.setColorData(particleColorData);
        });
    }

    /**
     * Saves the note color data to the player's save file or database entry
     * 
     * @param playerUUID The UUID of the player
     * @param particleNoteColorData The data that is being saved
     */
    public void savePPlayer(UUID playerUUID, NoteColor particleNoteColorData) {
        getPPlayer(playerUUID, (pplayer) -> {
            if (!PlayerParticles.useMySQL) {
                ConfigurationSection section = playerDataYaml.getConfigurationSection(playerUUID.toString() + ".noteColorData");
                section.set("note", (byte) (particleNoteColorData.getValueX() * 24));
                save();
            } else {
                async(() -> {
                    try {
                        PlayerParticles.mySQL.updateSQL("UPDATE pp_data_note SET note = " + (byte) (particleNoteColorData.getValueX() * 24) + " WHERE uuid = '" + playerUUID + "';");
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
        
            pplayer.setNoteColorData(particleNoteColorData);
        });
    }

    /**
     * Saves a fixed effect to save data
     * 
     * @param fixedEffect The fixed effect to save
     */
    public void saveFixedEffect(FixedParticleEffect fixedEffect) {
        if (!PlayerParticles.useMySQL) {
            if (!playerDataYaml.isConfigurationSection(fixedEffect.getOwnerUniqueId().toString() + ".fixedEffect." + fixedEffect.getId())) {
                ConfigurationSection baseSection = playerDataYaml.createSection(fixedEffect.getOwnerUniqueId().toString() + ".fixedEffect." + fixedEffect.getId());

                baseSection.createSection("effect");
                baseSection.createSection("style");
                baseSection.createSection("itemData");
                baseSection.createSection("blockData");
                baseSection.createSection("colorData");
                baseSection.createSection("noteColorData");
            } else {
                System.out.println("Tried to create a fixed effect with ID " + fixedEffect.getId() + " that already exists!");
                return;
            }

            ConfigurationSection section = playerDataYaml.getConfigurationSection(fixedEffect.getOwnerUniqueId().toString() + ".fixedEffect." + fixedEffect.getId());
            ConfigurationSection effectSection = section.getConfigurationSection("effect");
            ConfigurationSection styleSection = section.getConfigurationSection("style");
            ConfigurationSection itemDataSection = section.getConfigurationSection("itemData");
            ConfigurationSection blockDataSection = section.getConfigurationSection("blockData");
            ConfigurationSection colorDataSection = section.getConfigurationSection("colorData");
            ConfigurationSection noteColorDataSection = section.getConfigurationSection("noteColorData");

            section.set("id", fixedEffect.getId());
            section.set("worldName", fixedEffect.getLocation().getWorld().getName());
            section.set("xPos", fixedEffect.getLocation().getX());
            section.set("yPos", fixedEffect.getLocation().getY());
            section.set("zPos", fixedEffect.getLocation().getZ());
            effectSection.set("name", fixedEffect.getParticleEffect().getName());
            styleSection.set("name", fixedEffect.getParticleStyle().getName());
            itemDataSection.set("material", fixedEffect.getItemData().getMaterial().name());
            itemDataSection.set("data", fixedEffect.getItemData().getData());
            blockDataSection.set("material", fixedEffect.getBlockData().getMaterial().name());
            blockDataSection.set("data", fixedEffect.getBlockData().getData());
            colorDataSection.set("r", fixedEffect.getColorData().getRed());
            colorDataSection.set("g", fixedEffect.getColorData().getGreen());
            colorDataSection.set("b", fixedEffect.getColorData().getBlue());
            noteColorDataSection.set("note", (byte) (fixedEffect.getNoteColorData().getValueX() * 24));

            save();
            ParticleManager.addFixedEffect(fixedEffect);
        } else {
            async(() -> {
                PlayerParticles.mySQL.connect((connection) -> {
                    try (Statement statement = connection.createStatement();
                         ResultSet res = statement.executeQuery("SELECT * FROM pp_fixed WHERE player_uuid = '" + fixedEffect.getOwnerUniqueId() + "' AND id = " + fixedEffect.getId())) {
                        
                        if (res.next()) {
                            System.out.println("Tried to create a fixed effect with ID " + fixedEffect.getId() + " that already in the database!");
                            return;
                        }
                        
                        String fixedEffectUUID = UUID.randomUUID().toString();

                        PlayerParticles.mySQL.updateSQL("INSERT INTO pp_fixed (uuid, player_uuid, id, effect, style, worldName, xPos, yPos, zPos) VALUES (" +
                                                        "'" + fixedEffectUUID + "', " +
                                                        "'" + fixedEffect.getOwnerUniqueId().toString() + "', " +
                                                        fixedEffect.getId() + ", " +
                                                        "'" + fixedEffect.getParticleEffect().getName() + "', " +
                                                        "'" + fixedEffect.getParticleStyle().getName() + "', " +
                                                        "'" + fixedEffect.getLocation().getWorld().getName() + "', " +
                                                        fixedEffect.getLocation().getX() + ", " +
                                                        fixedEffect.getLocation().getY() + ", " +
                                                        fixedEffect.getLocation().getZ() +
                                                        "); " +
                                                        "INSERT INTO pp_data_item (uuid, material, data) VALUES (" +
                                                        "'" + fixedEffectUUID + "', " +
                                                        "'" + fixedEffect.getItemData().getMaterial().name() + "', " +
                                                        fixedEffect.getItemData().getData() + 
                                                        "); " +
                                                        "INSERT INTO pp_data_block (uuid, material, data) VALUES (" +
                                                        "'" + fixedEffectUUID + "', " +
                                                        "'" + fixedEffect.getBlockData().getMaterial().name() + "', " +
                                                        fixedEffect.getBlockData().getData() + 
                                                        "); " +
                                                        "INSERT INTO pp_data_color (uuid, r, g, b) VALUES (" +
                                                        "'" + fixedEffectUUID + "', " +
                                                        fixedEffect.getColorData().getRed() + ", " +
                                                        fixedEffect.getColorData().getGreen() + ", " +
                                                        fixedEffect.getColorData().getBlue() + 
                                                        "); " +
                                                        "INSERT INTO pp_data_note (uuid, note) VALUES (" +
                                                        "'" + fixedEffectUUID + "', " +
                                                        (byte) (fixedEffect.getNoteColorData().getValueX() * 24) +
                                                        ");"
                                                        );
                        
                        sync(() -> ParticleManager.addFixedEffect(fixedEffect));
                    }
                });
            });
        }
    }

    /**
     * Deletes a fixed effect from save data
     * 
     * @param playerUUID The player who owns the effect
     * @param id The id of the effect to remove
     * @param callback The callback to execute with if the fixed effect was removed or not
     */
    public void removeFixedEffect(UUID playerUUID, int id, ConfigurationCallback<Boolean> callback) {
        if (!PlayerParticles.useMySQL) {
            if (!playerDataYaml.isConfigurationSection(playerUUID.toString() + ".fixedEffect." + id)) {
                callback.execute(false);
                return;
            }

            playerDataYaml.set(playerUUID.toString() + ".fixedEffect." + id, null);

            save();
            ParticleManager.removeFixedEffectForPlayer(playerUUID, id);
            callback.execute(true);
        } else {
            async(() -> {
                PlayerParticles.mySQL.connect((connection) -> {
                    try (Statement statement = connection.createStatement();
                         ResultSet res = statement.executeQuery("SELECT uuid FROM pp_fixed WHERE player_uuid = '" + playerUUID.toString() + "' AND id = " + id)) {
                        
                        if (!res.next()) {
                            callback.execute(false);
                            return;
                        }
                        
                        String uuid = res.getString("uuid");
                        PlayerParticles.mySQL.updateSQL("DELETE FROM pp_fixed WHERE uuid = '" + uuid + "';" + 
                                                        "DELETE FROM pp_data_item WHERE uuid = '" + uuid + "';" +
                                                        "DELETE FROM pp_data_block WHERE uuid = '" + uuid + "';" +
                                                        "DELETE FROM pp_data_color WHERE uuid = '" + uuid + "';" +
                                                        "DELETE FROM pp_data_note WHERE uuid = '" + uuid + "';"
                                                        );
                        
                        sync(() -> {
                            ParticleManager.removeFixedEffectForPlayer(playerUUID, id);
                            callback.execute(true);
                        });
                    }
                });
            });
        }
    }

    /**
     * Resets all fixed effects for a given player
     * 
     * @param playerUUID The player to remove all effects from
     */
    public void resetFixedEffects(UUID playerUUID) {
        if (!PlayerParticles.useMySQL) {
            playerDataYaml.set(playerUUID.toString() + ".fixedEffect", null);
            save();
        } else {
            async(() -> {
                try { // @formatter:off
                    PlayerParticles.mySQL.updateSQL("DELETE FROM pp_data_item WHERE uuid IN (SELECT uuid FROM pp_fixed WHERE player_uuid = '" + playerUUID.toString() + "');" +
                                                    "DELETE FROM pp_data_block WHERE uuid IN (SELECT uuid FROM pp_fixed WHERE player_uuid = '" + playerUUID.toString() + "');" +
                                                    "DELETE FROM pp_data_color WHERE uuid IN (SELECT uuid FROM pp_fixed WHERE player_uuid = '" + playerUUID.toString() + "');" +
                                                    "DELETE FROM pp_data_note WHERE uuid IN (SELECT uuid FROM pp_fixed WHERE player_uuid = '" + playerUUID.toString() + "');" +
                                                    "DELETE FROM pp_fixed WHERE player_uuid = '" + playerUUID.toString() + "';"
                                                    ); // @formatter:on
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            });
        }

        ParticleManager.removeAllFixedEffectsForPlayer(playerUUID);
    }

    /**
     * Gets a list of all saved fixed particle effects
     * 
     * @param callback The callback to execute with a list of all saved fixed particle effects
     */
    public void getAllFixedEffects(ConfigurationCallback<List<FixedParticleEffect>> callback) {
        if (!PlayerParticles.useMySQL) {
            List<FixedParticleEffect> fixedEffects = new ArrayList<FixedParticleEffect>();

            Set<String> playerKeys = playerDataYaml.getKeys(false);
            for (String playerKey : playerKeys) {
                if (playerDataYaml.isConfigurationSection(playerKey + ".fixedEffect")) {
                    Set<String> fixedEffectKeys = playerDataYaml.getConfigurationSection(playerKey + ".fixedEffect").getKeys(false);

                    for (String fixedEffectKey : fixedEffectKeys) {
                        ConfigurationSection section = playerDataYaml.getConfigurationSection(playerKey + ".fixedEffect." + fixedEffectKey);
                        ConfigurationSection effectSection = section.getConfigurationSection("effect");
                        ConfigurationSection styleSection = section.getConfigurationSection("style");
                        ConfigurationSection itemDataSection = section.getConfigurationSection("itemData");
                        ConfigurationSection blockDataSection = section.getConfigurationSection("blockData");
                        ConfigurationSection colorDataSection = section.getConfigurationSection("colorData");
                        ConfigurationSection noteColorDataSection = section.getConfigurationSection("noteColorData");

                        int id = section.getInt("id");
                        String worldName = section.getString("worldName");
                        double xPos = section.getDouble("xPos");
                        double yPos = section.getDouble("yPos");
                        double zPos = section.getDouble("zPos");
                        ParticleEffect particleEffect = ParticleEffect.fromName(effectSection.getString("name"));
                        ParticleStyle particleStyle = ParticleStyleManager.styleFromString(styleSection.getString("name"));
                        ItemData particleItemData = new ItemData(Material.matchMaterial(itemDataSection.getString("material")), (byte) itemDataSection.getInt("data"));
                        BlockData particleBlockData = new BlockData(Material.matchMaterial(blockDataSection.getString("material")), (byte) blockDataSection.getInt("data"));
                        OrdinaryColor particleColorData = new OrdinaryColor(colorDataSection.getInt("r"), colorDataSection.getInt("g"), colorDataSection.getInt("b"));
                        NoteColor particleNoteColorData = new NoteColor(noteColorDataSection.getInt("note"));

                        fixedEffects.add(new FixedParticleEffect(UUID.fromString(playerKey), id, worldName, xPos, yPos, zPos, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData));
                    }
                }
            }

            callback.execute(fixedEffects);
        } else { // @formatter:off
            async(() -> {
                PlayerParticles.mySQL.connect((connection) -> {
                    String query = "SELECT * FROM pp_fixed f " + 
                                   "JOIN pp_data_item i ON f.uuid = i.uuid " + 
                                   "JOIN pp_data_block b ON f.uuid = b.uuid " +
                                   "JOIN pp_data_color c ON f.uuid = c.uuid " +
                                   "JOIN pp_data_note n ON f.uuid = n.uuid";
                    try (Statement statement = connection.createStatement();
                         ResultSet res = statement.executeQuery(query)) {
                        
                        List<FixedParticleEffect> fixedEffects = new ArrayList<FixedParticleEffect>();

                        while (res.next()) {
                            UUID pplayerUUID = UUID.fromString(res.getString("f.player_uuid"));
                            int id = res.getInt("f.id");
                            String worldName = res.getString("f.worldName");
                            double xPos = res.getDouble("f.xPos");
                            double yPos = res.getDouble("f.yPos");
                            double zPos = res.getDouble("f.zPos");
                            ParticleEffect particleEffect = ParticleManager.effectFromString(res.getString("f.effect"));
                            ParticleStyle particleStyle = ParticleStyleManager.styleFromString(res.getString("f.style"));
                            ItemData particleItemData = new ItemData(Material.matchMaterial(res.getString("i.material")), res.getByte("i.data"));
                            BlockData particleBlockData = new BlockData(Material.matchMaterial(res.getString("b.material")), res.getByte("b.data"));
                            OrdinaryColor particleColorData = new OrdinaryColor(res.getInt("c.r"), res.getInt("c.g"), res.getInt("c.b"));
                            NoteColor particleNoteColorData = new NoteColor(res.getByte("n.note"));
                            
                            fixedEffects.add(new FixedParticleEffect(pplayerUUID, id, worldName, xPos, yPos, zPos, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData));
                        }

                        sync(() -> callback.execute(fixedEffects));
                    }
                });
            });
        }
    }

    /**
     * Gets a fixed effect for a pplayer by its id
     * 
     * @param pplayerUUID The player who owns the effect
     * @param id The id for the effect to get
     * @param callback The callback to execute with the effect, if one exists
     */
    public void getFixedEffectForPlayerById(UUID pplayerUUID, int id, ConfigurationCallback<FixedParticleEffect> callback) {
        if (!PlayerParticles.useMySQL) {
            if (playerDataYaml.isConfigurationSection(pplayerUUID.toString() + ".fixedEffect." + id)) {
                ConfigurationSection section = playerDataYaml.getConfigurationSection(pplayerUUID + ".fixedEffect." + id);
                ConfigurationSection effectSection = section.getConfigurationSection("effect");
                ConfigurationSection styleSection = section.getConfigurationSection("style");
                ConfigurationSection itemDataSection = section.getConfigurationSection("itemData");
                ConfigurationSection blockDataSection = section.getConfigurationSection("blockData");
                ConfigurationSection colorDataSection = section.getConfigurationSection("colorData");
                ConfigurationSection noteColorDataSection = section.getConfigurationSection("noteColorData");

                String worldName = section.getString("worldName");
                double xPos = section.getDouble("xPos");
                double yPos = section.getDouble("yPos");
                double zPos = section.getDouble("zPos");
                ParticleEffect particleEffect = ParticleEffect.fromName(effectSection.getString("name"));
                ParticleStyle particleStyle = ParticleStyleManager.styleFromString(styleSection.getString("name"));
                ItemData particleItemData = new ItemData(Material.matchMaterial(itemDataSection.getString("material")), (byte) itemDataSection.getInt("data"));
                BlockData particleBlockData = new BlockData(Material.matchMaterial(blockDataSection.getString("material")), (byte) blockDataSection.getInt("data"));
                OrdinaryColor particleColorData = new OrdinaryColor(colorDataSection.getInt("r"), colorDataSection.getInt("g"), colorDataSection.getInt("b"));
                NoteColor particleNoteColorData = new NoteColor(noteColorDataSection.getInt("note"));
                
                callback.execute(new FixedParticleEffect(pplayerUUID, id, worldName, xPos, yPos, zPos, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData));
            }
        } else { 
            async(() -> {
                PlayerParticles.mySQL.connect((connection) -> { // @formatter:off
                    String query = "SELECT * FROM pp_fixed f " +  
                                   "JOIN pp_data_item i ON f.uuid = i.uuid " +
                                   "JOIN pp_data_block b ON f.uuid = b.uuid " +
                                   "JOIN pp_data_color c ON f.uuid = c.uuid " +
                                   "JOIN pp_data_note n ON f.uuid = n.uuid " +
                                   "WHERE f.player_uuid = '" + pplayerUUID.toString() + "' AND f.id = '" + id + "'"; // @formatter:on
                    try (Statement statement = connection.createStatement();
                         ResultSet res = statement.executeQuery(query)) {
                        
                        if (res.next()) {
                            String worldName = res.getString("f.worldName");
                            double xPos = res.getDouble("f.xPos");
                            double yPos = res.getDouble("f.yPos");
                            double zPos = res.getDouble("f.zPos");
                            ParticleEffect particleEffect = ParticleManager.effectFromString(res.getString("f.effect"));
                            ParticleStyle particleStyle = ParticleStyleManager.styleFromString(res.getString("f.style"));
                            ItemData particleItemData = new ItemData(Material.matchMaterial(res.getString("i.material")), res.getByte("i.data"));
                            BlockData particleBlockData = new BlockData(Material.matchMaterial(res.getString("b.material")), res.getByte("b.data"));
                            OrdinaryColor particleColorData = new OrdinaryColor(res.getInt("c.r"), res.getInt("c.g"), res.getInt("c.b"));
                            NoteColor particleNoteColorData = new NoteColor(res.getByte("n.note"));

                            sync(() -> callback.execute(new FixedParticleEffect(pplayerUUID, id, worldName, xPos, yPos, zPos, particleEffect, particleStyle, particleItemData, particleBlockData, particleColorData, particleNoteColorData)));
                        }
                    }
                });
            });
        }
    }

    /**
     * Gets a list of all fixed effect ids for a player
     * 
     * @param pplayerUUID The player
     * @param callback The callback to execute with a list of all fixed effect ids for the given player
     */
    public void getFixedEffectIdsForPlayer(UUID pplayerUUID, ConfigurationCallback<List<Integer>> callback) {
        if (!PlayerParticles.useMySQL) {
            List<Integer> ids = new ArrayList<Integer>();
            
            if (playerDataYaml.isConfigurationSection(pplayerUUID.toString() + ".fixedEffect")) {
                Set<String> keys = playerDataYaml.getConfigurationSection(pplayerUUID.toString() + ".fixedEffect").getKeys(false);
                for (String key : keys) {
                    ids.add(Integer.parseInt(key));
                }
            }
            
            callback.execute(ids);
        } else {
            async(() -> {
                PlayerParticles.mySQL.connect((connection) -> {
                    try (Statement statement = connection.createStatement();
                         ResultSet res = statement.executeQuery("SELECT id FROM pp_fixed WHERE player_uuid = '" + pplayerUUID.toString() + "'")) {
                        
                        List<Integer> ids = new ArrayList<Integer>();
                        
                        while (res.next()) {
                            ids.add(res.getInt(1));
                        }
                        
                        sync(() -> callback.execute(ids));
                    }
                });
            });
        }
    }

    /**
     * Checks if the given player has reached the max number of fixed effects
     * 
     * @param pplayerUUID The player to check
     * @param callback The callback to execute with if the player can create any more fixed effects
     */
    public void hasPlayerReachedMaxFixedEffects(UUID pplayerUUID, ConfigurationCallback<Boolean> callback) {
        if (maxFixedEffects == -1) { // Initialize on the fly
            maxFixedEffects = PlayerParticles.getPlugin().getConfig().getInt("max-fixed-effects");
        }

        if (Bukkit.getPlayer(pplayerUUID).hasPermission("playerparticles.fixed.unlimited")) {
            callback.execute(false);
            return;
        }

        if (!PlayerParticles.useMySQL) {
            if (playerDataYaml.isConfigurationSection(pplayerUUID.toString() + ".fixedEffect")) {
                callback.execute(playerDataYaml.getConfigurationSection(pplayerUUID.toString() + ".fixedEffect").getKeys(false).size() >= maxFixedEffects);
            } else callback.execute(false);
        } else {
            async(() -> {
                PlayerParticles.mySQL.connect((connection) -> {
                    try (Statement statement = connection.createStatement();
                         ResultSet res = statement.executeQuery("SELECT COUNT(1) FROM pp_fixed WHERE player_uuid = '" + pplayerUUID.toString() + "'")) {
                        
                        boolean hasReachedMax;
                        if (res.next()) 
                            hasReachedMax = res.getInt(1) >= maxFixedEffects;
                        else 
                            hasReachedMax = false;
                        
                        sync(() -> callback.execute(hasReachedMax));
                    }
                });
            });
        }
    }

    /**
     * Gets the next Id for a player's fixed effects
     * 
     * @param pplayerUUID The player to get the Id for
     * @param callback The callback to execute with the smallest available Id the player can use
     */
    public void getNextFixedEffectId(UUID pplayerUUID, ConfigurationCallback<Integer> callback) {
        if (!PlayerParticles.useMySQL) {
            if (!playerDataYaml.isConfigurationSection(pplayerUUID.toString() + ".fixedEffect")) {
                callback.execute(1);
                return;
            }
            
            Set<String> idsSet = playerDataYaml.getConfigurationSection(pplayerUUID.toString() + ".fixedEffect").getKeys(false);
            
            if (idsSet.isEmpty()) callback.execute(1);
            
            int[] ids = new int[idsSet.size()];
            int i = 0;
            for (String key : idsSet)
                ids[i++] = Integer.parseInt(key);

            callback.execute(ParticleUtils.getSmallestPositiveInt(ids));
        } else {
            async(() -> {
                PlayerParticles.mySQL.connect((connection) -> {
                    try (Statement statement = connection.createStatement();
                         ResultSet res = statement.executeQuery("SELECT id FROM pp_fixed WHERE player_uuid = '" + pplayerUUID.toString() + "'")) {
                        
                        Set<String> idsSet = new HashSet<String>();
                        
                        while (res.next()) 
                            idsSet.add(res.getInt(1) + "");
                        
                        if (idsSet.isEmpty()) {
                            sync(() -> callback.execute(1));
                            return;
                        }
                        
                        int[] ids = new int[idsSet.size()];
                        int i = 0;
                        for (String key : idsSet)
                            ids[i++] = Integer.parseInt(key);
                        
                        sync(() -> callback.execute(ParticleUtils.getSmallestPositiveInt(ids)));
                    }
                });
            });
        }
    }

    /**
     * Gets the max distance a fixed effect can be created from the player
     * 
     * @return The max distance a fixed effect can be created from the player
     */
    public int getMaxFixedEffectCreationDistance() {
        if (maxFixedEffectCreationDistance == -1) { // Initialize on the fly
            maxFixedEffectCreationDistance = PlayerParticles.getPlugin().getConfig().getInt("max-fixed-effect-creation-distance");
        }
        return maxFixedEffectCreationDistance;
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
    public List<String> getDisabledWorlds() {
        if (disabledWorlds == null) { // Initialize on the fly
            disabledWorlds = PlayerParticles.getPlugin().getConfig().getStringList("disabled-worlds");
        }
        return disabledWorlds;
    }
    
    /**
     * Asynchronizes the callback with it's own thread
     * 
     * @param asyncCallback The callback to run on a separate thread
     */
    private void async(SyncInterface asyncCallback) {
        new BukkitRunnable() {
            public void run() {
                System.out.println("Async with thread: " + Thread.currentThread().getName());
                asyncCallback.execute();
            }
        }.runTaskAsynchronously(PlayerParticles.getPlugin());
    }
    
    /**
     * Synchronizes the callback with the main thread
     * 
     * @param syncCallback The callback to run on the main thread
     */
    private void sync(SyncInterface syncCallback) {
        new BukkitRunnable() {
            public void run() {
                System.out.println("Resynced with thread: " + Thread.currentThread().getName());
                syncCallback.execute();
            }
        }.runTask(PlayerParticles.getPlugin());
    }
    
    /**
     * Provides an easy way to run a section of code either synchronously or asynchronously using a callback
     */
    private static interface SyncInterface {
        public void execute();
    }
    
    /**
     * Allows callbacks to be passed between configuration methods and executed for returning objects after database queries
     */
    public static interface ConfigurationCallback<T> {
        public void execute(T obj);
    }

}
