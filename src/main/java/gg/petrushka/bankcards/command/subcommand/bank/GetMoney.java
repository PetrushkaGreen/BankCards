package gg.petrushka.bankcards.command.subcommand.bank;

import gg.petrushka.bankcards.command.model.SubCommandImpl;
import gg.petrushka.bankcards.item.ItemManager;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GetMoney extends SubCommandImpl {
    public GetMoney(String name, boolean forPlayers, ConfigData data) {
        super(name, forPlayers, data);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        boolean a = super.execute(sender, args);
        if(a) return true;
        Player player = (Player) sender;
        if(args == null || args.length < 2){
            player.sendMessage(ColorUtil.colorizeString(getUsage()));
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException ignored){
            player.sendMessage(ColorUtil.colorizeString(getUsage()));
            return false;
        }
        ItemManager.addMoneyStack(player, amount);
        return false;
    }
}
