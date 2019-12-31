package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.ParticleManager;
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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class FixedCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        Player p = pplayer.getPlayer();

        if (!PlayerParticles.getInstance().getManager(PermissionManager.class).canUseFixedEffects(p)) {
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
        default:
            localeManager.sendMessage(pplayer, "fixed-invalid-command");
            localeManager.sendMessage(pplayer, "command-description-fixed-create");
            localeManager.sendMessage(pplayer, "command-description-fixed-edit");
            localeManager.sendMessage(pplayer, "command-description-fixed-remove");
            localeManager.sendMessage(pplayer, "command-description-fixed-list");
            localeManager.sendMessage(pplayer, "command-description-fixed-info");
            localeManager.sendMessage(pplayer, "command-description-fixed-clear");
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

        double xPos, yPos, zPos;

        if (args[0].equalsIgnoreCase("looking")) {
            Block targetBlock = p.getTargetBlock((Set<Material>) null, 8); // Need the Set<Material> cast for 1.9 support
            int maxDistanceSqrd = 6 * 6;
            if (targetBlock.getLocation().distanceSquared(p.getLocation()) > maxDistanceSqrd) {
                localeManager.sendMessage(pplayer, "fixed-create-looking-too-far");
                return;
            }

            Location blockLocation = targetBlock.getLocation().clone().add(0.5, 0.5, 0.5); // Center of block

            xPos = blockLocation.getX();
            yPos = blockLocation.getY();
            zPos = blockLocation.getZ();

            // Pad the args with the coordinates so we don't have to adjust all the indices
            String[] paddedArgs = new String[args.length + 2];
            paddedArgs[0] = String.valueOf(xPos);
            paddedArgs[1] = String.valueOf(yPos);
            paddedArgs[2] = String.valueOf(zPos);
            System.arraycopy(args, 1, paddedArgs, 3, args.length - 1);
            args = paddedArgs;
        } else {
            try {
                if (args[0].startsWith("~")) {
                    if (args[0].equals("~")) xPos = p.getLocation().getX();
                    else xPos = p.getLocation().getX() + Double.parseDouble(args[0].substring(1));
                } else {
                    xPos = Double.parseDouble(args[0]);
                }

                if (args[1].startsWith("~")) {
                    if (args[1].equals("~")) yPos = p.getLocation().getY() + 1;
                    else yPos = p.getLocation().getY() + 1 + Double.parseDouble(args[1].substring(1));
                } else {
                    yPos = Double.parseDouble(args[1]);
                }

                if (args[2].startsWith("~")) {
                    if (args[2].equals("~")) zPos = p.getLocation().getZ();
                    else zPos = p.getLocation().getZ() + Double.parseDouble(args[2].substring(1));
                } else {
                    zPos = Double.parseDouble(args[2]);
                }
            } catch (Exception e) {
                localeManager.sendMessage(pplayer, "fixed-create-invalid-coords");
                return;
            }
        }

        double distanceFromEffect = p.getLocation().distance(new Location(p.getWorld(), xPos, yPos, zPos));
        int maxCreationDistance = permissionManager.getMaxFixedEffectCreationDistance();
        if (maxCreationDistance != 0 && distanceFromEffect > maxCreationDistance) {
            localeManager.sendMessage(pplayer, "fixed-create-out-of-range", StringPlaceholders.single("range", maxCreationDistance));
            return;
        }

        ParticleEffect effect = ParticleEffect.fromName(args[3]);
        if (effect == null) {
            localeManager.sendMessage(pplayer, "fixed-create-effect-invalid", StringPlaceholders.single("effect", args[3]));
            return;
        } else if (!permissionManager.hasEffectPermission(p, effect)) {
            localeManager.sendMessage(pplayer, "fixed-create-effect-no-permission", StringPlaceholders.single("effect", effect.getName()));
            return;
        }

        ParticleStyle style = ParticleStyle.fromName(args[4]);
        if (style == null) {
            localeManager.sendMessage(pplayer, "fixed-create-style-invalid", StringPlaceholders.single("style", args[4]));
            return;
        } else if (!permissionManager.hasStylePermission(p, style)) {
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

        if (args.length > 5) {
            if (effect.hasProperty(ParticleProperty.COLORABLE)) {
                if (effect == ParticleEffect.NOTE) {
                    if (args[5].equalsIgnoreCase("rainbow")) {
                        noteColorData = new NoteColor(99);
                    } else if (args[5].equalsIgnoreCase("random")) {
                        noteColorData = new NoteColor(98);
                    } else {
                        int note;
                        try {
                            note = Integer.parseInt(args[5]);
                        } catch (Exception e) {
                            localeManager.sendMessage(pplayer, "fixed-create-data-error");
                            return;
                        }

                        if (note < 0 || note > 24) {
                            localeManager.sendMessage(pplayer, "fixed-create-data-error");
                            return;
                        }

                        noteColorData = new NoteColor(note);
                    }
                } else {
                    if (args[5].equalsIgnoreCase("rainbow")) {
                        colorData = new OrdinaryColor(999, 999, 999);
                    } else if (args[5].equalsIgnoreCase("random")) {
                        colorData = new OrdinaryColor(998, 998, 998);
                    } else {
                        int r, g, b;

                        try {
                            r = Integer.parseInt(args[5]);
                            g = Integer.parseInt(args[6]);
                            b = Integer.parseInt(args[7]);
                        } catch (Exception e) {
                            localeManager.sendMessage(pplayer, "fixed-create-data-error");
                            return;
                        }

                        if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                            localeManager.sendMessage(pplayer, "fixed-create-data-error");
                            return;
                        }

                        colorData = new OrdinaryColor(r, g, b);
                    }
                }
            } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                    Material material;
                    try {
                        material = ParticleUtils.closestMatch(args[5]);
                        if (material == null) material = Material.matchMaterial(args[5]);
                        if (material == null || !material.isBlock()) throw new Exception();
                    } catch (Exception e) {
                        localeManager.sendMessage(pplayer, "fixed-create-data-error");
                        return;
                    }

                    blockData = material;
                } else if (effect == ParticleEffect.ITEM) {
                    Material material;
                    try {
                        material = ParticleUtils.closestMatch(args[5]);
                        if (material == null) material = Material.matchMaterial(args[5]);
                        if (material == null || material.isBlock()) throw new Exception();
                    } catch (Exception e) {
                        localeManager.sendMessage(pplayer, "fixed-create-data-error");
                        return;
                    }

                    itemData = material;
                }
            }
        }

        int nextFixedEffectId = pplayer.getNextFixedEffectId();
        ParticlePair particle = new ParticlePair(pplayer.getUniqueId(), nextFixedEffectId, effect, style, itemData, blockData, colorData, noteColorData);
        FixedParticleEffect fixedEffect = new FixedParticleEffect(p.getUniqueId(), nextFixedEffectId, p.getLocation().getWorld(), xPos, yPos, zPos, particle);

        localeManager.sendMessage(pplayer, "fixed-create-success");
        PlayerParticles.getInstance().getManager(DataManager.class).saveFixedEffect(fixedEffect);
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

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception ex) {
            localeManager.sendMessage(pplayer, "fixed-edit-invalid-id");
            return;
        }

        FixedParticleEffect fixedEffect = pplayer.getFixedEffectById(id);
        if (fixedEffect == null) {
            localeManager.sendMessage(pplayer, "fixed-edit-invalid-id");
            return;
        }

        String editType = args[1].toLowerCase();
        switch (editType) {
            case "location":
                double xPos, yPos, zPos;

                if (args[2].equalsIgnoreCase("looking")) {
                    Block targetBlock = p.getTargetBlock((Set<Material>) null, 8); // Need the Set<Material> cast for 1.9 support
                    int maxDistanceSqrd = 6 * 6;
                    if (targetBlock.getLocation().distanceSquared(p.getLocation()) > maxDistanceSqrd) {
                        localeManager.sendMessage(pplayer, "fixed-edit-looking-too-far");
                        return;
                    }

                    Location blockLocation = targetBlock.getLocation().clone().add(0.5, 0.5, 0.5); // Center of block

                    xPos = blockLocation.getX();
                    yPos = blockLocation.getY();
                    zPos = blockLocation.getZ();
                } else {
                    try {
                        if (args[2].startsWith("~")) {
                            if (args[2].equals("~")) xPos = p.getLocation().getX();
                            else xPos = p.getLocation().getX() + Double.parseDouble(args[2].substring(1));
                        } else {
                            xPos = Double.parseDouble(args[2]);
                        }

                        if (args[3].startsWith("~")) {
                            if (args[3].equals("~")) yPos = p.getLocation().getY() + 1;
                            else yPos = p.getLocation().getY() + 1 + Double.parseDouble(args[3].substring(1));
                        } else {
                            yPos = Double.parseDouble(args[3]);
                        }

                        if (args[4].startsWith("~")) {
                            if (args[4].equals("~")) zPos = p.getLocation().getZ();
                            else zPos = p.getLocation().getZ() + Double.parseDouble(args[4].substring(1));
                        } else {
                            zPos = Double.parseDouble(args[4]);
                        }
                    } catch (Exception e) {
                        localeManager.sendMessage(pplayer, "fixed-edit-invalid-coords");
                        return;
                    }
                }

                double distanceFromEffect = p.getLocation().distance(new Location(p.getWorld(), xPos, yPos, zPos));
                int maxCreationDistance = permissionManager.getMaxFixedEffectCreationDistance();
                if (maxCreationDistance != 0 && distanceFromEffect > maxCreationDistance) {
                    localeManager.sendMessage(pplayer, "fixed-edit-out-of-range", StringPlaceholders.single("range", maxCreationDistance));
                    return;
                }

                fixedEffect.setCoordinates(xPos, yPos, zPos);
                break;
            case "effect": {
                ParticleEffect effect = ParticleEffect.fromName(args[2]);
                if (effect == null) {
                    localeManager.sendMessage(pplayer, "fixed-edit-effect-invalid", StringPlaceholders.single("effect", args[2]));
                    return;
                } else if (!permissionManager.hasEffectPermission(pplayer.getPlayer(), effect)) {
                    localeManager.sendMessage(pplayer, "fixed-edit-effect-no-permission", StringPlaceholders.single("effect", effect.getName()));
                    return;
                }

                fixedEffect.getParticlePair().setEffect(effect);
                break;
            }
            case "style":
                ParticleStyle style = ParticleStyle.fromName(args[2]);
                if (style == null) {
                    localeManager.sendMessage(pplayer, "fixed-edit-style-invalid", StringPlaceholders.single("style", args[2]));
                    return;
                } else if (!permissionManager.hasStylePermission(pplayer.getPlayer(), style)) {
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
                        if (args[2].equalsIgnoreCase("rainbow")) {
                            noteColorData = new NoteColor(99);
                        } else if (args[2].equalsIgnoreCase("random")) {
                            noteColorData = new NoteColor(98);
                        } else {
                            int note;
                            try {
                                note = Integer.parseInt(args[2]);
                            } catch (Exception e) {
                                localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                                return;
                            }

                            if (note < 0 || note > 24) {
                                localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                                return;
                            }

                            noteColorData = new NoteColor(note);
                        }
                    } else {
                        if (args[2].equalsIgnoreCase("rainbow")) {
                            colorData = new OrdinaryColor(999, 999, 999);
                        } else if (args[2].equalsIgnoreCase("random")) {
                            colorData = new OrdinaryColor(998, 998, 998);
                        } else {
                            int r, g, b;

                            try {
                                r = Integer.parseInt(args[2]);
                                g = Integer.parseInt(args[3]);
                                b = Integer.parseInt(args[4]);
                            } catch (Exception e) {
                                localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                                return;
                            }

                            if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                                localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                                return;
                            }

                            colorData = new OrdinaryColor(r, g, b);
                        }
                    }
                } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                    if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                        Material material;
                        try {
                            material = ParticleUtils.closestMatch(args[2]);
                            if (material == null) material = Material.matchMaterial(args[2]);
                            if (material == null || !material.isBlock()) throw new Exception();
                        } catch (Exception e) {
                            localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                            return;
                        }

                        blockData = material;
                    } else if (effect == ParticleEffect.ITEM) {
                        Material material;
                        try {
                            material = ParticleUtils.closestMatch(args[2]);
                            if (material == null) material = Material.matchMaterial(args[2]);
                            if (material == null || material.isBlock()) throw new Exception();
                        } catch (Exception e) {
                            localeManager.sendMessage(pplayer, "fixed-edit-data-error");
                            return;
                        }

                        itemData = material;
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

        PlayerParticles.getInstance().getManager(DataManager.class).updateFixedEffect(fixedEffect);
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

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
            localeManager.sendMessage(pplayer, "fixed-remove-args-invalid");
            return;
        }

        if (pplayer.getFixedEffectById(id) != null) {
            PlayerParticles.getInstance().getManager(DataManager.class).removeFixedEffect(pplayer.getUniqueId(), id);
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

        List<Integer> ids = pplayer.getFixedEffectIds();
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

        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
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
        ParticleManager particleManager = PlayerParticles.getInstance().getManager(ParticleManager.class);
        DataManager dataManager = PlayerParticles.getInstance().getManager(DataManager.class);

        if (!permissionManager.canClearFixedEffects(p)) {
            localeManager.sendMessage(pplayer, "fixed-clear-no-permission");
            return;
        }

        if (args.length < 1) {
            localeManager.sendMessage(pplayer, "fixed-clear-no-args");
            return;
        }

        int radius;
        try {
            radius = Math.abs(Integer.parseInt(args[0]));
        } catch (Exception e) {
            localeManager.sendMessage(pplayer, "fixed-clear-invalid-args");
            return;
        }

        ArrayList<FixedParticleEffect> fixedEffectsToRemove = new ArrayList<>();

        for (PPlayer ppl : particleManager.getPPlayers())
            for (FixedParticleEffect fixedEffect : ppl.getFixedParticles())
                if (fixedEffect.getLocation().getWorld() == p.getLocation().getWorld() && fixedEffect.getLocation().distance(p.getLocation()) <= radius)
                    fixedEffectsToRemove.add(fixedEffect);

        for (FixedParticleEffect fixedEffect : fixedEffectsToRemove)
            dataManager.removeFixedEffect(fixedEffect.getOwnerUniqueId(), fixedEffect.getId());

        localeManager.sendMessage(pplayer, "fixed-clear-success", StringPlaceholders.builder("amount", fixedEffectsToRemove.size()).addPlaceholder("range", radius).build());
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        Player p = pplayer.getPlayer();
        List<String> matches = new ArrayList<>();

        if (args.length <= 1) {
            List<String> possibleCmds = new ArrayList<>(Arrays.asList("create", "edit", "remove", "list", "info", "clear"));
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
                        StringUtil.copyPartialMatches(args[4], permissionManager.getEffectNamesUserHasPermissionFor(p), matches);
                    } else if (args.length == 6) {
                        StringUtil.copyPartialMatches(args[5], permissionManager.getFixableStyleNamesUserHasPermissionFor(p), matches);
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
                                    if (args.length <= 9 && !args[2].equalsIgnoreCase("rainbow") && !args[2].equalsIgnoreCase("random")) {
                                        possibleValues.add("<0-255>");
                                    }
                                    if (args.length <= 8 && !args[2].equalsIgnoreCase("rainbow") && !args[2].equalsIgnoreCase("random")) {
                                        possibleValues.add("<0-255> <0-255>");
                                    }
                                    if (args.length <= 7) {
                                        possibleValues.add("<0-255> <0-255> <0-255>");
                                        possibleValues.add("rainbow");
                                        possibleValues.add("random");
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
                            List<String> possibleValues = new ArrayList<String>();
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
                            StringUtil.copyPartialMatches(args[3], permissionManager.getEffectNamesUserHasPermissionFor(p), matches);
                        } else if (property.equals("style") && args.length == 4) {
                            StringUtil.copyPartialMatches(args[3], permissionManager.getFixableStyleNamesUserHasPermissionFor(p), matches);
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
                                        if (args.length == 6 && !args[3].equalsIgnoreCase("rainbow") && !args[3].equalsIgnoreCase("random")) {
                                            possibleValues.add("<0-255>");
                                        }
                                        if (args.length == 5 && !args[3].equalsIgnoreCase("rainbow") && !args[3].equalsIgnoreCase("random")) {
                                            possibleValues.add("<0-255> <0-255>");
                                        }
                                        if (args.length == 4) {
                                            possibleValues.add("<0-255> <0-255> <0-255>");
                                            possibleValues.add("rainbow");
                                            possibleValues.add("random");
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

    public boolean requiresEffects() {
        return true;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
