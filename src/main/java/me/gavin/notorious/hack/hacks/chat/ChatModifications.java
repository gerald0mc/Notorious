package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.ClientChatEvent;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

@RegisterHack(name = "ChatModifications", description = "Modifies chat.", category = Hack.Category.Chat)
public class ChatModifications extends Hack {

    @RegisterSetting
    public final BooleanSetting chatSuffix = new BooleanSetting("ChatSuffix", true);
    @RegisterSetting
    public final BooleanSetting chatTimestamps = new BooleanSetting("ChatTimestamps", true);

    @Override
    public void onEnable() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void onDisable() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    @SubscribeEvent
    public void onChat(ClientChatEvent event) {
        String originalMessage = event.getOriginalMessage();
        if(chatSuffix.isEnabled()) {
            String suffix = " | Notorious";
            if(event.getMessage().startsWith("!")) return;
            if(event.getMessage().startsWith(".")) return;
            if(event.getMessage().startsWith("/")) return;
            event.setMessage(originalMessage + suffix);
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
