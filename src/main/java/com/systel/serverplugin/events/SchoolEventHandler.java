package com.systel.serverplugin.events;

import com.systel.serverplugin.School;
import com.systel.serverplugin.SchoolManager;
import com.systel.serverplugin.SystelPlugin;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;

public class SchoolEventHandler implements Listener {
    private final SystelPlugin plugin;
    private final SchoolManager schoolManager;

    public SchoolEventHandler(SystelPlugin plugin, SchoolManager schoolManager) {
        this.plugin = plugin;
        this.schoolManager = schoolManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        if (!schoolManager.hasSelectedSchool(player.getUniqueId())) {
            // Open school selector for new players after a short delay
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                player.openInventory(schoolManager.createSchoolSelector());
            }, 20L);
        } else {
            School school = schoolManager.getPlayerSchool(player.getUniqueId());
            schoolManager.setPlayerSchool(player, school);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.getView().getTitle().equals("Wähle deine Einrichtung")) return;

        event.setCancelled(true);

        if (event.getCurrentItem() == null) return;

        Player player = (Player) event.getWhoClicked();
        String schoolName = event.getCurrentItem().getItemMeta().getDisplayName();
        School school = School.getByDisplayName(schoolName.replaceAll("§[0-9a-fk-or]", ""));

        if (school != null) {
            schoolManager.setPlayerSchool(player, school);
            player.closeInventory();
            schoolManager.sendMsg(player, school.getColoredName() + " ausgewählt!");
            schoolManager.sendMsg(player, "Falls du die falsche Einrichtung ausgewählt hast, nutze /reset.");
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        School school = schoolManager.getPlayerSchool(player.getUniqueId());

        if (school != null && school != School.N_A) {
            event.setFormat(school.getChatPrefix() + "%s: %s");
        }
    }
}