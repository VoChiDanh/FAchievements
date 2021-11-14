package net.danh.achievements;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.Console;
import java.io.File;
import java.io.IOException;

public class Achievements extends JavaPlugin implements Listener {

    private File configFile;
    private FileConfiguration config;


    public void onEnable() {
        Bukkit.getServer().getPluginManager().registerEvents(this, (Plugin) this);
        createConfigs();


    }

    public void onDisable() {
    }

    public void createConfigs() {
        configFile = new File(getDataFolder(), "config.yml");
        if (!configFile.exists()) saveResource("config.yml", false);

        config = new YamlConfiguration();

        try {
            config.load(configFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void reloadConfigs() {
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public String convert(String s) {
        return s.replace("&", "§");
    }


    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase("FAchievements")) {
            if (args.length == 0) {
                for (String helpadmin : getConfig().getStringList("Help")) {
                    sender.sendMessage(convert(helpadmin));
                    return true;
                }
            }

            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                reloadConfigs();
                sender.sendMessage(convert(getConfig().getString("Reload")));
            }

            if (args.length >= 2) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < args.length; i++) {
                    if (i == 1) {
                        sb.append("");
                        sb.append(args[i]);
                    }
                    if (i >= 2) {
                        sb.append(" ");
                        sb.append(args[i]);
                    }
                }
                String fakeAchieve = sb.toString();
                String target = args[0];
                Bukkit.getServer().broadcastMessage(convert(getConfig().getString("Achievements")).replace("%p", String.valueOf(String.valueOf(target))).replace("%a", fakeAchieve));
                return true;
            }
        }

        return true;
    }
}
