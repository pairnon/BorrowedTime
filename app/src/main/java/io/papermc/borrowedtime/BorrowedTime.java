package io.papermc.borrowedtime;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.boss.*;
import org.bukkit.entity.*;

public class BorrowedTime extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        player.sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));

        BTPlayer btPlayer = new BTPlayer(player.getUniqueId(), 25);
        
        BossBar timeRem = Bukkit.createBossBar("x", BarColor.PURPLE, BarStyle.SOLID);
        timeRem.addPlayer(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                btPlayer.decSecondsRemaining();
                timeRem.setTitle("Seconds Remaining: " + btPlayer.getSecondsRemaining());
                if(btPlayer.getSecondsRemaining() <= 0) {
                    player.damage(1000000);
                    btPlayer.setSecondsRemaining(25);
                }
            }
        }.runTaskTimer(this, 0L, 20L);

    }

}