package me.gavin.notorious.hack.hacks.render;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@RegisterHack(name = "Fullbright", description = "Makes it fully bright", category = Hack.Category.Render)
public class Fullbright extends Hack {

    private final List<Float> previousLevels = new ArrayList<>();

    @Override
    public void onEnable() {
        if(mc.player != null && mc.world != null) {
            final float[] table = mc.world.provider.getLightBrightnessTable();
            if (mc.world.provider != null) {
                for (float f : table) {
                    previousLevels.add(f);
                }

                Arrays.fill(table, 1f);
            }
        }else {
            toggle();
        }
    }

    @Override
    public void onDisable() {
        if(mc.player != null && mc.world != null) {
            final float[] table = mc.world.provider.getLightBrightnessTable();
            for (int i = 0; i < table.length; i++) {
                table[i] = previousLevels.get(i);
            }

            previousLevels.clear();
        }
    }
}