package io.papermc.funnygame;

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

public class FunnyPlugin extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        player.sendMessage(Component.text("Hello, " + event.getPlayer().getName() + "!"));

        FunnyPlayer funnyPlayer = new FunnyPlayer(player.getUniqueId(), 25);
        
        BossBar timeRem = Bukkit.createBossBar("x", BarColor.PURPLE, BarStyle.SOLID);
        timeRem.addPlayer(player);

        new BukkitRunnable() {
            @Override
            public void run() {
                funnyPlayer.decSecondsRemaining();
                timeRem.setTitle("Seconds Remaining: " + funnyPlayer.getSecondsRemaining());
                if(funnyPlayer.getSecondsRemaining() <= 0) {
                    player.damage(1000000);
                    funnyPlayer.setSecondsRemaining(25);
                }
            }
        }.runTaskTimer(this, 0L, 20L);

    }

}