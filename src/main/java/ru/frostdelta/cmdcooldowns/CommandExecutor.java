package ru.frostdelta.cmdcooldowns;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    CmdCooldowns plugin;
    public CommandExecutor(CmdCooldowns instance){

        this.plugin = instance;

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(cmd.getName().equalsIgnoreCase("execute") && args.length == 0){
            String group = plugin.getConfig().getString("group");
            for(String permission : plugin.getConfig().getStringList("permissions")){
                plugin.getServer().dispatchCommand(Bukkit.getConsoleSender(), "pex group " + group + " add "+ permission);
                sender.sendMessage(ChatColor.GREEN + "Permission " + permission + " added!");
            }
            return true;
        }else
        if(cmd.getName().equalsIgnoreCase("cmdreload")){

            plugin.reloadConfig();
            plugin.saveConfig();
            sender.sendMessage(ChatColor.GREEN + "Config reloaded!");
            return true;
        }else
        if(cmd.getName().equalsIgnoreCase("getcases") && args.length == 0){
            Network network = new Network();
            Player player = (Player) sender;
            UUID uuid = player.getUniqueId();
            for(String col : plugin.getConfig().getStringList("coloumns")){
                Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), "crate give to " + player.getName() + " " + getCaseName(col) + " " + network.getCases(col, uuid.toString()) + " online");
            }
            sender.sendMessage(ChatColor.GREEN + "Кейсы выданы!");
            return true;
        }else
        if(cmd.getName().equalsIgnoreCase("gadgets") && args.length == 0){
            Bukkit.getServer().dispatchCommand(sender, "uc menu main");
        }
        return true;
    }


    public String getCaseName(String var){
        switch (var){
            case "amountCase1":
                return "MoneyChest";
            case "amountCase2":
                return "PrefixChest";
            case "amountCase3":
                return "DonateChest";
            default:
                break;
        }
        return null;
    }

}
