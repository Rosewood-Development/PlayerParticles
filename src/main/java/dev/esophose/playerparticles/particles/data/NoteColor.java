package dev.esophose.playerparticles.particles.data;

import dev.esophose.playerparticles.particles.ParticleEffect;
import java.util.Objects;

/**
 * Represents the color for the {@link ParticleEffect#NOTE} effect
 * <p>
 * This class is part of the <b>ParticleEffect Library</b> and follows the
 * same usage conditions
 *
 * @author DarkBlade12
 * @since 1.7
 */
public final class NoteColor extends ParticleColor {
    public static final NoteColor RAINBOW = new NoteColor(99);
    public static final NoteColor RANDOM = new NoteColor(98);

    private final int note;

    /**
     * Construct a new note color
     *
     * @param note Note id which determines color
     * @throws IllegalArgumentException If the note value is lower than 0 or
     *             higher than 24
     */
    public NoteColor(int note) throws IllegalArgumentException {
        if (note == 99 || note == 98) { // Allow rainbow and random values
            this.note = note;
        } else {
            if (note < 0) {
                throw new IllegalArgumentException("The note value is lower than 0");
            }
            if (note > 24) {
                throw new IllegalArgumentException("The note value is higher than 24");
            }
            this.note = note;
        }
    }

    /**
     * Returns the note value
     *
     * @return The note value
     */
    public int getNote() {
        return this.note;
    }

    /**
     * Returns the note value divided by 24
     *
     * @return The offsetX value
     */
    @Override
    public float getValueX() {
        return (float) this.note / 24F;
    }

    /**
     * Returns zero because the offsetY value is unused
     *
     * @return zero
     */
    @Override
    public float getValueY() {
        return 0;
    }

    /**
     * Returns zero because the offsetZ value is unused
     *
     * @return zero
     */
    @Override
    public float getValueZ() {
        return 0;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof NoteColor))
            return false;
        NoteColor otherColor = (NoteColor) other;
        return this.note == otherColor.note;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.note);
    }

}
