package ru.frostdelta.cmdcooldowns;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import ru.frostdelta.cmdcooldowns.handler.EventListener;

import java.util.ArrayList;
import java.util.List;

public class Scheduler extends BukkitRunnable {

    private static CmdCooldowns plugin;
    private Player player;

    public Scheduler(CmdCooldowns instance, Player player){

        plugin = instance;
        this.player = player;
    }

    private static List<Player> list = new ArrayList<Player>();

    @Override
    public void run() {
        EventListener.getCommand().remove(player);
    }

    public static List<Player> getList(){
        return list;
    }

}