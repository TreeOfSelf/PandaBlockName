package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public abstract class LeafDecayMixin {

	@Inject(method = "randomTick", at = @At("HEAD"))
	private void onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Block")) return;
		if (state.getValue(LeavesBlock.DISTANCE) == 7 && !state.getValue(LeavesBlock.PERSISTENT)) {
			BlockEntity blockEntity = level.getBlockEntity(pos);
			if (blockEntity != null) {
				level.removeBlockEntity(pos);
			}
		}
	}
}
