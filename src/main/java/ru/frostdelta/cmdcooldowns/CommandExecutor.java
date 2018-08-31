package ru.frostdelta.cmdcooldowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    CmdCooldowns plugin;
    public CommandExecutor(CmdCooldowns instance){

        this.plugin = instance;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(args.length == 0){
            String group = plugin.getConfig().getString("group");
            for(String permission : plugin.getConfig().getStringList("permissions")){
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex group " + group + " add "+ permission);
                sender.sendMessage(ChatColor.GREEN + "Permission " + permission + " added!");
            }
            return true;
        }
        return true;
    }


}
