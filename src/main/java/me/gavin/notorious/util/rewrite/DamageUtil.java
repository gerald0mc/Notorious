package me.gavin.notorious.util.rewrite;

import me.gavin.notorious.stuff.IMinecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.CombatRules;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.Explosion;

import java.util.Objects;

public class DamageUtil implements IMinecraft {
    public static float calculateDamage(EntityEnderCrystal crystal, EntityPlayer player) {
        return calculateDamage(crystal.posX, crystal.posY, crystal.posZ, player);
    }

    public static float calculateDamage(BlockPos position, EntityPlayer player) {
        return calculateDamage(position.getX() + 0.5, position.getY() + 1.0, position.getZ() + 0.5, player);
    }

    public static float calculateDamage(double posX, double posY, double posZ, Entity entity) {
        float doubleSize = 12.0F;
        double size = entity.getDistance(posX, posY, posZ) / (double) doubleSize;
        Vec3d vec3d = new Vec3d(posX, posY, posZ);
        double value = (1.0D - size) * entity.world.getBlockDensity(vec3d, entity.getEntityBoundingBox());
        float damage = (float) ((int) ((value * value + value) / 2.0D * 7.0D * (double) doubleSize + 1.0D));
        double finalDamage = 1.0D;

        if (entity instanceof EntityLivingBase) {
            finalDamage = getBlastReduction((EntityLivingBase) entity, getMultipliedDamage(damage), new Explosion(mc.world, null, posX, posY, posZ, 6.0F, false, true));
        }

        return (float) finalDamage;
    }

    public static float getBlastReduction(final EntityLivingBase entity, float damage, final Explosion explosion) {
        try {
            if (entity instanceof EntityPlayer) {
                final EntityPlayer player = (EntityPlayer) entity;
                final DamageSource source = DamageSource.causeExplosionDamage(explosion);

                damage = CombatRules.getDamageAfterAbsorb(damage, (float) player.getTotalArmorValue(), (float) player.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
                final float modifier = MathHelper.clamp((float) EnchantmentHelper.getEnchantmentModifierDamage(player.getArmorInventoryList(), source), 0.0f, 20.0f);
                damage *= 1.0f - modifier / 25.0f;

                if (entity.isPotionActive(Objects.requireNonNull(Potion.getPotionById(11)))) damage -= damage / 4.0f;
                return damage;
            }
        } catch (NullPointerException exception){
            exception.printStackTrace();
        }

        damage = CombatRules.getDamageAfterAbsorb(damage, (float) entity.getTotalArmorValue(), (float) entity.getEntityAttribute(SharedMonsterAttributes.ARMOR_TOUGHNESS).getAttributeValue());
        return damage;
    }

    private static float getMultipliedDamage(float damage) {
        return damage * (mc.world.getDifficulty().getId() == 0 ? 0.0F : (mc.world.getDifficulty().getId() == 2 ? 1.0F : (mc.world.getDifficulty().getId() == 1 ? 0.5F : 1.5F)));
    }

    public static boolean shouldBreakArmor(EntityPlayer player, float targetPercent) {
        for (ItemStack stack : player.getArmorInventoryList()) {
            if (stack == null || stack.getItem() == Items.AIR) return true;
            float armorPercent = ((float) (stack.getMaxDamage() - stack.getItemDamage()) / stack.getMaxDamage()) * 100.0f;
            if (targetPercent >= armorPercent && stack.getCount() < 2) return true;
        }

        return false;
    }
}
