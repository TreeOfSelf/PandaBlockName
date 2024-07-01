package me.sebastian420.PandaBlockName.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.context.LootContextParameterSet;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = net.minecraft.block.AbstractBlock.class, priority = 5000)
public class BlockDropMixin {

    @Inject(method = "getDroppedStacks", at = @At(value = "TAIL"), cancellable = true)
    private void getDroppedStacks(BlockState state, LootContextParameterSet.Builder builder, CallbackInfoReturnable<List<ItemStack>> cir) {
        BlockEntity blockEntity = builder.getOptional(LootContextParameters.BLOCK_ENTITY);
        if (blockEntity != null) {
            if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_NAME)) {
                    List<ItemStack> items = cir.getReturnValue();
                    for (ItemStack item : items) {
                        item.set(DataComponentTypes.CUSTOM_NAME,  blockEntity.getComponents().get(DataComponentTypes.CUSTOM_NAME));
                    }
            }
        }
    }
}
