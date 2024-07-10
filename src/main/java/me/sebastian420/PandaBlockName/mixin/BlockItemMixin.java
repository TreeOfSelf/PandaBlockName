package me.sebastian420.PandaBlockName.mixin;


import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = BlockItem.class, priority = 5000)
public class BlockItemMixin {


    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "HEAD"))
    public void prePlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir, @Share("prevBlockState") LocalRef<BlockState> prevBlockState) {
        prevBlockState.set(context.getWorld().getBlockState(context.getBlockPos()));
    }


    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onPlaced(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)V"))
    public void place(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir,
                      @Share("prevBlockState") LocalRef<BlockState> prevBlockState,
                      @Local(ordinal = 1) BlockState blockState) {
                    System.out.println("PLACED DETECTED");
                    System.out.println(prevBlockState.get());
                    System.out.println(blockState);
    }
}
