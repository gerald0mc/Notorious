package me.gavin.notorious.hack.hacks.combat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "Quiver", description = "Automatically places a totem in your offhand.", category = Hack.Category.Combat, bind = Keyboard.KEY_C)
public class Quiver extends Hack{

    @RegisterSetting
    public final NumSetting tickDelay = new NumSetting("HoldTime", 2.5f, 0, 8, 0.5f);

    private boolean hasStartedShooting;

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if (mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow && mc.player.isHandActive() && mc.player.getItemInUseMaxCount() >= tickDelay.getValue()) {
            mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.cameraYaw, -90.0f, mc.player.onGround));
            hasStartedShooting = true;
            mc.player.stopActiveHand();
        }
    }

    public void stuff() {
        if(hasStartedShooting) {
            toggle();
            hasStartedShooting = false;
        }
    }
}
