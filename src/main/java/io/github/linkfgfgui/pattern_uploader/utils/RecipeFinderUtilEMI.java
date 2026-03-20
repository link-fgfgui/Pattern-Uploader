package io.github.linkfgfgui.pattern_uploader.utils;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.GenericStack;
import com.mojang.logging.LogUtils;
import dev.emi.emi.api.EmiApi;
import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeManager;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.List;

/**
 * 基于 EMI 查找配方
 */
public final class RecipeFinderUtilEMI {
    private static final Logger LOGGER = LogUtils.getLogger();
    public static EmiRecipeManager manager = EmiApi.getRecipeManager();

    @Nullable
    public static EmiRecipe findRecipeById(ResourceLocation location) {
        return manager.getRecipe(location);
    }

    @Nullable
    public static EmiRecipe findRecipeById(String id) {
        return findRecipeById(ResourceLocation.parse(id));
    }

    @Nullable
    public static ResourceLocation getRecipeCategoryIdByRecipeId(ResourceLocation id) {
        EmiRecipe recipe = findRecipeById(id);
        if (recipe != null) {
            return recipe.getCategory().getId();
        }
        return null;
    }

    @Nullable
    public static ResourceLocation getRecipeCategoryIdByRecipeId(String id) {
        return getRecipeCategoryIdByRecipeId(ResourceLocation.tryParse(id));
    }

    @Nullable
    public static List<EmiStack> getWorkstationStacksByRecipeId(String id) {
        EmiRecipe recipe = RecipeFinderUtilEMI.findRecipeById(id);
        if (recipe != null) {
            List<EmiIngredient> workstations = EmiApi.getRecipeManager().getWorkstations(recipe.getCategory());
            if (!workstations.isEmpty()) {
                return workstations.stream().map(EmiIngredient::getEmiStacks).flatMap(List::stream).toList();
            }
        }
        return null;
    }

    @Nullable
    public static List<ResourceLocation> getWorkstationIdsByRecipeId(String id) {
        List<EmiStack> workstations = RecipeFinderUtilEMI.getWorkstationStacksByRecipeId(id);
        if (workstations != null) {
            return workstations.stream().map(EmiStack::getId).toList();
        }
        return null;
    }

    @Nullable
    public static Component getWorkstationComponentByRecipeId(String id) {
        List<EmiStack> workstations = RecipeFinderUtilEMI.getWorkstationStacksByRecipeId(id);
        if (workstations != null) {
            return workstations.getFirst().getName();
        }
        return null;
    }

    public static boolean isRecipeEqualToPattern(ItemStack itemStack, ResourceLocation location, Level level) {
        EmiRecipe recipe = findRecipeById(location);
        // 检查样板与配方是否对应
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
}

