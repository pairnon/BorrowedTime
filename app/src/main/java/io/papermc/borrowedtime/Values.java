package io.papermc.borrowedtime;

import java.io.File;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.Material;

public class Values {

    public static int getUnitValue(Material item) {
        File configPath = new File("plugins/btvalues.yaml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(configPath);

        return 0;
    }

}