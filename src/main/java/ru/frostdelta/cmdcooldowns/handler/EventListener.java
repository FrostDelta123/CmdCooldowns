package ru.frostdelta.cmdcooldowns.handler;


import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitTask;
import ru.frostdelta.cmdcooldowns.CmdCooldowns;
import ru.frostdelta.cmdcooldowns.Scheduler;

import java.util.HashMap;
import java.util.Map;

public class EventListener implements Listener {

    CmdCooldowns plugin;
    public EventListener(CmdCooldowns instance){

        this.plugin = instance;

    }


    private static Map<Player, String> command = new HashMap<Player, String>();

    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();

        for(String comm : plugin.getConfig().getStringList("commands")){
           if(e.getMessage().startsWith(comm)){
               Integer cooldown = plugin.getConfig().getInt("cooldowns." + e.getMessage());
               if(!getCommand().containsKey(p)){
                   getCommand().put(p, e.getMessage());
                   BukkitTask task = new Scheduler(plugin, p).runTaskLaterAsynchronously(this.plugin, cooldown*20);
               }else{
                   if(getCommand().get(p).equalsIgnoreCase(e.getMessage())) {
                       e.setCancelled(true);
                       p.sendMessage(ChatColor.RED + "Подождите перед использованием!");
                   }
               }
           }
        }

    }


    public static Map<Player, String> getCommand(){
        return command;
    }

}
