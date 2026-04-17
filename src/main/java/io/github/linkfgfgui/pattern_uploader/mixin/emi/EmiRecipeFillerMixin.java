package io.github.linkfgfgui.pattern_uploader.mixin.emi;

import appeng.client.gui.me.items.PatternEncodingTermScreen;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.handler.EmiCraftContext;
import dev.emi.emi.registry.EmiRecipeFiller;
import io.github.linkfgfgui.pattern_uploader.utils.SendPackets;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EmiRecipeFiller.class)
public class EmiRecipeFillerMixin {
    @Inject(method = "performFill", at = @At("RETURN"), remap = false)
    private static <T extends AbstractContainerMenu> void onPerformFill(EmiRecipe recipe, AbstractContainerScreen<T> screen, EmiCraftContext.Type type, EmiCraftContext.Destination destination, int amount, CallbackInfoReturnable<Boolean> cir) {
        if (screen instanceof PatternEncodingTermScreen) {
            ResourceLocation categoryId = recipe.getCategory().getId();
            SendPackets.send(categoryId);
        }
    }
}
