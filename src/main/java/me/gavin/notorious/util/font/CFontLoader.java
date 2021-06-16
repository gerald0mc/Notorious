package me.gavin.notorious.util.font;

import java.awt.Font;

public class CFontLoader {

    public static final Font MULI_SEMIBOLD = getFontByName("muli-semibold").deriveFont(18f);
    public static final Font COMFORTAA = getFontByName("comfortaa").deriveFont(18f);

    public static Font getFontByName(String name) {
        if (name.equalsIgnoreCase("muli-semibold")) {
            return getFontFromInput("/assets/notorious/Muli-SemiBold.ttf");
        } else if (name.equalsIgnoreCase("Comfortaa")) {
            return getFontFromInput("/assets/notorious/Comfortaa-Regular.ttf");
        }

        return null;
    }

    public static Font getFontFromInput(String path) {
        try {
            Font newFont = Font.createFont(Font.TRUETYPE_FONT, CFontLoader.class.getResourceAsStream(path));
            return newFont;
        }
        catch (Exception e) {
            return null;
        }
    }
}