package me.gavin.notorious.hack.hacks.player;

import me.gavin.notorious.event.events.BlockEvent;
import me.gavin.notorious.event.events.PlayerLivingUpdateEvent;
import me.gavin.notorious.hack.Hack;
import me.gavin.notorious.hack.RegisterHack;
import me.gavin.notorious.hack.RegisterSetting;
import me.gavin.notorious.mixin.mixins.accessor.IPlayerControllerMPMixin;
import me.gavin.notorious.setting.BooleanSetting;
import me.gavin.notorious.setting.ColorSetting;
import me.gavin.notorious.setting.ModeSetting;
import me.gavin.notorious.setting.NumSetting;
import me.gavin.notorious.util.InventoryUtil;
import me.gavin.notorious.util.RenderUtil;
import net.minecraft.init.Items;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.awt.*;

@RegisterHack(name = "SpeedMine", description = "Mine blocks faster and better.", category = Hack.Category.Player)
public class SpeedMine extends Hack {

	@RegisterSetting
	public final ModeSetting modeSetting = new ModeSetting("Mode", "Packet", "Damage", "Packet", "Instant", "Breaker");
	@RegisterSetting
	public final NumSetting endDamage = new NumSetting("End Damage", .8f, 0f, 1f, .1f);
	@RegisterSetting
	public final NumSetting range = new NumSetting("Range", 5, 0, 10, 1);
	@RegisterSetting
	public final BooleanSetting render = new BooleanSetting("Render", true);
	@RegisterSetting
	public final ColorSetting color = new ColorSetting("Mining Color", new Color(0xffaaffee));

	private BlockPos pos;
	private EnumFacing facing;

	@SubscribeEvent
	public void onTick(PlayerLivingUpdateEvent event) {
		if (mc.player == null || mc.world == null) return;

		if (pos == null || facing == null) return;

		if (modeSetting.getMode().equalsIgnoreCase("Breaker"))
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));

		if (mc.player.getDistance(pos.getX(), pos.getY(), pos.getZ()) < range.getValue()) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.ABORT_DESTROY_BLOCK, pos, facing));
			reset();
		}
	}

	@Override
	protected void onDisable() {
		reset();
	}

	@SubscribeEvent
	public void onRenderWorldLast(RenderWorldLastEvent event) {
		if (mc.player == null || mc.world == null) return;

		if (pos == null || facing == null) return;

		if (render.getValue() && pos != null) {
			AxisAlignedBB bb = new AxisAlignedBB(new BlockPos(pos));
			RenderUtil.renderFilledBB(bb, color.getAsColor());
			RenderUtil.renderOutlineBB(bb, color.getAsColor());
		}
	}

	@SubscribeEvent
	public void onBlockClick(BlockEvent.Click event) {
		if (mc.player == null || mc.world == null) return;

		if (!canBreak(event.getPos())) return;

		if (pos != null && facing != null) {
			pos = event.getPos();
			facing = event.getFacing();
		}

		if (!modeSetting.getMode().equalsIgnoreCase("Damage")) {
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.START_DESTROY_BLOCK, pos, facing));
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, pos, facing));
			if (modeSetting.getMode().equalsIgnoreCase("Instant"))
				mc.world.setBlockToAir(pos);
		}
	}

	@SubscribeEvent
	public void onBlockDamage(BlockEvent.Damage event) {
		if (mc.player == null || mc.world == null) return;

		if (modeSetting.getMode().equalsIgnoreCase("Damage") && ((IPlayerControllerMPMixin) mc.playerController).getCurBlockDamageMP() >= endDamage.getValue())
			((IPlayerControllerMPMixin) mc.playerController).setCurBlockDamageMP(1f);
	}

	@SubscribeEvent
	public void onBlockDestroy(BlockEvent.Destroy event) {
		if (mc.player == null || mc.world == null) return;
		reset();
	}

	public void reset() {
		pos = null;
		facing = null;
	}

	private boolean canBreak(BlockPos pos) {
		return mc.world.getBlockState(pos).getBlockHardness(mc.world, pos) != -1;
	}

}
