package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.ComponentTransfer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.PumpkinBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.function.BiConsumer;

@Mixin(PumpkinBlock.class)
public abstract class PumpkinShearMixin {

	@ModifyArgs(
			method = "useItemOn",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/block/PumpkinBlock;dropFromBlockInteractLootTable(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/entity/BlockEntity;Lnet/minecraft/world/item/ItemInstance;Lnet/minecraft/world/entity/Entity;Ljava/util/function/BiConsumer;)Z"
			)
	)
	private static void panda_carveArgs(Args args) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("PumpkinShearing")) {
			return;
		}
		BlockEntity be = args.get(3);
		@SuppressWarnings("unchecked")
		BiConsumer<ServerLevel, ItemStack> consumer = args.get(6);
		args.set(6, (BiConsumer<ServerLevel, ItemStack>) (sl, stack) -> {
			ComponentTransfer.itemFromBlockEntity(be, stack);
			consumer.accept(sl, stack);
		});
	}
}
