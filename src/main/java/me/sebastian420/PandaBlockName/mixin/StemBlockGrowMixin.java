package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.StemBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(StemBlock.class)
public class StemBlockGrowMixin {
    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Z", ordinal = 1))
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                              @Local(ordinal = 1) BlockPos blockPos,
                              @Local(ordinal = 0) Optional<Block> optional) {
        System.out.println("GREW GOURDD");
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,((Block)optional.get()).getDefaultState()));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

}
