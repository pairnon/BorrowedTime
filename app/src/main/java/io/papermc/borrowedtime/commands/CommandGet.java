package io.papermc.borrowedtime.commands;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;

public class CommandGet implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player player = (Player) sender;
            
            Material material = null;
            String item;
            int amount = 0;

            if( args.length == 1 ) {
                item = args[0].toUpperCase();
                material = Material.getMaterial(item);
                if (material != null) {
                    amount = 1;
                }
            }

            else if( args.length == 2 ) {
                item = args[0].toUpperCase();
                material = Material.getMaterial(item);
                if (material != null) {
                    try {
                        amount = Integer.parseInt(args[1]);
                    }
                    catch (NumberFormatException e) {
                        return false;
                    }
                }
                else {
                    return false;
                }
            }
            else {
                return false;
            }

            ItemStack stack = new ItemStack(material, amount);

            
            player.getInventory().addItem(stack);
            String materialName = material.name().toLowerCase();
            player.sendMessage(Component.text("You got " + amount + " " + materialName));

        }
        
        return true;
    }


}