package me.gavin.notorious.hack.hacks.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "Respawn", description = "Respawn automatically", category = Hack.Category.Misc)
public class Respawn extends Hack {

    @RegisterSetting
    public final BooleanSetting antiDeathScreen = new BooleanSetting("Respawn", true);
    @RegisterSetting
    public final BooleanSetting deathCoord = new BooleanSetting("DeathCoords", true);

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + deathCoords + ChatFormatting.RESET + "]";
    }

    String deathCoords = "X:0 Y:0 Z:0";

    @SubscribeEvent
    public void onDeath(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            int x = mc.player.getPosition().getX();
            int y = mc.player.getPosition().getY();
            int z = mc.player.getPosition().getZ();
            if (antiDeathScreen.isEnabled())
                event.setCanceled(true);
            if (mc.player.getHealth() <= 0f)
                mc.player.respawnPlayer();
            if(deathCoord.isEnabled()) {
                if (mc.player.dimension == -1)
                    notorious.messageManager.sendRemovableMessage("You died at X: " + ChatFormatting.RED + ChatFormatting.BOLD + x + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + ChatFormatting.BOLD +  y + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + ChatFormatting.BOLD + z + ChatFormatting.RESET + " Dimension: " + ChatFormatting.RED + ChatFormatting.BOLD + "Nether", 1);
                if (mc.player.dimension == 0)
                    notorious.messageManager.sendRemovableMessage("You died at X: " + ChatFormatting.RED + ChatFormatting.BOLD + x + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + ChatFormatting.BOLD + y + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + ChatFormatting.BOLD + z + ChatFormatting.RESET + " Dimension: " + ChatFormatting.GREEN + ChatFormatting.BOLD + "Overworld", 2);
                if (mc.player.dimension == 1)
                    notorious.messageManager.sendRemovableMessage("You died at X: " + ChatFormatting.RED + ChatFormatting.BOLD + x + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + ChatFormatting.BOLD + y + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + ChatFormatting.BOLD + z + ChatFormatting.RESET + " Dimension: " + ChatFormatting.DARK_PURPLE + ChatFormatting.BOLD + "End", 3);
            }
            deathCoords = "X:" + x + " Y:" + y + " Z:" + z;
        }
    }
}