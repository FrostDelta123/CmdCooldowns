package ru.frostdelta.cmdcooldowns.handler;


import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitTask;
import ru.frostdelta.cmdcooldowns.CmdCooldowns;
import ru.frostdelta.cmdcooldowns.Network;
import ru.frostdelta.cmdcooldowns.Scheduler;
import ru.frostdelta.cmdcooldowns.Vault;

import java.util.*;

public class EventListener implements Listener {

    Network network = new Network();
    CmdCooldowns plugin;
    public EventListener(CmdCooldowns instance){

        this.plugin = instance;

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMove(PlayerMoveEvent e){
        if(e.getPlayer().hasPermission("cmdcooldown.bypass")){
            return;
        }
        ApplicableRegionSet set = WGBukkit.getRegionManager(Bukkit.getServer().getWorlds().get(0)).getApplicableRegions(e.getTo());
        Set<ProtectedRegion> list = set.getRegions();
        for(ProtectedRegion region : list){
            if(!region.getId().equalsIgnoreCase("spawn")){
                return;
            }
        }
        if(e.getPlayer().getGameMode().equals(GameMode.CREATIVE)){
            e.getPlayer().setGameMode(GameMode.SURVIVAL);
        }
        if(e.getPlayer().isFlying()){
            e.getPlayer().setAllowFlight(false);
            e.getPlayer().setFlying(false);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e){
        switch (Vault.permission.getPrimaryGroup(e.getPlayer())){
            case "premium":
                break;
            case "master":
                break;
            case "deluxe":
                break;
            case "legend":
                break;
            case "titan":
                break;
            default:
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pex user "+e.getPlayer().getName()+" group set default");
                break;
        }
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
