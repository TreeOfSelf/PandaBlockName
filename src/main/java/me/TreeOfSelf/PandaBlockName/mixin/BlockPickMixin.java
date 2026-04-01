package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.ComponentTransfer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.class)
public class BlockPickMixin {

	@Inject(method = "getCloneItemStack", at = @At("RETURN"), cancellable = true)
	private void onGetCloneItemStack(LevelReader level, BlockPos pos, BlockState state, boolean includeData, CallbackInfoReturnable<ItemStack> cir) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Block")) return;
		BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity != null) {
			ItemStack stack = cir.getReturnValue();
			ComponentTransfer.itemFromBlockEntity(blockEntity, stack);
			cir.setReturnValue(stack);
		}
	}
}
