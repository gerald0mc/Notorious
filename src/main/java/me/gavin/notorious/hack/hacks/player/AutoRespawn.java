package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "AutoRespawn", description = "Respawn automatically", category = Hack.Category.Player)
public class AutoRespawn extends Hack {

    @RegisterSetting
    public final BooleanSetting antiDeathScreen = new BooleanSetting("AntiDeathScreen", true);

    @SubscribeEvent
    public void onDeath(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            if (antiDeathScreen.isEnabled())
                event.setCanceled(true);

            if (mc.player.getHealth() <= 0f)
                mc.player.respawnPlayer();
        }
    }
}