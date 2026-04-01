package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.MushroomBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomBlock.class)
public class MushroomGrowMixin {

	@Inject(method = "randomTick", at = @At("HEAD"))
	protected void preGrow(
			BlockState state,
			ServerLevel level,
			BlockPos pos,
			RandomSource random,
			CallbackInfo ci,
			@Share("originalPos") LocalRef<BlockPos> originalPos
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("MushroomGrowth")) return;
		originalPos.set(pos);
	}

	@Inject(
			method = "randomTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
					shift = At.Shift.AFTER
			)
	)
	protected void postGrow(
			BlockState state,
			ServerLevel level,
			BlockPos pos,
			RandomSource random,
			CallbackInfo ci,
			@Local(ordinal = 1) BlockPos blockPos,
			@Share("originalPos") LocalRef<BlockPos> originalPos
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("MushroomGrowth")) return;
		BlockPos origin = originalPos.get();
		if (origin != null) BlockEntityPlacer.move(level, origin, blockPos);
	}
}
