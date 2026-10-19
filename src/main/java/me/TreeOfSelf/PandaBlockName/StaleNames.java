package me.TreeOfSelf.PandaBlockName;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.ArrayList;
import java.util.List;

// A named block that changes into another block leaves its EmptyBlockEntity behind (vanilla only removes block entities
// of blocks that have one). It is removed at the end of the tick, so growth/move code reading it in the same call still can.
public final class StaleNames {
	private record Pending(Level level, BlockPos pos, BlockEntity blockEntity) {}

	private static final List<Pending> pending = new ArrayList<>();

	public static void add(Level level, BlockPos pos, BlockEntity blockEntity) {
		pending.add(new Pending(level, pos.immutable(), blockEntity));
	}

	public static void flush() {
		if (pending.isEmpty()) return;
		List<Pending> list = new ArrayList<>(pending);
		pending.clear();
		for (Pending p : list) {
			if (p.level.getBlockEntity(p.pos) != p.blockEntity) continue;
			if (p.level.getBlockState(p.pos).is(p.blockEntity.getBlockState().getBlock())) continue;
			p.level.removeBlockEntity(p.pos);
			p.level.getChunkAt(p.pos).markUnsaved();
		}
	}
}
