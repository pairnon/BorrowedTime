package io.papermc.borrowedtime.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.papermc.borrowedtime.BTPlayer;
import io.papermc.borrowedtime.Main;
import io.papermc.borrowedtime.FileRW;
import io.papermc.borrowedtime.Values;

@SuppressWarnings("deprecation")
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

            ArrayList<BTPlayer> btPlayers = FileRW.readFile(Main.playersPath);

            BTPlayer btPlayer = BTPlayer.getBTPlayerByUUID(player.getUniqueId(), btPlayers);

            btPlayer.addSecondsRemaining(total);
            FileRW.writeFile(Main.playersPath, btPlayers);

            player.sendTitle(ChatColor.GREEN + "You gained " + total + Values.handlePlural(" second", total) + "!", "", 5, 20, 5);

        }
        
        return true;
    }

}