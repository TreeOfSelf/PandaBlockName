package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;


@Mixin(WorldChunk.class)
public abstract class WorldChunkMixin {

    @Shadow @Final private World world;

    @Shadow public abstract Map<BlockPos, BlockEntity> getBlockEntities();

    @Shadow public abstract BlockState getBlockState(BlockPos pos);

    @Inject(at = @At("HEAD"), method = "setBlockEntity", cancellable = true)
    public void setBlockEntity(BlockEntity blockEntity, CallbackInfo ci) {


        if (blockEntity instanceof EmptyBlockEntity) {
            BlockPos blockPos = blockEntity.getPos();

            BlockState blockState = this.getBlockState(blockPos);
            if (!blockState.hasBlockEntity()) {
                blockEntity.setCachedState(blockState);

                blockEntity.setWorld(this.world);
                blockEntity.cancelRemoval();
                BlockEntity blockEntity2 = this.getBlockEntities().put(blockPos.toImmutable(), blockEntity);
                if (blockEntity2 != null && blockEntity2 != blockEntity) {
                    blockEntity2.markRemoved();
                }
                ci.cancel();
            } else {
                BlockState blockState2 = blockEntity.getCachedState();
                if (blockState != blockState2) {
                    if (!blockEntity.getType().supports(blockState)) {
                        blockEntity.setCachedState(blockState);

                        blockEntity.setWorld(this.world);
                        blockEntity.cancelRemoval();
                        BlockEntity blockEntity2 = this.getBlockEntities().put(blockPos.toImmutable(), blockEntity);
                        if (blockEntity2 != null && blockEntity2 != blockEntity) {
                            blockEntity2.markRemoved();
                        }
                        ci.cancel();
                        return;
                    }

                    if (blockState.getBlock() != blockState2.getBlock()) {
                        blockEntity.setCachedState(blockState);

                        blockEntity.setWorld(this.world);
                        blockEntity.cancelRemoval();
                        BlockEntity blockEntity2 = this.getBlockEntities().put(blockPos.toImmutable(), blockEntity);
                        if (blockEntity2 != null && blockEntity2 != blockEntity) {
                            blockEntity2.markRemoved();
                        }
                        ci.cancel();
                    }
                }
            }
        }
    }
}
