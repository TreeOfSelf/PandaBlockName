package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.BlockEntityPlacer;
import net.minecraft.block.BambooBlock;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BambooBlock.class)
public class BambooOtherGrowMixin {
    @Inject(method = "updateLeaves", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 2 , shift = At.Shift.AFTER))
    protected void grow(BlockState state, World world, BlockPos pos, Random random, int height, CallbackInfo ci) {
        BlockEntityPlacer.move(world, pos, pos.up());
    }
}
