package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.awt.Color;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ParsableOrdinaryColor extends Parsable<OrdinaryColor> {

    public static final Map<String, OrdinaryColor> COLOR_NAME_MAP;

    static {
        COLOR_NAME_MAP = new HashMap<String, OrdinaryColor>() {{
            this.put("red", new OrdinaryColor(255, 0, 0));
            this.put("orange", new OrdinaryColor(255, 140, 0));
            this.put("yellow", new OrdinaryColor(255, 255, 0));
            this.put("lime", new OrdinaryColor(50, 205, 50));
            this.put("green", new OrdinaryColor(0, 128, 0));
            this.put("blue", new OrdinaryColor(0, 0, 255));
            this.put("cyan", new OrdinaryColor(0, 139, 139));
            this.put("light_blue", new OrdinaryColor(173, 216, 230));
            this.put("purple", new OrdinaryColor(138, 43, 226));
            this.put("magenta", new OrdinaryColor(202, 31, 123));
            this.put("pink", new OrdinaryColor(255, 182, 193));
            this.put("brown", new OrdinaryColor(139, 69, 19));
            this.put("black", new OrdinaryColor(0, 0, 0));
            this.put("gray", new OrdinaryColor(128, 128, 128));
            this.put("light_gray", new OrdinaryColor(192, 192, 192));
            this.put("white", new OrdinaryColor(255, 255, 255));
            this.put("rainbow", OrdinaryColor.RAINBOW);
            this.put("random", OrdinaryColor.RANDOM);
        }};
    }

    public ParsableOrdinaryColor() {
        super(OrdinaryColor.class);
    }

    @Override
    public OrdinaryColor parse(PPlayer pplayer, List<String> inputs) {
        String input = inputs.remove(0);

        // Try hex values first
        if (input.startsWith("#")) {
            try {
                Color color = Color.decode(input);
                return new OrdinaryColor(color.getRed(), color.getGreen(), color.getBlue());
            } catch (NumberFormatException ignored) { }
        }

        // Try color names
        OrdinaryColor namedColor = COLOR_NAME_MAP.get(input.toLowerCase());
        if (namedColor != null)
            return namedColor;

        String input2 = inputs.remove(0);
        String input3 = inputs.remove(0);

        // Use rgb
        return new OrdinaryColor(Integer.parseInt(input), Integer.parseInt(input2), Integer.parseInt(input3));
    }
    
}
