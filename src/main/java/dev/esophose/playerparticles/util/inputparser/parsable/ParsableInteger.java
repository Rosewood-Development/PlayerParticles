package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.util.List;

public class ParsableInteger extends Parsable<Integer> {

    public ParsableInteger() {
        super(Integer.class);
    }

    @Override
    public Integer parse(PPlayer pplayer, List<String> inputs) {
        String input = inputs.remove(0);
        return Integer.parseInt(input);
    }

}
