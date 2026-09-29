package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.EndermanEntityAccess;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.entity.monster.Enderman$EndermanTakeBlockGoal")
public class EndermanPickUpMixin {

	@Shadow
	@Final
	private Enderman enderman;

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/Level;removeBlock(Lnet/minecraft/core/BlockPos;Z)Z"
			)
	)
	public void onTick(CallbackInfo ci, @Local BlockPos pos) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Enderman")) return;
		BlockEntity blockEntity = this.enderman.level().getBlockEntity(pos);
		if (blockEntity != null) {
			EndermanEntityAccess accessor = (EndermanEntityAccess) this.enderman;
			accessor.setItemComponentMap(blockEntity.components());
		}
	}
}
