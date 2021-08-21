package me.gavin.notorious.hack.hacks.client;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.NotoriousMod;
import me.gavin.notorious.friend.Friend;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.AnimationUtil;
import me.gavin.notorious.util.ColorUtil;
import me.gavin.notorious.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

@RegisterHack(name = "HUD", description = "ez", category = Hack.Category.Client)
public class HUD extends Hack {

    @RegisterSetting
    public final ModeSetting renderMode = new ModeSetting("Mode", "Skeet", "Skeet", "Basic");
    @RegisterSetting
    public final BooleanSetting waterMark = new BooleanSetting("Watermark", true);
    @RegisterSetting
    public final ModeSetting watermarkMode = new ModeSetting("WaterMarkColor", "Rainbow", "Rainbow", "RGB", "ClientSync");
    @RegisterSetting
    public final BooleanSetting arrayList = new BooleanSetting("HackList", true);
    @RegisterSetting
    public final ModeSetting arrayListMode = new ModeSetting("HackListColor", "Flow", "Flow", "RGB", "Sexy", "ClientSync");
    @RegisterSetting
    public final BooleanSetting friendList = new BooleanSetting("FriendList", true);
    @RegisterSetting
    public final ModeSetting friendListMode = new ModeSetting("FriendListColor", "Rainbow", "Rainbow", "RGB", "ClientSync");
    @RegisterSetting
    public final ColorSetting rgbWatermark = new ColorSetting("RGBWatermark", 255, 255, 255, 255);
    @RegisterSetting
    public final ColorSetting rgbArrayList = new ColorSetting("RGBHackList", 255, 255, 255, 255);
    @RegisterSetting
    public final ColorSetting rgbFriendList = new ColorSetting("RGBFriendList", 255, 255, 255, 255);
    @RegisterSetting
    public final NumSetting length = new NumSetting("Length", 7f, 1f, 15f, 1f);
    @RegisterSetting
    public final NumSetting saturation = new NumSetting("Saturation", 0.5f, 0.1f, 1f, 0.1f);
    @RegisterSetting
    public final NumSetting wordSpacing = new NumSetting("WordSpacing", 0.1f, 0.1f, 30, 0.1f);
    @RegisterSetting
    public final BooleanSetting version = new BooleanSetting("Version", false);
    @RegisterSetting
    public final BooleanSetting name = new BooleanSetting("Name", true);
    @RegisterSetting
    public final BooleanSetting ping = new BooleanSetting("Ping", true);
    @RegisterSetting
    public final BooleanSetting ip = new BooleanSetting("IP", true);
    @RegisterSetting
    public final BooleanSetting fps = new BooleanSetting("FPS", false);
    @RegisterSetting
    public final NumSetting xWatermark = new NumSetting("XWatermark", 2.0f, 0.1f, 1000.0f, 0.1f);
    @RegisterSetting
    public final NumSetting yWatermark = new NumSetting("YWatermark", 2.0f, 0.1f, 600.0f, 0.1f);
    @RegisterSetting
    public final NumSetting xFriendList = new NumSetting("XFriendList", 2.0f, 0.1f, 1000.0f, 0.1f);
    @RegisterSetting
    public final NumSetting yFriendList = new NumSetting("YFriendList", 2.0f, 0.1f, 600.0f, 0.1f);

