package gg.petrushka.bankcards.gui.model;

import gg.petrushka.bankcards.BankCards;
import gg.petrushka.bankcards.gui.button.GuiElement;
import gg.petrushka.bankcards.gui.service.GuiService;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public abstract class Gui implements InventoryHolder, IGui {
    @Getter
    private Inventory inventory;

    private GuiElement[] contents;
    @Getter @Setter
    private String title;
    @Getter @Setter
    private int size;
    @Getter
    private final Player player;
    @Getter
    private final GuiService guiService;

    public Gui(Player player, int size){
        this.player = player;
        this.title = " ";
        this.size = size;
        guiService = BankCards.getInstance().getGuiService();
    }

    @Override
    public GuiElement[] getContent() {
        return contents;
    }

    @Override
    public void onClick(InventoryClickEvent e) {
        e.setCancelled(true);
        GuiElement element = getItem(e.getSlot());
        if(element == null || element.getClickHandler() == null) return;
        element.getClickHandler().onClick(e);
    }

    @Override
    public void reopen() {

    }

    @Override
    public void open() {
        player.openInventory(inventory);
        guiService.addGui(this);
    }

    @Override
    public void init(){
        inventory = Bukkit.createInventory(this, size, title);
        contents = new GuiElement[size];
    }

    public void addItem(GuiElement element){
        for(int i = 0; i < contents.length; i++){
            if(contents[i] == null){
                contents[i] = element;
                break;
            }
        }
        inventory.addItem(element.getGuiItem());
    }

    public void setItem(int slot, GuiElement element){
        contents[slot] = element;
        inventory.setItem(slot, element.getGuiItem());
    }

    public void setItem(int[] slots, GuiElement element){
        for(int i : slots){
            contents[i] = element;
            inventory.setItem(i ,element.getGuiItem());
        }

    }

    public void clearGui(){
        contents = new GuiElement[size];
        inventory.clear();
    }

    public GuiElement getItem(int slot){
        return contents[slot];
    }

    public void removeItem(int slot){
        contents[slot] = null;
        inventory.clear(slot);
    }

}
