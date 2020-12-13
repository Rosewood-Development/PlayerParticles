package dev.esophose.playerparticles.nms.v1_16_R3.param;

import java.util.Locale;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.MathHelper;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.Particle;
import net.minecraft.server.v1_16_R3.ParticleParam;

public class ParticleParamRedstoneTransition implements ParticleParam {

    private final float r, g, b, r2, g2, b2, size;

    public ParticleParamRedstoneTransition(float r, float g, float b, float r2, float g2, float b2, float size) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.size = MathHelper.a(size, 0.01F, 4.0F);
    }

    @Override
    public void a(PacketDataSerializer serializer) {
        serializer.writeFloat(this.r);
        serializer.writeFloat(this.g);
        serializer.writeFloat(this.b);
        serializer.writeFloat(this.r2);
        serializer.writeFloat(this.g2);
        serializer.writeFloat(this.b2);
        serializer.writeFloat(this.size);
    }

    @Override
    public String a() {
        return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %.2f %.2f %.2f %.2f", IRegistry.PARTICLE_TYPE.getKey(this.getParticle()), this.r, this.g, this.b, this.r2, this.g2, this.b2, this.size);
    }

    @Override
    public Particle<?> getParticle() {
        return null; // DUST_COLOR_TRANSITION
    }

}
