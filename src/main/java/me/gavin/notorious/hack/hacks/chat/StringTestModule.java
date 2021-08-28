package me.gavin.notorious.hack.hacks.chat;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.StringSetting;

@RegisterHack(name = "StringTestModule", description = "ez", category = Hack.Category.Chat)
public class StringTestModule extends Hack {

    @RegisterSetting
    public final StringSetting string = new StringSetting("String", "uwu");

    @Override
    public void onEnable() {
        mc.player.sendChatMessage(string.getString());
        toggle();
    }
}
