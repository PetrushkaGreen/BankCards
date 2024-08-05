package gg.petrushka.bankcards.transfer;

import gg.petrushka.bankcards.card.BankCard;
import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.item.ItemManager;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DepositManager {

    @Setter
    private static CardService cardService;
    @Setter
    private static ConfigData configData;

    private static final List<Player> depositSessions = new ArrayList<>();

    public static void addPlayer(Player player){
        depositSessions.add(player);
    }

    public static void removePlayer(Player player){
        depositSessions.remove(player);
    }

    public static void depositMoney(Player player, int value){
        @NotNull BankCard senderCard = cardService.getCard(player);
        if(ItemManager.getPhysicalCurrency(player) < value){
            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.not-enough-physical-balance")));
            return;
        }
        senderCard.addBalance(value);
        ItemManager.removeMoney(player, value);
        removePlayer(player);
        player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.deposit-successful"))
                .replaceAll("\\{DEPOSIT-COUNT}", String.valueOf(value)));
    }

    public static boolean isInDepositState(Player player){
        return depositSessions.contains(player);
    }
}
