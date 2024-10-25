package stfu.mixin;

import net.minecraft.recipe.RecipeEntry;
import net.minecraft.recipe.book.RecipeBook;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(RecipeBook.class)
public class ShowAllRecipes {
    @Inject(method = "contains(Lnet/minecraft/recipe/RecipeEntry;)Z", at = @At("HEAD"), cancellable = true)
    private void contains(RecipeEntry<?> recipe, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }
}
