/**
 * Copyright Esophose 2016
 * While using any of the code provided by this plugin
 * you must not claim it as your own. This plugin may
 * be modified and installed on a server, but may not
 * be distributed to any person by any means.
 */

package com.esophose.playerparticles;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.esophose.playerparticles.libraries.particles.ParticleEffect.ParticleType;

public class ConfigManager {
	
	private static ConfigManager instance = new ConfigManager("effectData");
	private static ConfigManager styleInstance = new ConfigManager("styleData");
	private File file;
	private FileConfiguration config;
	
	public static ConfigManager getInstance() {
		return instance;
	}
	
	public static ConfigManager getStyleInstance() {
		return styleInstance;
	}
	
	private ConfigManager(String fileName) {		
		if (!PlayerParticles.getPlugin().getDataFolder().exists()) PlayerParticles.getPlugin().getDataFolder().mkdir();
		
		file = new File(PlayerParticles.getPlugin().getDataFolder(), fileName + ".yml");
		
		if (!file.exists()) {
			try { file.createNewFile(); }
			catch (Exception e) { e.printStackTrace(); }
		}
		
		config = YamlConfiguration.loadConfiguration(file);
	}
	
	public void flushData() {
		for(String key : config.getKeys(false)) {
			config.set(key, null);
		}
		try {config.save(file);}
		catch (IOException e) {e.printStackTrace();}
	}
	
	public static void flushDatabase() {
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("TRUNCATE playerparticles;");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setParticle(ParticleType type, Player player){
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("UPDATE playerparticles SET particle = '" + type.toString().toLowerCase().replace("_", "") + "' WHERE player_name = '" + player.getName() + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			config.set(player.getName(), type.toString().toLowerCase().replace("_", ""));
			try {config.save(file);}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public void resetParticle(Player player){
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("UPDATE playerparticles SET particle = NULL WHERE player_name = '" + player.getName() + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			config.set(player.getName(), ParticleStyle.NONE.toString().toLowerCase().replace("_", ""));
			try {config.save(file);}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public ParticleType getParticle(Player player){
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				ResultSet res = statement.executeQuery("SELECT * FROM playerparticles WHERE player_name = '" + player.getName() + "';");
				res.next();
				return ParticleCreator.particleFromString(res.getString("particle"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			String effectToLowerCase = (String) config.getString(player.getName());
			return ParticleCreator.particleFromString(effectToLowerCase);
		}
		return null;
	}
	
	public void setStyle(ParticleStyle style, Player player) {
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("UPDATE playerparticles SET style = '" + style.toString().toLowerCase().replace("_", "") + "' WHERE player_name = '" + player.getName() + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			config.set(player.getName(), style.toString().toLowerCase().replace("_", ""));
			try {config.save(file);}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public void resetStyle(Player player) {
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				statement.executeUpdate("UPDATE playerparticles SET style = '" + ParticleStyle.NONE.toString().toLowerCase().replace("_", "") + "' WHERE player_name = '" + player.getName() + "';");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			config.set(player.getName(), ParticleStyle.NONE.toString().toLowerCase().replace("_", ""));
			try {config.save(file);}
			catch (IOException e) {e.printStackTrace();}
		}
	}
	
	public ParticleStyle getStyle(Player player) {
		if(PlayerParticles.useMySQL) {
			Statement statement;
			try {
				statement = PlayerParticles.c.createStatement();
				ResultSet res = statement.executeQuery("SELECT * FROM playerparticles WHERE player_name = '" + player.getName() + "';");
				res.next();
				return ParticleStyle.styleFromString(res.getString("style"));
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}else{
			String styleToLowerCase = (String) config.getString(player.getName());
			return ParticleStyle.styleFromString(styleToLowerCase);
		}
		return ParticleStyle.NONE;
	}
	
	@SuppressWarnings("unchecked")
	public boolean isWorldDisabled(String world) {
		if(PlayerParticles.getPlugin().getConfig().get("disabled-worlds") != null && ((ArrayList<String>) PlayerParticles.getPlugin().getConfig().get("disabled-worlds")).contains(world)) {
			return true;
		}else return false;
	}
	
	@SuppressWarnings("unchecked")
	public ArrayList<String> getDisabledWorlds() {
		if(PlayerParticles.getPlugin().getConfig().get("disabled-worlds") != null) {
			return ((ArrayList<String>) PlayerParticles.getPlugin().getConfig().get("disabled-worlds"));
		}else return null;
	}
	
	public void updateConfig(JavaPlugin plugin) {
        HashMap<String, Object> newConfig = getConfigVals();
        FileConfiguration c = plugin.getConfig();
        for (String var : c.getKeys(false)) {
            newConfig.remove(var);
        }
        if (newConfig.size() != 0) {
            for (String key : newConfig.keySet()) {
                c.set(key, newConfig.get(key));
            }
            try {
            	
                c.set("version", getVersion());
                c.save(new File(plugin.getDataFolder(), "config.yml"));
            } catch (IOException e) {}
        }
    }
	
    public HashMap<String, Object> getConfigVals() {
        HashMap<String, Object> var = new HashMap<>();
        YamlConfiguration config = new YamlConfiguration();
        try {
            config.loadFromString(stringFromInputStream(PlayerParticles.class.getResourceAsStream("/config.yml")));
        } catch (InvalidConfigurationException e) {}
        for (String key : config.getKeys(false)) {
            var.put(key, config.get(key));
        }
        return var;
    }
    
    public double getVersion() {
    	double version = -1;
    	try {
    		YamlConfiguration config = new YamlConfiguration();
    		config.loadFromString(stringFromInputStream(PlayerParticles.class.getResourceAsStream("/config.yml")));
    		version = config.getDouble("version");
    	}catch(InvalidConfigurationException e) { }
    	return version;
    }
    
    public String stringFromInputStream(InputStream in) {
    	Scanner scanner = new Scanner(in);
    	String string = scanner.useDelimiter("\\A").next();
    	scanner.close();
        return string;
    }
	
}
