package gg.petrushka.bankcards.command;

import gg.petrushka.bankcards.card.BankCard;
import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.penalty.Penalty;
import gg.petrushka.bankcards.penalty.PenaltyManager;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PenaltyCommand implements CommandExecutor {

    private final ConfigData configData;
    private final CardService cardService;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(!(sender instanceof Player player)) return true;
        int length = args.length;
        if(length < 4){
            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("commands.penalty-usage")));
            return true;
        }
        Player target = Bukkit.getPlayer(args[0]);
        if(target == null){
            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.wrong-player")));
            return true;
        }
        BankCard card = cardService.getCard(target);
        if(card == null){
            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.no-player-card")));
            return true;
        }
        int amount;
        try {
            amount = Integer.parseInt(args[1]);
        } catch (NumberFormatException e){
            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.wrong-number")));
            return true;
        }
        int minutes;
        try {
            minutes = Integer.parseInt(args[2]);
        } catch (NumberFormatException e){
            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.wrong-number")));
            return true;
        }
        StringBuilder reasonBuilder = new StringBuilder();
        for (int i = 3; i < length; i++){
            reasonBuilder.append(" ").append(args[i]);
        }
        Penalty penalty = new Penalty();

        penalty.setPlayerId(target.getUniqueId());
        penalty.setAmount(amount);
        penalty.setReason(reasonBuilder.toString());
        penalty.setTime(minutes * 60);
        PenaltyManager.addPenalty(player, penalty);
        player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.send-penalty")));
        return false;
    }


    public PenaltyCommand(ConfigData configData, CardService cardService) {
        this.configData = configData;
        this.cardService = cardService;
    }


}
