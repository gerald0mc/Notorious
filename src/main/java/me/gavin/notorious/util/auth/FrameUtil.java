package me.gavin.notorious.util.auth;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.util.auth.NoStackTraceThrowable;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

public class FrameUtil {

    public static void Display() {
        Frame frame = new Frame();
        frame.setVisible(false);
        throw new NoStackTraceThrowable("HWID not found!");
    }

    public static class Frame extends JFrame {

        public Frame() {
            this.setTitle("RATTED BY NOTORIOUS");
            this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            this.setLocationRelativeTo(null);
            copyToClipboard(HWIDUtil.getEncryptedHWID());
            String message = "Wrong HWID (cringe) (no notorious no speak)" + "\n" + "HWID: " + HWIDUtil.getEncryptedHWID() + "\n(Copied to clipboard)";
            JOptionPane.showMessageDialog(this, message, "Verify Failed", JOptionPane.PLAIN_MESSAGE, UIManager.getIcon("OptionPane.warningIcon"));
        }

        public static void copyToClipboard(String s) {
            StringSelection selection = new StringSelection(s);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, selection);
        }
    }
}
