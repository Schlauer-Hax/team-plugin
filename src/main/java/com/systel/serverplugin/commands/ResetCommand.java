package com.systel.serverplugin.commands;

import com.systel.serverplugin.SchoolManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class ResetCommand implements CommandExecutor {
    private final SchoolManager schoolManager;

    public ResetCommand(SchoolManager schoolManager) {
        this.schoolManager = schoolManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (!(commandSender instanceof Player player)) {
            return false;
        }

        player.setPlayerListName(player.getName());
        schoolManager.resetPlayerSchool(player);
        player.openInventory(schoolManager.createSchoolSelector());
        schoolManager.sendMsg(player, "Dein Name wurde zurückgesetzt!");

        return true;
    }
}
