package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@RegisterHack(name = "Fullbright", description = "Makes it fully bright", category = Hack.Category.Render, bind = Keyboard.KEY_B)
public class Fullbright extends Hack {

    private List<Float> previousLevels = new ArrayList<>();

    @Override
    public void onEnable() {
        if (mc.world.provider != null) {
            for (float f : mc.world.provider.lightBrightnessTable) {
                previousLevels.add(f);
            }

            Arrays.fill(mc.world.provider.lightBrightnessTable, 1f);
        }
    }

    @Override
    public void onDisable() {
        for (int i = 0; i < mc.world.provider.lightBrightnessTable.length; i++) {
            mc.world.provider.lightBrightnessTable[i] = previousLevels.get(i);
        }

        previousLevels.clear();
    }
}