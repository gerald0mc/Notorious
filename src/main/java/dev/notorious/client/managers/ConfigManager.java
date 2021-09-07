package dev.notorious.client.managers;

import dev.notorious.client.Notorious;
import dev.notorious.client.hacks.Hack;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigManager {
    public void load(){
        try {
            Notorious.HACK_MANAGER.loadConfig();
            Notorious.COMMAND_MANAGER.loadConfig();
            Notorious.FRIEND_MANAGER.loadConfig();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void save(){
        try {
            registerFolders();

            Notorious.HACK_MANAGER.saveConfig();
            Notorious.COMMAND_MANAGER.saveConfig();
            Notorious.FRIEND_MANAGER.saveConfig();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void attach(){
        Runtime.getRuntime().addShutdownHook(new SaveThread());
    }

    public void registerFolders() throws IOException {
        if (!Files.exists(Paths.get("Notorious/"))) Files.createDirectories(Paths.get("Notorious/"));
        if (!Files.exists(Paths.get("Notorious/Hacks"))) Files.createDirectories(Paths.get("Notorious/Hacks"));
        if (!Files.exists(Paths.get("Notorious/Client"))) Files.createDirectories(Paths.get("Notorious/Client"));

        for (Hack.Category category : Hack.Category.values()){
            if (!Files.exists(Paths.get("Notorious/Hacks/" + category.getName() + "/"))) Files.createDirectories(Paths.get("Notorious/Hacks/" + category.getName() + "/"));
        }
    }

    public static class SaveThread extends Thread {
        @Override
        public void run(){
            Notorious.CONFIG_MANAGER.save();
        }
    }
}
