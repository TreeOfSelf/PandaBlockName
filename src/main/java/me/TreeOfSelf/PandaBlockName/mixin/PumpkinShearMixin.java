package me.TreeOfSelf.PandaBlockName.mixin;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.text.TextCodecs;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PumpkinBlock.class)
public abstract class PumpkinShearMixin {

    @Inject(method = "onUseWithItem", at = @At(value = "HEAD"), cancellable = true)
    private void onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!PandaBlockNameConfig.isVegetationFeatureEnabled("PumpkinShearing")) {
            return;
        }

        if (!stack.isOf(Items.SHEARS)) {
            return;
        }

        if (world.isClient()) {
            cir.setReturnValue(ActionResult.SUCCESS);
            return;
        }

        Direction direction = hit.getSide();
        Direction direction2 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
        world.playSound(null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
        world.setBlockState(pos, Blocks.CARVED_PUMPKIN.getDefaultState().with(CarvedPumpkinBlock.FACING, direction2), 11);

        ItemStack modifiedSeeds = new ItemStack(Items.PUMPKIN_SEEDS, 4);
        BlockEntity blockEntity = world.getBlockEntity(pos);
        if (blockEntity != null) {
            if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_NAME)) {

                Text customName = blockEntity.getComponents().get(DataComponentTypes.CUSTOM_NAME);
                if (customName.getString().startsWith("{")) {
                    try {
                        JsonElement jsonElement = JsonParser.parseString(blockEntity.getComponents().get(DataComponentTypes.CUSTOM_NAME).getString());
                        DataResult<Pair<Text, JsonElement>> result = TextCodecs.CODEC.decode(JsonOps.INSTANCE, jsonElement);
                        customName = result.getOrThrow().getFirst();
                    } catch (Exception ignored) {
                    }
                }

                modifiedSeeds.set(DataComponentTypes.CUSTOM_NAME, customName);
            }
            if (blockEntity.getComponents().contains(DataComponentTypes.LORE)) {
                modifiedSeeds.set(DataComponentTypes.LORE, blockEntity.getComponents().get(DataComponentTypes.LORE));
            }
            
            if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_DATA)) {
                NbtCompound customData = blockEntity.getComponents().get(DataComponentTypes.CUSTOM_DATA).copyNbt();
                if (customData.contains("itemCustomData_1")) {
                    NbtCompound itemCustomData = customData.getCompound("itemCustomData_1").get();
                    modifiedSeeds.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(itemCustomData));
                }
            }
        }

        ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5 + (double)direction2.getOffsetX() * 0.65, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5 + (double)direction2.getOffsetZ() * 0.65, modifiedSeeds);
        itemEntity.setVelocity(0.05 * (double)direction2.getOffsetX() + world.random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction2.getOffsetZ() + world.random.nextDouble() * 0.02);
        world.spawnEntity(itemEntity);
        stack.damage(1, player, hand);
        world.emitGameEvent(player, GameEvent.SHEAR, pos);
        player.incrementStat(Stats.USED.getOrCreateStat(Items.SHEARS));
        cir.setReturnValue(ActionResult.SUCCESS);
    }
}
