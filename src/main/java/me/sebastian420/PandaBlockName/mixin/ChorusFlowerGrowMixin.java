package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChorusFlowerBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChorusFlowerBlock.class)
public class ChorusFlowerGrowMixin {
    @Inject(method = "randomTick", at = @At(value = "HEAD"))
    private void preGrow(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci,
                         @Share("origin") LocalRef<BlockPos> origin ) {
            origin.set(pos);
    }

    @Inject(method = "grow", at = @At(value = "INVOKE", target="Lnet/minecraft/world/World;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", shift = At.Shift.AFTER))
    private void postGrow(World world, BlockPos pos, int age, CallbackInfo ci,
                         @Share("origin") LocalRef<BlockPos> origin) {
        BlockPos originalPos = origin.get();
        if (originalPos != null) {
            BlockEntity blockEntity = world.getBlockEntity(originalPos);
            if (blockEntity instanceof EmptyBlockEntity) {
                world.addBlockEntity(new EmptyBlockEntity(pos, world.getBlockState(pos)));
                world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
            }
        }
    }
}
