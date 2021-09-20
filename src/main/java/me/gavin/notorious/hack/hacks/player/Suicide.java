package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;

@RegisterHack(name = "Suicide", description = "Automatically kills you.", category = Hack.Category.Player)
public class Suicide extends Hack {

    @Override
    public void onEnable() {
        if(mc.player != null)
            mc.player.sendChatMessage("/kill");
        toggle();
    }
}
