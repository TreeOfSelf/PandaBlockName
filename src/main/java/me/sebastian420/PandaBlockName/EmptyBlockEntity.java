

package me.sebastian420.PandaBlockName;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
import net.minecraft.util.math.BlockPos;

import static me.sebastian420.PandaBlockName.PandaBlockName.EMPTY_BLOCK_ENTITY_TYPE;

public class EmptyBlockEntity extends BlockEntity {

    public EmptyBlockEntity(BlockPos pos, BlockState state) {
        super(EMPTY_BLOCK_ENTITY_TYPE, pos, state);
    }

    @Override
    protected void readData(ReadView view) {
        super.readData(view);
    }

    protected void writeData(WriteView view) {
        super.writeData(view);
    }

}