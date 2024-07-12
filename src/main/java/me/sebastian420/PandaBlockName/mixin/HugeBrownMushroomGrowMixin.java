package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.BlockEntityPlacer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeBrownMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeBrownMushroomFeature.class)
public class HugeBrownMushroomGrowMixin {
    @Inject(method = "generateCap", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeBrownMushroomFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", shift = At.Shift.AFTER))
    protected void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config, CallbackInfo ci) {
        BlockEntityPlacer.move((World) world, start, mutable);
    }
}