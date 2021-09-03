package me.gavin.notorious.manager;

import com.google.gson.*;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.NotoriousMod;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.setting.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigManager {
    public void load(){
        try {
            loadHacks();
            loadToggles();
            loadBinds();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void save(){
        try {
            registerFolders();
            saveHacks();
            saveToggles();
            saveBinds();
        } catch (IOException exception){
            exception.printStackTrace();
        }
    }

    public void attach(){
        Runtime.getRuntime().addShutdownHook(new SaveThread());
    }

    public void registerFolders() throws IOException {
        if (!Files.exists(Paths.get("Notorious/"))) Files.createDirectories(Paths.get("Notorious/"));
        if (!Files.exists(Paths.get("Notorious/Hacks/"))) Files.createDirectories(Paths.get("Notorious/Hacks/"));
        if (!Files.exists(Paths.get("Notorious/Client/"))) Files.createDirectories(Paths.get("Notorious/Client/"));

        for (Hack.Category category : Hack.Category.values()){
            if (!Files.exists(Paths.get("Notorious/Hacks/" + category.toString() + "/"))) Files.createDirectories(Paths.get("Notorious/Hacks/" + category + "/"));
        }
    }

    public void loadHacks() throws IOException {
        for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()){
            if (!Files.exists(Paths.get("Notorious/Hacks/" + hack.getCategory().toString() + "/" + hack.getName() + ".json")))
                return;

            InputStream stream = Files.newInputStream(Paths.get("Notorious/Hacks/" + hack.getCategory().toString() + "/" + hack.getName() + ".json"));
            JsonObject hackObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
            if (hackObject.get("Hack") == null) return;

            JsonObject settingObject = hackObject.get("Settings").getAsJsonObject();
            for (Setting setting : hack.getSettings()){
                JsonElement dataObject = settingObject.get(setting.getName());
                try {
                    if (dataObject != null && dataObject.isJsonPrimitive()){
                        if (setting instanceof BooleanSetting){
                            ((BooleanSetting) setting).setValue(dataObject.getAsBoolean());
                        } else if (setting instanceof NumSetting){
                            ((NumSetting) setting).setValue(dataObject.getAsFloat());
                        } else if (setting instanceof ModeSetting){
                            ((ModeSetting) setting).setMode(dataObject.getAsString());
                        } else if (setting instanceof StringSetting){
                            ((StringSetting) setting).setString(dataObject.getAsString());
                        } else if (setting instanceof ColorSetting){
                            // This color picker SUCKS! TODO
                        }
                    }
                } catch (NumberFormatException exception){
                    NotoriousMod.LOGGER.error("Faulty setting found. Stacktrace below. (" + setting.getName() + ")");
                    exception.printStackTrace();
                }
            }
            stream.close();
        }
    }

    public void saveHacks() throws IOException {
        for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()){
            if (Files.exists(Paths.get("Notorious/Hacks/" + hack.getCategory().toString() + "/" + hack.getName() + ".json")))
                new File("Notorious/Hacks/" + hack.getCategory().toString() + "/" + hack.getName() + ".json").delete();
            Files.createFile(Paths.get("Notorious/Hacks/" + hack.getCategory().toString() + "/" + hack.getName() + ".json"));

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream("Notorious/Hacks/" + hack.getCategory().toString() + "/" + hack.getName() + ".json"), StandardCharsets.UTF_8);
            JsonObject hackObject = new JsonObject();
            JsonObject settingObject = new JsonObject();
            hackObject.add("Hack", new JsonPrimitive(hack.getName()));

            for (Setting setting : hack.getSettings()){
                if (setting instanceof BooleanSetting){
                    settingObject.add(setting.getName(), new JsonPrimitive(((BooleanSetting) setting).getValue()));
                } else if (setting instanceof NumSetting){
                    settingObject.add(setting.getName(), new JsonPrimitive(((NumSetting) setting).getValue()));
                } else if (setting instanceof ModeSetting){
                    settingObject.add(setting.getName(), new JsonPrimitive(((ModeSetting) setting).getMode()));
                } else if (setting instanceof StringSetting){
                    settingObject.add(setting.getName(), new JsonPrimitive(((StringSetting) setting).getString()));
                } else if (setting instanceof ColorSetting){
                    settingObject.add(setting.getName(), new JsonPrimitive(((ColorSetting) setting).getAsColor().getRGB()));
                }
            }

            hackObject.add("Settings", settingObject);
            stream.write(gson.toJson(new JsonParser().parse(hackObject.toString())));
            stream.close();
        }
    }

    public void loadToggles() throws IOException {
        if (!Files.exists(Paths.get("Notorious/Client/Toggles.json")))
            return;

        InputStream stream = Files.newInputStream(Paths.get("Notorious/Client/Toggles.json"));
        JsonObject hackObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        if (hackObject.get("Hacks") == null) return;

        JsonObject toggleObject = hackObject.get("Hacks").getAsJsonObject();

        for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()){
            JsonElement dataObject = toggleObject.get(hack.getName());
            if (dataObject != null && dataObject.isJsonPrimitive()){
                if (dataObject.getAsBoolean()){
                    hack.enable();
                }
            }
        }

        stream.close();
    }

    public void saveToggles() throws IOException {
        if (Files.exists(Paths.get("Notorious/Client/Toggles.json")))
            new File("Notorious/Client/Toggles.json").delete();
        Files.createFile(Paths.get("Notorious/Client/Toggles.json"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream("Notorious/Client/Toggles.json"), StandardCharsets.UTF_8);
        JsonObject hackObject = new JsonObject();
        JsonObject toggleObject = new JsonObject();

        for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()){
            toggleObject.add(hack.getName(), new JsonPrimitive(hack.isEnabled()));
        }

        hackObject.add("Hacks", toggleObject);
        stream.write(gson.toJson(new JsonParser().parse(hackObject.toString())));
        stream.close();
    }

    public void loadBinds() throws IOException {
        if (!Files.exists(Paths.get("Notorious/Client/Binds.json")))
            return;

        InputStream stream = Files.newInputStream(Paths.get("Notorious/Client/Binds.json"));
        JsonObject hackObject = new JsonParser().parse(new InputStreamReader(stream)).getAsJsonObject();
        if (hackObject.get("Hacks") == null) return;

        JsonObject bindObject = hackObject.get("Hacks").getAsJsonObject();

        for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()){
            JsonElement dataObject = bindObject.get(hack.getName());
            if (dataObject != null && dataObject.isJsonPrimitive()){
                hack.setBind(dataObject.getAsInt());
            }
        }

        stream.close();
    }

    public void saveBinds() throws IOException {
        if (Files.exists(Paths.get("Notorious/Client/Binds.json")))
            new File("Notorious/Client/Binds.json").delete();
        Files.createFile(Paths.get("Notorious/Client/Binds.json"));

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter stream = new OutputStreamWriter(new FileOutputStream("Notorious/Client/Binds.json"), StandardCharsets.UTF_8);
        JsonObject hackObject = new JsonObject();
        JsonObject bindObject = new JsonObject();

        for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()){
            bindObject.add(hack.getName(), new JsonPrimitive(hack.getBind()));
        }

        hackObject.add("Hacks", bindObject);
        stream.write(gson.toJson(new JsonParser().parse(hackObject.toString())));
        stream.close();
    }

    public static class SaveThread extends Thread {
        @Override
        public void run(){
            Notorious.INSTANCE.configManager.save();
        }
    }
}