package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.particles.data.NoteColor;
import dev.esophose.playerparticles.particles.data.OrdinaryColor;
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
                    localeManager.sendMessage(pplayer, "effect-invalid", StringPlaceholders.single("effect", args[1]));
                    return;
                } else if (!permissionManager.hasEffectPermission(pplayer, effect)) {
                    localeManager.sendMessage(pplayer, "effect-no-permission", StringPlaceholders.single("effect", effect.getName()));
                    return;
                }

                primaryParticle.setEffect(effect);
                break;
            }
            case "style": {
                ParticleStyle style = inputParser.next(ParticleStyle.class);
                if (style == null) {
                    localeManager.sendMessage(pplayer, "style-invalid", StringPlaceholders.single("style", args[1]));
                    return;
                } else if (!permissionManager.hasStylePermission(pplayer, style)) {
                    localeManager.sendMessage(pplayer, "style-no-permission", StringPlaceholders.single("style", style.getName()));
                    return;
                }

                primaryParticle.setStyle(style);
                break;
            }
            case "data": {
                ParticleEffect effect = primaryParticle.getEffect();
                if (effect.hasProperty(ParticleProperty.COLORABLE)) {
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
                } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                    if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                        Material blockData = inputParser.next(Material.class);
                        if (blockData == null || !blockData.isBlock()) {
                            localeManager.sendMessage(pplayer, "data-invalid-block");
                            return;
                        }
                        primaryParticle.setBlockMaterial(blockData);
                    } else if (effect == ParticleEffect.ITEM) {
                        Material itemData = inputParser.next(Material.class);
                        if (itemData == null || itemData.isBlock()) {
                            localeManager.sendMessage(pplayer, "data-invalid-item");
                            return;
                        }
                        primaryParticle.setItemMaterial(itemData);
                    }
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
                .addPlaceholder("style", primaryParticle.getStyle().getName())
                .addPlaceholder("data", primaryParticle.getDataString()).build();
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
                    if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                        if (effect == ParticleEffect.NOTE) { // Note data
                            if (args.length == 2) {
                                possibleValues.add("<0-24>");
                                possibleValues.add("rainbow");
                                possibleValues.add("random");
                            }
                        } else { // Color data
                            if (args.length == 2) {
                                possibleValues.add("<0-255> <0-255> <0-255>");
                                possibleValues.addAll(ParsableOrdinaryColor.getColorNameMap().keySet());
                                possibleValues.add("<#hexCode>");
                            } else if (args.length <= 3 && !ParsableOrdinaryColor.getColorNameMap().containsKey(args[1].toLowerCase())) {
                                possibleValues.add("<0-255> <0-255>");
                            } else if (args.length <= 4 && !ParsableOrdinaryColor.getColorNameMap().containsKey(args[1].toLowerCase())) {
                                possibleValues.add("<0-255>");
                            }
                        }
                    } else if (args.length == 3 && effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                        if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) { // Block material
                            possibleValues.addAll(ParticleUtils.getAllBlockMaterials());
                        } else if (effect == ParticleEffect.ITEM) { // Item material
                            possibleValues.addAll(ParticleUtils.getAllItemMaterials());
                        }
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
