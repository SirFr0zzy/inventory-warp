package dev.frozzy.inventorywarp;

import dev.frozzy.inventorywarp.command.WarpCommand;
import dev.frozzy.inventorywarp.util.WarpManager;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class InventoryWarp extends JavaPlugin {
    @Getter
    private static FileConfiguration warpConfig;
    @Getter
    private static File warpFile;

    private final WarpManager warpManager = new WarpManager(this);

    @Override
    public void onEnable() {
        try {
            loadWarpFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        warpManager.loadWarps();
        Objects.requireNonNull(getCommand("warp")).setExecutor(new WarpCommand(this, warpManager));
        Objects.requireNonNull(getCommand("warp")).setTabCompleter(new WarpCommand(this, warpManager));

    }

    @Override
    public void onDisable() {
        try {
            warpManager.safeWarpsToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void loadWarpFile() throws IOException {
        warpFile = new File(getDataFolder(), "warps.yml");
        warpConfig = YamlConfiguration.loadConfiguration(warpFile);
        warpConfig.options().copyDefaults(true);
        warpConfig.save(warpFile);
    }

}
