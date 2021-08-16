package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.Notorious;
import me.gavin.notorious.NotoriousMod;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.setting.StringSetting;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@RegisterHack(name = "ChatModifications", description = "Modifies chat.", category = Hack.Category.Chat)
public class ChatModifications extends Hack {

    @RegisterSetting
    public final ModeSetting mode = new ModeSetting("SuffixMode", "Unicode", "Unicode", "Vanilla", "UnicodeVer", "Custom");
    @RegisterSetting
    public final ModeSetting chatColor = new ModeSetting("ChatColor", "Green", "Green", "Red", "Cyan");
    @RegisterSetting
    public final BooleanSetting chatSuffix = new BooleanSetting("ChatSuffix", true);
    @RegisterSetting
    public final BooleanSetting chatTimestamps = new BooleanSetting("ChatTimestamps", true);
    @RegisterSetting
    public final BooleanSetting colorChat = new BooleanSetting("ColorChat", false);

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    public String suffix = "";
    public String color = "";

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        String originalMessage = event.getOriginalMessage();
        if(chatSuffix.isEnabled()) {
            if(mode.getMode().equals("Vanilla")) {
                suffix = " | Notorious";
            }else if(mode.getMode().equals("Unicode")){
                suffix = " \u23d0 \u0274\u1D0F\u1D1B\u1D0F\u0280\u026A\u1D0F\u1D1C\uA731";
            }else if(mode.getMode().equals("UnicodeVersion")){
                suffix = " \u23d0 \u0274\u1D0F\u1D1B\u1D0F\u0280\u026A\u1D0F\u1D1C\uA731\u1D0F " + NotoriousMod.VERSION;
            }
            if(event.getMessage().startsWith("!")) return;
            if(event.getMessage().startsWith(".")) return;
            if(event.getMessage().startsWith("/")) return;
            event.setMessage(originalMessage + suffix);
        }
        if(colorChat.isEnabled()) {
            if(chatColor.getMode().equals("Green")) {
                color = ">";
            }else if(chatColor.getMode().equals("Red")) {
                color = "@";
            }else if(chatColor.getMode().equals("Cyan")) {
                color = "^";
            }
            if(event.getMessage().startsWith("!")) return;
            if(event.getMessage().startsWith(".")) return;
            if(event.getMessage().startsWith("/")) return;
            event.setMessage(color + originalMessage);
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
        if(chatTimestamps.isEnabled()) {
            String time = new SimpleDateFormat("k:mm").format(new Date());
            TextComponentString text = new TextComponentString(ChatFormatting.GRAY + "<" + time + ">" + " " + ChatFormatting.RESET);
            event.setMessage(text.appendSibling(event.getMessage()));
        }
    }
}
