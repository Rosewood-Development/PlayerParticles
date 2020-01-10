package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.util.List;

public class ParsableParticleEffect extends Parsable<ParticleEffect> {

    public ParsableParticleEffect() {
        super(ParticleEffect.class);
    }

    @Override
    public ParticleEffect parse(PPlayer pplayer, List<String> inputs) {
        String input = inputs.remove(0);
        return ParticleEffect.fromName(input);
    }

}
