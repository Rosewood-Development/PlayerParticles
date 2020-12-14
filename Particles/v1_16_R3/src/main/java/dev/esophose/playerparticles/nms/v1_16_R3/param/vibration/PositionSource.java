package dev.esophose.playerparticles.nms.v1_16_R3.param.vibration;

import java.util.Optional;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.MinecraftKey;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.WorldServer;

public abstract class PositionSource {

    private final String key;

    public PositionSource(String key) {
        this.key = key;
    }

    public abstract void write(PacketDataSerializer serializer);

    public abstract Optional<BlockPosition> getPosition(WorldServer world);

    public MinecraftKey getResourceKey() {
        return MinecraftKey.a(this.key);
    }

}
