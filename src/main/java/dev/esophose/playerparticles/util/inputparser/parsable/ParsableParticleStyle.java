package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.util.List;

public class ParsableParticleStyle extends Parsable<ParticleStyle> {

    public ParsableParticleStyle() {
        super(ParticleStyle.class);
    }

    @Override
    public ParticleStyle parse(PPlayer pplayer, List<String> inputs) {
        String input = inputs.remove(0);
        return ParticleStyle.fromName(input);
    }

}
