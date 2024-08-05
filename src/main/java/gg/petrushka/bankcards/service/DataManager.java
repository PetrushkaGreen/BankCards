package gg.petrushka.bankcards.service;

import gg.petrushka.bankcards.card.BankCard;
import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.penalty.Penalty;
import gg.petrushka.bankcards.penalty.PenaltyManager;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class DataManager {

    public static void saveData(CardService cardService){
        ConfigManager.setObject("data" + File.separator + "data.yml", "cards", " ");
        ConfigManager.setObject("data" + File.separator + "data.yml", "penalties", " ");
        for(BankCard card : cardService.getCards()){
            String code = card.getCode();
            ConfigManager.setObject("data" + File.separator + "data.yml", "cards." + code + ".balance", card.getBalance());
            ConfigManager.setObject("data" + File.separator + "data.yml", "cards." + code + ".uuid", card.getUserId().toString());
            ConfigManager.setObject("data" + File.separator + "data.yml", "cards." + code + ".player-name", card.getPlayerName());
        }
        for(UUID uuid : PenaltyManager.getPlayerPenaltyMap().keySet()){
            List<Penalty> penalties = PenaltyManager.getPlayerPenalties(uuid);
            int a = 0;
            for(Penalty penalty : penalties){
                ConfigManager.setObject("data" + File.separator + "data.yml", "penalties." + uuid + ".penalty-" + a + ".amount", penalty.getAmount());
                ConfigManager.setObject("data" + File.separator + "data.yml", "penalties." + uuid + ".penalty-" + a + ".time", penalty.getTime());
                ConfigManager.setObject("data" + File.separator + "data.yml", "penalties." + uuid + ".penalty-" + a + ".reason", penalty.getReason());
                a++;
            }
        }
        ConfigManager.saveConfig("data" + File.separator + "data.yml");
    }

    public static void loadData(CardService cardService){
        cardService.getCards().clear();
        PenaltyManager.getPlayerPenaltyMap().clear();
        if(ConfigManager.getConfigurationsection("data" + File.separator + "data.yml", "cards") != null) {
            for(String key : ConfigManager.getConfigKeys("data" + File.separator + "data.yml", "cards")){
                BankCard card = new BankCard(key, UUID.fromString(ConfigManager.getString("data" + File.separator + "data.yml", "cards." + key + ".uuid")));
                card.setBalance(ConfigManager.getInt("data" + File.separator + "data.yml", "cards." + key + ".balance"));
                card.setPlayerName(ConfigManager.getString("data" + File.separator + "data.yml", "cards." + key + ".player-name"));
                cardService.addCard(card);
            }
        }
        if(ConfigManager.getConfigurationsection("data" + File.separator + "data.yml", "penalties") != null) {
            for(String key : ConfigManager.getConfigKeys("data" + File.separator + "data.yml", "penalties")) {
                for (String penaltyKey : ConfigManager.getConfigKeys("data" + File.separator + "data.yml", "penalties." + key)) {
                    Penalty penalty = new Penalty();
                    penalty.setAmount(ConfigManager.getInt("data" + File.separator + "data.yml", "penalties." + key + "." + penaltyKey + ".amount"));
                    penalty.setTime(ConfigManager.getInt("data" + File.separator + "data.yml", "penalties." + key + "." + penaltyKey + ".time"));
                    penalty.setReason(ConfigManager.getString("data" + File.separator + "data.yml", "penalties." + key + "." + penaltyKey + ".reason"));
                    penalty.setPlayerId(UUID.fromString(key));
                    PenaltyManager.addPenalty(UUID.fromString(key), penalty);
                }
            }
        }
    }
}
