package dev.esophose.playerparticles.particles.data;

import dev.esophose.playerparticles.particles.ParticleEffect;
import java.util.Objects;
import org.bukkit.Color;

/**
 * Represents the color for effects like {@link ParticleEffect#ENTITY_EFFECT},
 * {@link ParticleEffect#AMBIENT_ENTITY_EFFECT} and {@link ParticleEffect#NOTE}
 * <p>
 * This class is part of the <b>ParticleEffect Library</b> and follows the
 * same usage conditions
 *
 * @author DarkBlade12
 * @since 1.7
 */
public final class OrdinaryColor extends ParticleColor {
    public static final OrdinaryColor RAINBOW = new OrdinaryColor(999, 999, 999);
    public static final OrdinaryColor RANDOM = new OrdinaryColor(998, 998, 998);

    private final int red;
    private final int green;
    private final int blue;

    /**
     * Construct a new ordinary color
     *
     * @param red Red value of the RGB format
     * @param green Green value of the RGB format
     * @param blue Blue value of the RGB format
     * @throws IllegalArgumentException If one of the values is lower than 0
     *             or higher than 255
     */
    public OrdinaryColor(int red, int green, int blue) throws IllegalArgumentException {
        if ((red == 999 && green == 999 && blue == 999) || (red == 998 && green == 998 && blue == 998)) { // Allow rainbow and random values
            this.red = red;
            this.green = green;
            this.blue = blue;
        } else {
            if (red < 0) {
                throw new IllegalArgumentException("The red value is lower than 0");
            }
            if (red > 255) {
                throw new IllegalArgumentException("The red value is higher than 255");
            }
            this.red = red;
            if (green < 0) {
                throw new IllegalArgumentException("The green value is lower than 0");
            }
            if (green > 255) {
                throw new IllegalArgumentException("The green value is higher than 255");
            }
            this.green = green;
            if (blue < 0) {
                throw new IllegalArgumentException("The blue value is lower than 0");
            }
            if (blue > 255) {
                throw new IllegalArgumentException("The blue value is higher than 255");
            }
            this.blue = blue;
        }
    }

    /**
     * Returns the red value of the RGB format
     *
     * @return The red value
     */
    public int getRed() {
        return this.red;
    }

    /**
     * Returns the green value of the RGB format
     *
     * @return The green value
     */
    public int getGreen() {
        return this.green;
    }

    /**
     * Returns the blue value of the RGB format
     *
     * @return The blue value
     */
    public int getBlue() {
        return this.blue;
    }

    /**
     * Returns the red value divided by 255
     *
     * @return The offsetX value
     */
    @Override
    public float getValueX() {
        if (this.equals(OrdinaryColor.RAINBOW) || this.equals(OrdinaryColor.RANDOM))
            return 0F;
        return (float) this.red / 255F;
    }

    /**
     * Returns the green value divided by 255
     *
     * @return The offsetY value
     */
    @Override
    public float getValueY() {
        if (this.equals(OrdinaryColor.RAINBOW) || this.equals(OrdinaryColor.RANDOM))
            return 0F;
        return (float) this.green / 255F;
    }

    /**
     * Returns the blue value divided by 255
     *
     * @return The offsetZ value
     */
    @Override
    public float getValueZ() {
        if (this.equals(OrdinaryColor.RAINBOW) || this.equals(OrdinaryColor.RANDOM))
            return 0F;
        return (float) this.blue / 255F;
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

    public Color toSpigot() {
        if (this == RAINBOW || this == RANDOM)
            throw new IllegalStateException("Cannot convert special value to Spigot Color");
        return Color.fromRGB(this.red, this.green, this.blue);
    }

}
