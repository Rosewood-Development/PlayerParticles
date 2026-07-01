package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.OtherPPlayer;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.inputparser.InputParser;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class SetCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        // Require override permission
        if (!permissionManager.canOverride(pplayer.getUnderlyingExecutor())) {
            localeManager.sendMessage(pplayer, "set-no-permission");
            return;
        }

        // /pp set <player> <effect|style> <value>
        if (args.length < 3) {
            CommandModule.printUsage(pplayer, this);
            return;
        }

        String targetName = args[0];
        String property = args[1];
        String[] valueArgs = Arrays.copyOfRange(args, 2, args.length);

        Player targetPlayer = Bukkit.getPlayer(targetName);
        if (targetPlayer == null) {
            localeManager.sendMessage(pplayer, "set-unknown-player", StringPlaceholders.of("player", targetName));
            return;
        }

        PlayerParticles.getInstance().getManager(DataManager.class).getPPlayer(targetPlayer.getUniqueId(), (targetPPlayer) -> {
            OtherPPlayer otherPPlayer = new OtherPPlayer(pplayer.getUnderlyingExecutor(), targetPPlayer);

            switch (property.toLowerCase()) {
                case "effect":
                    this.setEffect(pplayer, otherPPlayer, targetPlayer, valueArgs);
                    break;
                case "style":
                    this.setStyle(pplayer, otherPPlayer, targetPlayer, valueArgs);
                    break;
                default:
                    localeManager.sendMessage(pplayer, "set-invalid-property", StringPlaceholders.of("prop", property));
                    break;
            }
        });
    }

    /**
     * Sets the effect on all of a target player's active particles.
     * If the target has no particles, a new one is created with the effect and default style.
     *
     * @param executor The PPlayer who ran the command
     * @param target   The OtherPPlayer wrapping the target
     * @param player   The target Bukkit Player
     * @param args     Remaining args (effect name)
     */
    private void setEffect(PPlayer executor, OtherPPlayer target, Player player, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        if (args.length == 0) {
            CommandModule.printUsage(executor, this);
            return;
        }

        InputParser inputParser = new InputParser(target, args);
        ParticleEffect effect = inputParser.next(ParticleEffect.class);
        if (effect == null) {
            localeManager.sendMessage(executor, "effect-invalid", StringPlaceholders.of("effect", args[0]));
            return;
        }

        if (!permissionManager.hasEffectPermission(target, effect)) {
            localeManager.sendMessage(executor, "set-effect-no-permission",
                    StringPlaceholders.builder("player", player.getName()).add("effect", effect.getName()).build());
            return;
        }

        ParticleGroup group = target.getActiveParticleGroup();

        if (group.getParticles().isEmpty()) {
            // No particles yet — create one with defaults
            ParticlePair newParticle = ParticlePair.getNextDefault(target);
            newParticle.setEffect(effect);
            group.getParticles().put(newParticle.getId(), newParticle);
        } else {
            for (ParticlePair particle : group.getParticles().values()) {
                particle.setEffect(effect);
            }
        }

        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(player, group);
        localeManager.sendMessage(executor, "set-effect-success",
                StringPlaceholders.builder("player", player.getName()).add("effect", effect.getName()).build());
    }

    /**
     * Sets the style on all of a target player's active particles.
     * If the target has no particles, a new one is created with the style and default effect.
     *
     * @param executor The PPlayer who ran the command
     * @param target   The OtherPPlayer wrapping the target
     * @param player   The target Bukkit Player
     * @param args     Remaining args (style name)
     */
    private void setStyle(PPlayer executor, OtherPPlayer target, Player player, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        if (args.length == 0) {
            CommandModule.printUsage(executor, this);
            return;
        }

        InputParser inputParser = new InputParser(target, args);
        ParticleStyle style = inputParser.next(ParticleStyle.class);
        if (style == null) {
            localeManager.sendMessage(executor, "style-invalid", StringPlaceholders.of("style", args[0]));
            return;
        }

        if (!permissionManager.hasStylePermission(target, style)) {
            localeManager.sendMessage(executor, "set-style-no-permission",
                    StringPlaceholders.builder("player", player.getName()).add("style", style.getName()).build());
            return;
        }

        ParticleGroup group = target.getActiveParticleGroup();

        if (group.getParticles().isEmpty()) {
            // No particles yet — create one with defaults
            ParticlePair newParticle = ParticlePair.getNextDefault(target);
            newParticle.setStyle(style);
            group.getParticles().put(newParticle.getId(), newParticle);
        } else {
            for (ParticlePair particle : group.getParticles().values()) {
                particle.setStyle(style);
            }
        }

        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(player, group);
        localeManager.sendMessage(executor, "set-style-success",
                StringPlaceholders.builder("player", player.getName()).add("style", style.getName()).build());
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        // Only show completions to players with override permission
        if (!permissionManager.canOverride(pplayer.getUnderlyingExecutor())) {
            return new ArrayList<>();
        }

        List<String> matches = new ArrayList<>();

        if (args.length <= 1) {
            // arg 0: online player names
            List<String> playerNames = Bukkit.getOnlinePlayers().stream()
                    .map(Player::getName)
                    .collect(Collectors.toList());
            if (args.length == 0) {
                matches = playerNames;
            } else {
                StringUtil.copyPartialMatches(args[0], playerNames, matches);
            }
        } else if (args.length == 2) {
            // arg 1: property — effect or style
            StringUtil.copyPartialMatches(args[1], Arrays.asList("effect", "style"), matches);
        } else if (args.length == 3) {
            // arg 2: effect/style value — use target player's permissions if online
            Player targetPlayer = Bukkit.getPlayer(args[0]);
            PPlayer targetPPlayer = null;
            if (targetPlayer != null) {
                targetPPlayer = PlayerParticles.getInstance().getManager(
                        dev.esophose.playerparticles.manager.DataManager.class).getPPlayer(targetPlayer.getUniqueId());
            }

            // Fall back to the executor's available effects/styles if target not loaded yet
            PPlayer resolvedTarget = targetPPlayer != null
                    ? new OtherPPlayer(pplayer.getUnderlyingExecutor(), targetPPlayer)
                    : pplayer;

            switch (args[1].toLowerCase()) {
                case "effect":
                    StringUtil.copyPartialMatches(args[2],
                            permissionManager.getEffectNamesUserHasPermissionFor(resolvedTarget), matches);
                    break;
                case "style":
                    StringUtil.copyPartialMatches(args[2],
                            permissionManager.getStyleNamesUserHasPermissionFor(resolvedTarget), matches);
                    break;
            }
        }

        return matches;
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-set";
    }

    @Override
    public String getArguments() {
        return "<player> <effect|style> <value>";
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
