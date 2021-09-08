package dev.notorious.client.hacks.client;

import dev.notorious.client.Notorious;
import dev.notorious.client.hacks.Hack;
import dev.notorious.client.hacks.RegisterHack;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "GUI", displayName = "GUI", description = "The client's Click GUI.", category = Hack.Category.CLIENT, bind = Keyboard.KEY_O)
public class GuiHack extends Hack {
    @Override
    public void onEnable(){
        super.onEnable();
        mc.displayGuiScreen(Notorious.CLICK_GUI);
    }
}
