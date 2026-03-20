package io.github.linkfgfgui.pattern_uploader.mixin;

import appeng.api.config.ActionItems;
import appeng.client.gui.me.items.PatternEncodingTermScreen;
import appeng.client.gui.widgets.ActionButton;
import io.github.linkfgfgui.pattern_uploader.network.UploadInventoryPatternsToProvidersC2SPacket;
import net.minecraft.client.gui.screens.Screen;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PatternEncodingTermScreen.class)
public class PatternEncodingTermScreenMixin {
    @ModifyVariable(method = "<init>", at = @At(value = "STORE"), name = "encodeBtn")
    private ActionButton eap$encodingButton(ActionButton button) {
        return new ActionButton(ActionItems.ENCODE, actionItems -> {
            if (Screen.hasAltDown()) {
                PacketDistributor.sendToServer(new UploadInventoryPatternsToProvidersC2SPacket());
            }else{
                var screen = (PatternEncodingTermScreen<?>) (Object) this;
                screen.getMenu().encode();
            }
        });
    }
}
