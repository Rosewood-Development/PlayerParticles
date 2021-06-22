package dev.esophose.playerparticles.locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class SpanishLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "es_ES";
    }

    @Override
    public String getTranslatorName() {
        return "Polinus7";
    }

    @Override
    public Map<String, String> getDefaultLocaleStrings() {
        return new LinkedHashMap<String, String>() {{
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "&7[<g:#00aaaa:#0066aa>PlayerParticles&7] ");

            this.put("#1", "Command Description Messages");
            this.put("command-error-missing-effects-or-styles", "&c¡Debes tener acceso a efectos y estilos para usar este comando!");
            this.put("command-error-unknown", "&cComando desconocido, usa &b/pp help &cpara obtener una lista con los comandos.");
            this.put("command-descriptions", "&eLos siguientes comandos están disponibles:");
            this.put("command-descriptions-usage", "&e/pp %cmd% %args%");
            this.put("command-descriptions-help-1", "&7> &b/pp %cmd% &e- %desc%");
            this.put("command-descriptions-help-2", "&7> &b/pp %cmd% %args% &e- %desc%");
            this.put("command-descriptions-help-other", "&7> &b/ppo <jugador> <comando> &e- Ejecuta un comando /pp como un jugador");
            this.put("command-description-add", "Añade una nueva partícula");
            this.put("command-description-data", "Compruebe qué tipo de datos utiliza un efecto");
            this.put("command-description-default", "El comando principal. De forma predeterminada, abre la GUI");
            this.put("command-description-edit", "Editar una partícula");
            this.put("command-description-effects", "Muestre una lista de efectos que puede usar");
            this.put("command-description-fixed", "Gestiona tus efectos fijos");
            this.put("command-description-group", "Gestiona tus grupos");
            this.put("command-description-gui", "Muestra la GUI para editar fácilmente las partículas");
            this.put("command-description-help", "Muestra el menú de ayuda ... Has llegado");
            this.put("command-description-info", "Obtiene la descripción de una de sus partículas activas");
            this.put("command-description-list", "Enumera los ID de sus partículas activas");
            this.put("command-description-reload", "Vuelve a cargar el archivo config.yml y lang");
            this.put("command-description-remove", "Elimina algunas partículas");
            this.put("command-description-reset", "Elimina todas tus partículas activas");
            this.put("command-description-styles", "Muestre una lista de estilos que puede usar");
            this.put("command-description-toggle", "Activa o desactiva la visibilidad de las partículas");
            this.put("command-description-use", "Modifica tu partícula primaria");
            this.put("command-description-version", "Muestra la versión y el autor del complemento");
            this.put("command-description-worlds", "Descubra en qué mundos están deshabilitadas las partículas");

            this.put("#2", "Fixed Particle Command Description Messages");
            this.put("command-description-fixed-create", "&e/pp fixed create <<x> <y> <z>|<looking>> <effect> <style> [data] - Crea un nuevo efecto fijo");
            this.put("command-description-fixed-create-console", "&e/pp fixed create <x> <y> <z> <world> <effect> <style> [data] - Crea un nuevo efecto fijo");
            this.put("command-description-fixed-edit", "&e/pp fixed edit <id> <effect|style|data|location> <args> - Editar parte de un efecto fijo por su ID");
            this.put("command-description-fixed-remove", "&e/pp fixed remove <ID> - Elimina un efecto fijo por su ID");
            this.put("command-description-fixed-list", "&e/pp fixed list - Enumera todos los ID de sus efectos fijos");
            this.put("command-description-fixed-info", "&e/pp fixed info <ID> - Obtiene información sobre uno de sus efectos fijos");
            this.put("command-description-fixed-clear", "&e/pp fixed clear <radius> - Borra todos los efectos fijos de todos los jugadores dentro del radio dado");
            this.put("command-description-fixed-clear-console", "&e/pp fixed clear <radius> <x> <y> <z> <world> - Borra todos los efectos fijos de todos los jugadores dentro del radio dado");
            this.put("command-description-fixed-teleport", "&e/pp fixed teleport <id> - Te teletransporta a uno de tus efectos fijos");

            this.put("#2.5", "Group Command Description Messages");
            this.put("command-description-group-save", "&e/pp group save <name> - Guarda todas las partículas activas en un nuevo grupo");
            this.put("command-description-group-load", "&e/pp group load <name> - Carga todas las partículas guardadas en un grupo");
            this.put("command-description-group-remove", "&e/pp group remove <name> - Elimina un grupo que ha guardado");
            this.put("command-description-group-list", "&e/pp group list <name> - Enumere todos los grupos de partículas que ha guardado");
            this.put("command-description-group-info", "&e/pp group info <name> - Enumere las partículas guardadas en el grupo");

            this.put("#3", "ID Messages");
            this.put("id-invalid", "&cEl ID que ingresó no es válido, debe de ser un número entero positivo!");
            this.put("id-unknown", "&cNo tiene una partícula aplicada con el ID &b%id%&c!");

            this.put("#4", "Other Messages");
            this.put("other-no-permission", "&cNo tienes permiso para ejecutar comandos de particulas de otros jugadores!");
            this.put("other-missing-args", "&cTe faltan algunos argumentos. &b/ppo <player> <command>");
            this.put("other-unknown-player", "&cel jugador &b%player% &cno fue encontrado. Deben estar ofline.");
            this.put("other-unknown-command", "&cel comando &b/pp %cmd% &cNo existe.");
            this.put("other-success", "&eEjecuta /pp command for &b%player%&e. Output:");

            this.put("#5", "Add Messages");
            this.put("add-reached-max", "&cNo se puede aplicar la partícula, ha alcanzado la cantidad máxima de &b%amount% &cpermitido!");
            this.put("add-particle-applied", "&aSe ha aplicado una nueva partícula con el efecto &b%effect%&a, estilo &b%style%&a, y datos &b%data%&a!");
            this.put("data-no-args", "&c¡Falta argumento para el efecto! Usa el comando: &b/pp data <effect>");

            this.put("#6", "Edit Messages");
            this.put("edit-invalid-property", "&cUna propiedad inválida &b%prop% &cfue dado. Propiedades válidas: &beffect&c, &bstyle&c, &bdata");
            this.put("edit-success-effect", "&aTu partícula con un ID de &b%id% &aha cambiado sus efectos a &b%effect%&a!");
            this.put("edit-success-style", "&aTu partícula con un ID de &b%id% &aha cambiado su estilo a &b%style%&a!");
            this.put("edit-success-data", "&aTu partícula con un ID de &b%id% &aha cambiado sus datos a &b%data%&a!");

            this.put("#7", "Group Messages");
            this.put("group-invalid", "&cNo existe un grupo guardado o un grupo preestablecido con el nombre &b%name%&c!");
            this.put("group-no-permission", "&cTe falta permiso para que un efecto o estilo use el grupo. &b%group%&c!");
            this.put("group-preset-no-permission", "&cLe falta permiso para que un efecto o estilo use el grupo preestablecido &b%group%&c!");
            this.put("group-reserved", "&cel grupo llamado &bactivado &cEstá reservado y no puede ser utilizado!");
            this.put("group-no-name", "&cNo proporcionaste un nombre de grupo! &b/pp %cmd% <groupName>");
            this.put("group-save-reached-max", "&cNo se puede guardar el grupo, has alcanzado el número máximo de grupos!");
            this.put("group-save-no-particles", "&cNo se puede guardar el grupo, no tiene ninguna partícula aplicada!");
            this.put("group-save-success", "&aSus partículas actuales se han guardado en el grupo llamado &b%name%&a!");
            this.put("group-save-success-overwrite", "&aEl grupo llamado &b%name% &ase ha actualizado con sus partículas actuales!");
            this.put("group-load-success", "&Aplicadas &b%amount% &aparticulas de tu grupo guardado llamado &b%name%&a!");
            this.put("group-load-preset-success", "&aAplicado &b%amount% &aparticulas del grupo preestablecido llamado &b%name%&a!");
            this.put("group-remove-preset", "&cNo puedes eliminar un grupo preestablecido!");
            this.put("group-remove-success", "&aSe eliminó el grupo de partículas llamado &b%name%&a!");
            this.put("group-info-header", "&eEl grupo &b%group% &etiene las siguientes partículas:");
            this.put("group-list-none", "&eNo tienes ningún grupo de partículas guardado!");
            this.put("group-list-output", "&eTienes los siguientes grupos guardados: &b%info%");
            this.put("group-list-presets", "&eLos siguientes grupos preestablecidos están disponibles: &b%info%");

            this.put("#8", "Reload Messages");
            this.put("reload-success", "&aEl complemento se ha recargado!");
            this.put("reload-no-permission", "&cNo tienes permiso para volver a cargar la configuración del complemento.!");

            this.put("#9", "Remove Messages");
            this.put("remove-no-args", "&cNo especificó un ID para eliminar! &b/pp remove <ID>");
            this.put("remove-id-success", "&aTu partícula con la id &b%id% &afueron removidas!");
            this.put("remove-effect-success", "&aRemovidasde tus partículas con el efecto de &b%amount% &aof your particles with the effect of &b%effect%&a!");
            this.put("remove-effect-none", "&cNo tiene ninguna partícula aplicada con el efecto &b%effect%&c!");
            this.put("remove-style-success", "&aRemovidas &b%amount% &ade tus partículas con el estilo de &b%style%&a!");
            this.put("remove-style-none", "&cNo tienes ninguna partícula aplicada con el estilo &b%style%&c!");
            this.put("remove-effect-style-none", "&cNo tiene ninguna partícula aplicada con el efecto o estilo &b%name%&c!");
            this.put("remove-unknown", "&cUn efecto o estilo con el nombre de &b%name% &cNo existe!");

            this.put("#10", "List Messages");
            this.put("list-none", "&eNo tienes partículas activas.!");
            this.put("list-you-have", "&eTiene las siguientes partículas aplicadas:");
            this.put("list-output", "&eID: &b%id% &eEfecto: &b%effect% &eEstilo: &b%style% &eDatos: &b%data%");

            this.put("#11", "Toggle Messages");
            this.put("toggle-on", "&eLas partículas se han cambiado &aON&e!");
            this.put("toggle-off", "&eLas partículas se han cambiado &cOFF&e!");

            this.put("#11.5", "Use Messages");
            this.put("use-particle-modified", "&aTu partícula primaria ha sido modificada para usar el efecto. &b%effect%&a, estilo &b%style%&a, y datos &b%data%&a!");

            this.put("#12", "Color Messages");
            this.put("rainbow", "&cR&6a&ei&an&bb&9o&dw");
            this.put("random", "Aleatorio");

            this.put("#13", "Effect Messages");
            this.put("effect-no-permission", "&cNo tienes permiso para usar el efecto. &b%effect%&c!");
            this.put("effect-invalid", "&cEl efecto &b%effect% &cNo existe! Usa &b/pp effects &cpara ver la lista de efectos.");
            this.put("effect-list", "&ePuedes usar los siguientes efectos: &b%effects%");
            this.put("effect-list-empty", "&cNo tienes permiso para usar ningún efecto.!");

            this.put("#14", "Style Messages");
            this.put("style-no-permission", "&cNo tienes permiso para usar el estilo: &b%style%&c!");
            this.put("style-event-spawning-info", "&eNota: el estilo &b%style% &espawnea particulas basado en un evento.");
            this.put("style-invalid", "&cEl estilo &b%style% &cNo existe! Usa &b/pp styles &cpara ver la lista de comandos de los estilos.");
            this.put("style-list", "&ePuedes usar los siguientes estilos: &b%styles%");

            this.put("#15", "Data Messages");
            this.put("data-usage-none", "&eEl efecto &b%effect% &eno usa ningún dato!");
            this.put("data-usage-block", "&eEl efecto &b%effect% &erequiere &bbloque &edato! &bFormato: <blockName>");
            this.put("data-usage-item", "&eEl efecto &b%effect% &erequiere &bitem &edato! &bFormato: <itemName>");
            this.put("data-usage-color", "&eEl efecto &b%effect% &erequiere &bcolor &edato! &bFormato: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-note", "&eEl efecto &b%effect% &erequiere &bNota &edato! &bFormato: <0-24>|<&brainbow>|<random>");
            this.put("data-usage-color-transition", "&eEl efecto &b%effect% &erequiere los datos de &btransición de color&e! &bFormato: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-vibration", "&eEl efecto &b%effect% &erequiere datos de &bvibración&e! &bFormato: <duración>");
            this.put("data-invalid-block", "&cEl &bblock &clos datos que ingresaste no son válidos! &bFormato: <blockName>");
            this.put("data-invalid-item", "&cEl &bitem &clos datos que ingresaste no son válidos! &bFormato: <itemName>");
            this.put("data-invalid-color", "&cEl &bcolor &clos datos que ingresaste no son válidos! &bFormato: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-note", "&cEl &bNota &clos datos que ingresaste no son válidos! &bFormato: <0-24>|<&brainbow>|<random>");
            this.put("data-invalid-color-transition", "&cLos datos de &btransición de color &cque has introducido son inválidos! &bFormato: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-vibration", "&cLos datos de &bvibración &cque has intruducidos son inválidos! &bFormato: <duración>");
            this.put("data-invalid-material-not-item", "&cel &bitem &cmaterial &b%material% &cque ingresaste no es un item!");
            this.put("data-invalid-material-not-block", "&cel &bbloque &cmaterial &b%material% &cque ingresaste no es un bloque!");
            this.put("data-invalid-material-item", "&cel &bitem &cmaterial &b%material% que ingresaste no existe!");
            this.put("data-invalid-material-block", "&cel &bbloque &cmaterial &b%material%que ingresaste no existe!");

            this.put("#16", "World Messages");
            this.put("disabled-worlds", "&eLas partículas están desactivadas en estos mundos: &b%worlds%");
            this.put("disabled-worlds-none", "&eLas partículas no están deshabilitadas en ningún mundo..");

            this.put("#17", "Reset Message");
            this.put("reset-success", "&aRemovidas &b%amount% &aparticulas activas!");
            this.put("reset-others-success", "&aPartículas eliminadas para &b%other%&a!");
            this.put("reset-others-none", "&eNo se eliminaron partículas a &b%other%&e.");

            this.put("#18", "Fixed Create Messages");
            this.put("fixed-create-missing-args", "&cNo se puede crear un efecto fijo, estás perdido &b%amount% &crequerido argumentos!");
            this.put("fixed-create-invalid-coords", "&cNo se puede crear un efecto fijo,una o más coordenadas que ingresaste no son válidas!");
            this.put("fixed-create-out-of-range", "&cNo se puede crear un efecto fijo, debes estar dentro &b%range% &cbloques de su ubicación!");
            this.put("fixed-create-looking-too-far", "&cNo se puede crear un efecto fijo, ¡Estás parado demasiado lejos del bloque que estás mirando!");
            this.put("fixed-create-effect-invalid", "&cNo se puede crear un efecto fijo, un efecto con el nombre &b%effect% &c¡no existe!");
            this.put("fixed-create-effect-no-permission", "&cNo se puede crear un efecto fijo,no tienes permiso para usar el efecto &b%effect%&c!");
            this.put("fixed-create-style-invalid", "&cNo se puede crear un efecto fijo, un estilo con el nombre &b%style% &c¡no existe!");
            this.put("fixed-create-style-no-permission", "&cNo se puede crear un efecto fijo,no tienes permiso para usar el estilo &b%style%&c!");
            this.put("fixed-create-style-non-fixable", "&cNo se puede crear un efecto fijo, el estilo &b%style% &cno se puede utilizar en efectos fijos!");
            this.put("fixed-create-data-error", "&cNo se puede crear un efecto fijo, ¡Los datos proporcionados no son correctos! usa &b/pp data <effect> &cpara encontrar el formato de datos correcto!");
            this.put("fixed-create-success", "&a¡Tu efecto fijo ha sido creado!");

            this.put("#19", "Fixed Edit Messages");
            this.put("fixed-edit-missing-args", "&cNo se puede editar el efecto fijo,te faltan algunos argumentos!");
            this.put("fixed-edit-invalid-id", "&cNo se puede editar el efecto fijo, el ID especificado no es válido o ¡no existe!");
            this.put("fixed-edit-invalid-property", "&cNo se puede editar el efecto fijo, ¡Se especificó una propiedad no válida! Solo &blocalización&c, &befecto&c, &bestilo&c, y &bdato &cSon válidos.");
            this.put("fixed-edit-invalid-coords", "&cNo se puede editar el efecto fijo, una o más coordenadas que ingresaste no son válidas!");
            this.put("fixed-edit-out-of-range", "&cNo se puede editar el efecto fijo, debes estar dentro &b%range% &cbloques de su ubicación!");
            this.put("fixed-edit-looking-too-far", "&cNo se puede editar el efecto fijo, ¡Estás parado demasiado lejos del bloque que estás mirando!");
            this.put("fixed-edit-effect-invalid", "&cNo se puede editar el efecto fijo, un efecto con el nombre &b%effect% &c¡no existe!");
            this.put("fixed-edit-effect-no-permission", "&cNo se puede editar el efecto fijo, no tienes permiso para usar el efecto &b%effect%&c!");
            this.put("fixed-edit-style-invalid", "&cNo se puede editar el efecto fijo, un estilo con el nombre &b%style% &c¡no existe!");
            this.put("fixed-edit-style-no-permission", "&cNo se puede editar el efecto fijo, no tienes permiso para usar el estilo &b%style%&c!");
            this.put("fixed-edit-style-non-fixable", "&cNo se puede editar el efecto fijo,el estilo &b%style% &cno se puede utilizar en efectos fijos!");
            this.put("fixed-edit-data-error", "&cNo se puede editar el efecto fijo, ¡Los datos proporcionados no son correctos! Usar &b/pp data <effect> &cpara encontrar el formato de datos correcto!");
            this.put("fixed-edit-data-none", "&cNo se puede editar el efecto fijo, el efecto no requiere ningún dato!");
            this.put("fixed-edit-success", "&aActualizado el &b%prop% &adel efecto fijo con un ID de &b%id%&a!");

            this.put("#20", "Fixed Remove Messages");
            this.put("fixed-remove-invalid", "&cNo se puede eliminar el efecto fijo, no tiene un efecto fijo con el ID de &b%id%&c!");
            this.put("fixed-remove-no-args", "&cNo especificó un ID para eliminar!");
            this.put("fixed-remove-args-invalid", "&cNo se puede eliminar, el ID especificado debe ser un número!");
            this.put("fixed-remove-success", "&aTu efecto fijo con el ID &b%id% &aha sido removido!");

            this.put("#21", "Fixed List Messages");
            this.put("fixed-list-none", "&eNo tienes efectos fijos!");
            this.put("fixed-list-success", "&eYou have fixed effects with these IDs: &b%ids%");

            this.put("#22", "Fixed Info Messages");
            this.put("fixed-info-invalid", "&cNo se puede obtener información, no tiene un efecto fijo con el ID de &b%id%&c!");
            this.put("fixed-info-no-args", "&cNo especificó un ID para mostrar información !");
            this.put("fixed-info-invalid-args", "&cNo se puede obtener información, el ID especificado debe ser un número!");
            this.put("fixed-info-success", "&eID: &b%id% &eMundo: &b%world% &eX: &b%x% &eY: &b%y% &eZ: &b%z% &eEffecto: &b%effect% &eEstilo: &b%style% &eDato: &b%data%");

            this.put("#23", "Fixed Clear Messages");
            this.put("fixed-clear-no-permission", "&c¡No tienes permiso para borrar efectos fijos cercanos!");
            this.put("fixed-clear-no-args", "&c¡No proporcionaste un radio para borrar efectos fijos!");
            this.put("fixed-clear-invalid-args", "&cEl radio que proporcionó no es válido, debe ser un número entero y positivo.");
            this.put("fixed-clear-success", "&aDespejado &b%amount% &aeffectos fijos &b%range% &abloques de tu ubicación!");

            this.put("#23.5", "Fixed Teleport Message");
            this.put("fixed-teleport-no-permission", "&c¡No tienes permiso para teletransportarte!");
            this.put("fixed-teleport-no-args", "&c¡No especificó una identificación para teletransportarse!");
            this.put("fixed-teleport-invalid-args", "&cIncapaz de teletransportarse, la ID especificada no es válida!");
            this.put("fixed-teleport-success", "&eTeletransportado a su efecto fijo con un ID de &b%id%&e!");

            this.put("#24", "Fixed Other Messages");
            this.put("fixed-no-permission", "&c¡No tienes permiso para usar efectos fijos!");
            this.put("fixed-max-reached", "&c¡Has alcanzado el máximo de efectos fijos permitidos!");
            this.put("fixed-invalid-command", "&cSubcomando no válido para &b/pp fijo&c!");

            this.put("#25", "Plugin Update Message");
            this.put("update-available", "&e¡Una actualización (&bv%new%&e) está disponible! Estas corriendo &bv%current%&e. https://www.spigotmc.org/resources/playerparticles.40261/");

            this.put("#26", "GUI Messages");
            this.put("gui-disabled", "&cLos administradores del server an Deshabilitado el GUI!");
            this.put("gui-no-permission", "&ctu no tienes permisos para abrir el GUI!");

            this.put("#27", "GUI Color Messages");
            this.put("gui-color-icon-name", "&a");
            this.put("gui-color-info", "&e");
            this.put("gui-color-subtext", "&b");
            this.put("gui-color-unavailable", "&c");

            this.put("#28", "GUI Info Messages");
            this.put("gui-commands-info", "Encuentra información sobre los comandos con &b/pp help");
            this.put("gui-toggle-visibility-on", "Las particulas ahora estan &avisibles");
            this.put("gui-toggle-visibility-off", "Las particulas ahora estan &cocultas");
            this.put("gui-toggle-visibility-info", "Haga clickk para alternar la visibilidad de las partículas");
            this.put("gui-back-button", "volver atras");
            this.put("gui-next-page-button", "siguiente pagina (%start%/%end%)");
            this.put("gui-previous-page-button", "pagina anterior (%start%/%end%)");
            this.put("gui-click-to-load", "Haga clickk para cargar lo siguiente %amount% particle(s):");
            this.put("gui-shift-clickkk-to-delete", "Shift clickkk para suprimir");
            this.put("gui-particle-info", "  - ID: &b%id% &eEffectos: &b%effect% &eEstilos: &b%style% &eDatos: &b%data%");
            this.put("gui-playerparticles", "PlayerParticles");
            this.put("gui-active-particles", "Particulas activadas: &b%amount%");
            this.put("gui-saved-groups", "Grupos Guardados: &b%amount%");
            this.put("gui-fixed-effects", "Efectos fijos: &b%amount%");

            this.put("#29", "GUI Edit Primary Messages");
            this.put("gui-edit-primary-effect", "Editar efectos primarios");
            this.put("gui-edit-primary-effect-description", "Edita el efecto de tu partícula primaria");
            this.put("gui-edit-primary-style", "Editar estilos primarios");
            this.put("gui-edit-primary-style-missing-effect", "Primero debes seleccionar un efecto");
            this.put("gui-edit-primary-style-description", "Edite el estilo de su partícula primaria");
            this.put("gui-edit-primary-data", "Editar datos primarios");
            this.put("gui-edit-primary-data-missing-effect", "Primero debes seleccionar un efecto");
            this.put("gui-edit-primary-data-unavailable", "Su efecto principal no usa ningún dato");
            this.put("gui-edit-primary-data-description", "Edite los datos de su partícula primaria");

            this.put("#30", "GUI Manage Particles Messages");
            this.put("gui-manage-your-particles", "Administra tus particulas");
            this.put("gui-manage-your-particles-description", "Crea, edita y elimina tus partículas");
            this.put("gui-manage-your-groups", "Administra tus grupos");
            this.put("gui-manage-your-groups-description", "Cree, elimine y cargue sus grupos de partículas");

            this.put("#31", "GUI Load Messages");
            this.put("gui-load-a-preset-group", "Cargar un grupo preestablecido");
            this.put("gui-load-a-preset-group-description", "Cargar un grupo de partículas prefabricado");

            this.put("#32", "GUI Save Messages");
            this.put("gui-save-group", "Guardar Nuevo Grupo");
            this.put("gui-save-group-description", "Haga clickk para guardar un nuevo grupo. Se le preguntara\npara ingresar el nuevo nombre del grupo en el chat.");
            this.put("gui-save-group-full", "Has alcanzado el número máximo de grupos");
            this.put("gui-save-group-no-particles", "No tiene ninguna partícula aplicada");
            this.put("gui-save-group-hotbar-message", "&eTipo &b1 &epalabra en el chat para el nuevo nombre del grupo.  &ccancelar&e para cancelar. (&b%seconds%&es restantes)");
            this.put("gui-save-group-chat-message", "&eUse &b/pp group save <name> &eto save a new particle group.");

            this.put("#33", "GUI Reset Messages");
            this.put("gui-reset-particles", "Resetear tus particulas");
            this.put("gui-reset-particles-description", "Elimina todas sus partículas activas");

            this.put("#34", "GUI Misc Messages");
            this.put("gui-particle-name", "Particula #%id%");
            this.put("gui-clickkk-to-edit-particle", "Haga clickk para editar el efecto, el estilo o los datos de esta partícula");
            this.put("gui-editing-particle", "Editando Particula #%id%");

            this.put("#35", "GUI Edit Messages");
            this.put("gui-edit-effect", "Editar Effecto");
            this.put("gui-edit-effect-description", "Haga clickk para editar el effecto de esta partícula");
            this.put("gui-edit-style", "Editar estilo");
            this.put("gui-edit-style-description", "Haga clickk para editar el estilo de esta partícula");
            this.put("gui-edit-data", "Editar Datos");
            this.put("gui-edit-data-description", "Haga clickk para editar los datos de esta partícula");
            this.put("gui-edit-data-unavailable", "El efecto de esta partícula no utiliza ningún dato.");
            this.put("gui-data-none", "ninguno");

            this.put("#36", "GUI Create Messages");
            this.put("gui-create-particle", "Crear nuevas particulas");
            this.put("gui-create-particle-description", "Crea una nueva partícula con efecto, estilo y datos.");
            this.put("gui-create-particle-unavailable", "Ha alcanzado la cantidad máxima de partículas que puede crear.");

            this.put("#37", "GUI Select Messages");
            this.put("gui-select-effect", "Seleccionar efecto de partículas");
            this.put("gui-select-effect-description", "Establece el efecto de partículas &b%effect%");
            this.put("gui-select-style", "Seleccionar estilo de partícula");
            this.put("gui-select-style-description", "Establece el estilo de partícula &b%style%");
            this.put("gui-select-data", "Seleccionar datos de partículas");
            this.put("gui-select-data-description", "Establece los datos de partículas &b%data%");
            this.put("gui-select-data-note", "note #%note%");
            this.put("gui-select-data-color-transition-start", "&eSelecciona el color del &binicio");
            this.put("gui-select-data-color-transition-end", "&eSelecciona el color del &bfinal");
            this.put("gui-select-data-vibration", "&b%ticks% &eticks");

            this.put("#38", "GUI Color Name Messages");
            this.put("gui-edit-data-color-red", "#ff0000Rojo");
            this.put("gui-edit-data-color-orange", "#ff8c00Naranja");
            this.put("gui-edit-data-color-yellow", "#ffff00Amarillo");
            this.put("gui-edit-data-color-lime-green", "#32cd32Verde Lima");
            this.put("gui-edit-data-color-green", "#008000Verde");
            this.put("gui-edit-data-color-blue", "#0000ffAzul");
            this.put("gui-edit-data-color-cyan", "#008b8bCian");
            this.put("gui-edit-data-color-light-blue", "#add8e6Azul Claro");
            this.put("gui-edit-data-color-purple", "#8a2be2Morado");
            this.put("gui-edit-data-color-magenta", "#ca1f7bMagenta");
            this.put("gui-edit-data-color-pink", "#ffb6c1Rosa");
            this.put("gui-edit-data-color-brown", "#8b4513Marron");
            this.put("gui-edit-data-color-black", "#000000Negro");
            this.put("gui-edit-data-color-gray", "#808080Gris");
            this.put("gui-edit-data-color-light-gray", "#c0c0c0Gris Claro");
            this.put("gui-edit-data-color-white", "#ffffffBlanco");
        }};
    }

}
