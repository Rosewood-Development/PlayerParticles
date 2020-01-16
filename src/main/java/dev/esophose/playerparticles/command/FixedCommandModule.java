package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.api.PlayerParticlesAPI;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.FixedParticleEffect;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import dev.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import dev.esophose.playerparticles.util.inputparser.InputParser;
import dev.esophose.playerparticles.util.inputparser.parsable.ParsableOrdinaryColor;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class FixedCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        Player p = pplayer.getPlayer();

        if (!PlayerParticles.getInstance().getManager(PermissionManager.class).canUseFixedEffects(pplayer)) {
            localeManager.sendMessage(pplayer, "fixed-no-permission");
            return;
        }

        if (args.length == 0) { // General information on command
            localeManager.sendMessage(pplayer, "command-description-fixed-create");
            localeManager.sendMessage(pplayer, "command-description-fixed-edit");
            localeManager.sendMessage(pplayer, "command-description-fixed-remove");
            localeManager.sendMessage(pplayer, "command-description-fixed-list");
            localeManager.sendMessage(pplayer, "command-description-fixed-info");
            localeManager.sendMessage(pplayer, "command-description-fixed-clear");
            localeManager.sendMessage(pplayer, "command-description-fixed-teleport");
            return;
        }

        String cmd = args[0];

        String[] cmdArgs = new String[args.length - 1];
        System.arraycopy(args, 1, cmdArgs, 0, args.length - 1);

        switch (cmd.toLowerCase()) {
        case "create":
            this.handleCreate(pplayer, p, cmdArgs);
            return;
        case "edit":
            this.handleEdit(pplayer, p, cmdArgs);
            return;
        case "remove":
            this.handleRemove(pplayer, p, cmdArgs);
            return;
        case "list":
            this.handleList(pplayer, p, cmdArgs);
            return;
        case "info":
            this.handleInfo(pplayer, p, cmdArgs);
            return;
        case "clear":
            this.handleClear(pplayer, p, cmdArgs);
            return;
        case "teleport":
            this.handleTeleport(pplayer, p, cmdArgs);
            return;
        default:
            localeManager.sendMessage(pplayer, "fixed-invalid-command");
            localeManager.sendMessage(pplayer, "command-description-fixed-create");
            localeManager.sendMessage(pplayer, "command-description-fixed-edit");
            localeManager.sendMessage(pplayer, "command-description-fixed-remove");
            localeManager.sendMessage(pplayer, "command-description-fixed-list");
            localeManager.sendMessage(pplayer, "command-description-fixed-info");
            localeManager.sendMessage(pplayer, "command-description-fixed-clear");
            localeManager.sendMessage(pplayer, "command-description-fixed-teleport");
        }
    }

    /**
     * Handles the command /pp fixed create
     *
     * @param pplayer The PPlayer
     * @param p The Player
     * @param args The command arguments
     */
    private void handleCreate(PPlayer pplayer, Player p, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        boolean reachedMax = permissionManager.hasPlayerReachedMaxFixedEffects(pplayer);
        if (reachedMax) {
            localeManager.sendMessage(pplayer, "fixed-max-reached");
            return;
        }

        if (args.length < 5 && !(args.length > 0 && args[0].equalsIgnoreCase("looking") && args.length >= 3)) {
            localeManager.sendMessage(pplayer, "fixed-create-missing-args", StringPlaceholders.single("amount", 5 - args.length));
            return;
        }

        InputParser inputParser = new InputParser(pplayer, args);
        Location location = inputParser.next(Location.class);
        if (location == null) {
            if (args[0].equalsIgnoreCase("looking")) {
                localeManager.sendMessage(pplayer, "fixed-create-looking-too-far");
            } else {
                localeManager.sendMessage(pplayer, "fixed-create-invalid-coords");
            }
            return;
        }

        double distanceFromEffect = p.getLocation().distance(location);
        int maxCreationDistance = permissionManager.getMaxFixedEffectCreationDistance();
        if (maxCreationDistance != 0 && distanceFromEffect > maxCreationDistance) {
            localeManager.sendMessage(pplayer, "fixed-create-out-of-range", StringPlaceholders.single("range", maxCreationDistance));
            return;
        }

        ParticleEffect effect = inputParser.next(ParticleEffect.class);
        if (effect == null) {
            localeManager.sendMessage(pplayer, "fixed-create-effect-invalid", StringPlaceholders.single("effect", args[3]));
            return;
        } else if (!permissionManager.hasEffectPermission(pplayer, effect)) {
            localeManager.sendMessage(pplayer, "fixed-create-effect-no-permission", StringPlaceholders.single("effect", effect.getName()));
            return;
        }

        ParticleStyle style = inputParser.next(ParticleStyle.class);
        if (style == null) {
            localeManager.sendMessage(pplayer, "fixed-create-style-invalid", StringPlaceholders.single("style", args[4]));
            return;
        } else if (!permissionManager.hasStylePermission(pplayer, style)) {
            localeManager.sendMessage(pplayer, "fixed-create-style-no-permission", StringPlaceholders.single("style", args[4]));
            return;
        }

        if (!style.canBeFixed()) {
            localeManager.sendMessage(pplayer, "fixed-create-style-non-fixable", StringPlaceholders.single("style", style.getName()));
            return;
        }

        Material itemData = null;
        Material blockData = null;
        OrdinaryColor colorData = null;
        NoteColor noteColorData = null;

        if (inputParser.hasNext() && (effect.hasProperty(ParticleProperty.COLORABLE) || effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA))) {
            if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                if (effect == ParticleEffect.NOTE) {
                    noteColorData = inputParser.next(NoteColor.class);
                    if (noteColorData == null) {
                        localeManager.sendMessage(pplayer, "fixed-create-data-error");
                        return;
                    }
                } else {
                    colorData = inputParser.next(OrdinaryColor.class);
                    if (colorData == null) {
                        localeManager.sendMessage(pplayer, "fixed-create-data-error");
                        return;
                    }
                }
            } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                    blockData = inputParser.next(Material.class);
                    if (blockData == null || !blockData.isBlock()) {
                        localeManager.sendMessage(pplayer, "fixed-create-data-error");
                        return;
                    }
                } else if (effect == ParticleEffect.ITEM) {
                    itemData = inputParser.next(Material.class);
                    if (itemData == null || itemData.isBlock()) {
                        localeManager.sendMessage(pplayer, "fixed-create-data-error");
                        return;
                    }
                }
            }
        }

        ParticlePair particle = new ParticlePair(pplayer.getUniqueId(), pplayer.getNextFixedEffectId(), effect, style, itemData, blockData, colorData, noteColorData);
        PlayerParticlesAPI.getInstance().createFixedParticleEffect(pplayer.getPlayer(), location, particle);
        localeManager.sendMessage(pplayer, "fixed-create-success");
    }

    /**
     * Handles the command /pp fixed edit
     *
     * @param pplayer The PPlayer
     * @param p The Player
     * @param args The command arguments
     */
    private void handleEdit(PPlayer pplayer, Player p, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        if (args.length < 3) {
            localeManager.sendMessage(pplayer, "fixed-edit-missing-args");
            return;
        }

        InputParser inputParser = new InputParser(pplayer, args);

        Integer id = inputParser.next(Integer.class);
        if (id == null) {
            localeManager.sendMessage(pplayer, "fixed-edit-invalid-id");
            return;
        }

        FixedParticleEffect fixedEffect = pplayer.getFixedEffectById(id);
        if (fixedEffect == null) {
            localeManager.sendMessage(pplayer, "fixed-edit-invalid-id");
            return;
        }

        String editType = inputParser.next(String.class);
        switch (editType) {
            case "location":
                Location location = inputParser.next(Location.class);
                if (location == null) {
                    if (args[2].equalsIgnoreCase("looking")) {
                        localeManager.sendMessage(pplayer, "fixed-edit-looking-too-far");
                    } else {
                        localeManager.sendMessage(pplayer, "fixed-edit-invalid-coords");
                    }
                    return;
                }

                double distanceFromEffect = p.getLocation().distance(location);
                int maxCreationDistance = permissionManager.getMaxFixedEffectCreationDistance();
                if (maxCreationDistance != 0 && distanceFromEffect > maxCreationDistance) {
                    localeManager.sendMessage(pplayer, "fixed-edit-out-of-range", StringPlaceholders.single("range", maxCreationDistance));
                    return;
                }

                fixedEffect.setCoordinates(location.getX(), location.getY(), location.getZ());
                break;
            case "effect": {
                ParticleEffect effect = inputParser.next(ParticleEffect.class);
                if (effect == null) {
                    localeManager.sendMessage(pplayer, "fixed-edit-effect-invalid", StringPlaceholders.single("effect", args[2]));
                    return;
                } else if (!permissionManager.hasEffectPermission(pplayer, effect)) {
                    localeManager.sendMessage(pplayer, "fixed-edit-effect-no-permission", StringPlaceholders.single("effect", effect.getName()));
                    return;
                }

                fixedEffect.getParticlePair().setEffect(effect);
                break;
            }
            case "style":
                ParticleStyle style = inputParser.next(ParticleStyle.class);
                if (style == null) {
                    localeManager.sendMessage(pplayer, "fixed-edit-style-invalid", StringPlaceholders.single("style", args[2]));
                    return;
                } else if (!permissionManager.hasStylePermission(pplayer, style)) {
                    localeManager.sendMessage(pplayer, "fixed-edit-style-no-permission", StringPlaceholders.single("style", style.getName()));
                    return;
                } else if (!style.canBeFixed()) {
                    localeManager.sendMessage(pplayer, "fixed-edit-style-non-fixable", StringPlaceholders.single("style", style.getName()));
                    return;
                }

                fixedEffect.getParticlePair().setStyle(style);
                break;
            case "data": {
                Material itemData = null;
                Material blockData = null;
                OrdinaryColor colorData = null;
                NoteColor noteColorData = null;

                ParticleEffect effect = fixedEffect.getParticlePair().getEffect();
                if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                    if (effect == ParticleEffect.NOTE) {
                        noteColorData = inputParser.next(NoteColor.class);
                        if (noteColorData == null) {
                            localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                            return;
                        }
                    } else {
                        colorData = inputParser.next(OrdinaryColor.class);
                        if (colorData == null) {
                            localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                            return;
                        }
                    }
                } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                    if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                        blockData = inputParser.next(Material.class);
                        if (blockData == null || !blockData.isBlock()) {
                            localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                            return;
                        }
                    } else if (effect == ParticleEffect.ITEM) {
                        itemData = inputParser.next(Material.class);
                        if (itemData == null || itemData.isBlock()) {
                            localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                            return;
                        }
                    }
                } else {
                    localeManager.sendMessage(pplayer, "fixed-edit-data-none");
                    return;
                }

                fixedEffect.getParticlePair().setColor(colorData);
                fixedEffect.getParticlePair().setNoteColor(noteColorData);
                fixedEffect.getParticlePair().setItemMaterial(itemData);
                fixedEffect.getParticlePair().setBlockMaterial(blockData);
                break;
            }
            default:
                localeManager.sendMessage(pplayer, "fixed-edit-invalid-property");
                return;
        }

        PlayerParticlesAPI.getInstance().editFixedParticleEffect(pplayer.getPlayer(), fixedEffect);
        localeManager.sendMessage(pplayer, "fixed-edit-success", StringPlaceholders.builder("prop", editType).addPlaceholder("id", id).build());
    }

    /**
     * Handles the command /pp fixed remove
     *
     * @param pplayer The PPlayer
     * @param p The Player
     * @param args The command arguments
     */
    private void handleRemove(PPlayer pplayer, Player p, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        if (args.length < 1) {
            localeManager.sendMessage(pplayer, "fixed-remove-no-args");
            return;
        }

        InputParser inputParser = new InputParser(pplayer, args);

        Integer id = inputParser.next(Integer.class);
        if (id == null) {
            localeManager.sendMessage(pplayer, "fixed-remove-args-invalid");
            return;
        }

        if (pplayer.getFixedEffectById(id) != null) {
            PlayerParticlesAPI.getInstance().removeFixedEffect(pplayer.getPlayer(), id);
            localeManager.sendMessage(pplayer, "fixed-remove-success", StringPlaceholders.single("id", id));
        } else {
            localeManager.sendMessage(pplayer, "fixed-remove-invalid", StringPlaceholders.single("id", id));
        }
    }

    /**
     * Handles the command /pp fixed list
     *
     * @param pplayer The PPlayer
     * @param p The Player
     * @param args The command arguments
     */
    private void handleList(PPlayer pplayer, Player p, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        List<Integer> ids = new ArrayList<>(pplayer.getFixedEffectIds());
        Collections.sort(ids);

        if (ids.isEmpty()) {
            localeManager.sendMessage(pplayer, "fixed-list-none");
            return;
        }

        StringBuilder msg = new StringBuilder();
        boolean first = true;
        for (int id : ids) {
            if (!first) msg.append(", ");
            else first = false;
            msg.append(id);
        }

        localeManager.sendMessage(pplayer, "fixed-list-success", StringPlaceholders.single("ids", msg.toString()));
    }

    /**
     * Handles the command /pp fixed info
     *
     * @param pplayer The PPlayer
     * @param p The Player
     * @param args The command arguments
     */
    private void handleInfo(PPlayer pplayer, Player p, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        if (args.length < 1) {
            localeManager.sendMessage(pplayer, "fixed-info-no-args");
            return;
        }

        InputParser inputParser = new InputParser(pplayer, args);

        Integer id = inputParser.next(Integer.class);
        if (id == null) {
            localeManager.sendMessage(pplayer, "fixed-info-invalid-args");
            return;
        }

        FixedParticleEffect fixedEffect = pplayer.getFixedEffectById(id);
        if (fixedEffect == null) {
            localeManager.sendMessage(pplayer, "fixed-info-invalid", StringPlaceholders.single("id", id));
            return;
        }

        ParticlePair particle = fixedEffect.getParticlePair();

        DecimalFormat df = new DecimalFormat("0.##"); // Decimal formatter so the coords aren't super long
        StringPlaceholders stringPlaceholders = StringPlaceholders.builder("id", fixedEffect.getId())
                .addPlaceholder("world", fixedEffect.getLocation().getWorld().getName())
                .addPlaceholder("x", df.format(fixedEffect.getLocation().getX()))
                .addPlaceholder("y", df.format(fixedEffect.getLocation().getY()))
                .addPlaceholder("z", df.format(fixedEffect.getLocation().getZ()))
                .addPlaceholder("effect", particle.getEffect().getName())
                .addPlaceholder("style", particle.getStyle().getName())
                .addPlaceholder("data", particle.getDataString())
                .build();
        localeManager.sendMessage(pplayer, "fixed-info-success", stringPlaceholders);
    }

    /**
     * Handles the command /pp fixed clear
     *
     * @param pplayer The PPlayer
     * @param p The Player
     * @param args The command arguments
     */
    private void handleClear(PPlayer pplayer, Player p, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        if (!permissionManager.canClearFixedEffects(pplayer)) {
            localeManager.sendMessage(pplayer, "fixed-clear-no-permission");
            return;
        }

        if (args.length < 1) {
            localeManager.sendMessage(pplayer, "fixed-clear-no-args");
            return;
        }

        InputParser inputParser = new InputParser(pplayer, args);

        Integer radius = inputParser.next(Integer.class);
        if (radius == null) {
            localeManager.sendMessage(pplayer, "fixed-clear-invalid-args");
            return;
        }
        radius = Math.abs(radius);

        int amountRemoved = PlayerParticlesAPI.getInstance().removeFixedEffectsInRange(p.getLocation(), radius);
        localeManager.sendMessage(pplayer, "fixed-clear-success", StringPlaceholders.builder("amount", amountRemoved).addPlaceholder("range", radius).build());
    }

    /**
     * Handles the command /pp fixed teleport
     *
     * @param pplayer The PPlayer
     * @param p The Player
     * @param args The command arguments
     */
    private void handleTeleport(PPlayer pplayer, Player p, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);

        if (!permissionManager.canTeleportToFixedEffects(pplayer)) {
            localeManager.sendMessage(pplayer, "fixed-teleport-no-permission");
            return;
        }

        if (args.length < 1) {
            localeManager.sendMessage(pplayer, "fixed-teleport-no-args");
            return;
        }

        InputParser inputParser = new InputParser(pplayer, args);

        Integer id = inputParser.next(Integer.class);
        if (id == null) {
            localeManager.sendMessage(pplayer, "fixed-teleport-invalid-args");
            return;
        }

        FixedParticleEffect fixedEffect = pplayer.getFixedEffectById(id);
        if (fixedEffect == null) {
            localeManager.sendMessage(pplayer, "fixed-teleport-invalid-args");
            return;
        }

        p.teleport(fixedEffect.getLocation());
        localeManager.sendMessage(pplayer, "fixed-teleport-success", StringPlaceholders.single("id", id));
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        Player p = pplayer.getPlayer();
        List<String> matches = new ArrayList<>();

        if (args.length <= 1) {
            List<String> possibleCmds = new ArrayList<>(Arrays.asList("create", "edit", "remove", "list", "info", "clear", "teleport"));
            if (args.length == 0) matches = possibleCmds;
            else StringUtil.copyPartialMatches(args[0], possibleCmds, matches);
        } else {
            switch (args[0].toLowerCase()) {
                case "create":
                    if (args.length <= 4) {
                        List<String> possibleValues = new ArrayList<>();
                        if (args.length == 4) {
                            possibleValues.add("~");
                        }
                        if (args.length == 3) {
                            possibleValues.add("~ ~");
                        }
                        if (args.length == 2) {
                            possibleValues.add("~ ~ ~");
                            possibleValues.add("looking");
                        }
                        StringUtil.copyPartialMatches(args[args.length - 1], possibleValues, matches);
                    }

                    // Pad arguments if the first coordinate is "looking"
                    if (args[1].equalsIgnoreCase("looking")) {
                        String[] paddedArgs = new String[args.length + 2];
                        paddedArgs[0] = paddedArgs[1] = paddedArgs[2] = paddedArgs[3] = "";
                        System.arraycopy(args, 2, paddedArgs, 4, args.length - 2);
                        args = paddedArgs;
                    }

                    if (args.length == 5) {
                        StringUtil.copyPartialMatches(args[4], permissionManager.getEffectNamesUserHasPermissionFor(pplayer), matches);
                    } else if (args.length == 6) {
                        StringUtil.copyPartialMatches(args[5], permissionManager.getFixableStyleNamesUserHasPermissionFor(pplayer), matches);
                    } else if (args.length >= 7) {
                        ParticleEffect effect = ParticleEffect.fromName(args[4]);
                        if (effect != null) {
                            if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                                List<String> possibleValues = new ArrayList<>();
                                if (effect == ParticleEffect.NOTE) { // Note data
                                    if (args.length == 7) {
                                        possibleValues.add("<0-24>");
                                        possibleValues.add("rainbow");
                                        possibleValues.add("random");
                                    }
                                } else { // Color data
                                    if (args.length <= 7) {
                                        possibleValues.add("<0-255> <0-255> <0-255>");
                                        possibleValues.addAll(ParsableOrdinaryColor.getColorNameMap().keySet());
                                        possibleValues.add("<#hexCode>");
                                    } else if (args.length <= 8 && !ParsableOrdinaryColor.getColorNameMap().containsKey(args[2].toLowerCase())) {
                                        possibleValues.add("<0-255> <0-255>");
                                    } else if (args.length <= 9 && !ParsableOrdinaryColor.getColorNameMap().containsKey(args[2].toLowerCase())) {
                                        possibleValues.add("<0-255>");
                                    }
                                }
                                StringUtil.copyPartialMatches(args[args.length - 1], possibleValues, matches);
                            } else if (args.length == 7 && effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                                if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) { // Block material
                                    StringUtil.copyPartialMatches(args[6], ParticleUtils.getAllBlockMaterials(), matches);
                                } else if (effect == ParticleEffect.ITEM) { // Item material
                                    StringUtil.copyPartialMatches(args[6], ParticleUtils.getAllItemMaterials(), matches);
                                }
                            }
                        }
                    }
                    break;
                case "edit":
                    if (args.length == 2) {
                        StringUtil.copyPartialMatches(args[1], pplayer.getFixedEffectIds().stream().map(String::valueOf).collect(Collectors.toList()), matches);
                    } else if (args.length == 3) {
                        String[] validProperties = new String[] { "location", "effect", "style", "data" };
                        StringUtil.copyPartialMatches(args[2], Arrays.asList(validProperties), matches);
                    } else {
                        String property = args[2].toLowerCase();
                        if (property.equals("location")) {
                            List<String> possibleValues = new ArrayList<>();
                            if (args.length == 6 && !args[3].equalsIgnoreCase("looking")) {
                                possibleValues.add("~");
                            }
                            if (args.length == 5 && !args[3].equalsIgnoreCase("looking")) {
                                possibleValues.add("~ ~");
                            }
                            if (args.length == 4) {
                                possibleValues.add("~ ~ ~");
                                possibleValues.add("looking");
                            }
                            StringUtil.copyPartialMatches(args[args.length - 1], possibleValues, matches);
                        } else if (property.equals("effect") && args.length == 4) {
                            StringUtil.copyPartialMatches(args[3], permissionManager.getEffectNamesUserHasPermissionFor(pplayer), matches);
                        } else if (property.equals("style") && args.length == 4) {
                            StringUtil.copyPartialMatches(args[3], permissionManager.getFixableStyleNamesUserHasPermissionFor(pplayer), matches);
                        } else if (property.equals("data")) {
                            int id = -1;
                            try {
                                id = Integer.parseInt(args[1]);
                            } catch (Exception e) { }

                            FixedParticleEffect fixedEffect = pplayer.getFixedEffectById(id);
                            if (fixedEffect != null) {
                                ParticleEffect effect = fixedEffect.getParticlePair().getEffect();
                                if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                                    List<String> possibleValues = new ArrayList<>();
                                    if (effect == ParticleEffect.NOTE) { // Note data
                                        if (args.length == 4) {
                                            possibleValues.add("<0-24>");
                                            possibleValues.add("rainbow");
                                            possibleValues.add("random");
                                        }
                                    } else { // Color data
                                        if (args.length <= 4) {
                                            possibleValues.add("<0-255> <0-255> <0-255>");
                                            possibleValues.addAll(ParsableOrdinaryColor.getColorNameMap().keySet());
                                            possibleValues.add("<#hexCode>");
                                        } else if (args.length <= 5 && !ParsableOrdinaryColor.getColorNameMap().containsKey(args[3].toLowerCase())) {
                                            possibleValues.add("<0-255> <0-255>");
                                        } else if (args.length <= 6 && !ParsableOrdinaryColor.getColorNameMap().containsKey(args[3].toLowerCase())) {
                                            possibleValues.add("<0-255>");
                                        }
                                    }
                                    StringUtil.copyPartialMatches(args[args.length - 1], possibleValues, matches);
                                } else if (args.length == 4 && effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                                    if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) { // Block material
                                        StringUtil.copyPartialMatches(args[3], ParticleUtils.getAllBlockMaterials(), matches);
                                    } else if (effect == ParticleEffect.ITEM) { // Item material
                                        StringUtil.copyPartialMatches(args[3], ParticleUtils.getAllItemMaterials(), matches);
                                    }
                                }
                            }
                        }
                    }
                    break;
                case "remove":
                case "info":
                case "teleport":
                    StringUtil.copyPartialMatches(args[1], pplayer.getFixedEffectIds().stream().map(String::valueOf).collect(Collectors.toList()), matches);
                    break;
                case "clear":
                    matches.add("<radius>");
                    break;
                case "list":
                    break;
            }
        }

        return matches;
    }

    public String getName() {
        return "fixed";
    }

    public String getDescriptionKey() {
        return "command-description-fixed";
    }

    public String getArguments() {
        return "<sub-command>";
    }

    public boolean requiresEffectsAndStyles() {
        return true;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
