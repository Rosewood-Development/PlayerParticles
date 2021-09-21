package dev.esophose.playerparticles.locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class PolishLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "pl_PL";
    }

    @Override
    public String getTranslatorName() {
        return "THE___BULDI___";
    }

    @Override
    public Map<String, String> getDefaultLocaleStrings() {
        return new LinkedHashMap<String, String>() {{
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "&7[<g:#00aaaa:#0066aa>PlayerParticles&7] ");

            this.put("#1", "Command Description Messages");
            this.put("command-error-missing-effects-or-styles", "&cMusisz mieć dostęp do cząsteczek i wzorów aby użyć tej komendy!");
            this.put("command-error-unknown", "&cNieznana komenda, użyj &b/pp help &caby wyświetlić listę komend.");
            this.put("command-descriptions", "&eDostępne są następujące komendy:");
            this.put("command-descriptions-usage", "&e/pp %cmd% %args%");
            this.put("command-descriptions-help-1", "&7> &b/pp %cmd% &e- %desc%");
            this.put("command-descriptions-help-2", "&7> &b/pp %cmd% %args% &e- %desc%");
            this.put("command-descriptions-help-other", "&7> &b/ppo <gracz> <komenda> &e- Wykonujesz komendę /pp jako podany gracz");
            this.put("command-description-add", "Dodaje nowy efekt");
            this.put("command-description-data", "Sprawdź jakie dane ma dana cząsteczka");
            this.put("command-description-default", "Główna komenda. Standardowo otwiera konsolę efektów");
            this.put("command-description-edit", "Edytuj efekt");
            this.put("command-description-effects", "Wyświetl listę cząsteczek które możesz użyć");
            this.put("command-description-fixed", "Zarządzaj twoimi uziemionymi efektami");
            this.put("command-description-group", "Zarządzaj twoimi grupami");
            this.put("command-description-gui", "Wyświetl konsolę efektów aby łatwiej zarządzać edycją efektów");
            this.put("command-description-help", "Wyświetl opis komend... właśnie na to patrzysz");
            this.put("command-description-info", "Wyświetl opis jednego, konkretnego efektu");
            this.put("command-description-list", "Wyświetl listę twoich aktywnych efektów");
            this.put("command-description-reload", "Odświerza config.yml i dokument tekstowy z tłumaczeniem");
            this.put("command-description-remove", "Usuwa dany efekt");
            this.put("command-description-reset", "Usuwa wszystkie aktywne efekty");
            this.put("command-description-styles", "Wyświetl listę wzorów które możesz użyć");
            this.put("command-description-toggle", "Włącz/Wyłącz widoczność cząsteczek");
            this.put("command-description-use", "Edytuj twój główny efekt");
            this.put("command-description-version", "Wyświetl aktualną wersję pluginu oraz autora");
            this.put("command-description-worlds", "Sprawdź w jakich światach efekty są wyłączone");

            this.put("#2", "Fixed Particle Command Description Messages");
            this.put("command-description-fixed-create", "&e/pp fixed create <<x> <y> <z>|<looking>> <cząsteczka> <wzór> [dane] - Tworzy nowy uziemiony efekt");
            this.put("command-description-fixed-create-console", "&e/pp fixed create <x> <y> <z> <world> <cząsteczka> <wzór> [dane] - Tworzy nowy uziemiony efekt");
            this.put("command-description-fixed-edit", "&e/pp fixed edit <Nr> <cząsteczka|wzór|dane|pozycja> <zmienne> - Edytuj daną część uziemonego efektu bazując na jego numerze");
            this.put("command-description-fixed-remove", "&e/pp fixed remove <Nr> - Usuwa uziemiony efekt o danym numerze");
            this.put("command-description-fixed-list", "&e/pp fixed list - Wyświetla listę z numerami twoich uziemionych efektów");
            this.put("command-description-fixed-info", "&e/pp fixed info <Nr> - Pokazuje informacje o uziemionym efekcie o danym numerze");
            this.put("command-description-fixed-clear", "&e/pp fixed clear <odległość> - Usuwa wszystkie uziemione efekty w podanej odlagłości od ciebie");
            this.put("command-description-fixed-clear-console", "&e/pp fixed clear <odległość> <x> <y> <z> <świat> - Usuwa wszystkie uziemione efekty w podanej odlagłości od ustawionych koordynatów");
            this.put("command-description-fixed-teleport", "&e/pp fixed teleport <Nr> - Teleportuje do uziemionego efektu o podanym numerze");

            this.put("#3", "Group Command Description Messages");
            this.put("command-description-group-save", "&e/pp group save <nazwa> - Zapisz wszystkie aktywne efekty w nowej grupie");
            this.put("command-description-group-load", "&e/pp group load <nazwa> - Załaduj zapisaną grupę efektów");
            this.put("command-description-group-remove", "&e/pp group remove <nazwa> - Usuń zapisaną grupę efektów");
            this.put("command-description-group-list", "&e/pp group list <nazwa> - Wyświetl wszystkie zapisane grupy efektów");
            this.put("command-description-group-info", "&e/pp group info <nazwa> - Wyśiwetla listę efektów zapisanych w danej grupie");

            this.put("#4", "ID Messages");
            this.put("id-invalid", "&cNumer który podałeś jest nie prawidłowy! Musi to być dodatnie liczba całkowita.");
            this.put("id-unknown", "&cNie masz efektu o numerze &b%id%&c!");

            this.put("#5", "Other Messages");
            this.put("other-no-permission", "&cNie masz permisji aby wykonywać te komendy jako inny gracz!");
            this.put("other-missing-args", "&cTwoja komenda jest niekompletna.&b Użyj /ppo <gracz> <komenda>");
            this.put("other-unknown-player", "&cNie znaleziono gracza &b%player%. Ta osoba musi być online.");
            this.put("other-unknown-command", "&cNie ma takiej komendy jak &b/pp %cmd%.");
            this.put("other-success", "&eWykonywanie komendy /pp dla gracza &b%player%&e.");

            this.put("#6", "Add Messages");
            this.put("add-reached-max", "&cNie udało się dodać efektu, osiągnąłeś limit w postaci &b%amount% &cmożliwych efektów!");
            this.put("add-particle-applied", "&aNowy efekt został utworzony z cząsteczką &b%effect%&a, wzorem &b%style%&a, i danymi &b%data%&a!");
            this.put("data-no-args", "&cBrakuje nazwy cząsteczki! Poprawne użycie: &b/pp data <cząsteczka>");

            this.put("#7", "Edit Messages");
            this.put("edit-invalid-property", "&cZostała podana nieprawidłowa wartość &b%prop%. Prawidłowymi wartościami mogą być &bczątecki&c, &bwzory&c oraz &bdane");
            this.put("edit-success-effect", "&aTwój efekt o numerze &b%id% &amiał zmienioną cząsteczkę na &b%effect%&a!");
            this.put("edit-success-style", "&aTwój efekt o numerzef &b%id% &amiał zmieniony wzór na &b%style%&a!");
            this.put("edit-success-data", "&aTwój efekt o numerze &b%id% &amiał zmienione dane na &b%data%&a!");

            this.put("#8", "Group Messages");
            this.put("group-invalid", "&cZapisana grupa efektów o nazwie &b%name%&c nie istnieje!");
            this.put("group-no-permission", "&cNie masz dostępu do którejś cząsteczki bądź wzoru z grupy &b%group%&c!");
            this.put("group-preset-no-permission", "&cNie masz dostępu do którejś cząsteczki bądź wzoru z grupy &b%group%&c!");
            this.put("group-reserved", "&cNie możesz nazwać grupy &bactive &c!");
            this.put("group-no-name", "&cNie podałeś nazwy grupy! &b/pp %cmd% <nazwaGrupy>");
            this.put("group-save-reached-max", "&cNie udało się zapisac grupy. Osiągnąłeś limit grup efektów!");
            this.put("group-save-no-particles", "&cNie udało się zapisac grupy. Nie masz nałożonych żadnych efektów!");
            this.put("group-save-success", "&aTwoje aktualne efekty zostały zapisane w grupie &b%name%&a!");
            this.put("group-save-success-overwrite", "&agrupa efektówd &b%name% &azostała zaaktualizowana o twoje aktualne efekty!");
            this.put("group-load-success", "&aZaładowano &b%amount% &aefektów z grupy efektów &b%name%&a!");
            this.put("group-load-preset-success", "&aZaładowano &b%amount% &aefektów z presetu administracyjnego &b%name%&a!");
            this.put("group-remove-preset", "&cNie możesz usuwać presetów administracyjnych!");
            this.put("group-remove-success", "&aUsunięto grupę efektów &b%name%&a!");
            this.put("group-info-header", "&eGrupa &b%group% &ema następujące efekty:");
            this.put("group-list-none", "&eNie masz zapisanych żadnych grup efektów!");
            this.put("group-list-output", "&eMasz zapisane podane grupy efektów: &b%info%");
            this.put("group-list-presets", "&eMasz dostęp do podanych presetów administracyjnych: &b%info%");

            this.put("#9", "Reload Messages");
            this.put("reload-success", "&aPugin został odświeżony!");
            this.put("reload-no-permission", "&cNie masz dostępu do odświeżania pluginu!");

            this.put("#10", "Remove Messages");
            this.put("remove-no-args", "&cNie podałeś numeru do usunięcia! &b/pp remove <numer>");
            this.put("remove-id-success", "&aEfekt o numerze &b%id% &azostał usunięty!");
            this.put("remove-effect-success", "&aUsunięto &b%amount% &atwoich efektów z cząsteczkę &b%effect%&a!");
            this.put("remove-effect-none", "&cNie masz żadnych efektów z cząsteczką &b%effect%&c!");
            this.put("remove-style-success", "&aUsunięto &b%amount% &atwoich efektów ze wzorem &b%style%&a!");
            this.put("remove-style-none", "&cNie masz żadnych efektów ze wzorem &b%style%&c!");
            this.put("remove-effect-style-none", "&cNie masz żadnych efektów z cząsteczką lub wzorem &b%name%&c!");
            this.put("remove-unknown", "&cCząsteczka bądź wzór o nazwie &b%name% &cnie istnieje!");

            this.put("#11", "List Messages");
            this.put("list-none", "&eNie masz żadnych aktywnych efektów!");
            this.put("list-you-have", "&eMasz nałożone następujące efekty:");
            this.put("list-output", "&eNumer: &b%id% &eCząsteczka: &b%effect% &eWzór: &b%style% &eDane: &b%data%");

            this.put("#12", "Toggle Messages");
            this.put("toggle-on", "&eWidoczność efektów została dla ciebie &aWłączona&e!");
            this.put("toggle-off", "&eWidoczność efektów została dla ciebie &cWyłączona&e!");

            this.put("#13", "Use Messages");
            this.put("use-particle-modified", "&aTwój główny efekt ma od teraz cząsteczkę &b%effect%&a, wzór &b%style%&a, oraz dane &b%data%&a!");

            this.put("#14", "Color Messages");
            this.put("rainbow", "&cR&6a&ei&an&bb&9o&dw");
            this.put("random", "Random");

            this.put("#15", "Effect Messages");
            this.put("effect-no-permission", "&cNie masz dostępu do cząsteczki &b%effect%&c!");
            this.put("effect-invalid", "&cCząteczka &b%effect% &cnie istnieje! Użyj &b/pp effects &caby wyświetlić dostępną listę cząsteczek.");
            this.put("effect-list", "&eMasz dostęp do następujących cząsteczek: &b%effects%");
            this.put("effect-list-empty", "&cNie masz dostępu do żadnych cząsteczek!");

            this.put("#16", "Style Messages");
            this.put("style-no-permission", "&cNie masz dostępu do wzoru &b%style%&c!");
            this.put("style-event-spawning-info", "&eNota: Wzór &b%style% &espawnuje cząsteczki na postawie zachowania gracza.");
            this.put("style-invalid", "&cWzór &b%style% &cnie istnieje! Wpisz &b/pp styles &caby zobaczyć listę dostępnych wzorów.");
            this.put("style-list", "&eMasz dostęp do następujących wzorów: &b%styles%");

            this.put("#17", "Data Messages");
            this.put("data-usage-none", "&eCząsteczka &b%effect% &eNie wymaga żadnych danych!");
            this.put("data-usage-block", "&eCząsteczka &b%effect% &erequires &bblock &edata! &bFormat: <nazwaBloku>");
            this.put("data-usage-item", "&eCząsteczka &b%effect% &erequires &bitem &edata! &bFormat: <nazwaItemu>");
            this.put("data-usage-color", "&eCząsteczka &b%effect% &erequires &bcolor &edata! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-note", "&eCząsteczka &b%effect% &erequires &bnote &edata! &bFormat: <0-24>|<&brainbow>|<random>");
            this.put("data-usage-color-transition", "&eTCząsteczka &b%effect% &erequires &bcolor transition &edata! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-vibration", "&eCząsteczka &b%effect% &erequires &bvibration &edata! &bFormat: <długość>");
            this.put("data-invalid-block", "&cDane &bbloku&c które podałeś są nieprawidłowe! &bFormat: <nazwaBloku>");
            this.put("data-invalid-item", "&cDane &bitemu&c które podałeś są nieprawidłowe! &bFormat: <nazwaItemu>");
            this.put("data-invalid-color", "&cDane &bkoloru&c które podałeś są nieprawidłowe! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-note", "&cDane &bdźwięku&c które podałeś są nieprawidłowe! &bFormat: <0-24>|<&brainbow>|<random>");
            this.put("data-invalid-color-transition", "&cDane &bzmieny koloru&c które podałeś są nieprawidłowe! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-vibration", "&cDane &bwibracji&c które podałeś są nieprawidłowe! &bFormat: <długość>");
            this.put("data-invalid-material-not-item", "&cMateriał &bprzedmiotu &b%material% &cktóry wprowadziłeś, nie jest przedmiotem!");
            this.put("data-invalid-material-not-block", "&cMateriał &bbloku &b%material% &cKtóry wprowadziłeś, nie jest blokiem!");
            this.put("data-invalid-material-item", "&cMateriał &bprzedmiotu &b%material% który wprowadziłeś, nie istnieje!");
            this.put("data-invalid-material-block", "&cMateriał &bbloku &b%material% który wprowadziłeś, nie istnieje!");

            this.put("#18", "World Messages");
            this.put("disabled-worlds", "&eEfekty są wyłączone w podanych światach: &b%worlds%");
            this.put("disabled-worlds-none", "&eEfekty są dostępne w każdym świecie.");

            this.put("#19", "Reset Message");
            this.put("reset-success", "&aUsunięto &b%amount% &aaktywnych efektów!");
            this.put("reset-others-success", "&aUsunięto efekt dla &b%other%&a!");
            this.put("reset-others-none", "&eNie usunięto żadnego efektu dla &b%other%&e.");

            this.put("#20", "Fixed Create Messages");
            this.put("fixed-create-missing-args", "&cNie udało się stworzyć uziemionego efektu, brakuje &b%amount% &cwymaganych argumentów!");
            this.put("fixed-create-invalid-coords", "&cNie udało się stworzyć uziemionego efektu, jeden lub więcej koordynatów jest nieprawidłowych!");
            this.put("fixed-create-out-of-range", "&cNie udało się stworzyć uziemionego efektu, musisz być w odległości &b%range% &ckratek od miejsca docelowego!");
            this.put("fixed-create-looking-too-far", "&cNie udało się stworzyć uziemionego efektu, stoisz za dalego od docelowego bloku!");
            this.put("fixed-create-effect-invalid", "&cNie udało się stworzyć uziemionego efektu, cząsteczka &b%effect% &cnie istnieje!");
            this.put("fixed-create-effect-no-permission", "&cNie udało się stworzyć uziemionego efektu, nie masz dostępu do cząsteczki &b%effect%&c!");
            this.put("fixed-create-style-invalid", "&cNie udało się stworzyć uziemionego efektu, wzór &b%style% &cnie istnieje!");
            this.put("fixed-create-style-no-permission", "&cNie udało się stworzyć uziemionego efektu, nie masz dostępu do wzoru &b%style%&c!");
            this.put("fixed-create-style-non-fixable", "&cNie udało się stworzyć uziemionego efektu, wzór &b%style% &cnie może zostać użyty w uziemionych efektach!");
            this.put("fixed-create-data-error", "&cNie udało się stworzyć uziemionego efektu, podane dane są nieprawidłowe! Użyj &b/pp data <cząsteczka> &caby sprawdzić prawidłowe dane do cząsteczki!");
            this.put("fixed-create-success", "&aTwój uziemiony efekt został stworzony!");

            this.put("#21", "Fixed Edit Messages");
            this.put("fixed-edit-missing-args", "&cNie udało się edytować uziemionego efektu, brakuje którychś argumentów!");
            this.put("fixed-edit-invalid-id", "&cNie udało się edytować uziemionego efektu, numer jest nieprawidłowy bądź go nie ma!");
            this.put("fixed-edit-invalid-property", "&cNie udało się edytować uziemionego efektu, został dodany nieprawidłowy argument! Jedynie &bPozycja&c, &bCząsteczka&c, &bWzór&c, oraz &bDane &csą prawidłowe.");
            this.put("fixed-edit-invalid-coords", "&cNie udało się edytować uziemionego efektu, jeden lub więcej koordynatów które wprowadziłeś są nieprawidłowe!");
            this.put("fixed-edit-out-of-range", "&cNie udało się edytować uziemionego efektu, musisz być w odległości &b%range% &ckratek od miejsca docelowego");
            this.put("fixed-edit-looking-too-far", "&cNie udało się edytować uziemionego efektu, stoisz za dalego od docelowego bloku!");
            this.put("fixed-edit-effect-invalid", "&cNie udało się edytować uziemionego efektu, cząsteczka &b%effect% &cnie istnieje!");
            this.put("fixed-edit-effect-no-permission", "&cNie udało się edytować uziemionego efektu, nie masz dostępu do cząsteczki &b%effect%&c!");
            this.put("fixed-edit-style-invalid", "&cNie udało się edytować uziemionego efektu, wzór &b%style% &cnie istnieje!");
            this.put("fixed-edit-style-no-permission", "&cNie udało się edytować uziemionego efektu, nie masz dostępu do wzoru &b%style%&c!");
            this.put("fixed-edit-style-non-fixable", "&cNie udało się edytować uziemionego efektu, wzór &b%style% &cnie może zostać użyty w uziemionych efektach!");
            this.put("fixed-edit-data-error", "&cNie udało się edytować uziemionego efektu, podane dane są nieprawidłowe! Użyj &b/pp data <cząsteczka> &caby sprawdzić prawidłowe dane do cząsteczki!");
            this.put("fixed-edit-data-none", "&cNie udało się edytować uziemionego efektu, ta cząsteczka nie wymaga żadnych danych!");
            this.put("fixed-edit-success", "&aZaaktualizowano &b%prop% &aw uziemionym efekcie o numerze &b%id%&a!");

            this.put("#22", "Fixed Remove Messages");
            this.put("fixed-remove-invalid", "&cNie udało się usunąć uziemionego efektu, nie masz efektu o numerze &b%id%&c!");
            this.put("fixed-remove-no-args", "&cNie podałeś numeru efektu który usunąć!");
            this.put("fixed-remove-args-invalid", "&cNie udało się usunąć, podany numer efektu musi być liczbą!");
            this.put("fixed-remove-success", "&aTwój uziemiony efekt o numerze &b%id% &azostał usunięty!");

            this.put("#23", "Fixed List Messages");
            this.put("fixed-list-none", "&eNie masz żadnych uziemionych efektów!");
            this.put("fixed-list-success", "&eMasz uziemione efekty o podanych numerach: &b%ids%");

            this.put("#24", "Fixed Info Messages");
            this.put("fixed-info-invalid", "&cNie udało się znaleźć informacji, nie masz uziemionego efektu o numerze &b%id%&c!");
            this.put("fixed-info-no-args", "&cNie podałeś numeru uziemionego efektu, którego informacje chcesz wyświetlić!");
            this.put("fixed-info-invalid-args", "&cNie udało się znaleźć nformacji, podany numer musi być liczbą!");
            this.put("fixed-info-success", "&eNumer: &b%id% &eŚwiat: &b%world% &eX: &b%x% &eY: &b%y% &eZ: &b%z% &eCząsteczka: &b%effect% &eWzór: &b%style% &eDane: &b%data%");

            this.put("#25", "Fixed Clear Messages");
            this.put("fixed-clear-no-permission", "&cNie masz dostępu do usuwania pobliskich, uziemionych efektów!");
            this.put("fixed-clear-no-args", "&cNie podałeś odległości w jakiej mają zostać usunięte uziemione efekty!");
            this.put("fixed-clear-invalid-args", "&cPodana odległość jest nieprawidłowa. Musi to być dodatnie liczba całkowita!");
            this.put("fixed-clear-success", "&aUsunięto &b%amount% &auziemionych efektów w odległości &b%range% &akratek!");

            this.put("#26", "Fixed Teleport Message");
            this.put("fixed-teleport-no-permission", "&cNie masz dostępu do teleportacji do uziemionych efektów!");
            this.put("fixed-teleport-no-args", "&cNie podałeś numeru uziemionego efektu do którego chcesz się teleportować!");
            this.put("fixed-teleport-invalid-args", "&cNie udało się teleportować, Podany numer jest nieprawidłowy!");
            this.put("fixed-teleport-success", "&eTeleportowano do uziemionego efektu o numerze &b%id%&e!");

            this.put("#27", "Fixed Other Messages");
            this.put("fixed-no-permission", "&cNie masz dostępu do korzystania z uziemionych efektów!");
            this.put("fixed-max-reached", "&cOsiągnąłeś maksymalną liczbę uziemionych efektów jakie możesz stworzyć!");
            this.put("fixed-invalid-command", "&cNieprawidłowa pod-komenda &b/pp fixed&c!");

            this.put("#28", "Plugin Update Message");
            this.put("update-available", "&eDostępna jest aktualizacja (&bv%new%&e)! Twoja aktualna wersja: &bv%current%&e. https://www.spigotmc.org/resources/playerparticles.40261/");

            this.put("#29", "GUI Messages");
            this.put("gui-disabled", "&cAdministrator Wyłączył dostęp do konsoli efektów!");
            this.put("gui-no-permission", "&cNie masz dostępu do konsoli efektów!");

            this.put("#30", "GUI Color Messages");
            this.put("gui-color-icon-name", "&a");
            this.put("gui-color-info", "&e");
            this.put("gui-color-subtext", "&b");
            this.put("gui-color-unavailable", "&c");

            this.put("#31", "GUI Info Messages");
            this.put("gui-commands-info", "Komendy do efektów znajdziesz pod &b/pp help");
            this.put("gui-toggle-visibility-on", "Efekty są dla ciebie &aWidoczne");
            this.put("gui-toggle-visibility-off", "Efekty są dla ciebie &cSchowane");
            this.put("gui-toggle-visibility-info", "Kliknij aby &aWłączyć&8/&cWyłączyć &ewidoczność efektów");
            this.put("gui-back-button", "Powrót");
            this.put("gui-next-page-button", "Następna Strona (%start%/%end%)");
            this.put("gui-previous-page-button", "Poprzednia Strona (%start%/%end%)");
            this.put("gui-click-to-load", "Kliknij aby wczytać %amount% cząsteczek:");
            this.put("gui-shift-click-to-delete", "Przytrzymaj shift i kliknij aby usunąć");
            this.put("gui-particle-info", "  - Numer: &b%id% &eCząsteczka: &b%effect% &eWzór: &b%style% &eDane: &b%data%");
            this.put("gui-playerparticles", "Konsola Efektów");
            this.put("gui-active-particles", "Aktywne Efekty: &b%amount%");
            this.put("gui-saved-groups", "Zapisane Grupy: &b%amount%");
            this.put("gui-fixed-effects", "Uziemione Efekty: &b%amount%");

            this.put("#32", "GUI Edit Primary Messages");
            this.put("gui-edit-primary-effect", "Edytuj Główny Efekt");
            this.put("gui-edit-primary-effect-description", "Kliknij aby edytować cząsteczkę twojego głównego efektu");
            this.put("gui-edit-primary-style", "Edytuj Główny Wzór");
            this.put("gui-edit-primary-style-missing-effect", "Musisz najpierw wybrać główną cząsteczkę");
            this.put("gui-edit-primary-style-description", "Kliknij aby edytować wzór twojego głównego efektu");
            this.put("gui-edit-primary-data", "Edytuj Dane Głównego Efektu");
            this.put("gui-edit-primary-data-missing-effect", "Musisz najpierw wybrać główną cząsteczkę");
            this.put("gui-edit-primary-data-unavailable", "Twoja wybrana główna cząsteczka nie korzysta z żadnych danych");
            this.put("gui-edit-primary-data-description", "Kliknij aby edytować dane twojego głównego efektu");

            this.put("#33", "GUI Manage Particles Messages");
            this.put("gui-manage-your-particles", "Zarządzaj Swoimi Efektami");
            this.put("gui-manage-your-particles-description", "Twórz, edytuj oraz usuwaj twoje efekty");
            this.put("gui-manage-your-groups", "Zarządzaj Swoimi Grupami");
            this.put("gui-manage-your-groups-description", "Twórz, edytuj oraz usuwaj twoje grupy efektów");

            this.put("#34", "GUI Load Messages");
            this.put("gui-load-a-preset-group", "Załaduj Preset Administracyjny");
            this.put("gui-load-a-preset-group-description", "Załaduj preset efektów stworzoną przez administrację");

            this.put("#35", "GUI Save Messages");
            this.put("gui-save-group", "Zapisz Nową Grupę");
            this.put("gui-save-group-description", "Kliknij aby zapisać nową grupę efektów. Będziesz poinstruowany\naby wpisać nazwą nowej grupy na chatcie.");
            this.put("gui-save-group-full", "Osiągnąłeś maksymalną liczbę grup jakie możesz stworzyć");
            this.put("gui-save-group-no-particles", "Nie masz nałożonych żadnych efektów");
            this.put("gui-save-group-hotbar-message", "&eWpisz &b1 &esłowo na chatcie jako nazwę grupy. Wpisz &ccancel&e aby anulować. (&ePozostało &b%seconds%&es)");
            this.put("gui-save-group-chat-message", "&eUżyj &b/pp group save <nazwa> &eaby zapisać grupę efektów.");

            this.put("#36", "GUI Reset Messages");
            this.put("gui-reset-particles", "Zresetuj Twoje Efekty");
            this.put("gui-reset-particles-description", "Usuń wszystkie twoje aktywne efekty");

            this.put("#37", "GUI Misc Messages");
            this.put("gui-particle-name", "Efekt #%id%");
            this.put("gui-click-to-edit-particle", "Kliknij aby edytować cząsteczkę, wzór bądź dane efektu");
            this.put("gui-editing-particle", "Edytuję Efekt #%id%");

            this.put("#38", "GUI Edit Messages");
            this.put("gui-edit-effect", "Edytuj Cząsteczkę");
            this.put("gui-edit-effect-description", "Kliknij aby edytować cząsteczkę twojego efektu");
            this.put("gui-edit-style", "Edytuj Wzór");
            this.put("gui-edit-style-description", "Kliknij aby edytować wzór twojego efektu");
            this.put("gui-edit-data", "Edytuj Dane");
            this.put("gui-edit-data-description", "Kliknij aby edytować dane twojego efektu");
            this.put("gui-edit-data-unavailable", "Ten efekt nie wymaga podawania dodatkowych danych");
            this.put("gui-data-none", "brak");

            this.put("#39", "GUI Create Messages");
            this.put("gui-create-particle", "Stwórz Nowy Efekt");
            this.put("gui-create-particle-description", "Tutaj stworzysz nowy efekt z daną cząsteczką, wzorem oraz danymi");
            this.put("gui-create-particle-unavailable", "Osiągnąłeś maksymalną liczbę efektów jakie możesz stworzyć");

            this.put("#40", "GUI Select Messages");
            this.put("gui-select-effect", "Wybierz cząsteczkę efektu");
            this.put("gui-select-effect-description", "Ustawia cząsteczkę efektu na &b%effect%");
            this.put("gui-select-style", "Wybierz wzór efektu");
            this.put("gui-select-style-description", "Ustawia wzór efektu na &b%style%");
            this.put("gui-select-data", "Ustaw dane efektu");
            this.put("gui-select-data-description", "Ustawia dane efektu na &b%data%");
            this.put("gui-select-data-note", "Note #%note%");
            this.put("gui-select-data-color-transition-start", "&eSelect the &bstart &ecolor");
            this.put("gui-select-data-color-transition-end", "&eSelect the &bend &ecolor");
            this.put("gui-select-data-vibration", "&b%ticks% &eliczba ticków");

            this.put("#41", "GUI Color Name Messages");
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
