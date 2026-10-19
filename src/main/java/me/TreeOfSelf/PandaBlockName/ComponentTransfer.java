package me.TreeOfSelf.PandaBlockName;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class ComponentTransfer {

	private ComponentTransfer() {
	}

	public static void itemFromBlockEntity(BlockEntity blockEntity, ItemStack stack) {
		if (blockEntity == null) return;
		if (blockEntity.components().has(DataComponents.CUSTOM_NAME)) {
			stack.set(DataComponents.CUSTOM_NAME, blockEntity.components().get(DataComponents.CUSTOM_NAME));
		}
		if (blockEntity.components().has(DataComponents.LORE)) {
			stack.set(DataComponents.LORE, blockEntity.components().get(DataComponents.LORE));
		}
		if (blockEntity.components().has(DataComponents.CUSTOM_DATA)) {
			CompoundTag customData = blockEntity.components().get(DataComponents.CUSTOM_DATA).copyTag();
			if (customData.contains("itemCustomData_1")) {
				CompoundTag itemCustomData = customData.getCompoundOrEmpty("itemCustomData_1");
				stack.set(DataComponents.CUSTOM_DATA, CustomData.of(itemCustomData));
			}
		}
	}
}
