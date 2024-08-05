package gg.petrushka.bankcards.service;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nullable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class ConfigManager {

    public static List<Config> configs = new ArrayList<>();

    @Getter @Setter
    private static JavaPlugin plugin;

    /**
     * Получение объекта по пути конфига и по самой к объекту.
     *
     * @param filePath Путь к конфигу.
     * @param path Путь к объекту.
     * @return {@link Object} из указанного конфига и пути. Может вернуть {@code null} если объект или конфиг не найден.
     */
    public static @Nullable Object getByConfig(String filePath, String path){
        Config config = getConfigByPath(filePath);
        if(config == null) return null;
        return config.config.get(path);
    }

    public static @Nullable List<String> getStringList(String filePath, String path){
        Config config = getConfigByPath(filePath);
        if(config == null) return null;
        return config.config.getStringList(path);
    }

    public static void saveConfigs() {
        configs.forEach(Config::saveConfigFile);
    }

    public static void saveConfig(String path){
        Config config = getConfigByPath(path);
        if(config == null) return;
        config.saveConfigFile();
    }

    public static @Nullable Config getConfigByPath(String path){
        if(path == null || path.isEmpty()) return null;
        return configs.stream().filter(config -> config.configFile.getPath().equals(plugin.getDataFolder() + File.separator +  path)).findFirst().orElse(null);
    }

    public static @Nullable String getString(String filePath, String path){
        Object o = getByConfig(filePath, path);
        if(o instanceof String s) {
            return s;
        }
        return null;
    }

    public static int getInt(String filePath, String path){
        Object o = getByConfig(filePath, path);
        if(o instanceof Integer i) {
            return i;
        }
        return 0;
    }

    public static double getDouble(String filePath, String path){
        Object o = getByConfig(filePath, path);
        if(o instanceof Double d) {
            return d;
        }
        return 0;
    }

    public static @Nullable ConfigurationSection getConfigurationsection(String filePath, String path){
       Config config = configs.stream().filter(cfg -> cfg.configFile.getPath().equals(plugin.getDataFolder() + File.separator + filePath)).findFirst().orElse(null);
       if(config == null) return null;
       return config.config.getConfigurationSection(path);
    }

    public static Set<String> getConfigKeys(String filePath, String path){
        return getConfigurationsection(filePath, path).getKeys(false);
    }

    public static void createConfig(String fileName){
        File file = new File(plugin.getDataFolder() + File.separator, fileName);
        Config config = new Config(plugin);
        config.setupConfig(file);
        configs.add(config);
    }

    public static void createConfig(String fileName, String parent){
        File folder = new File(plugin.getDataFolder(), parent);
        if(!folder.exists()){
            folder.mkdirs();
        }
        File file = new File(folder, fileName);
        Config config = new Config(plugin);
        config.setupConfigWithFolder(file, parent);
        configs.add(config);
    }

    public static void setObject(String filePath, String path, Object value){
        for(Config cfg : configs){
            if(cfg.configFile.getPath().equals(plugin.getDataFolder() + File.separator + filePath)){
                cfg.config.set(path, value);
            }
        }
    }

}

