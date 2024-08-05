package gg.petrushka.bankcards.service;


import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public class Config {

    public YamlConfiguration config;
    public File configFile;

    private JavaPlugin plugin;

    public Config(JavaPlugin auction) {
        this.plugin = auction;
    }

    public void setupConfig(File configFile){
        this.configFile = configFile;
        loadConfig();

        if(!configFile.exists()) {
            plugin.saveResource(configFile.getName(), false);
            loadConfig();
        } else {
            loadConfig();
        }
    }

    public void setupConfigWithFolder(File configFile, String folder){
        this.configFile = configFile;
        loadConfig();
        if(!configFile.exists()) {
            plugin.saveResource(folder + File.separator + configFile.getName(), false);
            loadConfig();
        } else {
            loadConfig();
        }
    }


    private void loadConfig(){
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public void saveConfigContent(){

    }

    public final void saveConfigFile(){
        saveConfigContent();
        try {
            config.save(configFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
