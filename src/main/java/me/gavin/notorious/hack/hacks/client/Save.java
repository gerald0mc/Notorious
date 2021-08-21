package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;

@RegisterHack(name = "Save", description = "ez", category = Hack.Category.Client)
public class Save extends Hack {

    @Override
    public void onEnable() {
        try {
            notorious.configManager.save();
            notorious.messageManager.sendMessage("Saved your config");
            toggle();
        }catch (Exception var5) {
            var5.printStackTrace();
            notorious.messageManager.sendMessage("Save failed for some reason?");
            toggle();
        }
    }
}
