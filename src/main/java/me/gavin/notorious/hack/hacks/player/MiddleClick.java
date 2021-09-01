package me.gavin.notorious.hack.hacks.player;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IMinecraftMixin;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Mouse;

@RegisterHack(name = "MiddleClick", description = "ez", category = Hack.Category.Player)
public class MiddleClick extends Hack {

    @RegisterSetting
    public final BooleanSetting friend = new BooleanSetting("Friend", false);
    @RegisterSetting
    public final BooleanSetting xp = new BooleanSetting("SilentXP", true);
    @RegisterSetting
    public final BooleanSetting footXP = new BooleanSetting("FootXP", true);
    @RegisterSetting
    public final BooleanSetting pearl = new BooleanSetting("Pearl", false);

    private boolean mouseHolding = false;
    private int serverSlot = -1;

    @SubscribeEvent
    public void onMouse(InputEvent.MouseInputEvent event) {
        if (Mouse.getEventButton() == 2) {
            if (Mouse.getEventButtonState()) {
                mouseHolding = true;
            }else {
                mouseHolding = false;
                if(xp.isEnabled())
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                serverSlot = -1;
            }
        }
    }

    @SubscribeEvent
    public void onMouse(PlayerLivingUpdateEvent event) {
        if (mouseHolding) {
            if(friend.isEnabled()) {
                mouseHolding = false;
                final RayTraceResult result = mc.objectMouseOver;
                if (result == null || result.typeOfHit != RayTraceResult.Type.ENTITY || !(result.entityHit instanceof EntityPlayer))
                    return;
                if (notorious.friend.isFriend(mc.objectMouseOver.entityHit.getName())) {
                    notorious.friend.delFriend(mc.objectMouseOver.entityHit.getName());
                    notorious.messageManager.sendMessage(ChatFormatting.RED + "Removed " + ChatFormatting.LIGHT_PURPLE + mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " from friends list");
                } else {
                    notorious.friend.addFriend(mc.objectMouseOver.entityHit.getName());
                    notorious.messageManager.sendMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.LIGHT_PURPLE + mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " to friends list");
                }
            }
            if(xp.isEnabled()) {
                int getSlot = getXPSlot();

                if (getSlot != -1) {
                    if (serverSlot == -1) {
                        mc.player.connection.sendPacket(new CPacketHeldItemChange(getSlot));
                        serverSlot = getSlot;
                    } else {
                        if (mc.player.inventory.getStackInSlot(serverSlot).getItem() != Items.EXPERIENCE_BOTTLE) {
                            serverSlot = -1;
                        } else {
                            if(footXP.isEnabled()) {
                                mc.player.connection.sendPacket(new CPacketPlayer.Rotation(mc.player.rotationYaw, 90.0f, mc.player.onGround));
                            }
                            ((IMinecraftMixin) mc).setRightClickDelayTimerAccessor(0);
                            mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                        }
                    }
                }
            }
            if(pearl.isEnabled()) {
                for (int i = 0; i < 9; ++i) {
                    mouseHolding = false;
                    ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
                    if (itemStack.getItem() != Items.ENDER_PEARL) continue;
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(i));
                    mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
                    mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
                }
            }
        }
    }

    private int getXPSlot() {
        final Item item = Items.EXPERIENCE_BOTTLE;
        final Minecraft mc = Minecraft.getMinecraft();
        int itemSlot = mc.player.getHeldItemMainhand().getItem() == item ? mc.player.inventory.currentItem : -1;
        if (itemSlot == -1) {
            for (int l = 0; l < 9; ++l) {
                if (mc.player.inventory.getStackInSlot(l).getItem() == item) {
                    itemSlot = l;
                }
            }
        }

        return itemSlot;
    }
}
