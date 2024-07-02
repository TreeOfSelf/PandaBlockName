package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.PistonExtensionBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.component.ComponentMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;


@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {

    @Unique
    List<ComponentMap> list3;
    List<BlockPos> list4;

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void preMove(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir) {
        PistonHandler pistonHandler = new PistonHandler(world, pos, dir, retract);
        pistonHandler.calculatePush();
        List<BlockPos> list = pistonHandler.getMovedBlocks();
        this.list3 = new ArrayList<>();
        this.list4 = new ArrayList<>();
        if (!list.isEmpty())  this.list4 = list.subList(0,list.size());
        int j;
        BlockPos blockPos3;
        for(j = list.size() - 1; j >= 0; --j) {
            blockPos3 = list.get(j);
            BlockEntity blockEntity = world.getBlockEntity(blockPos3);
            if (blockEntity != null && blockEntity instanceof EmptyBlockEntity) {
                this.list3.addFirst(blockEntity.getComponents());
            } else {
                this.list3.addFirst(null);
            }
        }
    }

    @Inject(method = "move", at = @At("TAIL"))
    private void postMove(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir) {
        Direction direction = retract ? dir : dir.getOpposite();
        int j;
        BlockPos blockPos3;
        ComponentMap componentMap;
        for(j = list4.size() - 1; j >= 0; --j) {
            blockPos3 = list4.get(j);
            blockPos3 = blockPos3.offset(direction);
            componentMap = this.list3.get(j);
            BlockEntity blockEntity = world.getBlockEntity(blockPos3);
            if (componentMap != null) {
                blockEntity.setComponents(componentMap);
            }
        }

    }
}