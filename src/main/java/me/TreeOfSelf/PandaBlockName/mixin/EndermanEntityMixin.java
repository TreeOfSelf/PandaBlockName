package me.TreeOfSelf.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.TreeOfSelf.PandaBlockName.EndermanEntityAccess;
import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderMan.class)
public abstract class EndermanEntityMixin implements EndermanEntityAccess {

	@Unique
	private DataComponentMap panda_block_name$componentMap;

	@Override
	public void setItemComponentMap(DataComponentMap componentMap) {
		this.panda_block_name$componentMap = componentMap;
	}

	@Override
	public DataComponentMap getItemComponentMap() {
		return this.panda_block_name$componentMap;
	}

	@Inject(
			method = "dropCustomDeathLoot",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/entity/monster/EnderMan;spawnAtLocation(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"
			)
	)
	protected void panda_onCarriedBlockDrop(ServerLevel level, DamageSource source, boolean killedByPlayer, CallbackInfo ci, @Local(name = "itemStack") ItemStack itemStack) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Enderman")) return;

		DataComponentMap componentMap = getItemComponentMap();
		if (componentMap == null) return;

		if (componentMap.has(DataComponents.CUSTOM_NAME)) {
			itemStack.set(DataComponents.CUSTOM_NAME, componentMap.get(DataComponents.CUSTOM_NAME));
		}
		if (componentMap.has(DataComponents.LORE)) {
			itemStack.set(DataComponents.LORE, componentMap.get(DataComponents.LORE));
		}
		if (componentMap.has(DataComponents.CUSTOM_DATA)) {
			CompoundTag customData = componentMap.get(DataComponents.CUSTOM_DATA).copyTag();
			if (customData.contains("itemCustomData_1")) {
				CompoundTag itemCustomData = customData.getCompound("itemCustomData_1").orElse(new CompoundTag());
				itemStack.set(DataComponents.CUSTOM_DATA, CustomData.of(itemCustomData));
			}
		}
		setItemComponentMap(null);
	}
}
