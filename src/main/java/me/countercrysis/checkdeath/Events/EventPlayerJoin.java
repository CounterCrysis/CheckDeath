package me.countercrysis.checkdeath.Events;

import me.countercrysis.checkdeath.Services.YAMLServices;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;

public class EventPlayerJoin implements Listener {

    Plugin plugin;
    YAMLServices ys;

    public EventPlayerJoin(Plugin plugin, YAMLServices ys) {
        this.plugin = plugin;
        this.ys = ys;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        String uuid = event.getPlayer().getUniqueId().toString();
        String username = event.getPlayer().getName();
        ys.put(uuid);
        ys.cachePlayer(username);
        ys.setTranslate(username, uuid);
        ys.set(uuid, "username", username);

    }

}
