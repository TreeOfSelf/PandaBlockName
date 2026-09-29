package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.HugeFungusFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeFungusFeature.class)
public class HugeFungusGrowMixin {

	@Inject(
			method = "placeStem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/WorldGenLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
					shift = At.Shift.AFTER
			)
	)
	private void panda_afterStemSetBlock(
			WorldGenLevel level,
			RandomSource random,
			BlockPos surfaceOrigin,
			int totalHeight,
			boolean isHuge,
			CallbackInfo ci,
			@Local BlockPos.MutableBlockPos blockPos
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("HugeFungusGeneration")) return;
		BlockEntityPlacer.move(level, surfaceOrigin, blockPos);
	}

	@Inject(
			method = "placeStem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/feature/HugeFungusFeature;setBlock(Lnet/minecraft/world/level/LevelWriter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
					shift = At.Shift.AFTER
			)
	)
	private void panda_afterStemFeatureSetBlock(
			WorldGenLevel level,
			RandomSource random,
			BlockPos surfaceOrigin,
			int totalHeight,
			boolean isHuge,
			CallbackInfo ci,
			@Local BlockPos.MutableBlockPos blockPos
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("HugeFungusGeneration")) return;
		BlockEntityPlacer.move(level, surfaceOrigin, blockPos);
	}

	@Inject(
			method = "placeHat",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/feature/HugeFungusFeature;placeHatBlock(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos$MutableBlockPos;FFF)V",
					shift = At.Shift.AFTER
			)
	)
	private void panda_afterPlaceHatBlock(
			WorldGenLevel level,
			RandomSource random,
			BlockPos surfaceOrigin,
			int totalHeight,
			boolean isHuge,
			CallbackInfo ci,
			@Local BlockPos.MutableBlockPos blockPos
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("HugeFungusGeneration")) return;
		BlockEntityPlacer.move(level, surfaceOrigin, blockPos);
	}

	@Inject(
			method = "placeHat",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/feature/HugeFungusFeature;placeHatDropBlock(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/util/RandomSource;Lnet/minecraft/core/BlockPos;Z)V",
					shift = At.Shift.AFTER
			)
	)
	private void panda_afterPlaceHatDropBlock(
			WorldGenLevel level,
			RandomSource random,
			BlockPos surfaceOrigin,
			int totalHeight,
			boolean isHuge,
			CallbackInfo ci,
			@Local BlockPos.MutableBlockPos blockPos
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("HugeFungusGeneration")) return;
		BlockEntityPlacer.move(level, surfaceOrigin, blockPos);
	}
}
