package io.papermc.borrowedtime.commands;

import io.papermc.borrowedtime.*;

import java.util.*;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public class CommandSellAll implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player player = (Player) sender;

            ItemStack itemInHand = player.getItemInHand();

            Material itemType = itemInHand.getType();
            int itemAmount = 0;

            Inventory inventory = player.getInventory();

            ItemStack[] contents = inventory.getContents();

            ArrayList<ItemStack> itemsToSell = new ArrayList<ItemStack>();

            for ( ItemStack itemStack : contents ) {
                if(itemStack != null && itemStack.getType().equals(itemType)) {
                    itemsToSell.add(itemStack);
                }
            }

            for ( ItemStack itemToSell : itemsToSell ) {
                itemAmount += itemToSell.getAmount();
            }

            int total = Values.calcValue(itemType, itemAmount);

            if(total != 0) {
                for ( ItemStack itemToSell : itemsToSell ) {
                    itemToSell.setAmount(0);
                }
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