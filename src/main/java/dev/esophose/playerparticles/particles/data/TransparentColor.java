package dev.esophose.playerparticles.particles.data;

import java.util.Objects;
import org.bukkit.Color;

/**
 * Represents the color for a particle effect
 */
public final class TransparentColor extends OrdinaryColor {
    private final int alpha;

    public TransparentColor(OrdinaryColor ordinaryColor, int alpha) throws IllegalArgumentException {
        this(ordinaryColor.red, ordinaryColor.green, ordinaryColor.blue, alpha);
    }

    public TransparentColor(int red, int green, int blue, int alpha) throws IllegalArgumentException {
        super(red, green, blue);

        if (alpha < 0) throw new IllegalArgumentException("The alpha value is lower than 0");
        if (alpha > 255) throw new IllegalArgumentException("The alpha value is higher than 255");
        this.alpha = alpha;
    }

    /**
     * @return the alpha value
     */
    public int getAlpha() {
        return this.alpha;
    }

    @Override
    public Color toSpigot() {
        if (this == RAINBOW || this == RANDOM)
            throw new IllegalStateException("Cannot convert special value to Spigot Color");
        return Color.fromARGB(this.alpha, this.red, this.green, this.blue);
    }

    @Override
    public boolean equals(Object other) {
        if (other == OrdinaryColor.RAINBOW || other == OrdinaryColor.RANDOM)
            return other.equals(this);
        if (!(other instanceof TransparentColor))
            return false;
        TransparentColor otherColor = (TransparentColor) other;
        return this.red == otherColor.red && this.green == otherColor.green && this.blue == otherColor.blue && this.alpha == otherColor.alpha;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.red, this.green, this.blue, this.alpha);
    }

}
