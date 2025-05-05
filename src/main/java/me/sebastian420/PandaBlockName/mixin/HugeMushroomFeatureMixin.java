package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.BlockEntityPlacer;
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
    @Inject(method = "generateStem(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/world/gen/feature/HugeMushroomFeatureConfig;ILnet/minecraft/util/math/BlockPos$Mutable;)V",
    at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeMushroomFeature;generateStem(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos$Mutable;Lnet/minecraft/block/BlockState;)V",
    ordinal = 0, shift = At.Shift.AFTER))

    private void onGenerateStem(WorldAccess world, Random random, BlockPos pos,
                                HugeMushroomFeatureConfig config, int height,
                                BlockPos.Mutable mutablePos, CallbackInfo ci) {
        BlockEntityPlacer.move(world, pos, mutablePos);
    }

}