package gg.petrushka.bankcards.transfer;

import gg.petrushka.bankcards.card.BankCard;
import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class TransferManager {
    @Setter
    private static CardService cardService;
    @Setter
    private static ConfigData configData;

    private static final Map<Player, Player> transferSessions = new HashMap<>();

    public static void newSession(Player sender, Player recipient){
        transferSessions.put(sender, recipient);
    }

    public static void removePlayer(Player player){
        transferSessions.remove(player);
    }

    public static void transferMoney(Player sender, int value){
        @NotNull BankCard senderCard = cardService.getCard(sender);
        Player recipient = transferSessions.get(sender);
        if(recipient == null || !recipient.isOnline()){
            sender.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.wrong-number")));
            return;
        }
        BankCard recipientCard = cardService.getCard(recipient);
        if(recipientCard == null){
            sender.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.no-recipient-card")));
            return;
        }
        if(senderCard.getBalance() < value){
            sender.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.not-enough-virtual-balance")));
            return;
        }
        senderCard.addBalance(-value);
        recipientCard.addBalance(value);
        removePlayer(sender);

        sender.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.transfer-successful-sender"))
                .replaceAll("\\{TRANSFER-COUNT}", String.valueOf(value))
                .replaceAll("\\{TARGET-NAME}", recipient.getName()));

        recipient.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.transfer-successful-recipient"))
                .replaceAll("\\{TRANSFER-COUNT}", String.valueOf(value))
                .replaceAll("\\{SENDER-NAME}", sender.getName()));
    }

    public static boolean isInTransfer(Player player){
        return transferSessions.containsKey(player);
    }

}
