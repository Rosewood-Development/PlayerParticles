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

public class EditCommandModule implements CommandModule {

    @Override
    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        if (args.length < 3) {
            CommandModule.printUsage(pplayer, this);
            return;
        }

        InputParser inputParser = new InputParser(pplayer, args);

        Integer id = inputParser.next(Integer.class);
        if (id == null) {
            localeManager.sendMessage(pplayer, "id-invalid");
            return;
        }
        
        if (id <= 0) {
            localeManager.sendMessage(pplayer, "id-invalid");
            return;
        }
        
        if (pplayer.getActiveParticle(id) == null) {
            localeManager.sendMessage(pplayer, "id-unknown", StringPlaceholders.single("id", id));
            return;
        }
        
        String[] cmdArgs = new String[args.length - 2];
        System.arraycopy(args, 2, cmdArgs, 0, args.length - 2);
        
        switch (args[1].toLowerCase()) {
        case "effect":
            this.editEffect(pplayer, id, cmdArgs);
            break;
        case "style":
            this.editStyle(pplayer, id, cmdArgs);
            break;
        case "data":
            this.editData(pplayer, id, cmdArgs);
            break;
        default:
            localeManager.sendMessage(pplayer, "edit-invalid-property", StringPlaceholders.single("prop", args[1]));
            break;
        }
    }
    
    /**
     * Executes the effect subcommand
     * 
     * @param pplayer The PPlayer executing the command
     * @param id The target particle ID
     * @param args The rest of the args
     */
    private void editEffect(PPlayer pplayer, int id, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        InputParser inputParser = new InputParser(pplayer, args);
        ParticleEffect effect = inputParser.next(ParticleEffect.class);
        if (effect == null) {
            localeManager.sendMessage(pplayer, "effect-invalid", StringPlaceholders.single("effect", args[0]));
            return;
        } else if (!PlayerParticles.getInstance().getManager(PermissionManager.class).hasEffectPermission(pplayer, effect)) {
            localeManager.sendMessage(pplayer, "effect-no-permission", StringPlaceholders.single("effect", effect.getName()));
            return;
        }
        
        ParticleGroup group = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : group.getParticles().values()) {
            if (particle.getId() == id) {
                particle.setEffect(effect);
                break;
            }
        }
        
        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);
        localeManager.sendMessage(pplayer, "edit-success-effect", StringPlaceholders.builder("id", id).addPlaceholder("effect", effect.getName()).build());
    }
    
    /**
     * Executes the style subcommand
     * 
     * @param pplayer The PPlayer executing the command
     * @param id The target particle ID
     * @param args The rest of the args
     */
    private void editStyle(PPlayer pplayer, int id, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        InputParser inputParser = new InputParser(pplayer, args);
        ParticleStyle style = inputParser.next(ParticleStyle.class);
        if (style == null) {
            localeManager.sendMessage(pplayer, "style-invalid", StringPlaceholders.single("style", args[0]));
            return;
        } else if (!PlayerParticles.getInstance().getManager(PermissionManager.class).hasStylePermission(pplayer, style)) {
            localeManager.sendMessage(pplayer, "style-no-permission", StringPlaceholders.single("style", style.getName()));
            return;
        }
        
        ParticleGroup group = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : group.getParticles().values()) {
            if (particle.getId() == id) {
                particle.setStyle(style);
                break;
            }
        }

        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);
        localeManager.sendMessage(pplayer, "edit-success-style", StringPlaceholders.builder("id", id).addPlaceholder("style", style.getName()).build());
    }
    
    /**
     * Executes the data subcommand
     * 
     * @param pplayer The PPlayer executing the command
     * @param id The target particle ID
     * @param args The rest of the args
     */
    private void editData(PPlayer pplayer, int id, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        Material itemData = null;
        Material blockData = null;
        OrdinaryColor colorData = null;
        NoteColor noteColorData = null;
        ColorTransition colorTransitionData = null;
        Vibration vibrationData = null;

        ParticleEffect effect = pplayer.getActiveParticle(id).getEffect();

        InputParser inputParser = new InputParser(pplayer, args);
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
        
        String updatedDataString = null;
        ParticleGroup group = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : group.getParticles().values()) {
            if (particle.getId() == id) {
                if (itemData != null) particle.setItemMaterial(itemData);
                if (blockData != null) particle.setBlockMaterial(blockData);
                if (colorData != null) particle.setColor(colorData);
                if (noteColorData != null) particle.setNoteColor(noteColorData);
                if (colorTransitionData != null) particle.setColorTransition(colorTransitionData);
                if (vibrationData != null) particle.setVibration(vibrationData);
                updatedDataString = particle.getDataString();
                break;
            }
        }

        PlayerParticlesAPI.getInstance().savePlayerParticleGroup(pplayer.getPlayer(), group);
        localeManager.sendMessage(pplayer, "edit-success-data", StringPlaceholders.builder("id", id).addPlaceholder("data", updatedDataString).build());
    }

    @Override
    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        List<String> matches = new ArrayList<>();
        List<String> ids = new ArrayList<>();
        
        for (ParticlePair particles : pplayer.getActiveParticles())
            ids.add(String.valueOf(particles.getId()));
        
        if (args.length == 0) {
            matches = ids;  
        } else if (args.length == 1) {
            StringUtil.copyPartialMatches(args[0], ids, matches);
        }
        
        int id = -1;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception ignored) { }
        
        if (pplayer.getActiveParticle(id) != null) {
            if (args.length == 2) {
                List<String> possibleValues = new ArrayList<>();
                possibleValues.add("effect");
                possibleValues.add("style");
                possibleValues.add("data");
                StringUtil.copyPartialMatches(args[1], possibleValues, matches);
            } else if (args.length >= 3) {
                switch (args[1].toLowerCase()) {
                case "effect":
                    if (args.length == 3)
                        StringUtil.copyPartialMatches(args[2], permissionManager.getEffectNamesUserHasPermissionFor(pplayer), matches);
                    break;
                case "style":
                    if (args.length == 3)
                        StringUtil.copyPartialMatches(args[2], permissionManager.getStyleNamesUserHasPermissionFor(pplayer), matches);
                    break;
                case "data":
                    ParticleEffect effect = pplayer.getActiveParticle(id).getEffect();
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
                    break;
                }
            }
        }
        
        return matches;
    }

    @Override
    public String getName() {
        return "edit";
    }

    @Override
    public String getDescriptionKey() {
        return "command-description-edit";
    }

    @Override
    public String getArguments() {
        return "<ID> <effect>|<style>|<data> <args>";
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
