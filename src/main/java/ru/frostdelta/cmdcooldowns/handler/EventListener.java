package ru.frostdelta.cmdcooldowns.handler;


import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitTask;
import ru.frostdelta.cmdcooldowns.CmdCooldowns;
import ru.frostdelta.cmdcooldowns.Network;
import ru.frostdelta.cmdcooldowns.Scheduler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventListener implements Listener {

    Network network = new Network();
    CmdCooldowns plugin;
    public EventListener(CmdCooldowns instance){

        this.plugin = instance;

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        UUID uuid = e.getPlayer().getUniqueId();
        e.getPlayer().sendTitle(ChatColor.RED + "Scroll" + ChatColor.BLUE + "Mine", ChatColor.GOLD + "Добро пожаловать, " + e.getPlayer().getName());
        if(network.isExists(uuid.toString()) == 0){
            network.addUUID(uuid.toString());
        }
    }

    private static Map<Player, String> command = new HashMap<Player, String>();

    @EventHandler
    public void onCommandUse(PlayerCommandPreprocessEvent e){
        Player p = e.getPlayer();

        if (p.hasPermission("cmdcooldown.bypass")){
            return;
        }

        for(String comm : plugin.getConfig().getStringList("commands")){
           if(e.getMessage().startsWith(comm)){
               Integer cooldown = plugin.getConfig().getInt("cooldowns." + comm);
               if(!getCommand().containsKey(p)){
                   getCommand().put(p, comm);
                   BukkitTask task = new Scheduler(plugin, p).runTaskLaterAsynchronously(this.plugin, cooldown*20);
               }else{
                   if(getCommand().get(p).equalsIgnoreCase(comm)) {
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
