package io.papermc.borrowedtime;

import java.util.*;
import java.io.*;

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

}