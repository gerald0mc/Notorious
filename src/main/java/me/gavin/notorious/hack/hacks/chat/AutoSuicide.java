package me.gavin.notorious.hack.hacks.chat;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "AutoSuicide", description = "Automatically kills you.", category = Hack.Category.Chat)
public class AutoSuicide extends Hack {

    @Override
    public void onEnable() {
        if(mc.player != null)
            mc.player.sendChatMessage("/kill");
        toggle();
    }
}
