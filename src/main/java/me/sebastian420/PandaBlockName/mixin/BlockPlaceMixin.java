package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.sebastian420.PandaBlockName.BlockEntityPlacer;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;


@Mixin(value = BlockItem.class, priority = 5000)
public class BlockPlaceMixin {



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
        ComponentMap prevComponenetMap = prevEntityComponentsRef.get();

        BlockEntityPlacer.place(world, prevBlockState, blockState, blockPos, itemStack, prevComponenetMap);

    }
}
