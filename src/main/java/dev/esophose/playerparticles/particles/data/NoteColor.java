package dev.esophose.playerparticles.particles.data;

import java.util.Objects;
import org.bukkit.Color;

/**
 * Represents the color for a note particle effect
 */
public final class NoteColor implements ParticleColor {
    public static final NoteColor RAINBOW = new NoteColor(99);
    public static final NoteColor RANDOM = new NoteColor(98);

    private final int note;

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
     * @return the note value
     */
    public int getNote() {
        return this.note;
    }

    @Override
    public float getValueX() {
        return (float) this.note / 24F;
    }

    @Override
    public float getValueY() {
        return 0;
    }

    @Override
    public float getValueZ() {
        return 0;
    }

    @Override
    public Color toSpigot() {
        return Color.fromRGB(Math.round(this.getValueX() * 255), 0, 0);
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
