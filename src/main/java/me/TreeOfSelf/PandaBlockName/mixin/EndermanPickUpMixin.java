package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.EndermanEntityAccess;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net/minecraft/entity/mob/EndermanEntity$PickUpBlockGoal")
public class EndermanPickUpMixin {

    @Shadow @Final private EndermanEntity enderman;

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z"))
    public void onTick(CallbackInfo ci, @Local(ordinal = 0) BlockPos pos) {

        if (!PandaBlockNameConfig.isFeatureEnabled("Enderman")) return;

        BlockEntity blockEntity = this.enderman.getEntityWorld().getBlockEntity(pos);

        if (blockEntity != null) {
            EndermanEntityAccess accessor = (EndermanEntityAccess) this.enderman;
            accessor.setItemComponentMap(blockEntity.getComponents());
        }
    }
}