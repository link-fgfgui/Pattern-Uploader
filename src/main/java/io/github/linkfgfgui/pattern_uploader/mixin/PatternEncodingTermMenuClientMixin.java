package io.github.linkfgfgui.pattern_uploader.mixin;

import appeng.menu.me.items.PatternEncodingTermMenu;
import io.github.linkfgfgui.pattern_uploader.network.UploadInventoryPatternsToProvidersC2SPacket;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PatternEncodingTermMenu.class)
public class PatternEncodingTermMenuClientMixin {
    @Inject(method = "encode", at = @At("HEAD"), remap = false, cancellable = true)
    private void onEncode(CallbackInfo ci) {
        if (((PatternEncodingTermMenu) (Object) this).isClientSide()) {
            if (Screen.hasAltDown()) {
                PacketDistributor.sendToServer(new UploadInventoryPatternsToProvidersC2SPacket());
                ci.cancel();
            }
        }
    }
}

