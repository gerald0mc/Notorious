package me.gavin.notorious.hack.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import com.sun.istack.internal.NotNull;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IMinecraftMixin;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.*;
import net.minecraft.block.Block;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemEndCrystal;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RegisterHack(name = "AutoGerald", description = "ez", category = Hack.Category.Combat)
public class AutoCrystal extends Hack {

    @RegisterSetting
    private final NumSetting range = new NumSetting("Range", 4.5f, 1f, 6f, 0.5f);

    @RegisterSetting
    private NumSetting attackDistance = new NumSetting("AttackRange", 4f, 1f, 6f, 1f);
    @RegisterSetting
    private NumSetting placeDistance = new NumSetting("PlaceRange", 4f, 1f, 6f, 1f);
    @RegisterSetting
    private NumSetting minDmg = new NumSetting("MinDmg", 4f, 0.1f, 10.0f, 1f);
    @RegisterSetting
    private NumSetting maxSelfDmg = new NumSetting("MaxSelfDmg", 15f, 1f, 30f, 1f);
    @RegisterSetting
    private NumSetting breakDelay = new NumSetting("BreakDelay", 2f, 0f, 20f, 1f);
    @RegisterSetting
    private NumSetting placeDelay = new NumSetting("PlaceDelay", 2f, 0f, 20f, 1f);
    @RegisterSetting
    private ModeSetting logic = new ModeSetting("Logic", "PlaceBreak", "PlaceBreak", "BreakPlace");
    @RegisterSetting
    private ModeSetting switchMode = new ModeSetting("SwitchMode", "Silent", "Silent", "Normal", "None");
    @RegisterSetting
    public final ModeSetting renderMode = new ModeSetting("RenderMode", "Both", "Both", "Outline", "Fill");
    @RegisterSetting
    public final ColorSetting boxColor = new ColorSetting("Box", new NColor(255, 255, 255, 125));
    @RegisterSetting
    public final ColorSetting outlineColor = new ColorSetting("Outline", new NColor(255, 255, 255, 255));
    @RegisterSetting
    private BooleanSetting setDead = new BooleanSetting("SetDead", true);
    @RegisterSetting
    private BooleanSetting fastPlace = new BooleanSetting("FastPlace",true);

    private EntityPlayer targetPlayer = null;

    /* :flushed: */
    @SubscribeEvent
    public void onEjaculate(PlayerLivingUpdateEvent event) {
        if (logic.getMode().equals("PlaceBreak")) {
            place();
            break_();
        } else {
            break_();
            place();
        }
    }

    private void place() {
        if (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL)
            return;

        if (targetPlayer == null) {
            targetPlayer = findPlayerTarget();
        } else {
            if (!isTargetStillViable(targetPlayer)) {
                targetPlayer = null;
                return;
            }

            final BlockPos pos = getBestPlacementPosition(targetPlayer);
            if (pos != null) {
                mc.playerController.processRightClickBlock(mc.player, mc.world, pos, EnumFacing.UP, Vec3d.ZERO, EnumHand.MAIN_HAND);
                mc.player.swingArm(EnumHand.MAIN_HAND);
            }
        }
    }

    private BlockPos getBestPlacementPosition(@NotNull EntityPlayer player) {
        double bestDamage = -1;
        BlockPos bestPosition = null;
        for (BlockPos pos : BlockUtil.getSurroundingBlocks((int)placeDistance.getValue(), true)) {
            if (canPlaceCrystal(pos)) {
                double damage = calculateDamage(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, player);
                if (damage > bestDamage) {
                    bestDamage = damage;
                    bestPosition = pos;
                }
            }
        }

        return bestPosition;
    }

    private boolean canPlaceCrystal(@NotNull BlockPos pos) {
        return getBlock(pos) == Blocks.OBSIDIAN
                && getBlock(pos.add(0, 1, 0)) == Blocks.AIR
                && getBlock(pos.add(0, 2, 0)) == Blocks.AIR
                && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(pos.add(0, 1, 0))).size() == 0;
    }

    private Block getBlock(@NotNull BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    private void break_() {

    }

    private EntityPlayer findPlayerTarget() {
        return mc.world.playerEntities.stream()
                .filter(player -> !player.equals(mc.player))
                .filter(player -> player.getDistance(mc.player) <= range.getValue())
                .filter(player -> player.getHealth() > 0)
                .filter(EntityLivingBase::isEntityAlive)
                .filter(player -> !player.isDead)
                .findFirst().orElse(null);
    }

    private boolean isTargetStillViable(EntityPlayer player) {
        return  player.getDistance(mc.player) <= range.getValue() && player.getHealth() > 0 && player.isEntityAlive() && !player.isDead;
    }

//    private EntityEnderCrystal findCrystalTarget() {
//
//    }

    public float getBlastReduction(EntityLivingBase entity, float damage, Explosion explosion) {
        if (entity instanceof EntityPlayer) {
            EntityPlayer ep = (EntityPlayer) entity;
            DamageSource ds = DamageSource.causeExplosionDamage(explosion);
            damage = CombatRules.getDamageAfterAbsorb(damage, (float) ep.getTotalArmorValue(), (float) ep.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());

            int k = EnchantmentHelper.getEnchantmentModifierDamage(ep.getArmorInventoryList(), ds);
            float f = MathHelper.clamp(k, 0.0F, 20.0F);
            damage *= 1.0F - f / 25.0F;

            if (entity.isPotionActive(Potion.getPotionById(11))) {
                damage = damage - (damage / 4);
            }
            damage = Math.max(damage, 0.0F);
            return damage;
        }
        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private float getDamageMultiplied(float damage) {
        int diff = mc.world.getDifficulty().getId();
        return damage * (diff == 0 ? 0 : (diff == 2 ? 1 : (diff == 1 ? 0.5f : 1.5f)));
    }

    public float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleExplosionSize = 12.0F;
        double distancedsize = entity.getDistance(posX, posY, posZ) / (double) doubleExplosionSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double blockDensity = entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        double v = (1.0D - distancedsize) * blockDensity;
        float damage = (float) ((int) ((v * v + v) / 2.0D * 7.0D * (double) doubleExplosionSize + 1.0D));
        double finald = 1.0D;

        if (entity instanceof EntityLivingBase) {
            finald = getBlastReduction((EntityLivingBase) entity, getDamageMultiplied(damage), new Explosion(mc.world, null, posX, posY, posZ, 6F, false, true));
        }
        return (float) finald;
    }
}