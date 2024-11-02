package com.systel.serverplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

import java.util.*;
import java.util.stream.Collectors;

public class TeamManager {
    private final TeamPlugin plugin;
    private final FileConfiguration config;
    private final Scoreboard scoreboard;

    public TeamManager(TeamPlugin plugin) {
        this.plugin = plugin;
        this.config = plugin.getConfig();
        ScoreboardManager manager = Bukkit.getScoreboardManager();
        this.scoreboard = manager.getMainScoreboard();
        plugin.saveDefaultConfig();
    }

    public void loadData() {
        if (config.contains("players")) {
            for (String uuidStr : Objects.requireNonNull(config.getConfigurationSection("players")).getKeys(false)) {
                String teamName = config.getString("players." + uuidStr);
                OfflinePlayer player = Bukkit.getOfflinePlayer(UUID.fromString(uuidStr));
                assert teamName != null;
                Bukkit.getScoreboardManager().getMainScoreboard().getTeam("team-"+teamName).addPlayer(player);
            }
        }
    }

    public void saveData() {
        for (Team team : Bukkit.getScoreboardManager().getMainScoreboard().getTeams()) {
            team.getEntries().forEach(entry -> {
                config.set("players." + Bukkit.getOfflinePlayer(entry).getUniqueId(), team.getName());
            });
        }
        plugin.saveConfig();
    }

    public void removePlayerFromTeam(Player player) {
        filteredTeams(scoreboard.getTeams()).forEach(team -> {
            team.removePlayer(player);
        });
        saveData();
    }

    public void addPlayerToTeam(String teamName, Player player) {
        filteredTeams(scoreboard.getTeams()).forEach(team -> {
            if (team.getName().equals("team-"+teamName)) {
                team.addPlayer(player);
            }
        });
    }

    public Inventory createTeamSelector() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, Component.text("Select your Team"));

        int slot = 0;
        for (Team team : filteredTeams(scoreboard.getTeams())) {
            ItemStack item = new ItemStack(Material.GLASS_PANE);
            ItemMeta meta = item.getItemMeta();
            meta.displayName(Component.text(team.getName().replace("team-", "").toUpperCase()));
            item.setItemMeta(meta);
            inv.setItem(slot++, item);
        }

        return inv;
    }

    public boolean hasSelectedTeam(Player player) {
        for (Team team : filteredTeams(scoreboard.getTeams())) {
            if (team.hasPlayer(player)) {
                return true;
            }
        }
        return false;
    }

    public Set<Team> filteredTeams(Set<Team> teams) {
        return teams.stream().filter(team -> team.getName().startsWith("team-")).collect(Collectors.toSet());
    }
}