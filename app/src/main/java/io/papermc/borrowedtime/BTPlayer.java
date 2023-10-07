package io.papermc.borrowedtime;

import java.util.*;
import java.io.*;

import org.bukkit.entity.Player;

public class BTPlayer implements Serializable {

    private UUID uuid;
    private int secondsRemaining;

    public BTPlayer(UUID uuid, int secondsRemaining) {

        this.uuid = uuid;
        this.secondsRemaining = secondsRemaining;

    }

    public void addSecondsRemaining(int seconds) {
        secondsRemaining += seconds;
    }

    public void setSecondsRemaining(int seconds) {
        secondsRemaining = seconds;
    }

    public void decSecondsRemaining() {
        secondsRemaining--;
    }

    public UUID getUUID() {
        return uuid;
    }

    public int getSecondsRemaining() {
        return secondsRemaining;
    }

    public String toString() {
        String out = "UUID: " + uuid + "\nSeconds Remaining: " + secondsRemaining;
        return out;
    }

    public static boolean checkPlayerInBTPlayers(Player player, ArrayList<BTPlayer> arrlist) {
        for( BTPlayer b : arrlist ) {
            if( b.getUUID().equals(player.getUniqueId()) ) {
                return true;
            }
        }
        return false;
    }

    public static BTPlayer getBTPlayerByUUID(UUID uuid, ArrayList<BTPlayer> arrlist) {
        for ( BTPlayer b : arrlist ) {
            if( b.getUUID().equals(uuid) ) {
                return b;
            }
        }
        return null;
    }

}