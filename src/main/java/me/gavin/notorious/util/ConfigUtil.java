package me.gavin.notorious.util;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.friend.Friend;
import me.gavin.notorious.friend.Friends;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.setting.*;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class ConfigUtil {

    Minecraft mc = Minecraft.getMinecraft();
    public static File NotoriousFile;
    public static File SettingFile;

    public ConfigUtil() {
        NotoriousFile = new File(mc.gameDir + File.separator + "Notorious");
        SettingFile = new File(mc.gameDir + File.separator + "Notorious" + File.separator + "Settings");
        if(!NotoriousFile.exists()) {
            NotoriousFile.mkdirs();
        }

        loadMods();
        loadFriends();
    }

    //SAVE SHIT

    public static void saveMods() {
        try {
            File file = new File(NotoriousFile.getAbsolutePath(), "EnabledModules.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            Iterator var3 = Notorious.INSTANCE.hackManager.getHacks().iterator();

            while(var3.hasNext()) {
                Hack hack = (Hack) var3.next();
                if(hack.isEnabled()) {
                    out.write(hack.getName());
                    out.write("\r\n");
                }
            }
            out.close();
        }catch (Exception var5) { }
    }

    public static void saveFriends() {
        try {
            File file = new File(NotoriousFile.getAbsolutePath(), "Friends.txt");
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            Iterator var3 = Friends.getFriends().iterator();

            while(var3.hasNext()) {
                Friend friend = (Friend) var3.next();
                out.write(friend.getName());
                out.write("\r\n");
            }
            out.close();
        }catch (Exception var5) { }
    }

    public static void saveSettings() {
        File file;
        BufferedWriter out;
        Iterator var3;
        Setting i;
        try {
            file = new File(SettingFile.getAbsolutePath(), "Number.txt");
            out = new BufferedWriter(new FileWriter(file));
            var3 = SettingGroup.getValues().iterator();

            while(var3.hasNext()) {
                i = (Setting) var3.next();
                if(i instanceof NumSetting) {
                    out.write(i.getName() + ":" + ((NumSetting) i).getValue() + "\r\n");
                }
            }
            out.close();
        }catch (Exception var5) {var5.printStackTrace();}
        try {
            file = new File(SettingFile.getAbsolutePath(), "Boolean.txt");
            out = new BufferedWriter(new FileWriter(file));
            var3 = SettingGroup.getValues().iterator();

            while(var3.hasNext()) {
                i = (Setting) var3.next();
                if(i instanceof BooleanSetting) {
                    out.write(i.getName() + ":" + ((BooleanSetting) i).getValue() + "\r\n");
                }
            }
            out.close();
        }catch (Exception var5) {var5.printStackTrace();}
        try {
            file = new File(SettingFile.getAbsolutePath(), "Mode.txt");
            out = new BufferedWriter(new FileWriter(file));
            var3 = SettingGroup.getValues().iterator();

            while(var3.hasNext()) {
                i = (Setting) var3.next();
                if(i instanceof ModeSetting) {
                    out.write(i.getName() + ":" + ((ModeSetting) i).getMode() + "\r\n");
                }
            }
            out.close();
        }catch (Exception var5) {var5.printStackTrace();}
        try {
            file = new File(SettingFile.getAbsolutePath(), "Color.txt");
            out = new BufferedWriter(new FileWriter(file));
            var3 = SettingGroup.getValues().iterator();

            while(var3.hasNext()) {
                i = (Setting) var3.next();
                if(i instanceof ColorSetting) {
                    out.write(i.getName() + ":" + ((ColorSetting) i).getAsColor().getRed() + ":" + ((ColorSetting) i).getAsColor().getGreen() + ":" + ((ColorSetting) i).getAsColor().getBlue() + ":" + ((ColorSetting) i).getAsColor().getAlpha() + "\r\n");
                }
            }
            out.close();
        }catch (Exception var5) {var5.printStackTrace();}
    }

    //LOAD SHIT

    public void loadMods() {
        try {
            File file = new File(NotoriousFile.getAbsolutePath(), "EnabledModules.txt");
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            String line;
            while((line = br.readLine()) != null) {
                Iterator var6 = Notorious.INSTANCE.hackManager.getHacks().iterator();

                while(var6.hasNext()) {
                    Hack h = (Hack)var6.next();
                    if (h.getName().equals(line)) {
                        h.toggle();
                    }
                }
            }
            br.close();
        } catch (Exception var8) {
            var8.printStackTrace();
        }
    }

    public void loadFriends() {
        try {
            File file = new File(NotoriousFile.getAbsolutePath(), "Friends.txt");
            FileInputStream fstream = new FileInputStream(file.getAbsolutePath());
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));

            Friends.friends.clear();
            String line;
            while((line = br.readLine()) != null) {
                Friends.addFriend(line);
            }

            br.close();
        }catch (Exception var5) {var5.printStackTrace();}
    }

    public void loadSettings() {
        File file;
        FileInputStream fstream;
        DataInputStream in;
        BufferedReader br;
        String line;
        String curLine;
        String name;
        String isOn;
        String m;
        Setting mod;
        int color;
        try {
            file = new File(SettingFile.getAbsolutePath(), "Boolean.txt");
            fstream = new FileInputStream(file.getAbsolutePath());
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));

            while((line = br.readLine()) != null) {
                curLine = line.trim();
                name = curLine.split(":")[0];
                isOn = curLine.split(":")[1];
                m = curLine.split(":")[2];
                for(Setting setting : SettingGroup.getValues()) {
                    if (setting != null && setting.getName().equalsIgnoreCase(m)) {
                        mod = BooleanSetting.getFromDisplayString(name);
                        mod.equals(Boolean.parseBoolean(isOn));
                    }
                }
            }

            br.close();
        } catch (Exception var12) {
            var12.printStackTrace();
        }
        try {
            file = new File(SettingFile.getAbsolutePath(), "Number.txt");
            fstream = new FileInputStream(file.getAbsolutePath());
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));

            while((line = br.readLine()) != null) {
                curLine = line.trim();
                name = curLine.split(":")[0];
                isOn = curLine.split(":")[1];
                m = curLine.split(":")[2];
                for(Setting setting : SettingGroup.getValues()) {
                    if (setting != null && setting.getName().equalsIgnoreCase(m)) {
                        mod = NumSetting.getFromDisplayString(name);
                        mod.equals(Double.parseDouble(isOn));
                    }
                }
            }

            br.close();
        } catch (Exception var12) {
            var12.printStackTrace();
        }
        try {
            file = new File(SettingFile.getAbsolutePath(), "Mode.txt");
            fstream = new FileInputStream(file.getAbsolutePath());
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));

            while((line = br.readLine()) != null) {
                curLine = line.trim();
                name = curLine.split(":")[0];
                isOn = curLine.split(":")[1];
                m = curLine.split(":")[2];
                for(Setting setting : SettingGroup.getValues()) {
                    if (setting != null && setting.getName().equalsIgnoreCase(m)) {
                        mod = ModeSetting.getFromDisplayString(name);
                        mod.equals(isOn);
                    }
                }
            }

            br.close();
        } catch (Exception var12) {
            var12.printStackTrace();
        }
        try {
            file = new File(SettingFile.getAbsolutePath(), "Color.txt");
            fstream = new FileInputStream(file.getAbsolutePath());
            in = new DataInputStream(fstream);
            br = new BufferedReader(new InputStreamReader(in));

            while((line = br.readLine()) != null) {
                curLine = line.trim();
                name = curLine.split(":")[0];
                isOn = curLine.split(":")[1];
                m = curLine.split(":")[2];
                for(Setting setting : SettingGroup.getValues()) {
                    if (setting != null && setting.getName().equalsIgnoreCase(m)) {
                        mod = ColorSetting.getFromDisplayString(name);
                        mod.equals(Boolean.valueOf(isOn));
                    }
                }
            }

            br.close();
        } catch (Exception var12) {
            var12.printStackTrace();
        }
    }
}
