package io.papermc.borrowedtime.commands;

import io.papermc.borrowedtime.BTPlayer;
import io.papermc.borrowedtime.FileRW;

import java.util.*;

import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.command.*;
import org.bukkit.inventory.*;
import org.bukkit.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.ChatColor;
import org.bukkit.inventory.meta.*;

public class CommandValues implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player) {
            Player player = (Player) sender;

            openValuesDirectory(player);

        }
        
        return true;
    }

    public static void openValuesDirectory(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Item Values");
        ItemStack itemStack;
        ItemMeta meta;
        List<String> lore;

        itemStack = new ItemStack(Material.DIAMOND);
        meta = itemStack.getItemMeta();
        lore = new ArrayList<>();
        lore.add("...");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.AQUA + "Ores");
        itemStack.setItemMeta(meta);
        inv.setItem(10, itemStack);

        itemStack = new ItemStack(Material.WHEAT);
        meta = itemStack.getItemMeta();
        lore = new ArrayList<>();
        lore.add("...");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.GREEN + "Farming / Food");
        itemStack.setItemMeta(meta);
        inv.setItem(12, itemStack);

        itemStack = new ItemStack(Material.STONE);
        meta = itemStack.getItemMeta();
        lore = new ArrayList<>();
        lore.add("...");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.GRAY + "Blocks");
        itemStack.setItemMeta(meta);
        inv.setItem(14, itemStack);

        itemStack = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        meta = itemStack.getItemMeta();
        lore = new ArrayList<>();
        lore.add("...");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.GOLD + "Valuables");
        itemStack.setItemMeta(meta);
        inv.setItem(16, itemStack);

        ItemStack[] contents = inv.getContents();

        for(int i = 0; i < contents.length; i++) {
            if(contents[i] == null) {
                ItemStack filler = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
                meta = filler.getItemMeta();
                meta.setDisplayName(" ");
                filler.setItemMeta(meta);
                contents[i] = filler;
            }
        }

        inv.setContents(contents);

        player.openInventory(inv);
    }

    public static void handleClickEvent(InventoryClickEvent event) {
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (title.equals("Item Values")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item != null) {
                Material mat = item.getType();
                Player player = (Player) event.getWhoClicked();
                player.sendMessage(ChatColor.GREEN + "Value: " + calcValue(mat, 1) + " seconds");
            }
        }
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