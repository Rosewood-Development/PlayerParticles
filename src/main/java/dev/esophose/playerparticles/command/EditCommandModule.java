package dev.esophose.playerparticles.command;

import dev.esophose.playerparticles.PlayerParticles;
import dev.esophose.playerparticles.manager.DataManager;
import dev.esophose.playerparticles.manager.LocaleManager;
import dev.esophose.playerparticles.manager.PermissionManager;
import dev.esophose.playerparticles.particles.PPlayer;
import dev.esophose.playerparticles.particles.ParticleEffect;
import dev.esophose.playerparticles.particles.ParticleEffect.NoteColor;
import dev.esophose.playerparticles.particles.ParticleEffect.OrdinaryColor;
import dev.esophose.playerparticles.particles.ParticleEffect.ParticleProperty;
import dev.esophose.playerparticles.particles.ParticleGroup;
import dev.esophose.playerparticles.particles.ParticlePair;
import dev.esophose.playerparticles.styles.ParticleStyle;
import dev.esophose.playerparticles.util.ParticleUtils;
import dev.esophose.playerparticles.util.StringPlaceholders;
import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

public class EditCommandModule implements CommandModule {

    public void onCommandExecute(PPlayer pplayer, String[] args) {
        LocaleManager localeManager = PlayerParticles.getInstance().getManager(LocaleManager.class);

        if (args.length < 3) {
            CommandModule.printUsage(pplayer, this);
            return;
        }
        
        int id;
        try {
            id = Integer.parseInt(args[0]);
        } catch (Exception e) {
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

        ParticleEffect effect = ParticleEffect.fromName(args[0]);
        if (effect == null) {
            localeManager.sendMessage(pplayer, "effect-invalid", StringPlaceholders.single("effect", args[0]));
            return;
        } else if (!PlayerParticles.getInstance().getManager(PermissionManager.class).hasEffectPermission(pplayer.getPlayer(), effect)) {
            localeManager.sendMessage(pplayer, "effect-no-permission", StringPlaceholders.single("effect", effect.getName()));
            return;
        }
        
        ParticleGroup group = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : group.getParticles()) {
            if (particle.getId() == id) {
                particle.setEffect(effect);
                break;
            }
        }
        
        PlayerParticles.getInstance().getManager(DataManager.class).saveParticleGroup(pplayer.getUniqueId(), group);
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

        ParticleStyle style = ParticleStyle.fromName(args[0]);
        if (style == null) {
            localeManager.sendMessage(pplayer, "style-invalid", StringPlaceholders.single("style", args[0]));
            return;
        } else if (!PlayerParticles.getInstance().getManager(PermissionManager.class).hasStylePermission(pplayer.getPlayer(), style)) {
            localeManager.sendMessage(pplayer, "style-no-permission", StringPlaceholders.single("style", style.getName()));
            return;
        }
        
        ParticleGroup group = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : group.getParticles()) {
            if (particle.getId() == id) {
                particle.setStyle(style);
                break;
            }
        }

        PlayerParticles.getInstance().getManager(DataManager.class).saveParticleGroup(pplayer.getUniqueId(), group);
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
        
        ParticleEffect effect = pplayer.getActiveParticle(id).getEffect();

