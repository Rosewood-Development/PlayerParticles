package dev.esophose.playerparticles.nms.v1_16_R3;

import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_16_R3.ParticleParam;

class ModifiedPacketPlayOutWorldParticles extends PacketPlayOutWorldParticles {

    private int particleId;
    private double x;
    private double y;
    private double z;
    private float offsetX;
    private float offsetY;
    private float offsetZ;
    private float extra;
    private int count;
    private boolean longDistance;
    private ParticleParam particleParam;

    public <T extends ParticleParam> ModifiedPacketPlayOutWorldParticles(int particleId, T particalParam, boolean longDistance, double x, double y, double z, float offsetX, float offsetY, float offsetZ, float extra, int count) {
        this.particleId = particleId;
        this.particleParam = particalParam;
        this.longDistance = longDistance;
        this.x = x;
        this.y = y;
        this.z = z;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.offsetZ = offsetZ;
        this.extra = extra;
        this.count = count;
    }

    @Override
    public void b(PacketDataSerializer var0) {
        var0.writeInt(this.particleId);
        var0.writeBoolean(this.longDistance);
        var0.writeDouble(this.x);
        var0.writeDouble(this.y);
        var0.writeDouble(this.z);
        var0.writeFloat(this.offsetX);
        var0.writeFloat(this.offsetY);
        var0.writeFloat(this.offsetZ);
        var0.writeFloat(this.extra);
        var0.writeInt(this.count);

        if (this.particleParam != null)
            this.particleParam.a(var0);
    }

}
