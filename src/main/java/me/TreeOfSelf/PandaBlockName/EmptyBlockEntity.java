package me.TreeOfSelf.PandaBlockName;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import static me.TreeOfSelf.PandaBlockName.PandaBlockName.EMPTY_BLOCK_ENTITY_TYPE;

public class EmptyBlockEntity extends BlockEntity {

	public EmptyBlockEntity(BlockPos pos, BlockState state) {
		super(EMPTY_BLOCK_ENTITY_TYPE, pos, state);
	}
}
