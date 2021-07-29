package me.gavin.notorious.hack.hacks.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import net.minecraft.client.entity.EntityOtherPlayerMP;

import java.util.UUID;

@RegisterHack(name = "FakePlayer", description = "Spawns a fake player", category = Hack.Category.Misc)
public class FakePlayer extends Hack {

    private EntityOtherPlayerMP fakePlayer;

    @Override
    protected void onEnable() {
        UUID playerUUID = mc.player.getUniqueID();
        if (mc.world == null || mc.player == null) {
            disable();
        } else {
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString(playerUUID.toString()), "gerald0mc"));
            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.inventory.copyInventory(mc.player.inventory);
            mc.world.addEntityToWorld(-7777, fakePlayer);
            notorious.messageManager.sendMessage("Added a " + ChatFormatting.GREEN + "fake player" + ChatFormatting.RESET + " to your world.");
        }
    }

    @Override
    protected void onDisable() {
        if (fakePlayer != null && mc.world != null) {
            mc.world.removeEntityFromWorld(-7777);
            notorious.messageManager.sendMessage("Removed a " + ChatFormatting.RED + "fake player" + ChatFormatting.RESET + " from your world.");
            fakePlayer = null;
        }
    }
}
