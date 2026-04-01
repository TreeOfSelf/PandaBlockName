package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.StemBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(StemBlock.class)
public class StemBlockGrowMixin {

	@Inject(
			method = "randomTick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
					ordinal = 1,
					shift = At.Shift.AFTER
			)
	)
	protected void randomTick(
			BlockState state,
			ServerLevel level,
			BlockPos pos,
			RandomSource random,
			CallbackInfo ci,
			@Local(name = "relative") BlockPos relative
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("StemGrowth")) return;
		BlockEntityPlacer.move(level, pos, relative);
	}
}
