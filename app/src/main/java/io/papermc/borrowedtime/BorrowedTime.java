package io.papermc.borrowedtime;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;
import java.time.Duration;
import java.util.logging.*;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.boss.*;
import org.bukkit.entity.*;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;

import io.papermc.borrowedtime.commands.*;

public class BorrowedTime extends JavaPlugin implements Listener {

    private final int startingSeconds = 75;

    private ArrayList<BTPlayer> btPlayers = new ArrayList<BTPlayer>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Logger logger = Bukkit.getLogger();

        if( !(FileRW.readFile("btplayers.ser") == null) ) {
            btPlayers = FileRW.readFile("btplayers.ser");
        }

        else {
            logger.log(Level.WARNING, "btplayers.ser not found! Initializing new BTPlayer databse.");
            FileRW.writeFile("btplayers.ser", btPlayers);
            btPlayers = FileRW.readFile("btplayers.ser");
        }
        

        this.getCommand("freedirt").setExecutor(new CommandFreedirt());
        this.getCommand("sellhand").setExecutor(new CommandSellHand());
        this.getCommand("sellall").setExecutor(new CommandSellAll());
        this.getCommand("get").setExecutor(new CommandGet());

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        btPlayers = FileRW.readFile("btplayers.ser");

        if ( !BTPlayer.checkPlayerInBTPlayers(player, btPlayers) ) {
            BTPlayer newBTPlayer = new BTPlayer(player.getUniqueId(), startingSeconds);
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

                PlayerHasTimeLeft(player, btPlayer);

                if(checkPlayer(player)) {
                    btPlayer.decSecondsRemaining();
                    int secondsRemaining = btPlayer.getSecondsRemaining();
                    timeRem.setTitle(calcColor(secondsRemaining) + "Time Remaining: " + formatSeconds(secondsRemaining));
                    warnPlayerBySeconds(player, secondsRemaining);
                    if (!PlayerHasTimeLeft(player, btPlayer)) {
                        timeRem.removeAll();
                    }

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

    public boolean PlayerHasTimeLeft(Player player, BTPlayer btPlayer) {
        if(btPlayer.getSecondsRemaining() <= 0) {
            player.setHealth(0.0);
            btPlayer.setSecondsRemaining(startingSeconds);
            return false;
        }
        return true;
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

    public void warnPlayerBySeconds(Player player, int seconds) {
        Location loc = player.getLocation();
        Sound sound = null;
        float volume = 1.0F;
        float pitch = 1.0F;

        String title = "Warning!!!";
        String subtitle = "You are low on time!";
        int fadeIn = 5;
        int stay = 10;
        int fadeOut = 5;

        if ( seconds < 10 ) {
            sound = Sound.valueOf("ENTITY_WITHER_DEATH");
            player.sendTitle(calcColor(seconds) + title, calcColor(seconds-1) + subtitle, fadeIn, stay, fadeOut);
        }
        else if ( seconds < 30 ) {
            sound = Sound.valueOf("BLOCK_NOTE_BLOCK_CHIME");
            pitch = 0.5F;
            player.sendTitle(calcColor(seconds) + title, calcColor(seconds-1) + subtitle, fadeIn, stay, fadeOut);
        }
        else if ( seconds < 60 ) { 
            sound = Sound.valueOf("BLOCK_NOTE_BLOCK_BELL");
            pitch = 0.5F;
            player.sendTitle(calcColor(seconds) + title, calcColor(seconds-1) + subtitle, fadeIn, stay, fadeOut);
        }
        else {
            return;
        }
        player.playSound(loc, sound, volume, pitch);
    }

}