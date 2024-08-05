package gg.petrushka.bankcards.command.manager;

import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.command.model.SubCommand;
import gg.petrushka.bankcards.command.subcommand.bank.Create;
import gg.petrushka.bankcards.command.subcommand.bank.GetMoney;
import gg.petrushka.bankcards.command.subcommand.bank.Reload;
import gg.petrushka.bankcards.service.ConfigData;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class SubCommandManager {

    @Getter
    private final List<SubCommand> subCommands = new ArrayList<>();

    public void createSubCommands(ConfigData configData, CardService cardService){
        SubCommand create = new Create("create", true, configData, cardService);
        create.setPermission("bankcards.bank.create");
        create.setUsage(" ");
        create.setDescription((String) configData.getData("commands.create-description"));

        SubCommand reload = new Reload("reload", true, configData);
        reload.setPermission("bankcards.bank.reload");
        reload.setUsage(" ");
        reload.setDescription((String) configData.getData("commands.reload-description"));

        SubCommand getmoney = new GetMoney("getmoney", true, configData);
        getmoney.setPermission("bankcards.bank.getmoney");
        getmoney.setUsage((String) configData.getData("commands.getmoney-usage"));
        getmoney.setDescription((String) configData.getData("commands.getmoney-description"));

        subCommands.add(create);
        subCommands.add(reload);
        subCommands.add(getmoney);
    }

}
