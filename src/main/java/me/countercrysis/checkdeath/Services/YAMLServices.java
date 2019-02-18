package me.countercrysis.checkdeath.Services;

import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class YAMLServices {

    private String folder;
    private JavaPlugin plugin;
    private List<String> players;

    // Constructor
    public YAMLServices(JavaPlugin plugin) {
        this.plugin = plugin;
        folder = "PlayerData";
        initPlayersCache();
    }

    // Load player game names into cache
    private void initPlayersCache() {
        players = new ArrayList();
        MemorySection ms = (MemorySection) get("translate","users");
        if (ms == null) return;
        ((MemorySection) get("translate","users")).getKeys(false)
               .forEach(p -> players.add(p));
    }

    // Load player game names into cache
    public void cachePlayer(String username) {
        if (!players.contains(username)) {
            players.add(username.toLowerCase());
        }
    }

    public List<String> getCachedPlayers() {
        return players;
    }

    public List<String> getDeathIds(String username) {
        List<String> ids = new ArrayList();
        String uuid = getUUID(username);
        MemorySection ms = (MemorySection) get(uuid,"deaths");
        if (ms != null) {
            ms.getKeys(false).forEach(id -> ids.add(id));
        }
        return ids;
    }

    // Add player to uuid translate
    public void setTranslate (String username, String uuid) {
        username = username.toLowerCase();
        set("translate", "users."+username, uuid);
    }

    // Add player to uuid translate
    public String getUUID (String username) {
        username = username.toLowerCase();
        System.out.println(get("translate", "users." + username));
        return (String) get("translate", "users." + username);
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
