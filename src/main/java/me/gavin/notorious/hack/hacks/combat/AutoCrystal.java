package me.gavin.notorious.hack.hacks.combat;

import com.mojang.realmsclient.gui.ChatFormatting;
import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IMinecraftMixin;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.InventoryUtil;
import me.gavin.notorious.util.NColor;
import me.gavin.notorious.util.RenderUtil;
import me.gavin.notorious.util.TickTimer;
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

    @Override
    public String getMetaData() {
        return " [" + ChatFormatting.GRAY + targetName + ChatFormatting.RESET + "]";
    }

    private final TickTimer breakTickTimer = new TickTimer();
    private final TickTimer placeTickTimer = new TickTimer();

    private ArrayList<BlockPos> placedCrystals = new ArrayList<>();

    private EntityEnderCrystal targetCrystal;
    private BlockPos lastPlacedPos;
    private boolean outline = false;
    private boolean fill = false;
    private BlockPos targetedBlock = null;
    private String targetName = "";
    private int oldSlot;

    @Override
    public void onEnable() {
        oldSlot = mc.player.inventory.currentItem;
    }

    @Override
    public void onDisable() {
        if(switchMode.getMode().equals("Normal")) {
            InventoryUtil.switchToSlot(oldSlot);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent event) {
        int oldSlot = mc.player.inventory.currentItem;
        if (fastPlace.getValue()) {
            if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL) {
                ((IMinecraftMixin)mc).setRightClickDelayTimerAccessor(0);
            }
        }

        int slot = InventoryUtil.getItemSlot(Items.END_CRYSTAL);
        EnumHand hand = null;
        if (switchMode.getMode().equals("Silent")) {
            if (slot != -1) {
                if (mc.player.isHandActive()) {
                    hand = mc.player.getActiveHand();
                }
                mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
            }
        }else if (switchMode.getMode().equals("Normal")) {
            InventoryUtil.switchToSlot(slot);
        }

        if(logic.getMode().equals("PlaceBreak")) {
            doPlaceLogic();
            doBreakLogic();
        }else {
            doBreakLogic();
            doPlaceLogic();
        }
        targetedBlock = null;
    }

    @SubscribeEvent
    public void onUpdate(RenderWorldLastEvent event) {
        if(renderMode.getMode().equals("Both")) {
            outline = true;
            fill = true;
        }else if(renderMode.getMode().equals("Outline")) {
            outline = true;
            fill = false;
        }else {
            fill = true;
            outline = false;
        }
        if(targetedBlock != null) {
            final AxisAlignedBB bb = new AxisAlignedBB(targetedBlock);
            if(fill)
                RenderUtil.renderFilledBB(bb, boxColor.getAsColor());
            if(outline)
                RenderUtil.renderOutlineBB(bb, outlineColor.getAsColor());
        }
    }

    @SubscribeEvent
    public void onPacket(PacketEvent.Receive event) {
        if (!setDead.getValue())
            return;

        if (event.getPacket() instanceof SPacketSoundEffect) {
            SPacketSoundEffect sound = (SPacketSoundEffect) event.getPacket();
            if (sound.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                if (canAttackCrystal(targetCrystal))
                    targetCrystal.setDead();
            }
        }
    }

    private void doBreakLogic() {
        targetCrystal = mc.world.loadedEntityList.stream()
                .filter(e -> e.getDistance(mc.player) <= attackDistance.getValue())
                .filter(e -> e instanceof EntityEnderCrystal)
                .map(e -> (EntityEnderCrystal) e)
                .filter(this::canAttackCrystal)
                .findFirst().orElse(null);

        if (targetCrystal == null)
            return;

        if (canAttackCrystal(targetCrystal) && breakTickTimer.hasTicksPassed((long) breakDelay.getValue())) {
            mc.player.swingArm(EnumHand.MAIN_HAND);
            mc.player.connection.sendPacket(new CPacketUseEntity(targetCrystal));
            breakTickTimer.reset();
        }
    }

    EntityPlayer targetPlayer;

    private void doPlaceLogic() {

        // finding target based on highest damage
        List<BlockPos> crystalBlocks =
                getBlocksAroundPlayer(placeDistance.getValue())
                        .stream()
                        .filter(this::canPlaceCrystal)
                        .sorted(Comparator.comparing(blockPos -> mc.player.getDistanceSq(blockPos)))
                        .collect(Collectors.toList());

        BlockPos crystalPosition = null;

        for (EntityPlayer e : mc.world.playerEntities) {
            if (e.getDistance(mc.player) <= placeDistance.getValue()) {
                if (e.equals(mc.player))
                    continue;

                targetPlayer = e;

                targetName = e.getDisplayNameString();

                double bestDamage = Double.MIN_VALUE;
                BlockPos bestPos = null;

                for (BlockPos pos : crystalBlocks) {
                    // adding 0.5 to x for crystal width
                    // adding 1.0 to y because crystal will be placed above
                    double targetDamage = calculateDamage(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, targetPlayer);
                    double selfDmg = calculateDamage(pos.getX() + 0.5D, pos.getY() + 1, pos.getZ() + 0.5D, mc.player);

                    if (targetDamage >= minDmg.getValue() && selfDmg <= maxSelfDmg.getValue()) {
                        if (targetDamage > bestDamage) {
                            bestDamage = targetDamage;
                            bestPos = pos;
                            targetedBlock = pos;
                        }
                    }
                }

                if (bestPos != null) {
                    crystalPosition = bestPos;
                }
            }
        }
        if (crystalPosition != null) {
            assert crystalPosition != null;
            if (placeTickTimer.hasTicksPassed((long) placeDelay.getValue())) {
                mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(crystalPosition, EnumFacing.UP, EnumHand.MAIN_HAND, 0f, 0f, 0f));
                mc.player.swingArm(EnumHand.MAIN_HAND);
                placeTickTimer.reset();
                lastPlacedPos = crystalPosition;
            }
        }
    }

    private boolean canPlaceCrystal(BlockPos pos) {
        // checking if blocks above are air
        if (getBlock(pos.add(0, 1, 0)) == Blocks.AIR && getBlock(pos.add(0, 2, 0)) == Blocks.AIR) {

            // seeing if it is obsidian or bedrock
            if (getBlock(pos) == Blocks.OBSIDIAN || getBlock(pos) == Blocks.BEDROCK) {
                // checking if nobody is standing directly above the block
                BlockPos air1 = pos.add(0, 1, 0);
                BlockPos air2 = pos.add(0, 2, 0);

                return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(air1)).isEmpty()
                        && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(air2)).isEmpty();

            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private Block getBlock(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock();
    }

    private boolean canAttackCrystal(EntityEnderCrystal targetCrystal) {
        if (targetCrystal != null) {
            if (mc.player.getDistance(targetCrystal) <= attackDistance.getValue()) {
                return !targetCrystal.isDead;
            }
        }

        return false;
    }

    private ArrayList<BlockPos> getBlocksAroundPlayer(float range) {
        ArrayList<BlockPos> posList = new ArrayList<>();
        for (float x = -range; x < range; x++) {
            // loop for y axis
            for (float y = range + 1; y > -range; y--) {
                // loop for z axis
                for (float z = -range; z < range; z++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    posList.add(pos.add(mc.player.getPosition()));
                }
            }
        }

        return posList;
    }

    // credit to srgantmoomoo and postman for these methods
    // i was too lazy to write these 3 by myself
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