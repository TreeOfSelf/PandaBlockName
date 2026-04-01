package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.ChorusFlowerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerGrowMixin {

	@Inject(
			method = "randomTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/ChorusFlowerBlock;placeGrownFlower(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;I)V",
					ordinal = 0,
					shift = At.Shift.AFTER
			)
	)
	private void growOne(
			BlockState state,
			ServerLevel level,
			BlockPos pos,
			RandomSource random,
			CallbackInfo ci,
			@Local(name = "above") BlockPos above
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("ChorusFlowerGrowth")) return;
		BlockEntityPlacer.move(level, pos, above);
	}

	@Inject(
			method = "randomTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/ChorusFlowerBlock;placeGrownFlower(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;I)V",
					ordinal = 1,
					shift = At.Shift.AFTER
			)
	)
	private void growTwo(
			BlockState state,
			ServerLevel level,
			BlockPos pos,
			RandomSource random,
			CallbackInfo ci,
			@Local(name = "target") BlockPos target
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("ChorusFlowerGrowth")) return;
		BlockEntityPlacer.move(level, pos, target);
	}
}
