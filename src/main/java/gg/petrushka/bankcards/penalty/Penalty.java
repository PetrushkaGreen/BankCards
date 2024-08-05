package gg.petrushka.bankcards.penalty;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class Penalty {

    @Getter @Setter
    private String reason;
    @Getter @Setter
    private int amount;
    @Getter @Setter
    private int time;

    @Getter @Setter
    private UUID playerId;

}
