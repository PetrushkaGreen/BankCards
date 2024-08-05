package gg.petrushka.bankcards.service;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;

public class ConfigData {

    private final FileConfiguration configuration;

    private final Map<String, Object> hash;

    public ConfigData(FileConfiguration config) {
        configuration = config;
        hash = new HashMap<>();
        configuration.getKeys(true).forEach(key -> hash.put(key, configuration.get(key)));
    }

    public Object getData(String key){
        return hash.getOrDefault(key, null);
    }

}
