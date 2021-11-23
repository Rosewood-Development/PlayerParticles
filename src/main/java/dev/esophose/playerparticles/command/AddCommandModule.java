package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.ParticleStyleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.ColorTransition;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
import dev.esophose.playerparticles.particles.data.Vibration;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import dev.esophose.playerparticles.util.inputparser.InputParser;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableOrdinaryColor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.util.StringUtil;

public class AddCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        if (args.length < 2) {
            CommandModule.printUsage(pplayer, this);
            return;
        }

        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        
        int maxParticlesAllowed = permissionManager.getMaxParticlesAllowed(pplayer);
        if (pplayer.getActiveParticles().size() >= maxParticlesAllowed) {
            localeManager.sendMessage(pplayer, "add-reached-max", StringPlaceholders.single("amount", maxParticlesAllowed));
            return;
        }

        InputParser inputParser = new InputParser(pplayer, args);
        
        ParticleEffect effect = inputParser.next(ParticleEffect.class);
        if (effect == null) {
            localeManager.sendMessage(pplayer, "effect-invalid", StringPlaceholders.single("effect", args[0]));
            return;
        } else if (!permissionManager.hasEffectPermission(pplayer, effect)) {
            localeManager.sendMessage(pplayer, "effect-no-permission", StringPlaceholders.single("effect", effect.getName()));
            return;
        }

        ParticleStyle style = inputParser.next(ParticleStyle.class);
        if (style == null) {
            localeManager.sendMessage(pplayer, "style-invalid", StringPlaceholders.single("style", args[1]));
            return;
        } else if (!permissionManager.hasStylePermission(pplayer, style)) {
            localeManager.sendMessage(pplayer, "style-no-permission", StringPlaceholders.single("style", style.getName()));
            return;
        }

        Material itemData = null;
        Material blockData = null;
        OrdinaryColor colorData = null;
        NoteColor noteColorData = null;
        ColorTransition colorTransitionData = null;
        Vibration vibrationData = null;

        if (args.length > 2) {
            if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                if (effect == ParticleEffect.NOTE) {
                    noteColorData = inputParser.next(NoteColor.class);
                    if (noteColorData == null) {
                        localeManager.sendMessage(pplayer, "data-invalid-note");
                        return;
                    }
                } else {
                    colorData = inputParser.next(OrdinaryColor.class);
                    if (colorData == null) {
                        localeManager.sendMessage(pplayer, "data-invalid-color");
                        return;
                    }
                }
            } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST || effect == ParticleEffect.BLOCK_MARKER) {
                    blockData = inputParser.next(Material.class);
                    if (blockData == null || !blockData.isBlock()) {
                        localeManager.sendMessage(pplayer, "data-invalid-block");
                        return;
                    }
                } else if (effect == ParticleEffect.ITEM) {
                    itemData = inputParser.next(Material.class);
                    if (itemData == null || itemData.isBlock()) {
                        localeManager.sendMessage(pplayer, "data-invalid-item");
                        return;
                    }
                }
            } else if (effect.hasProperty(ParticleProperty.COLORABLE_TRANSITION)) {
                colorTransitionData = inputParser.next(ColorTransition.class);
                if (colorTransitionData == null) {
                    localeManager.sendMessage(pplayer, "data-invalid-color-transition");
                    return;
                }
            } else if (effect.hasProperty(ParticleProperty.VIBRATION)) {
                vibrationData = inputParser.next(Vibration.class);
                if (vibrationData == null) {
                    localeManager.sendMessage(pplayer, "data-invalid-vibration");
                    return;
                }
            }
        }
        
        ParticleGroup group = pplayer.getActiveParticleGroup();
        ParticlePair newParticle = new ParticlePair(pplayer.getUniqueId(), pplayer.getNextActiveParticleId(), effect, style, itemData, blockData, colorData, noteColorData, colorTransitionData, vibrationData);
        group.getParticles().put(newParticle.getId(), newParticle);
        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);

        StringPlaceholders addParticlePlaceholders = StringPlaceholders.builder("effect", newParticle.getEffect().getName())
                .addPlaceholder("style", newParticle.getStyle().getName())
                .addPlaceholder("data", newParticle.getDataString()).build();
        localeManager.sendMessage(pplayer, "add-particle-applied", addParticlePlaceholders);
        
        if (PlayerParticles.getInstance().getManager(ParticleStyleManager.class).isEventHandled(newParticle.getStyle())) {
            localeManager.sendMessage(pplayer, "style-event-spawning-info", StringPlaceholders.single("style", newParticle.getStyle().getName()));
        }
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        List<String> matches = new ArrayList<>();
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        if (args.length <= 1) { // Effect name
            if (args.length == 0) matches = permissionManager.getEffectNamesUserHasPermissionFor(pplayer);
            else StringUtil.copyPartialMatches(args[0], permissionManager.getEffectNamesUserHasPermissionFor(pplayer), matches);
        } else if (args.length == 2) { // Style name
            StringUtil.copyPartialMatches(args[1], permissionManager.getStyleNamesUserHasPermissionFor(pplayer), matches);
        } else { // Data
            ParticleEffect effect = ParticleEffect.fromName(args[0]);
            if (effect != null) {
                if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                    List<String> possibleValues = new ArrayList<>();
                    if (effect == ParticleEffect.NOTE) { // Note data
                        if (args.length == 3) {
                            possibleValues.add("<0-24>");
                            possibleValues.add("rainbow");
                            possibleValues.add("random");
                        }
                    } else { // Color data
                        if (args.length <= 3) {
                            possibleValues.add("<0-255> <0-255> <0-255>");
                            possibleValues.addAll(ParsableOrdinaryColor.COLOR_NAME_MAP.keySet());
                            possibleValues.add("<#hexCode>");
                        } else if (args.length <= 4 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[2].toLowerCase())) {
                            possibleValues.add("<0-255> <0-255>");
                        } else if (args.length <= 5 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[2].toLowerCase())) {
                            possibleValues.add("<0-255>");
                        }
                    }
                    StringUtil.copyPartialMatches(args[args.length - 1], possibleValues, matches);
                } else if (args.length == 3 && effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                    if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST || effect == ParticleEffect.BLOCK_MARKER) { // Block material
                        StringUtil.copyPartialMatches(args[2], ParticleUtils.BLOCK_MATERIALS_STRING, matches);
                    } else if (effect == ParticleEffect.ITEM) { // Item material
                        StringUtil.copyPartialMatches(args[2], ParticleUtils.ITEM_MATERIALS_STRING, matches);
                    }
                } else if (effect.hasProperty(ParticleProperty.COLORABLE_TRANSITION)) {
                    String[] dataArgs = Arrays.copyOfRange(args, 2, args.length);
                    InputParser inputParser = new InputParser(pplayer, dataArgs);
                    List<String> possibleValues = new ArrayList<>();
                    boolean firstComplete = inputParser.next(OrdinaryColor.class) == null;
                    int argsRemaining = inputParser.numRemaining();
                    int nextStart = 2 + dataArgs.length - argsRemaining;
                    if (firstComplete) {
                        if (args.length <= 3) {
                            possibleValues.add("<0-255> <0-255> <0-255>");
                            possibleValues.addAll(ParsableOrdinaryColor.COLOR_NAME_MAP.keySet());
                            possibleValues.add("<#hexCode>");
                        } else if (args.length <= 4 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[2].toLowerCase())) {
                            possibleValues.add("<0-255> <0-255>");
                        } else if (args.length <= 5 && !ParsableOrdinaryColor.COLOR_NAME_MAP.containsKey(args[2].toLowerCase())) {
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
                    StringUtil.copyPartialMatches(args[args.length - 1], possibleValues, matches);
                } else if (args.length == 3 && effect.hasProperty(ParticleProperty.VIBRATION)) {
                    return Arrays.asList("<duration>", "20", "40", "60");
                }
            }
        }
        
        return matches;
    }

    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-add";
    }

    @Override
    public String getArguments() {
        return "<effect> <style> [data]";
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
