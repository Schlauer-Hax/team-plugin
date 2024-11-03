package me.haxis.teamplugin;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;
import static java.util.Map.entry;

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
    }

    public void removePlayerFromTeam(Player player) {
        filteredTeams(scoreboard.getTeams()).forEach(team -> {
            team.removePlayer(player);
        });
    }

    public void addPlayerToTeam(String teamName, Player player) {
        filteredTeams(scoreboard.getTeams()).forEach(team -> {
            if (team.getName().equalsIgnoreCase("team-"+teamName)) {
                team.addPlayer(player);
            }
        });
    }

    public static Map<String, Material> colorMap = Map.ofEntries(
            entry("aqua", Material.LIGHT_BLUE_STAINED_GLASS_PANE),
            entry("black", Material.BLACK_STAINED_GLASS_PANE),
            entry("blue", Material.BLUE_STAINED_GLASS_PANE),
            entry("dark_aqua", Material.CYAN_STAINED_GLASS_PANE),
            entry("dark_blue", Material.BLUE_STAINED_GLASS_PANE),
            entry("dark_gray", Material.GRAY_STAINED_GLASS_PANE),
            entry("dark_green", Material.GREEN_STAINED_GLASS_PANE),
            entry("dark_purple", Material.PURPLE_STAINED_GLASS_PANE),
            entry("dark_red", Material.RED_STAINED_GLASS_PANE),
            entry("gold", Material.ORANGE_STAINED_GLASS_PANE),
            entry("gray", Material.LIGHT_GRAY_STAINED_GLASS_PANE),
            entry("green", Material.LIME_STAINED_GLASS_PANE),
            entry("light_purple", Material.PINK_STAINED_GLASS_PANE),
            entry("red", Material.RED_STAINED_GLASS_PANE),
            entry("white", Material.WHITE_STAINED_GLASS_PANE),
            entry("yellow", Material.YELLOW_STAINED_GLASS_PANE)
    );

    public Inventory createTeamSelector() {
        Inventory inv = Bukkit.createInventory(null, InventoryType.CHEST, Component.text("Select your Team"));

        int slot = 0;
        for (Team team : filteredTeams(scoreboard.getTeams())) {
            ItemStack item = new ItemStack(colorMap.get(team.displayName().children().get(0).color().toString()));
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