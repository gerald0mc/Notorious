package me.gavin.notorious.util.auth;

import me.gavin.notorious.NotoriousMod;
import net.minecraftforge.fml.common.FMLLog;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class NetworkUtil {

    public static List<String> getHWIDList() {
        List<String> HWIDList = new ArrayList<>();
        try {
            final URL url = new URL(NotoriousMod.HWID_URL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                HWIDList.add(inputLine);
            }
        } catch (Exception e) {
            FMLLog.log.info("Failed to load Notorious!");
        }
        return HWIDList;
    }
}
