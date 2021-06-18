package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.util.AnimationUtil;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@RegisterHack(name = "ArrayList", description = "Shows enabled modules.", category = Hack.Category.Client, bind = Keyboard.KEY_Z)
public class HackList extends Hack {

    public HackList() {
        this.toggle();
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        //final ScaledResolution sr = new ScaledResolution(mc);
        int yOffset = 2;
        for (Hack hack : notorious.hackManager.getSortedHacks()) {
            if (hack.isEnabled() || (!hack.isEnabled() && (System.currentTimeMillis() - hack.lastDisabledTime) < 250)) {
                int color = ColorUtil.getRGBWave(8f, 0.65f, yOffset * 20L);
                final String n = hack.getName();
                final double startPos = (notorious.fontRenderer.getStringWidth(n) + 2);
                double x;
                if (hack.isEnabled()) {
                    x = -(startPos) + ((startPos + 1) * MathHelper.clamp(AnimationUtil.getSmooth2Animation(250, System.currentTimeMillis() - hack.lastEnabledTime), 0.0, 1.0));
                } else {
                    x = ((startPos) * -MathHelper.clamp(AnimationUtil.getSmooth2Animation(250, System.currentTimeMillis() - hack.lastDisabledTime), 0.0, 1.0));
                }
                double y = yOffset;
                Gui.drawRect((int)x - 3, (int)y - 2, (int)(x + startPos + 1), (int)(y + notorious.fontRenderer.getHeight() + 2), 0x90000000);
                Gui.drawRect((int) x - 4, (int) y - 2, (int) x + 1, (int)(y + notorious.fontRenderer.getHeight() + 2), color);
                //Gui.drawRect((int)(x + startPos), (int)y - 2, (int)(x + startPos + 2), (int)(y + notorious.fontRenderer.getHeight() + 2), color);
                notorious.fontRenderer.drawStringWithShadow(hack.getName(), x + 2, y, new Color(color));
                yOffset += notorious.fontRenderer.getHeight() + 4;
            }
        }
    }
}