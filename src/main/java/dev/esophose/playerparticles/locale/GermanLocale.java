package dev.esophose.playerparticles.locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class GermanLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "de_DE";
    }

    @Override
    public String getTranslatorName() {
        return "Drynael";
    }

    @Override
    public Map<String, String> getDefaultLocaleStrings() {
        return new LinkedHashMap<String, String>() {{
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "&7[<g:#00aaaa:#0066aa>PlayerParticles&7] ");

            this.put("#1", "Command Description Messages");
            this.put("command-error-missing-effects-or-styles", "&cSie müssen Zugriff auf Effekte und Stile haben, um diesen Befehl verwenden zu können!");
            this.put("command-error-unknown", "&cUnbekannter Befehl. Verwenden Sie &b/pp help &c, um eine Liste der Befehle anzuzeigen.");
            this.put("command-descriptions", "Die folgenden Befehle stehen zur Auswahl:");
            this.put("command-descriptions-usage", "&e/pp %cmd% %args%");
            this.put("command-descriptions-help-1", "&7> &b/pp %cmd% &e- %desc%");
            this.put("command-descriptions-help-2", "&7> &b/pp %cmd% %args% &e- %desc%");
            this.put("command-descriptions-help-other", "&7> &b/ppo <player> <command> &e- Führe /pp als einen Spieler aus");
            this.put("command-description-add", "Fügen Sie einen neuen Partikel hinzu");
            this.put("command-description-data", "Üprüfen Sie, welche Art von Daten ein Effekt verwendet");
            this.put("command-description-default", "Der Hauptbefehl. Standardmäßig wird die GUI geöffnet");
            this.put("command-description-edit", "Bearbeiten Sie ein Partikel");
            this.put("command-description-effects", "Zeigen Sie eine Liste der Effekte an, die Sie verwenden können");
            this.put("command-description-fixed", "Verwalten Sie Ihre festen Effekte");
            this.put("command-description-group", "Verwalten Sie Ihre Gruppen");
            this.put("command-description-gui", "Zeigen Sie die GUI zum einfachen Bearbeiten von Partikeln an");
            this.put("command-description-help", "Zeigt das Hilfemenü an ... Sie sind angekommen");
            this.put("command-description-info", "Ruft die Beschreibung eines Ihrer aktiven Partikel ab");
            this.put("command-description-list", "Listet die IDs Ihrer aktiven Partikel auf");
            this.put("command-description-reload", "Lädt die Dateien config.yml und Sprache neu");
            this.put("command-description-remove", "Entfernt einen Partikel");
            this.put("command-description-reset", "Entfernt alle aktiven Partikel");
            this.put("command-description-styles", "Zeigen Sie eine Liste der Stile an, die Sie verwenden können");
            this.put("command-description-toggle", "Schaltet die Partikelsichtbarkeit ein / aus");
            this.put("command-description-use", "Modifizieren Sie Ihr Primärteilchen");
            this.put("command-description-version", "Zeigen Sie die Plugin-Version und den Autor an");
            this.put("command-description-worlds", "Finde heraus, in welchen Welten Partikel deaktiviert sind");

            this.put("#2", "Fixed Particle Command Description Messages");
            this.put("command-description-fixed-create", "&e/pp fixed create <<x> <y> <z>|<looking>> <effect> <style> [data] - Erstellt einen neun fixen Effekt");
            this.put("command-description-fixed-create-console", "&e/pp fixed create <x> <y> <z> <world> <effect> <style> [data] - Erstellt einen neun fixen Effekt");
            this.put("command-description-fixed-edit", "&e/pp fixed edit <id> <effect|style|data|location> <args> - Bearbeiten Sie einen Teil eines festen Effekts anhand seiner ID");
            this.put("command-description-fixed-remove", "&e/pp fixed remove <id> - Entfernt einen festen Effekt anhand seiner ID");
            this.put("command-description-fixed-list", "&e/pp fixed list - Listet alle IDs Ihrer festen Effekte auf");
            this.put("command-description-fixed-info", "&e/pp fixed info <id> - Ruft Informationen zu einem Ihrer Fixen Effekte ab");
            this.put("command-description-fixed-clear", "&e/pp fixed clear <radius> - Löscht alle festen Effekte aller Spieler innerhalb des angegebenen Radius");
            this.put("command-description-fixed-clear-console", "&e/pp fixed clear <radius> <x> <y> <z> <world> - Löscht alle festen Effekte aller Spieler innerhalb des angegebenen Radius");
            this.put("command-description-fixed-teleport", "&e/pp fixed teleport <id> - Teleportiert Sie zu einem Ihrer festen Effekte");

            this.put("#2.5", "Group Command Description Messages");
            this.put("command-description-group-save", "&e/pp group save <name> - Speichert alle aktiven Partikel in einer neuen Gruppe");
            this.put("command-description-group-load", "&e/pp group load <name> - Lädt alle in einer Gruppe gespeicherten Partikel");
            this.put("command-description-group-remove", "&e/pp group remove <name> - Entfernt eine gespeicherte Gruppe");
            this.put("command-description-group-list", "&e/pp group list <name> - Listen Sie alle Partikelgruppen auf, die Sie gespeichert haben");
            this.put("command-description-group-info", "&e/pp group info <name> - Listen Sie die in der Gruppe gespeicherten Partikel auf");

            this.put("#3", "ID Messages");
            this.put("id-invalid", "&cDie eingegebene ID ist ungültig, es muss eine positive ganze Zahl sein!");
            this.put("id-unknown", "&cSie haben kein Partikel mit der ID &b%id%&cangelegt!");

            this.put("#4", "Other Messages");
            this.put("other-no-permission", "&cSie haben keine Berechtigung, PlayerParticles-Befehle für andere Spieler auszuführen!");
            this.put("other-missing-args", "&cEs fehlen einige Argumente. &b/ppo <player> <command>");
            this.put("other-unknown-player", "&cDer Spieler &b%player% &cwurde nicht gefunden. Der Spieler mussen online sein.");
            this.put("other-unknown-command", "&cDer Befehl &b/pp %cmd% &cexistiert nicht.");
            this.put("other-success", "&eBefehl /pp command für &b%player%&eausgeführt. Ausgabe:");

            this.put("#5", "Add Messages");
            this.put("add-reached-max", "&cPartikel kann nicht angewendet werden, Sie haben die maximal zulässige Menge von &b%amount% &cerreicht!");
            this.put("add-particle-applied", "&aEs wurde ein neues Partikel mit dem Effekt &b%effect%&a, dem Stil &b%style%&aund den Daten &b%data%&aangewendet!");
            this.put("data-no-args", "&cFehlendes Argument für die Wirkung! Befehlsverwendung: &b/pp data <effect>");

            this.put("#6", "Edit Messages");
            this.put("edit-invalid-property", "&cEine ungültige Eigenschaft &b%prop% &cwurde angegeben. Gültige Eigenschaften: &beffect&c, &bstyle&c, &bdata");
            this.put("edit-success-effect", "&aDer Effekt Ihres Partikels mit der ID &b%id% &awurde in &b%effect%&ageändert!");
            this.put("edit-success-style", "&aDer Stil Ihres Partikels mit der ID &b%id% &awurde in &b%style%&ageändert!");
            this.put("edit-success-data", "&aDie Daten Ihres Partikels mit der ID &b%id% &awurden in &b%data%&ageändert!");

            this.put("#7", "Group Messages");
            this.put("group-invalid", "&cEs existiert keine gespeicherte Gruppe oder Voreinstellungsgruppe mit dem Namen &b%name%&c!");
            this.put("group-no-permission", "&cSie haben keine Berechtigung für einen Effekt oder Stil, um die Gruppe &b%group%&czu verwenden!");
            this.put("group-preset-no-permission", "&cEs fehlt die Berechtigung für einen Effekt oder Stil, um die voreingestellte Gruppe &b%group%&czu verwenden!");
            this.put("group-reserved", "&cDer Gruppenname &bactive &cist reserviert und kann nicht verwendet werden!");
            this.put("group-no-name", "&cSie haben keinen Gruppennamen angegeben! &b/pp %cmd% <groupName>");
            this.put("group-save-reached-max", "&cDie Gruppe kann nicht gespeichert werden, Sie haben die maximale Anzahl von Gruppen erreicht!");
            this.put("group-save-no-particles", "&cGruppe kann nicht gespeichert werden, es wurden keine Partikel angewendet!");
            this.put("group-save-success", "&aIhre aktuellen Partikel wurden unter der Gruppe &b%name%&agespeichert!");
            this.put("group-save-success-overwrite", "&aDie Gruppe mit dem Namen &b%name% &awurde mit Ihren aktuellen Partikeln aktualisiert!");
            this.put("group-load-success", "&aAngewendete &b%amount% &aPartikel aus Ihrer gespeicherten Gruppe mit dem Namen &b%name%&a!");
            this.put("group-load-preset-success", "&aAngewendete &b%amount% &aPartikel aus der voreingestellten Gruppe mit dem Namen &b%name%&a!");
            this.put("group-remove-preset", "&cSie können keine voreingestellte Gruppe entfernen!");
            this.put("group-remove-success", "&aDie Partikelgruppe mit dem Namen &b%name%&awurde entfernt!");
            this.put("group-info-header", "&eDie Gruppe &b%group% &eenthält die folgenden Partikel:");
            this.put("group-list-none", "&eSie haben keine Partikelgruppen gespeichert!");
            this.put("group-list-output", "&eFolgende Gruppen wurden gespeichert: &b%info%");
            this.put("group-list-presets", "&eDie folgenden voreingestellten Gruppen sind verfügbar: &b%info%");

            this.put("#8", "Reload Messages");
            this.put("reload-success", "&aDas Plugin wurde neu geladen!");
            this.put("reload-no-permission", "&cSie haben keine Berechtigung, die Plugin-Einstellungen neu zu laden!");

            this.put("#9", "Remove Messages");
            this.put("remove-no-args", "&cSie haben keine ID zum Entfernen angegeben! &b/pp remove <ID>");
            this.put("remove-id-success", "&aDein Partikel mit der ID &b%id% &awurde entfernt!");
            this.put("remove-effect-success", "&aEntferne &b%amount% &adeiner Partikel mit dem Effekt von &b%effect%&a!");
            this.put("remove-effect-none", "&cSie haben keine Partikel mit dem Effekt &b%effect%&cangelegt!");
            this.put("remove-style-success", "&b%amount% &adeiner Partikel im Stil von &b%style% &aentfernt!");
            this.put("remove-style-none", "&cSie haben keine Partikel mit dem Stil &b%style%&cangelegt!");
            this.put("remove-effect-style-none", "&cSie haben keine Partikel mit der Wirkung oder dem Stil &b%name% &cangewendet!");
            this.put("remove-unknown", "&cEs existiert kein Effekt oder Stil mit dem Namen &b%name%&c!");

            this.put("#10", "List Messages");
            this.put("list-none", "&eSie haben keine aktiven Partikel!");
            this.put("list-you-have", "&eSie haben die folgenden Partikel angewendet:");
            this.put("list-output", "&eID: &b%id% &eEffekt: &b%effect% &eStil: &b%style% &eDaten: &b%data%");

            this.put("#11", "Toggle Messages");
            this.put("toggle-on", "&ePartikel wurden umgeschaltet &aAN&e!");
            this.put("toggle-off", "&ePartikel wurden umgeschaltet &cAUS&e!");

            this.put("#11.5", "Use Messages");
            this.put("use-particle-modified", "&aIhr Primärpartikel wurde modifiziert, um den Effekt &b%effect%&a, style &b%style%&a und data &b%data%&a zu verwenden!");

            this.put("#12", "Color Messages");
            this.put("rainbow", "&cR&6e&eg&ae&bn&9b&do&cg&4e&2n");
            this.put("random", "Zufällig");

            this.put("#13", "Effect Messages");
            this.put("effect-no-permission", "&cSie haben keine Berechtigung, den Effekt &b%effect% &czu verwenden!");
            this.put("effect-invalid", "&cDer Effekt &b%effect% &cexistiert nicht! Verwenden Sie &b/pp effects &c, um eine Liste der Effekte anzuzeigen, die Sie verwenden können.");
            this.put("effect-list", "&eSie können die folgenden Effekte verwenden: &b%effects%");
            this.put("effect-list-empty", "&cSie haben keine Berechtigung, Effekte zu verwenden!");

            this.put("#14", "Style Messages");
            this.put("style-no-permission", "&cSie haben keine Berechtigung, den Stil &b%style% &czu verwenden!");
            this.put("style-event-spawning-info", "&eHinweis: Der Stil &b%style% &eerzeugt Partikel basierend auf einem Ereignis.");
            this.put("style-invalid", "&cDer Stil &b%style% &cexistiert nicht! Verwenden Sie &b/pp styles &cfür eine Liste der Stile, die Sie verwenden können.");
            this.put("style-list", "&eSie können die folgenden Stile verwenden: &b%styles%");

            this.put("#15", "Data Messages");
            this.put("data-usage-none", "&eDer Effekt &b%effect% &everwendet keine Daten!");
            this.put("data-usage-block", "&eFür den Effekt &b%effect% &ewerden &bBlockdaten &ebenötigt! &bFormat: <blockName>");
            this.put("data-usage-item", "&eFür den Effekt &b%effect% &esind &bItem &eDaten erforderlich! &bFormat: <itemName>");
            this.put("data-usage-color", "&eFür den Effekt &b%effect% &esind &bFarbdaten &eerforderlich! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-note", "&eFür den Effekt &b%effect% &esind &bAnmeldedaten &eerforderlich! &bFormat: <0-24>|<&brainbow>|<random>");
            this.put("data-usage-color-transition", "&eFür den Effekt &b%effect% &esind &bFarbübergang &eerforderlich! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-vibration", "&eFür den Effekt &b%effect% &esind &bVibration &eerforderlich! &bFormat: <duration>");
            this.put("data-invalid-block", "&cDie von Ihnen eingegebenen &bBlock &cDaten sind ungültig! &bFormat: <blockName>");
            this.put("data-invalid-item", "&cDie von Ihnen eingegebenen &bItem &cDaten sind ungültig! &bFormat: <itemName>");
            this.put("data-invalid-color", "&cDie von Ihnen eingegebenen &bFarbdaten &csind ungültig! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-note", "&cDie von Ihnen eingegebenen &bNotizdaten &csind ungültig! &bFormat: <0-24>|<&brainbow>|<random>");
            this.put("data-invalid-color-transition", "&cDie von Ihnen eingegebenen &bFarbübergang &csind ungültig! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-vibration", "&cDie von Ihnen eingegebenen &bVibration &csind ungültig! &bFormat: <duration>");
            this.put("data-invalid-material-not-item", "&cDas von Ihnen eingegebene &bItem &cMaterial &b%material% &cist kein Item!");
            this.put("data-invalid-material-not-block", "&cDas von Ihnen eingegebene &bBlock &cMaterial &b%material% &cist kein Block!");
            this.put("data-invalid-material-item", "&cDas von Ihnen eingegebene &bItem &cMaterial &b%material% &cexistiert nicht!");
            this.put("data-invalid-material-block", "&cDas von Ihnen eingegebene &bBlock &cMaterial &b%material% &cexistiert nicht!");

            this.put("#16", "World Messages");
            this.put("disabled-worlds", "&ePartikel sind in diesen Welten deaktiviert: &b%worlds%");
            this.put("disabled-worlds-none", "&ePartikel sind in keiner Welt deaktiviert.");

            this.put("#17", "Reset Message");
            this.put("reset-success", "&b%amount% &aaktive Partikel entfernt!");
            this.put("reset-others-success", "&aEntfertigte Partikel für &b%other%&a!");
            this.put("reset-others-none", "&eFür &b%other%&e wurden keine Partikel entfernt.");

            this.put("#18", "Fixed Create Messages");
            this.put("fixed-create-missing-args", "&cFixer Effekt kann nicht erstellt werden, es fehlen &b%amount% &cerforderliche Argumente!");
            this.put("fixed-create-invalid-coords", "&cEin fester Effekt kann nicht erstellt werden, eine oder mehrere von Ihnen eingegebene Koordinaten sind ungültig!");
            this.put("fixed-create-out-of-range", "&cSie können keinen festen Effekt erstellen. Sie müssen sich innerhalb von &b%range% &cBlöcken von Ihrem gewünschten Standort befinden.");
            this.put("fixed-create-looking-too-far", "&cSie können keinen festen Effekt erzielen, da Sie zu weit von dem Block entfernt sind, den Sie gerade betrachten.");
            this.put("fixed-create-effect-invalid", "&cEs kann kein fester Effekt erstellt werden, ein Effekt mit dem Namen &b%effect% &cist nicht vorhanden!");
            this.put("fixed-create-effect-no-permission", "&cSie können keinen festen Effekt erstellen. Sie haben keine Berechtigung, den Effekt zu verwenden. &b%effect%&c!");
            this.put("fixed-create-style-invalid", "&cEs kann kein fester Effekt erstellt werden. Ein Stil mit dem Namen &b%style% &cist nicht vorhanden.");
            this.put("fixed-create-style-no-permission", "&cEs kann kein fester Effekt erstellt werden. Sie haben keine Berechtigung, den Stil &b%style%&czu verwenden.");
            this.put("fixed-create-style-non-fixable", "&cFeste Effekte können nicht erstellt werden. Der Stil &b%style% &ckann nicht für feste Effekte verwendet werden!");
            this.put("fixed-create-data-error", "&cEs kann kein fester Effekt erstellt werden, die angegebenen Daten sind nicht korrekt! Verwenden Sie &b/pp data <effect> &c, um das richtige Datenformat zu finden!");
            this.put("fixed-create-success", "&aDein fester Effekt wurde erstellt!");

            this.put("#19", "Fixed Edit Messages");
            this.put("fixed-edit-missing-args", "&cFixer Effekt kann nicht bearbeitet werden, es fehlen einige Argumente!");
            this.put("fixed-edit-invalid-id", "&cDer festgelegte Effekt kann nicht bearbeitet werden, die angegebene ID ist ungültig oder existiert nicht!");
            this.put("fixed-edit-invalid-property", "&cFixeffekt kann nicht bearbeitet werden, es wurde eine ungültige Eigenschaft angegeben! Es sind nur &bPositionsdaten&c, &bEffektdaten&c, &bStildaten&cund &bDaten &cgültig.");
            this.put("fixed-edit-invalid-coords", "&cFixer Effekt kann nicht bearbeitet werden, eine oder mehrere von Ihnen eingegebene Koordinaten sind ungültig!");
            this.put("fixed-edit-out-of-range", "&cFixierter Effekt kann nicht bearbeitet werden. Sie müssen sich innerhalb von &b%range% &cBlöcken von Ihrem gewünschten Standort befinden!");
            this.put("fixed-edit-looking-too-far", "&cEin fester Effekt kann nicht bearbeitet werden. Sie befinden sich zu weit entfernt von dem Block, den Sie gerade ansehen.");
            this.put("fixed-edit-effect-invalid", "&cFixer Effekt kann nicht bearbeitet werden, ein Effekt mit dem Namen &b%effect% &cist nicht vorhanden!");
            this.put("fixed-edit-effect-no-permission", "&cFixierter Effekt kann nicht bearbeitet werden. Sie haben keine Berechtigung, den Effekt zu verwenden. &b%effect%&c!");
            this.put("fixed-edit-style-invalid", "&cEin fester Effekt kann nicht bearbeitet werden, ein Stil mit dem Namen &b%style% &cist nicht vorhanden!");
            this.put("fixed-edit-style-no-permission", "&cFixierter Effekt kann nicht bearbeitet werden. Sie haben keine Berechtigung, den Stil &b%style%&czu verwenden.");
            this.put("fixed-edit-style-non-fixable", "&cFixer Effekt kann nicht bearbeitet werden. Der Stil &b%style% &ckann nicht für fixe Effekte verwendet werden!");
            this.put("fixed-edit-data-error", "&cFixeffekt kann nicht bearbeitet werden, die angegebenen Daten sind nicht korrekt! Verwenden Sie &b/pp data <effect> &c, um das richtige Datenformat zu finden!");
            this.put("fixed-edit-data-none", "&cFixierter Effekt kann nicht bearbeitet werden, für den Effekt sind keine Daten erforderlich!");
            this.put("fixed-edit-success", "&aAktualisiert die &b%prop% &ades festen Effekts mit einer ID von &b%id%&a!");

            this.put("#20", "Fixed Remove Messages");
            this.put("fixed-remove-invalid", "&cFixierter Effekt kann nicht entfernt werden, Sie haben keinen Fixierten Effekt mit der ID &b%id%&c!");
            this.put("fixed-remove-no-args", "&cSie haben keine ID zum Entfernen angegeben!");
            this.put("fixed-remove-args-invalid", "&cKann nicht entfernt werden, die angegebene ID muss eine Zahl sein!");
            this.put("fixed-remove-success", "&aDein fester Effekt mit der ID &b%id% &awurde entfernt!");

            this.put("#21", "Fixed List Messages");
            this.put("fixed-list-none", "&eDu hast keine festen Effekte!");
            this.put("fixed-list-success", "&eSie haben feste Effekte mit der ID: &b%ids%");

            this.put("#22", "Fixed Info Messages");
            this.put("fixed-info-invalid", "&cInformationen können nicht abgerufen werden. Sie haben keinen festen Effekt mit der ID &b%id%&c.");
            this.put("fixed-info-no-args", "&cSie haben keine ID angegeben, für die Informationen angezeigt werden sollen!");
            this.put("fixed-info-invalid-args", "&cInformationen können nicht abgerufen werden, die angegebene ID muss eine Zahl sein!");
            this.put("fixed-info-success", "&eID: &b%id% &eWelt: &b%world% &eX: &b%x% &eY: &b%y% &eZ: %z% &b &eEffekt: &b%effect% &eStil: &b%style% &eDaten: &b%data%");

            this.put("#23", "Fixed Clear Messages");
            this.put("fixed-clear-no-permission", "&cSie haben keine Berechtigung, in der Nähe befindliche Fixeffekte zu löschen!");
            this.put("fixed-clear-no-args", "&cSie haben keinen Radius zum Löschen fester Effekte für angegeben!");
            this.put("fixed-clear-invalid-args", "&cDer von Ihnen angegebene Radius ist ungültig. Es muss sich um eine positive ganze Zahl handeln.");
            this.put("fixed-clear-success", "&aEntfernte &b%amount% &aEffekte in &b%range% &aBlöcken von Ihrem Standort entfernt!");

            this.put("#23.5", "Fixed Teleport Message");
            this.put("fixed-teleport-no-permission", "&cSie haben nicht die Erlaubnis, sich zu festen Effekten zu teleportieren!");
            this.put("fixed-teleport-no-args", "&cSie haben keine ID zum Teleportieren angegeben!");
            this.put("fixed-teleport-invalid-args", "&cEs kann nicht teleportiert werden, die angegebene ID ist ungültig!");
            this.put("fixed-teleport-success", "&eTeleportiert zu Ihrem festen Effekt mit einer ID von &b%id%&e!");

            this.put("#24", "Fixed Other Messages");
            this.put("fixed-no-permission", "&cSie haben keine Berechtigung, Fixeffekte zu verwenden!");
            this.put("fixed-max-reached", "&cSie haben die maximal zulässigen festen Effekte erreicht!");
            this.put("fixed-invalid-command", "&cUngültiger Parameter für &b/pp fixed&c!");

            this.put("#25", "Plugin Update Message");
            this.put("update-available", "&eEin Update (&bv%new%&e) ist verfügbar! Aktuelle Version: &bv%current%&e. https://www.spigotmc.org/resources/playerparticles.40261/");

            this.put("#26", "GUI Messages");
            this.put("gui-disabled", "&cDer Serveradministrator hat die GUI deaktiviert!");
            this.put("gui-no-permission", "&cSie haben keine Berechtigung, das GUI zu öffnen!");

            this.put("#27", "GUI Color Messages");
            this.put("gui-color-icon-name", "&a");
            this.put("gui-color-info", "&e");
            this.put("gui-color-subtext", "&b");
            this.put("gui-color-unavailable", "&c");

            this.put("#28", "GUI Info Messages");
            this.put("gui-commands-info", "Informationen zu Befehlen finden Sie mit Hilfe von &b/pp help");
            this.put("gui-toggle-visibility-on", "Partikel sind derzeit &asichtbar");
            this.put("gui-toggle-visibility-off", "Partikel sind derzeit &causgeblendet");
            this.put("gui-toggle-visibility-info", "Klicken Sie, um die Sichtbarkeit der Partikel einzuschalten");
            this.put("gui-back-button", "Zurück");
            this.put("gui-next-page-button", "Nächste Seite (%start%/%end%)");
            this.put("gui-previous-page-button", "Vorherige Seite (%start%/%end%)");
            this.put("gui-click-to-load", "Klicken Sie, um die folgenden %amount% Partikel zu laden:");
            this.put("gui-shift-click-to-delete", "Umschaltklick zum Löschen");
            this.put("gui-particle-info", "- ID: &b%id% &eEffekt: &b%effect% &eStil: &b%style% &eDaten: &b%data%");
            this.put("gui-playerparticles", "PlayerParticles");
            this.put("gui-active-particles", "Aktive Partikel: &b%amount%");
            this.put("gui-saved-groups", "Gespeicherte Gruppen: &b%amount%");
            this.put("gui-fixed-effects", "feste Effekte: &b%amount%");

            this.put("#29", "GUI Edit Primary Messages");
            this.put("gui-edit-primary-effect", "Primäreffekt bearbeiten");
            this.put("gui-edit-primary-effect-description", "Bearbeiten Sie den Effekt Ihres Primärpartikel");
            this.put("gui-edit-primary-style", "Primärer Stil");
            this.put("gui-edit-primary-style-missing-effect", "Sie müssen zuerst einen Effekt auswählen");
            this.put("gui-edit-primary-style-description", "Bearbeiten Sie den Stil Ihres Primärpartikel");
            this.put("gui-edit-primary-data", "Primärdaten");
            this.put("gui-edit-primary-data-missing-effect", "Sie müssen zuerst einen Effekt auswählen");
            this.put("gui-edit-primary-data-unavailable", "Ihr primärer Effekt verwendet keine Daten");
            this.put("gui-edit-primary-data-description", "Bearbeiten Sie die Daten Ihres Primärpartikel");

            this.put("#30", "GUI Manage Particles Messages");
            this.put("gui-manage-your-particles", "Verwalten Sie Ihre Partikel");
            this.put("gui-manage-your-particles-description", "Erstellen, bearbeiten und löschen Sie Ihre Partikel");
            this.put("gui-manage-your-groups", "Verwalten Sie Ihre Gruppen");
            this.put("gui-manage-your-groups-description", "Erstellen, löschen und laden Sie Ihre Partikelgruppen");

            this.put("#31", "GUI Load Messages");
            this.put("gui-load-a-preset-group", "Laden Sie eine vorgefertigte Gruppe");
            this.put("gui-load-a-preset-group-description", "Laden Sie eine vorgefertigte Partikelgruppe");

            this.put("#32", "GUI Save Messages");
            this.put("gui-save-group", "Neue Gruppe");
            this.put("gui-save-group-description", "Klicken Sie hier, um eine neue Gruppe zu speichern. Sie werden aufgefordert, den neuen Gruppennamen im Chat einzugeben.");
            this.put("gui-save-group-full", "Sie haben die maximale Anzahl von Gruppen erreicht");
            this.put("gui-save-group-no-particles", "Sie haben keine Partikel");
            this.put("gui-save-group-hotbar-message", "&eGeben Sie im Chat &b1 &eWort für den neuen Gruppennamen ein. Geben Sie &ccancel&e ein, um den Vorgang abzubrechen. (Noch&b%seconds%&e)");
            this.put("gui-save-group-chat-message", "&eVerwenden Sie /pp group save <name>&e, um eine neue Partikelgruppe zu speichern.");

            this.put("#33", "GUI Reset Messages");
            this.put("gui-reset-particles", "Setzen Sie Ihre Partikel zurück");
            this.put("gui-reset-particles-description", "Löscht alle Ihre aktiven Partikel");

            this.put("#34", "GUI Misc Messages");
            this.put("gui-particle-name", "Partikel");
            this.put("gui-click-to-edit-particle", "Klicken Sie hier, um den Effekt, den Stil oder die Daten dieses Partikels zu bearbeiten");
            this.put("gui-editing-particle", "Partikel #%id% bearbeiten");

            this.put("#35", "GUI Edit Messages");
            this.put("gui-edit-effect", "Effekt bearbeiten");
            this.put("gui-edit-effect-description", "Klicken Sie, um den Effekt dieses Partikels zu bearbeiten");
            this.put("gui-edit-style", "Stil bearbeiten");
            this.put("gui-edit-style-description", "Klicken Sie, um den Stil dieses Partikels zu bearbeiten");
            this.put("gui-edit-data", "Daten bearbeiten");
            this.put("gui-edit-data-description", "Klicken Sie, um die Daten dieses Partikels zu bearbeiten");
            this.put("gui-edit-data-unavailable", "Für die Wirkung dieses Partikels werden keine Daten verwendet");
            this.put("gui-data-none", "kein");

            this.put("#36", "GUI Create Messages");
            this.put("gui-create-particle", "Erstellen Sie einen neuen Partikel");
            this.put("gui-create-particle-description", "Erstellen Sie ein neues Partikel mit einem Effekt, einem Stil und Daten");
            this.put("gui-create-particle-unavailable", "Sie haben die maximale Partikelmenge erreicht, die Sie erstellen können");

            this.put("#37", "GUI Select Messages");
            this.put("gui-select-effect", "Wählen Sie Partikeleffekt");
            this.put("gui-select-effect-description", "Setzt den Partikeleffekt auf &b%effect%");
            this.put("gui-select-style", "Wählen Sie Partikelstil");
            this.put("gui-select-style-description", "Legt den Partikelstil auf &b%style% fest");
            this.put("gui-select-data", "Wählen Sie Partikeldaten");
            this.put("gui-select-data-description", "Setzt die Partikeldaten auf &b%data%");
            this.put("gui-select-data-note", "Hinweis #%note%");
            this.put("gui-select-data-color-transition-start", "&eWählen Sie die &bStartfarbe");
            this.put("gui-select-data-color-transition-end", "&eWählen Sie die &bEndfarbe");
            this.put("gui-select-data-vibration", "&b%ticks% &eticks");

            this.put("#38", "GUI Color Name Messages");
            this.put("gui-edit-data-color-red", "#ff0000Rot");
            this.put("gui-edit-data-color-orange", "#ff8c00Orange");
            this.put("gui-edit-data-color-yellow", "#ffff00Gelb");
            this.put("gui-edit-data-color-lime-green", "#32cd32Limetten-Grün");
            this.put("gui-edit-data-color-green", "#008000Grün");
            this.put("gui-edit-data-color-blue", "#0000ffBlau");
            this.put("gui-edit-data-color-cyan", "#008b8bCyan");
            this.put("gui-edit-data-color-light-blue", "#add8e6Hellblau");
            this.put("gui-edit-data-color-purple", "#8a2be2Lila");
            this.put("gui-edit-data-color-magenta", "#ca1f7bMagenta");
            this.put("gui-edit-data-color-pink", "#ffb6c1Pink");
            this.put("gui-edit-data-color-brown", "#8b4513Braun");
            this.put("gui-edit-data-color-black", "#000000Schwarz");
            this.put("gui-edit-data-color-gray", "#808080Grau");
            this.put("gui-edit-data-color-light-gray", "#c0c0c0Hellgrau");
            this.put("gui-edit-data-color-white", "#ffffffWeiss");
        }};
    }
}
