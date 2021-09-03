package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.util.RenderUtil;
import me.gavin.notorious.util.TimerUtils;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@RegisterHack(name = "ChorusPredict", description = "ez", category = Hack.Category.Chat)
public class ChorusPredict extends Hack {

    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new Color(255, 255, 255, 255));
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new Color(255, 255, 255, 125));
    @RegisterSetting
    public final BooleanSetting render = new BooleanSetting("Render", true);

    private final TimerUtils timer = new TimerUtils();
    private BlockPos chorusPos;

    @SubscribeEvent
    public void onUpdate(PacketEvent.Receive event) {
        if (event.getPacket() instanceof SPacketSoundEffect) {
            final SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if(packet.getSound() == SoundEvents.ITEM_CHORUS_FRUIT_TELEPORT || packet.getSound() == SoundEvents.ENTITY_ENDERMEN_TELEPORT) {
                chorusPos = new BlockPos(packet.getX(), packet.getY(), packet.getZ());
                notorious.messageManager.sendMessage("Player has used a chorus and is now at X:" + ChatFormatting.RED + ChatFormatting.BOLD + packet.getX() + ChatFormatting.RESET + " Y:" + ChatFormatting.RED + ChatFormatting.BOLD + packet.getY() + ChatFormatting.RESET + " Z:" + ChatFormatting.RED + ChatFormatting.BOLD + packet.getZ() + ChatFormatting.RESET + "!");
                timer.reset();
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(chorusPos != null) {
            if(timer.hasTimeElapsed(2000L)) {
                chorusPos = null;
                return;
            }
            AxisAlignedBB bb = new AxisAlignedBB(chorusPos);
            RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
            RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
        }
    }
}