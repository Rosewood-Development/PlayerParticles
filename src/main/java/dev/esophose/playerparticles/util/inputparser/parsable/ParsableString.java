package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.util.List;

public class ParsableString extends Parsable<String> {

    public ParsableString() {
        super(String.class);
    }

    @Override
    public String parse(PPlayer pplayer, List<String> inputs) {
        return inputs.remove(0);
    }

}
