package io.papermc.borrowedtime.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.papermc.borrowedtime.BTPlayer;
import io.papermc.borrowedtime.BorrowedTime;
import io.papermc.borrowedtime.FileRW;
import io.papermc.borrowedtime.Values;

@SuppressWarnings("deprecation")
public class CommandSellHand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ItemStack itemInHand = player.getItemInHand();

            Material itemType = itemInHand.getType();
            int itemAmount = itemInHand.getAmount();

            int total = Values.calcValue(itemType, itemAmount);

            if(total != 0) {
                player.getInventory().setItemInMainHand(null);
            }

            ArrayList<BTPlayer> btPlayers = FileRW.readFile(BorrowedTime.playersPath);

            BTPlayer btPlayer = BTPlayer.getBTPlayerByUUID(player.getUniqueId(), btPlayers);

            btPlayer.addSecondsRemaining(total);
            FileRW.writeFile(BorrowedTime.playersPath, btPlayers);

            player.sendTitle(ChatColor.GREEN + "You gained " + total + Values.handlePlural(" second", total) + "!", "", 5, 20, 5);

        }
        
        return true;
    }

}