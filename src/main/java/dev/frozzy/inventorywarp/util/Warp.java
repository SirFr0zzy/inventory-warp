package dev.frozzy.inventorywarp.util;


import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;


public record Warp(String name, Location location, Material material) {

}
