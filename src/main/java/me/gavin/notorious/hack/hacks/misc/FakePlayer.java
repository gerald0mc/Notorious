package me.gavin.notorious.hack.hacks.misc;

import com.mojang.authlib.GameProfile;
import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.rewrite.DamageUtil;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.MoverType;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.UUID;

@RegisterHack(name = "FakePlayer", description = "Spawns a fake player", category = Hack.Category.Misc)
public class FakePlayer extends Hack {

    @RegisterSetting private final BooleanSetting pops = new BooleanSetting("TotemPops", true);
    @RegisterSetting private final BooleanSetting totemPopSound = new BooleanSetting("TotemPopSound", true);
    @RegisterSetting private final BooleanSetting totemPopParticle = new BooleanSetting("TotemPopParticle", true);
    @RegisterSetting private final BooleanSetting move = new BooleanSetting("Move", true);
    @RegisterSetting private final ModeSetting mode = new ModeSetting("MovementMode", "Static", "Static", "Chase");
    @RegisterSetting private final NumSetting chaseX = new NumSetting("ChaseX", 4, 1, 120, 0.1f);
    @RegisterSetting private final NumSetting chaseY = new NumSetting("ChaseY", 2, 1, 120, 0.1f);
    @RegisterSetting private final NumSetting chaseZ = new NumSetting("ChaseZ", 2, 1, 120, 0.1f);

    public EntityOtherPlayerMP fakePlayer;

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + mode.getMode() + ChatFormatting.RESET + "]";
    }

    @Override
    protected void onEnable() {
        if (mc.world == null || mc.player == null) {
            disable();
        }else {
            UUID playerUUID = mc.player.getUniqueID();
            fakePlayer = new EntityOtherPlayerMP(mc.world, new GameProfile(UUID.fromString(playerUUID.toString()), mc.player.getDisplayNameString()));
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
        if(move.isEnabled() && !mode.getMode().equals("Chase")) {
            travel(fakePlayer.moveStrafing, fakePlayer.moveVertical, fakePlayer.moveForward);
        }
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if(mode.getMode().equals("Chase")) {
            fakePlayer.posX = mc.player.posX + chaseX.getValue();
            fakePlayer.posY = chaseY.getValue();
            fakePlayer.posZ = mc.player.posZ + chaseZ.getValue();
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event) {
        if (fakePlayer == null)
            return;

        if (event.getPacket() instanceof SPacketExplosion) {
            final SPacketExplosion explosion = (SPacketExplosion) event.getPacket();
            if (fakePlayer.getDistance(explosion.getX(), explosion.getY(), explosion.getZ()) <= 15) {
                final double damage = DamageUtil.calculateDamage(explosion.getX(), explosion.getY(), explosion.getZ(), fakePlayer);
                if (damage > 0 && pops.isEnabled()) {
                    fakePlayer.setHealth((float) (fakePlayer.getHealth() - MathHelper.clamp(damage,0, 999)));
                }
            }
        }
    }

    public void travel(float strafe, float vertical, float forward) {
        double d0 = this.fakePlayer.posY;
        float f1 = 0.8F;
        float f2 = 0.02F;
        float f3 = EnchantmentHelper.getDepthStriderModifier(this.fakePlayer);
        if (f3 > 3.0F)
            f3 = 3.0F;
        if (!this.fakePlayer.onGround)
            f3 *= 0.5F;
        if (f3 > 0.0F) {
            f1 += (0.54600006F - f1) * f3 / 3.0F;
            f2 += (this.fakePlayer.getAIMoveSpeed() - f2) * f3 / 4.0F;
        }
        this.fakePlayer.moveRelative(strafe, vertical, forward, f2);
        this.fakePlayer.move(MoverType.SELF, this.fakePlayer.motionX, this.fakePlayer.motionY, this.fakePlayer.motionZ);
        this.fakePlayer.motionX *= f1;
        this.fakePlayer.motionY *= 0.800000011920929D;
        this.fakePlayer.motionZ *= f1;
        if (!this.fakePlayer.hasNoGravity())
            this.fakePlayer.motionY -= 0.02D;
        if (this.fakePlayer.collidedHorizontally && this.fakePlayer.isOffsetPositionInLiquid(this.fakePlayer.motionX, this.fakePlayer.motionY + 0.6000000238418579D - this.fakePlayer.posY + d0, this.fakePlayer.motionZ))
            this.fakePlayer.motionY = 0.30000001192092896D;
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
        if(totemPopParticle.isEnabled()) this.mc.effectRenderer.emitParticleAtEntity(entity, EnumParticleTypes.TOTEM, 30);
        if(totemPopSound.isEnabled()) this.mc.world.playSound(entity.posX, entity.posY, entity.posZ, SoundEvents.ITEM_TOTEM_USE, entity.getSoundCategory(), 1.0F, 1.0F, false);
    }
}
