package com.systel.serverplugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

public class SchoolManager {
    private final SystelPlugin plugin;
    private final HashMap<UUID, School> playerSchools;
    private final FileConfiguration config;
    private final Scoreboard scoreboard;

    public SchoolManager(SystelPlugin plugin) {
        this.plugin = plugin;
        this.playerSchools = new HashMap<>();
        this.config = plugin.getConfig();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getMainScoreboard();
        plugin.saveDefaultConfig();
        setupSchoolTeams();
    }

    public void loadData() {
        if (config.contains("players")) {
            for (String uuidStr : Objects.requireNonNull(config.getConfigurationSection("players")).getKeys(false)) {
                String schoolName = config.getString("players." + uuidStr);
                School school = School.getByDisplayName(schoolName);
                if (school != null) {
                    playerSchools.put(UUID.fromString(uuidStr), school);
                }
            }
        }
    }

    public void saveData() {
        for (UUID uuid : playerSchools.keySet()) {
            config.set("players." + uuid.toString(), playerSchools.get(uuid).getDisplayName());
        }
        plugin.saveConfig();
    }

    private void setupSchoolTeams() {
        for (School school : School.values()) {
            Team team = scoreboard.getTeam(school.name());
            if (team == null) {
                team = scoreboard.registerNewTeam(school.name());
            }
            if (school.name() != "N_A") {
                team.setPrefix(school.getChatPrefix());
            }
        }
    }

    public void removePlayerFromSchoolTeam(Player player) {
        School school = playerSchools.get(player.getUniqueId());
        if (school != null) {
            Team team = scoreboard.getTeam(school.name());
            if (team != null) {
                team.removeEntry(player.getName());
            }
        }

        player.setScoreboard(scoreboard);
    }

    public Inventory createSchoolSelector() {
        Inventory inv = Bukkit.createInventory(null, 9, "Wähle deine Einrichtung");

        int slot = 0;
        for (School school : School.values()) {
            ItemStack item = new ItemStack(school.getMaterial());
            ItemMeta meta = item.getItemMeta();
            meta.setDisplayName(school.getColoredName());
            item.setItemMeta(meta);
            inv.setItem(slot++, item);
        }

        return inv;
    }

    public void resetPlayerSchool(Player player) {
        removePlayerFromSchoolTeam(player);
        playerSchools.remove(player.getUniqueId());
        saveData();
    }

    public void sendMsg(Player player, String msg) {
        final String pluginPrefix = ChatColor.RED + "SystelPlugin" + ChatColor.DARK_GRAY + " » " + ChatColor.RESET;
        player.sendMessage(pluginPrefix + msg);
    }

    public void setPlayerSchool(Player player, School school) {
        playerSchools.put(player.getUniqueId(), school);
        saveData();

        Team team = scoreboard.getTeam(school.name());
        if (team != null) {
            team.addEntry(player.getName());
        }
        player.setScoreboard(scoreboard);
    }

    public School getPlayerSchool(UUID uuid) {
        return playerSchools.get(uuid);
    }

    public boolean hasSelectedSchool(UUID uuid) {
        return playerSchools.containsKey(uuid);
    }
}