package dev.esophose.playerparticles.nms.v1_16_R3.param.vibration;

import java.util.Optional;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.WorldServer;
import org.bukkit.Location;

public class BlockPositionSource extends PositionSource {

    private final BlockPosition blockPosition;

    public BlockPositionSource(Location location) {
        super("block");

        this.blockPosition = location != null ? new BlockPosition(location.getX(), location.getY(), location.getZ()) : null;
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.a(this.getResourceKey());
        if (this.blockPosition != null)
            serializer.a(this.blockPosition);
    }

    @Override
    public Optional<BlockPosition> getPosition(WorldServer world) {
        return Optional.ofNullable(this.blockPosition);
    }

}
