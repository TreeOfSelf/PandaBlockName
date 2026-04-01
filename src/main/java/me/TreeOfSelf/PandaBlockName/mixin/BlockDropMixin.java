package me.TreeOfSelf.PandaBlockName.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import me.TreeOfSelf.PandaBlockName.ItemData;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = BlockBehaviour.class, priority = 5000)
public class BlockDropMixin {

	@Unique
	private ItemData panda_getItemData(net.minecraft.world.level.Level world, CompoundTag customData, int index) {
		ItemData itemData = new ItemData();
		String nameKey = "itemName_" + index;
		String loreKey = "itemLore_" + index;
		String customDataKey = "itemCustomData_" + index;
		if (customData.contains(nameKey)) {
			JsonElement jsonElement = JsonParser.parseString(customData.getString(nameKey).orElse(""));
			DataResult<Pair<Component, JsonElement>> result = ComponentSerialization.CODEC.decode(JsonOps.INSTANCE, jsonElement);
			itemData.CustomName = result.getOrThrow().getFirst();
		}
		if (customData.contains(loreKey)) {
			String[] loreString = customData.getString(loreKey).orElse("").split("\\{\\\\\"\\\\}");
			List<Component> textList = new ArrayList<>();
			for (String s : loreString) {
				JsonElement jsonElement = JsonParser.parseString(s);
				DataResult<Pair<Component, JsonElement>> result = ComponentSerialization.CODEC.decode(JsonOps.INSTANCE, jsonElement);
				textList.add(result.getOrThrow().getFirst());
			}
			itemData.Lore = new ItemLore(textList);
		}
		if (customData.contains(customDataKey)) {
			itemData.CustomData = customData.getCompound(customDataKey).orElse(new CompoundTag());
		}
		return itemData;
	}

	@Unique
	private void panda_addOrCombine(List<ItemStack> list, ItemStack itemStack) {
		boolean add = true;
		for (ItemStack item : list) {
			if (item.getComponents().equals(itemStack.getComponents())) {
				item.grow(1);
				add = false;
				break;
			}
		}
		if (add) list.add(itemStack);
	}

