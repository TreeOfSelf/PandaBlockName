package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.ItemData;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(value = net.minecraft.block.AbstractBlock.class, priority = 5000)
public class BlockDropMixin {


    @Unique
    ItemData getItemData(World world, NbtCompound customData, int index){
        ItemData itemData = new ItemData();
        String nameKey = "itemName_"+index;
        String loreKey = "itemLore_"+index;
        if (customData.contains(nameKey))
            itemData.CustomName = Text.Serialization.fromJson(customData.getString(nameKey), world.getRegistryManager());
        if (customData.contains(loreKey)) {
            String[] loreString = customData.getString(loreKey).split("\\{\\\\\"\\\\}");
            List<Text> textList = new ArrayList<>();
            for (String s : loreString) {
                textList.add(Text.Serialization.fromJson(s,world.getRegistryManager()));
            }
            itemData.Lore = new LoreComponent(textList);
        }
        return itemData;
    }

    @Unique
    private void addOrCombine(List<ItemStack> list, ItemStack itemStack) {

        boolean add = true;
        for (ItemStack item : list) {
            if (item.getComponents().equals(itemStack.getComponents())) {
                item.increment(1);
                add = false;
                break;
            }
        }

        if (add) list.add(itemStack);
    }

    @Inject(method = "getDroppedStacks", at = @At(value = "TAIL"))
    private void getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {

        World world = builder.getWorld();
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity == null){
            blockEntity = builder.getWorld().getBlockEntity(BlockPos.ofFloored(builder.getOptional(LootContextParameters.ORIGIN)));
        }
        if (blockEntity != null) {

            boolean multiple = state.contains(Properties.PICKLES) ||
                    state.contains(Properties.CANDLES) ||
                    state.contains(Properties.LAYERS) ||
                    state.contains(Properties.SLAB_TYPE);

            //Multiple
            if (multiple) {

                int itemIndex = 1;

                NbtCompound customData = null;
                if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_DATA))
                    customData = blockEntity.getComponents().get(DataComponentTypes.CUSTOM_DATA).copyNbt();

                List<ItemStack> items = cir.getReturnValue();
                List<ItemStack> additionalItems = new ArrayList<>();

                outerLoop:
                for (ItemStack item : items) {
                    while (item.getCount() > 0) {
                        //First item
                        if (itemIndex == 1) {
                            ItemStack newItem = null;
                            boolean newItemChanged = false;
                            if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_NAME)) {
                                newItem = item.copyWithCount(1);
                                newItem.set(DataComponentTypes.CUSTOM_NAME, blockEntity.getComponents().get(DataComponentTypes.CUSTOM_NAME));
                                newItemChanged = true;
                            }
                            if (blockEntity.getComponents().contains(DataComponentTypes.LORE)) {
                                if (newItem == null) newItem = item.copyWithCount(1);
                                newItem.set(DataComponentTypes.LORE, blockEntity.getComponents().get(DataComponentTypes.LORE));
                                newItemChanged = true;
                            }
                            if (newItemChanged) {
                                item.decrement(1);
                                addOrCombine(additionalItems, newItem);
                                if (item.getCount() == 0) items.remove(item);
                            }
                            //Non first-item
                        } else {
                            if (customData == null) break outerLoop;
                            ItemStack newItem = null;
                            boolean newItemChanged = false;
                            ItemData itemData = getItemData(world, customData, itemIndex);
                            if (itemData.CustomName != null) {
                                newItem = item.copyWithCount(1);
                                newItem.set(DataComponentTypes.CUSTOM_NAME, itemData.CustomName);
                                newItemChanged = true;
                            }
                            if (itemData.Lore != null) {
                                if (newItem == null) newItem = item.copyWithCount(1);
                                newItem.set(DataComponentTypes.LORE, itemData.Lore);
                                newItemChanged = true;
                            }
                            if (newItemChanged) {
                                item.decrement(1);
                                addOrCombine(additionalItems, newItem);
                                if (item.getCount() == 0) items.remove(item);
                            } else {
                                break outerLoop;
                            }
                        }
                        itemIndex++;
                    }
                }

                items.addAll(additionalItems);

            //Single
            } else {
                List<ItemStack> items = cir.getReturnValue();

                if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_NAME)) {
                    for (ItemStack item : items) {
                        item.set(DataComponentTypes.CUSTOM_NAME,  blockEntity.getComponents().get(DataComponentTypes.CUSTOM_NAME));
                    }
                }
                if (blockEntity.getComponents().contains(DataComponentTypes.LORE)) {
                    for (ItemStack item : items) {
                        item.set(DataComponentTypes.LORE,  blockEntity.getComponents().get(DataComponentTypes.LORE));
                    }
                }
            }
            world.removeBlockEntity(blockEntity.getPos());
        }
    }
}
