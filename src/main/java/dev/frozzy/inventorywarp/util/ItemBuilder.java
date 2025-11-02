package dev.frozzy.inventorywarp.util;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Simple ItemBuilder for Bukkit/Spigot/Paper.
 * Supports: amount, displayName, lore.
 * Usage:
 *   ItemStack sword = new ItemBuilder(Material.DIAMOND_SWORD)
 *           .amount(1)
 *           .displayName("&bShiny Sword")
 *           .lore("&7A trusty blade.", "&eDeals extra sparkle damage.")
 *           .build();
 */
public class ItemBuilder {
    private Material material;
    private int amount = 1;
    private String displayName;
    private final List<String> lore = new ArrayList<>();

    /* Constructors */
    public ItemBuilder(Material material) {
        this.material = Objects.requireNonNull(material, "material");
    }

    public ItemBuilder(ItemStack base) {
        Objects.requireNonNull(base, "base");
        this.material = base.getType();
        this.amount = Math.max(1, base.getAmount());
        ItemMeta meta = base.getItemMeta();
        if (meta != null) {
            if (meta.hasDisplayName()) this.displayName = meta.getDisplayName();
            if (meta.hasLore() && meta.getLore() != null) this.lore.addAll(meta.getLore());
        }
    }

    /* Fluent setters */
    public ItemBuilder type(Material material) {
        this.material = Objects.requireNonNull(material, "material");
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = Math.max(1, amount);
        return this;
    }

    public ItemBuilder displayName(String name) {
        this.displayName = colorize(name);
        return this;
    }

    public ItemBuilder lore(List<String> lines) {
        this.lore.clear();
        if (lines != null) {
            for (String line : lines) this.lore.add(colorize(line));
        }
        return this;
    }

    public ItemBuilder lore(String... lines) {
        return lore(lines == null ? null : Arrays.asList(lines));
    }

    public ItemBuilder addLoreLine(String line) {
        this.lore.add(colorize(line));
        return this;
    }

    /* Build */
    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (meta != null) {
            if (displayName != null && !displayName.isEmpty()) meta.setDisplayName(displayName);
            if (!lore.isEmpty()) meta.setLore(new ArrayList<>(lore));
            // Optional: hide default flags if you like – commented out by default
            // meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_ENCHANTS);
            item.setItemMeta(meta);
        }
        return item;
    }

    /* Helpers */
    private static String colorize(String input) {
        return input == null ? null : ChatColor.translateAlternateColorCodes('&', input);
    }
}
