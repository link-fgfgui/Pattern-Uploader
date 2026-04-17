package io.github.linkfgfgui.pattern_uploader.utils;

import appeng.api.crafting.IPatternDetails;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RecipeFinderUtilREI implements RecipeFinderUtil {
    @Override
    public @Nullable List<ResourceLocation> getWorkstationIdsByCategoryId(ResourceLocation id) {
        return null;
    }

    @Override
    public @Nullable Component getWorkstationComponentByCategoryId(String id) {
        return null;
    }

    @Override
    public boolean isRecipeEqualToPattern(@Nullable IPatternDetails pattern, ResourceLocation location) {
        return false;
    }
}
