package gg.petrushka.bankcards.command.model;




import gg.petrushka.bankcards.service.ConfigData;
import gg.petrushka.bankcards.util.ColorUtil;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SubCommandImpl implements SubCommand{

    private String permission;

    private String usage;

    private String name;

    private final boolean forPlayers;
    private String description;
    private ConfigData configData;

    public SubCommandImpl(String name, boolean forPlayers, ConfigData data){
        this.name = name;
        this.forPlayers = forPlayers;
        this.configData = data;
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(forPlayers && !(sender instanceof Player)){
            String message = ColorUtil.colorizeString("&f[&bFrameColiseum&f] #42AAFFЭта команда только для игроков!");
            sender.sendMessage(message);
            return true;
        }
        if(!sender.hasPermission(getPermission()) || !sender.hasPermission("coliseum.*")){
            String message = ColorUtil.colorizeString((String) configData.getData("messages.no-permissions"));
            sender.sendMessage(message);
            return true;
        }

        return false;
    }

    @Override
    public String getUsage() {
        return usage;
    }

    @Override
    public void setUsage(String usage) {
        this.usage = usage;
    }

    @Override
    public String getPermission() {
        return permission;
    }

    @Override
    public void setPermission(String permission) {
        this.permission = permission;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(String description) {
        this.description = description;
    }


}
