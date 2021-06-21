package dev.esophose.playerparticles.locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class FrenchLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "fr_FR";
    }

    @Override
    public String getTranslatorName() {
        return "maxime_n2 & SevenX";
    }

    @Override
    public Map<String, String> getDefaultLocaleStrings() {
        return new LinkedHashMap<String, String>() {{
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "&7[<g:#00aaaa:#0066aa>PlayerParticles&7] ");

            this.put("#1", "Command Description Messages");
            this.put("command-error-missing-effects-or-styles", "&cVous devez avoir accès aux effets et aux styles pour utiliser cette commande !");
            this.put("command-error-unknown", "&cCommande inconnue, utilisez &b/pp help &cpour afficher la liste des commandes disponibles pour ce plugin.");
            this.put("command-descriptions", "&eLes commandes suivantes sont disponibles :");
            this.put("command-descriptions-usage", "&e/pp %cmd% %args%");
            this.put("command-descriptions-help-1", "&7> &b/pp %cmd% &e- %desc%");
            this.put("command-descriptions-help-2", "&7> &b/pp %cmd% %args% &e- %desc%");
            this.put("command-descriptions-help-other", "&7> &b/ppo <player> <command> &e- Exécute une commande /pp en tant qu'un autre joueur.");
            this.put("command-description-add", "Ajoutez une nouvelle particule");
            this.put("command-description-data", "Voir les paramètres utilisées par la particule");
            this.put("command-description-default", "Commande principale. Par défaut elle ouvre l'interface.");
            this.put("command-description-edit", "Modifiez une particule");
            this.put("command-description-effects", "Affichez une liste des effets utilisés");
            this.put("command-description-fixed", "Gérez vos effets fixes");
            this.put("command-description-group", "Gérez vos groupes d'effets");
            this.put("command-description-gui", "Affichez l'interface pour modifier facilement les effets");
            this.put("command-description-help", "Affichez le menu d'aide... Vous y êtes déjà !");
            this.put("command-description-info", "Voir la description d'une de vos particules actives");
            this.put("command-description-list", "Listez les IDs de vos particules actives");
            this.put("command-description-reload", "Rechargez le fichier config.yml et de langue");
            this.put("command-description-remove", "Supprimez vos particules actives.");
            this.put("command-description-reset", "Supprimez toutes vos particules actives");
            this.put("command-description-styles", "Affichez une liste des styles de particules que vous utilisez");
            this.put("command-description-toggle", "Activez ou désactivez vos particules actives");
            this.put("command-description-use", "Modifiez votre particule primaire");
            this.put("command-description-version", "Affichez la version du plugin et son créateur");
            this.put("command-description-worlds", "Voir les mondes où ce plugin n'est pas autorisé");

            this.put("#2", "Fixed Particle Command Description Messages");
            this.put("command-description-fixed-create", "&e/pp fixed create <<x> <y> <z>|<looking>> <effect> <style> [data] - Créez une particule fixe");
            this.put("command-description-fixed-create-console", "&e/pp fixed create <x> <y> <z> <world> <effect> <style> [data] - Créez une particule fixe");
            this.put("command-description-fixed-edit", "&e/pp fixed edit <id> <effect|style|data|location> <args> - Modifiez une partie d'une particule fixe par son ID");
            this.put("command-description-fixed-remove", "&e/pp fixed remove <id> - Supprimez une particule fixe par son ID");
            this.put("command-description-fixed-list", "&e/pp fixed list - Affiche l'ID de tous vos effets fixes");
            this.put("command-description-fixed-info", "&e/pp fixed info <id> - Voir des informations sur l'une de vos particules fixe");
            this.put("command-description-fixed-clear", "&e/pp fixed clear <radius> - Supprimez tous les effets fixe de tous les joueurs d'un rayon");
            this.put("command-description-fixed-clear-console", "&e/pp fixed clear <radius> <x> <y> <z> <world> - Supprimez tous les effets fixe de tous les joueurs d'un rayon");
            this.put("command-description-fixed-teleport", "&e/pp fixed teleport <id> - Vous téléporte vers un de vos effets fixes");

            this.put("#2.5", "Group Command Description Messages");
            this.put("command-description-group-save", "&e/pp group save <name> - Sauvegardez toutes les particules actives dans un nouveau groupe");
            this.put("command-description-group-load", "&e/pp group load <name> - Chargez toutes les particules sauvegardées dans un groupe");
            this.put("command-description-group-remove", "&e/pp group remove <name> - Supprimez un groupe que vous avez créé");
            this.put("command-description-group-list", "&e/pp group list <name> - Voir toutes les particules sauvegardées d'un groupe que vous avez créé");
            this.put("command-description-group-info", "&e/pp group info <name> - Voir toutes les particules sauvegardées du groupe");

            this.put("#3", "ID Messages");
            this.put("id-invalid", "&cL'ID rentrée n'est pas valide, il doit être un nombre entier positif");
            this.put("id-unknown", "&cVous n'avez pas de particules appliquées avec : &b%id% &c!");

            this.put("#4", "Other Messages");
            this.put("other-no-permission", "&cVous n'avez pas la permission pour exécuter une commande /pp en tant qu'un autre joueur.");
            this.put("other-missing-args", "&cVous oubliez des arguments dans votre commande. &b/ppo <player> <command>");
            this.put("other-unknown-player", "&cLe joueur &b%player% &cn'a pas été trouvé. Il doit être en ligne.");
            this.put("other-unknown-command", "&cLa commande &b/pp %cmd% &cn existe pas.");
            this.put("other-success", "&Commande /pp exécutée. &b%player%&e. Retour de la commande :");

            this.put("#5", "Add Messages");
            this.put("add-reached-max", "Impossible d'appliquer la particule, vous avez atteint la limite de particules qui est de &b%amount% &c!");
            this.put("add-particle-applied", "&aUne nouvelle particule est appliquée avec l'effet &b%effect%&a, le style &b%style%&a, et les paramètres &b%data% &a!");
            this.put("data-no-args", "&cVous oubliez des arguments dans l'effet. Utilisation de la commande: &b/pp data <effect>");

            this.put("#6", "Edit Messages");
            this.put("edit-invalid-property", "cLa propriété &b%prop% &cest interdite. Propriétés valides : &beffect&c, &bstyle&c, &bdata");
            this.put("edit-success-effect", "&aVotre particule avec l'ID de &b%id% &aa son effet changé à &b%effect% &a!");
            this.put("edit-success-style", "&aVotre particule avec l'ID de &b%id% &aa son style changé à &b%style% &a!");
            this.put("edit-success-data", "&aVotre particule avec l'ID de &b%id% &aa ses paramètres changés à &b%data% &a!");

            this.put("#7", "Group Messages");
            this.put("group-invalid", "&cUn groupe ou un preset de groupe sauvegardé n'existe pas avec le nom &b%name% &c!");
            this.put("group-no-permission", "&cVous oubliez une permission pour un style ou un effet afin d'utiliser les groupe &b%group% &c!");
            this.put("group-preset-no-permission", "&cVous oubliez une permission pour un style ou un effet afin d'utiliser les presets du groupe &b%group% &c!");
            this.put("group-reserved", "&cLe nom de groupe &bactive &cest réservé et ne peut pas être utiliser !");
            this.put("group-no-name", "&cVous n'avez pas rentré de nom de groupe ! &b/pp %cmd% <groupName>");
            this.put("group-save-reached-max", "&cImpossible de sauvegarder le groupe, vous avez atteint le nombre maximun de groupes !");
            this.put("group-save-no-particles", "&cImpossible de sauvegarder le groupe, vous n'avez pas appliqué de particules !");
            this.put("group-save-success", "&aVos particules actuelles ont été sauvegardées sous le nom de groupe &b%name% &a!");
            this.put("group-save-success-overwrite", "&aLe groupe &b%name% &aa été mis à jour avec vos particules actuelle !");
            this.put("group-load-success", "&b%amount% &aparticule(s) appliqués venant du groupe sauvegardé nommé &b%name% &a!");
            this.put("group-load-preset-success", "&b%amount% &aparticule(s) appliqués venant du preset sauvegardé nommé &b%name% &a!");
            this.put("group-remove-preset", "&cVous ne pouvez pas supprimer un groupe de presets !");
            this.put("group-remove-success", "&aGroupe de particules &b%name% &asupprimé !");
            this.put("group-info-header", "&eLe groupe &b%group% &eposséde les particules suivantes :");
            this.put("group-list-none", "&eVous n'avez pas de groupes de particules sauvegardés !");
            this.put("group-list-output", "&eVous avez ces groupes suivants sauvegardés : &b%info%");
            this.put("group-list-presets", "&eCes presets de groupes suivants sont disponibles : &b%info%");

            this.put("#8", "Reload Messages");
            this.put("reload-success", "&aLe plugin a été rechargé...");
            this.put("reload-no-permission", "&cVous n'avez pas la permission pour recharger la configuration de ce plugin !");

            this.put("#9", "Remove Messages");
            this.put("remove-no-args", "&cVous n'avez pas rentrer d'ID à supprimer ! &b/pp remove <ID>");
            this.put("remove-id-success", "&aVotre particule avec l'ID &b%id% &aa été supprimée !");
            this.put("remove-effect-success", "&aSuppression de &b%amount% &ade votre particule avec l'effet &b%effect% &a!");
            this.put("remove-effect-none", "&cVous n'avez pas de particules appliquées avec l'effet &b%effect% &c!");
            this.put("remove-style-success", "&aSuppression &b%amount% &ade votre particule avec le style &b%style% &a!");
            this.put("remove-style-none", "&cVous n'avez pas de particules appliquées avec le style &b%style% &c!");
            this.put("remove-effect-style-none", "&cVous n'avez pas de particules appliquées avec l'effet ou le style &b%name% &c!");
            this.put("remove-unknown", "&cL'effect avec le nom ou le style &b%name% &cn'existe pas !");

            this.put("#10", "List Messages");
            this.put("list-none", "&eVous n'avez pas de particules actives.");
            this.put("list-you-have", "&eVous avez les particules suivantes appliquées :");
            this.put("list-output", "&eID: &b%id% &eEffet: &b%effect% &eStyle: &b%style% &eParamètre: &b%data%");

            this.put("#11", "Toggle Messages");
            this.put("toggle-on", "&eLes particules sont maintenant en mode &aON &e!");
            this.put("toggle-off", "&eLes particules sont maintenant en mode &cOFF &e!");

            this.put("#11.5", "Use Messages");
            this.put("use-particle-modified", "&aVotre particule primaire a été modifiée pour utiliser l'effet &b%effect%&a, le style &b%style%&a, et les données &b%data%&a !");

            this.put("#12", "Color Messages");
            this.put("rainbow", "&cr&6a&ei&an&bb&9o&dw");
            this.put("random", "aléatoire");

            this.put("#13", "Effect Messages");
            this.put("effect-no-permission", "&cVous n'avez pas la permission pour utiliser la particule &b%effect% &c!");
            this.put("effect-invalid", "&cL'effet &b%effect% &cn existe pas ! Utilisez &b/pp effects &cpour afficher les effets disponibles.");
            this.put("effect-list", "&eVous pouvez utiliser les effets suivants : &b%effects%");
            this.put("effect-list-empty", "&cVous n'avez pas la permission pour utiliser des effets !");

            this.put("#14", "Style Messages");
            this.put("style-no-permission", "&cVous n'avez pas la permission pour utiliser le style &b%style% &c!");
            this.put("style-event-spawning-info", "&eNote: Le style &b%style% &efait apparaitre des particules seulement lors d'évènements spécifiques.");
            this.put("style-invalid", "&cLe style &b%style% &cn'existe pas ! Utilisez &b/pp styles &cpour afficher les styles disponibles.");
            this.put("style-list", "&eVous pouvez utiliser les styles suivants : &b%styles%");

            this.put("#15", "Data Messages");
            this.put("data-usage-none", "&eL'effet &b%effect% &en'est pas paramétrable.");
            this.put("data-usage-block", "&eL'effet &b%effect% &erequière l'ID du bloc ! &bFormat: <blockName>");
            this.put("data-usage-item", "&eL'effet &b%effect% &erequière l'ID de l'item ! &bFormat: <itemName>");
            this.put("data-usage-color", "&eL'effet &b%effect% &erequière l'ID de la couleur ! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-note", "&eL'effet &b%effect% &erequière l'ID de la note ! &bFormat: <0-24>|<&brainbow>|<random>");
            this.put("data-usage-color-transition", "&eL'effet &b%effect% &erequière l'ID de transition de couleur ! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-vibration", "&eL'effet &b%effect% &erequière l'ID de la vibration ! &bFormat: <duration>");
            this.put("data-invalid-block", "&cL'ID du bloc que vous avez rentré n'est pas valide ! &bFormat: <blockName>");
            this.put("data-invalid-item", "&cL'ID de l'item que vous avez rentré n'est pas valide ! &bFormat: <itemName>");
            this.put("data-invalid-color", "&cL'ID de la couleur que vous avez rentré n'est pas valide ! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-note", "&cL'ID de la note que vous avez rentré n'est pas valide ! &bFormat: <0-24>|<&brainbow>|<random>");
            this.put("data-invalid-color-transition", "&cL'ID de transition de couleur que vous avez rentré n'est pas valide ! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-vibration", "&cL'ID de la vibration que vous avez rentré n'est pas valide ! &bFormat: <duration>");
            this.put("data-invalid-material-not-item", "&cL'ID &b%material% &cne correspond pas à un item !");
            this.put("data-invalid-material-not-block", "&cL'ID &b%material% &cne correspond pas à un bloc !");
            this.put("data-invalid-material-item", "&cL'ID &b%material% n'existe pas !");
            this.put("data-invalid-material-block", "&cL'ID &b%material% n'existe pas !");

            this.put("#16", "World Messages");
            this.put("disabled-worlds", "&eLes particules sont désactivées dans ces mondes : &b%worlds%");
            this.put("disabled-worlds-none", "&Les particules sont désactivées dans aucuns mondes.");

            this.put("#17", "Reset Message");
            this.put("reset-success", "&b%amount% &aparticule(s) actives supprimées !");
            this.put("reset-others-success", "&aParticules enlevées pour &b%other%&a !");
            this.put("reset-others-none", "&eAucune particule n'a été enlevée pour &b%other%&e.");

            this.put("#18", "Fixed Create Messages");
            this.put("fixed-create-missing-args", "&cImpossible de créer un effet fixe, vous oubliez des arguments : &b%amount%");
            this.put("fixed-create-invalid-coords", "&cImpossible de créer un effet fixe, coordonnées invalides !");
            this.put("fixed-create-out-of-range", "&cImpossible de créer un effet fixe, Vous devez être à &b%range% &cblocs de la position rentrée !");
            this.put("fixed-create-looking-too-far", "&cImpossible de créer un effet fixe, vous êtes trop loin du bloc que vous regardez !");
            this.put("fixed-create-effect-invalid", "&cImpossible de créer un effet fixe, l effet &b%effect% &cn'existe pas !");
            this.put("fixed-create-effect-no-permission", "&cImpossible de créer un effet fixe, vous n'avez pas la permission pour utiliser le style &b%effect% &c!");
            this.put("fixed-create-style-invalid", "&cImpossible de créer un effet fixe, le style &b%style% &cn'existe pas!");
            this.put("fixed-create-style-no-permission", "&cImpossible de créer un effet fixe, vous n'avez pas la permission pour utiliser le style &b%style% &c!");
            this.put("fixed-create-style-non-fixable", "&cImpossible de créer un effet fixe, le style &b%style% &cne peut pas être utilisé en tant qu'effet fixe !");
            this.put("fixed-create-data-error", "&cImpossible de créer un effet fixe, l'ID rentrée n'est pas valide ! Utilisez &b/pp data <effect> &cpour afficher les IDs valides.");
            this.put("fixed-create-success", "&aL'effet fixe à été créer.");

            this.put("#19", "Fixed Edit Messages");
            this.put("fixed-edit-missing-args", "&cImpossible de modifier l'effet fixe, vous oubliez des arguments !");
            this.put("fixed-edit-invalid-id", "&cImpossible de modifier l'effet fixe, ID incorrecte !");
            this.put("fixed-edit-invalid-property", "&cImpossible de modifier l'effet fixe, argument incorrect ! Seuls localisation, effet, style, et paramètre sont valides.");
            this.put("fixed-edit-invalid-coords", "&cImpossible de modifier l'effet fixe, coordonnée incorrecte !");
            this.put("fixed-edit-out-of-range", "&cImpossible de modifier l'effet fixe, vous devez être à &b%range% &cblocs de l'emplacement désiré !");
            this.put("fixed-edit-looking-too-far", "&cImpossible de modifier l'effet fixe, vous êtes trop loin du bloc que vous regardez !");
            this.put("fixed-edit-effect-invalid", "&cImpossible de modifier l'effet fixe, l'effet &b%effect% &cn'existe pas !");
            this.put("fixed-edit-effect-no-permission", "&cImpossible de modifier l'effet fixe, vous n'avez pas la permission pour utiliser l'effet &b%effect% &c!");
            this.put("fixed-edit-style-invalid", "&cImpossible de modifier l'effet fixe, le style &b%style% &cn'existe pas !");
            this.put("fixed-edit-style-no-permission", "&cImpossible de modifier l'effet fixe, vous n'avez pas la permission pour utiliser le style &b%style% &c!");
            this.put("fixed-edit-style-non-fixable", "&cImpossible de modifier l'effet fixe, the style &b%style% &cne peut pas être utilisé dans des effets fixes !");
            this.put("fixed-edit-data-error", "&cImpossible de modifier l'effet fixe, paramètre incorrect ! Utilisez &b/pp data <effect> pour afficher les paramètres valides.");
            this.put("fixed-edit-data-none", "&cImpossible de modifier l'effet fixe, l'effet ne requière pas de paramètres !");
            this.put("fixed-edit-success", "&aMise à jour de &b%prop% &ade l'effet fixe avec l'ID &b%id% &a!");

            this.put("#20", "Fixed Remove Messages");
            this.put("fixed-remove-invalid", "&cImpossible de supprimer l'effet fixe, vous n'avez pas d effets fixes avec l'ID &b%id% &c!");
            this.put("fixed-remove-no-args", "&cVous n'avez pas rentrer d'ID à supprimer !");
            this.put("fixed-remove-args-invalid", "&cImpossible de supprimer, l'ID doit être un nombre !");
            this.put("fixed-remove-success", "&aTous vos effets fixes avec l'ID &b%id% &aont été supprimés !");

            this.put("#21", "Fixed List Messages");
            this.put("fixed-list-none", "&eVous n'avez pas d'effets fixes !");
            this.put("fixed-list-success", "&eVous avez des effets fixes avec ces IDs :&b%ids%");

            this.put("#22", "Fixed Info Messages");
            this.put("fixed-info-invalid", "&cImpossible d'obtenir l'information, vous n'avez pas de particules fixes avec l'ID &b%id%& c!");
            this.put("fixed-info-no-args", "&cVous n'avez pas rentrée d'ID pour obtenir d'infomations !");
            this.put("fixed-info-invalid-args", "&cImpossible d'obtenir l'information, vous n'avez pas de particules fixes avec l'ID, l'ID spécifiée doit être un nombre !");
            this.put("fixed-info-success", "&eID: &b%id% &eWorld: &b%world% &eX: &b%x% &eY: &b%y% &eZ: &b%z% &eEffect: &b%effect% &eStyle: &b%style% &eData: &b%data%");

            this.put("#23", "Fixed Clear Messages");
            this.put("fixed-clear-no-permission", "&cVous n'avez la permission pour supprimer les effets proches !");
            this.put("fixed-clear-no-args", "&cVous n'avez pas rentré de rayon !");
            this.put("fixed-clear-invalid-args", "&cLe rayon rentré n'est pas valide, il doit être un nombre rond !");
            this.put("fixed-clear-success", "&b%amount% &aeffets ont été supprimés dans un rayon &b%range% &cblocs !");

            this.put("#23.5", "Fixed Teleport Message");
            this.put("fixed-teleport-no-permission", "&cVous n'avez pas l'autorisation de vous téléporter dans les effets fixes !");
            this.put("fixed-teleport-no-args", "&cVous n'avez pas spécifié d'identifiant pour la téléportation !");
            this.put("fixed-teleport-invalid-args", "&cImpossible de se téléporter, l'ID spécifié n'est pas valide !");
            this.put("fixed-teleport-success", "&eTéléporté à votre effet fixe avec un ID de &b%id%&e !");

            this.put("#24", "Fixed Other Messages");
            this.put("fixed-no-permission", "&cVous n'avez pas la permission d'ajouter des effets fixes !");
            this.put("fixed-max-reached", "&cVous avez atteint le nombre maximum de particules fixes !");
            this.put("fixed-invalid-command", "&cArguments invalides pour la commande &b/pp fixed&c!");

            this.put("#25", "Plugin Update Message");
            this.put("update-available", "&eLa version (&bv%new%&e) est disponible ! vous utilisez la version &bv%current%&d. https://www.spigotmc.org/resources/playerparticles.40261/");

            this.put("#26", "GUI Messages");
            this.put("gui-disabled", "&cL'interface du plugin à été désactivée !");
            this.put("gui-no-permission", "&cVous n'avez pas la permission d'ouvrir l'interface graphique !");

            this.put("#27", "GUI Color Messages");
            this.put("gui-color-icon-name", "&a");
            this.put("gui-color-info", "&e");
            this.put("gui-color-subtext", "&b");
            this.put("gui-color-unavailable", "&c");

            this.put("#28", "GUI Info Messages");
            this.put("gui-commands-info", "Affichez des informations avec &b/pp help");
            this.put("gui-toggle-visibility-on", "Les particules sont actuellement &avisibles");
            this.put("gui-toggle-visibility-off", "Les particules sont actuellement &ccachées");
            this.put("gui-toggle-visibility-info", "Cliquer pour basculer la visibilité des particules");
            this.put("gui-back-button", "Retour en arrière");
            this.put("gui-next-page-button", "Page Suivante (%start%/%end%)");
            this.put("gui-previous-page-button", "Page Précédente (%start%/%end%)");
            this.put("gui-click-to-load", "Cliquez pour charger : %amount%");
            this.put("gui-shift-click-to-delete", "Touche SHIFT + clic-gauche pour effacer un effet");
            this.put("gui-particle-info", "  - ID: &b%id% &eEffet: &b%effect% &eStyle: &b%style% &eParamètre: &b%data%");
            this.put("gui-playerparticles", "PlayerParticles");
            this.put("gui-active-particles", "Particules actives: &b%amount%");
            this.put("gui-saved-groups", "Groupes sauvegardés: &b%amount%");
            this.put("gui-fixed-effects", "Effets fixes: &b%amount%");

            this.put("#29", "GUI Edit Primary Messages");
            this.put("gui-edit-primary-effect", "Editez l'effet primaire");
            this.put("gui-edit-primary-effect-description", "Editez l effet d une de vos particules primaire");
            this.put("gui-edit-primary-style", "Editez le style primaire");
            this.put("gui-edit-primary-style-missing-effect", "Vous devez d'abord sélectionner un effet !");
            this.put("gui-edit-primary-style-description", "Editez le style d'une particule primaire");
            this.put("gui-edit-primary-data", "Editez les paramètres d'une particules primaire");
            this.put("gui-edit-primary-data-missing-effect", "Vous devez d'abord sélectionner un effet");
            this.put("gui-edit-primary-data-unavailable", "Votre effet primaire n'est pas paramétrable");
            this.put("gui-edit-primary-data-description", "Editer les paramères de votre effet primaire");

            this.put("#30", "GUI Manage Particles Messages");
            this.put("gui-manage-your-particles", "Gérez vos particules");
            this.put("gui-manage-your-particles-description", "Créez, éditez, et supprimez vos particules");
            this.put("gui-manage-your-groups", "Gérez vos groupes");
            this.put("gui-manage-your-groups-description", "Créez, éditez, et supprimez vos groupes particules");

            this.put("#31", "GUI Load Messages");
            this.put("gui-load-a-preset-group", "Chargez un groupe de presets");
            this.put("gui-load-a-preset-group-description", "Chargez un groupe de particules déjà fait");

            this.put("#32", "GUI Save Messages");
            this.put("gui-save-group", "Sauvegardez un nouveau groupe");
            this.put("gui-save-group-description", "Cliquez pour sauvegardez un nouveau groupe. Vous allez être invité\nà écrire un nouveau nom dans le tchat.");
            this.put("gui-save-group-full", "Vous avez atteint le nombre maximal de groupe !");
            this.put("gui-save-group-no-particles", "Vous avez aucunes particules appliquées");
            this.put("gui-save-group-hotbar-message", "&eTapez &b1 &enom dans le tchat pour le nouveau nom du groupe. Tapez &ccancel&e pour annuler. (&b%seconds%&es restants)");
            this.put("gui-save-group-chat-message", "&eUtilisez &b/pp group save <name> &epour enregistrer un nouveau groupe de particules.");

            this.put("#33", "GUI Reset Messages");
            this.put("gui-reset-particles", "Réinitialisez vos particules");
            this.put("gui-reset-particles-description", "Supprimez vos particules actives");

            this.put("#34", "GUI Misc Messages");
            this.put("gui-particle-name", "Particule #%id%");
            this.put("gui-click-to-edit-particle", "Cliquez pour éditer cette particule");
            this.put("gui-editing-particle", "Edition particule #%id%");

            this.put("#35", "GUI Edit Messages");
            this.put("gui-edit-effect", "Editez l'effet");
            this.put("gui-edit-effect-description", "Cliquez pour éditer l'effet de cette particule");
            this.put("gui-edit-style", "Editez le style");
            this.put("gui-edit-style-description", "Cliquez pour éditer le style de cette particule");
            this.put("gui-edit-data", "Editez les paramères");
            this.put("gui-edit-data-description", "Cliquez pour éditer les paramètres de la particule");
            this.put("gui-edit-data-unavailable", "Cliquez pour éditer les paramères de cette particule");
            this.put("gui-data-none", "aucuns");

            this.put("#36", "GUI Create Messages");
            this.put("gui-create-particle", "Créez une nouvelle particule");
            this.put("gui-create-particle-description", "Créez une nouvelle particule");
            this.put("gui-create-particle-unavailable", "Vous avez atteint le nombre maximum de particules que vous pouvez créer");

            this.put("#37", "GUI Select Messages");
            this.put("gui-select-effect", "Sélectionnez les effets");
            this.put("gui-select-effect-description", "Mettez les effets de la particules à &b%effect%");
            this.put("gui-select-style", "Sélectionnez le style");
            this.put("gui-select-style-description", "Mets le style de la particule à &b%style%");
            this.put("gui-select-data", "Sélectionnez les paramètres");
            this.put("gui-select-data-description", "Mets les paramètres de la particule à &b%data%");
            this.put("gui-select-data-note", "Note #%note%");
            this.put("gui-select-data-color-transition-start", "&eSélectionnez la couleur de &bdépart");
            this.put("gui-select-data-color-transition-end", "&eSélectionnez la couleur de &bfin");
            this.put("gui-select-data-vibration", "&b%ticks% &eticks");

            this.put("#38", "GUI Color Name Messages");
            this.put("gui-edit-data-color-red", "#ff0000Rouge");
            this.put("gui-edit-data-color-orange", "#ff8c00Orange");
            this.put("gui-edit-data-color-yellow", "#ffff00Jaune");
            this.put("gui-edit-data-color-lime-green", "#32cd32Vert Citron");
            this.put("gui-edit-data-color-green", "#008000Vert");
            this.put("gui-edit-data-color-blue", "#0000ffBleu");
            this.put("gui-edit-data-color-cyan", "#008b8bCyan");
            this.put("gui-edit-data-color-light-blue", "#add8e6Bleu Clair");
            this.put("gui-edit-data-color-purple", "#8a2be2Mauve");
            this.put("gui-edit-data-color-magenta", "#ca1f7bMagenta");
            this.put("gui-edit-data-color-pink", "#ffb6c1Rose");
            this.put("gui-edit-data-color-brown", "#8b4513Brun");
            this.put("gui-edit-data-color-black", "#000000Noir");
            this.put("gui-edit-data-color-gray", "#808080Gris");
            this.put("gui-edit-data-color-light-gray", "#c0c0c0Gris Clair");
            this.put("gui-edit-data-color-white", "#ffffffBlanc");
        }};
    }
}
