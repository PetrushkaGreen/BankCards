package gg.petrushka.bankcards.command.subcommand.bank;

import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.command.model.SubCommandImpl;
import gg.petrushka.bankcards.service.ConfigData;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Create extends SubCommandImpl {

    private CardService cardService;

    public Create(String name, boolean forPlayers, ConfigData data, CardService service) {
        super(name, forPlayers, data);
        this.cardService = service;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        boolean a = super.execute(sender, args);
        if(a) return true;
        Player player = (Player) sender;
        cardService.createCard(player);
        return false;
    }
}
