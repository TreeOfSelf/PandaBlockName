package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.VineBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(VineBlock.class)
public abstract class VineGrowMixin {

	private static final String SET_BLOCK = "Lnet/minecraft/server/level/ServerLevel;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z";

	@Inject(method = "randomTick", at = @At(value = "INVOKE", target = SET_BLOCK, ordinal = 0, shift = At.Shift.AFTER))
	protected void randomTickOne(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci, @Local(ordinal = 2) BlockPos blockPos) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("VineGrowth")) return;
		BlockEntityPlacer.move(level, pos, blockPos);
	}

	@Inject(method = "randomTick", at = @At(value = "INVOKE", target = SET_BLOCK, ordinal = 1, shift = At.Shift.AFTER))
	protected void randomTickTwo(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci, @Local(ordinal = 2) BlockPos blockPos) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("VineGrowth")) return;
		BlockEntityPlacer.move(level, pos, blockPos);
	}

	@Inject(method = "randomTick", at = @At(value = "INVOKE", target = SET_BLOCK, ordinal = 2, shift = At.Shift.AFTER))
	protected void randomTickThree(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci, @Local(ordinal = 3) BlockPos blockPos) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("VineGrowth")) return;
		BlockEntityPlacer.move(level, pos, blockPos);
	}

	@Inject(method = "randomTick", at = @At(value = "INVOKE", target = SET_BLOCK, ordinal = 3, shift = At.Shift.AFTER))
	protected void randomTickFour(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci, @Local(ordinal = 4) BlockPos blockPos) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("VineGrowth")) return;
		BlockEntityPlacer.move(level, pos, blockPos);
	}

	@Inject(method = "randomTick", at = @At(value = "INVOKE", target = SET_BLOCK, ordinal = 4, shift = At.Shift.AFTER))
	protected void randomTickFive(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci, @Local(ordinal = 2) BlockPos blockPos) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("VineGrowth")) return;
		BlockEntityPlacer.move(level, pos, blockPos);
	}

	@Inject(method = "randomTick", at = @At(value = "INVOKE", target = SET_BLOCK, ordinal = 7, shift = At.Shift.AFTER))
	protected void randomTickEight(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci, @Local(ordinal = 1) BlockPos blockPos) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("VineGrowth")) return;
		BlockEntityPlacer.move(level, pos, blockPos);
	}

	@Inject(method = "randomTick", at = @At(value = "INVOKE", target = SET_BLOCK, ordinal = 8, shift = At.Shift.AFTER))
	protected void randomTickNine(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci, @Local(ordinal = 2) BlockPos blockPos) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("VineGrowth")) return;
		BlockEntityPlacer.move(level, pos, blockPos);
	}
}
