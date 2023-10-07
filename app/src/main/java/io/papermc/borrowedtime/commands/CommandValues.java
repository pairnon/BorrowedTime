package io.papermc.borrowedtime.commands;

import io.papermc.borrowedtime.BTPlayer;
import io.papermc.borrowedtime.FileRW;

import java.util.*;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public class CommandValues implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player player = (Player) sender;

        }
        
        return true;
    }

    public static int calcValue(Material material, int itemAmount) {

        int unitValue = 0;
        int finalValue = 0;

        if(material.equals(Material.DIRT)) {
            unitValue = 1;
        }

        else if(material.equals(Material.DIAMOND)) {
            unitValue = 500;
        }

        finalValue = unitValue * itemAmount;

        return finalValue;

    }

    public static String handlePlural(String noun, int amount) {
        if(amount == 1) {
            return noun;
        }
        else {
            return noun + "s";
        }
    }

}