package me.gavin.notorious.hack.hacks.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "AutoRespawn", description = "Respawn automatically", category = Hack.Category.Misc)
public class AutoRespawn extends Hack {

    @RegisterSetting
    public final BooleanSetting antiDeathScreen = new BooleanSetting("AntiDeathScreen", true);
    @RegisterSetting
    public final BooleanSetting deathCoords = new BooleanSetting("DeathCoords", true);

    private boolean hasAnnounced = false;

    @SubscribeEvent
    public void onDeath(GuiOpenEvent event) {
        if (event.getGui() instanceof GuiGameOver) {
            hasAnnounced = false;
            if (antiDeathScreen.isEnabled())
                event.setCanceled(true);
            if (mc.player.getHealth() <= 0f)
                mc.player.respawnPlayer();
            if(deathCoords.isEnabled() && !hasAnnounced) {
                if (mc.player.dimension == -1)
                    notorious.messageManager.sendMessage("You died at X: " + ChatFormatting.RED + ChatFormatting.BOLD + mc.player.getPosition().getX() + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + ChatFormatting.BOLD +  mc.player.getPosition().getY() + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + ChatFormatting.BOLD + mc.player.getPosition().getZ() + ChatFormatting.RESET + " Dimension: " + ChatFormatting.RED + ChatFormatting.BOLD + "Nether");
                if (mc.player.dimension == 0)
                    notorious.messageManager.sendMessage("You died at X: " + ChatFormatting.RED + ChatFormatting.BOLD + mc.player.getPosition().getX() + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + ChatFormatting.BOLD + mc.player.getPosition().getY() + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + ChatFormatting.BOLD + mc.player.getPosition().getZ() + ChatFormatting.RESET + " Dimension: " + ChatFormatting.GREEN + ChatFormatting.BOLD + "Overworld");
                if (mc.player.dimension == 1)
                    notorious.messageManager.sendMessage("You died at X: " + ChatFormatting.RED + ChatFormatting.BOLD + mc.player.getPosition().getX() + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + ChatFormatting.BOLD + mc.player.getPosition().getY() + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + ChatFormatting.BOLD + mc.player.getPosition().getZ() + ChatFormatting.RESET + " Dimension: " + ChatFormatting.DARK_PURPLE + ChatFormatting.BOLD + "End");
                hasAnnounced = true;
            }
        }
    }
}