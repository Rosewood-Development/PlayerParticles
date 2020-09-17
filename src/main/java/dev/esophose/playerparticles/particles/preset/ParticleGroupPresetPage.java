package dev.esophose.playerparticles.particles.preset;

import dev.esophose.playerparticles.gui.GuiInventory.BorderColor;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ParticleGroupPresetPage {

    private final String title;
    private final List<ParticleGroupPreset> presets;
    private final Map<Integer, BorderColor> extraBorder;

    public ParticleGroupPresetPage(String title, List<ParticleGroupPreset> presets, Map<Integer, BorderColor> extraBorder) {
        this.title = title;
        this.presets = presets;
        this.extraBorder = extraBorder;
    }

    /**
     * @return the title of this preset page
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * @return the presets to display on this page
     */
    public List<ParticleGroupPreset> getPresets() {
        return Collections.unmodifiableList(this.presets);
    }

    /**
     * @return the extra border to display on the page
     */
    public Map<Integer, BorderColor> getExtraBorder() {
        return Collections.unmodifiableMap(this.extraBorder);
    }

}
