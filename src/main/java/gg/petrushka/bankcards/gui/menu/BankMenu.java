package gg.petrushka.bankcards.gui.menu;

import gg.petrushka.bankcards.BankCards;
import gg.petrushka.bankcards.card.BankCard;
import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.gui.button.GuiElement;
import gg.petrushka.bankcards.gui.model.Gui;
import gg.petrushka.bankcards.item.ItemBuilder;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.transfer.DepositManager;
import gg.petrushka.bankcards.transfer.TransferManager;
import gg.petrushka.bankcards.transfer.WithdrawManager;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class BankMenu extends Gui {

    private ConfigData configData;
    private ConfigData guiData;
    private CardService cardService;

    private Player player;

    public BankMenu(Player player) {
        super(player, 27);
        this.configData = BankCards.getInstance().getConfigData();
        this.guiData = BankCards.getInstance().getGuiData();
        this.cardService = BankCards.getInstance().getCardService();
        setTitle(ColorUtil.colorizeString((String) guiData.getData("gui.bank.title")));
        if(cardService.getCard(player) == null) return;
        init();
    }

    @Override
    public void init() {
        super.init();
        this.player = getPlayer();
        fillGui();
        withdrawItem();
        depositItem();
        transferItem();
        penaltyItem();
    }

    private void fillGui(){
        int[] slots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8,
                                9, 11, 13, 15, 17,
                                18, 19, 20, 21, 22, 23, 24, 25, 26};
        ItemStack fillStack = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        setItem(slots, new GuiElement(fillStack));
    }

    private void withdrawItem(){
        ItemStack withdraw = new ItemBuilder(Material.valueOf((String) guiData.getData("gui.bank.withdraw.material")))
                .setName(ColorUtil.colorizeString((String) guiData.getData("gui.bank.withdraw.title")))
                .setLore(ColorUtil.colorizeList((List<String>) guiData.getData("gui.bank.withdraw.lore")))
                .setCustomModelData((int) guiData.getData("gui.bank.withdraw.custom-model-data"))
                .build();
        GuiElement withdrawElement = new GuiElement(withdraw);
        withdrawElement.setClickHandler(event -> {
            player.closeInventory();

            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.withdraw")));
            player.playSound(player.getLocation(), (String) guiData.getData("gui.bank.withdraw.sound"), (int) guiData.getData("gui.bank.withdraw.sound-volume"), (int) guiData.getData("gui.bank.withdraw.sound-pitch"));

            if(DepositManager.isInDepositState(player)) DepositManager.removePlayer(player);
            if(TransferManager.isInTransfer(player)) TransferManager.removePlayer(player);
            if(!WithdrawManager.isInWithdrawState(player)) WithdrawManager.addPlayer(player);
        });
        setItem(10, withdrawElement);

    }

    private void depositItem(){
        ItemStack deposit = new ItemBuilder(Material.valueOf((String) guiData.getData("gui.bank.deposit.material")))
                .setName(ColorUtil.colorizeString((String) guiData.getData("gui.bank.deposit.title")))
                .setLore(ColorUtil.colorizeList((List<String>) guiData.getData("gui.bank.deposit.lore")))
                .setCustomModelData((int) guiData.getData("gui.bank.deposit.custom-model-data"))
                .build();
        GuiElement depositElement = new GuiElement(deposit);
        depositElement.setClickHandler(event -> {
            player.closeInventory();

            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.deposit")));
            player.playSound(player.getLocation(), (String) guiData.getData("gui.bank.deposit.sound"), (int) guiData.getData("gui.bank.deposit.sound-volume"), (int) guiData.getData("gui.bank.deposit.sound-pitch"));

            if(WithdrawManager.isInWithdrawState(player)) WithdrawManager.removePlayer(player);
            if(TransferManager.isInTransfer(player)) TransferManager.removePlayer(player);
            if(!DepositManager.isInDepositState(player))  DepositManager.addPlayer(player);
        });
        setItem(12, depositElement);
    }

    public void transferItem(){
        ItemStack transfer = new ItemBuilder(Material.valueOf((String) guiData.getData("gui.bank.transfer.material")))
                .setName(ColorUtil.colorizeString((String) guiData.getData("gui.bank.transfer.title")))
                .setLore(ColorUtil.colorizeList((List<String>) guiData.getData("gui.bank.transfer.lore")))
                .setCustomModelData((int) guiData.getData("gui.bank.transfer.custom-model-data"))
                .build();
        GuiElement transferElement = new GuiElement(transfer);
        transferElement.setClickHandler(event -> {
            player.closeInventory();
            TransferMenu menu = new TransferMenu(player);
            menu.open();
            player.playSound(player.getLocation(), (String) guiData.getData("gui.bank.deposit.sound"), (int) guiData.getData("gui.bank.transfer.sound-volume"), (int) guiData.getData("gui.bank.transfer.sound-pitch"));
        });
        setItem(14, transferElement);
    }

    private void penaltyItem(){
        ItemStack penalty = new ItemBuilder(Material.valueOf((String) guiData.getData("gui.bank.penalty.material")))
                .setName(ColorUtil.colorizeString((String) guiData.getData("gui.bank.penalty.title")))
                .setLore(ColorUtil.colorizeList((List<String>) guiData.getData("gui.bank.penalty.lore")))
                .setCustomModelData((int) guiData.getData("gui.bank.penalty.custom-model-data"))
                .build();
        GuiElement penaltyElement = new GuiElement(penalty);
        penaltyElement.setClickHandler(event -> {
            player.closeInventory();
            PenaltyMenu menu = new PenaltyMenu(player);
            menu.open();
            player.playSound(player.getLocation(), (String) guiData.getData("gui.bank.penalty.sound"), (int) guiData.getData("gui.bank.penalty.sound-volume"), (int) guiData.getData("gui.bank.penalty.sound-pitch"));
        });
        setItem(16, penaltyElement);
    }


}
