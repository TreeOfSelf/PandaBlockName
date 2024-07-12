package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import me.sebastian420.PandaBlockName.EmptyBlockEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.gen.feature.HugeFungusFeature;
import net.minecraft.world.gen.feature.HugeFungusFeatureConfig;
import net.minecraft.world.gen.feature.util.FeatureContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HugeFungusFeature.class)
public class HugeFungusGrowMixin {
    @Inject(method = "generate", at = @At(value = "HEAD"))
    public void generate(FeatureContext<HugeFungusFeatureConfig> context, CallbackInfoReturnable<Boolean> cir,
                         @Share("origin") LocalRef<BlockPos> origin) {
        origin.set(context.getOrigin());
    }

    @Inject(method = "generateStem", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/StructureWorldAccess;setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)Z"))
    private void generateStem(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                              @Share("origin") LocalRef<BlockPos> origin,
                              @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        BlockPos originalPos = origin.get();
        if (originalPos != null){
            BlockEntity blockEntity = world.getBlockEntity(originalPos);
            if (blockEntity instanceof EmptyBlockEntity) {
                World realWorld = (World) world;
                realWorld.addBlockEntity(new EmptyBlockEntity(blockPos,realWorld.getBlockState(blockPos)));
                realWorld.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
            }
        }
    }

    @Inject(method = "generateStem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void generateStemTwo(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                                 @Share("origin") LocalRef<BlockPos> origin,
                                 @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        BlockPos originalPos = origin.get();
        if (originalPos != null){
            BlockEntity blockEntity = world.getBlockEntity(originalPos);
            if (blockEntity instanceof EmptyBlockEntity) {
                World realWorld = (World) world;
                realWorld.addBlockEntity(new EmptyBlockEntity(blockPos,realWorld.getBlockState(blockPos)));
                realWorld.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
            }
        }
    }


    @Inject(method = "generateStem", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void generateStemThree(StructureWorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos pos, int stemHeight, boolean thickStem, CallbackInfo ci,
                                   @Share("origin") LocalRef<BlockPos> origin,
                                   @Local(ordinal = 0) BlockPos.Mutable blockPos) {
        BlockPos originalPos = origin.get();
        if (originalPos != null){
            BlockEntity blockEntity = world.getBlockEntity(originalPos);
            if (blockEntity instanceof EmptyBlockEntity) {
                World realWorld = (World) world;
                realWorld.addBlockEntity(new EmptyBlockEntity(blockPos,realWorld.getBlockState(blockPos)));
                realWorld.getBlockEntity(blockPos).setComponents(blockEntity.getComponents());
            }
        }
    }

    @Inject(method = "placeHatBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", ordinal = 0, shift = At.Shift.AFTER))
    private void placeHateBlock(WorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos.Mutable pos, float decorationChance, float generationChance, float vineChance, CallbackInfo ci,
                                @Share("origin") LocalRef<BlockPos> origin) {
        BlockPos originalPos = origin.get();
        if (originalPos != null){
            BlockEntity blockEntity = world.getBlockEntity(originalPos);
            if (blockEntity instanceof EmptyBlockEntity) {
                World realWorld = (World) world;
                realWorld.addBlockEntity(new EmptyBlockEntity(pos,realWorld.getBlockState(pos)));
                realWorld.getBlockEntity(pos).setComponents(blockEntity.getComponents());
            }
        }
    }


    @Inject(method = "placeHatBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/gen/feature/HugeFungusFeature;setBlockState(Lnet/minecraft/world/ModifiableWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V", ordinal = 1, shift = At.Shift.AFTER))
    private void placeHateBlockTwo(WorldAccess world, Random random, HugeFungusFeatureConfig config, BlockPos.Mutable pos, float decorationChance, float generationChance, float vineChance, CallbackInfo ci,
                                   @Share("origin") LocalRef<BlockPos> origin) {
        BlockPos originalPos = origin.get();
        if (originalPos != null){
            BlockEntity blockEntity = world.getBlockEntity(originalPos);
            if (blockEntity instanceof EmptyBlockEntity) {
                World realWorld = (World) world;
                realWorld.addBlockEntity(new EmptyBlockEntity(pos,realWorld.getBlockState(pos)));
                realWorld.getBlockEntity(pos).setComponents(blockEntity.getComponents());
            }
        }
    }
}