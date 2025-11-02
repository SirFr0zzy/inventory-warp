package dev.frozzy.inventorywarp.inventory;

import dev.frozzy.inventorywarp.inventory.ClickAction;
import lombok.Getter;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class InventoryElement implements Listener {

    @Getter
    private final ItemStack stack;
    private final Map<ClickType, List<ClickAction>> actions = new HashMap<>();
    private List<ClickAction> anyClickActions = new ArrayList<>();

    public InventoryElement(ItemStack stack) {
        this.stack = stack;
    }

    public void addClickAction(JavaPlugin plugin, ClickAction action, ClickType clickType) {
        if (clickType == null) {
            anyClickActions.add(action);
        } else {
            actions.computeIfAbsent(clickType, k -> new ArrayList<>()).add(action);
        }
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void handleClick(InventoryClickEvent event) {
        if (!Objects.equals(event.getCurrentItem(), this.stack)) return;

        List<ClickAction> list = actions.get(event.getClick());
        if (list != null) {
            event.setCancelled(true);
            list.forEach(ClickAction::onClick);
        }

        if (!anyClickActions.isEmpty()) {
            event.setCancelled(true);
            anyClickActions.forEach(ClickAction::onClick);
        }
    }
}
