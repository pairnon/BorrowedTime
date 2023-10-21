package io.papermc.borrowedtime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import java.util.logging.*;
import org.bukkit.Bukkit;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

public class Values {

    private static File getYaml(String path) {

        Logger logger = Bukkit.getLogger();

        File configPath = new File(path);
        if( !configPath.exists() ) {
            logger.log(Level.WARNING, path + " not found! Initializing new values config.");
            try {
                configPath.createNewFile();
            }catch(Exception e) {
                logger.log(Level.WARNING, "Error initializing new values config at " + path);
            }
        }
        return configPath;
    }

    public static double getUnitValue(Material mat) {

        String name = mat.name().toLowerCase();
        double unitValue = 0.0;
        File configPath = getYaml("plugins/btvalues.yaml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configPath);
        ConfigurationSection root = config.getConfigurationSection("");
        Set<String> categories = root.getKeys(false);
        for (String category : categories) {
            ConfigurationSection categorySection = root.getConfigurationSection(category);
            if (categorySection != null) {
                Set<String> items = categorySection.getKeys(false);
                for(String item : items) {
                    ConfigurationSection itemSection = categorySection.getConfigurationSection(item);
                    if(itemSection != null && item.equals(name)) {
                        unitValue = itemSection.getDouble("sell");
                    }
                }
            }

        }
        return unitValue;
    }

    public static ArrayList<ItemStack> getItemsOfSection(String section) {

        ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();

        File configPath = getYaml("plugins/btvalues.yaml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configPath);
        ConfigurationSection root = config.getConfigurationSection("");

        Set<String> categories = root.getKeys(false);

        for (String category : categories) {
            ConfigurationSection categorySection = root.getConfigurationSection(category);
            if (categorySection != null && category.equals(section)) {
                Set<String> items = categorySection.getKeys(false);
                ItemStack itemStack;
                ItemMeta meta;
                List<String> lore;
                Material mat;
                mat = Material.BARRIER;
                itemStack = new ItemStack(mat);
                meta = itemStack.getItemMeta();
                meta.setDisplayName(ChatColor.RED + "Back");
                itemStack.setItemMeta(meta);
                itemStacks.add(itemStack);
                for(String item : items) {
                    ConfigurationSection itemSection = categorySection.getConfigurationSection(item);
                    if(itemSection != null) {
                        double unitValue = itemSection.getDouble("sell");
                        mat = Material.getMaterial(item.toUpperCase());
                        itemStack = new ItemStack(mat);
                        meta = itemStack.getItemMeta();
                        lore = new ArrayList<>();
                        double value = unitValue * 1;
                        lore.add("Sell: " + ChatColor.GREEN + value + handlePlural(" second", value));
                        meta.setLore(lore);
                        itemStack.setItemMeta(meta);
                        itemStacks.add(itemStack);
                    }
                }
            }
        }
        return itemStacks;
    }

    public static int calcValue(Material material, int itemAmount) {

        double unitValue = 0.0;
        double finalValue = 0.0;
        
        unitValue = Values.getUnitValue(material);

        finalValue = unitValue * itemAmount;

        return (int)finalValue;
    }

    public static String handlePlural(String noun, double amount) {
        if((int)amount == 1) {
            return noun;
        }
        else {
            return noun + "s";
        }
    }

}