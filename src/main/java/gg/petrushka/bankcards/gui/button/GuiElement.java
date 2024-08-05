package gg.petrushka.bankcards.gui.button;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class GuiElement {


    @Getter @Setter
    private ClickHandler<InventoryClickEvent> clickHandler;

    @Getter @Setter
    private ItemStack guiItem;

    public GuiElement(ItemStack guiItem){
        this.guiItem = guiItem;
    }


}
