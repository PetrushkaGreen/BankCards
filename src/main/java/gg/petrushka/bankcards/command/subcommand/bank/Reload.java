package gg.petrushka.bankcards.command.subcommand.bank;

import gg.petrushka.bankcards.BankCards;
import gg.petrushka.bankcards.command.model.SubCommandImpl;
import gg.petrushka.bankcards.penalty.PenaltyManager;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Reload extends SubCommandImpl {

    public Reload(String name, boolean forPlayers, ConfigData data) {
        super(name, forPlayers, data);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        boolean a = super.execute(sender, args);
        if(a) return true;
        BankCards.getInstance().configs();
        BankCards.getInstance().initConfigData();
        PenaltyManager.startRunnable();
        String message = ColorUtil.colorizeString("&f[&bBankCards&f] #60DD14Плагин перезагружен!");
        sender.sendMessage(message);
        if(sender instanceof Player){
            Player player = (Player) sender;
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 10, 10);
        }
        return false;
    }
}
