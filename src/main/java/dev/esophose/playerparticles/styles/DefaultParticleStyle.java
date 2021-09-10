package dev.esophose.playerparticles.styles;

import com.google.common.collect.ObjectArrays;
import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.util.ParticleUtils;
import java.io.File;
import java.util.List;
import org.bukkit.Material;

public abstract class DefaultParticleStyle implements ParticleStyle {

    protected PlayerParticles playerParticles;
    private CommentedFileConfiguration config;
    private boolean changed;

    private String internalStyleName;
    private boolean canBeFixedByDefault;
    private boolean canToggleWithMovementByDefault;
    private boolean canToggleWithCombatByDefault;
    private double fixedEffectOffsetByDefault;

    private String styleName;
    private boolean enabled;
    private boolean canBeFixed;
    private boolean canToggleWithMovement;
    private boolean canToggleWithCombat;
    private double fixedEffectOffset;
    private Material guiIconMaterial;

    protected DefaultParticleStyle(String internalStyleName, boolean canBeFixedByDefault, boolean canToggleWithMovementByDefault, double fixedEffectOffsetByDefault) {
        this.internalStyleName = internalStyleName;
        this.canBeFixedByDefault = canBeFixedByDefault;
        this.canToggleWithMovementByDefault = canToggleWithMovementByDefault;
        this.canToggleWithCombatByDefault = true;
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

        this.changed = false;
        this.setIfNotExists("style-name", this.internalStyleName, "The name the style will display as");
        this.setIfNotExists("enabled", true, "If the style is enabled or not");
        this.setIfNotExists("can-be-fixed", this.canBeFixedByDefault, "If the style can be used in /pp fixed");
        this.setIfNotExists("can-toggle-with-movement", this.canToggleWithMovementByDefault, "If the style will toggle to a different appearance when the player is moving", "Also requires the setting in the config.yml to be enabled");
        this.setIfNotExists("can-toggle-with-combat", this.canToggleWithCombatByDefault, "If particles for this style will be hidden if the player is in combat", "Also requires the setting in the config.yml to be enabled");
        this.setIfNotExists("fixed-effect-offset", this.fixedEffectOffsetByDefault, "How far vertically to offset the style position for fixed effects");
        this.setIfNotExists("gui-icon-material", this.getGuiIconMaterialNames(), "The material of the icon to display in the GUI");

        this.setDefaultSettings(this.config);

        if (this.changed)
            this.config.save();
    }

    /**
     * Loads the settings shared for each style then calls loadSettings(CommentedFileConfiguration)
     *
     * @param reloadConfig If the settings should be reloaded or not
     */
    public final void loadSettings(boolean reloadConfig) {
        if (reloadConfig)
            this.setDefaultSettings();

        this.styleName = this.config.getString("style-name");
        this.enabled = this.config.getBoolean("enabled");
        this.canBeFixed = this.config.getBoolean("can-be-fixed");
        this.canToggleWithMovement = this.config.getBoolean("can-toggle-with-movement");
        this.canToggleWithCombat = this.config.getBoolean("can-toggle-with-combat");
        this.fixedEffectOffset = this.config.getDouble("fixed-effect-offset");
        this.guiIconMaterial = ParticleUtils.closestMatchWithFallback(true, this.config.getStringList("gui-icon-material").toArray(new String[0]));

        this.loadSettings(this.config);
    }

    /**
     * Sets a value to the config if it doesn't already exist
     *
     * @param setting The setting name
     * @param value The setting value
     * @param comments Comments for the setting
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
        this.changed = true;
    }

    @Override
    public final boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public final String getInternalName() {
        return this.internalStyleName;
    }

    @Override
    public final String getName() {
        return this.styleName;
    }

    @Override
    public final Material getGuiIconMaterial() {
        return this.guiIconMaterial;
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
    public final boolean canToggleWithCombat() {
        return this.canToggleWithCombat;
    }

    @Override
    public final double getFixedEffectOffset() {
        return this.fixedEffectOffset;
    }

    /**
     * @return A list of Strings to try to turn into Materials
     */
    protected abstract List<String> getGuiIconMaterialNames();

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
