package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PistonBlockEntity.class)
public class PistonBlockEntityMixin {

    @Inject(method = "finish", at = @At("RETURN"))
    private void ourFinish(CallbackInfo ci) {
    }

    @Inject(method = "tick", at = @At("RETURN"))
    private static void postTick(World world, BlockPos pos, BlockState state, PistonBlockEntity blockEntity, CallbackInfo ci) {
        if(blockEntity.isRemoved() || world.getBlockEntity(pos) == null){
            ComponentMap savedComponents = blockEntity.getComponents();
            if (!savedComponents.isEmpty() && !world.getBlockState(pos).hasBlockEntity()) {
                world.addBlockEntity(new EmptyBlockEntity(pos, world.getBlockState(pos)));
                BlockEntity newBlockEntity = world.getBlockEntity(pos);
                newBlockEntity.setComponents(savedComponents);
            }
        }
    }



}
