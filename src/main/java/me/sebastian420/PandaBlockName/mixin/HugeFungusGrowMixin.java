package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.BlockEntityPlacer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.HugeFungusFeature;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HugeFungusFeature.class)
public class HugeFungusGrowMixin {

    @Inject(method = "generateStem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z", shift = At.Shift.AFTER))
    private void generateStem(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                              @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        if (!(world instanceof World)) return;
        BlockEntityPlacer.move((World) world, pos, blockPos);
    }

    @Inject(method = "generateStem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void generateStemTwo(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                                 @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        if (!(world instanceof World)) return;
        BlockEntityPlacer.move((World) world, pos, blockPos);
    }

    @Inject(method = "generateStem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void generateStemThree(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                                   @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        if (!(world instanceof World)) return;
        BlockEntityPlacer.move((World) world, pos, blockPos);
    }

    @Inject(method = "generateHat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;placeHatBlock(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/gen/feature/HugeFungusFeatureConfig;Lnet/minecraft/util/math/BlockPos$Mutable;FFF)V", ordinal = 0, shift = At.Shift.AFTER))
    private void generateHat(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                                   @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        if (!(world instanceof World)) return;
        BlockEntityPlacer.move((World) world, pos, blockPos);
    }

    @Inject(method = "generateHat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;placeHatBlock(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/gen/feature/HugeFungusFeatureConfig;Lnet/minecraft/util/math/BlockPos$Mutable;FFF)V", ordinal = 1, shift = At.Shift.AFTER))
    private void generateHatTwo(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                             @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        if (!(world instanceof World)) return;
        BlockEntityPlacer.move((World) world, pos, blockPos);
    }

    @Inject(method = "generateHat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;placeHatBlock(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/world/gen/feature/HugeFungusFeatureConfig;Lnet/minecraft/util/math/BlockPos$Mutable;FFF)V", ordinal = 2, shift = At.Shift.AFTER))
    private void generateHatThree(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                             @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        if (!(world instanceof World)) return;
        BlockEntityPlacer.move((World) world, pos, blockPos);
    }

    @Inject(method = "generateHat", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;placeWithOptionalVines(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V", shift = At.Shift.AFTER))
    private void optionalVine(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                                  @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        if (!(world instanceof World)) return;
        BlockEntityPlacer.move((World) world, pos, blockPos);
    }

}