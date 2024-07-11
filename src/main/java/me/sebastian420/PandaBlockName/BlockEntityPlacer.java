package me.sebastian420.PandaBlockName;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

public class BlockEntityPlacer {

    @Unique
    private static String encodeListTextToString(List<Text> texts, DynamicRegistryManager registryManager) {

        StringBuilder combined = new StringBuilder();

        for (int i = 0; i < texts.size(); i++) {
            combined.append(Text.Serialization.toJsonString(texts.get(i), registryManager));
            if (i < texts.size() - 1) {
                combined.append("{\\\"\\}");
            }
        }

        return combined.toString();
    }

    private static String getStringRef(String checkString, NbtCompound customData){
        int checkNumber = 2;
        while (customData.contains(checkString+checkNumber)) checkNumber++;
        return checkString+checkNumber;
    }

    @Unique
    private static ComponentMap setAdditionalData(World world, ComponentMap prevComponenetMap, BlockState blockState, ItemStack itemStack, IntProperty property){
        NbtCompound customData = new NbtCompound();
        if (prevComponenetMap.contains(DataComponentTypes.CUSTOM_DATA)) {
            customData = prevComponenetMap.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
        }

        if (itemStack.contains(DataComponentTypes.CUSTOM_NAME)) {
            customData.putString(getStringRef("itemName_",customData),Text.Serialization.toJsonString(itemStack.get(DataComponentTypes.CUSTOM_NAME),world.getRegistryManager()));
        }
        if (itemStack.contains(DataComponentTypes.LORE) && !itemStack.get(DataComponentTypes.LORE).lines().isEmpty()) {
            String jsonString = encodeListTextToString(itemStack.get(DataComponentTypes.LORE).lines(),world.getRegistryManager());
            customData.putString(getStringRef("itemLore_",customData),jsonString);
        }


        ComponentMap.Builder componentMapBuilder = ComponentMap.builder();
        componentMapBuilder.addAll(prevComponenetMap);
        componentMapBuilder.add(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(customData));
        return componentMapBuilder.build();
    }

    public static void place(World world, BlockState prevBlockState, BlockState blockState,BlockPos blockPos, ItemStack itemStack, ComponentMap prevComponentMap ) {

        //Don't change if the block hasn't changed (fix this for slabs)
        if (prevBlockState.getBlock() == blockState.getBlock()) {
            BlockEntity prevBlocKEntity = world.getBlockEntity(blockPos);
            if (prevBlocKEntity != null) {

                //Handle Pickles (lol)
                if (blockState.contains(Properties.PICKLES)){
                    ComponentMap newComponentMap = setAdditionalData(world, prevComponentMap, blockState, itemStack, Properties.PICKLES);
                    prevBlocKEntity.setComponents(newComponentMap);
                    return;
                }
                //Handle layers
                else if (blockState.contains(Properties.LAYERS)){
                    ComponentMap newComponentMap = setAdditionalData(world, prevComponentMap, blockState, itemStack, Properties.LAYERS);
                    prevBlocKEntity.setComponents(newComponentMap);
                    return;
                }
                //Handle Candles (lol)
                else if (blockState.contains(Properties.CANDLES)){
                    ComponentMap newComponentMap = setAdditionalData(world, prevComponentMap, blockState, itemStack, Properties.CANDLES);
                    prevBlocKEntity.setComponents(newComponentMap);
                    return;
                    //Handle Slabs
                }  else if (blockState.contains(Properties.SLAB_TYPE)){

                    NbtCompound customData = new NbtCompound();
                    if (prevComponentMap.contains(DataComponentTypes.CUSTOM_DATA)) {
                        customData = prevComponentMap.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                    }

                    if (itemStack.contains(DataComponentTypes.CUSTOM_NAME)) {
                        customData.putString("itemName_2", Text.Serialization.toJsonString(itemStack.get(DataComponentTypes.CUSTOM_NAME),world.getRegistryManager()));
                    }
                    if (itemStack.contains(DataComponentTypes.LORE) && !itemStack.get(DataComponentTypes.LORE).lines().isEmpty()) {
                        String jsonString = encodeListTextToString(itemStack.get(DataComponentTypes.LORE).lines(),world.getRegistryManager());
                        customData.putString("itemLore_2",jsonString);
                    }

                    ComponentMap.Builder componentMapBuilder = ComponentMap.builder();
                    componentMapBuilder.addAll(prevComponentMap);
                    componentMapBuilder.add(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(customData));
                    prevBlocKEntity.setComponents(componentMapBuilder.build());
                    return;
                }else {
                    prevBlocKEntity.setComponents(prevComponentMap);
                    return;
                }
            }

        }

        if (itemStack.contains(DataComponentTypes.CUSTOM_NAME) ||
                (itemStack.contains(DataComponentTypes.LORE) && !itemStack.get(DataComponentTypes.LORE).lines().isEmpty())) {

            BlockPos checkPos = blockPos;

            //If bed
            if (blockState.contains(Properties.BED_PART)) {
                checkPos = checkPos.offset(blockState.get(Properties.FACING));
            }

            //Handle double places (lilac/doors)
            if (blockState.contains(Properties.DOUBLE_BLOCK_HALF)) {
                if (blockState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER){
                    checkPos = checkPos.offset(Direction.DOWN);
                }
            }

            //Create entity if one doesn't already exist
            if (world.getBlockEntity(checkPos) == null) {
                world.addBlockEntity(new EmptyBlockEntity(checkPos, blockState));
            }


            BlockEntity blockEntity = world.getBlockEntity(checkPos);

            ComponentMap.Builder newBlockEntityComponents = ComponentMap.builder();
            newBlockEntityComponents.addAll(blockEntity.getComponents());

            if (itemStack.contains(DataComponentTypes.CUSTOM_NAME)) {
                newBlockEntityComponents.add(DataComponentTypes.CUSTOM_NAME, itemStack.get(DataComponentTypes.CUSTOM_NAME));
            }
            if (itemStack.contains(DataComponentTypes.LORE) && !itemStack.get(DataComponentTypes.LORE).lines().isEmpty()) {
                newBlockEntityComponents.add(DataComponentTypes.LORE, itemStack.get(DataComponentTypes.LORE));
            }

            blockEntity.setComponents(newBlockEntityComponents.build());

        }
    }
}
