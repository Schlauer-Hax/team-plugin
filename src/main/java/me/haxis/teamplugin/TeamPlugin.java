package me.haxis.teamplugin;

import me.haxis.teamplugin.commands.TeamCommand;
import me.haxis.teamplugin.events.TeamEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class TeamPlugin extends JavaPlugin {


    @Override
    public void onEnable() {
        TeamManager teamManager = new TeamManager(this);
        getServer().getPluginManager().registerEvents(new TeamEventHandler(this, teamManager), this);
        getCommand("selectteam").setExecutor(new TeamCommand(teamManager));

        getLogger().info("TeamPlugin enabled");
    }
}
