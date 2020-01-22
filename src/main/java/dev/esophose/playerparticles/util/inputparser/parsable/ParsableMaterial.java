package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.util.List;
import org.bukkit.Material;

public class ParsableMaterial extends Parsable<Material> {

    public ParsableMaterial() {
        super(Material.class);
    }

    @Override
    public Material parse(PPlayer pplayer, List<String> inputs) {
        String input = inputs.remove(0);
        return ParticleUtils.closestMatch(input);
    }

}
