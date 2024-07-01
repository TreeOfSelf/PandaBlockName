package me.sebastian420.PandaBlockName.mixin;

import net.minecraft.block.entity.PistonBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PistonBlockEntity.class)
public interface PistonBlockEntityAccessor {
    @Accessor("savedWorldTime")
    void setSavedWorldTime(long savedWorldTime);

    @Accessor("lastProgress")
    float getLastProgress();

    @Accessor("lastProgress")
    void setLastProgress(float lastProgress);

    @Accessor("progress")
    float getProgress();

    @Accessor("progress")
    void setProgress(float progress);

    @Accessor("field_26705")
    int getField26705();

    @Accessor("field_26705")
    void setField26705(int field26705);


    @Invoker("pushEntities")
    static void callPushEntities(World world, BlockPos pos, float f, PistonBlockEntity blockEntity) {
        throw new UnsupportedOperationException();
    }

    @Invoker("moveEntitiesInHoneyBlock")
    static void callMoveEntitiesInHoneyBlock(World world, BlockPos pos, float f, PistonBlockEntity blockEntity) {
        throw new UnsupportedOperationException();
    }
}
