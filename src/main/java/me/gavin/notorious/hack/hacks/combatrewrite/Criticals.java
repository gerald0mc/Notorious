package me.gavin.notorious.hack.hacks.combatrewrite;

import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.ModeSetting;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "Criticals", description = "Makes every one of your hits a critical.", category = Hack.Category.CombatRewrite)
public class Criticals extends Hack {
    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Packet", "Packet", "FakeJump", "Jump", "MiniJump");

    @SubscribeEvent
    public void onPacketSend(PacketEvent.Send event){
        if (event.getPacket() instanceof CPacketUseEntity){
            CPacketUseEntity packet = (CPacketUseEntity) event.getPacket();
            if (packet.getAction() == CPacketUseEntity.Action.ATTACK && !(packet.getEntityFromWorld(mc.world) instanceof EntityEnderCrystal) && !AutoCrystal.isPredicting){
                if (!mc.player.onGround) return;
                if (mc.player.isInWater() || mc.player.isInLava()) return;

                if (mode.getMode().equals("Packet")){
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625, mc.player.posZ, true));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 1.1E-5, mc.player.posZ, false));
                    mc.player.connection.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
                } else if (mode.getMode().equals("FakeJump")){
                    mc.player.motionY = 0.1f;
                    mc.player.fallDistance = 0.1f;
                    mc.player.onGround = false;
                } else {
                    mc.player.jump();
                    if (mode.getMode().equals("MiniJump")){
                        mc.player.motionY /= 2;
                    }
                }
            }
        }
    }
}
