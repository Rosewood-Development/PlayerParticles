package dev.esophose.playerparticles.particles.data;

import java.util.Objects;
import org.bukkit.Color;

/**
 * Represents the color for a particle effect
 */
public class OrdinaryColor implements ParticleColor {
    public static final OrdinaryColor RAINBOW = new OrdinaryColor(999, 999, 999);
    public static final OrdinaryColor RANDOM = new OrdinaryColor(998, 998, 998);

    protected final int red;
    protected final int green;
    protected final int blue;

    public OrdinaryColor(int red, int green, int blue) throws IllegalArgumentException {
        if ((red == 999 && green == 999 && blue == 999) || (red == 998 && green == 998 && blue == 998)) { // Allow rainbow and random values
            this.red = red;
            this.green = green;
            this.blue = blue;
        } else {
            if (red < 0) throw new IllegalArgumentException("The red value is lower than 0");
            if (red > 255) throw new IllegalArgumentException("The red value is higher than 255");
            this.red = red;

            if (green < 0) throw new IllegalArgumentException("The green value is lower than 0");
            if (green > 255) throw new IllegalArgumentException("The green value is higher than 255");
            this.green = green;

            if (blue < 0) throw new IllegalArgumentException("The blue value is lower than 0");
            if (blue > 255) throw new IllegalArgumentException("The blue value is higher than 255");
            this.blue = blue;
        }
    }

    /**
     * @return the red value
     */
    public int getRed() {
        return this.red;
    }

    /**
     * @return the green value
     */
    public int getGreen() {
        return this.green;
    }

    /**
     * @return the blue value
     */
    public int getBlue() {
        return this.blue;
    }

    @Override
    public float getValueX() {
        if (this.equals(OrdinaryColor.RAINBOW) || this.equals(OrdinaryColor.RANDOM))
            return 0F;
        return (float) this.red / 255F;
    }

    @Override
    public float getValueY() {
        if (this.equals(OrdinaryColor.RAINBOW) || this.equals(OrdinaryColor.RANDOM))
            return 0F;
        return (float) this.green / 255F;
    }

    @Override
    public float getValueZ() {
        if (this.equals(OrdinaryColor.RAINBOW) || this.equals(OrdinaryColor.RANDOM))
            return 0F;
        return (float) this.blue / 255F;
    }

    @Override
    public Color toSpigot() {
        if (this == RAINBOW || this == RANDOM)
            throw new IllegalStateException("Cannot convert special value to Spigot Color");
        return Color.fromRGB(this.red, this.green, this.blue);
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof OrdinaryColor))
            return false;
        OrdinaryColor otherColor = (OrdinaryColor) other;
        return this.red == otherColor.red && this.green == otherColor.green && this.blue == otherColor.blue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.red, this.green, this.blue);
    }

}
