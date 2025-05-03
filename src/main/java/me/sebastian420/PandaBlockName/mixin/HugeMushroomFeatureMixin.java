package me.sebastian420.PandaBlockName.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeMushroomFeature.class)
public class HugeMushroomFeatureMixin {
    @Unique
    private static ThreadLocal<BlockPos> ORIGINAL_POS = new ThreadLocal<>();

    @Inject(
            method = "generateStem(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/HugeMushroomFeatureConfig;ILnet/minecraft/util/math/BlockPos$Mutable;)V",
            at = @At("HEAD")
    )
    private void storeOriginalPos(WorldAccess world, Random random, BlockPos pos,
                                  HugeMushroomFeatureConfig config, int height,
                                  BlockPos.Mutable mutablePos, CallbackInfo ci) {
        ORIGINAL_POS.set(pos.toImmutable());
    }

    @Inject(
            method = "generateStem(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos$Mutable;Lnet/minecraft/block/BlockState;)V",
            at = @At("TAIL")
    )
    private void onSetBlockState(WorldAccess world, BlockPos.Mutable pos, BlockState state, CallbackInfo ci) {
        BlockPos originalPos = ORIGINAL_POS.get();

    }
}