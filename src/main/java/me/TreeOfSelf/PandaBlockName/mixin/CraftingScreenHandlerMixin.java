package me.TreeOfSelf.PandaBlockName.mixin;

import me.TreeOfSelf.PandaBlockName.PandaBlockNameConfig;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.ResultContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(CraftingMenu.class)
public abstract class CraftingScreenHandlerMixin {

	@ModifyArgs(
			method = "slotChangedCraftingGrid",
			at = @At(
					value = "INVOKE",
					target = "Lnet/minecraft/world/inventory/ResultContainer;setItem(ILnet/minecraft/world/item/ItemStack;)V"
			)
	)
	private static void panda_mergeCraftingComponents(
			Args args,
			AbstractContainerMenu menu,
			ServerLevel level,
			Player player,
			CraftingContainer container,
			ResultContainer resultSlots,
			@Nullable RecipeHolder<CraftingRecipe> recipeHint
	) {
		if (!PandaBlockNameConfig.isFeatureEnabled("Crafting")) return;
		ItemStack itemStack = args.get(1);
		if (itemStack.isEmpty()) return;
		net.minecraft.network.chat.Component customName = null;
		ItemLore customLore = null;
		CustomData customData = null;
		boolean allSame = true;
		for (int index = 0; index < container.getContainerSize(); index++) {
			ItemStack item = container.getItem(index);
			if (item.isEmpty()) continue;
			if (customName == null && customLore == null && customData == null) {
				if (item.has(DataComponents.CUSTOM_NAME)) customName = item.get(DataComponents.CUSTOM_NAME);
				if (item.has(DataComponents.LORE)) customLore = item.get(DataComponents.LORE);
				if (item.has(DataComponents.CUSTOM_DATA)) customData = item.get(DataComponents.CUSTOM_DATA);
				if (customName == null && customLore == null && customData == null) break;
			} else {
				if (customName != null) {
					if (item.has(DataComponents.CUSTOM_NAME)) {
						if (!item.get(DataComponents.CUSTOM_NAME).equals(customName)) {
							allSame = false;
							break;
						}
					} else {
						allSame = false;
						break;
					}
				}
				if (customLore != null) {
					if (item.has(DataComponents.LORE)) {
						if (!item.get(DataComponents.LORE).equals(customLore)) {
							allSame = false;
							break;
						}
					} else {
						allSame = false;
						break;
					}
				}
				if (customData != null) {
					if (item.has(DataComponents.CUSTOM_DATA)) {
						if (!item.get(DataComponents.CUSTOM_DATA).equals(customData)) {
							allSame = false;
							break;
						}
					} else {
						allSame = false;
						break;
					}
				}
			}
		}
		if (!allSame) return;
		ItemStack out = itemStack.copy();
		if (customName != null) out.set(DataComponents.CUSTOM_NAME, customName);
		if (customLore != null) out.set(DataComponents.LORE, customLore);
		if (customData != null) out.set(DataComponents.CUSTOM_DATA, customData);
		args.set(1, out);
	}
}
