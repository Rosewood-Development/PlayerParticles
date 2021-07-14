package dev.esophose.playerparticles.particles.data;

public class Vibration {

    private org.bukkit.Vibration vibration;
    private final int duration;

    public Vibration(org.bukkit.Vibration vibration) {
        this.vibration = vibration;
        this.duration = vibration.getArrivalTime();
    }
    public Vibration(int duration) {
        this.duration = duration;
    }

    public int getDuration() {
        return this.duration;
    }

    public org.bukkit.Vibration getVibration() {
        return this.vibration;
    }
}
