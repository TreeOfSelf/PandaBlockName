package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.ComponentTransfer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.BiConsumer;

@Mixin(SweetBerryBushBlock.class)
public abstract class SweetBerryPickMixin {

	@ModifyArgs(
			method = "useWithoutItem",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/Block;dropFromBlockInteractLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/item/ItemInstance;Lnet/minecraft/world/entity/Entity;Ljava/util/function/BiConsumer;)Z"
			)
	)
	private static void panda_sweetBerryArgs(Args args) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("SweetBerryPicking")) {
			return;
		}
		BlockEntity be = args.get(4);
		@SuppressWarnings("unchecked")
		BiConsumer<ServerLevel, ItemStack> consumer = args.get(7);
		args.set(7, (BiConsumer<ServerLevel, ItemStack>) (sl, stack) -> {
			ComponentTransfer.itemFromBlockEntity(be, stack);
			consumer.accept(sl, stack);
		});
	}
}
