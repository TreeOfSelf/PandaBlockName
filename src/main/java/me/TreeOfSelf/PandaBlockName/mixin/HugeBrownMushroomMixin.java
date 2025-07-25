package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeBrownMushroomFeature;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeBrownMushroomFeature.class)
public class HugeBrownMushroomMixin {

    @Inject(
            method = "generateCap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/HugeBrownMushroomFeature;generateStem(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos$Mutable;Lnet/minecraft/block/BlockState;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void afterGenerateStem(WorldAccess world, net.minecraft.util.math.random.Random random,
                                   BlockPos start, int y, BlockPos.Mutable mutable,
                                   HugeMushroomFeatureConfig config, CallbackInfo ci) {
        if (!PandaBlockNameConfig.isVegetationFeatureEnabled("HugeBrownMushroomGeneration")) return;
        BlockEntityPlacer.move(world, start, mutable);
    }
}