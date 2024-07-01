package me.sebastian420.PandaBlockName.mixin;


import com.mojang.serialization.MapCodec;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.entity.*;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(value = Block.class, priority = 5000)
public abstract class BlockPlaceMixin {
    @Shadow protected abstract MapCodec<? extends Block> getCodec();

    @Inject(at = @At("TAIL"), method = "onPlaced")
    private void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo ci) {

        if (itemStack.contains(DataComponentTypes.CUSTOM_NAME)) {

            Text customName = itemStack.get(DataComponentTypes.CUSTOM_NAME);
            String nameString = Text.Serialization.toJsonString(customName, DynamicRegistryManager.EMPTY);

            BlockPos checkPos = pos;

            if (state.getBlock() instanceof BedBlock) {
                BedBlock bedBlock = (BedBlock) state.getBlock();
                checkPos = checkPos.offset((Direction)state.get(bedBlock.FACING));
            }

            if (world.getBlockEntity(checkPos) == null) {
                world.addBlockEntity(new EmptyBlockEntity(checkPos, state));
            }

            BlockEntity blockEntity = world.getBlockEntity(checkPos);

            ComponentMap.Builder newBlockEntityComponents = ComponentMap.builder();
            newBlockEntityComponents.addAll(blockEntity.getComponents());
            newBlockEntityComponents.add(DataComponentTypes.CUSTOM_NAME, customName);

            blockEntity.setComponents(newBlockEntityComponents.build());

        }

    }
}
