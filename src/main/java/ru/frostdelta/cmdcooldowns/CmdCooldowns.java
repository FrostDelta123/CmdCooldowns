package ru.frostdelta.cmdcooldowns;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import ru.frostdelta.cmdcooldowns.handler.EventListener;

import java.sql.SQLException;

public final class CmdCooldowns extends JavaPlugin {

    Network ntw = new Network();

    @Override
    public void onEnable() {
        CommandExecutor executor = new CommandExecutor(this);
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        this.getCommand("execute").setExecutor(executor);
        this.getCommand("cmdreload").setExecutor(executor);
        this.getCommand("getcases").setExecutor(executor);

        ntw.setPassword(this.getConfig().getString("password"));
        ntw.setUrl(this.getConfig().getString("url"));
        ntw.setUsername(this.getConfig().getString("username"));

        Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable() {
            @Override
            public void run() {
                try {
                    ntw.openConnection();
                } catch (SQLException e) {
                    getLogger().severe("ERROR! Cant load SQL, check config!");
                    getLogger().severe("PLUGIN DISABLED");
                    getLogger().severe("Set debug to true in config.yml");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void onDisable() {
        this.getLogger().info("Disabled!");
    }
}
