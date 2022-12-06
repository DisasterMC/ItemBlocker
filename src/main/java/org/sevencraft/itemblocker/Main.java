package org.sevencraft.itemblocker;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        ConfigManager.getInstance().setPlugin(this);
        getLogger().info("Enabled");
        this.getCommand("itemblockerreload").setExecutor(new Command());
        getServer().getPluginManager().registerEvents(new BlockItemMove(), this);
        Bukkit.getLogger().info("[ItemBlocker] Author: PineAppleGrits");
        Bukkit.getLogger().info("[ItemBlocker] ItemBlocker enabled!");
    }



    @Override
    public void onDisable() {
        Bukkit.getLogger().info("[ItemBlocker] ItemBlocker disabled!");
    }

    public static FileConfiguration getConfiguration(){
        return ConfigManager.getInstance().getConfig("config.yml");
    }

    public static void reloadConfiguration(){
        ConfigManager.getInstance().reloadConfigs();
    }

}