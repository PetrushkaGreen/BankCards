package gg.petrushka.bankcards.gui.model;

import gg.petrushka.bankcards.gui.button.GuiElement;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

import java.util.List;

public interface IGui {

    int getSize();


    String getTitle();

    GuiElement[] getContent();

    void onClick(InventoryClickEvent e);

    void reopen();
    Player getPlayer();

    void open();

    void init();

    Inventory getInventory();
}
