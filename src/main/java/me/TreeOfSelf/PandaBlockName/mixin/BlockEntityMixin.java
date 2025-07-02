package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockEntity.class)
public class BlockEntityMixin {
    @Inject(
            method = "validateSupports",
            at = @At("HEAD"),
            cancellable = true
    )
    private void injectValidateSupports(BlockState state, CallbackInfo ci) {
        if (!PandaBlockNameConfig.isFeatureEnabled("Block")) return;
        ci.cancel();
    }
}