    @SubscribeEvent
    public void onRenderWatermark(RenderGameOverlayEvent.Text event) {
        if(waterMark.isEnabled()) {
            float saturation = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).saturation.getValue();
            float time = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).length.getValue();
            Font font = Notorious.INSTANCE.hackManager.getHack(Font.class);
            ServerData data = mc.getCurrentServerData();
            String string = "";
            //version
            if (version.isEnabled()) {
                string = NotoriousMod.NAME_VERSION;
            } else {
                string = NotoriousMod.MOD_NAME;
            }
            //name
            if (name.isEnabled()) {
                string += (Notorious.INSTANCE.hackManager.getHack(Font.class).isEnabled() ? " | " : " \u23d0 ") + mc.player.getDisplayNameString();
            }
            //ping
            if (ping.isEnabled()) {
                string += (Notorious.INSTANCE.hackManager.getHack(Font.class).isEnabled() ? " | " : " \u23d0 ") + "Ping:" + getPing(mc.player);
            }
            //ip
            if (ip.isEnabled()) {
                if (mc.getConnection() != null && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null) {
                    string += (Notorious.INSTANCE.hackManager.getHack(Font.class).isEnabled() ? " | " : " \u23d0 ") + data.serverIP;
                } else {
                    string += (Notorious.INSTANCE.hackManager.getHack(Font.class).isEnabled() ? " | " : " \u23d0 ") + "Singeplayer";
                }
            }
            //fps
            if (fps.isEnabled()) {
                string += (Notorious.INSTANCE.hackManager.getHack(Font.class).isEnabled() ? " | " : " \u23d0 ") + "FPS:" + Minecraft.getDebugFPS();
            }
            Color colorRainbow;
            if (watermarkMode.getMode().equals("Rainbow")) {
                colorRainbow = ColorUtil.colorRainbow((int) time, saturation, 1f);
            } else if (watermarkMode.getMode().equals("ClientSync")) {
                colorRainbow = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor();
            } else {
                colorRainbow = rgbWatermark.getAsColor();
            }
            int intRainbow;
            if (watermarkMode.getMode().equals("Rainbow")) {
                intRainbow = ColorUtil.getRainbow(time, saturation);
            } else if (watermarkMode.getMode().equals("ClientSync")) {
                intRainbow = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getRGB();
            } else {
                intRainbow = rgbWatermark.getAsColor().getRGB();
            }
            if (renderMode.getMode().equals("Basic")) {
                if (font.isEnabled()) {
                    notorious.fontRenderer.drawStringWithShadow(string, xWatermark.getValue() + 2, yWatermark.getValue(), colorRainbow);
                } else {
                    mc.fontRenderer.drawStringWithShadow(string, (int) xWatermark.getValue() + 2, (int) yWatermark.getValue(), intRainbow);
                }
            }
            if (renderMode.getMode().equals("Skeet")) {
                //background
                Gui.drawRect((int) xWatermark.getValue(), (int) yWatermark.getValue(), (int) xWatermark.getValue() + (Notorious.INSTANCE.hackManager.getHack(Font.class).isEnabled() ? notorious.fontRenderer.getStringWidth(string) : mc.fontRenderer.getStringWidth(string)) + 4, (int) yWatermark.getValue() + mc.fontRenderer.FONT_HEIGHT + 3, new Color(0, 0, 0, 255).getRGB());
                //rainbow line
                Gui.drawRect((int) xWatermark.getValue(), (int) yWatermark.getValue(), (int) xWatermark.getValue() + (Notorious.INSTANCE.hackManager.getHack(Font.class).isEnabled() ? notorious.fontRenderer.getStringWidth(string) : mc.fontRenderer.getStringWidth(string)) + 4, (int) yWatermark.getValue() + 1, ColorUtil.getRainbow(3f, 1f));
                //string
                if (font.isEnabled()) {
                    notorious.fontRenderer.drawStringWithShadow(string, xWatermark.getValue() + 2, yWatermark.getValue() + 4, colorRainbow);
                } else {
                    mc.fontRenderer.drawStringWithShadow(string, (int) xWatermark.getValue() + 2, (int) yWatermark.getValue() + 3, intRainbow);
                }
            }
        }
        if(arrayList.isEnabled()) {
            int yOffset = 2;
            for (Hack hack : notorious.hackManager.getSortedHacks()) {
                if (hack.isEnabled() || (!hack.isEnabled() && (System.currentTimeMillis() - hack.lastDisabledTime) < 250)) {
                    final String n = hack.getName();
                    final String md = hack.getMetaData();
                    final String name = n + md;
                    Font font = Notorious.INSTANCE.hackManager.getHack(Font.class);
                    final double startPos;
                    final int color;
                    if(font.isEnabled()) {
                        startPos = (notorious.fontRenderer.getStringWidth(name) + 2);
                    }else {
                        startPos = (mc.fontRenderer.getStringWidth(name) + 2);
                    }
                    if(arrayListMode.getMode().equals("Flow")) {
                        color = ColorUtil.getRGBWave(length.getValue(), saturation.getValue(), yOffset * 20L);
                    }else if(arrayListMode.getMode().equals("RGB")) {
                        color = rgbArrayList.getAsColor().getRGB();
                    }else if(arrayListMode.getMode().equals("Sexy")) {
                        final float value = ColorUtil.getColorFlow(yOffset / 60.0, 400.0, new Color(0x00d5ff), new Color(0xff4ae7)).getRGB();
                        color = ColorUtil.normalizedFade(value, new Color(0x03adfc), new Color(0x006a9c)).getRGB();
                    }else {
                        color = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getRGB();
                    }
                    double x;
                    if(hack.isEnabled()) {
                        x = -(startPos) + ((startPos + 1) * MathHelper.clamp(AnimationUtil.getSmooth2Animation(250, System.currentTimeMillis() - hack.lastEnabledTime), 0.0, 1.0));
                    } else {
                        x = ((startPos) * -MathHelper.clamp(AnimationUtil.getSmooth2Animation(250, System.currentTimeMillis() - hack.lastDisabledTime), 0.0, 1.0));
                    }
                    double y = yOffset;
                    if(waterMark.isEnabled() && renderMode.getMode().equals("Basic")) {
                        y = yOffset + 9;
                    }else if(waterMark.isEnabled() && renderMode.getMode().equals("Skeet")){
                        y = yOffset + 12;
                    }else {
                        y = yOffset;
                    }
                    if(font.isEnabled()) {
                        if(renderMode.getMode().equals("Skeet")) {
                            Gui.drawRect((int) x - 3, (int) y - 2, (int) (x + startPos + 1), (int) (y + notorious.fontRenderer.getHeight() + 2), 0x90000000);
                            Gui.drawRect((int) x - 4, (int) y - 2, (int) x + 1, (int) (y + notorious.fontRenderer.getHeight() + 2), color);
                        }
                    }else {
                        if(renderMode.getMode().equals("Skeet")) {
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
                        yOffset += notorious.fontRenderer.getHeight() + (int) wordSpacing.getValue();
                    }else {
                        yOffset += mc.fontRenderer.FONT_HEIGHT + (int) wordSpacing.getValue();
                    }
                }
            }
        }
        if(friendList.isEnabled()) {
            int yOffset = 2;
            String homies = ChatFormatting.BOLD + "Homies:";
            int color;
            if(friendListMode.getMode().equals("ClientSync")) {
                color = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getRGB();
            }else if(friendListMode.getMode().equals("RGB")) {
                color = rgbFriendList.getAsColor().getRGB();
            }else {
                color = ColorUtil.getRainbow(6f, 1f);
            }
            mc.fontRenderer.drawStringWithShadow(homies, xFriendList.getValue(), yFriendList.getValue(), color);
            if(!notorious.friend.getFriends().isEmpty()) {
                for (Friend f : notorious.friend.getFriends()) {
                    mc.fontRenderer.drawStringWithShadow(f.getName(), xFriendList.getValue(), yFriendList.getValue() + yOffset + 6, -1);
                    yOffset += mc.fontRenderer.FONT_HEIGHT + 0.5;
                }
            }
        }
    }

    public int getPing(EntityPlayer player) {
        int ping = 0;
        try {
            ping = MathUtil.clamp(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1, 99999);
        }catch (NullPointerException ignored) {}
        return ping;
    }
}
