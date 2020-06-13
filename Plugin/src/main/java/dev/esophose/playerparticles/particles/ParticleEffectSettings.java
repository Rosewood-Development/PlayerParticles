package dev.esophose.playerparticles.particles;

import com.google.common.collect.ObjectArrays;
import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.util.ParticleUtils;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;

public class ParticleEffectSettings {

    private static final List<ParticleEffect> disabledByDefaultParticleEffects = Arrays.asList(ParticleEffect.ELDER_GUARDIAN, ParticleEffect.FLASH);

    private ParticleEffect particleEffect;

    private CommentedFileConfiguration config;
    private boolean enabledByDefault;

    private String effectName;
    private boolean enabled;
    private Material guiIconMaterial;

    public ParticleEffectSettings(ParticleEffect particleEffect) {
        this.particleEffect = particleEffect;
        this.enabledByDefault = !disabledByDefaultParticleEffects.contains(particleEffect);

        this.setDefaultSettings();
        this.loadSettings(false);
    }

    /**
     * Sets the default settings for the particle type
     */
    private void setDefaultSettings() {
        File directory = new File(PlayerParticles.getInstance().getDataFolder(), "effects");
        directory.mkdirs();

        File file = new File(directory, this.getInternalName() + ".yml");
        this.config = CommentedFileConfiguration.loadConfiguration(PlayerParticles.getInstance(), file);

        boolean changed = this.setIfNotExists("effect-name", this.getInternalName(), "The name the effect will display as");
        changed |= this.setIfNotExists("enabled", this.enabledByDefault, "If the effect is enabled or not");
        changed |= this.setIfNotExists("gui-icon-material", this.particleEffect.getGuiIconMaterialNames());

        if (changed)
            this.config.save();
    }

    /**
     * Loads the settings shared for each style then calls loadSettings(CommentedFileConfiguration)
     *
     * @param reloadConfig If the settings should be reloaded or not
     */
    public void loadSettings(boolean reloadConfig) {
        if (reloadConfig)
            this.setDefaultSettings();

        this.effectName = this.config.getString("effect-name");
        this.enabled = this.config.getBoolean("enabled");
        this.guiIconMaterial = ParticleUtils.closestMatchWithFallback(true, this.config.getStringList("gui-icon-material").toArray(new String[0]));
    }

    /**
     * Sets a value to the config if it doesn't already exist
     *
     * @param setting The setting name
     * @param value The setting value
     * @param comments Comments for the setting
     * @return true if changes were made
     */
    private boolean setIfNotExists(String setting, Object value, String... comments) {
        if (this.config.get(setting) != null)
            return false;

        String defaultMessage = "Default: ";
        if (value instanceof String && ParticleUtils.containsConfigSpecialCharacters((String) value)) {
            defaultMessage += "'" + value + "'";
        } else {
            defaultMessage += value;
        }

        this.config.set(setting, value, ObjectArrays.concat(comments, new String[] { defaultMessage }, String.class));
        return true;
    }

    /**
     * @return the internal name of this particle effect that will never change
     */
    public String getInternalName() {
        return this.particleEffect.name().toLowerCase();
    }

    /**
     * @return the name that the style will display to the users as
     */
    public String getName() {
        return this.effectName;
    }

    /**
     * @return The Material icon that represents this style in the GUI
     */
    public Material getGuiIconMaterial() {
        return this.guiIconMaterial;
    }

    /**
     * @return true if this effect is enabled, otherwise false
     */
    public boolean isEnabled() {
        return this.enabled;
    }

}
