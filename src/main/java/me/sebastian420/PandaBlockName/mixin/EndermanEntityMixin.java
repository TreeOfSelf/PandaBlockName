package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.sebastian420.PandaBlockName.EndermanEntityAccess;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.EndermanEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndermanEntity.class)
public abstract class EndermanEntityMixin implements EndermanEntityAccess {

    @Unique
    private ComponentMap componentMap;

    @Override
    public void setItemComponentMap(ComponentMap componentMap) {
        this.componentMap = componentMap;
    }

    @Override
    public ComponentMap getItemComponentMap() {
        return this.componentMap;
    }

    @Inject(method = "dropEquipment", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/mob/EndermanEntity;dropStack(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/item/ItemStack;)Lnet/minecraft/entity/ItemEntity;"))
    protected void dropEquipment(ServerWorld world, DamageSource source, boolean causedByPlayer, CallbackInfo ci, @Local(ordinal = 1) ItemStack itemStack) {
        ComponentMap componentMap = getItemComponentMap();
        if (componentMap != null){
            if (componentMap.contains(DataComponentTypes.CUSTOM_NAME))
                itemStack.set(DataComponentTypes.CUSTOM_NAME,componentMap.get(DataComponentTypes.CUSTOM_NAME));
            if (componentMap.contains(DataComponentTypes.LORE))
                itemStack.set(DataComponentTypes.LORE,componentMap.get(DataComponentTypes.LORE));
            setItemComponentMap(null);
        }
    }
}
