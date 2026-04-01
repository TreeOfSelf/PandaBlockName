package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooStalkBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooStalkBlock.class)
public class BambooOtherGrowMixin {

	@Inject(
			method = "growBamboo(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/util/RandomSource;I)V",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
					ordinal = 2,
					shift = At.Shift.AFTER
			)
	)
	protected void growBamboo(BlockState state, Level level, BlockPos pos, RandomSource random, int height, CallbackInfo ci) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("BambooGrowth")) return;
		BlockEntityPlacer.move(level, pos, pos.above());
	}
}
