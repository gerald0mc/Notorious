package me.gavin.notorious.hack.hacks.misc;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "VClip", description = "ez", category = Hack.Category.Misc)
public class VClip extends Hack {

    @RegisterSetting
    public final NumSetting blockToClip = new NumSetting("BlocksToClip", 1, -20, 20, 1);

    public BlockPos originalPos;

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if(mc.player.getPosition() == originalPos) {
            notorious.messageManager.sendMessage("VClip failed tp'd back to previous pos.");
        }
        toggle();
    }

    @Override
    public void onEnable() {
        originalPos = mc.player.getPosition();
        if(!mc.player.onGround) {
            notorious.messageManager.sendMessage("Please be on the ground for VClip to work.");
        }
        if(mc.player.onGround) {
            mc.player.setPosition(mc.player.posX, mc.player.posY - blockToClip.getValue(), mc.player.posZ);
        }
    }
}
