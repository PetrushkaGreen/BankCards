package gg.petrushka.bankcards.gui.service;

import gg.petrushka.bankcards.gui.model.IGui;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class GuiServiceImpl implements GuiService{

    private final Map<UUID, IGui> guiMap = new HashMap<>();

    @Override
    public void addGui(IGui gui) {
        guiMap.put(gui.getPlayer().getUniqueId(), gui);
    }

    @Override
    public void removeGui(IGui gui) {
        guiMap.remove(gui.getPlayer().getUniqueId());
    }

    @Override @Nullable
    public IGui getGui(Player player) {
        return guiMap.get(player.getUniqueId());
    }
}
