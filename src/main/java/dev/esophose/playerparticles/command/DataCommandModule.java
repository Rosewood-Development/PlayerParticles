package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
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
                localeManager.sendMessage(pplayer, "effect-invalid", StringPlaceholders.of("effect", args[0]));
                return;
            }

            switch (effect.getDataType()) {
                case COLORABLE:
                case COLORABLE_TRANSPARENCY:
                    if (effect == ParticleEffect.NOTE) {
                        localeManager.sendMessage(pplayer, "data-usage-note", StringPlaceholders.of("effect", effect.getName()));
                    } else {
                        localeManager.sendMessage(pplayer, "data-usage-color", StringPlaceholders.of("effect", effect.getName()));
                    }
                    break;
                case BLOCK:
                    localeManager.sendMessage(pplayer, "data-usage-block", StringPlaceholders.of("effect", effect.getName()));
                    break;
                case ITEM:
                    localeManager.sendMessage(pplayer, "data-usage-item", StringPlaceholders.of("effect", effect.getName()));
                    break;
                case COLORABLE_TRANSITION:
                    localeManager.sendMessage(pplayer, "data-usage-color-transition", StringPlaceholders.of("effect", effect.getName()));
                    break;
                case VIBRATION:
                    localeManager.sendMessage(pplayer, "data-usage-vibration", StringPlaceholders.of("effect", effect.getName()));
                    break;
                default:
                    localeManager.sendMessage(pplayer, "data-usage-none", StringPlaceholders.of("effect", effect.getName()));
                    break;
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
