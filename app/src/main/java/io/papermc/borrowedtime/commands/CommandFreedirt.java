package io.papermc.borrowedtime.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public class CommandFreedirt implements CommandExecutor {

    // This method is called, when somebody uses our command
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            // Here we need to give items to our player

            // Create a new ItemStack (type: diamond)
            ItemStack dirt = new ItemStack(Material.DIRT, 8);

            // Set the amount of the ItemStack
            // dirt.setAmount(8);

            // Give the player our items (comma-seperated list of all ItemStack)
            player.getInventory().addItem(dirt);
            player.sendMessage(Component.text("You got " + dirt.getAmount() + " dirt!"));

        }
        
        return true;
    }


}