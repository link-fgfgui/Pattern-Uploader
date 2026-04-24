package io.github.linkfgfgui.pattern_uploader.network;

import net.minecraft.resources.Identifier;

public interface IPatternEncodingIdSync {
    /**
     * 由客户端发送的编码指令附带的 RecipeId。
     */
    void eap$clientRecipeIdUpload(Identifier id);
}
