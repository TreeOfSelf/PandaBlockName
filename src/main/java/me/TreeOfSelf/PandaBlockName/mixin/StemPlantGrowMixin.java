package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.block.AbstractPlantStemBlock;
import net.minecraft.block.BlockState;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractPlantStemBlock.class)
public abstract class StemPlantGrowMixin {


    @Inject(method = "grow", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", shift = At.Shift.AFTER))
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci,
                     @Local(ordinal = 1) BlockPos blockPos) {
        if (!PandaBlockNameConfig.isVegetationFeatureEnabled("StemPlantGrowth")) return;
        BlockEntityPlacer.move(world, pos, blockPos);
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", shift = At.Shift.AFTER))
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                              @Local(ordinal = 1) BlockPos blockPos) {
        if (!PandaBlockNameConfig.isVegetationFeatureEnabled("StemPlantGrowth")) return;
        BlockEntityPlacer.move(world, pos, blockPos);
    }
}
