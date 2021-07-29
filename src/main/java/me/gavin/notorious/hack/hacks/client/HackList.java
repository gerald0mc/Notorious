package me.gavin.notorious.hack.hacks.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.AnimationUtil;
import me.gavin.notorious.util.ColorUtil;
import me.gavin.notorious.util.NColor;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.awt.*;

@RegisterHack(name = "ArrayList", description = "Shows enabled modules.", category = Hack.Category.Client)
public class HackList extends Hack {

    @RegisterSetting
    public final ModeSetting listMode = new ModeSetting("ListMode", "Pretty", "Pretty", "Basic");
    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("Mode", "Flow", "Flow", "RGB");
    @RegisterSetting
    public final NumSetting length = new NumSetting("Length", 7f, 1f, 15f, 1f);
    @RegisterSetting
    public final NumSetting saturation = new NumSetting("Saturation", 0.5f, 0.1f, 1f, 0.1f);
    @RegisterSetting
    public final ColorSetting rgb = new ColorSetting("RGB", 255, 255, 255, 255);

    public HackList() {
        this.toggle();
    }

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + mode.getMode() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        //final ScaledResolution sr = new ScaledResolution(mc);
        int yOffset = 2;
        for (Hack hack : notorious.hackManager.getSortedHacks()) {
            if (hack.isEnabled() || (!hack.isEnabled() && (System.currentTimeMillis() - hack.lastDisabledTime) < 250)) {
                final String n = hack.getName();
                final String md = hack.getMetaData();
                final String name = n + md;
                Font font = ((Font)Notorious.INSTANCE.hackManager.getHack(Font.class));
                final double startPos;
                final int color;
                if(font.isEnabled()) {
                    startPos = (notorious.fontRenderer.getStringWidth(name) + 2);
                }else {
                    startPos = (mc.fontRenderer.getStringWidth(name) + 2);
                }
                if(mode.getMode().equals("Flow")) {
                    color = ColorUtil.getRGBWave(length.getValue(), saturation.getValue(), yOffset * 20L);
                }else {
                    color = rgb.getAsColor().getRGB();
                }
                double x;
                if(hack.isEnabled()) {
                    x = -(startPos) + ((startPos + 1) * MathHelper.clamp(AnimationUtil.getSmooth2Animation(250, System.currentTimeMillis() - hack.lastEnabledTime), 0.0, 1.0));
                } else {
                    x = ((startPos) * -MathHelper.clamp(AnimationUtil.getSmooth2Animation(250, System.currentTimeMillis() - hack.lastDisabledTime), 0.0, 1.0));
                }
                double y = yOffset;
                if(((WaterMark)Notorious.INSTANCE.hackManager.getHack(WaterMark.class)).isEnabled()) {
                    y = yOffset + 9;
                }else {
                    y = yOffset;
                }
                if(font.isEnabled()) {
                    if(listMode.getMode().equals("Pretty")) {
                        Gui.drawRect((int) x - 3, (int) y - 2, (int) (x + startPos + 1), (int) (y + notorious.fontRenderer.getHeight() + 2), 0x90000000);
                        Gui.drawRect((int) x - 4, (int) y - 2, (int) x + 1, (int) (y + notorious.fontRenderer.getHeight() + 2), color);
                    }
                }else {
                    if(listMode.getMode().equals("Pretty")) {
                        Gui.drawRect((int) x - 3, (int) y - 2, (int) (x + startPos + 1), (int) (y + mc.fontRenderer.FONT_HEIGHT + 2), 0x90000000);
                        Gui.drawRect((int) x - 4, (int) y - 2, (int) x + 1, (int) (y + mc.fontRenderer.FONT_HEIGHT + 2), color);
                    }
                }
                //Gui.drawRect((int)(x + startPos), (int)y - 2, (int)(x + startPos + 2), (int)(y + notorious.fontRenderer.getHeight() + 2), color);
                if(font.isEnabled()) {
                    notorious.fontRenderer.drawStringWithShadow(name, x + 2, y, new Color(color));
                }else {
                    mc.fontRenderer.drawStringWithShadow(name,(int) x + 2,(int) y, color);
                }
                if(font.isEnabled()) {
                    yOffset += notorious.fontRenderer.getHeight() + 4;
                }else {
                    yOffset += mc.fontRenderer.FONT_HEIGHT + 4;
                }
            }
        }
    }
}