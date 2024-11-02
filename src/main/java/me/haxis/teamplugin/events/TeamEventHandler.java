package me.haxis.teamplugin.events;

import me.haxis.teamplugin.TeamManager;
import me.haxis.teamplugin.TeamPlugin;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class TeamEventHandler implements Listener {
    private final TeamPlugin plugin;
    private final TeamManager teamManager;

    public TeamEventHandler(TeamPlugin plugin, TeamManager teamManager) {
        this.plugin = plugin;
        this.teamManager = teamManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!teamManager.hasSelectedTeam(player)) {
            // Open school selector for new players after a short delay
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.openInventory(teamManager.createTeamSelector());
            }, 20L);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Select your Team")) return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        String teamName = event.getCurrentItem().getItemMeta().getDisplayName();
        teamManager.removePlayerFromTeam(player);
        teamManager.addPlayerToTeam(teamName.toLowerCase(), player);
        player.closeInventory();
        player.sendMessage(Component.text("Successfully joined Team "+ teamName + "!"));
    }
}