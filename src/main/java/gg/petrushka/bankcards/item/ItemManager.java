package gg.petrushka.bankcards.item;

import gg.petrushka.bankcards.card.BankCard;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {


    private static ConfigData configData;

    public static void init(ConfigData data){
        configData = data;
    }

    public static ItemStack createBankCard(BankCard card){
        List<String> lore = (List<String>) configData.getData("items.card-item.lore");
        List<String> newLore = new ArrayList<>();
        lore.forEach(s ->
                newLore.add(s.replaceAll("\\{CARD-NUMBER}", card.getCode()))

        );
        return new ItemBuilder(Material.valueOf((String) configData.getData("items.card-item.material")))
                .setName(ColorUtil.colorizeString((String) configData.getData("items.card-item.title")))
                .setLore(ColorUtil.colorizeList(newLore))
                .setAmount(1)
                .addPersistentData("card-number", PersistentDataType.STRING, card.getCode())
                .setCustomModelData((int) configData.getData("items.card-item.custom-model-data"))
                .build();
    }

    public static int getPhysicalCurrency(Player player){
        int amount = 0;
        for(ItemStack stack : player.getInventory().getContents()){
            if(stack == null) continue;
            if(!stack.hasItemMeta()) continue;
            PersistentDataContainer container = stack.getItemMeta().getPersistentDataContainer();
            if(!container.has(NamespacedKey.fromString("bank-item-id"), PersistentDataType.STRING)) continue;
            amount += stack.getAmount();
        }
        return amount;
    }

    public static void addMoneyStack(Player player, int amount){
        ItemStack stack = new ItemBuilder(Material.valueOf((String) configData.getData("items.money.material")))
                .setName(ColorUtil.colorizeString((String) configData.getData("items.money.title")))
                .setLore(ColorUtil.colorizeList((List<String>) configData.getData("items.money.lore")))
                .setAmount(amount)
                .setCustomModelData((int) configData.getData("items.money.custom-model-data"))
                .addPersistentData("bank-item-id", PersistentDataType.STRING, "money")
                .build();
        player.getInventory().addItem(stack);
    }

    public static ItemStack getMoneyStack(int amount){
        return new ItemBuilder(Material.valueOf((String) configData.getData("items.money.material")))
                .setName(ColorUtil.colorizeString((String) configData.getData("items.money.title")))
                .setLore(ColorUtil.colorizeList((List<String>) configData.getData("items.money.lore")))
                .setAmount(amount)
                .setCustomModelData((int) configData.getData("items.money.custom-model-data"))
                .addPersistentData("bank-item-id", PersistentDataType.STRING, "money")
                .build();
    }

    public static ItemStack getMoneyStack(){
        return new ItemBuilder(Material.valueOf((String) configData.getData("items.money.material")))
                .setName(ColorUtil.colorizeString((String) configData.getData("items.money.title")))
                .setLore(ColorUtil.colorizeList((List<String>) configData.getData("items.money.lore")))
                .setCustomModelData((int) configData.getData("items.money.custom-model-data"))
                .addPersistentData("bank-item-id", PersistentDataType.STRING, "money")
                .build();
    }

    public static void removeMoney(Player player, int amount){
        int a = amount;
        for(ItemStack itemStack : player.getInventory().getContents()){
            if(itemStack == null || !itemStack.hasItemMeta()) continue;
            if(!itemStack.getItemMeta().getPersistentDataContainer().has(NamespacedKey.fromString("bank-item-id"), PersistentDataType.STRING)) continue;
            int itemStackAmount = itemStack.getAmount();
            if(itemStackAmount > a){
                itemStack.setAmount(itemStackAmount - a);
                return;
            } else {
                itemStack.setAmount(itemStackAmount - a);
                a -= itemStackAmount;
            }
        }
    }
}
