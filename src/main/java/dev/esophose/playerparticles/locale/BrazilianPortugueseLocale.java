package dev.esophose.playerparticles.locale;

import dev.rosewood.rosegarden.locale.Locale;

import java.util.LinkedHashMap;
import java.util.Map;

public class BrazilianPortugueseLocale implements Locale {

    @Override
    public String getLocaleName() {
        return "pt_BR";
    }

    @Override
    public String getTranslatorName() {
        return "Guztaver";
    }
    
    @Override
    public Map<String, Object> getDefaultLocaleValues() {
        return new LinkedHashMap<String, Object>() {{
            this.put("#0", "Prefixo da Mensagem do Plugin");
            this.put("prefix", "&7[<g:#00aaaa:#0066aa>PlayerParticles&7] ");

            this.put("#1", "Mensagens de Descrição do Comando");
            this.put("command-error-missing-effects-or-styles", "&cVocê deve ter acesso aos efeitos e estilos para usar este comando!");
            this.put("command-error-unknown", "&cComando desconhecido, use &b/pp help &cpara ver a lista de comandos.");
            this.put("command-descriptions", "&cOs seguintes comandos estão disponíveis:");
            this.put("command-descriptions-usage", "&e/pp %cmd% %args%");
            this.put("command-descriptions-help-1", "&7> &b/pp %cmd% &e- %desc%");
            this.put("command-descriptions-help-2", "&7> &b/pp %cmd% %args% &e- %desc%");
            this.put("command-descriptions-help-other", "&7> &b/ppo <jogador> <comando> &e- Rode o /pp command como um jogador");
            this.put("command-description-add", "Adiciona uma nova particula");
            this.put("command-description-data", "Verifica que tipo de dados um efeito usa");
            this.put("command-description-default", "O comando principal. Por padrão, abre a interface gráfica");
            this.put("command-description-edit", "Edita uma partícula");
            this.put("command-description-effects", "Exibe uma lista de efeitos que você pode usar");
            this.put("command-description-fixed", "Gerencie os efeitos fixados");
            this.put("command-description-group", "Gerencie os seus grupos");
            this.put("command-description-gui", "Exibe uma interface gráfica para uma edição mais fácil das partículas");
            this.put("command-description-help", "Exibe o menu de ajuda... Você chegou aqui");
            this.put("command-description-info", "Pega a descrição de uma das suas partículas ativas");
            this.put("command-description-list", "Lista os IDs de suas partículas ativas");
            this.put("command-description-reload", "Recarrega o config.yml e os arquivos lang");
            this.put("command-description-remove", "Remove algumas partículas");
            this.put("command-description-reset", "Remove todas as suas partículas");
            this.put("command-description-styles", "Exibe uma lista de estilos que você pode usar");
            this.put("command-description-toggle", "Alterna a visibilidade das sua partículas entre ligado/desligado");
            this.put("command-description-use", "Modifica sua partícula principal");
            this.put("command-description-version", "Exibe a versão do plugin e o autor");
            this.put("command-description-worlds", "Encontra em quais mundos as partículas estão desabilitadas");

            this.put("#2", "Mensagens de descrição de partículas fixas");
            this.put("command-description-fixed-create", "&e/pp fixed create <<x> <y> <z>|<looking>> <efeito> <estilo> [dados] - Cria um novo efeito fixo");
            this.put("command-description-fixed-create-console", "&e/pp fixed create <x> <y> <z> <mundo> <efeito> <estilo> [dados] - Cria um novo efeito fixo");
            this.put("command-description-fixed-edit", "&e/pp fixed edit <id> <effect|style|data|location> <argumentos> - Edita parte de um efeito fixo pelo seu ID");
            this.put("command-description-fixed-remove", "&e/pp fixed remove <ID> - Remove um efeito fixo pelo seu ID");
            this.put("command-description-fixed-list", "&e/pp fixed list - Lista todos os IDs dos seus efeitos fixos");
            this.put("command-description-fixed-info", "&e/pp fixed info <ID> - Obtém informações sobre um dos seus efeitos fixos");
            this.put("command-description-fixed-clear", "&e/pp fixed clear <radius> - Limpa todos os efeitos fixos de todos os jogadores dentro do raio fornecido");
            this.put("command-description-fixed-clear-console", "&e/pp fixed clear <raio> <x> <y> <z> <mundo> - Limpa todos os efeitos fixos de todos os jogadores dentro do raio fornecido");
            this.put("command-description-fixed-teleport", "&e/pp fixed teleport <id> - Teleporta você para um dos seus efeitos fixos");

            this.put("#2.5", "Mensagens de descrição de comandos de grupos");
            this.put("command-description-group-save", "&e/pp group save <nome> - Salva todas as partículas ativas em um novo grupo");
            this.put("command-description-group-load", "&e/pp group load <nome> - Carrega todas as partículas salvas em um grupo");
            this.put("command-description-group-remove", "&e/pp group remove <nome> - Remove um grupo que você salvou");
            this.put("command-description-group-list", "&e/pp group list <nome> - Lista todos os grupos de partículas que você salvou");
            this.put("command-description-group-info", "&e/pp group info <nome> - Lista as partículas salvas no grupo");


            this.put("#3", "ID Messages");
            this.put("id-invalid", "&cO ID que você colocou é inválido, o número deve ser um positivo inteiro!");
            this.put("id-unknown", "&cVocê não tem uma particula aplicada com o ID! &b%id%&c!");

            this.put("#4", "Other Messages");
            this.put("other-no-permission", "&cVocê não tem permissão para executar o comando PlayerParticles para outros jogadores!");
            this.put("other-missing-args", "&cVocê está se esquecendo de algun argumentos. &b/ppo <jogador> <comando>");
            this.put("other-unknown-player", "&cO jogador &b%player% &cnão foi encontrador. Ele deve estar online.");
            this.put("other-unknown-command", "&cO comando &b/pp %cmd% &cnãp existe.");
            this.put("other-success", "&eComando exucatado /pp para &b%player%&e. Saída:");

            this.put("#5", "Mensagens de Adição");
            this.put("add-reached-max", "&cNão é possível aplicar a partícula, você atingiu o limite máximo de &b%amount% &cpermitido(s)!");
            this.put("add-particle-applied", "&aUma nova partícula foi aplicada com o efeito &b%effect%&a, estilo &b%style%&a e dados &b%data%&a!");
            this.put("data-no-args", "&cArgumento ausente para o efeito! Uso do comando: &b/pp data <efeito>");

            this.put("#6", "Mensagens de Edição");
            this.put("edit-invalid-property", "&cFoi fornecida uma propriedade inválida: &b%prop%&c. Propriedades válidas: &beffect&c, &bstyle&c, &bdata");
            this.put("edit-success-effect", "&aSua partícula com ID &b%id% &ateve seu efeito alterado para &b%effect%&a!");
            this.put("edit-success-style", "&aSua partícula com ID &b%id% &ateve seu estilo alterado para &b%style%&a!");
            this.put("edit-success-data", "&aSua partícula com ID &b%id% &ateve seus dados alterados para &b%data%&a!");

            this.put("#7", "Mensagens de Grupo");
            this.put("group-invalid", "&cNão existe um grupo salvo ou grupo pré-definido com o nome &b%name%&c!");
            this.put("group-no-permission", "&cVocê não tem permissão para usar um efeito ou estilo do grupo &b%group%&c!");
            this.put("group-preset-no-permission", "&cVocê não tem permissão para usar um efeito ou estilo do grupo pré-definido &b%group%&c!");
            this.put("group-reserved", "&cO nome do grupo &bactive &cé reservado e não pode ser usado!");
            this.put("group-no-name", "&cVocê não forneceu um nome de grupo! &b/pp %cmd% <nomeDoGrupo>");
            this.put("group-save-reached-max", "&cNão é possível salvar o grupo, você atingiu o número máximo de grupos!");
            this.put("group-save-no-particles", "&cNão é possível salvar o grupo, você não tem nenhuma partícula aplicada!");
            this.put("group-save-success", "&aSuas partículas atuais foram salvas com o nome do grupo &b%name%&a!");
            this.put("group-save-success-overwrite", "&aO grupo com o nome &b%name% &afoi atualizado com suas partículas atuais!");
            this.put("group-load-success", "&aForam aplicada(s) &b%amount% &apartícula(s) do grupo salvo com o nome &b%name%&a!");
            this.put("group-load-preset-success", "&aForam aplicada(s) &b%amount% &apartícula(s) do grupo pré-definido com o nome &b%name%&a!");
            this.put("group-unload-success", "&aForam removida(s) &b%amount% &apartícula(s) correspondentes ao grupo &b%name%&a!");
            this.put("group-remove-preset", "&cVocê não pode remover um grupo pré-definido!");
            this.put("group-remove-success", "&aO grupo de partículas com o nome &b%name% &afoi removido!");
            this.put("group-info-header", "&eO grupo &b%group% &econtém as seguintes partículas:");
            this.put("group-list-none", "&eVocê não possui nenhum grupo de partículas salvo!");
            this.put("group-list-output", "&eVocê possui os seguintes grupos salvos: &b%info%");
            this.put("group-list-presets", "&eOs seguintes grupos pré-definidos estão disponíveis: &b%info%");

            this.put("#8", "Mensagens de Recarregamento");
            this.put("reload-success", "&aO plugin foi recarregado!");
            this.put("reload-no-permission", "&cVocê não tem permissão para recarregar as configurações do plugin!");

            this.put("#9", "Mensagens de Remoção");
            this.put("remove-no-args", "&cVocê não especificou um ID para remover! &b/pp remove <ID>");
            this.put("remove-id-success", "&aSua partícula com o ID &b%id% &afoi removida!");
            this.put("remove-effect-success", "&aForam removida(s) &b%amount% &apartícula(s) com o efeito &b%effect%&a!");
            this.put("remove-effect-none", "&cVocê não possui nenhuma partícula aplicada com o efeito &b%effect%&c!");
            this.put("remove-style-success", "&aForam removida(s) &b%amount% &apartícula(s) com o estilo &b%style%&a!");
            this.put("remove-style-none", "&cVocê não possui nenhuma partícula aplicada com o estilo &b%style%&c!");
            this.put("remove-effect-style-none", "&cVocê não possui nenhuma partícula aplicada com o efeito ou estilo &b%name%&c!");
            this.put("remove-unknown", "&cNão existe um efeito ou estilo com o nome &b%name%&c!");

            this.put("#10", "Mensagens de Listagem");
            this.put("list-none", "&eVocê não possui nenhuma partícula ativa!");
            this.put("list-you-have", "&eVocê possui as seguintes partículas aplicadas:");
            this.put("list-output", "&eID: &b%id% &eEfeito: &b%effect% &eEstilo: &b%style% &eDados: &b%data%");

            this.put("#11", "Mensagens de Ativação");
            this.put("toggle-on", "&eAs partículas foram ativadas &aLIGADO&e!");
            this.put("toggle-off", "&eAs partículas foram desativadas &cDESLIGADO&e!");

            this.put("#11.5", "Mensagens de Uso");
            this.put("use-particle-modified", "&aSua partícula principal foi modificada para usar o efeito &b%effect%&a, estilo &b%style%&a e dados &b%data%&a!");

            this.put("#12", "Mensagens de Cores");
            this.put("rainbow", "&cVermelho&6Laranja&eAmarelo&aVerde&bAzul&9Anil&dVioleta&dw");
            this.put("random", "Aleatório");

            this.put("#13", "Mensagens de Efeito");
            this.put("effect-no-permission", "&cVocê não tem permissão para usar o efeito &b%effect%&c!");
            this.put("effect-invalid", "&cO efeito &b%effect% &cnão existe! Use &b/pp effects &cpara ver a lista de efeitos disponíveis.");
            this.put("effect-list", "&eVocê pode usar os seguintes efeitos: &b%effects%");
            this.put("effect-list-empty", "&cVocê não tem permissão para usar nenhum efeito!");

            this.put("#14", "Mensagens de Estilo");
            this.put("style-no-permission", "&cVocê não tem permissão para usar o estilo &b%style%&c!");
            this.put("style-event-spawning-info", "&eObservação: O estilo &b%style% &ecria partículas com base em um evento.");
            this.put("style-invalid", "&cO estilo &b%style% &cnão existe! Use &b/pp styles &cpara ver a lista de estilos disponíveis.");
            this.put("style-list", "&eVocê pode usar os seguintes estilos: &b%styles%");

            this.put("#16", "Mensagens de Mundo");
            this.put("disabled-worlds", "&ePartículas estão desativadas nestes mundos: &b%worlds%");
            this.put("disabled-worlds-none", "&ePartículas não estão desativadas em nenhum mundo.");

            this.put("#17", "Mensagem de Reinício");
            this.put("reset-success", "&aRemovida(s) &b%amount% &apartícula(s) ativa(s)!");
            this.put("reset-others-success", "&aPartículas removidas para &b%other%&a!");
            this.put("reset-others-none", "&eNenhuma partícula foi removida para &b%other%&e.");

            this.put("#18", "Mensagens de Criação Fixa");
            this.put("fixed-create-missing-args", "&cNão foi possível criar o efeito fixo, você está faltando &b%amount% &cargumentos obrigatórios!");
            this.put("fixed-create-invalid-coords", "&cNão foi possível criar o efeito fixo, uma ou mais coordenadas inseridas são inválidas!");
            this.put("fixed-create-out-of-range", "&cNão foi possível criar o efeito fixo, você deve estar a até &b%range% &cblocos da localização desejada!");
            this.put("fixed-create-looking-too-far", "&cNão foi possível criar o efeito fixo, você está muito longe do bloco em que está olhando!");
            this.put("fixed-create-effect-invalid", "&cNão foi possível criar o efeito fixo, um efeito com o nome &b%effect% &cnão existe!");
            this.put("fixed-create-effect-no-permission", "&cNão foi possível criar o efeito fixo, você não tem permissão para usar o efeito &b%effect%&c!");
            this.put("fixed-create-style-invalid", "&cNão foi possível criar o efeito fixo, um estilo com o nome &b%style% &cnão existe!");
            this.put("fixed-create-style-no-permission", "&cNão foi possível criar o efeito fixo, você não tem permissão para usar o estilo &b%style%&c!");
            this.put("fixed-create-style-non-fixable", "&cNão foi possível criar o efeito fixo, o estilo &b%style% &cnão pode ser usado em efeitos fixos!");
            this.put("fixed-create-data-error", "&cNão foi possível criar o efeito fixo, os dados fornecidos não estão corretos! Use &b/pp data <effect> &cpara encontrar o formato de dados correto!");
            this.put("fixed-create-success", "&aSeu efeito fixo foi criado!");

            this.put("#19", "Mensagens de Edição Fixa");
            this.put("fixed-edit-missing-args", "&cNão foi possível editar o efeito fixo, você está faltando alguns argumentos!");
            this.put("fixed-edit-invalid-id", "&cNão foi possível editar o efeito fixo, o ID especificado é inválido ou não existe!");
            this.put("fixed-edit-invalid-property", "&cNão foi possível editar o efeito fixo, foi especificada uma propriedade inválida! Apenas &blocation&c, &beffect&c, &bstyle&c e &bdata &csão válidas.");
            this.put("fixed-edit-invalid-coords", "&cNão foi possível editar o efeito fixo, uma ou mais coordenadas inseridas são inválidas!");
            this.put("fixed-edit-out-of-range", "&cNão foi possível editar o efeito fixo, você deve estar a até &b%range% &cblocos da localização desejada!");
            this.put("fixed-edit-looking-too-far", "&cNão foi possível editar o efeito fixo, você está muito longe do bloco em que está olhando!");
            this.put("fixed-edit-effect-invalid", "&cNão foi possível editar o efeito fixo, um efeito com o nome &b%effect% &cnão existe!");
            this.put("fixed-edit-effect-no-permission", "&cNão foi possível editar o efeito fixo, você não tem permissão para usar o efeito &b%effect%&c!");
            this.put("fixed-edit-style-invalid", "&cNão foi possível editar o efeito fixo, um estilo com o nome &b%style% &cnão existe!");
            this.put("fixed-edit-style-no-permission", "&cNão foi possível editar o efeito fixo, você não tem permissão para usar o estilo &b%style%&c!");
            this.put("fixed-edit-style-non-fixable", "&cNão foi possível editar o efeito fixo, o estilo &b%style% &cnão pode ser usado em efeitos fixos!");
            this.put("fixed-edit-data-error", "&cNão foi possível editar o efeito fixo, os dados fornecidos não estão corretos! Use &b/pp data <effect> &cpara encontrar o formato de dados correto!");
            this.put("fixed-edit-data-none", "&cNão foi possível editar o efeito fixo, o efeito não requer nenhum dado!");
            this.put("fixed-edit-success", "&aA propriedade &b%prop% &ado efeito fixo com ID &b%id% &afoi atualizada!");

            this.put("#20", "Mensagens de Remoção Fixa");
            this.put("fixed-remove-invalid", "&cNão foi possível remover o efeito fixo, você não possui um efeito fixo com o ID &b%id%&c!");
            this.put("fixed-remove-no-args", "&cVocê não especificou um ID para remover!");
            this.put("fixed-remove-args-invalid", "&cNão foi possível remover, o ID especificado deve ser um número!");
            this.put("fixed-remove-success", "&aSeu efeito fixo com o ID &b%id% &afoi removido!");

            this.put("#21", "Mensagens de Listagem Fixa");
            this.put("fixed-list-none", "&eVocê não possui efeitos fixos!");
            this.put("fixed-list-success", "&eVocê possui efeitos fixos com os seguintes IDs: &b%ids%");

            this.put("#22", "Mensagens de Informações Fixas");
            this.put("fixed-info-invalid", "&cNão foi possível obter informações, você não possui um efeito fixo com o ID &b%id%&c!");
            this.put("fixed-info-no-args", "&cVocê não especificou um ID para exibir informações!");
            this.put("fixed-info-invalid-args", "&cNão foi possível obter informações, o ID especificado deve ser um número!");
            this.put("fixed-info-success", "&eID: &b%id% &eMundo: &b%world% &eX: &b%x% &eY: &b%y% &eZ: &b%z% &eEfeito: &b%effect% &eEstilo: &b%style% &eDados: &b%data%");

            this.put("#23", "Mensagens de Limpeza Fixa");
            this.put("fixed-clear-no-permission", "&cVocê não tem permissão para limpar os efeitos fixos próximos!");
            this.put("fixed-clear-no-args", "&cVocê não forneceu um raio para limpar os efeitos fixos!");
            this.put("fixed-clear-invalid-args", "&cO raio fornecido é inválido, deve ser um número inteiro positivo!");
            this.put("fixed-clear-success", "&aLimpos &b%amount% &aefeitos fixos dentro de um raio de &b%range% &ablocos da sua localização!");

            this.put("#23.5", "Mensagem de Teletransporte Fixo");
            this.put("fixed-teleport-no-permission", "&cVocê não tem permissão para se teletransportar para efeitos fixos!");
            this.put("fixed-teleport-no-args", "&cVocê não especificou um ID para se teletransportar!");
            this.put("fixed-teleport-invalid-args", "&cNão foi possível se teletransportar, o ID especificado é inválido!");
            this.put("fixed-teleport-success", "&eTeleportado para o seu efeito fixo com o ID &b%id%&e!");

            this.put("#24", "Mensagens Adicionais de Fixo");
            this.put("fixed-no-permission", "&cVocê não tem permissão para usar efeitos fixos!");
            this.put("fixed-max-reached", "&cVocê atingiu o limite máximo de efeitos fixos permitidos!");
            this.put("fixed-invalid-command", "&cSub-comando inválido para &b/pp fixed&c!");

            this.put("#25", "Mensagem de Atualização do Plugin");
            this.put("update-available", "&eUma atualização está disponível (v%new%&e)! Você está usando a versão &bv%current%&e. https://www.spigotmc.org/resources/playerparticles.40261/");

            this.put("#26", "Mensagens da GUI");
            this.put("gui-disabled", "&cO administrador do servidor desativou a GUI!");
            this.put("gui-no-permission", "&cVocê não tem permissão para abrir a GUI!");

            this.put("#27", "Mensagens de Cores da GUI");
            this.put("gui-color-icon-name", "&a");
            this.put("gui-color-info", "&e");
            this.put("gui-color-subtext", "&b");
            this.put("gui-color-unavailable", "&c");

            this.put("#28", "Mensagens de Informações da GUI");
            this.put("gui-commands-info", "Encontre informações sobre comandos com &b/pp help");
            this.put("gui-toggle-visibility-on", "As partículas estão &avisíveis");
            this.put("gui-toggle-visibility-off", "As partículas estão &cinvisíveis");
            this.put("gui-toggle-visibility-info", "Clique para alternar a visibilidade das partículas");
            this.put("gui-back-button", "Voltar");
            this.put("gui-next-page-button", "Próxima Página (%start%/%end%)");
            this.put("gui-previous-page-button", "Página Anterior (%start%/%end%)");
            this.put("gui-click-to-load", "Clique para carregar a(s) seguinte(s) %amount% partícula(s):");
            this.put("gui-shift-click-to-delete", "Shift + clique para excluir");
            this.put("gui-particle-info", "  - ID: &b%id% &eEfeito: &b%effect% &eEstilo: &b%style% &eDados: &b%data%");
            this.put("gui-playerparticles", "PlayerParticles");
            this.put("gui-active-particles", "Partículas Ativas: &b%amount%");
            this.put("gui-saved-groups", "Grupos Salvos: &b%amount%");
            this.put("gui-fixed-effects", "Efeitos Fixos: &b%amount%");

            this.put("#29", "Mensagens de Edição Primária da GUI");
            this.put("gui-edit-primary-effect", "Editar Efeito Primário");
            this.put("gui-edit-primary-effect-description", "Edite o efeito da sua partícula primária");
            this.put("gui-edit-primary-style", "Editar Estilo Primário");
            this.put("gui-edit-primary-style-missing-effect", "Você deve selecionar um efeito primeiro");
            this.put("gui-edit-primary-style-description", "Edite o estilo da sua partícula primária");
            this.put("gui-edit-primary-data", "Editar Dados Primários");
            this.put("gui-edit-primary-data-missing-effect", "Você deve selecionar um efeito primeiro");
            this.put("gui-edit-primary-data-unavailable", "Seu efeito primário não utiliza nenhum dado");
            this.put("gui-edit-primary-data-description", "Edite os dados da sua partícula primária");

            this.put("#30", "Mensagens de Gerenciamento de Partículas da GUI");
            this.put("gui-manage-your-particles", "Gerenciar Suas Partículas");
            this.put("gui-manage-your-particles-name", "Gerenciar Suas Partículas");
            this.put("gui-manage-your-particles-description", "Crie, edite e exclua suas partículas");
            this.put("gui-manage-your-groups", "Gerenciar Seus Grupos");
            this.put("gui-manage-your-groups-name", "Gerenciar Seus Grupos");
            this.put("gui-manage-your-groups-description", "Crie, exclua e carregue seus grupos de partículas");

            this.put("#31", "Mensagens de Carregamento da GUI");
            this.put("gui-load-a-preset-group", "Carregar um Grupo Pré-definido");
            this.put("gui-load-a-preset-group-description", "Carregue um grupo de partículas pré-criado");

            this.put("#32", "Mensagens de Salvamento da GUI");
            this.put("gui-save-group", "Salvar Novo Grupo");
            this.put("gui-save-group-description", "Clique para salvar um novo grupo. Você será solicitado\na digitar o nome do novo grupo no chat.");
            this.put("gui-save-group-full", "Você atingiu o número máximo de grupos");
            this.put("gui-save-group-no-particles", "Você não tem nenhuma partícula aplicada");
            this.put("gui-save-group-hotbar-message", "&eDigite &b1 &epalavra no chat para o nome do novo grupo. Digite &ccancelar&e para cancelar. (&b%seconds%&es restantes)");
            this.put("gui-save-group-chat-message", "&eUse &b/pp group save <nome> &epara salvar um novo grupo de partículas.");

            this.put("#33", "Mensagens de Redefinição da GUI");
            this.put("gui-reset-particles", "Redefinir Suas Partículas");
            this.put("gui-reset-particles-description", "Exclui todas as suas partículas ativas");

            this.put("#34", "Mensagens Diversas da GUI");
            this.put("gui-particle-name", "Partícula #%id%");
            this.put("gui-click-to-edit-particle", "Clique para editar o efeito, estilo ou dados desta partícula");
            this.put("gui-editing-particle", "Editando Partícula #%id%");

            this.put("#35", "Mensagens de Edição da GUI");
            this.put("gui-edit-effect", "Editar Efeito");
            this.put("gui-edit-effect-description", "Clique para editar o efeito desta partícula");
            this.put("gui-edit-style", "Editar Estilo");
            this.put("gui-edit-style-description", "Clique para editar o estilo desta partícula");
            this.put("gui-edit-data", "Editar Dados");
            this.put("gui-edit-data-description", "Clique para editar os dados desta partícula");
            this.put("gui-edit-data-unavailable", "O efeito desta partícula não utiliza nenhum dado");
            this.put("gui-data-none", "nenhum");

            this.put("#36", "Mensagens de Criação da GUI");
            this.put("gui-create-particle", "Criar uma Nova Partícula");
            this.put("gui-create-particle-description", "Crie uma nova partícula com um efeito, estilo e dados");
            this.put("gui-create-particle-unavailable", "Você atingiu a quantidade máxima de partículas que pode criar");

            this.put("#37", "Mensagens de Seleção da GUI");
            this.put("gui-select-effect", "Selecionar Efeito de Partícula");
            this.put("gui-select-effect-description", "Define o efeito da partícula para &b%effect%");
            this.put("gui-select-style", "Selecionar Estilo de Partícula");
            this.put("gui-select-style-description", "Define o estilo da partícula para &b%style%");
            this.put("gui-select-data", "Selecionar Dados de Partícula");
            this.put("gui-select-data-description", "Define os dados da partícula para &b%data%");
            this.put("gui-select-data-note", "Nota #%note%");
            this.put("gui-select-data-color-transition-start", "&eSelecione a cor &binicial");
            this.put("gui-select-data-color-transition-end", "&eSelecione a cor &bfinal");
            this.put("gui-select-data-vibration", "&b%ticks% &epulsos");

            this.put("#38", "Mensagens de Nome de Cores da GUI");
            this.put("gui-edit-data-color-red", "#ff0000Vermelho");
            this.put("gui-edit-data-color-orange", "#ff8c00Laranja");
            this.put("gui-edit-data-color-yellow", "#ffff00Amarelo");
            this.put("gui-edit-data-color-lime-green", "#32cd32Verde Limão");
            this.put("gui-edit-data-color-green", "#008000Verde");
            this.put("gui-edit-data-color-blue", "#0000ffAzul");
            this.put("gui-edit-data-color-cyan", "#008b8bCiano");
            this.put("gui-edit-data-color-light-blue", "#add8e6Azul Claro");
            this.put("gui-edit-data-color-purple", "#8a2be2Roxo");
            this.put("gui-edit-data-color-magenta", "#ca1f7bMagenta");
            this.put("gui-edit-data-color-pink", "#ffb6c1Rosa");
            this.put("gui-edit-data-color-brown", "#8b4513Marrom");
            this.put("gui-edit-data-color-black", "#000000Preto");
            this.put("gui-edit-data-color-gray", "#808080Cinza");
            this.put("gui-edit-data-color-light-gray", "#c0c0c0Cinza Claro");
            this.put("gui-edit-data-color-white", "#ffffffBranco");

        }};
    }

}
