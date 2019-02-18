package me.countercrysis.checkdeath.Services;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class YAMLServices {

    private String folder;
    private JavaPlugin plugin;

    public YAMLServices(JavaPlugin plugin) {
        this.plugin = plugin;
        folder = "PlayerData";
    }

    public YAMLServices(JavaPlugin plugin, String folder) {
        this.plugin = plugin;
        this.folder = folder;
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
