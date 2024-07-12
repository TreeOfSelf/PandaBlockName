package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.sebastian420.PandaBlockName.BlockEntityPlacer;
import net.minecraft.block.BlockState;
import net.minecraft.block.MushroomPlantBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MushroomPlantBlock.class)
public class MushroomGrowMixin {

    @Inject(method = "randomTick", at = @At(value = "HEAD"))
    protected void preGrow(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                           @Share("originalPos") LocalRef<BlockPos> originalPos) {
        originalPos.set(pos);
    }


    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", shift = At.Shift.AFTER))
    protected void postGrow(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                        @Local(ordinal = 1) BlockPos blockPos,
                        @Share("originalPos") LocalRef<BlockPos> originalPos) {
        BlockEntityPlacer.move(world, originalPos.get(), blockPos);
    }
}
