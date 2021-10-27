package dev.esophose.playerparticles.styles;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.config.CommentedFileConfiguration;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.particles.PParticle;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.util.NMSUtil;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class ParticleStyleSwords extends DefaultParticleStyle implements Listener {

    private static final List<String> DEFAULT_SWORD_NAMES;

    private int multiplier;
    private List<String> swordNames;

    static {
        DEFAULT_SWORD_NAMES = new ArrayList<>();
        DEFAULT_SWORD_NAMES.addAll(Arrays.asList("WOOD_SWORD", "WOODEN_SWORD", "STONE_SWORD", "IRON_SWORD", "GOLD_SWORD", "GOLDEN_SWORD", "DIAMOND_SWORD", "NETHERITE_SWORD", "TRIDENT"));
    }

    protected ParticleStyleSwords() {
        super("swords", false, false, 0);
    }

    @Override
    public List<PParticle> getParticles(ParticlePair particle, Location location) {
        List<PParticle> particles = new ArrayList<>();

        for (int i = 0; i < this.multiplier; i++)
            particles.addAll(DefaultStyles.NORMAL.getParticles(particle, location));

        return particles;
    }

    @Override
    public void updateTimers() {

    }

    @Override
    protected List<String> getGuiIconMaterialNames() {
        return Collections.singletonList("IRON_SWORD");
    }

    @Override
    public boolean hasLongRangeVisibility() {
        return true;
    }

    @Override
    protected void setDefaultSettings(CommentedFileConfiguration config) {
        this.setIfNotExists("multiplier", 15, "The multiplier for the number of particles to spawn", "This style uses the same spawning as the 'normal' style");
        this.setIfNotExists("sword-materials", DEFAULT_SWORD_NAMES, "The materails that are considered swords", "Set to [] to allow everything to be considered a sword", "Use AIR to allow a bare hand to be considered a sword");
    }

    @Override
    protected void loadSettings(CommentedFileConfiguration config) {
        this.multiplier = config.getInt("multiplier");
        this.swordNames = config.getStringList("sword-materials");
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onEntityDamageEntity(EntityDamageByEntityEvent event) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);

        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            Player player = (Player) event.getDamager();
            LivingEntity entity = (LivingEntity) event.getEntity();
            PPlayer pplayer = PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(player.getUniqueId());
            if (pplayer == null)
                return;

            if (NMSUtil.getVersionNumber() > 8) {
                if (!this.isSword(player.getInventory().getItemInMainHand()))
                    return;
            } else {
                if (!this.isSword(player.getInventory().getItemInHand()))
                    return;
            }

            for (ParticlePair particle : pplayer.getActiveParticlesForStyle(DefaultStyles.SWORDS)) {
                Location loc = entity.getLocation().add(0, 1, 0);
                particleManager.displayParticles(pplayer, player.getWorld(), particle, DefaultStyles.SWORDS.getParticles(particle, loc), false);
            }
        }
    }

    private boolean isSword(ItemStack itemStack) {
        if (this.swordNames.isEmpty())
            return true;

        if (itemStack == null)
            return this.swordNames.contains("AIR");

        return this.swordNames.contains(itemStack.getType().name());
    }

}
