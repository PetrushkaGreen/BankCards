package gg.petrushka.bankcards.card.service;

import gg.petrushka.bankcards.card.BankCard;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

public interface CardService {

    void addCard(BankCard card);

    BankCard getCard(String code);

    BankCard getCard(Player player);

    BankCard getCard(UUID uuid);

    void deleteCard(BankCard bankCard);

    void createCard(Player player);

    List<BankCard> getCards();
}
