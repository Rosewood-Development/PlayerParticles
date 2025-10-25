package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.data.TransparentColor;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.awt.Color;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParsableTransparentColor extends Parsable<TransparentColor> {

    public static final Map<String, TransparentColor> COLOR_NAME_MAP = ParsableOrdinaryColor.COLOR_NAME_MAP.entrySet()
            .stream()
            .map(entry -> new AbstractMap.SimpleEntry<>(entry.getKey(), new TransparentColor(entry.getValue(), 255)))
            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    public ParsableTransparentColor() {
        super(TransparentColor.class);
    }

    @Override
    public TransparentColor parse(PPlayer pplayer, List<String> inputs) {
        String input = inputs.remove(0);

        // Try hex values first
        if (input.startsWith("#")) {
            try {
                Color color = Color.decode(input);
                return new TransparentColor(color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
            } catch (NumberFormatException ignored) { }
        }

        // Try color names
        TransparentColor namedColor = COLOR_NAME_MAP.get(input.toLowerCase());
        if (namedColor != null)
            return namedColor;

        String input2 = inputs.remove(0);
        String input3 = inputs.remove(0);

        int alpha = inputs.isEmpty() ? 255 : Integer.parseInt(inputs.remove(0));

        // Use rgb
        return new TransparentColor(Integer.parseInt(input), Integer.parseInt(input2), Integer.parseInt(input3), alpha);
    }
    
}
