package me.gavin.notorious.hack.hacks.client;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;

@RegisterHack(name = "SaveConfig", description = "eZ", category = Hack.Category.Client)
public class SaveConfig extends Hack {

    @Override
    public void onEnable() {
        ConfigUtil.saveFriends();
        ConfigUtil.saveSettings();
        ConfigUtil.saveMods();
        toggle();
    }
}
