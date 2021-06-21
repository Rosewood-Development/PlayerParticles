package dev.esophose.playerparticles.locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class EnglishLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "en_US";
    }

    @Override
    public String getTranslatorName() {
        return "Esophose";
    }

    @Override
    public Map<String, String> getDefaultLocaleStrings() {
        return new LinkedHashMap<String, String>() {{
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "&7[<g:#00aaaa:#0066aa>PlayerParticles&7] ");

            this.put("#1", "Command Description Messages");
            this.put("command-error-missing-effects-or-styles", "&cYou must have access to effects and styles to use this command!");
            this.put("command-error-unknown", "&cUnknown command, use &b/pp help &cfor a list of commands.");
            this.put("command-descriptions", "&eThe following commands are available:");
            this.put("command-descriptions-usage", "&e/pp %cmd% %args%");
            this.put("command-descriptions-help-1", "&7> &b/pp %cmd% &e- %desc%");
            this.put("command-descriptions-help-2", "&7> &b/pp %cmd% %args% &e- %desc%");
            this.put("command-descriptions-help-other", "&7> &b/ppo <player> <command> &e- Run a /pp command as a player");
            this.put("command-description-add", "Add a new particle");
            this.put("command-description-data", "Check what type of data an effect uses");
            this.put("command-description-default", "The main command. By default, opens the GUI");
            this.put("command-description-edit", "Edit a particle");
            this.put("command-description-effects", "Display a list of effects you can use");
            this.put("command-description-fixed", "Manage your fixed effects");
            this.put("command-description-group", "Manage your groups");
            this.put("command-description-gui", "Display the GUI for easy editing of particles");
            this.put("command-description-help", "Displays the help menu... You have arrived");
            this.put("command-description-info", "Gets the description of one of your active particles");
            this.put("command-description-list", "Lists the IDs of your active particles");
            this.put("command-description-reload", "Reloads the config.yml and lang file");
            this.put("command-description-remove", "Removes some particles");
            this.put("command-description-reset", "Removes all your active particles");
            this.put("command-description-styles", "Display a list of styles you can use");
            this.put("command-description-toggle", "Toggles particle visibility on/off");
            this.put("command-description-use", "Modify your primary particle");
            this.put("command-description-version", "Display the plugin version and author");
            this.put("command-description-worlds", "Find out what worlds particles are disabled in");

            this.put("#2", "Fixed Particle Command Description Messages");
            this.put("command-description-fixed-create", "&e/pp fixed create <<x> <y> <z>|<looking>> <effect> <style> [data] - Creates a new fixed effect");
            this.put("command-description-fixed-create-console", "&e/pp fixed create <x> <y> <z> <world> <effect> <style> [data] - Creates a new fixed effect");
            this.put("command-description-fixed-edit", "&e/pp fixed edit <id> <effect|style|data|location> <args> - Edit part of a fixed effect by its ID");
            this.put("command-description-fixed-remove", "&e/pp fixed remove <ID> - Removes a fixed effect by its ID");
            this.put("command-description-fixed-list", "&e/pp fixed list - Lists all IDs of your fixed effects");
            this.put("command-description-fixed-info", "&e/pp fixed info <ID> - Gets info on one of your fixed effects");
            this.put("command-description-fixed-clear", "&e/pp fixed clear <radius> - Clears all fixed effects of all players within the given radius");
            this.put("command-description-fixed-clear-console", "&e/pp fixed clear <radius> <x> <y> <z> <world> - Clears all fixed effects of all players within the given radius");
            this.put("command-description-fixed-teleport", "&e/pp fixed teleport <id> - Teleports you to one of your fixed effects");

            this.put("#2.5", "Group Command Description Messages");
            this.put("command-description-group-save", "&e/pp group save <name> - Saves all active particles in a new group");
            this.put("command-description-group-load", "&e/pp group load <name> - Loads all particles saved in a group");
            this.put("command-description-group-remove", "&e/pp group remove <name> - Removes a group you have saved");
            this.put("command-description-group-list", "&e/pp group list <name> - List all particle groups you have saved");
            this.put("command-description-group-info", "&e/pp group info <name> - List the particles saved in the group");

            this.put("#3", "ID Messages");
            this.put("id-invalid", "&cThe ID you entered is invalid, it must be a positive whole number!");
            this.put("id-unknown", "&cYou do not have a particle applied with the ID &b%id%&c!");

            this.put("#4", "Other Messages");
            this.put("other-no-permission", "&cYou do not have permission to execute PlayerParticles commands for other players!");
            this.put("other-missing-args", "&cYou are missing some arguments. &b/ppo <player> <command>");
            this.put("other-unknown-player", "&cThe player &b%player% &cwas not found. They must be online.");
            this.put("other-unknown-command", "&cThe command &b/pp %cmd% &cdoes not exist.");
            this.put("other-success", "&eExecuted /pp command for &b%player%&e. Output:");

            this.put("#5", "Add Messages");
            this.put("add-reached-max", "&cUnable to apply particle, you have reached the maximum amount of &b%amount% &callowed!");
            this.put("add-particle-applied", "&aA new particle has been applied with the effect &b%effect%&a, style &b%style%&a, and data &b%data%&a!");
            this.put("data-no-args", "&cMissing argument for effect! Command usage: &b/pp data <effect>");

            this.put("#6", "Edit Messages");
            this.put("edit-invalid-property", "&cAn invalid property &b%prop% &cwas provided. Valid properties: &beffect&c, &bstyle&c, &bdata");
            this.put("edit-success-effect", "&aYour particle with an ID of &b%id% &ahas had its effect changed to &b%effect%&a!");
            this.put("edit-success-style", "&aYour particle with an ID of &b%id% &ahas had its style changed to &b%style%&a!");
            this.put("edit-success-data", "&aYour particle with an ID of &b%id% &ahas had its data changed to &b%data%&a!");

            this.put("#7", "Group Messages");
            this.put("group-invalid", "&cA saved group or preset group does not exist with the name &b%name%&c!");
            this.put("group-no-permission", "&cYou are missing permission for an effect or style to use the group &b%group%&c!");
            this.put("group-preset-no-permission", "&cYou are missing permission for an effect or style to use the preset group &b%group%&c!");
            this.put("group-reserved", "&cThe group name &bactive &cis reserved and cannot be used!");
            this.put("group-no-name", "&cYou did not provide a group name! &b/pp %cmd% <groupName>");
            this.put("group-save-reached-max", "&cUnable to save group, you have reached the max number of groups!");
            this.put("group-save-no-particles", "&cUnable to save group, you do not have any particles applied!");
            this.put("group-save-success", "&aYour current particles have been saved under the group named &b%name%&a!");
            this.put("group-save-success-overwrite", "&aThe group named &b%name% &ahas been updated with your current particles!");
            this.put("group-load-success", "&aApplied &b%amount% &aparticle(s) from your saved group named &b%name%&a!");
            this.put("group-load-preset-success", "&aApplied &b%amount% &aparticle(s) from the preset group named &b%name%&a!");
            this.put("group-remove-preset", "&cYou cannot remove a preset group!");
            this.put("group-remove-success", "&aRemoved the particle group named &b%name%&a!");
            this.put("group-info-header", "&eThe group &b%group% &ehas the following particles:");
            this.put("group-list-none", "&eYou do not have any particle groups saved!");
            this.put("group-list-output", "&eYou have the following groups saved: &b%info%");
            this.put("group-list-presets", "&eThe following preset groups are available: &b%info%");

            this.put("#8", "Reload Messages");
            this.put("reload-success", "&aThe plugin has been reloaded!");
            this.put("reload-no-permission", "&cYou do not have permission to reload the plugin settings!");

            this.put("#9", "Remove Messages");
            this.put("remove-no-args", "&cYou did not specify an ID to remove! &b/pp remove <ID>");
            this.put("remove-id-success", "&aYour particle with the ID &b%id% &ahas been removed!");
            this.put("remove-effect-success", "&aRemoved &b%amount% &aof your particles with the effect of &b%effect%&a!");
            this.put("remove-effect-none", "&cYou do not have any particles applied with the effect &b%effect%&c!");
            this.put("remove-style-success", "&aRemoved &b%amount% &aof your particles with the style of &b%style%&a!");
            this.put("remove-style-none", "&cYou do not have any particles applied with the style &b%style%&c!");
            this.put("remove-effect-style-none", "&cYou do not have any particles applied with the effect or style &b%name%&c!");
            this.put("remove-unknown", "&cAn effect or style with the name of &b%name% &cdoes not exist!");

            this.put("#10", "List Messages");
            this.put("list-none", "&eYou do not have any active particles!");
            this.put("list-you-have", "&eYou have the following particles applied:");
            this.put("list-output", "&eID: &b%id% &eEffect: &b%effect% &eStyle: &b%style% &eData: &b%data%");

            this.put("#11", "Toggle Messages");
            this.put("toggle-on", "&eParticles have been toggled &aON&e!");
            this.put("toggle-off", "&eParticles have been toggled &cOFF&e!");

            this.put("#11.5", "Use Messages");
            this.put("use-particle-modified", "&aYour primary particle has been modified to use the effect &b%effect%&a, style &b%style%&a, and data &b%data%&a!");

            this.put("#12", "Color Messages");
            this.put("rainbow", "&cR&6a&ei&an&bb&9o&dw");
            this.put("random", "Random");

            this.put("#13", "Effect Messages");
            this.put("effect-no-permission", "&cYou do not have permission to use the effect &b%effect%&c!");
            this.put("effect-invalid", "&cThe effect &b%effect% &cdoes not exist! Use &b/pp effects &cfor a list of effects you can use.");
            this.put("effect-list", "&eYou can use the following effects: &b%effects%");
            this.put("effect-list-empty", "&cYou do not have permission to use any effects!");

            this.put("#14", "Style Messages");
            this.put("style-no-permission", "&cYou do not have permission to use the style &b%style%&c!");
            this.put("style-event-spawning-info", "&eNote: The style &b%style% &espawns particles based on an event.");
            this.put("style-invalid", "&cThe style &b%style% &cdoes not exist! Use &b/pp styles &cfor a list of styles you can use.");
            this.put("style-list", "&eYou can use the following styles: &b%styles%");

            this.put("#15", "Data Messages");
            this.put("data-usage-none", "&eThe effect &b%effect% &edoes not use any data!");
            this.put("data-usage-block", "&eThe effect &b%effect% &erequires &bblock &edata! &bFormat: <blockName>");
            this.put("data-usage-item", "&eThe effect &b%effect% &erequires &bitem &edata! &bFormat: <itemName>");
            this.put("data-usage-color", "&eThe effect &b%effect% &erequires &bcolor &edata! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-note", "&eThe effect &b%effect% &erequires &bnote &edata! &bFormat: <0-24>|<&brainbow>|<random>");
            this.put("data-usage-color-transition", "&eThe effect &b%effect% &erequires &bcolor transition &edata! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-vibration", "&eThe effect &b%effect% &erequires &bvibration &edata! &bFormat: <duration>");
            this.put("data-invalid-block", "&cThe &bblock &cdata you entered is invalid! &bFormat: <blockName>");
            this.put("data-invalid-item", "&cThe &bitem &cdata you entered is invalid! &bFormat: <itemName>");
            this.put("data-invalid-color", "&cThe &bcolor &cdata you entered is invalid! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-note", "&cThe &bnote &cdata you entered is invalid! &bFormat: <0-24>|<&brainbow>|<random>");
            this.put("data-invalid-color-transition", "&cThe &bcolor transition &cdata you entered is invalid! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-vibration", "&cThe &bvibration &cdata you entered is invalid! &bFormat: <duration>");
            this.put("data-invalid-material-not-item", "&cThe &bitem &cmaterial &b%material% &cyou entered is not an item!");
            this.put("data-invalid-material-not-block", "&cThe &bblock &cmaterial &b%material% &cyou entered is not a block!");
            this.put("data-invalid-material-item", "&cThe &bitem &cmaterial &b%material% you entered does not exist!");
            this.put("data-invalid-material-block", "&cThe &bblock &cmaterial &b%material% you entered does not exist!");

            this.put("#16", "World Messages");
            this.put("disabled-worlds", "&eParticles are disabled in these worlds: &b%worlds%");
            this.put("disabled-worlds-none", "&eParticles are not disabled in any worlds.");

            this.put("#17", "Reset Message");
            this.put("reset-success", "&aRemoved &b%amount% &aactive particle(s)!");
            this.put("reset-others-success", "&aRemoved particles for &b%other%&a!");
            this.put("reset-others-none", "&eNo particles were removed for &b%other%&e.");

            this.put("#18", "Fixed Create Messages");
            this.put("fixed-create-missing-args", "&cUnable to create fixed effect, you are missing &b%amount% &crequired arguments!");
            this.put("fixed-create-invalid-coords", "&cUnable to create fixed effect, one or more coordinates you entered is invalid!");
            this.put("fixed-create-out-of-range", "&cUnable to create fixed effect, you must be within &b%range% &cblocks of your desired location!");
            this.put("fixed-create-looking-too-far", "&cUnable to create fixed effect, you are standing too far away from the block you are looking at!");
            this.put("fixed-create-effect-invalid", "&cUnable to create fixed effect, an effect with the name &b%effect% &cdoes not exist!");
            this.put("fixed-create-effect-no-permission", "&cUnable to create fixed effect, you do not have permission to use the effect &b%effect%&c!");
            this.put("fixed-create-style-invalid", "&cUnable to create fixed effect, a style with the name &b%style% &cdoes not exist!");
            this.put("fixed-create-style-no-permission", "&cUnable to create fixed effect, you do not have permission to use the style &b%style%&c!");
            this.put("fixed-create-style-non-fixable", "&cUnable to create fixed effect, the style &b%style% &ccan not be used in fixed effects!");
            this.put("fixed-create-data-error", "&cUnable to create fixed effect, the data provided is not correct! Use &b/pp data <effect> &cto find the correct data format!");
            this.put("fixed-create-success", "&aYour fixed effect has been created!");

            this.put("#19", "Fixed Edit Messages");
            this.put("fixed-edit-missing-args", "&cUnable to edit fixed effect, you are missing some arguments!");
            this.put("fixed-edit-invalid-id", "&cUnable to edit fixed effect, the ID specified is invalid or does not exist!");
            this.put("fixed-edit-invalid-property", "&cUnable to edit fixed effect, an invalid property was specified! Only &blocation&c, &beffect&c, &bstyle&c, and &bdata &care valid.");
            this.put("fixed-edit-invalid-coords", "&cUnable to edit fixed effect, one or more coordinates you entered is invalid!");
            this.put("fixed-edit-out-of-range", "&cUnable to edit fixed effect, you must be within &b%range% &cblocks of your desired location!");
            this.put("fixed-edit-looking-too-far", "&cUnable to edit fixed effect, you are standing too far away from the block you are looking at!");
            this.put("fixed-edit-effect-invalid", "&cUnable to edit fixed effect, an effect with the name &b%effect% &cdoes not exist!");
            this.put("fixed-edit-effect-no-permission", "&cUnable to edit fixed effect, you do not have permission to use the effect &b%effect%&c!");
            this.put("fixed-edit-style-invalid", "&cUnable to edit fixed effect, a style with the name &b%style% &cdoes not exist!");
            this.put("fixed-edit-style-no-permission", "&cUnable to edit fixed effect, you do not have permission to use the style &b%style%&c!");
            this.put("fixed-edit-style-non-fixable", "&cUnable to edit fixed effect, the style &b%style% &ccan not be used in fixed effects!");
            this.put("fixed-edit-data-error", "&cUnable to edit fixed effect, the data provided is not correct! Use &b/pp data <effect> &cto find the correct data format!");
            this.put("fixed-edit-data-none", "&cUnable to edit fixed effect, the effect does not require any data!");
            this.put("fixed-edit-success", "&aUpdated the &b%prop% &aof the fixed effect with an ID of &b%id%&a!");

            this.put("#20", "Fixed Remove Messages");
            this.put("fixed-remove-invalid", "&cUnable to remove fixed effect, you do not have a fixed effect with the ID of &b%id%&c!");
            this.put("fixed-remove-no-args", "&cYou did not specify an ID to remove!");
            this.put("fixed-remove-args-invalid", "&cUnable to remove, the ID specified must be a number!");
            this.put("fixed-remove-success", "&aYour fixed effect with the ID &b%id% &ahas been removed!");

            this.put("#21", "Fixed List Messages");
            this.put("fixed-list-none", "&eYou do not have any fixed effects!");
            this.put("fixed-list-success", "&eYou have fixed effects with these IDs: &b%ids%");

            this.put("#22", "Fixed Info Messages");
            this.put("fixed-info-invalid", "&cUnable to get info, you do not have a fixed effect with the ID of &b%id%&c!");
            this.put("fixed-info-no-args", "&cYou did not specify an ID to display info for!");
            this.put("fixed-info-invalid-args", "&cUnable to get info, the ID specified must be a number!");
            this.put("fixed-info-success", "&eID: &b%id% &eWorld: &b%world% &eX: &b%x% &eY: &b%y% &eZ: &b%z% &eEffect: &b%effect% &eStyle: &b%style% &eData: &b%data%");

            this.put("#23", "Fixed Clear Messages");
            this.put("fixed-clear-no-permission", "&cYou do not have permission to clear nearby fixed effects!");
            this.put("fixed-clear-no-args", "&cYou did not provide a radius to clear fixed effects for!");
            this.put("fixed-clear-invalid-args", "&cThe radius you provided is invalid, it must be a positive whole number!");
            this.put("fixed-clear-success", "&aCleared &b%amount% &afixed effects within &b%range% &ablocks of your location!");

            this.put("#23.5", "Fixed Teleport Message");
            this.put("fixed-teleport-no-permission", "&cYou do not have permission to teleport to fixed effects!");
            this.put("fixed-teleport-no-args", "&cYou did not specify an ID to teleport to!");
            this.put("fixed-teleport-invalid-args", "&cUnable to teleport, the ID specified is invalid!");
            this.put("fixed-teleport-success", "&eTeleported to your fixed effect with an ID of &b%id%&e!");

            this.put("#24", "Fixed Other Messages");
            this.put("fixed-no-permission", "&cYou do not have permission to use fixed effects!");
            this.put("fixed-max-reached", "&cYou have reached the maximum allowed fixed effects!");
            this.put("fixed-invalid-command", "&cInvalid sub-command for &b/pp fixed&c!");

            this.put("#25", "Plugin Update Message");
            this.put("update-available", "&eAn update (&bv%new%&e) is available! You are running &bv%current%&e. https://www.spigotmc.org/resources/playerparticles.40261/");

            this.put("#26", "GUI Messages");
            this.put("gui-disabled", "&cThe server administrator has disabled the GUI!");
            this.put("gui-no-permission", "&cYou do not have permission to open the GUI!");

            this.put("#27", "GUI Color Messages");
            this.put("gui-color-icon-name", "&a");
            this.put("gui-color-info", "&e");
            this.put("gui-color-subtext", "&b");
            this.put("gui-color-unavailable", "&c");

            this.put("#28", "GUI Info Messages");
            this.put("gui-commands-info", "Find info about commands with &b/pp help");
            this.put("gui-toggle-visibility-on", "Particles are currently &avisible");
            this.put("gui-toggle-visibility-off", "Particles are currently &chidden");
            this.put("gui-toggle-visibility-info", "Click to toggle particle visibility");
            this.put("gui-back-button", "Go Back");
            this.put("gui-next-page-button", "Next Page (%start%/%end%)");
            this.put("gui-previous-page-button", "Previous Page (%start%/%end%)");
            this.put("gui-click-to-load", "Click to load the following %amount% particle(s):");
            this.put("gui-shift-click-to-delete", "Shift click to delete");
            this.put("gui-particle-info", "  - ID: &b%id% &eEffect: &b%effect% &eStyle: &b%style% &eData: &b%data%");
            this.put("gui-playerparticles", "PlayerParticles");
            this.put("gui-active-particles", "Active Particles: &b%amount%");
            this.put("gui-saved-groups", "Saved Groups: &b%amount%");
            this.put("gui-fixed-effects", "Fixed Effects: &b%amount%");

            this.put("#29", "GUI Edit Primary Messages");
            this.put("gui-edit-primary-effect", "Edit Primary Effect");
            this.put("gui-edit-primary-effect-description", "Edit the effect of your primary particle");
            this.put("gui-edit-primary-style", "Edit Primary Style");
            this.put("gui-edit-primary-style-missing-effect", "You must select an effect first");
            this.put("gui-edit-primary-style-description", "Edit the style of your primary particle");
            this.put("gui-edit-primary-data", "Edit Primary Data");
            this.put("gui-edit-primary-data-missing-effect", "You must select an effect first");
            this.put("gui-edit-primary-data-unavailable", "Your primary effect does not use any data");
            this.put("gui-edit-primary-data-description", "Edit the data of your primary particle");

            this.put("#30", "GUI Manage Particles Messages");
            this.put("gui-manage-your-particles", "Manage Your Particles");
            this.put("gui-manage-your-particles-description", "Create, edit, and delete your particles");
            this.put("gui-manage-your-groups", "Manage Your Groups");
            this.put("gui-manage-your-groups-description", "Create, delete, and load your particle groups");

            this.put("#31", "GUI Load Messages");
            this.put("gui-load-a-preset-group", "Load A Preset Group");
            this.put("gui-load-a-preset-group-description", "Load a premade particle group");

            this.put("#32", "GUI Save Messages");
            this.put("gui-save-group", "Save New Group");
            this.put("gui-save-group-description", "Click to save a new group. You will be prompted\nto enter the new group name in chat.");
            this.put("gui-save-group-full", "You have reached the max number of groups");
            this.put("gui-save-group-no-particles", "You do not have any particles applied");
            this.put("gui-save-group-hotbar-message", "&eType &b1 &eword in chat for the new group name. Type &ccancel&e to cancel. (&b%seconds%&es left)");
            this.put("gui-save-group-chat-message", "&eUse &b/pp group save <name> &eto save a new particle group.");

            this.put("#33", "GUI Reset Messages");
            this.put("gui-reset-particles", "Reset Your Particles");
            this.put("gui-reset-particles-description", "Deletes all your active particles");

            this.put("#34", "GUI Misc Messages");
            this.put("gui-particle-name", "Particle #%id%");
            this.put("gui-click-to-edit-particle", "Click to edit the effect, style, or data of this particle");
            this.put("gui-editing-particle", "Editing Particle #%id%");

            this.put("#35", "GUI Edit Messages");
            this.put("gui-edit-effect", "Edit Effect");
            this.put("gui-edit-effect-description", "Click to edit the effect of this particle");
            this.put("gui-edit-style", "Edit Style");
            this.put("gui-edit-style-description", "Click to edit the style of this particle");
            this.put("gui-edit-data", "Edit Data");
            this.put("gui-edit-data-description", "Click to edit the data of this particle");
            this.put("gui-edit-data-unavailable", "The effect of this particle does not use any data");
            this.put("gui-data-none", "none");

            this.put("#36", "GUI Create Messages");
            this.put("gui-create-particle", "Create A New Particle");
            this.put("gui-create-particle-description", "Create a new particle with an effect, style, and data");
            this.put("gui-create-particle-unavailable", "You have reached the maximum amount of particles you can create");

            this.put("#37", "GUI Select Messages");
            this.put("gui-select-effect", "Select Particle Effect");
            this.put("gui-select-effect-description", "Sets the particle effect to &b%effect%");
            this.put("gui-select-style", "Select Particle Style");
            this.put("gui-select-style-description", "Sets the particle style to &b%style%");
            this.put("gui-select-data", "Select Particle Data");
            this.put("gui-select-data-description", "Sets the particle data to &b%data%");
            this.put("gui-select-data-note", "Note #%note%");
            this.put("gui-select-data-color-transition-start", "&eSelect the &bstart &ecolor");
            this.put("gui-select-data-color-transition-end", "&eSelect the &bend &ecolor");
            this.put("gui-select-data-vibration", "&b%ticks% &eticks");

            this.put("#38", "GUI Color Name Messages");
            this.put("gui-edit-data-color-red", "#ff0000Red");
            this.put("gui-edit-data-color-orange", "#ff8c00Orange");
            this.put("gui-edit-data-color-yellow", "#ffff00Yellow");
            this.put("gui-edit-data-color-lime-green", "#32cd32Lime Green");
            this.put("gui-edit-data-color-green", "#008000Green");
            this.put("gui-edit-data-color-blue", "#0000ffBlue");
            this.put("gui-edit-data-color-cyan", "#008b8bCyan");
            this.put("gui-edit-data-color-light-blue", "#add8e6Light Blue");
            this.put("gui-edit-data-color-purple", "#8a2be2Purple");
            this.put("gui-edit-data-color-magenta", "#ca1f7bMagenta");
            this.put("gui-edit-data-color-pink", "#ffb6c1Pink");
            this.put("gui-edit-data-color-brown", "#8b4513Brown");
            this.put("gui-edit-data-color-black", "#000000Black");
            this.put("gui-edit-data-color-gray", "#808080Gray");
            this.put("gui-edit-data-color-light-gray", "#c0c0c0Light Gray");
            this.put("gui-edit-data-color-white", "#ffffffWhite");
        }};
    }

}
