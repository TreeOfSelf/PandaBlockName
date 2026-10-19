package me.TreeOfSelf.PandaBlockName;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.List;

// A broken named block (now air or a fluid) leaves its EmptyBlockEntity behind: vanilla only removes block entities of
// blocks that have one. It is removed at the end of the tick, so growth/move code reading it in the same call still can.
// Blocks that turn into other blocks (copper oxidizing or waxed, grass to dirt) keep their name.
public final class StaleNames {
	private record Pending(Level level, BlockPos pos, BlockEntity blockEntity) {}

	private static final List<Pending> pending = new ArrayList<>();

	public static boolean isGone(BlockState state) {
		return state.isAir() || state.getBlock() instanceof LiquidBlock;
	}

	public static void add(Level level, BlockPos pos, BlockEntity blockEntity) {
		pending.add(new Pending(level, pos.immutable(), blockEntity));
	}

	public static void flush() {
		if (pending.isEmpty()) return;
		List<Pending> list = new ArrayList<>(pending);
		pending.clear();
		for (Pending p : list) {
			if (!p.level.isLoaded(p.pos) || p.level.getBlockEntity(p.pos) != p.blockEntity) continue;
			if (!isGone(p.level.getBlockState(p.pos))) continue;
			p.level.removeBlockEntity(p.pos);
			p.level.getChunkAt(p.pos).markUnsaved();
		}
	}
}
