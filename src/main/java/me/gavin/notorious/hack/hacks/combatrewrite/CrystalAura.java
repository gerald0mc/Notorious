package me.gavin.notorious.hack.hacks.combatrewrite;

import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.RenderUtil;
import me.gavin.notorious.util.TimerUtils;
import me.gavin.notorious.util.zihasz.WorldUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.awt.*;

@RegisterHack(name = "AutoCrystalRewrite", description = "nigus", category = Hack.Category.CombatRewrite)
public class CrystalAura extends Hack {

	@RegisterSetting
	private final NumSetting targetRange = new NumSetting("TargetRange", 7, 0, 10, 1);

	@RegisterSetting
	private final NumSetting placeRange = new NumSetting("PlaceRange", 5, 0, 7, 1);

	@RegisterSetting
	private final NumSetting placeDelay = new NumSetting("PlaceDelay", 0, 0, 1000, 1);

	@RegisterSetting
	private final NumSetting breakDelay = new NumSetting("BreakDelay", 50, 0, 1000, 1);

	private final TimerUtils rTimer = new TimerUtils();
	private final TimerUtils pTimer = new TimerUtils();
	private final TimerUtils bTimer = new TimerUtils();

	private EntityPlayer target;
	private BlockPos renderPos;


	@Override
	protected void onEnable() {

	}

	@Override
	public void onTick() {
		doAutoCrystal();
	}

	@SubscribeEvent
	public void onRender3D(RenderWorldLastEvent event) {
		if (renderPos != null) {
			AxisAlignedBB bb = new AxisAlignedBB(renderPos);
			RenderUtil.renderFilledBB(bb, new Color(0x22ffffff));
			RenderUtil.renderOutlineBB(bb, new Color(0xffffffff));

			if (rTimer.hasTimeElapsed(500)) {
				renderPos = null;
				rTimer.reset();
			}
		}
	}

	@Override
	protected void onDisable() {
		target = null;
	}

	private void doAutoCrystal() {
		target = getTarget();

		doPlace();
		doBreak();
	}

	private void doPlace() {
		if (target == null) return;
		if (!pTimer.hasTimeElapsed((long) placeDelay.getValue())) return;

		// Debugging, just in case the null check fucks up
		FMLLog.log.info(target);

		BlockPos optimal = null;
		for (BlockPos block : WorldUtil.getSphere(mc.player.getPosition(), placeRange.getValue(), false)) {

			if (mc.world.isAirBlock(block)) continue;
			if (!isPlaceable(block)) continue;
			if (!mc.world.getEntitiesWithinAABB(Entity.class, new AxisAlignedBB(block).expand(0, 1, 0)).isEmpty()) continue;

			if (optimal == null)
				optimal = block;

			if (target.getDistanceSq(block) < target.getDistanceSq(optimal)) {
				optimal = block;
				continue;
			}
		}

		if (optimal == null) return;

		RayTraceResult result = mc.world.rayTraceBlocks(new Vec3d(optimal.getX(), optimal.getY(), optimal.getZ()), mc.player.getPositionVector());

		EnumFacing facing = result == null || result.sideHit == null ? EnumFacing.UP : result.sideHit;
		Vec3d hitVec = result == null || result.hitVec == null ? new Vec3d(0, 0, 0) : result.hitVec;

		renderPos = optimal;
		mc.playerController.processRightClickBlock(mc.player, mc.world, optimal, facing, hitVec, getHand());

		pTimer.reset();
	}

	private void doBreak() {
		if (!bTimer.hasTimeElapsed((long) breakDelay.getValue())) return;

		for (Entity entity : mc.world.loadedEntityList) {
			if (!(entity instanceof EntityEnderCrystal)) continue;
			if (entity.isDead) continue;

			mc.playerController.attackEntity(mc.player, entity);
		}

		bTimer.reset();
	}

	private EntityPlayer getTarget() {
		EntityPlayer optimal = null;

		for (EntityPlayer player : mc.world.playerEntities) {
			if (player == null) continue;
			if (player == mc.player) continue;
			if (player.isDead || player.getHealth() <= 0 || !player.isEntityAlive()) continue;

			if (notorious.friend.isFriend(player.getName())) continue;

			if (mc.player.getDistance(player) > targetRange.getValue())
				continue;

			if (optimal == null) {
				optimal = player;
				continue;
			}

			if (player.getHealth() > optimal.getHealth()) {
				optimal = player;
				continue;
			}

			if (mc.player.getDistance(player) < mc.player.getDistance(optimal)) {
				optimal = player;
				continue;
			}
		}

		return optimal;
	}

	private EnumHand getHand() {
		return mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND;
	}

	private boolean isPlaceable(BlockPos pos) {
		Block block = mc.world.getBlockState(pos).getBlock();
		return block.equals(Blocks.OBSIDIAN) || block.equals(Blocks.BEDROCK);
	}
	private boolean isHoldingCrystal() {
		return mc.player.getHeldItemMainhand().getItem().equals(Items.END_CRYSTAL) ||
				mc.player.getHeldItemOffhand().getItem().equals(Items.END_CRYSTAL);
	}

}
