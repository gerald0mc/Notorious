package me.gavin.notorious.util.auth;

import com.google.common.hash.Hashing;
import oshi.SystemInfo;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.Processor;
import oshi.software.os.OperatingSystem;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;

public class HWIDUtil {

    public static String getEncryptedHWID() {
        final SystemInfo systemInfo = new SystemInfo();
        final OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
        final HardwareAbstractionLayer hardwareAbstractionLayer = systemInfo.getHardware();
        final Processor[] centralProcessor = hardwareAbstractionLayer.getProcessors();

        String rawHwid = "";

        for (final Processor processor : centralProcessor) {
            rawHwid = rawHwid + processor.getIdentifier() + processor.getName() + processor.getVendor();
        }

        final String osName = System.getProperty("os.name").toLowerCase();

        if (!osName.startsWith("mac os x")) {
            final String command = osName.startsWith("windows") ? "wmic baseboard get serialnumber" : "dmidecode -s baseboard-serial-number";
            try {
                final Process child = Runtime.getRuntime().exec(command);
                final InputStream in = child.getInputStream();
                int ch;
                while ((ch = in.read()) != -1) {
                    rawHwid += ch;
                }
            } catch (IOException e) {
                Runtime.getRuntime().exit(0);
            }
        }

        rawHwid = rawHwid + operatingSystem.getManufacturer() + operatingSystem.getFamily() + System.getProperty("os.version");

        return Hashing.sha256().hashString(rawHwid, StandardCharsets.UTF_8).toString();
    }

}
