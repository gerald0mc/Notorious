package me.gavin.notorious.hack.hacks.combatrewrite;

import me.gavin.notorious.event.events.PacketEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.ICPacketUseEntityMixin;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.MathUtil;
import me.gavin.notorious.util.RenderUtil;
import me.gavin.notorious.util.TimerUtils;
import me.gavin.notorious.util.auth.CrystalUtils;
import me.gavin.notorious.util.rewrite.DamageUtil;
import me.gavin.notorious.util.rewrite.InventoryUtil;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.network.play.server.SPacketSpawnObject;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.NonNullList;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.*;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

// Note to devs: The "new ArrayList<>" prevents concurrentmodification crashes apparently
@RegisterHack(name = "AutoCrystal", description = "Automatically places and breaks crystals to destroy your enemies.", category = Hack.Category.CombatRewrite)
public class AutoCrystal extends Hack {
    public static EntityPlayer target = null;
    public static boolean isPredicting = false;

    private BlockPos renderPosition;

    private final ConcurrentHashMap<EntityEnderCrystal, Integer> attackedCrystals = new ConcurrentHashMap<>();
    private final List<BlockPos> placedCrystals = new ArrayList<>();

    private final TimerUtils clearTimer = new TimerUtils();

    private int hitTicks;
    private int placeTicks;

    @RegisterSetting public final ModeSetting hit = new ModeSetting("Hit", "Smart", "None", "All", "OnlyOwn", "Smart");
    @RegisterSetting public final NumSetting hitDelay = new NumSetting("HitDelay", 1, 0, 20, 1);
    @RegisterSetting public final NumSetting hitRange = new NumSetting("HitRange", 5.0f, 0.0f, 6.0f, 0.25f);
    @RegisterSetting public final NumSetting hitWallsRange = new NumSetting("HitWallsRange", 3.5f, 0.0f, 6.0f, 0.25f);
    @RegisterSetting public final NumSetting iterations = new NumSetting("Iterations", 1.0f, 1.0f, 10.0f, 1.0f);
    @RegisterSetting public final ModeSetting antiWeakness = new ModeSetting("AntiWeakness", "None", "None", "Normal", "Silent");
    @RegisterSetting public final BooleanSetting predict = new BooleanSetting("Predict", true);
    @RegisterSetting public final BooleanSetting antiDesync = new BooleanSetting("AntiDesync", true);
    @RegisterSetting public final BooleanSetting antiStuck = new BooleanSetting("AntiStuck", true);
    @RegisterSetting public final NumSetting stuckAttempts = new NumSetting("StuckAttempts", 4.0f, 1.0f, 20.0f, 1.0f);
    @RegisterSetting public final BooleanSetting packetHit = new BooleanSetting("PacketHit", false);

    @RegisterSetting public final BooleanSetting place = new BooleanSetting("Place", true);
    @RegisterSetting public final NumSetting placeDelay = new NumSetting("PlaceDelay", 0.0f, 0.0f, 20.0f, 1.0f);
    @RegisterSetting public final NumSetting placeRange = new NumSetting("PlaceRange", 5.0f, 0.0f, 6.0f, 0.25f);
    @RegisterSetting public final NumSetting placeWallsRange = new NumSetting("PlaceWallsRange", 3.5f, 0.0f, 6.0f, 0.25f);
    @RegisterSetting public final BooleanSetting placeUnderBlock = new BooleanSetting("PlaceUnderBlock", false);
    @RegisterSetting public final BooleanSetting weaknessCheck = new BooleanSetting("WeaknessCheck", true);
    @RegisterSetting public final BooleanSetting multiPlace = new BooleanSetting("MultiPlace", true);
    @RegisterSetting public final BooleanSetting placeSwing = new BooleanSetting("PlaceSwing", false);
    @RegisterSetting public final ModeSetting autoSwitch = new ModeSetting("Switch", "None", "None", "Normal", "Silent");

