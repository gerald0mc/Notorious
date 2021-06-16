package me.gavin.notorious.hack.hacks.movement;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@RegisterHack( name = "Step", description = "Automatically moves you up a block", category = Hack.Category.Movement, bind = Keyboard.KEY_Y)
public class Step extends Hack {

    @RegisterSetting
    public final NumSetting stepHeight = new NumSetting("Height", 2f, 0.5f, 3f, 0.5f);

    @SubscribeEvent
    public void onLivingUpdate(PlayerLivingUpdateEvent event) {
        mc.player.stepHeight = stepHeight.getValue();
    }

    @Override
    public void onDisable() {
        mc.player.stepHeight = 0.5f;
    }
}
