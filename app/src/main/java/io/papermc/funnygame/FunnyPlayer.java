package io.papermc.funnygame;

import java.util.*;
import java.io.*;

public class FunnyPlayer implements Serializable {

    private UUID uuid;
    private int secondsRemaining;

    public FunnyPlayer(UUID uuid, int secondsRemaining) {

        this.uuid = uuid;
        this.secondsRemaining = secondsRemaining;

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