package me.TreeOfSelf.PandaBlockName;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BlockEntityPlacer {

	@Unique
	private static String encodeListTextToString(List<Component> texts, RegistryAccess registryAccess) {
		StringBuilder combined = new StringBuilder();
		for (int i = 0; i < texts.size(); i++) {
			DataResult<JsonElement> json = ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, texts.get(i));
			String string = json.getOrThrow().toString();
			combined.append(string);
			if (i < texts.size() - 1) {
				combined.append("{\\\"\\}");
			}
		}
		return combined.toString();
	}

	private static String getStringRef(String checkString, CompoundTag customData) {
		int checkNumber = 2;
		while (customData.contains(checkString + checkNumber)) checkNumber++;
		return checkString + checkNumber;
	}

	@Unique
	private static DataComponentMap setAdditionalData(Level level, DataComponentMap prevComponenetMap, BlockState blockState, ItemStack itemStack, IntegerProperty property) {
		CompoundTag customData = new CompoundTag();
		if (prevComponenetMap.has(DataComponents.CUSTOM_DATA)) {
			customData = prevComponenetMap.get(DataComponents.CUSTOM_DATA).copyTag();
		}

		int currentPropertyValue = blockState.getValue(property);
		String itemIndex = String.valueOf(currentPropertyValue);

		if (itemStack.has(DataComponents.CUSTOM_NAME)) {
			DataResult<JsonElement> json = ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, itemStack.get(DataComponents.CUSTOM_NAME));
			String string = json.getOrThrow().toString();
			customData.putString("itemName_" + itemIndex, string);
		}
		if (itemStack.has(DataComponents.LORE) && !itemStack.get(DataComponents.LORE).lines().isEmpty()) {
			String jsonString = encodeListTextToString(itemStack.get(DataComponents.LORE).lines(), level.registryAccess());
			customData.putString("itemLore_" + itemIndex, jsonString);
		}

		if (itemStack.has(DataComponents.CUSTOM_DATA)) {
			CompoundTag itemCustomData = itemStack.get(DataComponents.CUSTOM_DATA).copyTag();
			customData.put("itemCustomData_" + itemIndex, itemCustomData);
		}

		DataComponentMap.Builder componentMapBuilder = DataComponentMap.builder();
		componentMapBuilder.addAll(prevComponenetMap);
		componentMapBuilder.set(DataComponents.CUSTOM_DATA, CustomData.of(customData));
		return componentMapBuilder.build();
	}

	public static void move(Level level, BlockPos moveFrom, BlockPos moveTo) {
		BlockEntity blockEntity = level.getBlockEntity(moveFrom);
		if (blockEntity instanceof EmptyBlockEntity) {
			level.setBlockEntity(new EmptyBlockEntity(moveTo, level.getBlockState(moveTo)));
			BlockEntity moveToEntity = level.getBlockEntity(moveTo);
			if (moveToEntity != null) moveToEntity.setComponents(blockEntity.components());
		}
	}

	public static void move(LevelAccessor world, BlockPos moveFrom, BlockPos moveTo) {
		BlockEntity blockEntity = world.getBlockEntity(moveFrom);
		if (blockEntity instanceof EmptyBlockEntity) {
			if (world.getServer() == null) return;

			AtomicReference<ServerLevel> savedWorld = new AtomicReference<>();
			world.getServer().getAllLevels().forEach(serverWorld -> {
				if (serverWorld.dimension() == resolveDimension(world)) {
					savedWorld.set(serverWorld);
				}
			});

			ServerLevel savedWorldReference = savedWorld.get();
			if (savedWorldReference != null) {
				savedWorldReference.setBlockEntity(new EmptyBlockEntity(moveTo, savedWorldReference.getBlockState(moveTo)));
				BlockEntity moveToEntity = savedWorldReference.getBlockEntity(moveTo);
				if (moveToEntity != null) moveToEntity.setComponents(blockEntity.components());
			}
		}
	}

	private static net.minecraft.resources.ResourceKey<Level> resolveDimension(LevelAccessor world) {
		if (world instanceof Level level) {
			return level.dimension();
		}
		if (world instanceof net.minecraft.world.level.ServerLevelAccessor sla) {
			return sla.getLevel().dimension();
		}
		return Level.OVERWORLD;
	}

	public static void place(Level world, BlockState prevBlockState, BlockState blockState, BlockPos blockPos, ItemStack itemStack, DataComponentMap prevComponentMap) {
		if (prevBlockState.getBlock() == blockState.getBlock()) {
			BlockEntity prevBlocKEntity = world.getBlockEntity(blockPos);
			if (prevBlocKEntity != null) {
				if (blockState.hasProperty(BlockStateProperties.PICKLES)) {
					DataComponentMap newComponentMap = setAdditionalData(world, prevComponentMap, blockState, itemStack, BlockStateProperties.PICKLES);
					prevBlocKEntity.setComponents(newComponentMap);
					return;
				} else if (blockState.hasProperty(BlockStateProperties.LAYERS)) {
					DataComponentMap newComponentMap = setAdditionalData(world, prevComponentMap, blockState, itemStack, BlockStateProperties.LAYERS);
					prevBlocKEntity.setComponents(newComponentMap);
					return;
				} else if (blockState.hasProperty(BlockStateProperties.CANDLES)) {
					DataComponentMap newComponentMap = setAdditionalData(world, prevComponentMap, blockState, itemStack, BlockStateProperties.CANDLES);
					prevBlocKEntity.setComponents(newComponentMap);
					return;
				} else if (blockState.hasProperty(BlockStateProperties.SLAB_TYPE)) {
					CompoundTag customData = new CompoundTag();
					if (prevComponentMap.has(DataComponents.CUSTOM_DATA)) {
						customData = prevComponentMap.get(DataComponents.CUSTOM_DATA).copyTag();
					}

					if (itemStack.has(DataComponents.CUSTOM_NAME)) {
						DataResult<JsonElement> json = ComponentSerialization.CODEC.encodeStart(JsonOps.INSTANCE, itemStack.get(DataComponents.CUSTOM_NAME));
						String string = json.getOrThrow().toString();
						customData.putString("itemName_2", string);
					}
					if (itemStack.has(DataComponents.LORE) && !itemStack.get(DataComponents.LORE).lines().isEmpty()) {
						String jsonString = encodeListTextToString(itemStack.get(DataComponents.LORE).lines(), world.registryAccess());
						customData.putString("itemLore_2", jsonString);
					}

					if (itemStack.has(DataComponents.CUSTOM_DATA)) {
						CompoundTag itemCustomData = itemStack.get(DataComponents.CUSTOM_DATA).copyTag();
						customData.put("itemCustomData_2", itemCustomData);
					}

					DataComponentMap.Builder componentMapBuilder = DataComponentMap.builder();
					componentMapBuilder.addAll(prevComponentMap);
					componentMapBuilder.set(DataComponents.CUSTOM_DATA, CustomData.of(customData));
					prevBlocKEntity.setComponents(componentMapBuilder.build());
					return;
				} else {
					prevBlocKEntity.setComponents(prevComponentMap);
					return;
				}
			}
		}

		if (itemStack.has(DataComponents.CUSTOM_NAME)
				|| (itemStack.has(DataComponents.LORE) && !itemStack.get(DataComponents.LORE).lines().isEmpty())) {

			BlockPos checkPos = blockPos;

			if (blockState.hasProperty(BedBlock.PART)) {
				checkPos = checkPos.relative(BedBlock.getConnectedDirection(blockState));
			}

			if (blockState.hasProperty(BlockStateProperties.DOUBLE_BLOCK_HALF)) {
				if (blockState.getValue(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
					checkPos = checkPos.relative(Direction.DOWN);
				}
			}

			if (world.getBlockEntity(checkPos) == null && !blockState.hasBlockEntity()) {
				world.setBlockEntity(new EmptyBlockEntity(checkPos, blockState));
			}

			BlockEntity blockEntity = world.getBlockEntity(checkPos);
			if (blockEntity == null) return;

			DataComponentMap.Builder newBlockEntityComponents = DataComponentMap.builder();
			newBlockEntityComponents.addAll(blockEntity.components());

			if (itemStack.has(DataComponents.CUSTOM_NAME)) {
				newBlockEntityComponents.set(DataComponents.CUSTOM_NAME, itemStack.get(DataComponents.CUSTOM_NAME));
			}
			if (itemStack.has(DataComponents.LORE) && !itemStack.get(DataComponents.LORE).lines().isEmpty()) {
				newBlockEntityComponents.set(DataComponents.LORE, itemStack.get(DataComponents.LORE));
			}

			if (itemStack.has(DataComponents.CUSTOM_DATA)) {
				CompoundTag existingCustomData = new CompoundTag();
				if (blockEntity.components().has(DataComponents.CUSTOM_DATA)) {
					existingCustomData = blockEntity.components().get(DataComponents.CUSTOM_DATA).copyTag();
				}
				CompoundTag itemCustomData = itemStack.get(DataComponents.CUSTOM_DATA).copyTag();
				existingCustomData.put("itemCustomData_1", itemCustomData);
				newBlockEntityComponents.set(DataComponents.CUSTOM_DATA, CustomData.of(existingCustomData));
			}

			blockEntity.setComponents(newBlockEntityComponents.build());
		}
	}
}
