package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.PistonBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.piston.PistonHandler;
import net.minecraft.component.ComponentMap;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;


@Mixin(PistonBlock.class)
public abstract class PistonBlockMixin {

    @Inject(method = "move", at = @At("HEAD"), cancellable = true)
    private void preMove(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir,
                         @Share("list3") LocalRef<List<ComponentMap>> list3,
                         @Share("list4") LocalRef<List<BlockPos>> list4) {
;
        PistonHandler pistonHandler = new PistonHandler(world, pos, dir, retract);
        pistonHandler.calculatePush();
        List<BlockPos> list = pistonHandler.getMovedBlocks();

        List<ComponentMap> temp_list3 = new ArrayList<>();
        List<BlockPos> temp_list4 = new ArrayList<>();
        if (!list.isEmpty())  temp_list4 = list.subList(0,list.size());
        int j;
        BlockPos blockPos3;
        for(j = list.size() - 1; j >= 0; --j) {
            blockPos3 = list.get(j);
            BlockEntity blockEntity = world.getBlockEntity(blockPos3);
            if (blockEntity != null && blockEntity instanceof EmptyBlockEntity) {
                temp_list3.addFirst(blockEntity.getComponents());
            } else {
                temp_list3.addFirst(null);
            }
        }

        list3.set(temp_list3);
        list4.set(temp_list4);
    }

    @Inject(method = "move", at = @At("TAIL"))
    private void postMove(World world, BlockPos pos, Direction dir, boolean retract, CallbackInfoReturnable<Boolean> cir,
                          @Share("list3") LocalRef<List<ComponentMap>> list3,
                          @Share("list4") LocalRef<List<BlockPos>> list4) {
        List<ComponentMap> temp_list3 = list3.get();
        List<BlockPos> temp_list4 = list4.get();
        Direction direction = retract ? dir : dir.getOpposite();
        int j;
        BlockPos blockPos3;
        ComponentMap componentMap;
        for(j = temp_list4.size() - 1; j >= 0; --j) {
            blockPos3 = temp_list4.get(j);
            blockPos3 = blockPos3.offset(direction);
            componentMap = temp_list3.get(j);
            BlockEntity blockEntity = world.getBlockEntity(blockPos3);
            if (componentMap != null) {
                blockEntity.setComponents(componentMap);
            }
        }

    }
}