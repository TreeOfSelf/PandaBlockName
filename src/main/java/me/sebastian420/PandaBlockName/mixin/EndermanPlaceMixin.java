package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import me.sebastian420.PandaBlockName.EndermanEntityAccess;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.component.ComponentMap;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/entity/mob/EndermanEntity$PlaceBlockGoal")
public class EndermanPlaceMixin {
    @Shadow @Final private EndermanEntity enderman;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/EndermanEntity;setCarriedBlock(Lnet/minecraft/block/BlockState;)V"))
    public void onTick(CallbackInfo ci, @Local(ordinal = 0) BlockPos pos) {
        EndermanEntityAccess accessor = (EndermanEntityAccess) this.enderman;
        ComponentMap componentMap = accessor.getItemComponentMap();

        if (componentMap != null){
            World world = this.enderman.getWorld();
            BlockState blockState = world.getBlockState(pos);
            world.addBlockEntity(new EmptyBlockEntity(pos,blockState));
            BlockEntity blockEntity = world.getBlockEntity(pos);
            blockEntity.setComponents(componentMap);
            accessor.setItemComponentMap(null);
        }
    }
}
