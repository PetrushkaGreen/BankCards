package gg.petrushka.bankcards.listener;

import gg.petrushka.bankcards.gui.menu.BankMenu;
import gg.petrushka.bankcards.gui.menu.InfoMenu;
import gg.petrushka.bankcards.gui.model.IGui;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Player player = e.getPlayer();
        if(!player.isOnline()) return;
        ItemStack stack = player.getInventory().getItemInMainHand();
        if(!stack.hasItemMeta()) return;
        PersistentDataContainer dataContainer = stack.getItemMeta().getPersistentDataContainer();
        if(!dataContainer.has(NamespacedKey.fromString("card-number"), PersistentDataType.STRING)) return;
        IGui gui = new InfoMenu(player);
        gui.open();
    }
}
