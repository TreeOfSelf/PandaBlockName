package me.sebastian420.PandaBlockName.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingResultInventory;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeEntry;
import net.minecraft.screen.CraftingScreenHandler;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.xml.crypto.Data;
import java.util.Optional;

@Mixin(CraftingScreenHandler.class)
public abstract class CraftingScreenHandlerMixin extends ScreenHandler {




    protected CraftingScreenHandlerMixin(@Nullable ScreenHandlerType<?> type, int syncId) {
        super(type, syncId);
    }

    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/inventory/CraftingResultInventory;setStack(ILnet/minecraft/item/ItemStack;)V"), cancellable = true)
    private static void onUpdateResult(ScreenHandler handler, ServerWorld world, PlayerEntity player, RecipeInputInventory craftingInventory, CraftingResultInventory resultInventory, RecipeEntry<CraftingRecipe> recipe, CallbackInfo ci, @Local(ordinal = 0) ItemStack itemStack) {
        Text customName = null;
        LoreComponent customLore = null;
        boolean allSame = true;
        for (int index = 0; index < craftingInventory.size() ; index ++){
            ItemStack item = craftingInventory.getStack(index);
            if (item.getItem() == Items.AIR) continue;

            if (customName == null && customLore == null) {
                if(item.contains(DataComponentTypes.CUSTOM_NAME)) customName = item.get(DataComponentTypes.CUSTOM_NAME);
                if (item.contains(DataComponentTypes.LORE)) customLore = item.get(DataComponentTypes.LORE);
                if (customName == null && customLore == null) break;
            } else {
                if (customName != null) {
                    if (item.contains(DataComponentTypes.CUSTOM_NAME)) {
                        if (!item.get(DataComponentTypes.CUSTOM_NAME).equals(customName)) {
                            allSame = false;
                            break;
                        }
                    } else {
                        allSame = false;
                        break;
                    }
                }
                if (customLore != null) {
                    if (item.contains(DataComponentTypes.LORE)) {
                        if (!item.get(DataComponentTypes.LORE).equals(customLore)) {
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

        if (allSame) {
            if (customName != null) itemStack.set(DataComponentTypes.CUSTOM_NAME, customName);
            if (customLore != null) itemStack.set(DataComponentTypes.LORE, customLore);
        }
    }
}
