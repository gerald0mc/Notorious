package me.gavin.notorious.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonWriter;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.NotoriousMod;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.setting.*;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class ConfigManager {

    private final Gson gson;
    private final File saveDir;

    public ConfigManager() {
        gson = new GsonBuilder().setPrettyPrinting().create();
        saveDir = new File(Minecraft.getMinecraft().gameDir, NotoriousMod.MOD_ID);
        if (!saveDir.exists())
            saveDir.mkdir();
    }

    public void save() throws IOException {
        for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()) {
            final JsonObject hackObj = new JsonObject();
            hackObj.addProperty("name", hack.getName());
            hackObj.addProperty("bind", hack.getBind());
            hackObj.addProperty("enabled", hack.isEnabled());

            for (Setting setting : hack.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    hackObj.addProperty(setting.getName(), ((BooleanSetting)setting).getValue());
                } else if (setting instanceof ModeSetting) {
                    hackObj.addProperty(setting.getName(), ((ModeSetting)setting).getMode());
                } else if (setting instanceof NumSetting) {
                    hackObj.addProperty(setting.getName(), ((NumSetting)setting).getValue());
                } else if (setting instanceof ColorSetting) {
                    hackObj.addProperty(setting.getName(), ((ColorSetting)setting).getAsColor().getRGB());
                }
            }

            final File saveFile = new File(saveDir, hack.getName() +".json");
            if (!saveFile.exists())
                saveFile.createNewFile();
            final FileWriter writer = new FileWriter(saveFile);
            gson.toJson(hackObj, writer);
            writer.flush();
            writer.close();
        }
    }

    public void load() throws IOException {
        for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()) {
            final File file = new File(saveDir, hack.getName() + ".json");
            if (!file.exists())
                continue;

            final JsonParser parser = new JsonParser();
            final FileReader reader = new FileReader(file);
            final JsonObject object = (JsonObject) parser.parse(reader);
            reader.close();

            if (!object.get("name").getAsString().equals(hack.getName()))
                continue;

            hack.setBind(object.get("bind").getAsInt());
            if (object.get("enabled").getAsBoolean())
                hack.toggle();

            for (Setting setting : hack.getSettings()) {
                if (setting instanceof BooleanSetting) {
                    if (object.get(setting.getName()).getAsBoolean())
                        ((BooleanSetting) setting).toggle();
                } else if (setting instanceof ModeSetting) {
                    final int index = ((ModeSetting)setting).getIndex(object.get(setting.getName()).getAsString());
                    if (index != -1) {
                        ((ModeSetting)setting).setMode(object.get(setting.getName()).getAsString());
                    }
                } else if (setting instanceof NumSetting) {
                    ((NumSetting)setting).setValue(object.get(setting.getName()).getAsFloat());
                } else if (setting instanceof ColorSetting) {
                    final Color tempColor = new Color(object.get(setting.getName()).getAsInt());
                    final float[] hsb = Color.RGBtoHSB(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), null);
                    final ColorSetting colorSetting = (ColorSetting) setting;
                    colorSetting.get
                }
            }
        }
    }
}
