package com.systel.serverplugin;

import com.systel.serverplugin.commands.TeamCommand;
import com.systel.serverplugin.events.TeamEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamPlugin extends JavaPlugin {

    private TeamManager teamManager;
    private TeamEventHandler eventHandler;

    @Override
    public void onEnable() {
        teamManager = new TeamManager(this);
        eventHandler = new TeamEventHandler(this, teamManager);
        getServer().getPluginManager().registerEvents(eventHandler, this);
        getCommand("selectteam").setExecutor(new TeamCommand(teamManager));

        teamManager.loadData();
        getLogger().info("TeamPlugin enabled");
    }

    @Override
    public void onDisable() {
        if (teamManager != null) {
            teamManager.saveData();
        }
    }
}
