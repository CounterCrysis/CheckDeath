package me.countercrysis.checkdeath.Services;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class YAMLServices {

    private String folder;
    private JavaPlugin plugin;
    private Map<String, UUID> players;

    // Constructor
    public YAMLServices(JavaPlugin plugin) {
        this.plugin = plugin;
        folder = "PlayerData";
        initPlayersCache();
    }

    // Load player Name and UUID from translate file, if available.
    private void initPlayersCache() {
        players = new HashMap<>();
        MemorySection ms = (MemorySection) get("translate","users");
        if (ms == null) return;
        ms.getValues(false)
                .forEach((k,v)->players.put(k,UUID.fromString((String)v)));
    }

    // Load player game names into cache
    public void cachePlayer(Player player) {
        if (players.get(player.getUniqueId()) != null) {
            players.put(player.getName(), player.getUniqueId());
        }
    }

    public Map<String, UUID> getCachedPlayers() {
        return players;
    }

    public List<String> getDeathIds(String username) {
        username = username.toLowerCase();
        List<String> ids = new ArrayList();
        UUID uuid = getUUID(username);
        MemorySection ms = (MemorySection) get(uuid.toString(),"deaths");
        if (ms != null) {
            ms.getKeys(false).forEach(id -> ids.add(id));
        }
        return ids;
    }

    // Add player to uuid translate
    public void setTranslate (String username, UUID uuid) {
        username = username.toLowerCase();
        set("translate", "users."+username, uuid.toString());
    }

    // Add player to uuid translate
    public UUID getUUID (String username) {
        return players.get(username);
    }

    // Add player to uuid translate
    public String getUsername (UUID uuid) {
        return players.entrySet().stream()
                .filter( u -> u.getValue().equals(uuid))
                .findAny()
                .map(k -> k.getKey())
                .orElse(null);
    }

    // Puts the file "name" in the folder
    public void put (String name) {
        try {
            File file = new File(
                    plugin.getDataFolder() + ((folder != null) ? File.separator + folder : ""),
                    name + ".yml" );
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            yml.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Store an obj in path
    public void set(String name, String path, Object object) {
        try {
            File file = new File(
                    plugin.getDataFolder() + ((folder != null) ? File.separator + folder : ""),
                    name + ".yml" );
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            yml.set(path, object);
            yml.save(file);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    // Gets object...
    public Object get(String name, String path) {
        try {
            File file = new File(
                    plugin.getDataFolder() + ((folder != null) ? File.separator + folder : ""),
                    name + ".yml" );
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            return yml.get(path);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public YamlConfiguration yml(String name){
        try{
            File file = new File(
                    plugin.getDataFolder() + ((folder != null) ? File.separator + folder : ""),
                    name + ".yml" );
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            return yml;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void save(String name) {
        try{
            File file = new File(
                    plugin.getDataFolder() + ((folder != null) ? File.separator + folder : ""),
                    name + ".yml" );
            YamlConfiguration yml = YamlConfiguration.loadConfiguration(file);
            yml.save(file);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

}
