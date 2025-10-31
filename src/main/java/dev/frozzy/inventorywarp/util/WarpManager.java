package dev.frozzy.inventorywarp.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class WarpManager {

    private final HashMap<Integer, Warp> warps;

    public WarpManager() {
        this.warps = new HashMap<>();
    }

    public Warp addWarp(String warpName, Location location, Material material) {
        Warp warp = new Warp(warpName, location, material);
        return warps.put(0, warp);
    }

    public void removeWarp(Location location) {

    }

    public Inventory warpInventory() {
        int warpSize = warps.size();
        int inventorySize = warpSize + (9  - (warpSize % 9)) % 9;
        int pages = (int) Math.ceil((double) warpSize /45);
        int warpsAdded = 0;
        for (int i = 0; i < pages; i++) {
            Inventory page = Bukkit.createInventory(null, inventorySize, "§e§lWarps");
            for (int k = 0; k < 45; k++ )  {
                if (warpsAdded < warpSize) {
                    warpsAdded++;
                    if (warps.get(warpsAdded).material() == null)
                        page.setItem(k, new ItemStack(Material.GRASS_BLOCK));
                    else {
                        page.setItem(k, new ItemStack(warps.get(warpsAdded).material()));
                    }
                }
            }
        }

        return null;
    }

    public void saveWarps() {

    }

    public void loadWarps() {

    }

}
