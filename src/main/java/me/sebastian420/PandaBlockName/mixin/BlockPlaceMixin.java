package me.sebastian420.PandaBlockName.mixin;


import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = BlockItem.class, priority = 5000)
public class BlockPlaceMixin {

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


    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "HEAD"))
    public void prePlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir,
            @Share("prevBlockState") LocalRef<BlockState> prevBlockStateRef,
            @Share("prevEntityComponents") LocalRef<ComponentMap> prevEntityComponentsRef)
    {
        prevBlockStateRef.set(context.getWorld().getBlockState(context.getBlockPos()));
        if (context.getWorld().getBlockEntity(context.getBlockPos()) != null) {
            prevEntityComponentsRef.set(context.getWorld().getBlockEntity(context.getBlockPos()).getComponents());
        }
    }

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V"))
    public void place(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir,
            @Share("prevBlockState") LocalRef<BlockState> prevBlockStateRef,
            @Share("prevEntityComponents") LocalRef<ComponentMap> prevEntityComponentsRef,
            @Local(ordinal = 1) BlockState blockState,
            @Local(ordinal = 0) ItemStack itemStack,
            @Local(ordinal = 0) BlockPos blockPos,
            @Local(ordinal = 0) World world) {


        BlockState prevBlockState = prevBlockStateRef.get();

        //Don't change if the block hasn't changed (fix this for slabs)
        if (prevBlockState.getBlock() == blockState.getBlock()) {
            BlockEntity prevBlocKEntity = world.getBlockEntity(blockPos);
            if (prevBlocKEntity != null) {

                //Handle Pickles (lol)
                if (blockState.contains(Properties.PICKLES)){
                    ComponentMap prevComponenetMap = prevEntityComponentsRef.get();
                    ComponentMap newComponentMap = setAdditionalData(world, prevComponenetMap, blockState, itemStack, Properties.PICKLES);
                    prevBlocKEntity.setComponents(newComponentMap);
                    return;
                }
                //Handle layers
                else if (blockState.contains(Properties.LAYERS)){
                    ComponentMap prevComponenetMap = prevEntityComponentsRef.get();
                    ComponentMap newComponentMap = setAdditionalData(world, prevComponenetMap, blockState, itemStack, Properties.LAYERS);
                    prevBlocKEntity.setComponents(newComponentMap);
                    return;
                }
                //Handle Candles (lol)
                 else if (blockState.contains(Properties.CANDLES)){
                    ComponentMap prevComponenetMap = prevEntityComponentsRef.get();
                    ComponentMap newComponentMap = setAdditionalData(world, prevComponenetMap, blockState, itemStack, Properties.CANDLES);
                    prevBlocKEntity.setComponents(newComponentMap);
                    return;
                    //Handle Slabs
                }  else if (blockState.contains(Properties.SLAB_TYPE)){
                    ComponentMap prevComponenetMap = prevEntityComponentsRef.get();

                    NbtCompound customData = new NbtCompound();
                    if (prevComponenetMap.contains(DataComponentTypes.CUSTOM_DATA)) {
                        customData = prevComponenetMap.get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                    }

                    if (itemStack.contains(DataComponentTypes.CUSTOM_NAME)) {
                        customData.putString("itemName_2",Text.Serialization.toJsonString(itemStack.get(DataComponentTypes.CUSTOM_NAME),world.getRegistryManager()));
                    }
                    if (itemStack.contains(DataComponentTypes.LORE) && !itemStack.get(DataComponentTypes.LORE).lines().isEmpty()) {
                        String jsonString = encodeListTextToString(itemStack.get(DataComponentTypes.LORE).lines(),world.getRegistryManager());
                        customData.putString("itemLore_2",jsonString);
                    }

                    ComponentMap.Builder componentMapBuilder = ComponentMap.builder();
                    componentMapBuilder.addAll(prevComponenetMap);
                    componentMapBuilder.add(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(customData));
                    prevBlocKEntity.setComponents(componentMapBuilder.build());
                    return;
                }else {
                    prevBlocKEntity.setComponents(prevEntityComponentsRef.get());
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
