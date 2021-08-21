package me.gavin.notorious;

import java.io.IOException;

public class ShutDownHook extends Thread{

    @Override
    public void run() {
        saveConfig();
    }

    public void saveConfig() {
        try {
            Notorious.INSTANCE.configManager.save();
        }catch (IOException ignored) {}
    }
}
