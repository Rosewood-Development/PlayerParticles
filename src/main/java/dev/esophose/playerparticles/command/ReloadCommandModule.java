package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.styles.DefaultStyles;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Bukkit;

public class ReloadCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        PlayerParticles playerParticles = PlayerParticles.getInstance();
        LocaleManager localeManager = playerParticles.getManager(LocaleManager.class);
        if (playerParticles.getManager(PermissionManager.class).canReloadPlugin(pplayer.getUnderlyingExecutor())) {
            playerParticles.reload();
            Bukkit.getScheduler().runTaskLaterAsynchronously(playerParticles, () -> {
                ParticleEffect.reloadSettings();
                DefaultStyles.reloadSettings(playerParticles.getManager(ParticleStyleManager.class));
                localeManager.sendMessage(pplayer, "reload-success");
                playerParticles.getLogger().info("Reloaded configuration.");
            }, 10L);
        } else {
            localeManager.sendMessage(pplayer, "reload-no-permission");
        }
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-reload";
    }

    @Override
    public String getArguments() {
        return "";
    }

    @Override
    public boolean requiresEffectsAndStyles() {
        return false;
    }

    @Override
    public boolean canConsoleExecute() {
        return true;
    }

}
