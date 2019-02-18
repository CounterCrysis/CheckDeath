package me.countercrysis.checkdeath;

import me.countercrysis.checkdeath.Events.EventCommand;
import me.countercrysis.checkdeath.Events.EventDeath;
import me.countercrysis.checkdeath.Events.EventPlayerJoin;
import me.countercrysis.checkdeath.Services.YAMLServices;
import org.bukkit.plugin.java.JavaPlugin;

public class CheckDeath extends JavaPlugin {

    private YAMLServices ys;

    @Override
    public void onEnable() {
        ys = new YAMLServices(this, "PlayerData");
        registerEvents();
    }

    private void registerEvents() {
        getServer().getPluginManager().registerEvents(new EventDeath(this, ys), this);
        getServer().getPluginManager().registerEvents(new EventPlayerJoin(this, ys), this);
        getCommand("checkdeath").setExecutor(new EventCommand(this, ys));
    }

}