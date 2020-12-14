package dev.esophose.playerparticles.nms.v1_16_R3.param.vibration;

import java.util.Optional;
import net.minecraft.server.v1_16_R3.BlockPosition;
import net.minecraft.server.v1_16_R3.Entity;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.WorldServer;

public class EntityPositionSource extends PositionSource {

    private final int sourceEntityId;

    public EntityPositionSource(int entityId) {
        super("entity");

        this.sourceEntityId = entityId;
    }

    @Override
    public void write(PacketDataSerializer serializer) {
        serializer.a(this.getResourceKey());
        serializer.d(this.sourceEntityId);
    }

    @Override
    public Optional<BlockPosition> getPosition(WorldServer world) {
        Entity entity = world.getEntity(this.sourceEntityId);
        return Optional.ofNullable(entity).map(x -> new BlockPosition(x.locX(), x.locY(), x.locZ()));
    }

}
