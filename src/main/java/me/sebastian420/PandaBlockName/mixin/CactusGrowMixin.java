package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.CactusBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CactusBlock.class)
public class CactusGrowMixin {
    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 0))
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                              @Local(ordinal = 1) BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,world.getBlockState(pos.up())));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }
}