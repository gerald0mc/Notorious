package me.gavin.notorious.hack.hacks.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.util.MessageUtil;
import net.minecraft.client.gui.GuiGameOver;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "AutoRespawn", description = "Respawn automatically", category = Hack.Category.Player)
public class AutoRespawn extends Hack {

    @RegisterSetting
    public final BooleanSetting antiDeathScreen = new BooleanSetting("AntiDeathScreen", true);
    @RegisterSetting
    public final BooleanSetting deathCoords = new BooleanSetting("DeathCoords", true);

    public boolean hasAnnounced = false;

    @SubscribeEvent
    public void onDeath(GuiOpenEvent event) {
        if(hasAnnounced = true) {
            hasAnnounced = false;
        }
        if (event.getGui() instanceof GuiGameOver) {
            if (antiDeathScreen.isEnabled())
                event.setCanceled(true);
            if (mc.player.getHealth() <= 0f)
                mc.player.respawnPlayer();
            if(deathCoords.isEnabled()) {
                if(!hasAnnounced) {
                    if (mc.player.dimension == -1)
                        MessageUtil.sendRawMessage("You died at X: " + ChatFormatting.RED + mc.player.getPosition().getX() + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + mc.player.getPosition().getY() + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + mc.player.getPosition().getZ() + ChatFormatting.RESET + " Dimension: " + ChatFormatting.RED + "Nether");
                    if (mc.player.dimension == 0)
                        MessageUtil.sendRawMessage("You died at X: " + ChatFormatting.RED + mc.player.getPosition().getX() + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + mc.player.getPosition().getY() + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + mc.player.getPosition().getZ() + ChatFormatting.RESET + " Dimension: " + ChatFormatting.GREEN + "Overworld");
                    if (mc.player.dimension == 1)
                        MessageUtil.sendRawMessage("You died at X: " + ChatFormatting.RED + mc.player.getPosition().getX() + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + mc.player.getPosition().getY() + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + mc.player.getPosition().getZ() + ChatFormatting.RESET + " Dimension: " + ChatFormatting.DARK_PURPLE + "End");
                    hasAnnounced = true;
                }else {
                    hasAnnounced = false;
                }
            }
        }
    }
}