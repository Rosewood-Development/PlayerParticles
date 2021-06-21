package dev.esophose.playerparticles.util.inputparser;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableColorTransition;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableInteger;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableLocation;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableMaterial;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableNoteColor;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableOrdinaryColor;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableParticleEffect;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableParticleStyle;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableString;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableVibration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;

public class InputParser {

    private static Map<Class<?>, Parsable<?>> inputTypes;

    static {
        inputTypes = new HashMap<Class<?>, Parsable<?>>() {{
            this.put(Integer.class, new ParsableInteger());
            this.put(Location.class, new ParsableLocation());
            this.put(Material.class, new ParsableMaterial());
            this.put(NoteColor.class, new ParsableNoteColor());
            this.put(OrdinaryColor.class, new ParsableOrdinaryColor());
            this.put(ColorTransition.class, new ParsableColorTransition());
            this.put(Vibration.class, new ParsableVibration());
            this.put(ParticleEffect.class, new ParsableParticleEffect());
            this.put(ParticleStyle.class, new ParsableParticleStyle());
            this.put(String.class, new ParsableString());
        }};
    }

    private PPlayer pplayer;
    private List<String> input;

    public InputParser(PPlayer pplayer, String[] inputs) {
        this.pplayer = pplayer;
        this.input = new ArrayList<>(Arrays.asList(inputs));
    }

    /**
     * Gets the next input parsed and casted to the desired type
     *
     * @param type The type of input to parse
     * @param <T> The input class type
     * @return The parsed input, casted to the desired type, or null if it was unable to be parsed
     */
    @SuppressWarnings("unchecked")
    public <T> T next(Class<T> type) {
        if (!inputTypes.containsKey(type))
            throw new IllegalArgumentException("Cannot handle given type: " + type.getName());

        try {
            return (T) inputTypes.get(type).parse(this.pplayer, this.input);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @return true if there are remaining inputs to be parsed
     */
    public boolean hasNext() {
        return !this.input.isEmpty();
    }

    /**
     * @return the number of input values remaining
     */
    public int numRemaining() {
        return this.input.size();
    }

}
