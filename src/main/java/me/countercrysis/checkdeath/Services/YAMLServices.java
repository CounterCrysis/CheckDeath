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
        players = new ArrayList<String>();
       ((MemorySection) get("translate","users")).getKeys(false)
               .forEach(p -> players.add(p));
    }

    // Add player to uuid translate
    public void setTranslate (String username, String uuid) {
        set("translate", "users."+username, uuid);
    }

    // Add player to uuid translate
    public String getUUID (String username) {
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
