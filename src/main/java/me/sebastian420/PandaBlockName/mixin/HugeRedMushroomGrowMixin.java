package me.sebastian420.PandaBlockName.mixin;

import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeMushroomFeatureConfig;
import net.minecraft.world.gen.feature.HugeRedMushroomFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeRedMushroomFeature.class)
public class HugeRedMushroomGrowMixin {
    @Inject(method = "generateCap", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/gen/feature/HugeRedMushroomFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    protected void generateCap(WorldAccess world, Random random, BlockPos start, int y, BlockPos.Mutable mutable, HugeMushroomFeatureConfig config, CallbackInfo ci) {
        World realWorld = (World) world;
        BlockEntity blockEntity = realWorld.getBlockEntity(start);
        if (blockEntity instanceof EmptyBlockEntity) {
            realWorld.addBlockEntity(new EmptyBlockEntity(mutable,realWorld.getBlockState(mutable)));
            realWorld.getBlockEntity(mutable).setComponents(blockEntity.getComponents());
        }
    }
}
