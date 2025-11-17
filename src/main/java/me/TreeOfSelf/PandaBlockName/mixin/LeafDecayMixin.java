package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.LeavesBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LeavesBlock.class)
public abstract class LeafDecayMixin {

    @Inject(method = "randomTick", at = @At(value = "HEAD"))
    private void onRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (!PandaBlockNameConfig.isFeatureEnabled("Block")) {
            return;
        }

        // Check if leaves will decay (distance is at max and not persistent)
        if (state.get(LeavesBlock.DISTANCE) == 7 && !state.get(LeavesBlock.PERSISTENT)) {
            // Remove the block entity before the leaves decay
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                world.removeBlockEntity(pos);
            }
        }
    }
}
