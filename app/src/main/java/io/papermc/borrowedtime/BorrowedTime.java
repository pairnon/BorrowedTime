package io.papermc.borrowedtime;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.time.Duration;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.boss.*;
import org.bukkit.entity.*;
import org.bukkit.ChatColor;

import io.papermc.borrowedtime.commands.*;

public class BorrowedTime extends JavaPlugin implements Listener {

    private ArrayList<BTPlayer> btPlayers = new ArrayList<BTPlayer>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        if( !(FileRW.readFile("btplayers.ser") == null) ) {
            btPlayers = FileRW.readFile("btplayers.ser");
        }

        else {
            FileRW.writeFile("btplayers.ser", btPlayers);
            btPlayers = FileRW.readFile("btplayers.ser");
        }
        

        this.getCommand("freedirt").setExecutor(new CommandFreedirt());
        this.getCommand("sell").setExecutor(new CommandSell());
        this.getCommand("get").setExecutor(new CommandGet());

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        if ( !BTPlayer.checkPlayerInBTPlayers(player, btPlayers) ) {
            BTPlayer newBTPlayer = new BTPlayer(player.getUniqueId(), 25);
            btPlayers.add(newBTPlayer);
            FileRW.writeFile("btplayers.ser", btPlayers);


            player.sendMessage(Component.text("Welcome, " + event.getPlayer().getName() + "!"));
        }

        else {
            player.sendMessage(Component.text("Welcome back, " + event.getPlayer().getName() + "!"));
        }

        runTime(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.sendMessage("You have respawned!");

        runTime(player);
    }

    public void runTime(Player player) {

        BossBar timeRem = Bukkit.createBossBar("x", BarColor.BLUE, BarStyle.SOLID);
        timeRem.addPlayer(player);

        new BukkitRunnable() {
            @Override
            public void run() {

                ArrayList<BTPlayer> btPlayers = FileRW.readFile("btplayers.ser");

                BTPlayer btPlayer = BTPlayer.getBTPlayerByUUID(player.getUniqueId(), btPlayers);
                int index = btPlayers.indexOf(btPlayer);

                checkPlayerTimeLeft(player, btPlayer);

                if(checkPlayer(player)) {
                    btPlayer.decSecondsRemaining();
                    int secondsRemaining = btPlayer.getSecondsRemaining();
                    timeRem.setTitle(calcColor(secondsRemaining) + "Time Remaining: " + formatSeconds(secondsRemaining));
                    checkPlayerTimeLeft(player, btPlayer);

                    // update ArrayList and file
                    btPlayers.set(index, btPlayer);
                    FileRW.writeFile("btplayers.ser", btPlayers);
                }
                else {
                    timeRem.removeAll();
                    this.cancel();
                }
            }
        }.runTaskTimer(this, 0L, 20L);
    }

    public void checkPlayerTimeLeft(Player player, BTPlayer btPlayer) {
        if(btPlayer.getSecondsRemaining() <= 0) {
            player.setHealth(0.0);
            btPlayer.setSecondsRemaining(25);
        }
    }

    public boolean checkPlayer(Player player) {
        if (player != null && player.isOnline() && !player.isDead()) {
            return true;
        } else {
            return false;
        }
    }

    public String formatSeconds(int totalSeconds) {
        Duration duration = Duration.ofSeconds(totalSeconds);
        long days = duration.toDaysPart();
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();
        long seconds = duration.toSecondsPart();
        return String.format("%d days, %d hours, %d minutes, %d seconds", days, hours, minutes, seconds);
    }

    public ChatColor calcColor(int seconds) {
        if( seconds < 30 ) {
            if ( seconds % 2 == 0) {
                return ChatColor.DARK_RED;
            }
            else {
                return ChatColor.RED;
            }
        }
        else if( seconds < 60 ) {
            if ( seconds % 2 == 0) {
                return ChatColor.YELLOW;
            }
            else {
                return ChatColor.GOLD;
            }
        }
        else if( seconds < 300 ) {
            return ChatColor.YELLOW;
        }
        else {
            return ChatColor.GREEN;
        }
    }

}