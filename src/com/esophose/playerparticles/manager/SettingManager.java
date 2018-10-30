package com.esophose.playerparticles.manager;

import java.util.List;

import com.esophose.playerparticles.PlayerParticles;

public class SettingManager {
    
    private enum PSettingType {
        BOOLEAN,
        INTEGER,
        LONG,
        DOUBLE,
        STRING,
        STRING_LIST
    }
    
    public enum PSetting {
        VERSION(PSettingType.DOUBLE),
        TICKS_PER_PARTICLE(PSettingType.LONG),
        CHECK_UPDATES(PSettingType.BOOLEAN),
        
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
    }

    private SettingManager() {
        
    }
    
    /**
     * Resets the settings to their default values
     */
    public static void reload() {
        for (PSetting setting : PSetting.values())
            setting.resetDefault();
    }

}
