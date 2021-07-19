package me.gavin.notorious.hack.hacks.render;

import com.mojang.authlib.GameProfile;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.hack.hacks.misc.FakePlayer;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.network.play.server.SPacketEntityStatus;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.List;

@RegisterHack(name = "PopESP", description = "ez", category = Hack.Category.Render)
public class PopESP extends Hack {

    @RegisterSetting
    public final ColorSetting color = new ColorSetting("Color", 255, 255, 255, 125);
    @RegisterSetting
    public final NumSetting timeToDisappear = new NumSetting("TimeToDisappear", 50, 1, 200, 1);
    @RegisterSetting
    public final BooleanSetting self = new BooleanSetting("Self", true);

    public EntityOtherPlayerMP player;
    public ModelPlayer playerModel;
    public long startTime;

    @SubscribeEvent
    public void onPacket(PacketEvent event){
        if (event.getPacket() instanceof SPacketEntityStatus){
            SPacketEntityStatus packet = ((SPacketEntityStatus) event.getPacket());
            if (packet.getOpCode() == 35 && packet.getEntity(mc.world) != null){
                if (self.getValue() || packet.getEntity(mc.world).getEntityId() != mc.player.getEntityId()) {
                    GameProfile profile = new GameProfile(mc.player.getUniqueID(), "");
                    player = new EntityOtherPlayerMP(FakePlayer.mc.world, profile);
                    player.copyLocationAndAnglesFrom(packet.getEntity(mc.world));
                    playerModel = new ModelPlayer(0, false);
                    startTime = System.currentTimeMillis();
                }
            }
        }
    }
}