	@Inject(method = "getDrops", at = @At("TAIL"))
	private void panda_getDrops(BlockState state, LootParams.Builder params, CallbackInfoReturnable<List<ItemStack>> cir) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Block")) return;
		net.minecraft.server.level.ServerLevel world = params.getLevel();
		BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
		if (blockEntity == null) {
			Vec3 origin = params.getOptionalParameter(LootContextParams.ORIGIN);
			if (origin != null) {
				blockEntity = world.getBlockEntity(BlockPos.containing(origin));
			}
		}
		if (blockEntity == null) return;
		boolean multiple = state.hasProperty(BlockStateProperties.PICKLES)
				|| state.hasProperty(BlockStateProperties.CANDLES)
				|| state.hasProperty(BlockStateProperties.LAYERS)
				|| state.hasProperty(BlockStateProperties.SLAB_TYPE);
		if (multiple) {
			CompoundTag customData = null;
			if (blockEntity.components().has(DataComponents.CUSTOM_DATA)) {
				customData = blockEntity.components().get(DataComponents.CUSTOM_DATA).copyTag();
			}
			List<ItemStack> items = cir.getReturnValue();
			List<ItemStack> additionalItems = new ArrayList<>();
			int maxPropertyValue = 1;
			if (state.hasProperty(BlockStateProperties.PICKLES)) {
				maxPropertyValue = state.getValue(BlockStateProperties.PICKLES);
			} else if (state.hasProperty(BlockStateProperties.CANDLES)) {
				maxPropertyValue = state.getValue(BlockStateProperties.CANDLES);
			} else if (state.hasProperty(BlockStateProperties.LAYERS)) {
				maxPropertyValue = state.getValue(BlockStateProperties.LAYERS);
			} else if (state.hasProperty(BlockStateProperties.SLAB_TYPE)) {
				maxPropertyValue = 2;
			}
			int currentItemIndex = 1;
			outerLoop:
			for (ItemStack item : new ArrayList<>(items)) {
				while (item.getCount() > 0 && currentItemIndex <= maxPropertyValue) {
					if (currentItemIndex == 1) {
						ItemStack newItem = null;
						boolean newItemChanged = false;
						if (blockEntity.components().has(DataComponents.CUSTOM_NAME)) {
							newItem = item.copyWithCount(1);
							Component customName = blockEntity.components().get(DataComponents.CUSTOM_NAME);
							if (customName.getString().startsWith("{")) {
								try {
									JsonElement jsonElement = JsonParser.parseString(blockEntity.components().get(DataComponents.CUSTOM_NAME).getString());
									DataResult<Pair<Component, JsonElement>> result = ComponentSerialization.CODEC.decode(JsonOps.INSTANCE, jsonElement);
									customName = result.getOrThrow().getFirst();
								} catch (Exception ignored) {
								}
							}
							newItem.set(DataComponents.CUSTOM_NAME, customName);
							newItemChanged = true;
						}
						if (blockEntity.components().has(DataComponents.LORE)) {
							if (newItem == null) newItem = item.copyWithCount(1);
							newItem.set(DataComponents.LORE, blockEntity.components().get(DataComponents.LORE));
							newItemChanged = true;
						}
						if (customData != null && customData.contains("itemCustomData_1")) {
							if (newItem == null) newItem = item.copyWithCount(1);
							CompoundTag firstItemCustomData = customData.getCompoundOrEmpty("itemCustomData_1");
							newItem.set(DataComponents.CUSTOM_DATA, CustomData.of(firstItemCustomData));
							newItemChanged = true;
						}
						if (newItemChanged) {
							item.shrink(1);
							panda_addOrCombine(additionalItems, newItem);
							if (item.getCount() == 0) items.remove(item);
						}
					} else {
						if (customData == null) break outerLoop;
						ItemStack newItem = null;
						boolean newItemChanged = false;
						ItemData itemData = panda_getItemData(world, customData, currentItemIndex);
						if (itemData.CustomName != null) {
							newItem = item.copyWithCount(1);
							newItem.set(DataComponents.CUSTOM_NAME, itemData.CustomName);
							newItemChanged = true;
						}
						if (itemData.Lore != null) {
							if (newItem == null) newItem = item.copyWithCount(1);
							newItem.set(DataComponents.LORE, itemData.Lore);
							newItemChanged = true;
						}
						if (itemData.CustomData != null) {
							if (newItem == null) newItem = item.copyWithCount(1);
							newItem.set(DataComponents.CUSTOM_DATA, CustomData.of(itemData.CustomData));
							newItemChanged = true;
						}
						if (newItemChanged) {
							item.shrink(1);
							panda_addOrCombine(additionalItems, newItem);
							if (item.getCount() == 0) items.remove(item);
						} else {
							break outerLoop;
						}
					}
					currentItemIndex++;
				}
			}
			items.addAll(additionalItems);
		} else {
			List<ItemStack> items = cir.getReturnValue();
			if (blockEntity.components().has(DataComponents.CUSTOM_NAME)) {
				for (ItemStack item : items) {
					Component customName = blockEntity.components().get(DataComponents.CUSTOM_NAME);
					if (customName.getString().startsWith("{")) {
						try {
							JsonElement jsonElement = JsonParser.parseString(blockEntity.components().get(DataComponents.CUSTOM_NAME).getString());
							DataResult<Pair<Component, JsonElement>> result = ComponentSerialization.CODEC.decode(JsonOps.INSTANCE, jsonElement);
							customName = result.getOrThrow().getFirst();
						} catch (Exception ignored) {
						}
					}
					item.set(DataComponents.CUSTOM_NAME, customName);
				}
			}
			if (blockEntity.components().has(DataComponents.LORE)) {
				for (ItemStack item : items) {
					item.set(DataComponents.LORE, blockEntity.components().get(DataComponents.LORE));
				}
			}
			CompoundTag customData = null;
			if (blockEntity.components().has(DataComponents.CUSTOM_DATA)) {
				customData = blockEntity.components().get(DataComponents.CUSTOM_DATA).copyTag();
			}
			if (customData != null && customData.contains("itemCustomData_1")) {
				for (ItemStack item : items) {
					CompoundTag itemCustomData = customData.getCompoundOrEmpty("itemCustomData_1");
					item.set(DataComponents.CUSTOM_DATA, CustomData.of(itemCustomData));
				}
			}
		}
	}
}
