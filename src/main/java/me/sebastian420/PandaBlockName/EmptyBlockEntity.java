

package me.sebastian420.PandaBlockName;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.util.math.BlockPos;

import static me.sebastian420.PandaBlockName.PandaBlockName.EMPTY_BLOCK_ENTITY_TYPE;

public class EmptyBlockEntity extends BlockEntity {

    public EmptyBlockEntity(BlockPos pos, BlockState state) {
        super(EMPTY_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    protected void readNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(nbt, registryLookup);

    }

    @Override
    protected void writeNbt(NbtCompound nbt, RegistryWrapper.WrapperLookup registryLookup) {
        super.writeNbt(nbt, registryLookup);
    }


}
