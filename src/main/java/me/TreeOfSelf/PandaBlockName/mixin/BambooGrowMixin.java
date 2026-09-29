package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BambooSaplingBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooSaplingBlock.class)
public class BambooGrowMixin {

	@Inject(
			method = "growBamboo",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;setBlockAndUpdate(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Z",
					shift = At.Shift.AFTER
			)
	)
	protected void growBamboo(Level level, BlockPos pos, CallbackInfo ci) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("BambooGrowth")) return;
		BlockEntityPlacer.move(level, pos, pos.above());
	}
}
