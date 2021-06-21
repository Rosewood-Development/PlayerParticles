package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.awt.Color;
import java.util.List;

public class ParsableColorTransition extends Parsable<ColorTransition> {

    public ParsableColorTransition() {
        super(ColorTransition.class);
    }

    @Override
    public ColorTransition parse(PPlayer pplayer, List<String> inputs) {
        OrdinaryColor startColor = this.parseColor(inputs);
        OrdinaryColor endColor = this.parseColor(inputs);
        return new ColorTransition(startColor, endColor);
    }

    private OrdinaryColor parseColor(List<String> inputs) {
        String input = inputs.remove(0);

        // Try hex values first
        if (input.startsWith("#")) {
            try {
                Color color = Color.decode(input);
                return new OrdinaryColor(color.getRed(), color.getGreen(), color.getBlue());
            } catch (NumberFormatException ignored) { }
        }

        // Try color names
        OrdinaryColor namedColor = ParsableOrdinaryColor.COLOR_NAME_MAP.get(input.toLowerCase());
        if (namedColor != null)
            return namedColor;

        String input2 = inputs.remove(0);
        String input3 = inputs.remove(0);

        // Use rgb
        return new OrdinaryColor(Integer.parseInt(input), Integer.parseInt(input2), Integer.parseInt(input3));
    }
    
}
