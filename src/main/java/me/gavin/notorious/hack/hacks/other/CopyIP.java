package me.gavin.notorious.hack.hacks.other;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;

@RegisterHack(name = "CopyIP", description = "Copies the current server IP to clipboard", category = Hack.Category.Other)
public class CopyIP extends Hack {

    @Override
    protected void onEnable() {
        if (mc.getConnection() != null) {
            if (mc.getCurrentServerData() != null) {
                
            } else {

            }
        } else {
            disable();
        }
    }
}
