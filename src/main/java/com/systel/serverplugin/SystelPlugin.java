package com.systel.serverplugin;

import com.systel.serverplugin.commands.ResetCommand;
import com.systel.serverplugin.events.SchoolEventHandler;
import org.bukkit.plugin.java.JavaPlugin;

public class SystelPlugin extends JavaPlugin {

    private SchoolManager schoolManager;
    private SchoolEventHandler eventHandler;

    @Override
    public void onEnable() {
        schoolManager = new SchoolManager(this);
        eventHandler = new SchoolEventHandler(this, schoolManager);
        getServer().getPluginManager().registerEvents(eventHandler, this);
        getCommand("reset").setExecutor(new ResetCommand(schoolManager));

        schoolManager.loadData();
        getLogger().info("SystelPlugin enabled");
    }

    @Override
    public void onDisable() {
        if (schoolManager != null) {
            schoolManager.saveData();
        }
    }
}
