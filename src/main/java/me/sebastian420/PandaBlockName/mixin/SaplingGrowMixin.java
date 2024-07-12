package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.SaplingGenerator;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(SaplingGenerator.class)
public class SaplingGrowMixin {
    @Inject(method = "generate", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 4))
    public void generateOne(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random, CallbackInfoReturnable<Boolean> cir,
                            @Local(ordinal = 0) int i,
                            @Local(ordinal = 1) int j) {
        BlockPos blockPos = pos.add(i, 0, j);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,state));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "generate", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 5))
    public void generateTwo(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random, CallbackInfoReturnable<Boolean> cir,
                            @Local(ordinal = 0) int i,
                            @Local(ordinal = 1) int j) {
        BlockPos blockPos = pos.add(i + 1, 0, j);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,state));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "generate", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 6))
    public void generateThree(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random, CallbackInfoReturnable<Boolean> cir,
                            @Local(ordinal = 0) int i,
                            @Local(ordinal = 1) int j) {
        BlockPos blockPos = pos.add(i , 0, j + 1);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,state));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "generate", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 7))
    public void generateFour(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random, CallbackInfoReturnable<Boolean> cir,
                              @Local(ordinal = 0) int i,
                              @Local(ordinal = 1) int j) {

        BlockPos blockPos = pos.add(i + 1 , 0, j + 1);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,state));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "generate", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 9))
    public void generateFive(ServerWorld world, ChunkGenerator chunkGenerator, BlockPos pos, BlockState state, Random random, CallbackInfoReturnable<Boolean> cir) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(pos,state));
            world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
        }
    }
}

