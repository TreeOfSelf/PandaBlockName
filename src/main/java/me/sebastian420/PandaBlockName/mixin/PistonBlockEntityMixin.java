package me.sebastian420.PandaBlockName.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin extends BlockEntity {

    @Shadow private float lastProgress;

    @Shadow private float progress;

    @Shadow private boolean source;

    @Shadow private BlockState pushedBlock;

    public PistonBlockEntityMixin(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Inject(method = "finish", at = @At(value = "HEAD"), cancellable = true)
    private void finish( CallbackInfo ci) {

        if (this.world != null && (this.lastProgress < 1.0F || this.world.isClient)) {
            this.progress = 1.0F;
            this.lastProgress = this.progress;
            //this.world.removeBlockEntity(this.pos);
            this.markRemoved();
            if (this.world.getBlockState(this.pos).isOf(Blocks.MOVING_PISTON)) {
                BlockState blockState;
                if (this.source) {
                    blockState = Blocks.AIR.getDefaultState();
                } else {
                    blockState = Block.postProcessState(this.pushedBlock, this.world, this.pos);
                }

                this.world.setBlockState(this.pos, blockState, 3);
                this.world.updateNeighbor(this.pos, blockState.getBlock(), this.pos);
            }
        }
        ci.cancel();
    }

   @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
   private static void onTick(World world, BlockPos pos, BlockState state, PistonBlockEntity blockEntity, CallbackInfo ci) {
       PistonBlockEntityAccessor accessor = (PistonBlockEntityAccessor) blockEntity;

       accessor.setSavedWorldTime(world.getTime());
       accessor.setLastProgress(accessor.getProgress());

       if (accessor.getLastProgress() >= 1.0F) {
           if (world.isClient && accessor.getField26705() < 5) {
               accessor.setField26705(accessor.getField26705() + 1);
           } else {
               ///world.removeBlockEntity(pos);
               blockEntity.markRemoved();
               if (world.getBlockState(pos).isOf(Blocks.MOVING_PISTON)) {
                   BlockState blockState = Block.postProcessState(blockEntity.getPushedBlock(), world, pos);
                   if (blockState.isAir()) {
                       world.setBlockState(pos, blockEntity.getPushedBlock(), 84);
                       Block.replace(blockEntity.getPushedBlock(), blockState, world, pos, 3);
                   } else {
                       if (blockState.contains(Properties.WATERLOGGED) && blockState.get(Properties.WATERLOGGED)) {
                           blockState = blockState.with(Properties.WATERLOGGED, false);
                       }

                       world.setBlockState(pos, blockState, 67);
                       world.updateNeighbor(pos, blockState.getBlock(), pos);
                   }
               }
           }
       } else {
           float f = accessor.getProgress() + 0.5F;
           PistonBlockEntityAccessor.callPushEntities(world, pos, f , blockEntity);
           PistonBlockEntityAccessor.callMoveEntitiesInHoneyBlock(world, pos, f, blockEntity);
           accessor.setProgress(f);
           if (accessor.getProgress() >= 1.0F) {
               accessor.setProgress(1.0F);
           }
       }
       ci.cancel();
   }
}
