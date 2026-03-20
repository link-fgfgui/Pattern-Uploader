package io.github.linkfgfgui.pattern_uploader.mixin;

import appeng.menu.AEBaseMenu;
import appeng.menu.me.items.PatternEncodingTermMenu;
import io.github.linkfgfgui.pattern_uploader.network.IPatternEncodingIdSync;
import io.github.linkfgfgui.pattern_uploader.utils.RecipeFinderUtilEMI;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static io.github.linkfgfgui.pattern_uploader.PatternUploader.recipeIdString;


@Mixin(PatternEncodingTermMenu.class)
public abstract class PatternEncodingTermMenuMixin implements IPatternEncodingIdSync {
    @Unique
    ResourceLocation eap$pendingRecipeIdUpload = null;

    @Unique
    public void eap$clientRecipeIdUpload(ResourceLocation id) {
        this.eap$pendingRecipeIdUpload = id;
    }


    @Inject(method = "encodePattern", at = @At("TAIL"), remap = false, cancellable = true)
    private void eap$writeEncodePlayerToPattern(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        if (itemStack != null && !itemStack.isEmpty()) {
            CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> {
                if (this.eap$pendingRecipeIdUpload != null) {
                    if (RecipeFinderUtilEMI.isRecipeEqualToPattern(itemStack, this.eap$pendingRecipeIdUpload, ((AEBaseMenu) (Object) this).getPlayer().level())) {
                        tag.putString(recipeIdString, this.eap$pendingRecipeIdUpload.toString());
                    }
                    this.eap$pendingRecipeIdUpload = null;
                }
            });
            cir.setReturnValue(itemStack);
        }
    }
}

