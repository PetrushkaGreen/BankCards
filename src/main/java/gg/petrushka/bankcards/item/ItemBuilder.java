package gg.petrushka.bankcards.item;


import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemBuilder {


    private ItemStack item;

    private ItemMeta meta;

    public ItemBuilder(Material material) {
        this.item = new ItemStack(material);
        this.meta = item.getItemMeta();
    }

    public ItemBuilder setName(String name) {
        meta.setDisplayName(name);
        return this;
    }

    public <T, Z> ItemBuilder addPersistentData(String key, PersistentDataType<T, Z> persistentDataType, Z value){
        meta.getPersistentDataContainer().set(NamespacedKey.fromString(key), persistentDataType, value);
        return this;
    }

    public ItemBuilder setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemBuilder setLore(List<String> lore) {
        meta.setLore(lore);
        return this;
    }

    public ItemBuilder setCustomModelData(int customModelData){
        meta.setCustomModelData(customModelData);
        return this;
    }

    public ItemBuilder addLore(List<String> lore) {
        List<String> itemLore = meta.getLore();

        if (itemLore == null) itemLore = new ArrayList<>();

        itemLore.addAll(lore);
        meta.setLore(itemLore);
        return this;
    }

    public ItemStack build() {
        item.setItemMeta(meta);
        return item;
    }
}
