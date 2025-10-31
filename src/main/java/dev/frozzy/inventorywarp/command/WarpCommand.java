package dev.frozzy.inventorywarp.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class WarpCommand implements CommandExecutor {

    //warps add "Name" <Icon>
    //warps
    //warps remove "Name"
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String @NotNull [] args) {
        if (!(sender instanceof Player))
            return false;

        if (args.length == 2) {
            switch (args[0]) {
                case "add":

                    break;
                case "remove":
                    break;
            }
        }

        return false;
    }
}
