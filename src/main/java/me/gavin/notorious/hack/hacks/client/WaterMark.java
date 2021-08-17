package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.Notorious;
import me.gavin.notorious.NotoriousMod;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import me.gavin.notorious.util.MathUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;
import java.util.Objects;

@RegisterHack(name = "WaterMark", description = "ez", category = Hack.Category.Client)
public class WaterMark extends Hack {

    @RegisterSetting
    public final ModeSetting renderMode = new ModeSetting("Mode", "Skeet", "Skeet", "Basic");
    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("ColorMode", "Rainbow", "Rainbow", "RGB", "ClientSync");
    @RegisterSetting
    public final ColorSetting rgb = new ColorSetting("RGB", 255, 255, 255, 255);
    @RegisterSetting
    public final NumSetting x = new NumSetting("X", 2.0f, 0.1f, 1000.0f, 0.1f);
    @RegisterSetting
    public final NumSetting y = new NumSetting("Y", 2.0f, 0.1f, 600.0f, 0.1f);
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

    @SubscribeEvent
    public void onRender(RenderGameOverlayEvent.Text event) {
        float saturation = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).saturation.getValue();
        float time = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).length.getValue();
        Font font = Notorious.INSTANCE.hackManager.getHack(Font.class);
        ServerData data = mc.getCurrentServerData();
        //version
        String string = "";
        if(version.isEnabled()) {
            string = NotoriousMod.NAME_VERSION;
        }else {
            string = NotoriousMod.MOD_NAME;
        }
        //name
        if(name.isEnabled()) {
            string += " \u23d0 " + mc.player.getDisplayNameString();
        }
        //ping
        if(ping.isEnabled()) {
            string += " \u23d0 " + "Ping:" + getPing(mc.player);
        }
        //ip
        if(ip.isEnabled()) {
            if (mc.getConnection() != null && mc.getCurrentServerData() != null && mc.getCurrentServerData().serverIP != null) {
                string += " \u23d0 " + data.serverIP;
            }else {
                string += " \u23d0 " + "Singeplayer";
            }
        }
        //fps
        if(fps.isEnabled()) {
            string += " \u23d0 " + "FPS:" + Minecraft.getDebugFPS();
        }
        Color colorRainbow;
        if(mode.getMode().equals("Rainbow")) {
            colorRainbow = ColorUtil.colorRainbow((int) time, saturation, 1f);
        }else if(mode.getMode().equals("ClientSync")){
            colorRainbow = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor();
        }else {
            colorRainbow = rgb.getAsColor();
        }
        int intRainbow;
        if(mode.getMode().equals("Rainbow")) {
            intRainbow = ColorUtil.getRainbow(time, saturation);
        }else if(mode.getMode().equals("ClientSync")){
            intRainbow = Notorious.INSTANCE.hackManager.getHack(ClickGUI.class).guiColor.getAsColor().getRGB();
        }else {
            intRainbow = rgb.getAsColor().getRGB();
        }
        if(renderMode.getMode().equals("Basic")) {
            if (font.isEnabled()) {
                notorious.fontRenderer.drawStringWithShadow(string, x.getValue() + 2, y.getValue(), colorRainbow);
            } else {
                mc.fontRenderer.drawStringWithShadow(string, (int) x.getValue() + 2, (int) y.getValue(), intRainbow);
            }
        }
        if(renderMode.getMode().equals("Skeet")){
            //background
            Gui.drawRect((int) x.getValue(), (int) y.getValue(), (int) x.getValue() + mc.fontRenderer.getStringWidth(string) + 4, (int) y.getValue() + mc.fontRenderer.FONT_HEIGHT + 3, new Color(0, 0, 0, 255).getRGB());
            //rainbow line
            Gui.drawRect((int) x.getValue(), (int) y.getValue(), (int) x.getValue() + mc.fontRenderer.getStringWidth(string) + 4, (int) y.getValue() + 1, ColorUtil.getRainbow(3f, 1f));
            //string
            if (font.isEnabled()) {
                notorious.fontRenderer.drawStringWithShadow(string, x.getValue() + 2, y.getValue() + 3, colorRainbow);
            } else {
                mc.fontRenderer.drawStringWithShadow(string, (int) x.getValue() + 2, (int) y.getValue() + 3, intRainbow);
            }
        }
    }

    public int getPing(EntityPlayer player) {
        int ping = 0;
        try {
            ping = (int) MathUtil.clamp(Objects.requireNonNull(mc.getConnection()).getPlayerInfo(player.getUniqueID()).getResponseTime(), 1, 99999);
        }
        catch (NullPointerException ignored) {
        }
        return ping;
    }
}
