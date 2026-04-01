package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = BlockItem.class, priority = 5000)
public class BlockPlaceMixin {

	@Inject(
			method = "place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;",
			at = @At("HEAD")
	)
	public void prePlace(
			BlockPlaceContext context,
			CallbackInfoReturnable<InteractionResult> cir,
			@Share("prevBlockState") LocalRef<BlockState> prevBlockStateRef,
			@Share("prevEntityComponents") LocalRef<DataComponentMap> prevEntityComponentsRef
	) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Block")) return;
		prevBlockStateRef.set(context.getLevel().getBlockState(context.getClickedPos()));
		if (context.getLevel().getBlockEntity(context.getClickedPos()) != null) {
			prevEntityComponentsRef.set(context.getLevel().getBlockEntity(context.getClickedPos()).components());
		}
	}

	@Inject(
			method = "place(Lnet/minecraft/world/item/context/BlockPlaceContext;)Lnet/minecraft/world/InteractionResult;",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/Block;setPlacedBy(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;)V"
			)
	)
	public void place(
			BlockPlaceContext context,
			CallbackInfoReturnable<InteractionResult> cir,
			@Share("prevBlockState") LocalRef<BlockState> prevBlockStateRef,
			@Share("prevEntityComponents") LocalRef<DataComponentMap> prevEntityComponentsRef,
			@Local(ordinal = 1) BlockState blockState,
			@Local(ordinal = 0) ItemStack itemStack,
			@Local(ordinal = 0) BlockPos blockPos,
			@Local(ordinal = 0) Level level
	) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Block")) return;
		BlockState prevBlockState = prevBlockStateRef.get();
		DataComponentMap prevComponentMap = prevEntityComponentsRef.get();
		if (prevComponentMap == null) prevComponentMap = DataComponentMap.EMPTY;
		BlockEntityPlacer.place(level, prevBlockState, blockState, blockPos, itemStack, prevComponentMap);
	}
}
