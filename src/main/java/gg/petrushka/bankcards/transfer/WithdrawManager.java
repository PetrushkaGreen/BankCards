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

public class WithdrawManager {

    @Setter
    private static CardService cardService;
    @Setter
    private static ConfigData configData;

    private static final List<Player> withdrawSessions = new ArrayList<>();

    public static void addPlayer(Player player){
        withdrawSessions.add(player);
    }

    public static void removePlayer(Player player){
        withdrawSessions.remove(player);
    }

    public static void withdrawMoney(Player player, int value){
        @NotNull BankCard senderCard = cardService.getCard(player);
        if(senderCard.getBalance() < value){
            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.not-enough-virtual-balance")));
            return;
        }
        senderCard.addBalance(-value);
        player.getInventory().addItem(ItemManager.getMoneyStack(value));
        removePlayer(player);
        player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.withdraw-successful"))
                .replaceAll("\\{WITHDRAW-COUNT}", String.valueOf(value)));
    }

    public static boolean isInWithdrawState(Player player){
        return withdrawSessions.contains(player);
    }
}
