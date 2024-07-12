package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.BlockEntityPlacer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeMushroomFeature.class)
public class HugeMushroomGrowMixin {
    @Inject(method = "generateStem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeMushroomFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", shift = At.Shift.AFTER))
    protected void generateStem(WorldAccess world, Random random, BlockPos pos, HugeMushroomFeatureConfig config, int height, BlockPos.Mutable mutablePos, CallbackInfo ci) {
        BlockEntityPlacer.move((World) world, pos, mutablePos);
    }
}