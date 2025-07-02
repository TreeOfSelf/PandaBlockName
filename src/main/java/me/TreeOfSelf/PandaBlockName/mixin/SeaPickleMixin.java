package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.SeaPickleBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SeaPickleBlock.class)
public class SeaPickleMixin {
    @Inject(method = "grow", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 0, shift = At.Shift.AFTER))
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state, CallbackInfo ci,
                     @Local(ordinal = 1) BlockPos blockPos) {
        if (!PandaBlockNameConfig.isVegetationFeatureEnabled("SeaPickleGrowth")) return;
        BlockEntityPlacer.move(world, pos, blockPos);
    }
}
