package ru.frostdelta.cmdcooldowns;

import net.milkbowl.vault.chat.Chat;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CommandExecutor implements org.bukkit.command.CommandExecutor {

    CmdCooldowns plugin;
    public CommandExecutor(CmdCooldowns instance){

        this.plugin = instance;

    }

    private List<Player> freeList = new ArrayList<Player>();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        Player p = (Player) sender;
        if(cmd.getName().equalsIgnoreCase("get")){
            Integer exp = p.getTotalExperience();
            Vault.economy.depositPlayer(p, exp*30);
            p.sendMessage(ChatColor.GREEN + "За " +exp+ " опыта вы получили: "+exp*30+ " монет!");
            p.setTotalExperience(0);
            p.setExp(0);
            p.setLevel(0);
            return true;
        }else
        if(cmd.getName().equalsIgnoreCase("free")){
            if(freeList.contains(p)){
                sender.sendMessage(ChatColor.RED + "Вы уже испытывали удачу!");
                return true;
            }
            int a = 0;
            int b = 100;
            int random1 = a + (int) (Math.random() * b);
            if(random1 <= 7){
                sender.sendMessage(ChatColor.GREEN + "Вы выиграли КЕЙС С ДОНАТОМ");
                Bukkit.broadcastMessage(ChatColor.RED + sender.getName() + ChatColor.GREEN + "Выиграл КЕЙС С ДОНАТОМ");
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "crate give to "+sender.getName()+" DonateChest 1 online");
            }else sender.sendMessage(ChatColor.RED + "Вы ничего не выиграли, попробуйте позже!");
            freeList.add(p);
            return true;
        }else
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
            network.deleteCases(player.getUniqueId().toString());
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