        if (effect.hasProperty(ParticleProperty.COLORABLE)) {
            if (effect == ParticleEffect.NOTE) {
                if (args[0].equalsIgnoreCase("rainbow")) {
                    noteColorData = new NoteColor(99);
                } else if (args[0].equalsIgnoreCase("random")) {
                    noteColorData = new NoteColor(98);
                } else {
                    int note;
                    try {
                        note = Integer.parseInt(args[0]);
                    } catch (Exception e) {
                        localeManager.sendMessage(pplayer, "data-invalid-note");
                        return;
                    }

                    if (note < 0 || note > 24) {
                        localeManager.sendMessage(pplayer, "data-invalid-note");
                        return;
                    }

                    noteColorData = new NoteColor(note);
                }
            } else {
                if (args[0].equalsIgnoreCase("rainbow")) {
                    colorData = new OrdinaryColor(999, 999, 999);
                } else if (args[0].equalsIgnoreCase("random")) {
                    colorData = new OrdinaryColor(998, 998, 998);
                } else {
                    int r, g, b;

                    try {
                        r = Integer.parseInt(args[0]);
                        g = Integer.parseInt(args[1]);
                        b = Integer.parseInt(args[2]);
                    } catch (Exception e) {
                        localeManager.sendMessage(pplayer, "data-invalid-color");
                        return;
                    }

                    if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) {
                        localeManager.sendMessage(pplayer, "data-invalid-color");
                        return;
                    }

                    colorData = new OrdinaryColor(r, g, b);
                }
            }
        } else if (effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
            if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) {
                try {
                    blockData = ParticleUtils.closestMatch(args[0]);
                    if (blockData == null || !blockData.isBlock()) throw new Exception();
                } catch (Exception e) {
                    localeManager.sendMessage(pplayer, "data-invalid-block");
                    return;
                }
            } else if (effect == ParticleEffect.ITEM) {
                try {
                    itemData = ParticleUtils.closestMatch(args[0]);
                    if (itemData == null || itemData.isBlock()) throw new Exception();
                } catch (Exception e) {
                    localeManager.sendMessage(pplayer, "data-invalid-item");
                    return;
                }
            }
        }
        
        String updatedDataString = null;
        ParticleGroup group = pplayer.getActiveParticleGroup();
        for (ParticlePair particle : group.getParticles()) {
            if (particle.getId() == id) {
                if (itemData != null) particle.setItemMaterial(itemData);
                if (blockData != null) particle.setBlockMaterial(blockData);
                if (colorData != null) particle.setColor(colorData);
                if (noteColorData != null) particle.setNoteColor(noteColorData);
                updatedDataString = particle.getDataString();
                break;
            }
        }

        PlayerParticles.getInstance().getManager(DataManager.class).saveParticleGroup(pplayer.getUniqueId(), group);
        localeManager.sendMessage(pplayer, "edit-success-data", StringPlaceholders.builder("id", id).addPlaceholder("data", updatedDataString).build());
    }

    public List<String> onTabComplete(PPlayer pplayer, String[] args) {
        PermissionManager permissionManager = PlayerParticles.getInstance().getManager(PermissionManager.class);
        Player p = pplayer.getPlayer();
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
                        StringUtil.copyPartialMatches(args[2], permissionManager.getEffectNamesUserHasPermissionFor(p), matches);
                    break;
                case "style":
                    if (args.length == 3)
                        StringUtil.copyPartialMatches(args[2], permissionManager.getStyleNamesUserHasPermissionFor(p), matches);
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
                            if (args.length <= 5 && !args[2].equalsIgnoreCase("rainbow") && !args[2].equalsIgnoreCase("random")) {
                                possibleValues.add("<0-255>");
                            }
                            if (args.length <= 4 && !args[2].equalsIgnoreCase("rainbow") && !args[2].equalsIgnoreCase("random")) {
                                possibleValues.add("<0-255> <0-255>");
                            }
                            if (args.length <= 3) {
                                possibleValues.add("<0-255> <0-255> <0-255>");
                                possibleValues.add("rainbow");
                                possibleValues.add("random");
                            }
                        }
                        StringUtil.copyPartialMatches(args[args.length - 1], possibleValues, matches);
                    } else if (args.length == 3 && effect.hasProperty(ParticleProperty.REQUIRES_MATERIAL_DATA)) {
                        if (effect == ParticleEffect.BLOCK || effect == ParticleEffect.FALLING_DUST) { // Block material
                            StringUtil.copyPartialMatches(args[2], ParticleUtils.getAllBlockMaterials(), matches);
                        } else if (effect == ParticleEffect.ITEM) { // Item material
                            StringUtil.copyPartialMatches(args[2], ParticleUtils.getAllItemMaterials(), matches);
                        }
                    }
                    break;
                }
            }
        }
        
        return matches;
    }

    public String getName() {
        return "edit";
    }

    public String getDescriptionKey() {
        return "command-description-edit";
    }

    public String getArguments() {
        return "<ID> <effect>|<style>|<data> <args>";
    }

    public boolean requiresEffectsAndStyles() {
        return true;
    }

    public boolean canConsoleExecute() {
        return false;
    }

}
