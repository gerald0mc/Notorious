package me.gavin.notorious.hack.hacks.misc;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.friend.Friend;
import me.gavin.notorious.friend.Friends;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.lwjgl.input.Mouse;

@RegisterHack(name = "MiddleClickFriend", description = "ez", category = Hack.Category.Misc)
public class MiddleClickFriend extends Hack {

    boolean hasClicked = false;

    @SubscribeEvent
    public void onUpdate(PlayerLivingUpdateEvent event) {
        if(!Mouse.isButtonDown(2)) {
            hasClicked = false;
            return;
        }
        if(!hasClicked) {
            hasClicked = true;
            final RayTraceResult result = mc.objectMouseOver;
            if(result == null || result.typeOfHit != RayTraceResult.Type.ENTITY || !(result.entityHit instanceof EntityPlayer))
                return;
            for(Friend f : Friends.getFriends()) {
                if(Friends.isFriend(mc.objectMouseOver.entityHit.getName())) {
                    Friends.delFriend(mc.objectMouseOver.entityHit.getName());
                    notorious.messageManager.sendMessage(ChatFormatting.RED + "Removed " + ChatFormatting.LIGHT_PURPLE + mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " from friends list");
                }else {
                    Friends.addFriend(mc.objectMouseOver.entityHit.getName());
                    notorious.messageManager.sendMessage(ChatFormatting.GREEN + "Added " + ChatFormatting.LIGHT_PURPLE + mc.objectMouseOver.entityHit.getName() + ChatFormatting.WHITE + " to friends list");
                }
            }
        }
    }
}
