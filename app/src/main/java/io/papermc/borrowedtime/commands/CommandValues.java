package io.papermc.borrowedtime.commands;

import io.papermc.borrowedtime.BTPlayer;
import io.papermc.borrowedtime.FileRW;
import io.papermc.borrowedtime.Values;

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
        inv.setItem(6, itemStack);

        itemStack = new ItemStack(Material.WHEAT);
        meta = itemStack.getItemMeta();
        lore = new ArrayList<>();
        lore.add("...");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.GREEN + "Farming");
        itemStack.setItemMeta(meta);
        inv.setItem(20, itemStack);

        itemStack = new ItemStack(Material.ROTTEN_FLESH);
        meta = itemStack.getItemMeta();
        lore = new ArrayList<>();
        lore.add("...");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.GREEN + "Mob Drops");
        itemStack.setItemMeta(meta);
        inv.setItem(24, itemStack);

        itemStack = new ItemStack(Material.GRASS_BLOCK);
        meta = itemStack.getItemMeta();
        lore = new ArrayList<>();
        lore.add("...");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.GRAY + "Blocks");
        itemStack.setItemMeta(meta);
        inv.setItem(2, itemStack);

        itemStack = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE);
        meta = itemStack.getItemMeta();
        lore = new ArrayList<>();
        lore.add("...");
        meta.setLore(lore);
        meta.setDisplayName(ChatColor.GOLD + "Valuables");
        itemStack.setItemMeta(meta);
        inv.setItem(13, itemStack);

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

    public static void openOres(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Ores");
        ArrayList<ItemStack> ores = Values.getItemsOfSection("ores");
        int index = 0;
        for (ItemStack ore : ores) {
            inv.setItem(index, ore);
            index++;
        }
        player.openInventory(inv);
    }

    public static void openFarming(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Farming");
        ArrayList<ItemStack> ores = Values.getItemsOfSection("farming");
        int index = 0;
        for (ItemStack ore : ores) {
            inv.setItem(index, ore);
            index++;
        }
        player.openInventory(inv);
    }

    public static void openMobDrops(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Mob Drops");
        ArrayList<ItemStack> ores = Values.getItemsOfSection("mob_drops");
        int index = 0;
        for (ItemStack ore : ores) {
            inv.setItem(index, ore);
            index++;
        }
        player.openInventory(inv);
    }

    public static void openBlocks(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Blocks");
        ArrayList<ItemStack> ores = Values.getItemsOfSection("blocks");
        int index = 0;
        for (ItemStack ore : ores) {
            inv.setItem(index, ore);
            index++;
        }
        player.openInventory(inv);
    }

    public static void openValuables(Player player) {
        Inventory inv = Bukkit.createInventory(null, 27, "Valuables");
        ArrayList<ItemStack> ores = Values.getItemsOfSection("valuables");
        int index = 0;
        for (ItemStack ore : ores) {
            inv.setItem(index, ore);
            index++;
        }
        player.openInventory(inv);

    }

    public static void handleClickEvent(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        InventoryView view = event.getView();
        String title = view.getTitle();
        if (title.equals("Item Values")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item != null) {
                Material mat = item.getType();

                if(mat.equals(Material.DIAMOND)) {
                    openOres(player);
                }
                else if(mat.equals(Material.WHEAT)) {
                    openFarming(player);
                }
                else if(mat.equals(Material.ROTTEN_FLESH)) {
                    openMobDrops(player);
                }
                else if(mat.equals(Material.GRASS_BLOCK)) {
                    openBlocks(player);
                }
                else if(mat.equals(Material.ENCHANTED_GOLDEN_APPLE)) {
                    openValuables(player);
                }

            }
        }
        else if(title.equals("Ores") || title.equals("Farming") || title.equals("Mob Drops") || title.equals("Blocks") || title.equals("Valuables")) {
            event.setCancelled(true);
            ItemStack item = event.getCurrentItem();
            if (item != null) {
                Material mat = item.getType();
                if(mat.equals(Material.BARRIER)) {
                    openValuesDirectory(player);
                }
            }
        }
    }

}