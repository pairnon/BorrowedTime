package io.papermc.borrowedtime.commands;

import io.papermc.borrowedtime.BTPlayer;
import io.papermc.borrowedtime.FileRW;

import java.util.*;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public class CommandSell implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ItemStack itemInHand = player.getItemInHand();

            Material itemType = itemInHand.getType();
            int itemAmount = itemInHand.getAmount();

            int total = calcValue(itemType, itemAmount);

            if(total != 0) {
                player.getInventory().setItemInMainHand(null);
            }

            ArrayList<BTPlayer> btPlayers = FileRW.readFile("btplayers.ser");

            BTPlayer btPlayer = BTPlayer.getBTPlayerByUUID(player.getUniqueId(), btPlayers);

            btPlayer.addSecondsRemaining(total);
            FileRW.writeFile("btplayers.ser", btPlayers);

            player.sendMessage(Component.text("You gained " + total + handlePlural(" second", total) + "!"));

        }
        
        return true;
    }

    public int calcValue(Material material, int itemAmount) {

        int unitValue = 0;
        int finalValue = 0;

        if(material.equals(Material.DIRT)) {
            unitValue = 1;
        }

        finalValue = unitValue * itemAmount;

        return finalValue;

    }

    public String handlePlural(String noun, int amount) {
        if(amount == 1) {
            return noun;
        }
        else {
            return noun + "s";
        }
    }

}