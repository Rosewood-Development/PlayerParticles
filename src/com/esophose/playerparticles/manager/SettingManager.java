package com.esophose.playerparticles.manager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;

import com.esophose.playerparticles.PlayerParticles;
import com.esophose.playerparticles.particles.ParticleEffect;
import com.esophose.playerparticles.util.ParticleUtils;

public class SettingManager {
    
    /**
     * The types of settings that can be loaded
     */
    private enum PSettingType {
        BOOLEAN,
        INTEGER,
        LONG,
        DOUBLE,
        STRING,
        STRING_LIST
    }
    
    /**
     * An enum containing all settings in the config.yml for the plugin
     */
    public enum PSetting {
        VERSION(PSettingType.DOUBLE),
        TICKS_PER_PARTICLE(PSettingType.LONG),
        CHECK_UPDATES(PSettingType.BOOLEAN),
        SEND_METRICS(PSettingType.BOOLEAN),
        GUI_ENABLED(PSettingType.BOOLEAN),
        GUI_BUTTON_SOUND(PSettingType.BOOLEAN),
        
        TOGGLE_ON_MOVE(PSettingType.BOOLEAN),
        TOGGLE_ON_MOVE_DELAY(PSettingType.INTEGER),
        
        PARTICLE_RENDER_RANGE_PLAYER(PSettingType.INTEGER),
        PARTICLE_RENDER_RANGE_FIXED_EFFECT(PSettingType.INTEGER),
        
        MESSAGES_ENABLED(PSettingType.BOOLEAN),
        USE_MESSAGE_PREFIX(PSettingType.BOOLEAN),
        MESSAGE_PREFIX(PSettingType.STRING),
        
        DATABASE_ENABLE(PSettingType.BOOLEAN),
        DATABASE_HOSTNAME(PSettingType.STRING),
        DATABASE_PORT(PSettingType.STRING),
        DATABASE_NAME(PSettingType.STRING),
        DATABASE_USER_NAME(PSettingType.STRING),
        DATABASE_USER_PASSWORD(PSettingType.STRING),
        
        MAX_FIXED_EFFECTS(PSettingType.INTEGER),
        MAX_FIXED_EFFECT_CREATION_DISTANCE(PSettingType.INTEGER),
        
        MAX_PARTICLES(PSettingType.INTEGER),
        
        MAX_GROUPS(PSettingType.INTEGER),
        
        RAINBOW_CYCLE_SPEED(PSettingType.INTEGER),
        
        DISABLED_WORLDS(PSettingType.STRING_LIST),
        
        LANG_FILE(PSettingType.STRING);
        
        private final PSettingType settingType;
        private Object value = null;
        
        private PSetting(PSettingType settingType) {
            this.settingType = settingType;
        }
        
        /**
         * Resets the setting's value so it will be fetched from the config the next time it's needed
         */
        private void resetDefault() {
            this.value = null;
        }
        
        /**
         * Gets the value from cache, or the config.yml if it isn't loaded yet
         * 
         * @return The value of this setting
         */
        private Object getValue() {
            if (this.value == null) {
                String configPath = this.name().toLowerCase().replaceAll("_", "-");
                switch (this.settingType) {
                case BOOLEAN:
                    this.value = PlayerParticles.getPlugin().getConfig().getBoolean(configPath);
                    break;
                case INTEGER:
                    this.value = PlayerParticles.getPlugin().getConfig().getInt(configPath);
                    break;
                case LONG:
                    this.value = PlayerParticles.getPlugin().getConfig().getLong(configPath);
                    break;
                case DOUBLE:
                    this.value = PlayerParticles.getPlugin().getConfig().getDouble(configPath);
                    break;
                case STRING:
                    this.value = PlayerParticles.getPlugin().getConfig().getString(configPath);
                    break;
                case STRING_LIST:
                    this.value = PlayerParticles.getPlugin().getConfig().getStringList(configPath);
                    break;
                }
            }
            return this.value;
        }
        
