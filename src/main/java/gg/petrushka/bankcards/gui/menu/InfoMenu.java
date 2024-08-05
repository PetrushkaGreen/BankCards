package gg.petrushka.bankcards.gui.menu;

import gg.petrushka.bankcards.BankCards;
import gg.petrushka.bankcards.card.BankCard;
import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.gui.button.GuiElement;
import gg.petrushka.bankcards.gui.model.Gui;
import gg.petrushka.bankcards.item.ItemBuilder;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class InfoMenu extends Gui {

    private ConfigData configData;
    private ConfigData guiData;
    private CardService cardService;
    private Player player;

    public InfoMenu(Player player) {
        super(player, 27);
        this.configData = BankCards.getInstance().getConfigData();
        this.guiData = BankCards.getInstance().getGuiData();
        this.cardService = BankCards.getInstance().getCardService();
        setTitle(ColorUtil.colorizeString((String) guiData.getData("gui.info.title")));
        if(cardService.getCard(player) == null) return;
        init();
    }

    @Override
    public void init() {
        super.init();
        player = getPlayer();
        fillGui();
        createInfo(cardService.getCard(getPlayer()));
        createClickButton();
    }

    private void fillGui(){
        int[] slots = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 10, 11, 13, 15, 16, 17,
                18, 19, 20, 21, 22, 23, 24, 25, 26};
        ItemStack fillStack = new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE)
                .setName("")
                .build();
        setItem(slots, new GuiElement(fillStack));
    }

    private void createInfo(BankCard card){
        List<String> lore = (List<String>) guiData.getData("gui.info.player-head.lore");
        List<String> newLore = new ArrayList<>();
        lore.forEach(s ->
                newLore.add(s.replaceAll("\\{CARD-NUMBER}", card.getCode())
                        .replaceAll("\\{PLAYER-NAME}", player.getName())
                        .replaceAll("\\{CARD-BALANCE}", String.valueOf(card.getBalance()))
                        .replaceAll("\\{PENALTY-SIZE}", String.valueOf(card.getAllPenalties(player))))
        );
        ItemStack description = new ItemBuilder(Material.PLAYER_HEAD)
                .setName(ColorUtil.colorizeString((String) guiData.getData("gui.info.player-head.title")))
                .setLore(ColorUtil.colorizeList(newLore))
                .build();
        GuiElement descriptionElement = new GuiElement(description);
        setItem(12, descriptionElement);
    }

    private void createClickButton(){
        ItemStack deposit = new ItemBuilder(Material.valueOf((String) guiData.getData("gui.info.menu-button.material")))
                .setName(ColorUtil.colorizeString((String) guiData.getData("gui.info.menu-button.title")))
                .setLore(ColorUtil.colorizeList((List<String>) guiData.getData("gui.info.menu-button.lore")))
                .setCustomModelData((int) guiData.getData("gui.info.menu-button.custom-model-data"))
                .build();
        GuiElement depositElement = new GuiElement(deposit);
        depositElement.setClickHandler(event -> {
            player.closeInventory();
            BankMenu menu = new BankMenu(player);
            menu.open();
            player.playSound(player.getLocation(), (String) guiData.getData("gui.info.menu-button.sound"), (int) guiData.getData("gui.info.menu-button.sound-volume"), (int) guiData.getData("gui.info.menu-button.sound-pitch"));
        });
        setItem(14, depositElement);
    }

}
