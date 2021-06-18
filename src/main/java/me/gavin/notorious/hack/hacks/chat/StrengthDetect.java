package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import java.awt.*;
import java.util.Collections;
import java.util.Set;
import java.util.WeakHashMap;

@RegisterHack(name = "StrengthDetect", description = "Tells you when someone runs out of strength", category = Hack.Category.Chat, bind = Keyboard.KEY_G)
public class StrengthDetect extends Hack {

    @RegisterSetting
    public final BooleanSetting render = new BooleanSetting("Render", true);
    @RegisterSetting
    public final NumSetting lineWidth = new NumSetting("LineWidth", 4f, 0.1f, 5f, 0.1f);

    private final Set<EntityPlayer> str = Collections.newSetFromMap(new WeakHashMap<>());

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            if(e instanceof EntityPlayer) {
                if(e.equals(mc.player))
                    continue;
                if(((EntityPlayer) e).isPotionActive(MobEffects.STRENGTH) && !str.contains(e)) {
                    notorious.messageManager.sendMessage("The player " + ChatFormatting.RED + ((EntityPlayer) e).getDisplayNameString() + ChatFormatting.RESET + " now has " + ChatFormatting.RED + "STRENGTH" + ChatFormatting.RESET + "!");
                    this.str.add((EntityPlayer) e);
                }
                if(!str.contains(e))
                    continue;
                if(((EntityPlayer) e).isPotionActive(MobEffects.STRENGTH))
                    continue;
                if(str.contains(e))
                    notorious.messageManager.sendMessage("The player " + ChatFormatting.RED + ((EntityPlayer) e).getDisplayNameString() + ChatFormatting.RESET + " has run out of " + ChatFormatting.RED + "STRENGTH" + ChatFormatting.RESET + "!");
                    str.remove(e);
            }
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            AxisAlignedBB bb = e.getEntityBoundingBox();
            Color red = new Color(255, 0, 0, 255);
            if(str.equals(e)) {
                GL11.glLineWidth(lineWidth.getValue());
                RenderUtil.renderOutlineBB(bb, red);
            }
        }
    }
}
