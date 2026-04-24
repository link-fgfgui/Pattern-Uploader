package io.github.linkfgfgui.pattern_uploader.mixin;

import appeng.menu.me.items.PatternEncodingTermMenu;
import com.mojang.blaze3d.platform.InputConstants;
import io.github.linkfgfgui.pattern_uploader.network.UploadInventoryPatternsToProvidersC2SPacket;
import net.minecraft.client.Minecraft;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(PatternEncodingTermMenu.class)
public class PatternEncodingTermMenuClientMixin {
    @Inject(method = "encode", at = @At("HEAD"), remap = false, cancellable = true)
    private void onEncode(CallbackInfo ci) {
        if (((PatternEncodingTermMenu) (Object) this).isClientSide()) {
            if (InputConstants.isKeyDown(Minecraft.getInstance().getWindow(), GLFW.GLFW_KEY_LEFT_ALT)) {
                ClientPacketDistributor.sendToServer(new UploadInventoryPatternsToProvidersC2SPacket());
                ci.cancel();
            }
        }
    }
}