        /**
         * Gets the setting's value as a boolean
         * 
         * @return The setting's value as a boolean
         */
        public boolean getBoolean() {
            return (boolean) this.getValue();
        }
        
        /**
         * Gets the setting's value as an int
         * 
         * @return The setting's value as an int
         */
        public int getInt() {
            return (int) this.getValue();
        }
        
        /**
         * Gets the setting's value as a long
         * 
         * @return The setting's value as a long
         */
        public long getLong() {
            return (long) this.getValue();
        }
        
        /**
         * Gets the setting's value as a double
         * 
         * @return The setting's value as a double
         */
        public double getDouble() {
            return (double) this.getValue();
        }
        
        /**
         * Gets the setting's value as a String
         * 
         * @return The setting's value as a String
         */
        public String getString() {
            return (String) this.getValue();
        }
        
        /**
         * Gets the setting's value as a List of Strings
         * 
         * @return The setting's value as a List of Strings
         */
        @SuppressWarnings("unchecked")
        public List<String> getStringList() {
            return (List<String>) this.getValue();
        }
        
        /**
         * Gets the setting's value as a Material
         * 
         * @return The setting's value as a Material
         */
        public Material getMaterial() {
            return (Material) this.getValue();
        }
    }
    
    /**
     * Used for grabbing/caching configurable GUI Icons from the config.yml
     */
    public enum GuiIcon {
        PARTICLES,
        GROUPS,
        PRESET_GROUPS,
        BACK,
        CREATE,
        EDIT_EFFECT,
        EDIT_STYLE,
        EDIT_DATA,
        RANDOMIZE,
        RESET,
        
        EFFECT,
        STYLE;
        
        private Map<String, Material> materials;
        
        private GuiIcon() {
            this.materials = new HashMap<String, Material>();
        }
        
        /**
         * Gets the Material for this icon from the 'gui-icon.misc' section in the config.yml
         * Tries to get from cache first, otherwise loads it
         * 
         * @return The Material for this Icon
         */
        public Material get() {
            return this.getInternal("gui-icon.misc." + this.name().toLowerCase());
        }
        
        /**
         * Gets the Material for a subsection of this icon in the config.yml
         * Tries to get from cache first, otherwise loads it
         * 
         * @param subsection The name of the icon in the section
         * @return The Material for this Icon
         */
        public Material get(String subsection) {
            return this.getInternal("gui-icon." + this.name().toLowerCase() + "." + subsection);
        }
        
        /**
         * Gets the Material for this icon
         * Tries to get from cache first, otherwise loads it
         * 
         * @param configPath Where to look in the config.yml for the Material name
         * @return The path in the config.yml to lookup
         */
        private Material getInternal(String configPath) {
            Material material = this.materials.get(configPath);
            if (material != null)
                return material;
            
            List<String> materials = PlayerParticles.getPlugin().getConfig().getStringList(configPath);
            
            try {
                if (materials.size() == 1) {
                    material = ParticleUtils.closestMatch(materials.get(0));
                } else {
                    if (ParticleEffect.VERSION_13) {
                        material = ParticleUtils.closestMatch(materials.get(0));
                    } else {
                        material = ParticleUtils.closestMatch(materials.get(1));
                    }
                }
            } catch (Exception e) {
                PlayerParticles.getPlugin().getLogger().severe("Missing GUI icon for '" + this.name().toLowerCase() + "'");
            }
            
            if (material == null)
                material = Material.BARRIER;
            
            this.materials.put(configPath, material);
            
            return material;
        }
        
        /**
         * Resets the setting's value so it will be fetched from the config the next time it's needed
         */
        private void resetDefault() {
            this.materials = new HashMap<String, Material>();
        }
    }

    private SettingManager() {
        
    }
    
    /**
     * Resets the settings to their default values
     */
    public static void reload() {
        for (PSetting setting : PSetting.values())
            setting.resetDefault();
        
        for (GuiIcon icon : GuiIcon.values())
            icon.resetDefault();
    }

}
