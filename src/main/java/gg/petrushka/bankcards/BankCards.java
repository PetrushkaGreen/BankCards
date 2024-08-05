package gg.petrushka.bankcards;

import gg.petrushka.bankcards.card.service.CardService;
import gg.petrushka.bankcards.card.service.CardServiceImpl;
import gg.petrushka.bankcards.command.BankCommand;
import gg.petrushka.bankcards.command.PenaltyCommand;
import gg.petrushka.bankcards.command.manager.SubCommandManager;
import gg.petrushka.bankcards.gui.service.GuiService;
import gg.petrushka.bankcards.gui.service.GuiServiceImpl;
import gg.petrushka.bankcards.item.ItemManager;
import gg.petrushka.bankcards.listener.ChatListener;
import gg.petrushka.bankcards.listener.InteractListener;
import gg.petrushka.bankcards.listener.InventoryListener;
import gg.petrushka.bankcards.penalty.PenaltyManager;
import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.service.ConfigManager;
import gg.petrushka.bankcards.service.DataManager;
import gg.petrushka.bankcards.transfer.DepositManager;
import gg.petrushka.bankcards.transfer.TransferManager;
import gg.petrushka.bankcards.transfer.WithdrawManager;
import gg.petrushka.bankcards.util.RandomUtil;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class BankCards extends JavaPlugin {

    @Getter
    private static BankCards instance;

    @Getter
    private ConfigData configData;
    @Getter
    private ConfigData guiData;


    @Getter
    private GuiService guiService;

    @Getter
    private CardService cardService;

    @Getter
    private SubCommandManager subCommandManager;

    @Override
    public void onEnable() {
        instance = this;

        ConfigManager.setPlugin(instance);
        configs();
        initConfigData();
        initGui();
        initCardService();

        DataManager.loadData(cardService);


        RandomUtil.setConfigData(configData);
        ItemManager.init(configData);

        initCommands();
        initEvents();
        PenaltyManager.startRunnable();
    }

    public void initConfigData() {
        configData = new ConfigData(ConfigManager.getConfigByPath("config.yml").config);
        guiData = new ConfigData(ConfigManager.getConfigByPath("gui.yml").config);
        WithdrawManager.setConfigData(configData);
        DepositManager.setConfigData(configData);
        TransferManager.setConfigData(configData);
        PenaltyManager.setConfigData(configData);
    }

    public void initCommands(){
        subCommandManager = new SubCommandManager();
        subCommandManager.createSubCommands(configData, cardService);
        BankCommand bankCommand = new BankCommand(configData);
        PenaltyCommand penaltyCommand = new PenaltyCommand(configData, cardService);
        getCommand("bank").setExecutor(bankCommand);
        getCommand("bank").setTabCompleter(bankCommand);
        getCommand("penalty").setExecutor(penaltyCommand);
    }

    public void initEvents(){
        Bukkit.getPluginManager().registerEvents(new InteractListener(), instance);
        Bukkit.getPluginManager().registerEvents(new InventoryListener(guiService), instance);
        Bukkit.getPluginManager().registerEvents(new ChatListener(configData), instance);
    }

    public void initGui(){
        guiService = new GuiServiceImpl();
    }

    public void initCardService(){
        cardService = new CardServiceImpl(configData);
        PenaltyManager.setCardService(cardService);
        WithdrawManager.setCardService(cardService);
        DepositManager.setCardService(cardService);
        TransferManager.setCardService(cardService);
    }

    @Override
    public void onDisable() {
        DataManager.saveData(cardService);
    }

    public void configs(){
        ConfigManager.configs.clear();
        ConfigManager.createConfig("config.yml");
        ConfigManager.createConfig("gui.yml");
        ConfigManager.createConfig("data.yml", "data");
    }
}
