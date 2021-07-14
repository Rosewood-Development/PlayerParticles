package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class ParticleStyleOutline extends DefaultParticleStyle {

    private double particleDistance;
    private int spawnDelay;

    private int step;

    protected ParticleStyleOutline() {
        super("outline", true, true, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        if (this.step != 0)
            return Collections.emptyList();

        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        boolean fixed = false;
        for (PPlayer pplayer : particleManager.getPPlayers().values()) {
            if (!pplayer.getUniqueId().equals(particle.getOwnerUniqueId()))
                continue;

            ParticlePair other = pplayer.getActiveParticle(particle.getId());
            if (other != particle) {
                fixed = true;
                break;
            }
        }

        Location corner1, corner2;
        if (fixed) {
            corner1 = new Location(location.getWorld(), location.getBlockX(), location.getBlockY(), location.getBlockZ());
            corner2 = corner1.clone().add(1, 1, 1);
        } else {
            Player player = Bukkit.getPlayer(particle.getOwnerUniqueId());
            if (player == null)
                return Collections.emptyList();

            corner1 = location.clone().subtract(0.3, 0.9, 0.3);
            corner2 = location.clone().add(0.3, 0.9, 0.3);
        }

        return this.getHollowCube(corner1, corner2, this.particleDistance).stream().map(PParticle::new).collect(Collectors.toList());
    }

    @Override
    public void updateTimers() {
        this.step = (this.step + 1) % this.spawnDelay;
    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("GLASS");
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("particle-distance", 0.15, "The distance between individual particles");
        this.setIfNotExists("spawn-delay", 3, "The number of ticks between spawns");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.particleDistance = config.getDouble("particle-distance");
        this.spawnDelay = config.getInt("spawn-delay");
    }

    private List<Location> getHollowCube(Location corner1, Location corner2, double particleDistance) {
        List<Location> result = new ArrayList<>();
        World world = corner1.getWorld();
        double minX = Math.min(corner1.getX(), corner2.getX());
        double minY = Math.min(corner1.getY(), corner2.getY());
        double minZ = Math.min(corner1.getZ(), corner2.getZ());
        double maxX = Math.max(corner1.getX(), corner2.getX());
        double maxY = Math.max(corner1.getY(), corner2.getY());
        double maxZ = Math.max(corner1.getZ(), corner2.getZ());

        for (double x = minX; x <= maxX; x += particleDistance) {
            result.add(new Location(world, x, minY, minZ));
            result.add(new Location(world, x, maxY, minZ));
            result.add(new Location(world, x, minY, maxZ));
            result.add(new Location(world, x, maxY, maxZ));
        }

        for (double y = minY; y <= maxY; y += particleDistance) {
            result.add(new Location(world, minX, y, minZ));
            result.add(new Location(world, maxX, y, minZ));
            result.add(new Location(world, minX, y, maxZ));
            result.add(new Location(world, maxX, y, maxZ));
        }

        for (double z = minZ; z <= maxZ; z += particleDistance) {
            result.add(new Location(world, minX, minY, z));
            result.add(new Location(world, maxX, minY, z));
            result.add(new Location(world, minX, maxY, z));
            result.add(new Location(world, maxX, maxY, z));
        }

        return result;
    }

}
