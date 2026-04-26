package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.EmptyBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(LevelChunk.class)
public abstract class WorldChunkMixin {

	@Shadow
	@Final
	private Level level;

	@Shadow
	public abstract Map<BlockPos, BlockEntity> getBlockEntities();

	@Shadow
	public abstract BlockState getBlockState(BlockPos pos);

	@Inject(method = "setBlockState", at = @At("HEAD"))
	public void preSetBlockState(BlockPos pos, BlockState state, int flags, CallbackInfoReturnable<BlockState> cir) {
		if (state.hasBlockEntity()) {
			BlockEntity existing = this.getBlockEntities().get(pos);
			if (existing instanceof EmptyBlockEntity) {
				existing.setRemoved();
				this.getBlockEntities().remove(pos);
			}
		}
	}

	@Inject(method = "setBlockEntity", at = @At("HEAD"), cancellable = true)
	public void setBlockEntity(BlockEntity blockEntity, CallbackInfo ci) {
		if (blockEntity instanceof EmptyBlockEntity) {
			BlockPos blockPos = blockEntity.getBlockPos();
			BlockState blockState = this.getBlockState(blockPos);
			if (!blockState.hasBlockEntity()) {
				blockEntity.setBlockState(blockState);
				blockEntity.setLevel(this.level);
				blockEntity.clearRemoved();
				BlockEntity blockEntity2 = this.getBlockEntities().put(blockPos.immutable(), blockEntity);
				if (blockEntity2 != null && blockEntity2 != blockEntity) {
					blockEntity2.setRemoved();
				}
				ci.cancel();
			} else {
				// Block has its own native BE — never store EmptyBlockEntity here
				ci.cancel();
			}
		}
	}
}
