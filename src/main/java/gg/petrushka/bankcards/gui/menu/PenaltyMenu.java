package gg.petrushka.bankcards.gui.menu;

import gg.petrushka.bankcards.BankCards;
import gg.petrushka.bankcards.card.BankCard;
import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.gui.button.GuiElement;
import gg.petrushka.bankcards.gui.model.Gui;
import gg.petrushka.bankcards.item.ItemBuilder;
import gg.petrushka.bankcards.penalty.Penalty;
import gg.petrushka.bankcards.penalty.PenaltyManager;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PenaltyMenu extends Gui {


    private ConfigData configData;
    private ConfigData guiData;
    private CardService cardService;
    private Player player;
    private int page;

    private final List<GuiElement> penaltyIcons = new ArrayList<>();
    private GuiElement border;

    private GuiElement backArrow;
    private GuiElement continueArrow;

    private List<Penalty> penalties = new ArrayList<>();

    public PenaltyMenu(Player player) {
        super(player, 27);
        this.configData = BankCards.getInstance().getConfigData();
        this.guiData = BankCards.getInstance().getGuiData();
        this.cardService = BankCards.getInstance().getCardService();
        setTitle(ColorUtil.colorizeString((String) guiData.getData("gui.penalty.title")));
        if(cardService.getCard(player) == null) return;
        init();
    }

    @Override
    public void init() {
        super.init();
        player = getPlayer();
        penalties = PenaltyManager.getPlayerPenalties(player);
        createPenaltyList(cardService.getCard(player));
        fillGui();
        createArrows();
        initGuiContent();
    }

    private void fillGui(){
        int[] slots = new int[]{19, 20, 21, 22, 23, 24, 25};
        border = new GuiElement(new ItemBuilder(Material.PURPLE_STAINED_GLASS_PANE)
                .setName(" ")
                .build());
        setItem(slots, border);
    }

    private void createArrows(){
        backArrow = new GuiElement(new ItemBuilder(Material.valueOf((String) guiData.getData("gui.penalty.back-arrow.material")))
                .setName(ColorUtil.colorizeString((String) guiData.getData("gui.penalty.back-arrow.title")))
                .setLore(ColorUtil.colorizeList((List<String>) guiData.getData("gui.penalty.back-arrow.lore")))
                .setCustomModelData((int) guiData.getData("gui.penalty.back-arrow.custom-model-data"))
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
        continueArrow = new GuiElement(new ItemBuilder(Material.valueOf((String) guiData.getData("gui.penalty.back-arrow.material")))
                .setName(ColorUtil.colorizeString((String) guiData.getData("gui.penalty.continue-arrow.title")))
                .setLore(ColorUtil.colorizeList((List<String>) guiData.getData("gui.penalty.continue-arrow.lore")))
                .setCustomModelData((int) guiData.getData("gui.penalty.continue-arrow.custom-model-data"))
                .build());
        continueArrow.setClickHandler(event -> {
            if(page < penaltyIcons.size() / 18) {
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
            if(penaltyIcons.size() <= j) break;
            if(penaltyIcons.get(j) == null) continue;
            setItem(slot, penaltyIcons.get(j));
            slot++;
        }
        if(page != 0) setItem(18, backArrow);
        if(page < (penaltyIcons.size() - 1) / 18) setItem(26, continueArrow);
    }

    private void createPenaltyList(BankCard playerCard){
        for(Penalty penalty : penalties){
            List<String> lore = (List<String>) guiData.getData("gui.penalty.penalty-item.lore");
            List<String> newLore = new ArrayList<>();
            lore.forEach(s ->
                    newLore.add(s.replaceAll("\\{REASON}", penalty.getReason())
                            .replaceAll("\\{PENALTY-SIZE}", String.valueOf(penalty.getAmount()))
                            .replaceAll("\\{HOURS}", String.valueOf(penalty.getTime() / 3600))
                            .replaceAll("\\{MINUTES}", String.valueOf((penalty.getTime() % 3600) / 60))
                            .replaceAll("\\{SECONDS}", String.valueOf(penalty.getTime() % 60)))
            );
            ItemStack stack = new ItemBuilder(Material.valueOf((String) guiData.getData("gui.penalty.penalty-item.material")))
                    .setName(ColorUtil.colorizeString((String) guiData.getData("gui.penalty.penalty-item.title")))
                    .setLore(ColorUtil.colorizeList(newLore))
                    .setCustomModelData((int) guiData.getData("gui.penalty.penalty-item.custom-model-data"))
                    .build();
            GuiElement element = new GuiElement(stack);
            element.setClickHandler(event -> {
                int balance = playerCard.getBalance();
                if(penalty.getAmount() <= balance){
                    playerCard.addBalance(-penalty.getAmount());
                    PenaltyManager.removePenalty(player, penalty);
                    player.closeInventory();
                    player.playSound(player.getLocation(), (String) guiData.getData("gui.penalty.penalty-item.sound"), (int) guiData.getData("gui.penalty.penalty-item.sound-volume"), (int) guiData.getData("gui.penalty.penalty-item.sound-pitch"));
                    player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.penalty-paid")));
                } else {
                    player.closeInventory();
                    player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.not-enough-virtual-balance")));
                }
            });
            penaltyIcons.add(element);
        }
    }
}
