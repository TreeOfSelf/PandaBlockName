package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.BonemealSource;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SeaPickleBlock.class)
public class SeaPickleMixin {

	@Inject(
			method = "performBonemeal",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/server/level/ServerLevel;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
					ordinal = 0,
					shift = At.Shift.AFTER
			)
	)
	public void performBonemeal(
			ServerLevel level,
			RandomSource random,
			BlockPos pos,
			BlockState state,
			BonemealSource source,
			CallbackInfo ci,
			@Local(name = "position") BlockPos position
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("SeaPickleGrowth")) return;
		BlockEntityPlacer.move(level, pos, position);
	}
}
