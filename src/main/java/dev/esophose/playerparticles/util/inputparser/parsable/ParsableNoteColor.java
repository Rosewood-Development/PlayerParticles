package dev.esophose.playerparticles.util.inputparser.parsable;

import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.util.inputparser.Parsable;
import java.util.List;

public class ParsableNoteColor extends Parsable<NoteColor> {

    public ParsableNoteColor() {
        super(NoteColor.class);
    }

    @Override
    public NoteColor parse(PPlayer pplayer, List<String> inputs) {
        String input = inputs.remove(0);
        if (input.equalsIgnoreCase("rainbow")) {
            return NoteColor.RAINBOW;
        } else if (input.equalsIgnoreCase("random")) {
            return NoteColor.RANDOM;
        }

        int note = Integer.parseInt(input);
        if (0 > note || note > 24)
            return null;

        return new NoteColor(note);
    }

}
