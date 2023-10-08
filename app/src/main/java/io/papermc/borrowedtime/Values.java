package io.papermc.borrowedtime;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.ChatColor;

public class Values {

    public static int getUnitValue(Material mat) {

        String name = mat.name().toLowerCase();
        int unitValue = 0;
        File configPath = new File("plugins/btvalues.yaml");
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
                        unitValue = itemSection.getInt("sell");
                    }
                }
            }

        }
        return unitValue;
    }

    public static ArrayList<ItemStack> getItemsOfSection(String section) {

        ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();

        File configPath = new File("plugins/btvalues.yaml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configPath);
        ConfigurationSection root = config.getConfigurationSection("");

        Set<String> categories = root.getKeys(false);

        for (String category : categories) {
            ConfigurationSection categorySection = root.getConfigurationSection(category);
            if (categorySection != null && category.equals(section)) {
                Set<String> items = categorySection.getKeys(false);
                for(String item : items) {
                    ConfigurationSection itemSection = categorySection.getConfigurationSection(item);
                    if(itemSection != null) {
                        int unitValue = itemSection.getInt("sell");
                        ItemStack itemStack;
                        ItemMeta meta;
                        List<String> lore;
                        Material mat;

                        mat = Material.getMaterial(item.toUpperCase());
                        itemStack = new ItemStack(mat);
                        meta = itemStack.getItemMeta();
                        lore = new ArrayList<>();
                        int value = unitValue * 1;
                        lore.add("Value: " + ChatColor.GREEN + value + handlePlural(" second", value));
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

        int unitValue = 0;
        int finalValue = 0;

        if(material.equals(Material.DIRT)) {
            unitValue = Values.getUnitValue(material);
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