package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.EmptyBlockEntity;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(TreeFeature.class)
public class SaplingGrowMixin {

	@Inject(
			method = "place",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/level/levelgen/structure/BoundingBox;encapsulatingPositions(Ljava/lang/Iterable;)Ljava/util/Optional;",
					shift = At.Shift.BEFORE
			)
	)
	private void panda_afterTreeGenerated(
			WorldGenLevel level,
			ChunkGenerator chunkGenerator,
			RandomSource random,
			BlockPos origin,
			CallbackInfoReturnable<Boolean> cir,
			@Local(name = "rootPositions") Set<BlockPos> rootPositions,
			@Local(name = "trunks") Set<BlockPos> trunks,
			@Local(name = "foliage") Set<BlockPos> foliage,
			@Local(name = "decorations") Set<BlockPos> decorations
	) {
		if (!PandaBlockNameConfig.isVegetationFeatureEnabled("SaplingGrowth")) return;
		ServerLevel world = level.getLevel();
		BlockEntity blockEntity = world.getBlockEntity(origin);
		if (!(blockEntity instanceof EmptyBlockEntity)) return;
		for (BlockPos pos : rootPositions) {
			world.setBlockEntity(new EmptyBlockEntity(pos, world.getBlockState(pos)));
			BlockEntity be = world.getBlockEntity(pos);
			if (be != null) be.setComponents(blockEntity.components());
		}
		for (BlockPos pos : trunks) {
			world.setBlockEntity(new EmptyBlockEntity(pos, world.getBlockState(pos)));
			BlockEntity be = world.getBlockEntity(pos);
			if (be != null) be.setComponents(blockEntity.components());
		}
		for (BlockPos pos : foliage) {
			world.setBlockEntity(new EmptyBlockEntity(pos, world.getBlockState(pos)));
			BlockEntity be = world.getBlockEntity(pos);
			if (be != null) be.setComponents(blockEntity.components());
		}
		for (BlockPos pos : decorations) {
			world.setBlockEntity(new EmptyBlockEntity(pos, world.getBlockState(pos)));
			BlockEntity be = world.getBlockEntity(pos);
			if (be != null) be.setComponents(blockEntity.components());
		}
	}
}
