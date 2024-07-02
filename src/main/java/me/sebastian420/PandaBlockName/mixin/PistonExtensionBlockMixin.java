package me.sebastian420.PandaBlockName.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonExtensionBlock.class)
public abstract class PistonExtensionBlockMixin {

    @Inject(method = "createBlockEntityPiston", at = @At("HEAD"), cancellable = true)
    private static void onCreateBlockEntityPiston(BlockPos pos, BlockState state, BlockState pushedBlock, Direction facing, boolean extending, boolean source, CallbackInfoReturnable<BlockEntity> cir) {
        // Your custom logic here

    }

    @Inject(method = "onStateReplaced", at = @At("HEAD"))
    private void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved, CallbackInfo ci) {
        // Your custom logic here
        System.out.println("onStateReplaced called");
        // You can add additional behavior here without cancelling the method call
    }
}