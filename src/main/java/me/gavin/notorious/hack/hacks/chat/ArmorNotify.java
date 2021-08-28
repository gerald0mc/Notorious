package me.gavin.notorious.hack.hacks.chat;

import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.friend.Friend;
import me.gavin.notorious.friend.Friends;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.ColorUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@RegisterHack(name = "ArmorNotify", description = "ez", category = Hack.Category.Chat)
public class ArmorNotify extends Hack {

    @RegisterSetting
    public final NumSetting x = new NumSetting("X", 2.0f, 0.1f, 1000.0f, 0.1f);
    @RegisterSetting
    public final NumSetting y = new NumSetting("Y", 2.0f, 0.1f, 600.0f, 0.1f);
    @RegisterSetting
    public final BooleanSetting rainbow = new BooleanSetting("Rainbow", true);

    boolean hasAnnounced = false;

    @SubscribeEvent
    public void onChat(PlayerLivingUpdateEvent event) {
        for(Entity e : mc.world.loadedEntityList) {
            boolean armorDurability = getArmorDurability();
            if(e.equals(mc.player))
                return;
            if(e instanceof EntityPlayer) {
                if(notorious.friend.isFriend(e.getName())) {
                    if(armorDurability && !hasAnnounced) {
                        mc.player.sendChatMessage("/msg " + ((EntityPlayer) e).getDisplayNameString() + " Hey bro you need to mend your armor :o");
                        hasAnnounced = true;
                    }
                    if(armorAboveSeventyFive()) {
                        hasAnnounced = false;
                    }
                }
            }
        }
    }

    @SubscribeEvent
    public void onUpdate(RenderGameOverlayEvent.Text event) {
        boolean armorDurability = getArmorDurability();

        if(armorDurability) {
            mc.fontRenderer.drawStringWithShadow("Armor is below 50%", x.getValue(), y.getValue(), rainbow.getValue() ? ColorUtil.getRainbow(6f, 1f) : -1);
        }
    }

    private boolean getArmorDurability(){
        for(ItemStack itemStack : mc.player.inventory.armorInventory){
            if ((itemStack.getMaxDamage()/2) < itemStack.getItemDamage()){
                return true;
            }
        }
        return false;
    }

    private boolean armorAboveSeventyFive() {
        for(ItemStack itemStack : mc.player.inventory.armorInventory) {
            if((itemStack.getMaxDamage()/3) < itemStack.getMaxDamage()) {
                return true;
            }
        }
        return false;
    }
}
