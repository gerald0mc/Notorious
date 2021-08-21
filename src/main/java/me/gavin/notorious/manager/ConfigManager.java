package me.gavin.notorious.manager;

import com.google.gson.*;
import com.google.gson.stream.JsonWriter;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.NotoriousMod;
import me.gavin.notorious.friend.Friend;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.setting.*;
import net.minecraft.client.Minecraft;

import java.awt.*;
import java.io.*;
import java.util.Iterator;

public class ConfigManager {

    private final JsonParser parser;
    private final Gson gson;
    private final File saveDir;
    private final File hackDir;
    private final File otherDir;

    public ConfigManager() {
        parser = new JsonParser();
        gson = new GsonBuilder().setPrettyPrinting().create();
        saveDir = makeDir(new File(Minecraft.getMinecraft().gameDir, NotoriousMod.MOD_ID));
        otherDir = makeDir(new File(saveDir, "other"));
        hackDir = makeDir(new File(saveDir, "hacks"));
    }

    private File makeDir(File file) {
        if (!file.exists())
            file.mkdir();
        return file;
    }

    private File makeFile(File file) throws IOException {
        if (!file.exists())
            file.createNewFile();
        return file;
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

            final File saveFile = makeFile(new File(hackDir, hack.getName() +".json"));
            final FileWriter writer = new FileWriter(saveFile);
            write(hackObj, writer);
        }

        final JsonArray friendList = new JsonArray();
        for (Friend friend : Notorious.INSTANCE.friend.getFriends()) {
            final JsonObject obj = new JsonObject();
            obj.addProperty("name", friend.getName());
            friendList.add(obj);
        }
        write(friendList, new FileWriter(makeFile(new File(otherDir, "friends.json"))));
    }

    public void load() throws IOException {
        for (Hack hack : Notorious.INSTANCE.hackManager.getHacks()) {
            final File file = new File(hackDir, hack.getName() + ".json");
            if (!file.exists())
                continue;

            final FileReader reader = new FileReader(file);
            final JsonObject object = (JsonObject) parser.parse(reader);
            reader.close();

            if (!object.get("name").getAsString().equals(hack.getName()))
                continue;

            hack.setBind(object.get("bind").getAsInt());
            if (object.get("enabled").getAsBoolean())
                hack.toggle();

            for (Setting setting : hack.getSettings()) {
                if (object.get(setting.getName()) != null) {
                    if (setting instanceof BooleanSetting) {
                        if (object.get(setting.getName()).getAsBoolean())
                            ((BooleanSetting) setting).toggle();
                    } else if (setting instanceof ModeSetting) {
                        if (object.has(setting.getName())) {
                            final int index = ((ModeSetting) setting).getIndex(object.get(setting.getName()).getAsString());
                            if (index != -1) {
                                ((ModeSetting) setting).setMode(object.get(setting.getName()).getAsString());
                            }
                        }
                    } else if (setting instanceof NumSetting) {
                        ((NumSetting) setting).setValue(object.get(setting.getName()).getAsFloat());
                    } else if (setting instanceof ColorSetting) {
                        final Color tempColor = new Color(object.get(setting.getName()).getAsInt());
                        final float[] hsb = Color.RGBtoHSB(tempColor.getRed(), tempColor.getGreen(), tempColor.getBlue(), null);
                        final ColorSetting colorSetting = (ColorSetting) setting;
                        colorSetting.getHue().setValue(hsb[0]);
                        colorSetting.getSaturation().setValue(hsb[1]);
                        colorSetting.getBrightness().setValue(hsb[2]);
                    }
                }
            }
        }

        // friend
        final File friendFile = new File(otherDir, "friends.json");
        if (friendFile.exists()) {
            final JsonArray parsedArray = (JsonArray) parse(new FileReader(friendFile));

            final Iterator<JsonElement> it = parsedArray.iterator();

            while (it.hasNext()) {
                final JsonObject obj = (JsonObject) it.next();
                Notorious.INSTANCE.friend.addFriend(obj.get("name").getAsString());
            }
        }
    }

    private void write(JsonElement element, Writer writer) throws IOException {
        gson.toJson(element, writer);
        writer.flush();
        writer.close();
    }

    private JsonElement parse(Reader reader) throws IOException {
        final JsonElement ele = parser.parse(reader);
        reader.close();
        return ele;
    }
}