package me.gavin.notorious.util.zihasz;

import me.gavin.notorious.util.Instance;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.List;

public class WorldUtil implements Instance {

	public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
		List<BlockPos> blocks = new ArrayList<>();

		int cx = loc.getX();
		int cy = loc.getY();
		int cz = loc.getZ();

		for (int x = cx - (int) r; x <= cx + r; x++) {
			for (int z = cz - (int) r; z <= cz + r; z++) {
				for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
					double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
					if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
						blocks.add(new BlockPos(x, y + plus_y, z));
					}
				}
			}
		}
		return blocks;
	}

	public static List<BlockPos> getSphere(BlockPos loc, float r, boolean hollow) {
		return getSphere(loc, r, (int) r, hollow, true, 0);
	}

}
