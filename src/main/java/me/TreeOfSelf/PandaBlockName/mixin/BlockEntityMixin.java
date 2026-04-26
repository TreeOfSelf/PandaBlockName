package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.EmptyBlockEntity;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {

	@Inject(method = "validateBlockState", at = @At("HEAD"), cancellable = true)
	private void panda_validateBlockState(BlockState blockState, CallbackInfo ci) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Block")) return;
		if ((Object) this instanceof EmptyBlockEntity && !blockState.hasBlockEntity()) ci.cancel();
	}
}
