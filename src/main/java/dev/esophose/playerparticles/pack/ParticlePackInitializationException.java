package dev.esophose.playerparticles.pack;

public class ParticlePackInitializationException extends RuntimeException {

    public ParticlePackInitializationException(String packName, Throwable cause) {
        super("An error occurred initializing a particle pack: " + packName, cause);
    }

}
