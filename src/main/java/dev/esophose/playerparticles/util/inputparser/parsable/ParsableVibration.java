package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.util.List;

public class ParsableVibration extends Parsable<Vibration> {

    public ParsableVibration() {
        super(Vibration.class);
    }

    @Override
    public Vibration parse(PPlayer pplayer, List<String> inputs) {
        String input = inputs.remove(0);
        return new Vibration(Math.abs(Integer.parseInt(input)));
    }

}
