package dev.esophose.playerparticles.particles.data;

public class ColorTransition {

    private final OrdinaryColor startColor, endColor;

    public ColorTransition(OrdinaryColor startColor, OrdinaryColor endColor) {
        this.startColor = startColor;
        this.endColor = endColor;
    }

    public OrdinaryColor getStartColor() {
        return this.startColor;
    }

    public OrdinaryColor getEndColor() {
        return this.endColor;
    }

}
