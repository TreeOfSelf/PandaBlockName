package me.TreeOfSelf.PandaBlockName.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.class)
public class BlockPickMixin {

    @Inject(method = "getPickStack", at = @At("RETURN"), cancellable = true)
    private void onGetPickStack(WorldView world, BlockPos pos, BlockState state, boolean includeData, CallbackInfoReturnable<ItemStack> cir) {
        if (!PandaBlockNameConfig.isFeatureEnabled("Block")) return;
        
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null) {
            ItemStack stack = cir.getReturnValue();
            
            if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_NAME)) {
                Text customName = blockEntity.getComponents().get(DataComponentTypes.CUSTOM_NAME);
                
                if (customName.getString().startsWith("{")) {
                    try {
                        JsonElement jsonElement = JsonParser.parseString(customName.getString());
                        DataResult<Pair<Text, JsonElement>> result = TextCodecs.CODEC.decode(JsonOps.INSTANCE, jsonElement);
                        customName = result.getOrThrow().getFirst();
                    } catch (Exception ignored) {
                    }
                }
                
                stack.set(DataComponentTypes.CUSTOM_NAME, customName);
            }
            
            if (blockEntity.getComponents().contains(DataComponentTypes.LORE)) {
                stack.set(DataComponentTypes.LORE, blockEntity.getComponents().get(DataComponentTypes.LORE));
            }
            
            cir.setReturnValue(stack);
        }
    }
}
