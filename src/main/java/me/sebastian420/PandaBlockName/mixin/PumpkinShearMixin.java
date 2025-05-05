package me.sebastian420.PandaBlockName.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CarvedPumpkinBlock;
import net.minecraft.block.PumpkinBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(PumpkinBlock.class)
public abstract class PumpkinShearMixin {
    @Shadow protected abstract ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit);

    @Inject(method = "onUseWithItem", at = @At(value = "HEAD"), cancellable = true)
    private ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir) {
        if (!stack.isOf(Items.SHEARS)) {
            return onUseWithItem(stack, state, world, pos, player, hand, hit);
        } else if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            Direction direction = hit.getSide();
            Direction direction2 = direction.getAxis() == Direction.Axis.Y ? player.getHorizontalFacing().getOpposite() : direction;
            world.playSound((Entity)null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
            world.setBlockState(pos, (BlockState) Blocks.CARVED_PUMPKIN.getDefaultState().with(CarvedPumpkinBlock.FACING, direction2), 11);

            ItemStack modifiedSeeds = new ItemStack(Items.PUMPKIN_SEEDS, 4);
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity != null) {
                if (blockEntity.getComponents().contains(DataComponentTypes.CUSTOM_NAME)) {

                    Text customName = blockEntity.getComponents().get(DataComponentTypes.CUSTOM_NAME);
                    if (customName.getString().startsWith("{")) {
                        try {
                            customName = Text.Serialization.fromJson(blockEntity.getComponents().get(DataComponentTypes.CUSTOM_NAME).getString(),world.getRegistryManager());
                        } catch (Exception ignored) {
                        }
                    }

                    modifiedSeeds.set(DataComponentTypes.CUSTOM_NAME, customName);
                }
                if (blockEntity.getComponents().contains(DataComponentTypes.LORE)) {
                    modifiedSeeds.set(DataComponentTypes.LORE, blockEntity.getComponents().get(DataComponentTypes.LORE));
                }
            }

            ItemEntity itemEntity = new ItemEntity(world, (double)pos.getX() + 0.5 + (double)direction2.getOffsetX() * 0.65, (double)pos.getY() + 0.1, (double)pos.getZ() + 0.5 + (double)direction2.getOffsetZ() * 0.65, modifiedSeeds);
            itemEntity.setVelocity(0.05 * (double)direction2.getOffsetX() + world.random.nextDouble() * 0.02, 0.05, 0.05 * (double)direction2.getOffsetZ() + world.random.nextDouble() * 0.02);
            world.spawnEntity(itemEntity);
            stack.damage(1, player, LivingEntity.getSlotForHand(hand));
            world.emitGameEvent(player, GameEvent.SHEAR, pos);
            player.incrementStat(Stats.USED.getOrCreateStat(Items.SHEARS));
            cir.setReturnValue(ActionResult.SUCCESS);
            return ActionResult.SUCCESS;
        }
    }
}
