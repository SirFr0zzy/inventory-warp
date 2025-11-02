package dev.frozzy.inventorywarp.command;

import dev.frozzy.inventorywarp.util.WarpManager;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;

public class WarpCommand implements CommandExecutor, TabCompleter {


    private final JavaPlugin plugin;
    private final WarpManager manager;

    public WarpCommand(JavaPlugin plugin, WarpManager manager) {
        this.plugin = plugin;
        this.manager = manager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player player))
            return false;

        //warps add <Name> <Icon>
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("add") && player.hasPermission("inventorywarp.add")) {
                String name = args[1].replaceAll("&", "§");
                Material icon = Material.valueOf(args[2]);
                manager.addWarp(name, player.getLocation(), icon);
                player.sendMessage("§aWarp erfolgreich hinzugefügt!");
            }
        } else if (args.length == 0) {
            if (player.hasPermission("inventorywarp.use")) {
                manager.warpInventory(player);
            }
        }

        return false;
    }


    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (args.length == 3) {
            return Arrays.stream(Material.values()).map(Material::name).toList();
        }
        return List.of();
    }
}
