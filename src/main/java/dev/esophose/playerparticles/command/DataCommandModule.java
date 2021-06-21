package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.util.StringUtil;

public class DataCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        if (args.length > 0) {
            ParticleEffect effect = ParticleEffect.fromName(args[0]);
            if (effect == null) {
                localeManager.sendMessage(pplayer, "effect-invalid", StringPlaceholders.single("effect", args[0]));
                return;
            }

            if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                if (effect == ParticleEffect.NOTE) {
                    localeManager.sendMessage(pplayer, "data-usage-note", StringPlaceholders.single("effect", effect.getName()));
                } else {
                    localeManager.sendMessage(pplayer, "data-usage-color", StringPlaceholders.single("effect", effect.getName()));
                }
            } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                if (effect == ParticleEffect.ITEM) {
                    localeManager.sendMessage(pplayer, "data-usage-item", StringPlaceholders.single("effect", effect.getName()));
                } else {
                    localeManager.sendMessage(pplayer, "data-usage-block", StringPlaceholders.single("effect", effect.getName()));
                }
            } else if (effect.hasProperty(ParticleProperty.COLORABLE_TRANSITION)) {
                localeManager.sendMessage(pplayer, "data-usage-color-transition", StringPlaceholders.single("effect", effect.getName()));
            } else if (effect.hasProperty(ParticleProperty.VIBRATION)) {
                localeManager.sendMessage(pplayer, "data-usage-vibration", StringPlaceholders.single("effect", effect.getName()));
            } else {
                localeManager.sendMessage(pplayer, "data-usage-none", StringPlaceholders.single("effect", effect.getName()));
            }
        } else {
            localeManager.sendMessage(pplayer, "data-no-args");
        }
    }

    @Override
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
