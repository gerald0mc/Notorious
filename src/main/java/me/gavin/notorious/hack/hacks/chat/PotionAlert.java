package me.gavin.notorious.hack.hacks.chat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.init.MobEffects;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import org.lwjgl.input.Keyboard;

@RegisterHack(name = "PotionAlert", description = "Tells you in chat when you are hit by a arrow.", category = Hack.Category.Chat)
public class PotionAlert extends Hack {

    @RegisterSetting
    public final BooleanSetting weakness = new BooleanSetting("Weakness", true);
    @RegisterSetting
    public final BooleanSetting slowness = new BooleanSetting("Slowness", true);

    private boolean hasAnnouncedWeakness = false;
    private boolean hasAnnouncedSlowness = false;

    @SubscribeEvent
    public void onUpdate(TickEvent.PlayerTickEvent event) {
        if(weakness.isEnabled()) {
            if(mc.player.isPotionActive(MobEffects.WEAKNESS) && !hasAnnouncedWeakness) {
                notorious.messageManager.sendMessage("RIP bro you now have " + ChatFormatting.GRAY + ChatFormatting.BOLD + "WEAKNESS" + ChatFormatting.RESET + "!");
                hasAnnouncedWeakness = true;
            }
            if(!mc.player.isPotionActive(MobEffects.WEAKNESS) && hasAnnouncedWeakness) {
                notorious.messageManager.sendMessage("Ey bro good job you don't have " + ChatFormatting.GRAY + ChatFormatting.BOLD + "WEAKNESS" + ChatFormatting.RESET + " anymore!");
                hasAnnouncedWeakness = false;
            }
        }
        if(slowness.isEnabled()) {
            if(mc.player.isPotionActive(MobEffects.SLOWNESS) && !hasAnnouncedSlowness) {
                notorious.messageManager.sendMessage("RIP bro you now have " + ChatFormatting.AQUA + ChatFormatting.BOLD + "SLOWNESS" + ChatFormatting.RESET + "!");
                hasAnnouncedSlowness = true;
            }
            if(!mc.player.isPotionActive(MobEffects.SLOWNESS) && hasAnnouncedSlowness) {
                notorious.messageManager.sendMessage("Ey bro good job you don't have " + ChatFormatting.AQUA + ChatFormatting.BOLD + "SLOWNESS" + ChatFormatting.RESET + " anymore!");
                hasAnnouncedSlowness = false;
            }
        }
    }
}
