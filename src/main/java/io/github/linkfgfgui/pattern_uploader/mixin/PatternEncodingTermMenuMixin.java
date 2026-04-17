package io.github.linkfgfgui.pattern_uploader.mixin;

import appeng.menu.me.items.PatternEncodingTermMenu;
import io.github.linkfgfgui.pattern_uploader.network.IPatternEncodingIdSync;
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
    ResourceLocation eap$pendingCategoryIdUpload = null;

    @Unique
    public void eap$clientCategoryIdUpload(ResourceLocation id) {
        this.eap$pendingCategoryIdUpload = id;
    }

    @Inject(method = "encodePattern", at = @At("TAIL"), remap = false, cancellable = true)
    private void eap$writeEncodePlayerToPattern(CallbackInfoReturnable<ItemStack> cir) {
        ItemStack itemStack = cir.getReturnValue();
        if (itemStack != null && !itemStack.isEmpty()) {
            CustomData.update(DataComponents.CUSTOM_DATA, itemStack, tag -> {
                if (this.eap$pendingCategoryIdUpload != null) {
                    tag.putString(recipeIdString, this.eap$pendingCategoryIdUpload.toString());
                    this.eap$pendingCategoryIdUpload = null;
                }
            });
            cir.setReturnValue(itemStack);
        }
    }
}

