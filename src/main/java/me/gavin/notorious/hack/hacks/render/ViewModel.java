package me.gavin.notorious.hack.hacks.render;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import net.minecraftforge.client.event.EntityViewRenderEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "ViewModel", description = "ez", category = Hack.Category.Render)
public class ViewModel extends Hack {

    @RegisterSetting
    public final NumSetting itemFOV = new NumSetting("ItemViewModel", 120, 0, 250, 1);

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + itemFOV.getValue() + ChatFormatting.RESET + "]";
    }

    @SubscribeEvent
    public void eventFOV(EntityViewRenderEvent.FOVModifier event) {
        event.setFOV(itemFOV.getValue());
    }
}
