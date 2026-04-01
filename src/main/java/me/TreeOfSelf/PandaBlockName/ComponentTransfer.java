package me.TreeOfSelf.PandaBlockName;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.block.entity.BlockEntity;

public final class ComponentTransfer {

	private ComponentTransfer() {
	}

	public static void itemFromBlockEntity(BlockEntity blockEntity, ItemStack stack) {
		if (blockEntity == null) return;
		if (blockEntity.components().has(DataComponents.CUSTOM_NAME)) {
			Component customName = blockEntity.components().get(DataComponents.CUSTOM_NAME);
			if (customName.getString().startsWith("{")) {
				try {
					JsonElement jsonElement = JsonParser.parseString(customName.getString());
					DataResult<Pair<Component, JsonElement>> result = ComponentSerialization.CODEC.decode(JsonOps.INSTANCE, jsonElement);
					customName = result.getOrThrow().getFirst();
				} catch (Exception ignored) {
				}
			}
			stack.set(DataComponents.CUSTOM_NAME, customName);
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
