package gg.petrushka.bankcards.card;

import gg.petrushka.bankcards.penalty.PenaltyManager;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class BankCard {

    @Getter
    private final String code;

    @Getter @Setter
    private int balance;


    @Getter
    private final UUID userId;
    @Getter @Setter
    private String playerName;

    public BankCard(String code, Player player){
        this.code = code;
        this.userId = player.getUniqueId();
        this.playerName = player.getName();

        balance = 0;
    }

    public BankCard(String code, UUID userId){
        this.code = code;
        this.userId = userId;

        balance = 0;
    }

    public void addBalance(int value){
        balance += value;
    }


    public int getAllPenalties(Player penaltyMember){
        AtomicInteger size = new AtomicInteger();
        PenaltyManager.getPlayerPenalties(penaltyMember).forEach(penalty -> size.addAndGet(penalty.getAmount()));
        return size.get();
    }

}
