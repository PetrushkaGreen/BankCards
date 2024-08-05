package gg.petrushka.bankcards.command;

import gg.petrushka.bankcards.BankCards;
import gg.petrushka.bankcards.command.model.SubCommand;
import gg.petrushka.bankcards.gui.menu.BankMenu;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BankCommand implements CommandExecutor, TabCompleter {

    private ConfigData configData;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player player)){
            sender.sendMessage("Только для игроков!");
            return true;
        }
        List<SubCommand> subCommands = BankCards.getInstance().getSubCommandManager().getSubCommands();

        if(args.length == 0){
            String message = ColorUtil.colorizeString((String) configData.getData("messages.about-bank-commands"));
            sender.sendMessage(message);
            for(SubCommand subCommand : subCommands){
                if(!sender.hasPermission(subCommand.getPermission()) && !sender.hasPermission("bankcards.*")) continue;
                String commandMessage = ColorUtil.colorizeString(subCommand.getDescription());
                sender.sendMessage(commandMessage);
            }
            return true;
        }
        for (SubCommand subCommand : subCommands){
            if(!subCommand.getName().equalsIgnoreCase(args[0])) continue;
            return subCommand.execute(sender, args);
        }
        String noneCommand = ColorUtil.colorizeString("&7Неизвестная команда!");
        sender.sendMessage(noneCommand);
        return false;
    }

    public BankCommand(ConfigData configData) {
        this.configData = configData;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        List<String> complete = new ArrayList<>();
        List<SubCommand> subCommands = BankCards.getInstance().getSubCommandManager().getSubCommands();
        if(args.length == 1){
            for(SubCommand subCommand : subCommands){
                complete.add(subCommand.getName());
            }
        }
        return filter(complete, args);
    }

    public List<String> filter(List<String> list, String[] args) {
        String last = args[args.length - 1].toLowerCase();
        List<String> result = new ArrayList<>();

        for (String s : list) {
            if (s.startsWith(last)) {
                result.add(s);
            }
        }

        return result;
    }
}
