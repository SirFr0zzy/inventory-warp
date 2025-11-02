package dev.frozzy.inventorywarp.util;

import dev.frozzy.inventorywarp.InventoryWarp;
import dev.frozzy.inventorywarp.inventory.InventoryElement;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WarpManager {

    private final List<Warp> warps;
    private final JavaPlugin plugin;

    public WarpManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.warps = new ArrayList<>();
    }

    public void addWarp(String warpName, Location location, Material material) {
        Warp warp = new Warp(warps.size(), warpName, location, material);
        warps.add(warp);
        FileConfiguration config = InventoryWarp.getWarpConfig();
        config.set(warps.size() + ".name", warpName);
        config.set(warps.size() + ".location.world", location.getWorld().getName());
        config.set(warps.size() + ".location.x", location.getX());
        config.set(warps.size() + ".location.y", location.getY());
        config.set(warps.size() + ".location.z", location.getZ());
        config.set(warps.size() + ".location.yaw", location.getYaw());
        config.set(warps.size() + ".location.pitch", location.getPitch());
        config.set(warps.size() + ".icon", material.name());
    }


    public void removeWarp(Warp warp) {
        warps.remove(warp);
        FileConfiguration config = InventoryWarp.getWarpConfig();
        config.set(warp.id() + "", null);
    }


    public void safeWarpsToFile() throws IOException {
        InventoryWarp.getWarpConfig().save(
                InventoryWarp.getWarpFile()
        );
    }

    public void warpInventory(Player player) {
        int warpSize = warps.size();
        int pages = (int) Math.ceil((double) warpSize /45);
        int warpsAdded = 0;
        HashMap<Integer, Inventory> pagesInv = new HashMap<>();
        if (pages == 0)
            pages = 1;
        for (int i = 0; i < pages; i++) {

            int inventorySize = 54;

            Inventory page = Bukkit.createInventory(null, inventorySize, "§e§lWarps - Seite " + (i + 1));

            for (int j = 0; j < inventorySize; j++) {
                page.setItem(j, new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayName("").build());
                InventoryElement placeholder = new InventoryElement(new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE).displayName("").build());
                placeholder.addClickAction(plugin, () -> {}, null);
                page.setItem(j, placeholder.getStack());
            }

            pagesInv.put(i, page);

            InventoryElement closeElement = new InventoryElement(new ItemBuilder(Material.BARRIER).displayName("&cClose").build());
            closeElement.addClickAction(plugin, player::closeInventory, ClickType.LEFT);
            page.setItem(49, closeElement.getStack());

            if (i != 0) {
                InventoryElement element = new InventoryElement(new ItemBuilder(Material.ARROW).displayName("&cBack").lore("", "Go Back to Page " + (i - 1)).build());
                int finalI = i;
                element.addClickAction(plugin, () -> {
                    player.openInventory(pagesInv.get(finalI - 1));
                    System.out.println("Opening inventory " +( finalI - 1));
                }, ClickType.LEFT);
                page.setItem(45, element.getStack());
            }

            if (i != pages - 1) {
                InventoryElement element = new InventoryElement(new ItemBuilder(Material.ARROW).displayName("&cNext").lore("", "Go to Page " + (i + 2)).build());
                int finalI = i;
                element.addClickAction(plugin, () -> {
                    player.openInventory(pagesInv.get(finalI + 1));
                    System.out.println("Opening inventory " +( finalI + 2));
                }, ClickType.LEFT);
                page.setItem(53, element.getStack());
            }

            for (int k = 0; k < 45; k++ )  {

                if (warpsAdded < warpSize) {
                    Warp warp = warps.get(warpsAdded);
                    InventoryElement warpElement = new InventoryElement(new ItemBuilder(warp.material()).displayName("&e" + warp.name()).build());
                    if (player.hasPermission("inventorywarp.remove")) {
                        warpElement = new InventoryElement(new ItemBuilder(warp.material()).displayName("&e" + warp.name()).lore("",  "§cRechtsklick um", "§cWarp zu entfernen", "").build());
                        warpElement.addClickAction(plugin, () -> {
                            removeWarp(warp);
                            player.closeInventory();
                            warpInventory(player);
                            player.sendMessage("§cWarp erfolgreich entfernt!");
                        }, ClickType.RIGHT);
                    }
                    warpElement.addClickAction(plugin, () -> {
                        player.teleport(warp.location());
                        player.closeInventory();
                        player.sendMessage("§bDu wurdest gewarped!");
                    }, ClickType.LEFT);
                    page.setItem(k, warpElement.getStack());
                    warpsAdded++;
                }
            }
        }
        System.out.println("Inventory size:" + pagesInv.size());

        player.openInventory(pagesInv.get(0));
    }

    public void loadWarps() {
        FileConfiguration config = InventoryWarp.getWarpConfig();
        config.getValues(false).forEach((key, value) -> {
            Warp warp = new Warp(
                    Integer.parseInt(key),
                    config.getString(key + ".name"),
                    new Location(
                            Bukkit.getWorld(key + ".location.world"),
                            config.getDouble(key + ".location.x"),
                            config.getDouble(key + ".location.y"),
                            config.getDouble(key + ".location.z"),
                            (float) config.getDouble(key + ".location.yaw"),
                            (float) config.getDouble(key + ".location.pitch")
                    ),
                    Material.valueOf(config.getString(key + ".icon"))
            );
            warps.add(warp);
        });
    }

}
