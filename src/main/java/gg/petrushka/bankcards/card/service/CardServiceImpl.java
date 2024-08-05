package gg.petrushka.bankcards.card.service;

import gg.petrushka.bankcards.card.BankCard;
import gg.petrushka.bankcards.item.ItemManager;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import gg.petrushka.bankcards.util.RandomUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CardServiceImpl implements CardService {

    @Getter
    private final List<BankCard> cards = new ArrayList<>();

    private final ConfigData configData;

    public CardServiceImpl(ConfigData data){
        this.configData = data;
    }

    @Override
    public void addCard(BankCard card) {
        cards.add(card);
    }

    @Override
    public BankCard getCard(String code) {
        return cards.stream().filter(card -> card.getCode().equals(code)).findFirst().orElse(null);
    }

    @Override
    public BankCard getCard(Player player) {
        return cards.stream().filter(card -> card.getUserId().equals(player.getUniqueId())).findFirst().orElse(null);
    }

    @Override
    public BankCard getCard(UUID uuid) {
        return cards.stream().filter(card -> card.getUserId().equals(uuid)).findFirst().orElse(null);
    }

    @Override
    public void deleteCard(BankCard bankCard) {
        cards.remove(bankCard);
        Player owner = Bukkit.getPlayer(bankCard.getUserId());
        if(owner != null){
            owner.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.card-deleted")));
        }
        bankCard = null;
    }

    @Override
    public void createCard(Player player) {
        if(getCard(player) != null){
            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.already-has-card")));
            return;
        }
        BankCard card = new BankCard(RandomUtil.generateCode(), player);

        ItemStack cardItem = ItemManager.createBankCard(card);
        addCard(card);

        player.getInventory().addItem(cardItem);
        String message = ((String) configData.getData("messages.card-created")).replaceAll("\\{CARD-NUMBER}", card.getCode());
        player.sendMessage(ColorUtil.colorizeString(message));
    }
}
