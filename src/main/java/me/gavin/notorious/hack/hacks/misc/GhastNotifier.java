package me.gavin.notorious.hack.hacks.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.init.SoundEvents;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Keyboard;

import java.util.HashSet;
import java.util.Set;

@RegisterHack(name = "GhastNotifier", description = "", category = Hack.Category.Misc)
public class GhastNotifier extends Hack {

    @RegisterSetting
    public final BooleanSetting playSound = new BooleanSetting("PlaySound", true);
    @RegisterSetting
    public final BooleanSetting glow = new BooleanSetting("Glow", true);

    private Set<Entity> ghasts = new HashSet<Entity>();

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + ghasts.toString() + ChatFormatting.RESET + "]";
    }

    @Override
    public void onEnable() {
        ghasts.clear();
    }

//    @SubscribeEvent
//    public void onUpdate(LivingEvent.LivingUpdateEvent event) {
//        for(Entity e : mc.world.loadedEntityList) {
//            if(!(e instanceof EntityGhast) || ghasts.contains(e))
//                continue;
//            notorious.messageManager.sendMessage("Ghast detected at X: " + ChatFormatting.RED + ChatFormatting.BOLD + e.getPosition().getX() + ChatFormatting.RESET + " Y: " + ChatFormatting.RED + ChatFormatting.BOLD +  e.getPosition().getY() + ChatFormatting.RESET + " Z: " + ChatFormatting.RED + ChatFormatting.BOLD + e.getPosition().getZ() + ChatFormatting.RESET + "!");
//            ghasts.add(e);
//            if(playSound.isEnabled())
//                mc.player.playSound(SoundEvents.BLOCK_ANVIL_DESTROY, 1.0f, 1.0f);
//            if(glow.isEnabled())
//                e.setGlowing(true);
//        }
//    }
}
