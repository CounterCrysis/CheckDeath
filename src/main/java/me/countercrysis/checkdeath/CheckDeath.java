package me.countercrysis.checkdeath;

import me.countercrysis.checkdeath.Events.*;
import me.countercrysis.checkdeath.Services.YAMLServices;
import org.bukkit.plugin.java.JavaPlugin;

public class CheckDeath extends JavaPlugin {

    private YAMLServices ys;

    @Override
    public void onEnable() {
        ys = new YAMLServices(this);
        registerEvents();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new EventDeath(this, ys), this);
        getServer().getPluginManager().registerEvents(new EventPlayerJoin(this, ys), this);
        getServer().getPluginManager().registerEvents(new EventInventoryClick(), this);
        getCommand("checkdeath").setExecutor(new EventCommand(this, ys));
        getCommand("checkdeath").setTabCompleter(new EventTabComplete(ys));
    }

}