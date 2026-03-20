package io.github.linkfgfgui.pattern_uploader.network;

import net.minecraft.resources.ResourceLocation;

public interface IPatternEncodingIdSync {
    /**
     * 由客户端发送的编码指令附带的 recipe id。
     */
    void eap$clientRecipeIdUpload(ResourceLocation id);
}
