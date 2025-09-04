package ababby.vbscr.mixin;

import ababby.vbscr.VehicleBlockSeparationCraftingRecipe;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Item.class)
public abstract class ItemMixin {
    @Shadow public abstract String toString();

    @Unique
    private boolean ending(String whole, String... strings) {
        for (String end : strings) {
            if (whole.endsWith(end)) {
                return true;
            }
        }
        return false;
    }

    @Unique
    private void log(String ingredient, String remainder) {
        VehicleBlockSeparationCraftingRecipe.LOGGER.info("\"{}\" remains of \"{}\"", remainder, ingredient); //This was necessary for debugging.
    }

    @Final
    @ModifyReturnValue(method = "getRecipeRemainder", at = @At("RETURN"))
    private ItemStack getRecipeRemainder(ItemStack original) {
        if (original == ItemStack.EMPTY) {
            String ingredient = this.toString();
            String remainder;
            if (ending(ingredient, "_minecart")) {
                remainder = Items.MINECART.toString();
            } else if (ending(ingredient, "_chest_boat", "_chest_raft")) {
                remainder = ingredient.substring(0, ingredient.length() - 11) + ingredient.substring(ingredient.length() - 5);
            } else return original;
            log(ingredient, remainder);
            return new ItemStack(Registries.ITEM.get(Identifier.of(remainder)));
        }
        return original;
    }
}
