package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class BlockChangeMixin {

    @Shadow public abstract Block getBlock();

    @Shadow protected abstract BlockState asBlockState();

    @Inject(method = "onStateReplaced", at = @At("HEAD"))
    private void onStateReplacedInject(World world, BlockPos pos, BlockState state, boolean moved, CallbackInfo info) {
        /*if (this.asBlockState().getBlock() != state.getBlock()) {
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof EmptyBlockEntity)  {
                System.out.println("REMOVED FOR");
                System.out.println(this.asBlockState());
                System.out.println(state);
                world.removeBlockEntity(pos);
            }
        }*/
    }
}
