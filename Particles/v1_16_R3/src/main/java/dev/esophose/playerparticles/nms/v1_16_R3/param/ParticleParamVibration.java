package dev.esophose.playerparticles.nms.v1_16_R3.param;

import dev.esophose.playerparticles.nms.v1_16_R3.param.vibration.PositionSource;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.Particle;
import net.minecraft.server.v1_16_R3.ParticleParam;
import org.bukkit.Location;

public class ParticleParamVibration implements ParticleParam {

    private final BlockPosition origin;
    private final PositionSource destination;
    private final int ticksToArrival;

    public ParticleParamVibration(Location origin, PositionSource destination, int ticksToArrival) {
        this.origin = new BlockPosition(origin.getX(), origin.getY(), origin.getZ());
        this.destination = destination;
        this.ticksToArrival = ticksToArrival;
    }

    @Override
    public void a(PacketDataSerializer serializer) {
        serializer.a(this.origin);
        this.destination.write(serializer);
        serializer.d(this.ticksToArrival);
    }

    @Override
    public String a() {
        return null;
    }

    @Override
    public Particle<?> getParticle() {
        return null; // VIBRATION
    }

}
