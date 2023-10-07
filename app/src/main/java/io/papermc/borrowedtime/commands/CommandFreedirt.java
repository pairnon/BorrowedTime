package io.papermc.borrowedtime.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public class CommandFreedirt implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            ItemStack dirt = new ItemStack(Material.DIRT, 8);

            player.getInventory().addItem(dirt);
            player.sendMessage(Component.text("You got " + dirt.getAmount() + " dirt!"));

        }
        
        return true;
    }


}