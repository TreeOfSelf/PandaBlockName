package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CactusBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CactusBlock.class)
public class CactusGrowMixin {

	@Inject(
			method = "randomTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
					ordinal = 0,
					shift = At.Shift.AFTER
			)
	)
	protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("CactusGrowth")) return;
		BlockEntityPlacer.move(level, pos, pos.above());
	}
}
