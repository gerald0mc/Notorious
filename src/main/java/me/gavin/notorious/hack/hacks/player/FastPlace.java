package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IMinecraftMixin;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * @author Gav06
 * @since 6/15/2021
 */

@RegisterHack(name = "FastPlace", description = "Use items faster", category = Hack.Category.Player)
public class FastPlace extends Hack {

    @RegisterSetting
    public final BooleanSetting xp = new BooleanSetting("XP", true);
    @RegisterSetting
    public final BooleanSetting crystals = new BooleanSetting("Crystals", true);
    @RegisterSetting
    public final BooleanSetting all = new BooleanSetting("All", false);

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if(mc.player.getHeldItemMainhand().getItem() == Items.EXPERIENCE_BOTTLE && xp.isEnabled()) {
            ((IMinecraftMixin)mc).setRightClickDelayTimerAccessor(0);
        }
        if(mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL && crystals.isEnabled()) {
            ((IMinecraftMixin)mc).setRightClickDelayTimerAccessor(0);
        }
        if(all.isEnabled()) {
            ((IMinecraftMixin)mc).setRightClickDelayTimerAccessor(0);
        }
    }
}