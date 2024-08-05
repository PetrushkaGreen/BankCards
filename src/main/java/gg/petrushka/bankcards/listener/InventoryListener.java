package gg.petrushka.bankcards.listener;

import gg.petrushka.bankcards.gui.model.IGui;
import gg.petrushka.bankcards.gui.service.GuiService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class InventoryListener implements Listener {

    private final GuiService guiService;

    @EventHandler
    public void onClick(InventoryClickEvent e){
        if(!(e.getWhoClicked() instanceof Player player)) return;
        if(e.getClickedInventory() == null) return;
        IGui gui = guiService.getGui(player);
        if(gui == null) return;
        if(!gui.getInventory().equals(e.getClickedInventory())) return;
        gui.onClick(e);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent e){
        if(!(e.getPlayer() instanceof Player player)) return;
        IGui gui = guiService.getGui(player);
        if(gui == null) return;
        guiService.removeGui(gui);
    }

    public InventoryListener(GuiService guiService){
        this.guiService = guiService;
    }
}
