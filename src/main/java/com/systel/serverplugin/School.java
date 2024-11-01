package com.systel.serverplugin;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum School {
    BA_LEIPZIG("BA-Leipzig", ChatColor.DARK_PURPLE, Material.PURPLE_TERRACOTTA),
    HS_FULDA("HS-Fulda", ChatColor.DARK_GREEN, Material.GREEN_TERRACOTTA),
    BA_RHEIN_MAIN("BA-Rhein-Main", ChatColor.BLUE, Material.BLUE_TERRACOTTA),
    HWR_BERLIN("HWR-Berlin", ChatColor.RED, Material.RED_TERRACOTTA),
    DHBW_MANNHEIM("DHBW-Mannheim", ChatColor.YELLOW, Material.YELLOW_TERRACOTTA),
    DHGE_EISENACH("DHGE-Eisenach", ChatColor.AQUA, Material.LIGHT_BLUE_TERRACOTTA),
    N_A("Keine Antwort", ChatColor.GRAY, Material.BARRIER);

    private final String displayName;
    private final ChatColor color;
    private final Material material;

    School(String displayName, ChatColor color, Material material) {
        this.displayName = displayName;
        this.color = color;
        this.material = material;
    }

    public String getDisplayName() {
        return displayName;
    }

    public ChatColor getColor() {
        return color;
    }

    public Material getMaterial() {
        return material;
    }

    public String getColoredName() {
        return color + displayName + ChatColor.RESET;
    }

    public String getChatPrefix() {
        if (this == N_A) {
            return "";
        }

        return color + displayName + ChatColor.DARK_GRAY + " » " + ChatColor.RESET;
    }

    public static School getByDisplayName(String name) {
        for (School school : values()) {
            if (school.getDisplayName().equalsIgnoreCase(name)) {
                return school;
            }
        }
        return null;
    }
}