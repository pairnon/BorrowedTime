package io.papermc.borrowedtime;

import java.time.Duration;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.papermc.borrowedtime.commands.CommandSellAll;
import io.papermc.borrowedtime.commands.CommandSellHand;
import io.papermc.borrowedtime.commands.CommandValues;

@SuppressWarnings("deprecation")
public class BorrowedTime extends JavaPlugin implements Listener {
    
    public static final String pluginDirPath = "./plugins/BorrowedTime/";
    public static final String playersPath = "./plugins/BorrowedTime/btplayers.ser";
    public static final String valuesPath = "./plugins/BorrowedTime/btvalues.yaml";

    private final int startingSeconds = 1200;

    private ArrayList<BTPlayer> btPlayers = new ArrayList<BTPlayer>();

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);

        Logger logger = Bukkit.getLogger();

        if( !(FileRW.readFile(playersPath) == null) ) {
            btPlayers = FileRW.readFile(playersPath);
        }

        else {
            logger.log(Level.WARNING, "btplayers.ser not found! Initializing new BTPlayer database.");
            FileRW.writeFile(playersPath, btPlayers);
            btPlayers = FileRW.readFile(playersPath);
        }
        
        this.getCommand("sellhand").setExecutor(new CommandSellHand());
        this.getCommand("sellall").setExecutor(new CommandSellAll());
        this.getCommand("values").setExecutor(new CommandValues());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();

        btPlayers = FileRW.readFile(playersPath);

        if ( !BTPlayer.checkPlayerInBTPlayers(player, btPlayers) ) {
            BTPlayer newBTPlayer = new BTPlayer(player.getUniqueId(), startingSeconds);
            btPlayers.add(newBTPlayer);
            FileRW.writeFile(playersPath, btPlayers);


            player.sendMessage(ChatColor.GOLD + "Welcome, " + event.getPlayer().getName() + "!");
        }

        else {
            player.sendMessage(ChatColor.GOLD + "Welcome back, " + event.getPlayer().getName() + "!");
        }

        runTime(player);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.sendMessage(ChatColor.GOLD + "You have respawned!");

        runTime(player);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        CommandValues.handleClickEvent(event);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        String msg = event.getDeathMessage();
        event.setDeathMessage(ChatColor.GOLD + "" + msg);
        btPlayers = FileRW.readFile(playersPath);
        BTPlayer btPlayer = BTPlayer.getBTPlayerByUUID(player.getUniqueId(), btPlayers);
        if(btPlayer.getSecondsRemaining() > 4) {
            int currentSeconds = btPlayer.getSecondsRemaining();
            int newSeconds = currentSeconds / 2;
            int index = btPlayers.indexOf(btPlayer);
            btPlayer.setSecondsRemaining(newSeconds);
            btPlayers.set(index, btPlayer);
        }
        else {
            event.setDeathMessage(ChatColor.GOLD + "" + player.getName() + " ran out of time!");
            int index = btPlayers.indexOf(btPlayer);
            btPlayer.setSecondsRemaining(startingSeconds);
            btPlayers.set(index, btPlayer);
        }
        FileRW.writeFile(playersPath, btPlayers);
    }

    public void runTime(Player player) {

        BossBar timeRem = Bukkit.createBossBar("Time Remaining: ", BarColor.BLUE, BarStyle.SOLID);
        timeRem.addPlayer(player);

        new BukkitRunnable() {
            @Override
            public void run() {

                ArrayList<BTPlayer> btPlayers = FileRW.readFile(playersPath);

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
                    FileRW.writeFile(playersPath, btPlayers);
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
        else if ( seconds < 300 ) { 
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