package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.VineBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static net.minecraft.block.VineBlock.getFacingProperty;

@Mixin(VineBlock.class)
public abstract class VineGrowMixin {
    @Shadow @Final public static BooleanProperty UP;


    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 0))
    protected void randomTickOne(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                           @Local(ordinal = 1) BlockPos blockPos,
                           @Local(ordinal = 1) Direction direction2) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,(BlockState) Blocks.VINE.getDefaultState().with(getFacingProperty(direction2), true)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 1))
    protected void randomTickTwo(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                 @Local(ordinal = 1) BlockPos blockPos,
                                 @Local(ordinal = 2) Direction direction3) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,(BlockState) Blocks.VINE.getDefaultState().with(getFacingProperty(direction3), true)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 2))
    protected void randomTickThree(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                 @Local(ordinal = 2) BlockPos blockPos,
                                 @Local(ordinal = 3) Direction direction4) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,(BlockState) Blocks.VINE.getDefaultState().with(getFacingProperty(direction4), true)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 3))
    protected void randomTickFour(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                   @Local(ordinal = 3) BlockPos blockPos,
                                   @Local(ordinal = 3) Direction direction4) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,(BlockState) Blocks.VINE.getDefaultState().with(getFacingProperty(direction4), true)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 4))
    protected void randomTickFive(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                 @Local(ordinal = 1) BlockPos blockPos) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,(BlockState) Blocks.VINE.getDefaultState().with(UP, true)));
            world.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 5))
    protected void randomTickSix(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                  @Local(ordinal = 0) Direction direction) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(pos,(BlockState) Blocks.VINE.getDefaultState().with(getFacingProperty(direction), true)));
            world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 6))
    protected void randomTickSeven(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(pos,(BlockState) Blocks.VINE.getDefaultState().with(UP, true)));
            world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 7))
    protected void randomTickEight(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                   @Local(ordinal = 0) BlockPos blockPos,
                                   @Local(ordinal = 1) BlockState blockState2) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,blockState2));
            world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
        }
    }

    @Inject(method = "randomTick", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/world/ServerWorld;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", ordinal = 8))
    protected void randomTickNine(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                                   @Local(ordinal = 1) BlockPos blockPos,
                                   @Local(ordinal = 3) BlockState blockState4) {
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(blockPos,blockState4));
            world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
        }
    }

}
