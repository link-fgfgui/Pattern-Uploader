package io.github.linkfgfgui.pattern_uploader.utils;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.GenericStack;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public final class RecipeFinderUtilEMI implements RecipeFinderUtil {
    @Nullable
    public EmiRecipe findRecipeById(ResourceLocation location) {
        return EmiApi.getRecipeManager().getRecipe(location);
    }

    @Nullable
    public EmiRecipe findRecipeById(String id) {
        return findRecipeById(ResourceLocation.parse(id));
    }

    @Nullable
    public List<EmiStack> getWorkstationStacksByCategory(EmiRecipeCategory category) {
        if (category != null) {
            List<EmiIngredient> workstations = EmiApi.getRecipeManager().getWorkstations(category);
            if (!workstations.isEmpty()) {
                return workstations.stream().map(EmiIngredient::getEmiStacks).flatMap(List::stream).toList();
            }
        }
        return null;
    }

    @Nullable
    public List<EmiStack> getWorkstationStacksByRecipeId(String id) {
        EmiRecipe recipe = findRecipeById(id);
        if (recipe != null) {
            return getWorkstationStacksByCategory(recipe.getCategory());
        }
        return null;
    }

    @Override
    public @Nullable List<ResourceLocation> getWorkstationIdsByCategoryId(ResourceLocation id) {
        List<EmiStack> workstations = getWorkstationStacksByCategory(getCategoryById(id));
        if (workstations != null) {
            return workstations.stream().map(EmiStack::getId).toList();
        }
        return null;
    }

    public @Nullable EmiRecipeCategory getCategoryById(ResourceLocation id) {
        return EmiApi.getRecipeManager().getCategories().stream().filter(c -> c.getId().equals(id)).findFirst().orElse(null);
    }

    @Override
    public @Nullable Component getWorkstationComponentByCategoryId(String id) {
        List<EmiStack> workstations = getWorkstationStacksByCategory(getCategoryById(ResourceLocation.parse(id)));
        if (workstations != null && !workstations.isEmpty()) {
            return workstations.getFirst().getName();
        }
        return null;
    }

    @Override
    public boolean isRecipeEqualToPattern(ItemStack itemStack, ResourceLocation location, Level level) {
        EmiRecipe recipe = findRecipeById(location);
        IPatternDetails pattern = PatternDetailsHelper.decodePattern(itemStack, level);
        if (pattern != null) {
            List<GenericStack> stacks = pattern.getOutputs();
            if (stacks != null && recipe != null) {
                List<EmiStack> stacks2 = recipe.getOutputs();

                List<ResourceLocation> ids1 = stacks.stream().map(s -> s.what().getId()).sorted().toList();
                List<ResourceLocation> ids2 = stacks2.stream().map(EmiStack::getId).sorted().toList();

                return ids1.equals(ids2);
            }
        }
        return false;
    }

    // it will never be called if you override the former.
    @Override
    public boolean isRecipeEqualToPattern(@Nullable IPatternDetails pattern, ResourceLocation location) {
        return false;
    }
}