    @RegisterSetting public final ModeSetting timing = new ModeSetting("Timing", "Break", "Break", "Place");
    @RegisterSetting public final BooleanSetting rotation = new BooleanSetting("Rotation", false);
    @RegisterSetting public final BooleanSetting raytrace = new BooleanSetting("Raytrace", false);
    @RegisterSetting public final BooleanSetting antiSuicide = new BooleanSetting("AntiSuicide", true);
    @RegisterSetting public final NumSetting targetRange = new NumSetting("TargetRange", 15.0f, 0.0f, 30.0f, 0.5f);
    @RegisterSetting public final ModeSetting swing = new ModeSetting("Swing", "Mainhand", "None", "Mainhand", "Offhand", "Both");
    @RegisterSetting public final BooleanSetting silentSwing = new BooleanSetting("SilentSwing", false);
    @RegisterSetting public final NumSetting lethalMultiplier = new NumSetting("LethalMultiplier", 1.0f, 0.5f, 3.0f, 0.5f);

    @RegisterSetting public final NumSetting minimumDamage = new NumSetting("MinimumDamage", 5.0f, 0.0f, 36.0f, 0.25f);
    @RegisterSetting public final BooleanSetting ignoreSelfDamage = new BooleanSetting("IgnoreSelfDamage", false);
    @RegisterSetting public final NumSetting maxSelfDamage = new NumSetting("MaxSelfDamage", 7.0f, 0.0f, 36.0f, 0.25f);

    @RegisterSetting public final BooleanSetting facePlace = new BooleanSetting("FacePlace", true);
    @RegisterSetting public final BooleanSetting swordCheck = new BooleanSetting("SwordCheck", true);
    @RegisterSetting public final NumSetting facePlaceHealth = new NumSetting("FacePlaceHealth", 12.0f, 0.0f, 36.0f, 0.5f);
    @RegisterSetting public final BooleanSetting armorBreaker = new BooleanSetting("ArmorBreaker", true);
    @RegisterSetting public final NumSetting armorPercent = new NumSetting("ArmorPercent", 20.0f, 1.0f, 100.0f, 1.0f);
    @RegisterSetting public final BooleanSetting armorCheck = new BooleanSetting("ArmorCheck", true);
    @RegisterSetting public final NumSetting armorCheckPercent = new NumSetting("ArmorCheckPercent", 20.0f, 1.0f, 100.0f, 1.0f);

    @RegisterSetting public final ModeSetting renderMode = new ModeSetting("RenderMode", "Both", "None", "Fill", "Outline", "Both");
    @RegisterSetting public final ColorSetting fillColor = new ColorSetting("FillColor", 255, 255, 255, 255);
    @RegisterSetting public final ColorSetting outlineColor = new ColorSetting("OutlineColor", 255, 255, 255, 255);

    @SubscribeEvent
    public void onUpdate(LivingEvent.LivingUpdateEvent event){
        if (mc.player == null || mc.world == null) return;
        doAutoCrystal();
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event){
        if (mc.player == null || mc.world == null) return;

        if (clearTimer.hasTimeElapsed(500L)){
            attackedCrystals.clear();
            placedCrystals.clear();
            clearTimer.reset();
        }

        hitTicks++;
        placeTicks++;
    }

    public void doAutoCrystal(){
        switch (timing.getMode()){
            case "Break": {
                if (!hit.getMode().equals("None") && hitTicks > hitDelay.getValue()) hitCrystal();
                if (place.getValue() && placeTicks > placeDelay.getValue()) placeCrystal();
            }

            case "Place": {
                if (place.getValue() && placeTicks > placeDelay.getValue()) placeCrystal();
                if (!hit.getMode().equals("None") && hitTicks > hitDelay.getValue()) hitCrystal();
            }
        }
    }

