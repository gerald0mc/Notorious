package me.gavin.notorious.hack.hacks.movement;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterValue;
import me.gavin.notorious.setting.Value;
import org.lwjgl.input.Keyboard;

@RegisterHack( name = "Step", description = "Automatically moves you up a block", category = Hack.Category.Movement, bind = Keyboard.KEY_Y)
public class Step extends Hack {

    @RegisterValue
    public final Value<Float> stepHeight = new Value<>("Height", 2f, 0.5f, 3f);

    @Override
    public void onEnable() {
        mc.player.stepHeight = stepHeight.value;
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5f;
    }
}
