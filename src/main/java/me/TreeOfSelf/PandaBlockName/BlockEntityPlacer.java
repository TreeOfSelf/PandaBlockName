package me.TreeOfSelf.PandaBlockName;

import com.google.gson.JsonElement;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import org.spongepowered.asm.mixin.Unique;



import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class BlockEntityPlacer {

    @Unique
    private static String encodeListTextToString(List<Text> texts, DynamicRegistryManager registryManager) {

        StringBuilder combined = new StringBuilder();

        for (int i = 0; i < texts.size(); i++) {

            DataResult<JsonElement> json = TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, texts.get(i));
            String string = json.getOrThrow().toString();

            combined.append(string);
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

        int currentPropertyValue = blockState.get(property);
        String itemIndex = String.valueOf(currentPropertyValue);

        if (itemStack.contains(DataComponentTypes.CUSTOM_NAME)) {
            DataResult<JsonElement> json = TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, itemStack.get(DataComponentTypes.CUSTOM_NAME));
            String string = json.getOrThrow().toString();
            customData.putString("itemName_" + itemIndex, string);
        }
        if (itemStack.contains(DataComponentTypes.LORE) && !itemStack.get(DataComponentTypes.LORE).lines().isEmpty()) {
            String jsonString = encodeListTextToString(itemStack.get(DataComponentTypes.LORE).lines(),world.getRegistryManager());
            customData.putString("itemLore_" + itemIndex, jsonString);
        }

        if (itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
            NbtCompound itemCustomData = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
            customData.put("itemCustomData_" + itemIndex, itemCustomData);
        }

        ComponentMap.Builder componentMapBuilder = ComponentMap.builder();
        componentMapBuilder.addAll(prevComponenetMap);
        componentMapBuilder.add(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(customData));
        return componentMapBuilder.build();
    }

    public static void move(World world, BlockPos moveFrom, BlockPos moveTo) {
        BlockEntity blockEntity = world.getBlockEntity(moveFrom);
        if (blockEntity instanceof EmptyBlockEntity) {
            world.addBlockEntity(new EmptyBlockEntity(moveTo,world.getBlockState(moveTo)));
            BlockEntity moveToEntity = world.getBlockEntity(moveTo);
            if (moveToEntity != null)  moveToEntity.setComponents(blockEntity.getComponents());
        }
    }
    public static void move(WorldAccess world, BlockPos moveFrom, BlockPos moveTo) {
        BlockEntity blockEntity = world.getBlockEntity(moveFrom);
        if (blockEntity instanceof EmptyBlockEntity) {
            AtomicReference<ServerWorld> savedWorld = new AtomicReference<>();

           if (world.getServer() == null) return;

           world.getServer().getWorlds().forEach(serverWorld -> {
                if (serverWorld.getDimension() == world.getDimension()) {
                    savedWorld.set(serverWorld);
                }
            });

            ServerWorld savedWorldReference = savedWorld.get();
            if (savedWorldReference != null) {
                savedWorldReference.addBlockEntity(new EmptyBlockEntity(moveTo,savedWorldReference.getBlockState(moveTo)));
                BlockEntity moveToEntity = savedWorldReference.getBlockEntity(moveTo);
                if (moveToEntity != null)  moveToEntity.setComponents(blockEntity.getComponents());
            }
        }
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

                        DataResult<JsonElement> json = TextCodecs.CODEC.encodeStart(JsonOps.INSTANCE, itemStack.get(DataComponentTypes.CUSTOM_NAME));
                        String string = json.getOrThrow().toString();
                        customData.putString("itemName_2", string);
                    }
                    if (itemStack.contains(DataComponentTypes.LORE) && !itemStack.get(DataComponentTypes.LORE).lines().isEmpty()) {
                        String jsonString = encodeListTextToString(itemStack.get(DataComponentTypes.LORE).lines(),world.getRegistryManager());
                        customData.putString("itemLore_2",jsonString);
                    }

                    if (itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
                        NbtCompound itemCustomData = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                        customData.put("itemCustomData_2", itemCustomData);
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
                checkPos = checkPos.offset(BedBlock.getOppositePartDirection(blockState));
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

            if (itemStack.contains(DataComponentTypes.CUSTOM_DATA)) {
                NbtCompound existingCustomData = new NbtCompound();
                if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_DATA)) {
                    existingCustomData = blockEntity.getComponents().get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                }
                NbtCompound itemCustomData = itemStack.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                existingCustomData.put("itemCustomData_1", itemCustomData);
                newBlockEntityComponents.add(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(existingCustomData));
            }

            blockEntity.setComponents(newBlockEntityComponents.build());

        }
    }
}
