package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.TreeFeature;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(TreeFeature.class)
public class SaplingGrowMixin {


    @Inject(method = "generate(Lnet/minecraft/world/gen/feature/util/FeatureContext;)Z", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    public final void generate(FeatureContext<TreeFeatureConfig> context, CallbackInfoReturnable<Boolean> cir,
                               @Local(ordinal = 0) StructureWorldAccess structureWorldAccess,
                               @Local(ordinal = 0) Set<BlockPos> set1,
                               @Local(ordinal = 1) Set<BlockPos> set2,
                               @Local(ordinal = 2) Set<BlockPos> set3,
                               @Local(ordinal = 3) Set<BlockPos> set4) {

        World world = (World) structureWorldAccess;
        BlockEntity blockEntity = world.getBlockEntity(context.getOrigin());

        if (blockEntity instanceof EmptyBlockEntity) {
            for (BlockPos pos : set1) {
                world.addBlockEntity(new EmptyBlockEntity(pos,world.getBlockState(pos)));
                world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
            }
            for (BlockPos pos : set2) {
                world.addBlockEntity(new EmptyBlockEntity(pos,world.getBlockState(pos)));
                world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
            }
            for (BlockPos pos : set3) {
                world.addBlockEntity(new EmptyBlockEntity(pos,world.getBlockState(pos)));
                world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
            }
            for (BlockPos pos : set4) {
                world.addBlockEntity(new EmptyBlockEntity(pos,world.getBlockState(pos)));
                world.getBlockEntity(pos).setComponents(blockEntity.getComponents());
            }
        }

    }


}