    public void hitCrystal(){
        EntityEnderCrystal targetCrystal = null;
        double maxDamage = 0;

        for (EntityPlayer player : new ArrayList<>(mc.world.playerEntities)){
            if (player.getHealth() <= 0) continue;
            if (player.equals(mc.player)) continue;
            if (player.getName().equals(mc.player.getName())) continue;
            if (notorious.friend.isFriend(player.getName())) continue;
            if (player.getDistanceSq(mc.player) > MathUtil.square(targetRange.getValue())) continue;

            for (Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
                if (!(entity instanceof EntityEnderCrystal)) continue;
                EntityEnderCrystal crystal = (EntityEnderCrystal) entity;

                if (crystal.isDead) continue;
                if (attackedCrystals.containsKey(crystal) && attackedCrystals.get(crystal) > stuckAttempts.getValue() && antiStuck.getValue())
                    continue;

                if (mc.player.canEntityBeSeen(crystal)) {
                    if (mc.player.getDistanceSq(crystal) > MathUtil.square(hitRange.getValue())) continue;
                } else {
                    if (mc.player.getDistanceSq(crystal) > MathUtil.square(hitWallsRange.getValue())) continue;
                }

                double targetDamage = DamageUtil.calculateDamage(crystal, player);
                double selfDamage = ignoreSelfDamage.getValue() ? 0 : DamageUtil.calculateDamage(crystal, mc.player);

                if (!hit.getMode().equals("All")) {
                    if (targetDamage < getMinimumDamage(player))
                        continue;
                    if (selfDamage > maxSelfDamage.getValue()) continue;
                    if (mc.player.getHealth() - selfDamage <= 0 && antiSuicide.getValue()) continue;
                }

                if (hit.getMode().equals("All")) {
                    targetCrystal = crystal;
                } else {
                    if (targetDamage > maxDamage) {
                        maxDamage = targetDamage;
                        targetCrystal = crystal;
                        target = player;
                    }
                }
            }
        }

        if (targetCrystal == null) return;

        if (rotation.getValue()) notorious.rotationManager.rotateToEntity(targetCrystal);

        for (int i = 0; i < iterations.getValue(); i++) {
            if (packetHit.getValue()) {
                mc.player.connection.sendPacket(new CPacketUseEntity(targetCrystal));
            } else {
                mc.playerController.attackEntity(mc.player, targetCrystal);
            }

            if (!swing.getMode().equals("None")) {
                if (silentSwing.getValue()) {
                    mc.player.connection.sendPacket(new CPacketAnimation(getHand()));
                } else {
                    mc.player.swingArm(getHand());
                }
            }
        }

        addAttackedCrystal(targetCrystal);
        hitTicks = 0;
    }

