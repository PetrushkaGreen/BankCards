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
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;


public class TransferMenu extends Gui {

    private ConfigData configData;
    private ConfigData guiData;
    private CardService cardService;
    private Player player;

    private int page;

    private List<Player> onlinePlayers;
    private final List<GuiElement> playerIcons = new ArrayList<>();
    private GuiElement border;

    private GuiElement backArrow;
    private GuiElement continueArrow;

    public TransferMenu(Player player) {
        super(player, 27);
        this.configData = BankCards.getInstance().getConfigData();
        this.guiData = BankCards.getInstance().getGuiData();
        this.cardService = BankCards.getInstance().getCardService();
        setTitle(ColorUtil.colorizeString((String) guiData.getData("gui.transfer.title")));
        if(cardService.getCard(player) == null) return;
        onlinePlayers = new ArrayList<>(Bukkit.getOnlinePlayers());
        init();
    }

    @Override
    public void init() {
        super.init();
        player = getPlayer();
        createPlayerList();
        fillGui();
        initGuiContent();
        createArrows();
    }

    private void fillGui(){
        int[] slots = new int[]{19, 20, 21, 22, 23, 24, 25};
        border = new GuiElement(new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE)
                .setName(" ")
                .build());
        setItem(slots, border);
    }

    private void createArrows(){
        backArrow = new GuiElement(new ItemBuilder(Material.valueOf((String) guiData.getData("gui.transfer.back-arrow.material")))
                .setName((String) guiData.getData("gui.transfer.back-arrow.title"))
                .setLore((List<String>) guiData.getData("gui.transfer.back-arrow.lore"))
                .setCustomModelData((int) guiData.getData("gui.transfer.back-arrow.custom-model-data"))
                .build());
        backArrow.setClickHandler(event -> {
            if(page != 0){
                page--;
                clearGui();
                fillGui();
                createArrows();
                initGuiContent();
            }
        });
        continueArrow = new GuiElement(new ItemBuilder(Material.valueOf((String) guiData.getData("gui.transfer.back-arrow.material")))
                .setName((String) guiData.getData("gui.transfer.continue-arrow.title"))
                .setLore((List<String>) guiData.getData("gui.transfer.continue-arrow.lore"))
                .setCustomModelData((int) guiData.getData("gui.transfer.continue-arrow.custom-model-data"))
                .build());
        continueArrow.setClickHandler(event -> {
            if(page < playerIcons.size() / 18){
                page++;
                clearGui();
                fillGui();
                createArrows();
                initGuiContent();
            }
        });
    }

    private void initGuiContent(){
        setItem(18, border);
        setItem(26, border);
        int i = 18 * page;
        int slot = 0;
        for(int j = i; j < i + 18; j++){
            if(playerIcons.size() <= j) break;
            if(playerIcons.get(j) == null) continue;
            setItem(slot, playerIcons.get(j));
            slot++;
        }
        if(page != 0) setItem(18, backArrow);
        if(page < (playerIcons.size() - 1) / 18) setItem(26, continueArrow);
    }

    private void createPlayerList(){
        for(Player online : onlinePlayers){
            if(online == null || player.equals(online)) continue;
            if(cardService.getCard(online) == null) continue;
            BankCard card = cardService.getCard(online);
            String name = ((String) guiData.getData("gui.transfer.player-head.title")).replaceAll("\\{PLAYER-NAME}", online.getName());
            List<String> lore = (List<String>) guiData.getData("gui.transfer.player-head.lore");
            List<String> newLore = new ArrayList<>();
            lore.forEach(s ->
                    newLore.add(s.replaceAll("\\{CARD-NUMBER}", card.getCode())
                            .replaceAll("\\{BALANCE}", String.valueOf(card.getBalance()))
                            .replaceAll("\\{PENALTY-SIZE}", String.valueOf(card.getAllPenalties(online))))
            );
            ItemStack stack = new ItemBuilder(Material.PLAYER_HEAD)
                    .setName(ColorUtil.colorizeString(name))
                    .setLore(ColorUtil.colorizeList(newLore))
                    .build();
            GuiElement element = new GuiElement(stack);
            element.setClickHandler(event -> {
                player.closeInventory();
                player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.transfer")));

                if(WithdrawManager.isInWithdrawState(player)) WithdrawManager.removePlayer(player);
                if(DepositManager.isInDepositState(player)) DepositManager.removePlayer(player);
                if(!TransferManager.isInTransfer(player))  TransferManager.newSession(player, online);
            });
            playerIcons.add(element);
        }
    }
}
