package dev.esophose.playerparticles.nms.v1_16_R3;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import dev.esophose.playerparticles.nms.v1_16_R3.param.ParticleParamRedstoneTransition;
import dev.esophose.playerparticles.nms.v1_16_R3.param.ParticleParamVibration;
import dev.esophose.playerparticles.nms.v1_16_R3.param.vibration.BlockPositionSource;
import dev.esophose.playerparticles.nms.wrapper.ParticleHandler;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.version.VersionMapping;
import io.netty.buffer.Unpooled;
import java.io.IOException;
import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.server.v1_16_R3.IRegistry;
import net.minecraft.server.v1_16_R3.PacketDataSerializer;
import net.minecraft.server.v1_16_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_16_R3.Particle;
import net.minecraft.server.v1_16_R3.ParticleParam;
import net.minecraft.server.v1_16_R3.ParticleParamBlock;
import net.minecraft.server.v1_16_R3.ParticleParamItem;
import net.minecraft.server.v1_16_R3.ParticleParamRedstone;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle.DustOptions;
import org.bukkit.block.data.BlockData;
import org.bukkit.craftbukkit.v1_16_R3.block.data.CraftBlockData;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_16_R3.util.CraftMagicNumbers;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.material.MaterialData;

@SuppressWarnings("deprecation")
public class ParticleHandlerImpl extends ParticleHandler {

    public ParticleHandlerImpl(VersionMapping versionMapping) {
        super(versionMapping);
    }

    @Override
    public void spawnParticle(ParticleEffect particleEffect, Collection<Player> players, Location location, int count, float offsetX, float offsetY, float offsetZ, float extra, Object data) {
        int particleId = this.versionMapping.getParticleEffectNameMapping().get(particleEffect);
        Particle nms = IRegistry.PARTICLE_TYPE.fromId(particleId);

        ParticleParam particleParam = null;
        if (data instanceof ItemStack) {
            ItemStack itemStack = (ItemStack) data;
            particleParam = new ParticleParamItem(nms, CraftItemStack.asNMSCopy(itemStack));
        } else if (data instanceof MaterialData) {
            MaterialData materialData = (MaterialData) data;
            particleParam = new ParticleParamBlock(nms, CraftMagicNumbers.getBlock(materialData));
        } else if (data instanceof BlockData) {
            BlockData blockData = (BlockData) data;
            particleParam = new ParticleParamBlock(nms, ((CraftBlockData) blockData).getState());
        } else if (data instanceof DustOptions) {
            DustOptions dustOptions = (DustOptions) data;
            Color color = dustOptions.getColor();
            if (particleEffect == ParticleEffect.DUST_COLOR_TRANSITION) {
                particleParam = new ParticleParamRedstoneTransition((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) 1 - color.getRed() / 255.0F, (float) 1 - color.getGreen() / 255.0F, (float) 1 - color.getBlue() / 255.0F, dustOptions.getSize());
            } else {
                particleParam = new ParticleParamRedstone((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, dustOptions.getSize());
            }
        } else if (particleEffect == ParticleEffect.VIBRATION) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            particleParam = new ParticleParamVibration(location, new BlockPositionSource(location.clone().add(
                    random.nextDouble() * 20 - 10,
                    random.nextDouble() * 20 - 10,
                    random.nextDouble() * 20 - 10
            )), 60);
        }

        PacketPlayOutWorldParticles packetplayoutworldparticles = new ModifiedPacketPlayOutWorldParticles(particleId, particleParam, true, location.getX(), location.getY(), location.getZ(), offsetX, offsetY, offsetZ, extra, count);

        PacketDataSerializer dataSerializer = new PacketDataSerializer(Unpooled.buffer());
        try {
            packetplayoutworldparticles.b(dataSerializer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProtocolManager protocolManager = ProtocolLibrary.getProtocolManager();

        byte[] bytes = new byte[dataSerializer.readableBytes()];
        dataSerializer.readBytes(bytes);

        try {
            for (Player player : players) {
                protocolManager.sendWirePacket(player, 0x23, bytes);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
