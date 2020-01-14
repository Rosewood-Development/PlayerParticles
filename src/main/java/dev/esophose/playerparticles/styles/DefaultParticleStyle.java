package dev.esophose.playerparticles.styles;

import com.google.common.collect.ObjectArrays;
import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.util.ParticleUtils;
import java.io.File;

public abstract class DefaultParticleStyle implements ParticleStyle {

    protected PlayerParticles playerParticles;
    private CommentedFileConfiguration config;
    private String internalStyleName;
    private boolean canBeFixedByDefault;
    private boolean canToggleWithMovementByDefault;
    private double fixedEffectOffsetByDefault;

    private String styleName;
    private boolean enabled;
    private boolean canBeFixed;
    private boolean canToggleWithMovement;
    private double fixedEffectOffset;

    public DefaultParticleStyle(String internalStyleName, boolean canBeFixedByDefault, boolean canToggleWithMovementByDefault, double fixedEffectOffsetByDefault) {
        this.internalStyleName = internalStyleName;
        this.canBeFixedByDefault = canBeFixedByDefault;
        this.canToggleWithMovementByDefault = canToggleWithMovementByDefault;
        this.fixedEffectOffsetByDefault = fixedEffectOffsetByDefault;
        this.playerParticles = PlayerParticles.getInstance();

        this.setDefaultSettings();
        this.loadSettings(false);
    }

    /**
     * Sets the default settings shared for each style then calls setDefaultSettings(CommentedFileConfiguration)
     */
    private void setDefaultSettings() {
        File directory = new File(this.playerParticles.getDataFolder(), "styles");
        directory.mkdirs();

        File file = new File(directory, this.internalStyleName + ".yml");
        this.config = CommentedFileConfiguration.loadConfiguration(PlayerParticles.getInstance(), file);

        this.setIfNotExists("style-name", this.internalStyleName, "The name the style will display as");
        this.setIfNotExists("enabled", true, "If the style is enabled or not");
        this.setIfNotExists("can-be-fixed", this.canBeFixedByDefault, "If the style can be used in /pp fixed");
        this.setIfNotExists("can-toggle-with-movement", this.canToggleWithMovementByDefault, "If the style will only be shown at the player's feet while moving");
        this.setIfNotExists("fixed-effect-offset", this.fixedEffectOffsetByDefault, "How far vertically to offset the style position for fixed effects");

        this.setDefaultSettings(this.config);
        this.config.save();
    }

    /**
     * Loads the settings shared for each style then calls loadSettings(CommentedFileConfiguration)
     */
    public final void loadSettings(boolean reloadConfig) {
        if (reloadConfig)
            this.config.reloadConfig();

        this.styleName = this.config.getString("style-name");
        this.enabled = this.config.getBoolean("enabled");
        this.canBeFixed = this.config.getBoolean("can-be-fixed");
        this.canToggleWithMovement = this.config.getBoolean("can-toggle-with-movement");
        this.fixedEffectOffset = this.config.getDouble("fixed-effect-offset");

        this.loadSettings(this.config);
    }

    /**
     * Sets a value to the config if it doesn't already exist
     *
     * @param setting The setting name
     * @param value The setting value
     */
    protected final void setIfNotExists(String setting, Object value, String... comments) {
        if (this.config.get(setting) != null)
            return;

        String defaultMessage = "Default: ";
        if (value instanceof String && ParticleUtils.containsConfigSpecialCharacters((String) value)) {
            defaultMessage += "'" + value + "'";
        } else {
            defaultMessage += value;
        }

        this.config.set(setting, value, ObjectArrays.concat(comments, new String[] { defaultMessage }, String.class));
    }

    @Override
    public final boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public final String getName() {
        return this.styleName;
    }

    @Override
    public final String getInternalName() {
        return this.internalStyleName;
    }

    @Override
    public final boolean canBeFixed() {
        return this.canBeFixed;
    }

    @Override
    public final boolean canToggleWithMovement() {
        return this.canToggleWithMovement;
    }

    @Override
    public final double getFixedEffectOffset() {
        return this.fixedEffectOffset;
    }

    /**
     * Sets the default settings for this style
     *
     * @param config The config to save to
     */
    protected abstract void setDefaultSettings(CommentedFileConfiguration config);

    /**
     * Loads the settings for this style
     *
     * @param config The config to load from
     */
    protected abstract void loadSettings(CommentedFileConfiguration config);

}
