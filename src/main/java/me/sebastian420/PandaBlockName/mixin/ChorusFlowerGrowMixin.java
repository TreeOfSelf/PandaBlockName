package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.BlockEntityPlacer;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerGrowMixin {

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/ChorusFlowerBlock;grow(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;I)V", ordinal = 0, shift = At.Shift.AFTER))
    private void growOne(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                         @Local(ordinal = 1) BlockPos blockPos) {
        BlockEntityPlacer.move(world, pos, blockPos);
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/ChorusFlowerBlock;grow(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;I)V", ordinal = 1, shift = At.Shift.AFTER))
    private void growTwo(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                         @Local(ordinal = 2) BlockPos blockPos) {
        BlockEntityPlacer.move(world, pos, blockPos);
    }


}
