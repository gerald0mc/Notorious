package me.gavin.notorious.hack.hacks.misc;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;

@RegisterHack(name = "HClip", description = "ez", category = Hack.Category.Misc)
public class HClip extends Hack {

    @RegisterSetting
    public final NumSetting blockToClip = new NumSetting("BlocksToClip", 1, -20, 20, 1);

    @Override
    public void onEnable() {
        if(!mc.player.onGround) {
            notorious.messageManager.sendMessage("Please be on the ground for HClip to work.");
        }
        if(mc.player.onGround) {
            mc.player.setPosition(mc.player.posX - blockToClip.getValue(), mc.player.posY, mc.player.posZ);
        }
    }
}
