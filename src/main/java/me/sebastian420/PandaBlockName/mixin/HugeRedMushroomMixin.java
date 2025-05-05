package me.sebastian420.PandaBlockName.mixin;

import com.jcraft.jorbis.Block;
import me.sebastian420.PandaBlockName.BlockEntityPlacer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import net.minecraft.world.gen.feature.HugeRedMushroomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeRedMushroomFeature.class)
public class HugeRedMushroomMixin {

    @Inject(
            method = "generateCap",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/gen/feature/HugeRedMushroomFeature;generateStem(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos$Mutable;Lnet/minecraft/block/BlockState;)V",
                    shift = At.Shift.AFTER
            )
    )
    private void afterGenerateStem(WorldAccess world, net.minecraft.util.math.random.Random random,
                                   BlockPos start, int y, BlockPos.Mutable mutable,
                                   HugeMushroomFeatureConfig config, CallbackInfo ci) {
        BlockEntityPlacer.move(world, start, mutable);
    }
}