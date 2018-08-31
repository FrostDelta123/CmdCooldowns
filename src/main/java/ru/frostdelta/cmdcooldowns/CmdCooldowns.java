package ru.frostdelta.cmdcooldowns;

import org.bukkit.plugin.java.JavaPlugin;
import ru.frostdelta.cmdcooldowns.handler.EventListener;

public final class CmdCooldowns extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandExecutor executor = new CommandExecutor(this);
        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);
        this.getCommand("execute").setExecutor(executor);
    }

    @Override
    public void onDisable() {

    }
}