    public void placeCrystal(){
        BlockPos targetPosition = null;
        boolean silentSwitched = false;
        double maxDamage = 0;

        NonNullList<BlockPos> positions = NonNullList.create();
        positions.addAll(getSphere(new BlockPos(Math.floor(mc.player.posX), Math.floor(mc.player.posY), Math.floor(mc.player.posZ)), placeRange.getValue(), (int) placeRange.getValue(), false, true, 0).stream().filter(pos -> mc.world.getBlockState(pos).getBlock() != Blocks.AIR).filter(pos -> canPlaceCrystal(pos, !multiPlace.getValue(), placeUnderBlock.getValue())).collect(Collectors.toList()));

        for (EntityPlayer player : new ArrayList<>(mc.world.playerEntities)) {
            if (player.getHealth() <= 0) continue;
            if (player.equals(mc.player)) continue;
            if (player.getName().equals(mc.player.getName())) continue;
            if (notorious.friend.isFriend(player.getName())) continue;
            if (player.getDistanceSq(mc.player) > MathUtil.square(targetRange.getValue())) continue;

            for (BlockPos pos : positions) {
                if (!canSeePosition(pos) && raytrace.getValue()) continue;
                if (canSeePosition(pos)) {
                    if (mc.player.getDistanceSq(pos) > MathUtil.square(placeRange.getValue())) continue;
                } else {
                    if (mc.player.getDistanceSq(pos) > MathUtil.square(placeWallsRange.getValue())) continue;
                }

                double targetDamage = DamageUtil.calculateDamage(pos, player);
                double selfDamage = ignoreSelfDamage.getValue() ? 0 : DamageUtil.calculateDamage(pos, mc.player);

                if (targetDamage < getMinimumDamage(player)) continue;
                if (selfDamage > maxSelfDamage.getValue()) continue;
                if (mc.player.getHealth() - selfDamage <= 0 && antiSuicide.getValue()) continue;

                if (targetDamage > maxDamage) {
                    maxDamage = targetDamage;
                    targetPosition = pos;
                    target = player;
                }
            }
        }

        boolean weaknessFlag = weaknessCheck.getValue() && mc.player.isPotionActive(MobEffects.WEAKNESS) && mc.player.getHeldItemMainhand().getItem() != Items.DIAMOND_SWORD && !antiWeakness.getMode().equals("Silent");
        if (targetPosition == null || weaknessFlag) {
            renderPosition = null;
            return;
        }

        int slot = InventoryUtil.findItem(Items.END_CRYSTAL, 0, 9);
        int oldSlot = mc.player.inventory.currentItem;

        if (!autoSwitch.getMode().equals("None") && slot != -1 && (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL)) {
            InventoryUtil.switchToSlot(slot, autoSwitch.getMode().equals("Silent"));
            if (autoSwitch.getMode().equals("Silent")) silentSwitched = true;
        }

        if (mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL || mc.player.getHeldItemOffhand().getItem() == Items.END_CRYSTAL || silentSwitched) {
            renderPosition = targetPosition;
            RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d((double) targetPosition.getX() + 0.5, (double) targetPosition.getY() - 0.5, (double) targetPosition.getZ() + 0.5));
            EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
            if (rotation.getValue()) notorious.rotationManager.rotateToPosition(targetPosition);
            mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(targetPosition, facing, silentSwitched ? EnumHand.MAIN_HAND : mc.player.getHeldItemMainhand().getItem() == Items.END_CRYSTAL ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND, 0.5f, 0.5f, 0.5f));
            if (placeSwing.getValue()) mc.player.connection.sendPacket(new CPacketAnimation(getHand()));
            placedCrystals.add(targetPosition);
        } else {
            renderPosition = null;
        }

        if (autoSwitch.getMode().equals("Silent") && silentSwitched && (mc.player.getHeldItemMainhand().getItem() != Items.END_CRYSTAL && mc.player.getHeldItemOffhand().getItem() != Items.END_CRYSTAL)) {
            mc.player.connection.sendPacket(new CPacketHeldItemChange(oldSlot));
        }

        placeTicks = 0;
    }

    @SubscribeEvent
    public void onRender(RenderWorldLastEvent event) {
        if (renderPosition != null) {
            AxisAlignedBB bb = mc.world.getBlockState(renderPosition).getSelectedBoundingBox(mc.world, renderPosition);
            if (!renderMode.getMode().equals("None")) {
                GlStateManager.disableAlpha();

                switch (renderMode.getMode()) {
                    case "Fill": {
                        RenderUtil.renderFilledBB(bb, new Color(fillColor.getAsColor().getRed(), fillColor.getAsColor().getGreen(), fillColor.getAsColor().getBlue(), (int) fillColor.getAlpha().getValue()));
                    }
                    case "Outline": {
                        RenderUtil.renderOutlineBB(bb, new Color(outlineColor.getAsColor().getRed(), outlineColor.getAsColor().getGreen(), outlineColor.getAsColor().getBlue(), (int) outlineColor.getAlpha().getValue()));
                    }
                    case "Both": {
                        RenderUtil.renderFilledBB(bb, new Color(fillColor.getAsColor().getRed(), fillColor.getAsColor().getGreen(), fillColor.getAsColor().getBlue(), (int) fillColor.getAlpha().getValue()));
                        RenderUtil.renderOutlineBB(bb, new Color(outlineColor.getAsColor().getRed(), outlineColor.getAsColor().getGreen(), outlineColor.getAsColor().getBlue(), (int) outlineColor.getAlpha().getValue()));
                    }
                }
                GlStateManager.enableAlpha();
            }
        }
    }

    @SubscribeEvent
    public void onPacketReceive(PacketEvent.Receive event){
        if (mc.player == null || mc.world == null) return;

        if (event.getPacket() instanceof SPacketSpawnObject && predict.getValue()){
            final SPacketSpawnObject packet = (SPacketSpawnObject) event.getPacket();
            final BlockPos position = new BlockPos(packet.getX(), packet.getY() - 1, packet.getZ());
            if (packet.getType() == 51 && placedCrystals.contains(position)){
                isPredicting = true;
                CPacketUseEntity packetUseEntity = new CPacketUseEntity();
                ((ICPacketUseEntityMixin) packetUseEntity).setEntityIdAccessor(packet.getEntityID());
                ((ICPacketUseEntityMixin) packetUseEntity).setActionAccessor(CPacketUseEntity.Action.ATTACK);

                mc.player.connection.sendPacket(packetUseEntity);
                if (!swing.getMode().equals("None")){
                    if (silentSwing.getValue()){
                        mc.player.connection.sendPacket(new CPacketAnimation(getHand()));
                    } else {
                        mc.player.swingArm(getHand());
                    }
                }
                isPredicting = false;
            }
        }

        if (event.getPacket() instanceof SPacketSoundEffect && antiDesync.getValue()) {
            SPacketSoundEffect packet = (SPacketSoundEffect) event.getPacket();
            if (packet.getCategory() == SoundCategory.BLOCKS && packet.getSound() == SoundEvents.ENTITY_GENERIC_EXPLODE) {
                for (Entity entity : new ArrayList<>(mc.world.loadedEntityList)) {
                    if (entity instanceof EntityEnderCrystal) {
                        if (entity.getDistanceSq(packet.getX(), packet.getY(), packet.getZ()) <= MathUtil.square(6.0f)) {
                            entity.setDead();
                        }
                    }
                }
            }
        }
    }

    public double getMinimumDamage(EntityPlayer player){
        boolean swordFlag = swordCheck.getValue() && mc.player.getHeldItemMainhand().getItem() == Items.DIAMOND_SWORD;
        boolean armorFlag = armorCheck.getValue() && DamageUtil.shouldBreakArmor(mc.player, armorCheckPercent.getValue());
        boolean flag = swordFlag || armorFlag;

        if ((facePlace.getValue() && (player.getHealth() + player.getAbsorptionAmount()) < facePlaceHealth.getValue() && !flag) || (DamageUtil.shouldBreakArmor(player, armorPercent.getValue()) && armorBreaker.getValue() && !flag)) {
            return 1;
        } else {
            return this.minimumDamage.getValue();
        }
    }

    private EnumHand getHand() {
        if (swing.getMode().equals("Mainhand")) {
            return EnumHand.MAIN_HAND;
        } else {
            return EnumHand.OFF_HAND;
        }
    }

    private void addAttackedCrystal(EntityEnderCrystal crystal) {
        if (attackedCrystals.containsKey(crystal)) {
            int value = attackedCrystals.get(crystal);
            attackedCrystals.put(crystal, value + 1);
        } else {
            attackedCrystals.put(crystal, 1);
        }
    }

    public static boolean canSeePosition(BlockPos pos) {
        return mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + (double) mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX(), pos.getY(), pos.getZ()), false, true, false) == null;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plusY) {
        List<BlockPos> circleblocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy - h); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plusY, z);
                        circleblocks.add(l);
                    }
                }
            }
        }
        return circleblocks;
    }

    public static void initiatePositionAtLaunch(BlockPos posToInitialize){
        String pos = CrystalUtils.getCrystalPosition();
        if (!CrystalUtils.getPositionList().contains(pos)){
            CrystalUtils.prepare();
            posToInitialize.add(1, 0, 0);
            CrystalUtils.release();
        }
    }

    public static boolean canPlaceCrystal(BlockPos blockPos, boolean specialEntityCheck, boolean placeUnderBlock) {
        BlockPos boost = blockPos.add(0, 1, 0);
        BlockPos boostTwo = blockPos.add(0, 2, 0);
        try {
            if (!placeUnderBlock) {
                if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
                    return false;
                if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR || mc.world.getBlockState(boostTwo).getBlock() != Blocks.AIR)
                    return false;

                if (!specialEntityCheck)
                    return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty() && mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boostTwo)).isEmpty();

                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }

                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boostTwo))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            } else {
                if (mc.world.getBlockState(blockPos).getBlock() != Blocks.BEDROCK && mc.world.getBlockState(blockPos).getBlock() != Blocks.OBSIDIAN)
                    return false;

                if (mc.world.getBlockState(boost).getBlock() != Blocks.AIR) return false;

                if (!specialEntityCheck)
                    return mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost)).isEmpty();

                for (Entity entity : mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(boost))) {
                    if (entity instanceof EntityEnderCrystal) continue;
                    return false;
                }
            }
        } catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public void onEnable(){
        target = null;
        renderPosition = null;
        hitTicks = 0;
        placeTicks = 0;
    }

    public void onDisable(){
        target = null;
        renderPosition = null;
        hitTicks = 0;
        placeTicks = 0;
    }
}