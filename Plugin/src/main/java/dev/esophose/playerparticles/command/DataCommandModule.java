package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.ParticleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleProperty;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.StringUtil;

public class DataCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        if (args.length > 0) {
            ParticleEffect effect = particleManager.getEffectFromName(args[0]);
            if (effect == null) {
                localeManager.sendMessage(pplayer, "effect-invalid", StringPlaceholders.single("effect", args[0]));
                return;
            }

            String effectName = particleManager.getEffectSettings(effect).getName();
            if (effect.hasProperty(ParticleProperty.REQUIRES_COLOR_DATA)) {
                if (effect == ParticleEffect.NOTE) {
                    localeManager.sendMessage(pplayer, "data-usage-note", StringPlaceholders.single("effect", effectName));
                } else {
                    localeManager.sendMessage(pplayer, "data-usage-color", StringPlaceholders.single("effect", effectName));
                }
            } else if (effect.hasProperty(ParticleProperty.REQUIRES_BLOCK_DATA)) {
                localeManager.sendMessage(pplayer, "data-usage-block", StringPlaceholders.single("effect", effectName));
            } else if (effect.hasProperty(ParticleProperty.REQUIRES_ITEM_DATA)) {
                localeManager.sendMessage(pplayer, "data-usage-item", StringPlaceholders.single("effect", effectName));
            } else {
                localeManager.sendMessage(pplayer, "data-usage-none", StringPlaceholders.single("effect", effectName));
            }
        } else {
            localeManager.sendMessage(pplayer, "data-no-args");
        }
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        List<String> matches = new ArrayList<>();
        if (args.length <= 1) {
            if (args.length == 0) {
                matches = permissionManager.getEffectNamesUserHasPermissionFor(pplayer);
            } else {
                StringUtil.copyPartialMatches(args[0], permissionManager.getEffectNamesUserHasPermissionFor(pplayer), matches);
            }
        }
        return matches;
    }

    public String getName() {
        return "data";
    }

    public String getDescriptionKey() {
        return "command-description-data";
    }

    public String getArguments() {
        return "<effect>";
    }

    public boolean requiresEffectsAndStyles() {
        return true;
    }

    public boolean canConsoleExecute() {
        return true;
    }

}
