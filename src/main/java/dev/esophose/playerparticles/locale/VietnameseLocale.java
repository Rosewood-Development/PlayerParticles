package dev.esophose.playerparticles.locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class VietnameseLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "vi_VN";
    }

    @Override
    public String getTranslatorName() {
        return "DUYSONGLOI";
    }

    @Override
    public Map<String, String> getDefaultLocaleStrings() {
        return new LinkedHashMap<String, String>() {{
            this.put("#0", "Plugin Message Prefix");
            this.put("prefix", "&7[<g:#00aaaa:#0066aa>PlayerParticles&7] ");

            this.put("#1", "Command Description Messages");
            this.put("command-error-missing-effects-or-styles", "&cBạn phải có quyền truy cập vào các hiệu ứng và phong cách để sử dụng lệnh này!");
            this.put("command-error-unknown", "&cLệnh không đúng, &6/pp help &cđể xem danh sách các câu lệnh.");
            this.put("command-descriptions", "&eCác lệnh có sẵn:");
            this.put("command-descriptions-usage", "&e/pp %cmd% %args%");
            this.put("command-descriptions-help-1", "&7> &b/pp %cmd% &e- %desc%");
            this.put("command-descriptions-help-2", "&7> &b/pp %cmd% %args% &e- %desc%");
            this.put("command-descriptions-help-other", "&7> &b/ppo <player> <command> &e- Chạy lệnh /pp bằng player");
            this.put("command-description-add", "Thêm một hiệu ứng mới");
            this.put("command-description-data", "Kiểm tra laoị dữ liệu mà hiệu ứng sử dụng");
            this.put("command-description-default", "Đã mở GUI hiệu ứng");
            this.put("command-description-edit", "Chỉnh sửa một hiệu ứng");
            this.put("command-description-effects", "Hiển thị danh sách hiệu ứng mà bạn có thể sử dụng");
            this.put("command-description-fixed", "Quản lí hiệu ứng cố định của bạn");
            this.put("command-description-group", "Quản lí Group của bạn");
            this.put("command-description-gui", "Hiển thị GUI để dễ dàng chỉnh sửa hiệu ứng hơn");
            this.put("command-description-help", "Đang hiển thị Help Menu...");
            this.put("command-description-info", "Nhận mô tả về hiệu ứng đang hoạt động của bạn");
            this.put("command-description-list", "Danh sách IDs các hiệu ứng đang hoạt động của bạn");
            this.put("command-description-reload", "Tải lại config và lang file");
            this.put("command-description-remove", "Xóa một vài hiệu ứng");
            this.put("command-description-reset", "Xóa tất cả hiệu ứng đang hoạt động của bạn");
            this.put("command-description-styles", "Hiển thị danh sách Style bạn có thể sử dụng");
            this.put("command-description-toggle", "Bật/Tắt tàng hình hiệu ứng");
            this.put("command-description-use", "Sửa đổi hạt chính của bạn");
            this.put("command-description-version", "Hiển thị phiên bản và tác giả của plugins");
            this.put("command-description-worlds", "Hiển thị danh sách thế giới bị cấm hiệu ứng");

            this.put("#2", "Fixed Particle Command Description Messages");
            this.put("command-description-fixed-create", "&e/pp fixed create <<x> <y> <z>|<looking>> <effect> <style> [data] - Tạo mới một hiệu ứng cố định");
            this.put("command-description-fixed-create-console", "&e/pp fixed create <x> <y> <z> <world> <effect> <style> [data] - Tạo mới một hiệu ứng cố định");
            this.put("command-description-fixed-edit", "&e/pp fixed edit <id> <effect|style|data|location> <args> - Chỉnh sửa hiệu ứng cố định bằng IDs của nó");
            this.put("command-description-fixed-remove", "&e/pp fixed remove <id> - Xóa hiệu ứng cố định");
            this.put("command-description-fixed-list", "&e/pp fixed list - Danh sách các hiệu ứng cố định dưới dạng IDs");
            this.put("command-description-fixed-info", "&e/pp fixed info <id> - Hiển thị thông tin về hiệu ứng cố định");
            this.put("command-description-fixed-clear", "&e/pp fixed clear <radius> - Xóa toàn bộ hiệu ứng trong khu vực bán kính");
            this.put("command-description-fixed-clear-console", "&e/pp fixed clear <radius> <x> <y> <z> <world> - Xóa toàn bộ hiệu ứng trong khu vực bán kính");
            this.put("command-description-fixed-teleport", "&e/pp fixed teleport <id> - Dịch chuyển bạn đến một trong những hiệu ứng cố định của bạn");

            this.put("#2.5", "Group Command Description Messages");
            this.put("command-description-group-save", "&e/pp group save <name> - Lưu toàn bộ hiêu ứng hoạt động vào Group mới");
            this.put("command-description-group-load", "&e/pp group load <name> - Load toàn bộ hiệu ứng hoạt động trong group");
            this.put("command-description-group-remove", "&e/pp group remove <name> - Xóa một group bạn đã lưu");
            this.put("command-description-group-list", "&e/pp group list <name> - Hiển thị danh sách Group hiệu ứng bạn đã lưu");
            this.put("command-description-group-info", "&e/pp group info <name> - Hiển thị danh sách hiệu ứng bạn đang lưu trong group");

            this.put("#3", "ID Messages");
            this.put("id-invalid", "&cID bạn nhập không tồn tại, nó phải là một số nguyên dương!");
            this.put("id-unknown", "&cBạn không có hiệu ứng nào được áp dụng với ID &b%id%&c!");

            this.put("#4", "Other Messages");
            this.put("other-no-permission", "&cbạn không có quyền để thi hành lệnh của plugins cho người chơi khác!");
            this.put("other-missing-args", "&cBạn đã quên một vài đối số. &b/ppo <player> <command>");
            this.put("other-unknown-player", "&cKhông thể tìm thấy người chơi &b%player% &c. Người chơi được chỉ định phải Online.");
            this.put("other-unknown-command", "&cLệnh &b/pp %cmd% &ckhông tồn tại.");
            this.put("other-success", "&eThi hành lệnh /pp command cho &b%player%&e. Output:");

            this.put("#5", "Add Messages");
            this.put("add-reached-max", "&Không thể áp dụng hiệu ứng, Bạn đã đạt tới số lượng giới hạn của&b%amount% &cđược cho phép!");
            this.put("add-particle-applied", "&aHạt (Particle) mới đã được áp dụng với effect &b%effect%&a, style &b%style%&a, và data &b%data%&a!");
            this.put("data-no-args", "&cĐã quên một đối số cho effect! Lệnh đúng: &b/pp data <effect>");

            this.put("#6", "Edit Messages");
            this.put("edit-invalid-property", "&cMột property không hợp lệ &b%prop% &cwđã được cung cấp. properties hợp lệ: &beffect&c, &bstyle&c, &bdata");
            this.put("edit-success-effect", "&aHạt hiệu ứng của bạn với ID &b%id% &ađã thanh đổi Hiệu ứng thành &b%effect%&a!");
            this.put("edit-success-style", "&aHạt hiệu ứng của bạn với ID &b%id% &ađã thanh đổi Kiểu hiệu ứng thành &b%style%&a!");
            this.put("edit-success-data", "&aHạt hiệu ứng của bạn với ID &b%id% &ađã thanh đổi Dữ liệu thành &b%data%&a!");

            this.put("#7", "Group Messages");
            this.put("group-invalid", "&cMột Group đã lưu hoặc Preset Group không được tìm thấy với tên &b%name%&c!");
            this.put("group-no-permission", "&cBạn không có quyền for 1 effect, style để sử dụng nhóm sẵn &b%group%&c!");
            this.put("group-preset-no-permission", "&cBạn không có quyền for 1 effect, style để sử dụng nhóm cài sẵn &b%group%&c!");
            this.put("group-reserved", "&cGroup tên &bactive &ckhông thể được sử dụng!");
            this.put("group-no-name", "&cBạn đã không cung cấp tên group! &b/pp %cmd% <groupName>");
            this.put("group-save-reached-max", "&cKhông thể lưu Group , bạn đã đạt giới hạn số lượng group có thể tạo!");
            this.put("group-save-no-particles", "&cKhông thể lưu group, bạn không có bất kì *hạt được được áp dụng nào!");
            this.put("group-save-success", "&aHạt hiệu ứng của bạn đã được lưu dưới tên của group &b%name%&a!");
            this.put("group-save-success-overwrite", "&aGroup tên&b%name% &ađã được cập nhật với hạt hiệu ứng hiện tại của bạn!");
            this.put("group-load-success", "&aĐã áp dụng &b%amount% &ahạt hiệu ứng từ group được lưu với tên &b%name%&a!");
            this.put("group-load-preset-success", "&aĐã áp dụng &b%amount% &ahạt hiệu ứng tự presen group với tên &b%name%&a!");
            this.put("group-remove-preset", "&cBạn không thể xóa một preset group!");
            this.put("group-remove-success", "&aĐã xóa một Nhóm hạt hiệu ứng với tên &b%name%&a!");
            this.put("group-info-header", "&eGroup &b%group% &eđang có những hạt hiệu ứng sau:");
            this.put("group-list-none", "&eBạn không có bất kì nhóm hạt hiệuứng nào được lưu!");
            this.put("group-list-output", "&eBạn đã lưu các Group sau: &b%info%");
            this.put("group-list-presets", "&eCác preset group có sẵn: &b%info%");

            this.put("#8", "Reload Messages");
            this.put("reload-success", "&aĐã tải lại plugins!");
            this.put("reload-no-permission", "&cBạn không có quyền để tải lại plugins!");

            this.put("#9", "Remove Messages");
            this.put("remove-no-args", "&cBạn không chỉ định IDs để xóa! &b/pp remove <ID>");
            this.put("remove-id-success", "&aHạt hiệu ứng của bạ với IDs &b%id% &ađã bị xóa!");
            this.put("remove-effect-success", "&aĐã xóa &b%amount% &ahạt hiệu ứng của bạn với hiệu ứng &b%effect%&a!");
            this.put("remove-effect-none", "&cBạn không có bất kì Hạt hiệu ứng nào để áp dụng hiệu ứng &b%effect%&c!");
            this.put("remove-style-success", "&aĐã xóa &b%amount% &aHạt hiệu ứng của bạn với style &b%style%&a!");
            this.put("remove-style-none", "&cBạn không có bất kì Hạt hiệu ứng nào để áp dụng style &b%style%&c!");
            this.put("remove-effect-style-none", "&cBạn không có bất kỳ hạt nào được áp dụng với hiệu ứng hoặc kiểu %name%&c!");
            this.put("remove-unknown", "&cEffect hoặc Style với tên &b%name% &ckhông tồn tại!");

            this.put("#10", "List Messages");
            this.put("list-none", "&eBạn không có bất kì Hạt hiệu ứng hoạt động nào!");
            this.put("list-you-have", "&eCác hạt hiệu ứng bạn đã áp dụng:");
            this.put("list-output", "&eID: &b%id% &eEffect: &b%effect% &eStyle: &b%style% &eData: &b%data%");

            this.put("#11", "Toggle Messages");
            this.put("toggle-on", "&eHạt hiệu ứng : &aON&e!");
            this.put("toggle-off", "&eHạt hiệu ứng : &cOFF&e!");

            this.put("#11.5", "Use Messages");
            this.put("use-particle-modified", "&aHạt chính của bạn đã được sửa đổi để sử dụng hiệu ứng &b%effect%&a, style &b%style%&a, và data &b%data%&a!");

            this.put("#12", "Color Messages");
            this.put("rainbow", "&cR&6a&ei&an&bb&9o&dw");
            this.put("random", "Ngẫu nhiên");

            this.put("#13", "Effect Messages");
            this.put("effect-no-permission", "&cBạn không có quyền để sử dụng Hiệu ứng &b%effect% &c!");
            this.put("effect-invalid", "&cHiệu ứng &b%effect% &ckhông tồn tại! Sử dụng &b/pp effects &cđể hiển thị danh sách hiệu ứng bạn có thể dùng.");
            this.put("effect-list", "&eBạn có thể sử dụng các hiệu ứng sau: &b%effects%");
            this.put("effect-list-empty", "&cBạn không có quyền để sử dụng bất kì hiệu ứng nào!");

            this.put("#14", "Style Messages");
            this.put("style-no-permission", "&cbạn không có quyền để sử dụng Kiểu hiệu ứng &b%style% &c!");
            this.put("style-event-spawning-info", "&eNhắc nhở: Kiểu hiệu ứng &b%style% &esinh ra dựa trên sự kiện nhất định được xảy ra.");
            this.put("style-invalid", "&cKiểu hiệu ứng &b%style% &ckhông tồn tại! Use &b/pp styles &cđể hiển thị danh sách Kiểu hiệu ứng bạn có thể dùng.");
            this.put("style-list", "&eBạn có thể sử dụng các Kiểu hiệu ứng sau: &b%styles%");

            this.put("#15", "Data Messages");
            this.put("data-usage-none", "&eHiệu ứng &b%effect% &ekhông sử dụng bất kì Dữ liệu nào!");
            this.put("data-usage-block", "&eHiệu ứng &b%effect% &eyêu cầu dữ liệu &bblock ! &bĐịnh dạng: <blockName>");
            this.put("data-usage-item", "&eHiệu ứng &b%effect% &eyêu cầu dữ liệu &bitem! &bĐịnh dạng: <itemName>");
            this.put("data-usage-color", "&eHiệu ứng &b%effect% &eyêu cầu dữ liệu &bcolor! &bĐịnh dạng: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-note", "&eHiệu ứng &b%effect% &eyêu cầu dữ liệu &bnote! &bĐịnh dạng: <0-24>|<&brainbow>|<random>");
            this.put("data-usage-color-transition", "&eHiệu ứng &b%effect% &eyêu cầu dữ liệu &bcolor transition! &bĐịnh dạng: &bFormat: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-usage-vibration", "&eHiệu ứng &b%effect% &eyêu cầu dữ liệu &bvibration! &bĐịnh dạng: <duration>");
            this.put("data-invalid-block", "&cDữ liệu &bblock &cbạn đã nhập không hợp lệ! &bĐịnh dạng: <blockName>");
            this.put("data-invalid-item", "&cDữ liệu &bitem &cbạn đã nhập không hợp lệ! &bĐịnh dạng: <itemName>");
            this.put("data-invalid-color", "&cDữ liệu &bcolor &cbạn đã nhập không hợp lệ! &bĐịnh dạng: <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-note", "&cDữ liệu &bnote &cbạn đã nhập không hợp lệ! &bĐịnh dạng: <0-24>|<&brainbow>|<random>");
            this.put("data-invalid-color-transition", "&cDữ liệu &bcolor transition &cbạn đã nhập không hợp lệ! &bĐịnh dạng: <<0-255> <0-255> <0-255>>|<&brainbow>|<random> <<0-255> <0-255> <0-255>>|<&brainbow>|<random>");
            this.put("data-invalid-vibration", "&cDữ liệu &bvibration &cbạn đã nhập không hợp lệ! &bĐịnh dạng: <duration>");
            this.put("data-invalid-material-not-item", "&cDữ liệu &bitem &c: Vật phẩm &b%material% &cbạn đã nhập không phải là một vật phẩm!");
            this.put("data-invalid-material-not-block", "&cDữ liệu &bblock &c: Vật phẩm &b%material% &cbạn đã nhập không phải là một khối!");
            this.put("data-invalid-material-item", "&cDữ liệu &bitem &c: Vật phẩm &b%material% bạn đã nhập không tồn tại!");
            this.put("data-invalid-material-block", "&cDữ liệu &bblock &c: Vật phẩm &b%material% bạn đã nhập không tồn tại!");

            this.put("#16", "World Messages");
            this.put("disabled-worlds", "&eCác Hạt hiệu ứng bị cấm tại thế giới này: &b%worlds%");
            this.put("disabled-worlds-none", "&eKhông có thể giới nào cấm Hạt hiệu ứng.");

            this.put("#17", "Reset Message");
            this.put("reset-success", "&aĐã xóa &b%amount% &aHạt hiệu ứng hoạt động!");
            this.put("reset-others-success", "&aCác hạt bị loại bỏ cho &b%other%&a!");
            this.put("reset-others-none", "&eKhông có hạt nào được loại bỏ cho &b%other%&e.");

            this.put("#18", "Fixed Create Messages");
            this.put("fixed-create-missing-args", "&cKhông thể tạo Hiệu ứng cố định, bạn đã quên &b%amount% &cđối số yêu cầu!");
            this.put("fixed-create-invalid-coords", "&cKhông thể tạo Hiệu ứng cố định, một hoặc nhiều tọa độ bạn đã nhập không hợp lệ!");
            this.put("fixed-create-out-of-range", "&cKhông thể tạo Hiệu ứng cố định, bạn phải ở trong bán kính &b%range% &ckhối tại vị trí mong muốn của bạn!");
            this.put("fixed-create-looking-too-far", "&cKhông thể tạo Hiệu ứng cố định, bạn đang đứng quá xa khối bạn nhìn!");
            this.put("fixed-create-effect-invalid", "&cKhông thể tạo Hiệu ứng cố định, Hiệu ứng với tên &b%effect% &ckhông tồn tại!");
            this.put("fixed-create-effect-no-permission", "&cKhông thể tạo Hiệu ứng cố định, bạn không có quyền để sử dụng Hiệu ứng &b%effect%&c!");
            this.put("fixed-create-style-invalid", "&cKhông thể tạo Hiệu ứng cố định, Kiểu hiệu ứng với tên &b%style% &ckhông tồn tại!");
            this.put("fixed-create-style-no-permission", "&cKhông thể tạo Hiệu ứng cố định, bạn không có quyền để sử dụng Kiểu hiệu ứng &b%style%&c!");
            this.put("fixed-create-style-non-fixable", "&cKhông thể tạo Hiệu ứng cố định, Kiểu hiệu ứng &b%style% &ckhông thể được sử dụng trong Hiệu ứng cố định!");
            this.put("fixed-create-data-error", "&cKhông thể tạo Hiệu ứng cố định, Dữ liệu bạn đã cung cấp không đúng! Sử dụng &b/pp data <effect> &cđể xem định dạng dữ liệu đúng!");
            this.put("fixed-create-success", "&aHiệu ứng cố định của bạn đã được tạo!");

            this.put("#19", "Fixed Edit Messages");
            this.put("fixed-edit-missing-args", "&cKhông thể điều chỉnh Hiệu ứng cố định, bạn chưa nhập một vài đối số!");
            this.put("fixed-edit-invalid-id", "&cKhông thể điều chỉnh Hiệu ứng cố định, IDs bạn chỉ định không hợp lệ hoặc không tồn tại!");
            this.put("fixed-edit-invalid-property", "&cKhông thể điều chỉnh Hiệu ứng cố định, một property không hợp lệ đã được chỉ định! Chỉ có &blocation&c, &beffect&c, &bstyle&c, và &bdata &là hợp lệ.");
            this.put("fixed-edit-invalid-coords", "&cKhông thể điều chỉnh Hiệu ứng cố định, một hoặc nhiều tọa độ bạn đã nhập không hợp lệ!");
            this.put("fixed-edit-out-of-range", "&cKhông thể điều chỉnh Hiệu ứng cố định, Bạn phải ở trong bán kính &b%range% &ckhối tại vị trí mong muốn của bạn!");
            this.put("fixed-edit-looking-too-far", "&cKhông thể điều chỉnh Hiệu ứng cố định, Bạn đang đứng quá xa so với khối bạn nhìn!");
            this.put("fixed-edit-effect-invalid", "&cKhông thể điều chỉnh Hiệu ứng cố định, một Hiệu ứng với tên &b%effect% &ckhông tồn tại!");
            this.put("fixed-edit-effect-no-permission", "&cKhông thể điều chỉnh Hiệu ứng cố định, bạn không có quyền để sử dụng hiệu ứng &b%effect%&c!");
            this.put("fixed-edit-style-invalid", "&cKhông thể điều chỉnh Hiệu ứng cố định, Kiểu hiệu ứng &b%style% &ckhông tồn tại!");
            this.put("fixed-edit-style-no-permission", "&cKhông thể điều chỉnh Hiệu ứng cố định, bạn không có quyền để sử dụng kiểu hiệu ứng &b%style%&c!");
            this.put("fixed-edit-style-non-fixable", "&cKhông thể điều chỉnh Hiệu ứng cố định, Kiểu hiệu ứng &b%style% &ckhông thể được sử dụng trong Hiệu ứng cố định!");
            this.put("fixed-edit-data-error", "&cKhông thể điều chỉnh Hiệu ứng cố định, Dữ liệu bạn đã cung cấp không đúng! Sử dụng &b/pp data <effect> &cđể xem định dạng dữ liệu đúng!");
            this.put("fixed-edit-data-none", "&cKhông thể điều chỉnh Hiệu ứng cố định, Hiệu ứng không yêu cầu bất cứ Dữ liệu nào!");
            this.put("fixed-edit-success", "&aĐã cập nhật &b%prop% &acủa Hiệu ứng cố định với một ID của &b%id%&a!");

            this.put("#20", "Fixed Remove Messages");
            this.put("fixed-remove-invalid", "&cKhông thể xóa Hiệu ứng cố định, bạn không có hiệu ứng cố định nào với ID &b%id%&c!");
            this.put("fixed-remove-no-args", "&cbạn không chỉ định ID để xóa!");
            this.put("fixed-remove-args-invalid", "&cKhông thể xóa, ID được chỉ định phải là số");
            this.put("fixed-remove-success", "&aHiệu ứng cố định của bạn với ID &b%id% &ađã bị xóa!");

            this.put("#21", "Fixed List Messages");
            this.put("fixed-list-none", "&eBạn không có bất kì Hiệu ứng cố định nào!");
            this.put("fixed-list-success", "&eBạn có các Hiệu ứng cố định với IDs: &b%ids%");

            this.put("#22", "Fixed Info Messages");
            this.put("fixed-info-invalid", "&cKhông thể lấy thông tin, bạn không có bất kì Hiệu ứng cố định nào với ID &b%id%&c!");
            this.put("fixed-info-no-args", "&cBạn không chỉ định ID để hiển thị thông tin Hiệu ứng có định!");
            this.put("fixed-info-invalid-args", "&cKhông thể hiển thị Hiệu ứng cố định, ID được chỉ định phải là một con số!");
            this.put("fixed-info-success", "&eID: &b%id% &eWorld: &b%world% &eX: &b%x% &eY: &b%y% &eZ: &b%z% &eEffect: &b%effect% &eStyle: &b%style% &eData: &b%data%");

            this.put("#23", "Fixed Clear Messages");
            this.put("fixed-clear-no-permission", "&cBạn không có quyền để làm điều này!");
            this.put("fixed-clear-no-args", "&cBạn không cung cấp bán kính để xóa các Hiệu ứng cố định!");
            this.put("fixed-clear-invalid-args", "&cbán kính được cung cấp không hợp lệ, nó phải là một số nguyên dương!");
            this.put("fixed-clear-success", "&aĐã xóa &b%amount% &aHiệu ứng cố định trong bán kính &b%range% &akhối từ vị trí của bạn!");

            this.put("#23.5", "Fixed Teleport Message");
            this.put("fixed-teleport-no-permission", "&cBạn không có quyền dịch chuyển tức thời đến các hiệu ứng cố định!");
            this.put("fixed-teleport-no-args", "&cBạn đã không chỉ định một ID để dịch chuyển đến!");
            this.put("fixed-teleport-invalid-args", "&cKhông thể dịch chuyển tức thời, ID được chỉ định không hợp lệ!");
            this.put("fixed-teleport-success", "&eĐược dịch chuyển đến hiệu ứng cố định của bạn với ID &b%id%&e!");

            this.put("#24", "Fixed Other Messages");
            this.put("fixed-no-permission", "&cBạn không có quyền để làm điều này!");
            this.put("fixed-max-reached", "&cBạn đã đạt tới giới hạn số Hiệu ứng cố định có thể sử dụng!");
            this.put("fixed-invalid-command", "&cLệnh phụ không hợp lệ, &b/pp fixed&c!");

            this.put("#25", "Plugin Update Message");
            this.put("update-available", "&eĐã có bản cập nhật (&bv%new%&e)! bạn đang sử dụng &bv%current%&e. https://www.spigotmc.org/resources/playerparticles.40261/");

            this.put("#26", "GUI Messages");
            this.put("gui-disabled", "&cAmdinistrator đã cấm GUI!");
            this.put("gui-no-permission", "&cBạn không có quyền mở GUI!");

            this.put("#27", "GUI Color Messages");
            this.put("gui-color-icon-name", "&a");
            this.put("gui-color-info", "&e");
            this.put("gui-color-subtext", "&b");
            this.put("gui-color-unavailable", "&c");

            this.put("#28", "GUI Info Messages");
            this.put("gui-commands-info", "Hiển thị thêm các lệnh bằng cách gõ &b/pp help");
            this.put("gui-toggle-visibility-on", "Các hạt hiện có thể nhìn thấy được");
            this.put("gui-toggle-visibility-off", "Các hạt hiện đang bị ẩn");
            this.put("gui-toggle-visibility-info", "Nhấp để chuyển đổi chế độ hiển thị hạt");
            this.put("gui-back-button", "Quay trở về");
            this.put("gui-next-page-button", "Trang tiếp theo (%start%/%end%)");
            this.put("gui-previous-page-button", "Trang trước (%start%/%end%)");
            this.put("gui-click-to-load", "Nhấn để tải %amount% Hạt hiệu ứng sau:");
            this.put("gui-shift-click-to-delete", "Shift Click để xóa");
            this.put("gui-particle-info", "  - ID: &b%id% &eEffect: &b%effect% &eStyle: &b%style% &eData: &b%data%");
            this.put("gui-playerparticles", "HIỆU ỨNG");
            this.put("gui-active-particles", "Hạt hiệu ứng đang hoạt động: &b%amount%");
            this.put("gui-saved-groups", "Groups đã lưu: &b%amount%");
            this.put("gui-fixed-effects", "Hiệu ứng cố định: &b%amount%");

            this.put("#29", "GUI Edit Primary Messages");
            this.put("gui-edit-primary-effect", "Chỉnh sửa hiệu ứng chính (primary effect)");
            this.put("gui-edit-primary-effect-description", "Chỉnh sửa Hiệu ứng Hạt hiệu ứng chính của bạn");
            this.put("gui-edit-primary-style", "Chỉnh sửa Kiểu hiệu ứng chính");
            this.put("gui-edit-primary-style-missing-effect", "Bạn cần chọn Hiệu ứng trước");
            this.put("gui-edit-primary-style-description", "Chỉnh sửa Kiểu hiệu ứng trong Hạt hiệu ứng chính của bạn");
            this.put("gui-edit-primary-data", "Chỉnh sửa Dữ liệu chính");
            this.put("gui-edit-primary-data-missing-effect", "Bạn cần chọn Hiệu ứng trước");
            this.put("gui-edit-primary-data-unavailable", "Hiệu ứng chính của bạn không cần sử dụng bất kì Dữ liệu nào");
            this.put("gui-edit-primary-data-description", "Chỉnh sửa Dữ liệu trong Hạt hiệu ứng chính của bạn");

            this.put("#30", "GUI Manage Particles Messages");
            this.put("gui-manage-your-particles", "Quản lí Hạt hiệu ứng của bạn");
            this.put("gui-manage-your-particles-description", "Tạo, chỉnh sửa, và xóa Hạt hiệu ứng của bạn");
            this.put("gui-manage-your-groups", "Quản lí Group của bạn");
            this.put("gui-manage-your-groups-description", "Tạo, xóa, và tải Group Hạt hiệu ứng của bạn");

            this.put("#31", "GUI Load Messages");
            this.put("gui-load-a-preset-group", "Tải một Preset Group");
            this.put("gui-load-a-preset-group-description", "Tải Group Hạt hiệu ứng có sẵn");

            this.put("#32", "GUI Save Messages");
            this.put("gui-save-group", "Lưu group mới");
            this.put("gui-save-group-description", "Nhấn để lưu Group mới. Bạn sẽ nhận được lời nhắc nhập tên Group ở chat.");
            this.put("gui-save-group-full", "Bạn đã đạt đến giới hạn số lượng group có thể tạo");
            this.put("gui-save-group-no-particles", "Bạn không có bất kì Hhạt hiệu ứng nào được áp dụng");
            this.put("gui-save-group-hotbar-message", "&eNhập &b1 &etên group trong chat. Nhập &ccancel&e để hủy. (&b%seconds%&es left)");
            this.put("gui-save-group-chat-message", "&eSử dụng &b/pp group save <Tên> &eđể lưu một nhóm hạt mới.");

            this.put("#33", "GUI Reset Messages");
            this.put("gui-reset-particles", "Làm mới Hạt hiệu ứng của bạn");
            this.put("gui-reset-particles-description", "Xóa tất cả Hạt hiệu ứng đang hoạt động của bạn");

            this.put("#34", "GUI Misc Messages");
            this.put("gui-particle-name", "Hạt hiệu ứng #%id%");
            this.put("gui-click-to-edit-particle", "Nhấn để chỉnh sửa Hiệu ứng, Kiểu hiệu ứng, hoặc Dữ liệu của Hạt hiệu ứng này");
            this.put("gui-editing-particle", "Đang chỉnh sửa Hạt hiệu ứng #%id%");

            this.put("#35", "GUI Edit Messages");
            this.put("gui-edit-effect", "Chỉnh sửa Hiệu ứng");
            this.put("gui-edit-effect-description", "Nhấn để chỉnh sửa Hiệu ứng của Hạt hiệu ứng này");
            this.put("gui-edit-style", "Chỉnh sửa Kiểu hiệu ứng");
            this.put("gui-edit-style-description", "Nhấn để chỉnh sửa Kiểu hiệu ứng của Kiểu hiệu ứng này");
            this.put("gui-edit-data", "Chỉnh sửa Dữ liệu");
            this.put("gui-edit-data-description", "Nhấn để chỉnh sửa Dữ liệu của Kiểu hiệu ứng này");
            this.put("gui-edit-data-unavailable", "Hiệu ứng của Hạt hiệu ứng này không cần sử dụng bất kì Dữ liệu nào");
            this.put("gui-data-none", "không ai");

            this.put("#36", "GUI Create Messages");
            this.put("gui-create-particle", "Tạo mới một Hạt hiệu ứng");
            this.put("gui-create-particle-description", "Tạo mới một Hạt hiệu ứng với Hiệu ứng, Kiểu hiệu ứng, và Dữ liệu");
            this.put("gui-create-particle-unavailable", "bạn đã đạt giới hạn số lượng Hạt hiệu ứng có thể tạo");

            this.put("#37", "GUI Select Messages");
            this.put("gui-select-effect", "Chọn Hạt hiệu ứng");
            this.put("gui-select-effect-description", "Thiết lập hiệu ứng của Hạt thành &b%effect%");
            this.put("gui-select-style", "Chọn Kiểu hiệu ứng của Hạt");
            this.put("gui-select-style-description", "Thiết lập Kiểu hiệu ứng của Hạt thành &b%style%");
            this.put("gui-select-data", "Chọn Dữ liệu của Hạt");
            this.put("gui-select-data-description", "Thiết lập DỮ liệu của Hạt thành &b%data%");
            this.put("gui-select-data-note", "Note #%note%");
            this.put("gui-select-data-color-transition-start", "&eChọn màu &bbắt đầu");
            this.put("gui-select-data-color-transition-end", "&eChọn màu &bkết thúc");
            this.put("gui-select-data-vibration", "&b%ticks% &eticks");

            this.put("#38", "GUI Color Name Messages");
            this.put("gui-edit-data-color-red", "#ff0000Đỏ mạnh mẽ");
            this.put("gui-edit-data-color-orange", "#ff8c00Cam dịu dàng");
            this.put("gui-edit-data-color-yellow", "#ffff00Vàng yêu thương");
            this.put("gui-edit-data-color-lime-green", "#32cd32Xanh nhẹ nhàng");
            this.put("gui-edit-data-color-green", "#008000Xanh đen tối");
            this.put("gui-edit-data-color-blue", "#0000ffXanh lam");
            this.put("gui-edit-data-color-cyan", "#008b8bLục lam");
            this.put("gui-edit-data-color-light-blue", "#add8e6Xanh Da trời");
            this.put("gui-edit-data-color-purple", "#8a2be2Tím cá tính");
            this.put("gui-edit-data-color-magenta", "#ca1f7bĐỏ tươi");
            this.put("gui-edit-data-color-pink", "#ffb6c1Hồng nam tính");
            this.put("gui-edit-data-color-brown", "#8b4513Nâu thâm thâm");
            this.put("gui-edit-data-color-black", "#000000Đen như tâm hồn");
            this.put("gui-edit-data-color-gray", "#808080Xám");
            this.put("gui-edit-data-color-light-gray", "#c0c0c0Xám nhạt");
            this.put("gui-edit-data-color-white", "#ffffffTrắng tinh khiết");
        }};
    }
}
