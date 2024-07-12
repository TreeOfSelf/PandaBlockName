package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.VineBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(VineBlock.class)
public abstract class VineGrowMixin {

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 0, shift = At.Shift.AFTER))
    protected void randomTickOne(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                 @Local(ordinal = 2) BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,world.getBlockState(blockPos)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1, shift = At.Shift.AFTER))
    protected void randomTickTwo(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                 @Local(ordinal = 2) BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,world.getBlockState(blockPos)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 2, shift = At.Shift.AFTER))
    protected void randomTickThree(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                   @Local(ordinal = 3) BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,world.getBlockState(blockPos)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 3, shift = At.Shift.AFTER))
    protected void randomTickFour(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                  @Local(ordinal = 4) BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,world.getBlockState(blockPos)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 4, shift = At.Shift.AFTER))
    protected void randomTickFive(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                  @Local(ordinal = 2) BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,world.getBlockState(blockPos)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 7, shift = At.Shift.AFTER))
    protected void randomTickEight(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                   @Local(ordinal = 1) BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,world.getBlockState(blockPos)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 8, shift = At.Shift.AFTER))
    protected void randomTickNine(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                  @Local(ordinal = 2) BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,world.getBlockState(blockPos)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

}