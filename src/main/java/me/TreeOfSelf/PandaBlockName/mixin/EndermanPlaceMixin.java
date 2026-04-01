package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.EmptyBlockEntity;
import me.TreeOfSelf.PandaBlockName.EndermanEntityAccess;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.world.entity.monster.EnderMan$EndermanLeaveBlockGoal")
public class EndermanPlaceMixin {
	@Shadow
	@Final
	private EnderMan enderman;

	@Inject(
			method = "tick",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/monster/EnderMan;setCarriedBlock(Lnet/minecraft/world/level/block/state/BlockState;)V"
			)
	)
	public void onTick(CallbackInfo ci, @Local(name = "pos") BlockPos pos) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Enderman")) return;
		EndermanEntityAccess accessor = (EndermanEntityAccess) this.enderman;
		DataComponentMap componentMap = accessor.getItemComponentMap();
		if (componentMap != null) {
			Level world = this.enderman.level();
			BlockState blockState = world.getBlockState(pos);
			world.setBlockEntity(new EmptyBlockEntity(pos, blockState));
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity != null) blockEntity.setComponents(componentMap);
			accessor.setItemComponentMap(null);
		}
	}
}
