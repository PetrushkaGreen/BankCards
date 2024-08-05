package gg.petrushka.bankcards.penalty;

import gg.petrushka.bankcards.BankCards;
import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

public class PenaltyManager {

    @Setter
    private static ConfigData configData;
    @Setter
    private static CardService cardService;

    @Getter
    private static final Map<UUID, List<Penalty>> playerPenaltyMap = new HashMap<>();

    public static void addPenalty(Player player, Penalty penalty){
        List<Penalty> penalties = getPlayerPenalties(player);
        penalties.add(penalty);
        playerPenaltyMap.put(player.getUniqueId(), penalties);
        player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.get-penalty")));
    }

    public static void addPenalty(UUID uuid, Penalty penalty){
        List<Penalty> penalties = getPlayerPenalties(uuid);
        penalties.add(penalty);
        playerPenaltyMap.put(uuid, penalties);
    }

    public static void removePenalty(Player player, Penalty penalty){
        List<Penalty> penalties = getPlayerPenalties(player);
        penalties.remove(penalty);
        playerPenaltyMap.put(player.getUniqueId(), penalties);
    }

    public static void removePenalty(UUID uuid, Penalty penalty){
        List<Penalty> penalties = getPlayerPenalties(uuid);
        penalties.remove(penalty);
        playerPenaltyMap.put(uuid, penalties);
    }

    public static List<Penalty> getPlayerPenalties(Player player){
        return playerPenaltyMap.getOrDefault(player.getUniqueId(), new ArrayList<>());
    }

    public static List<Penalty> getPlayerPenalties(UUID uuid){
        return playerPenaltyMap.getOrDefault(uuid, new ArrayList<>());
    }


    private static BukkitRunnable runnable;
    public static void startRunnable(){
        if(runnable != null){
            runnable.cancel();
        }

        runnable = new BukkitRunnable() {
            @Override
            public void run() {
                List<Penalty> penalties = playerPenaltyMap.values().stream()
                        .flatMap(List::stream)
                        .toList();
                for(Penalty penalty : penalties){
                    penalty.setTime(penalty.getTime() - 5);
                    if(penalty.getTime() <= 0){
                        Player player = Bukkit.getPlayer(penalty.getPlayerId());
                        if(player != null && player.isOnline()){
                            player.sendMessage(ColorUtil.colorizeString((String) configData.getData("messages.penalty-not-paid")));
                        }
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), ((String) configData.getData("card-settings.penalty-command"))
                                .replaceAll("\\{CARD-OWNER}", cardService.getCard(penalty.getPlayerId()).getPlayerName()));
                        removePenalty(penalty.getPlayerId(), penalty);
                    }
                }
            }
        };
        runnable.runTaskTimer(BankCards.getInstance(), 20, 100);
    }

}
