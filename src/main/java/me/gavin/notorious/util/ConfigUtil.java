package me.gavin.notorious.util;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.Hack;
import net.minecraft.client.Minecraft;

import java.io.*;
import java.util.Iterator;

public class ConfigUtil {

    Minecraft mc = Minecraft.getMinecraft();
    public static File NotoriousFile;

    public ConfigUtil() {
        NotoriousFile = new File(mc.gameDir + File.separator + "Notorious");
        if(!NotoriousFile.exists()) {
            NotoriousFile.mkdirs();
        }

        loadMods();
    }

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
}
