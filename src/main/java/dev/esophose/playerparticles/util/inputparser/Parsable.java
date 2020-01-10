package dev.esophose.playerparticles.util.inputparser;

import dev.esophose.playerparticles.particles.PPlayer;
import java.util.List;

public abstract class Parsable<T> {

    protected Class<T> targetType;

    public Parsable(Class<T> targetType) {
        this.targetType = targetType;
    }

    /**
     * Consumes and parses input from the list
     *
     * @param pplayer The PPlayer who entered the input
     * @param inputs The input contents to try parsing
     * @return The parsed value or null if parsing failed
     */
    public abstract T parse(PPlayer pplayer, List<String> inputs);

}
