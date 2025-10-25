package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.TransparentColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.inputparser.InputParser;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableOrdinaryColor;
import dev.rosewood.rosegarden.utils.StringPlaceholders;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.util.StringUtil;

public class UseCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (args.length < 2) {
            CommandModule.printUsage(pplayer, this);
            return;
        }

        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        InputParser inputParser = new InputParser(pplayer, Arrays.copyOfRange(args, 1, args.length));

        ParticlePair primaryParticle = pplayer.getPrimaryParticle();

        String type = args[0].toLowerCase();
        switch (type) {
            case "effect": {
                ParticleEffect effect = inputParser.next(ParticleEffect.class);
                if (effect == null) {
                    localeManager.sendMessage(pplayer, "effect-invalid", StringPlaceholders.of("effect", args[1]));
                    return;
                } else if (!permissionManager.hasEffectPermission(pplayer, effect)) {
                    localeManager.sendMessage(pplayer, "effect-no-permission", StringPlaceholders.of("effect", effect.getName()));
                    return;
                }

                primaryParticle.setEffect(effect);
                break;
            }
            case "style": {
                ParticleStyle style = inputParser.next(ParticleStyle.class);
                if (style == null) {
                    localeManager.sendMessage(pplayer, "style-invalid", StringPlaceholders.of("style", args[1]));
                    return;
                } else if (!permissionManager.hasStylePermission(pplayer, style)) {
                    localeManager.sendMessage(pplayer, "style-no-permission", StringPlaceholders.of("style", style.getName()));
                    return;
                }

                primaryParticle.setStyle(style);
                break;
            }
            case "data": {
                ParticleEffect effect = primaryParticle.getEffect();
                switch (effect.getDataType()) {
                    case COLORABLE:
                        if (effect == ParticleEffect.NOTE) {
                            NoteColor noteColorData = inputParser.next(NoteColor.class);
                            if (noteColorData == null) {
                                localeManager.sendMessage(pplayer, "data-invalid-note");
                                return;
                            }
                            primaryParticle.setNoteColor(noteColorData);
                        } else {
                            OrdinaryColor colorData = inputParser.next(OrdinaryColor.class);
                            if (colorData == null) {
                                localeManager.sendMessage(pplayer, "data-invalid-color");
                                return;
                            }
                            primaryParticle.setColor(colorData);
                        }
                        break;
                    case COLORABLE_TRANSPARENCY:
                        TransparentColor colorData = inputParser.next(TransparentColor.class);
                        if (colorData == null) {
                            localeManager.sendMessage(pplayer, "data-invalid-color");
                            return;
                        }
                        primaryParticle.setColor(colorData);
                        break;
                    case BLOCK:
                        Material blockData = inputParser.next(Material.class);
                        if (blockData == null || !blockData.isBlock()) {
                            localeManager.sendMessage(pplayer, "data-invalid-block");
                            return;
                        }
                        primaryParticle.setBlockMaterial(blockData);
                        break;
                    case ITEM:
                        Material itemData = inputParser.next(Material.class);
                        if (itemData == null || itemData.isBlock()) {
                            localeManager.sendMessage(pplayer, "data-invalid-item");
                            return;
                        }
                        primaryParticle.setItemMaterial(itemData);
                        break;
                    case COLORABLE_TRANSITION:
                        ColorTransition colorTransitionData = inputParser.next(ColorTransition.class);
                        if (colorTransitionData == null) {
                            localeManager.sendMessage(pplayer, "data-invalid-color-transition");
                            return;
                        }
                        primaryParticle.setColorTransition(colorTransitionData);
                        break;
                    case VIBRATION:
                        Vibration vibrationData = inputParser.next(Vibration.class);
                        if (vibrationData == null) {
                            localeManager.sendMessage(pplayer, "data-invalid-vibration");
                            return;
                        }
                        primaryParticle.setVibration(vibrationData);
                        break;
                }
                break;
            }
            default:
                CommandModule.printUsage(pplayer, this);
                return;
        }

        // Overwrite primary particle
        ParticleGroup activeGroup = pplayer.getActiveParticleGroup();
        activeGroup.getParticles().put(1, primaryParticle);
        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), activeGroup);

        StringPlaceholders addParticlePlaceholders = StringPlaceholders.builder("effect", primaryParticle.getEffect().getName())
                .add("style", primaryParticle.getStyle().getName())
                .add("data", primaryParticle.getDataString()).build();
        localeManager.sendMessage(pplayer, "use-particle-modified", addParticlePlaceholders);
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> matches = new ArrayList<>();
        if (args.length == 0)
            return matches;

        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        List<String> possibleValues = new ArrayList<>();
        if (args.length <= 1) {
            possibleValues.addAll(Arrays.asList("effect", "style", "data"));
        } else {
            switch (args[0].toLowerCase()) {
                case "effect":
                    if (args.length == 2)
                        possibleValues.addAll(permissionManager.getEffectNamesUserHasPermissionFor(pplayer));
                    break;
                case "style":
                    if (args.length == 2)
                        possibleValues.addAll(permissionManager.getStyleNamesUserHasPermissionFor(pplayer));
                    break;
                case "data":
                    ParticleEffect effect = pplayer.getPrimaryParticle().getEffect();
                    switch (effect.getDataType()) {
                        case COLORABLE:
                            if (effect == ParticleEffect.NOTE) { // Note data
                                if (args.length == 2) {
                                    possibleValues.add("<0-24>");
                                    possibleValues.add("rainbow");
                                    possibleValues.add("random");
                                }
                            } else { // Color data
                                if (args.length == 2) {
                                    possibleValues.add("<0-255> <0-255> <0-255>");
                                    possibleValues.addAll(ParsableOrdinaryColor.COLOR_NAME_MAP.keySet());
                                    possibleValues.add("<#hexCode>");
                                } else if (args.length <= 3 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[1].toLowerCase())) {
                                    possibleValues.add("<0-255> <0-255>");
                                } else if (args.length <= 4 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[1].toLowerCase())) {
                                    possibleValues.add("<0-255>");
                                }
                            }
                            break;
                        case COLORABLE_TRANSPARENCY:
                            if (args.length == 2) {
                                possibleValues.add("<0-255> <0-255> <0-255> [0-255]");
                                possibleValues.addAll(ParsableOrdinaryColor.COLOR_NAME_MAP.keySet());
                                possibleValues.add("<#hexCode>");
                            } else if (args.length <= 3 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[1].toLowerCase())) {
                                possibleValues.add("<0-255> <0-255> [0-255]");
                            } else if (args.length <= 4 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[1].toLowerCase())) {
                                possibleValues.add("<0-255> [0-255]");
                            } else if (args.length <= 5 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[1].toLowerCase())) {
                                possibleValues.add("[0-255]");
                            }
                            break;
                        case BLOCK:
                            possibleValues.addAll(ParticleUtils.BLOCK_MATERIALS_STRING);
                            break;
                        case ITEM:
                            possibleValues.addAll(ParticleUtils.ITEM_MATERIALS_STRING);
                            break;
                        case COLORABLE_TRANSITION:
                            String[] dataArgs = Arrays.copyOfRange(args, 1, args.length);
                            InputParser inputParser = new InputParser(pplayer, dataArgs);
                            boolean firstComplete = inputParser.next(OrdinaryColor.class) == null;
                            int argsRemaining = inputParser.numRemaining();
                            int nextStart = 1 + dataArgs.length - argsRemaining;
                            if (firstComplete) {
                                if (args.length <= 2) {
                                    possibleValues.add("<0-255> <0-255> <0-255>");
                                    possibleValues.addAll(ParsableOrdinaryColor.COLOR_NAME_MAP.keySet());
                                    possibleValues.add("<#hexCode>");
                                } else if (args.length <= 3 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[1].toLowerCase())) {
                                    possibleValues.add("<0-255> <0-255>");
                                } else if (args.length <= 4 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[1].toLowerCase())) {
                                    possibleValues.add("<0-255>");
                                }
                            } else if (inputParser.next(OrdinaryColor.class) == null) {
                                if (argsRemaining == 1) {
                                    possibleValues.add("<0-255> <0-255> <0-255>");
                                    possibleValues.addAll(ParsableOrdinaryColor.COLOR_NAME_MAP.keySet());
                                    possibleValues.add("<#hexCode>");
                                } else if (argsRemaining == 2 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[nextStart].toLowerCase())) {
                                    possibleValues.add("<0-255> <0-255>");
                                } else if (argsRemaining == 3 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[nextStart].toLowerCase())) {
                                    possibleValues.add("<0-255>");
                                }
                            }
                            break;
                        case VIBRATION:
                            possibleValues.addAll(Arrays.asList("<duration>", "20", "40", "60"));
                            break;
                    }
                    break;
            }
        }

        StringUtil.copyPartialMatches(args[args.length - 1], possibleValues, matches);
        return matches;
    }

    @Override
    public String getName() {
        return "use";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-use";
    }

    @Override
    public String getArguments() {
        return "<type> <value>";
    }

    @Override
    public boolean requiresEffectsAndStyles() {
        return true;
    }

    @Override
    public boolean canConsoleExecute() {
        return false;
    }

}
