package dev.esophose.playerparticles.manager;

import dev.esophose.playerparticles.PlayerParticles;

public abstract class Manager {

    protected PlayerParticles playerParticles;

    public Manager(PlayerParticles playerParticles) {
        this.playerParticles = playerParticles;
    }

    /**
     * Reloads the Manager's settings
     */
    public abstract void reload();

    /**
     * Cleans up the Manager's resources
     */
    public abstract void disable();

}
