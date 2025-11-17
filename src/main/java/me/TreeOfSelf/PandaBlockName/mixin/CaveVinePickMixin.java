package me.TreeOfSelf.PandaBlockName.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.CaveVines;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(CaveVines.class)
public interface CaveVinePickMixin {

    @Inject(method = "pickBerries", at = @At(value = "HEAD"), cancellable = true)
    private static void pickBerries(PlayerEntity player, BlockState state, World world, BlockPos pos, CallbackInfoReturnable<ItemActionResult> cir) {
        if (!PandaBlockNameConfig.isVegetationFeatureEnabled("GlowBerryPicking")) {
            return;
        }

        if (world.isClient()) {
            return;
        }

        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null) {
            if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_NAME) ||
                blockEntity.getComponents().contains(DataComponentTypes.LORE) ||
                (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_DATA) &&
                 blockEntity.getComponents().get(DataComponentTypes.CUSTOM_DATA).copyNbt().contains("itemCustomData_1"))) {

                // Cancel the default behavior and implement our own
                int i = 1 + world.getRandom().nextInt(2);
                ItemStack modifiedBerries = new ItemStack(Items.GLOW_BERRIES, i);

                // Apply custom name
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
                    modifiedBerries.set(DataComponentTypes.CUSTOM_NAME, customName);
                }

                // Apply custom lore
                if (blockEntity.getComponents().contains(DataComponentTypes.LORE)) {
                    modifiedBerries.set(DataComponentTypes.LORE, blockEntity.getComponents().get(DataComponentTypes.LORE));
                }

                // Apply custom data
                if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_DATA)) {
                    NbtCompound customData = blockEntity.getComponents().get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                    if (customData.contains("itemCustomData_1")) {
                        NbtCompound itemCustomData = customData.getCompound("itemCustomData_1").get();
                        modifiedBerries.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(itemCustomData));
                    }
                }

                // Drop the modified berries
                CaveVines.dropStack(world, pos, modifiedBerries);

                // Play sound
                float f = world.getRandom().nextFloat() * 0.1f + 0.9f;
                world.playSound(null, pos, SoundEvents.BLOCK_CAVE_VINES_PICK_BERRIES, SoundCategory.BLOCKS, 1.0f, f);

                // Update block state
                BlockState blockState = state.with(CaveVines.BERRIES, false);
                world.setBlockState(pos, blockState, 2);

                // Schedule tick
                world.scheduleBlockTick(pos, state.getBlock(), 1);

                cir.setReturnValue(ItemActionResult.success(world.isClient));
            }
        }
    }
}
