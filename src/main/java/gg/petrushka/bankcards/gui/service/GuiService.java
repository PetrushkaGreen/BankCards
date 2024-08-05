package gg.petrushka.bankcards.gui.service;

import gg.petrushka.bankcards.gui.model.IGui;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;

public interface GuiService {

    void addGui(IGui gui);

    void removeGui(IGui gui);

    @Nullable
    IGui getGui(Player player);
}
