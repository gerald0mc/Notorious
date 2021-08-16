package me.gavin.notorious.hack.hacks.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.hack.hacks.combat.AutoCrystal;
import me.gavin.notorious.setting.BooleanSetting;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@RegisterHack(name = "FakePlayer", description = "Spawns a fake player", category = Hack.Category.Misc)
public class FakePlayer extends Hack {

    @RegisterSetting
    private final BooleanSetting pops = new BooleanSetting("Totem Pops", true);

    public EntityOtherPlayerMP fakePlayer;

    @Override
    protected void onEnable() {
        if (mc.world == null || mc.player == null) {
            disable();
        }else {
            UUID playerUUID = mc.player.getUniqueID();
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString(playerUUID.toString()), "gerald0mc"));
            fakePlayer.copyLocationAndAnglesFrom(mc.player);
            fakePlayer.inventory.copyInventory(mc.player.inventory);
            mc.world.addEntityToWorld(-7777, fakePlayer);
            notorious.messageManager.sendMessage("Added a " + ChatFormatting.GREEN + "fake player" + ChatFormatting.RESET + " to your world.");
        }
    }

    @SubscribeEvent
    public void onTick(PlayerLivingUpdateEvent event) {
        if (pops.getValue()) {
            if (fakePlayer != null) {
                fakePlayer.inventory.offHandInventory.set(0, new ItemStack(Items.TOTEM_OF_UNDYING));
                if (fakePlayer.getHealth() <= 0) {
                    fakePop(fakePlayer);
                    fakePlayer.setHealth(20f);
                    notorious.popListener.handlePop(fakePlayer);
                }
            }
        }
    }


    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (fakePlayer == null)
            return;

        if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion explosion = (SPacketExplosion) event.getPacket();
            if (fakePlayer.getDistance(explosion.getX(), explosion.getY(), explosion.getZ()) <= 15) {
                final double damage = AutoCrystal.calculateDamage(explosion.getX(), explosion.getY(), explosion.getZ(), fakePlayer);
                if (damage > 0) {
                    fakePlayer.setHealth((float) (fakePlayer.getHealth() - MathHelper.clamp(damage,0, 999)));
                }
            }
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

    private void fakePop(Entity entity) {
        this.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, 30);
        this.mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
    }
}
