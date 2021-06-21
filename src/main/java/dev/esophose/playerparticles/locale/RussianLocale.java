package dev.esophose.playerparticles.locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class RussianLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "ru_RU";
    }

    @Override
    public String getTranslatorName() {
        return "Dimatron74";
    }

    @Override
    public Map<String, String> getDefaultLocaleStrings() {
        return new LinkedHashMap<String, String>() {{
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "&7[<g:#00aaaa:#0066aa>PlayerParticles&7] ");

            this.put("#1", "Command Description Messages");
            this.put("command-error-missing-effects-or-styles", "&cВы должны иметь доступ к эффектам и стилям, чтобы использовать эту команду!");
            this.put("command-error-unknown", "&cНеизвестная команда, напишите &b/pp help &c, чтобы узнать команды.");
            this.put("command-descriptions", "&eВам доступны команды ниже:");
            this.put("command-descriptions-usage", "&e/pp %cmd% %args%");
            this.put("command-descriptions-help-1", "&7> &b/pp %cmd% &e- %desc%");
            this.put("command-descriptions-help-2", "&7> &b/pp %cmd% %args% &e- %desc%");
            this.put("command-descriptions-help-other", "&7> &b/ppo <Игрок> <Команда> &e- Посмотреть командны /pp от лица другого игрока.");
            this.put("command-description-add", "Добавить новые частицы.");
            this.put("command-description-data", "Проверить, какой тип данных использует эффект.");
            this.put("command-description-default", "Главная команда. Обычно открывает интерфейс.");
            this.put("command-description-edit", "Изменить частицы.");
            this.put("command-description-effects", "Показать список эффектов, которые Вы можете использовать.");
            this.put("command-description-fixed", "Управление Вашими исправными эффектами.");
            this.put("command-description-group", "Управление Вашими группами.");
            this.put("command-description-gui", "Показать интерфейс для простого изменения частиц.");
            this.put("command-description-help", "Показать список команд... в котором Вы находитесь.");
            this.put("command-description-info", "Показать описание одного из Ваших активных эффектов.");
            this.put("command-description-list", "Списки ID Ваших активных частиц.");
            this.put("command-description-reload", "Перезапустить конфигурацию.");
            this.put("command-description-remove", "Убрать некоторые частицы.");
            this.put("command-description-reset", "Убрать все Ваши активные частицы.");
            this.put("command-description-styles", "Показать список стилей, которые Вы можете использовать.");
            this.put("command-description-toggle", "Сделать частицы видимыми или невидимыми.");
            this.put("command-description-use", "Измените вашу первичную частицу");
            this.put("command-description-version", "Показать версию и создателя плагина.");
            this.put("command-description-worlds", "Узнать, в каком мире Ваши частицы отключены.");

            this.put("#2", "Fixed Particle Command Description Messages");
            this.put("command-description-fixed-create", "&e/pp fixed create <<x> <y> <z>|<looking>> <Эффект> <Стиль> [данные] - Создаёт новый эффект.");
            this.put("command-description-fixed-create-console", "&e/pp fixed create <x> <y> <z> <Мир> <Эффект> <Стиль> [данные] - Создаёт новый эффект.");
            this.put("command-description-fixed-edit", "&e/pp fixed edit <id> <effect|style|data|location> <аргумент> - Изменяет чать эффекта по его ID.");
            this.put("command-description-fixed-remove", "&e/pp fixed remove <id> - Удаляет эффект по его ID.");
            this.put("command-description-fixed-list", "&e/pp fixed list - Показывает список ID всех Ваших эффектов.");
            this.put("command-description-fixed-info", "&e/pp fixed info <id> - Показывает информацию об одном из Ваших эффектов.");
            this.put("command-description-fixed-clear", "&e/pp fixed clear <Радиус> - Удаляет все эффекты игроков, находящихся в заданном радиусе.");
            this.put("command-description-fixed-clear-console", "&e/pp fixed clear <Радиус> <x> <y> <z> <Мир> - Удаляет все эффекты игроков, находящихся в заданном радиусе.");
            this.put("command-description-fixed-teleport", "&e/pp fixed teleport <id> - Телепортирует вас к одному из ваших фиксированных эффектов");

            this.put("#2.5", "Group Command Description Messages");
            this.put("command-description-group-save", "&e/pp group save <Имя> - Сохраняет все активные частицы в новой группе.");
            this.put("command-description-group-load", "&e/pp group load <name> - Загружает все частицы, сохранённые в группе.");
            this.put("command-description-group-remove", "&e/pp group remove <name> - Удаляет сохранённую Вами группу.");
            this.put("command-description-group-list", "&e/pp group list <name> - Список всех групп частиц, которые Вы сохранили.");
            this.put("command-description-group-info", "&e/pp group info <name> - Показывает частицы, сохранённые в группе.");

            this.put("#3", "ID Messages");
            this.put("id-invalid", "&cID, который Вы ввели, недействительный, это должно быть целое число!");
            this.put("id-unknown", "&cВы не имеете частиц с ID &b%id%&c!");

            this.put("#4", "Other Messages");
            this.put("other-no-permission", "&cВы не имеете права, чтобы выполнять команды от других игроков!");
            this.put("other-missing-args", "&cВы не ввели некоторые аргументы. &b/ppo <Игрок> <Команда>");
            this.put("other-unknown-player", "&cИгрок &b%player% &cwas не найден. Скорее всего игрок оффлайн.");
            this.put("other-unknown-command", "&cКоманды &b/pp %cmd% &cне существует.");
            this.put("other-success", "&eВыполнена команда /pp от лица &b%player%&e. Результат:");

            this.put("#5", "Add Messages");
            this.put("add-reached-max", "&cНевозможно применить частицу, Вы использовали &b%amount% &c, максимум допустимых!");
            this.put("add-particle-applied", "&aНовая частица была приложена к эффекту &b%effect%&a, стилю &b%style%&a и данным &b%data%&a!");
            this.put("data-no-args", "&cВы не ввели аргумент для эффекта! Используйте &b/pp data <Эффект>");

            this.put("#6", "Edit Messages");
            this.put("edit-invalid-property", "&cНедействительное свойство &b%prop% &cпредусмотрено. Действительные свойства: &bЭффект&c, &bСтиль&c, &bДанные.");
            this.put("edit-success-effect", "&aЭффект Ваших частиц под ID &b%id% &aбыл изменён на &b%effect%&a!");
            this.put("edit-success-style", "&aСтиль Ваших частиц под ID &b%id% &aбыл изменён на &b%style%&a!");
            this.put("edit-success-data", "&aДанные Ваших частиц под ID &b%id% &aбыли изменены на &b%data%&a!");

            this.put("#7", "Group Messages");
            this.put("group-invalid", "&cСохранённая или заданная группа под названием &b%name%&cне существует!");
            this.put("group-no-permission", "&cВы потеряли право использовать эффект или стиль в группе &b%group%&c!");
            this.put("group-preset-no-permission", "&cВы потеряли право использовать эффект или стиль в заданной группе &b%group%&c!");
            this.put("group-reserved", "&cНазвание группы &bактивно &cи не может быть использованно!");
            this.put("group-no-name", "&cВы не ввели название группы! &b/pp %cmd% <названиеГруппы>");
            this.put("group-save-reached-max", "&cНевозможно сохранить группу, Вы превысили максимальное количество групп!");
            this.put("group-save-no-particles", "&cНевозможно сохранить группу, у Вас нет никаких приложенных частиц!");
            this.put("group-save-success", "&aВаши эффекты были сохранены в группе под названием &b%name%&a!");
            this.put("group-save-success-overwrite", "&aГруппа &b%name% &aбыла обновлена с Вашими частицами!");
            this.put("group-load-success", "&aИз группы &b%name%&a прикреплено частиц - &b%amount%!");
            this.put("group-load-preset-success", "&aИз группы &b%name%&a прикреплено частиц - &b%amount%");
            this.put("group-remove-preset", "&cВы не можете удалить заданную группу!");
            this.put("group-remove-success", "&aУдалена группа под названием &b%name%&a!");
            this.put("group-info-header", "&eГруппа &b%group% &eимеет следующие частицы:");
            this.put("group-list-none", "&eУ Вас нет никаких сохранённых групп с частицами!");
            this.put("group-list-output", "&eУ Вас есть следующие сохранённые группы: &b%info%");
            this.put("group-list-presets", "&eДоступны следующие заданные группы: &b%info%");

            this.put("#8", "Reload Messages");
            this.put("reload-success", "&aКонфигурация перезагружена!");
            this.put("reload-no-permission", "&cВы не имеете права, чтобы перезагружать параметры плагина!");

            this.put("#9", "Remove Messages");
            this.put("remove-no-args", "&cВы не ввели ID для удаления! &b/pp remove <ID>");
            this.put("remove-id-success", "&aВаши частицы под ID &b%id% &aбыли успешно удалены!");
            this.put("remove-effect-success", "&aКоличество удалённых частиц - &b%amount% &a, эффектов - &b%effect%&a!");
            this.put("remove-effect-none", "&cУ Вас нет каких-либо частиц с эффектом &b%effect%&c!");
            this.put("remove-style-success", "&aКоличество удалённых частиц - &b%amount% &a, стилей - &b%style%&a!");
            this.put("remove-style-none", "&cУ Вас нет каких-либо частиц со стилем &b%style%&c!");
            this.put("remove-effect-style-none", "&cУ вас нет никаких частиц с эффектом или стилем &b%name%&c!");
            this.put("remove-unknown", "&cЭффект или стиль под названием &b%name% &cне существует!");

            this.put("#10", "List Messages");
            this.put("list-none", "&eУ вас нет каких-либо активных частиц!");
            this.put("list-you-have", "&eУ Вас есть следующие частицы:");
            this.put("list-output", "&eID: &b%id% &eЭффект: &b%effect% &eСтиль: &b%style% &eДанные: &b%data%");

            this.put("#11", "Toggle Messages");
            this.put("toggle-on", "&eЧастицы были &aВКЛЮЧЕНЫ&e!");
            this.put("toggle-off", "&eЧастицы были &cВЫКЛЮЧЕНЫ&e!");

            this.put("#11.5", "Use Messages");
            this.put("use-particle-modified", "&aВаша первичная частица была изменена, чтобы использовать эффект &b%effect%&a, стиль &b%style%&a, и данные &b%data%&a!");

            this.put("#12", "Color Messages");
            this.put("rainbow", "&cР&6а&eд&aу&bг&9а&d!");
            this.put("random", "Random");

            this.put("#13", "Effect Messages");
            this.put("effect-no-permission", "&cУ Вас нет прав использовать эффект &b%effect% &c!");
            this.put("effect-invalid", "&cЭффект &b%effect% &cне существует! Введите &b/pp effects, &cчтобы узнать доступные Вам эффекты.");
            this.put("effect-list", "&eВы можете использовать следующие эффекты: &b%effects%");
            this.put("effect-list-empty", "&cУ Вас нет права использовать какие-либо эффекты!");

            this.put("#14", "Style Messages");
            this.put("style-no-permission", "&cУ Вас нет прав использовать стиль &b%style% &c!");
            this.put("style-event-spawning-info", "&eЗаписка: стиль &b%style% &eспавнит частицы во время Ивента.");
            this.put("style-invalid", "&cСтиль &b%style% &cне существует! Введите &b/pp styles &cчтобы узнать доступные Вам стили.");
            this.put("style-list", "&eВы можете использовать следующие стили: &b%styles%");

            this.put("#15", "Data Messages");
            this.put("data-usage-none", "&eЭффект &b%effect% &eне использует какие-либо данные!");
            this.put("data-usage-block", "&eЭффект &b%effect% &eзапрашивает &bблок &eданных! &bФормат: <названиеБлока>");
            this.put("data-usage-item", "&eЭффект &b%effect% &eзапрашивает &bпредмет &eданных! &bФормат: <названиеПредмета>");
            this.put("data-usage-color", "&eЭффект &b%effect% &eзапрашивает &bцвет &eданных! &bФормат: <0-255> <0-255> <0-255>|<&brainbow>|<random>");
            this.put("data-usage-note", "&eЭффект &b%effect% &eзапрашивает &bноту &eданных! &bФормат: <0-24>|<&brainbow>|<random>");
            this.put("data-usage-color-transition", "&eЭффект &b%effect% &eзапрашивает &bпереход цвета &eданных! &bФормат: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-vibration", "&eЭффект &b%effect% &eзапрашивает &bвибрация &eданных! &bФормат: <продолжительность>");
            this.put("data-invalid-block", "&bБлок &cданных, который Вы ввели, недействителен! &bФормат: <названиеБлока>");
            this.put("data-invalid-item", "&bПредмет &cданных, который Вы ввели, недействителен! &bФормат: <названиеПредмета>");
            this.put("data-invalid-color", "&bЦвет &cданных, который Вы ввели, недействителен! &bФормат: <0-255> <0-255> <0-255>|<&brainbow>|<random>");
            this.put("data-invalid-note", "&bНота &cданных, которую Вы ввели, недействительна! &bФормат: <0-24>|<&brainbow>|<random>");
            this.put("data-invalid-color-transition", "&bпереход цвета &cданных, которую Вы ввели, недействительна! &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-vibration", "&bвибрация &cданных, которую Вы ввели, недействительна! &bFormat: <duration>");
            this.put("data-invalid-material-not-item", "&bМатериал &cпредмета&b%material%&c, который Вы ввели, не является предметом!");
            this.put("data-invalid-material-not-block",  "&bМатериал &cблока&b%material%&c, который Вы ввели, не является блоком!");
            this.put("data-invalid-material-item", "&bМатериал &cпредмета&b%material%, который Вы ввели, не существует!");
            this.put("data-invalid-material-block", "&bМатериал &cблока&b%material%, который Вы ввели, не существует!");

            this.put("#16", "World Messages");
            this.put("disabled-worlds", "&b%worlds% &eне поддерживает данные частицы.");
            this.put("disabled-worlds-none", "&eЧастицы не поддерживаются ни в каком мире.");

            this.put("#17", "Reset Message");
            this.put("reset-success", "&aУдалено &aактивных частиц - &b%amount%!");
            this.put("reset-others-success", "&aУдаленные частицы для &b%other%&a!");
            this.put("reset-others-none", "&eНикакие частицы не были удалены для &b%other% &e.");

            this.put("#18", "Fixed Create Messages");
            this.put("fixed-create-missing-args", "&cНевозможно создать эффект, не введено запрашиваемых аргументов - &b%amount%!");
            this.put("fixed-create-invalid-coords", "&cНевозможно создать эффект, одни или несколько координат, которые Вы ввели, неверны!");
            this.put("fixed-create-out-of-range", "&cНевозможно создать эффект, Вы должны быть в &b%range% &cблоках от желаемой локации!");
            this.put("fixed-create-looking-too-far", "&cНевозможно создать эффект, Вы стоите слишком далеко от блока, на который смотрите!");
            this.put("fixed-create-effect-invalid", "&cНевозможно создать эффект, эффект под названием &b%effect% &cне существует!");
            this.put("fixed-create-effect-no-permission", "&cНевозможно создать эффект, у Вас нет права использовать эффект &b%effect%&c!");
            this.put("fixed-create-style-invalid", "&cНевозможно создать эффект, стиль под названием &b%style% &cне существует!");
            this.put("fixed-create-style-no-permission", "&cНевозможно создать эффект, у Вас нет права использовать стиль &b%style%&c!");
            this.put("fixed-create-style-non-fixable", "&cНевозможно создать эффект, стиль &b%style% &cне может быть использован!");
            this.put("fixed-create-data-error", "&cНевозможно создать эффект, введённые данные неверны! Введите &b/pp data <Эффект>&c, чтобы найти правильный формат данных!");
            this.put("fixed-create-success", "&aВаш эффект был создан!");

            this.put("#19", "Fixed Edit Messages");
            this.put("fixed-edit-missing-args", "&cНевозможно изменить эффект, Вы не ввели некоторые аргументы!");
            this.put("fixed-edit-invalid-id", "&cНевозможно изменить эффект, введённый ID недействителен или не существует!");
            this.put("fixed-edit-invalid-property", "&cНевозможно изменить эффект, указано недействительное свойство! Только &bлокация&c, &bэффект&c, &bстиль&c, и &bданные &cдействительны.");
            this.put("fixed-edit-invalid-coords", "&cНевозможно изменить эффект, одни или несколько координат, которые Вы ввели, недействительны!");
            this.put("fixed-edit-out-of-range", "&cНевозможно изменить эффект, Вы должны быть в &b%range% &cблоках от желаемой локации!");
            this.put("fixed-edit-looking-too-far", "&cНевозможно изменить эффект, Вы стоите слишком далеко от блока, на который смотрите!");
            this.put("fixed-edit-effect-invalid", "&cНевозможно изменить эффект, эффект под названием &b%effect% &cне существует!");
            this.put("fixed-edit-effect-no-permission", "&cНевозможно создать эффект, у Вас нет права использовать эффект &b%effect%&c!");
            this.put("fixed-edit-style-invalid", "&cНевозможно изменить эффект, стиль под названием &b%style% &cне существует!");
            this.put("fixed-edit-style-no-permission", "&cНевозможно изменить эффект, у Вас нет права использовать стиль &b%style%&c!");
            this.put("fixed-edit-style-non-fixable", "&cНевозможно изменить эффект, стиль &b%style% &cне может быть использован!");
            this.put("fixed-edit-data-error", "&cНевозможно создать эффект, введённые данные неверны! Введите &b/pp data <Эффект>&c, чтобы найти правильный формат данных!");
            this.put("fixed-edit-data-none", "&cНевозможно изменить эффект, эффект не запрашивает какие-либо данные!");
            this.put("fixed-edit-success", "&aОбновлено эффектов под ID &b%id%&a - &b%prop%&a!");

            this.put("#20", "Fixed Remove Messages");
            this.put("fixed-remove-invalid", "&cНевозможно удалить эффект, у Вас нет эффекта под ID &b%id%&c!");
            this.put("fixed-remove-no-args", "&cYou did not specify an ID to remove!");
            this.put("fixed-remove-args-invalid", "&cНевозможно удалить, введённый ID должен состоять из чисел!");
            this.put("fixed-remove-success", "&aВаш эффект под ID &b%id% &aбыл удалён!");

            this.put("#21", "Fixed List Messages");
            this.put("fixed-list-none", "&eУ Вас нет каких-либо эффектов!");
            this.put("fixed-list-success", "&eУ Вас есть эффекты с этими ID: &b%ids%");

            this.put("#22", "Fixed Info Messages");
            this.put("fixed-info-invalid", "&cНевозможно получить информацию, у Вас нет эффекта под ID &b%id%&c!");
            this.put("fixed-info-no-args", "&cВы не ввели ID, по которому нужно узнать информацию!");
            this.put("fixed-info-invalid-args", "&cНевозможно получить информацию, введённый ID должен состоять из чисел!");
            this.put("fixed-info-success", "&eID: &b%id% &eМир: &b%world% &eX: &b%x% &eY: &b%y% &eZ: &b%z% &eЭффект: &b%effect% &eСтиль: &b%style% &eДанные: &b%data%");

            this.put("#23", "Fixed Clear Messages");
            this.put("fixed-clear-no-permission", "&cУ Вас нет права, чтобы убрать ближайшие эффекты!");
            this.put("fixed-clear-no-args", "&cВы не ввели радиус, на котором необходимо убрать эффекты!");
            this.put("fixed-clear-invalid-args", "&cВведённый радиус недействителен, это должно быть целое число!");
            this.put("fixed-clear-success", "&aУбрано эффектов  - &b%amount%&a!");

            this.put("#23.5", "Fixed Teleport Message");
            this.put("fixed-teleport-no-permission", "&cВы не имеете права телепортироваться на фиксированные эффекты!");
            this.put("fixed-teleport-no-args", "&cВы не указали ID для телепортации!");
            this.put("fixed-teleport-invalid-args", "&cНевозможно телепортироваться, указанный идентификатор недействителен!");
            this.put("fixed-teleport-success", "&eТелепортироваться на ваш фиксированный эффект с ID &b%id%&e!");

            this.put("#24", "Fixed Other Messages");
            this.put("fixed-no-permission", "&cУ Вас нет права, чтобы использовать этот эффект!");
            this.put("fixed-max-reached", "&cВы достигли максимального количества эффектов!");
            this.put("fixed-invalid-command", "&cНедействительная команда для &b/pp fixed&c!");

            this.put("#25", "Plugin Update Message");
            this.put("update-available", "&eОбновление (&bv%new%&e) доступно! Ваша версия - &bv%current%&e. https://www.spigotmc.org/resources/playerparticles.40261/");

            this.put("#26", "GUI Messages");
            this.put("gui-disabled", "&cАдминистратор отключил интерфейс!");
            this.put("gui-no-permission", "&cУ вас нет разрешения на открытие графического интерфейса!");

            this.put("#27", "GUI Color Messages");
            this.put("gui-color-icon-name", "&a");
            this.put("gui-color-info", "&e");
            this.put("gui-color-subtext", "&b");
            this.put("gui-color-unavailable", "&c");

            this.put("#28", "GUI Info Messages");
            this.put("gui-commands-info", "Узнать подробнее о командах - &b/pp help");
            this.put("gui-toggle-visibility-on", "Частицы в настоящее время &aвидны");
            this.put("gui-toggle-visibility-off", "В настоящее время частицы &cскрыты");
            this.put("gui-toggle-visibility-info", "Нажмите, чтобы переключить видимость частиц");
            this.put("gui-back-button", "Назад");
            this.put("gui-next-page-button", "Следующая страница (%start%/%end%)");
            this.put("gui-previous-page-button", "Предыдущая страница (%start%/%end%)");
            this.put("gui-click-to-load", "Доступны %amount% частицы для загрузки:");
            this.put("gui-shift-click-to-delete", "Shift+ЛКМ для удаления");
            this.put("gui-particle-info", " - ID: &b%id% &eЭффект: &b%effect% &eСтиль: &b%style% &eДанные: &b%data%");
            this.put("gui-playerparticles", "Выбор частиц");
            this.put("gui-active-particles", "Активные частицы - &b%amount%");
            this.put("gui-saved-groups", "Сохранённые группы - &b%amount%");
            this.put("gui-fixed-effects", "Эффекты - &b%amount%");

            this.put("#29", "GUI Edit Primary Messages");
            this.put("gui-edit-primary-effect", "Изменить эффект");
            this.put("gui-edit-primary-effect-description", "Изменение эффекта Ваших частиц");
            this.put("gui-edit-primary-style", "Изменить стиль");
            this.put("gui-edit-primary-style-missing-effect", "Для начала выберите эффект");
            this.put("gui-edit-primary-style-description", "Изменение стиля Ваших частиц");
            this.put("gui-edit-primary-data", "Изменить данные");
            this.put("gui-edit-primary-data-missing-effect", "Для начала выберите эффект");
            this.put("gui-edit-primary-data-unavailable", "Ваш эффект не использует какие-либо данные");
            this.put("gui-edit-primary-data-description", "Изменение данных Ваших частиц");

            this.put("#30", "GUI Manage Particles Messages");
            this.put("gui-manage-your-particles", "Создать свой эффект");
            this.put("gui-manage-your-particles-description", "Создание, изменение и удаление Ваших частиц");
            this.put("gui-manage-your-groups", "Управление группами");
            this.put("gui-manage-your-groups-description", "Создание, изменение и удаление Ваших групп");

            this.put("#31", "GUI Load Messages");
            this.put("gui-load-a-preset-group", "Установить группу");
            this.put("gui-load-a-preset-group-description", "Установка готовой группы частиц");

            this.put("#32", "GUI Save Messages");
            this.put("gui-save-group", "Сохранить новую группу");
            this.put("gui-save-group-description", "Нажмите, чтобы сохранить группу. Вам нужно будет ввести новое название группы в чат.");
            this.put("gui-save-group-full", "Вы достигли максимального числа групп!");
            this.put("gui-save-group-no-particles", "У Вас нет каких-либо активных частиц!");
            this.put("gui-save-group-hotbar-message", "&eВведите &b1 &eслово в чат, которое будет названием группы. Введите &cотмена&e для отмены.");
            this.put("gui-save-group-chat-message", "&eИспользуйте &b/pp group save <имя> &eдля сохранения новой группы частиц.");

            this.put("#33", "GUI Reset Messages");
            this.put("gui-reset-particles", "Удалить ваши частицы");
            this.put("gui-reset-particles-description", "Убирает все Ваши активные частицы");

            this.put("#34", "GUI Misc Messages");
            this.put("gui-particle-name", "Частицы #%id%");
            this.put("gui-click-to-edit-particle", "Нажмите, чтобы изменить эффект, стиль или данные частиц");
            this.put("gui-editing-particle", "Изменение частиц #%id%");

            this.put("#35", "GUI Edit Messages");
            this.put("gui-edit-effect", "Изменить эффект");
            this.put("gui-edit-effect-description", "Нажмите, чтобы изменить эффект частиц");
            this.put("gui-edit-style", "Изменить стиль");
            this.put("gui-edit-style-description", "Нажмите, чтобы изменить стиль частиц");
            this.put("gui-edit-data", "Изменить данные");
            this.put("gui-edit-data-description", "Нажмите, чтобы изменить данные частиц");
            this.put("gui-edit-data-unavailable", "Эффект этих частиц не использует каких-либо данных");
            this.put("gui-data-none", "пусто");

            this.put("#36", "GUI Create Messages");
            this.put("gui-create-particle", "Создать новые частицы");
            this.put("gui-create-particle-description", "Создать новые частицы с эффектом, стилем и данными");
            this.put("gui-create-particle-unavailable", "Вы достигли максимального числа частиц, которые можно создать");

            this.put("#37", "GUI Select Messages");
            this.put("gui-select-effect", "Выбор эффекта частиц");
            this.put("gui-select-effect-description", "Выбрать эффект для частиц &b%effect%");
            this.put("gui-select-style", "Выбор стиля частиц");
            this.put("gui-select-style-description", "Выбрать стиль для частиц &b%style%");
            this.put("gui-select-data", "Выбор данных частиц");
            this.put("gui-select-data-description", "Выбрать данные для частиц &b%data%");
            this.put("gui-select-data-note", "записка #%note%");
            this.put("gui-select-data-color-transition-start", "&eВыберите &bначальный &eцвет");
            this.put("gui-select-data-color-transition-end", "&eВыберите &bконечный &eцвет");
            this.put("gui-select-data-vibration", "&b%ticks% &eticks");

            this.put("#38", "GUI Color Name Messages");
            this.put("gui-edit-data-color-red", "#ff0000красный");
            this.put("gui-edit-data-color-orange", "#ff8c00оранжевый");
            this.put("gui-edit-data-color-yellow", "#ffff00желтый");
            this.put("gui-edit-data-color-lime-green", "#32cd32зеленый лайм");
            this.put("gui-edit-data-color-green", "#008000зеленый");
            this.put("gui-edit-data-color-blue", "#0000ffсиний");
            this.put("gui-edit-data-color-cyan", "#008b8bциан");
            this.put("gui-edit-data-color-light-blue", "#add8e6светло-синий");
            this.put("gui-edit-data-color-purple", "#8a2be2фиолетовый");
            this.put("gui-edit-data-color-magenta", "#ca1f7bфуксин");
            this.put("gui-edit-data-color-pink", "#ffb6c1розовый");
            this.put("gui-edit-data-color-brown", "#8b4513коричневый");
            this.put("gui-edit-data-color-black", "#000000черный");
            this.put("gui-edit-data-color-gray", "#808080серый");
            this.put("gui-edit-data-color-light-gray", "#c0c0c0светло-серый");
            this.put("gui-edit-data-color-white", "#ffffffбелый");
        }};
    }
}
