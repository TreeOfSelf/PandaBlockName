package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.HugeBrownMushroomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeBrownMushroomFeature.class)
public class HugeBrownMushroomCapMixin {

	@Inject(
			method = "makeCap",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/feature/HugeBrownMushroomFeature;placeMushroomBlock(Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos$MutableBlockPos;Lnet/minecraft/world/level/block/state/BlockState;)V",
					shift = At.Shift.AFTER
			)
	)
	private void panda_afterCapBlock(
			WorldGenLevel level,
			RandomSource random,
			BlockPos origin,
			int treeHeight,
			BlockPos.MutableBlockPos blockPos,
			CallbackInfo ci
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("HugeBrownMushroomGeneration")) return;
		BlockEntityPlacer.move(level, origin, blockPos.immutable());
	}
}
