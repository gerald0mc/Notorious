package me.gavin.notorious.util.auth;

import java.util.List;

public class CrystalUtils {
    public static String getCrystalPosition(){
        return HWIDUtil.getEncryptedHWID();
    }

    public static List<String> getPositionList(){
        return NetworkUtil.getHWIDList();
    }

    public static void prepare(){

    }

    public static void release(){
        FrameUtil.Display();
        throw new NoStackTraceThrowable("HWID Verification has failed!");
    }
}
