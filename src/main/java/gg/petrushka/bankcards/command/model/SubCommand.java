package gg.petrushka.bankcards.command.model;

import org.bukkit.command.CommandSender;

public interface SubCommand {

    boolean execute(CommandSender sender, String[] args);

    String getUsage();

    void setUsage(String usage);

    String getPermission();

    void setPermission(String permission);

    String getName();

    String getDescription();

    void setDescription(String description);
}
