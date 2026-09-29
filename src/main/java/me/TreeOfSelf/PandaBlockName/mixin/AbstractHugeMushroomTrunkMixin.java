package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.BlockEntityPlacer;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.feature.AbstractHugeMushroomFeature;
import net.minecraft.world.level.levelgen.feature.HugeBrownMushroomFeature;
import net.minecraft.world.level.levelgen.feature.HugeRedMushroomFeature;
import org.spongepowered.asm.mixin.Mixin;

// AbstractHugeMushroomFeature became an interface in 26.3, so override its default placeTrunk on the implementations
@Mixin({HugeRedMushroomFeature.class, HugeBrownMushroomFeature.class})
public abstract class AbstractHugeMushroomTrunkMixin implements AbstractHugeMushroomFeature {

	@Override
	public void placeTrunk(WorldGenLevel level, RandomSource random, BlockPos origin, int treeHeight, BlockPos.MutableBlockPos blockPos) {
		boolean enabled = PandaBlockNameConfig.isVegetationFeatureEnabled("HugeMushroomFeatures");
		for (int dy = 0; dy < treeHeight; dy++) {
			blockPos.set(origin).move(Direction.UP, dy);
			this.placeMushroomBlock(level, blockPos, this.stemProvider().value().getState(level, random, origin));
			if (enabled) BlockEntityPlacer.move(level, origin, blockPos.immutable());
		}
	}
}
