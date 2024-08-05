package gg.petrushka.bankcards.listener;

import gg.petrushka.bankcards.service.Config;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.transfer.DepositManager;
import gg.petrushka.bankcards.transfer.TransferManager;
import gg.petrushka.bankcards.transfer.WithdrawManager;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {


    private ConfigData configData;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e){
        Player player = e.getPlayer();
        if(DepositManager.isInDepositState(player)){
            e.setCancelled(true);
            String message = e.getMessage();
            if (message.equalsIgnoreCase("cancel")){
                DepositManager.removePlayer(player);
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(message);
            } catch (NumberFormatException ignored){
                player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.wrong-number")));
                return;
            }
            DepositManager.depositMoney(player, amount);
            return;
        }
        if(WithdrawManager.isInWithdrawState(player)){
            e.setCancelled(true);
            String message = e.getMessage();
            if (message.equalsIgnoreCase("cancel")){
                WithdrawManager.removePlayer(player);
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(message);
            } catch (NumberFormatException ignored){
                player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.wrong-number")));
                return;
            }
            WithdrawManager.withdrawMoney(player, amount);
            return;
        }
        if(TransferManager.isInTransfer(player)){
            e.setCancelled(true);
            String message = e.getMessage();
            if (message.equalsIgnoreCase("cancel")){
                TransferManager.removePlayer(player);
                return;
            }
            int amount;
            try {
                amount = Integer.parseInt(message);
            } catch (NumberFormatException ignored){
                player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.wrong-number")));
                return;
            }
            TransferManager.transferMoney(player, amount);
            return;
        }
    }

    public ChatListener(ConfigData configData){
        this.configData = configData;
    }
}
