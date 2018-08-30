package ru.frostdelta.cmdcooldowns;

import org.bukkit.plugin.java.JavaPlugin;
import ru.frostdelta.cmdcooldowns.handler.EventListener;

public final class CmdCooldowns extends JavaPlugin {

    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        this.getServer().getPluginManager().registerEvents(new EventListener(this), this);

    }

    @Override
    public void onDisable() {

    }
}
