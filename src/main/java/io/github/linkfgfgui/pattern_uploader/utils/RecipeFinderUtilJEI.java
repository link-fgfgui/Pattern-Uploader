package io.github.linkfgfgui.pattern_uploader.utils;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.stacks.AEItemKey;
import appeng.api.stacks.GenericStack;
import io.github.linkfgfgui.pattern_uploader.PatternUploader;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.recipe.IRecipeManager;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

@JeiPlugin
public final class RecipeFinderUtilJEI implements RecipeFinderUtil, IModPlugin {

    static public IRecipeManager iRecipeManager = null;

    @Nullable
    private static RecipeManager getRecipeManager() {
        if (Minecraft.getInstance().level != null) {
            return Minecraft.getInstance().level.getRecipeManager();
        } else if (Minecraft.getInstance().getConnection() != null) {
            return Minecraft.getInstance().getConnection().getRecipeManager();
        }
        return null;
    }

    @Nullable
    public Recipe<?> findRecipeById(ResourceLocation location) {
        RecipeManager manager = getRecipeManager();
        if (manager == null || iRecipeManager == null) return null;
        Optional<RecipeHolder<?>> recipe = manager.byKey(location);
        if (recipe.isEmpty()) return null;
        return recipe.get().value();
    }

    public @Nullable RecipeType<?> getRecipeCategoryByCategoryId(String id) {
        if (id == null || iRecipeManager == null) return null;
        ResourceLocation uid = ResourceLocation.parse(id);
        return iRecipeManager.getRecipeType(uid).orElse(null);
    }

    public @Nullable RecipeType<?> getRecipeCategoryByRecipeId(ResourceLocation id) {
        Recipe<?> recipe = findRecipeById(id);
        if (recipe == null) {
            return null;
        }
        ResourceLocation typeId = BuiltInRegistries.RECIPE_TYPE.getKey(recipe.getType());
        if (typeId == null) return null;

        return getRecipeCategoryByCategoryId(typeId.toString());
    }

    @Override
    public @Nullable List<ResourceLocation> getWorkstationIdsByCategoryId(ResourceLocation id) {
        RecipeType<?> recipeCategory = getRecipeCategoryByCategoryId(id.toString());
        List<ItemStack> workstations = getWorkstationsByCategory(recipeCategory, false);
        if (workstations == null) return null;
        return workstations
                .stream()
                .map(stack -> stack.getItemHolder().unwrapKey().map(ResourceKey::location).orElse(ResourceLocation.fromNamespaceAndPath("minecraft", "air")))
                .toList();
    }

    public @Nullable List<ItemStack> getWorkstationsByRecipeId(String id) {
        return getWorkstationsByRecipeId(id, false);
    }

    public @Nullable List<ItemStack> getWorkstationsByCategory(RecipeType<?> category, boolean once) {
        if (category != null) {
            var stream = iRecipeManager
                    .createRecipeCatalystLookup(category)
                    .get()
                    .map(typedIngredient -> typedIngredient.getItemStack().orElse(ItemStack.EMPTY))
                    .filter(stack -> !stack.isEmpty());
            if (once) {
                Optional<ItemStack> item = stream.findFirst();
                return item.map(List::of).orElse(null);
            } else {
                return stream.toList();
            }
        }
        return null;
    }

    public @Nullable List<ItemStack> getWorkstationsByRecipeId(String id, boolean once) {
        RecipeType<?> category = getRecipeCategoryByRecipeId(ResourceLocation.parse(id));
        return getWorkstationsByCategory(category, once);
    }

    @Override
    public @Nullable Component getWorkstationComponentByCategoryId(String id) {
        List<ItemStack> workstation = getWorkstationsByCategory(getRecipeCategoryByCategoryId(id), true);
        if (workstation != null && !workstation.isEmpty()) {
            return Component.translatable(workstation.getFirst().getItem().getDescriptionId());
        }
        return null;
    }

    // TODO: fix mek
    @Override
    public boolean isRecipeEqualToPattern(ItemStack itemStack, ResourceLocation location, Level level) {
        Recipe<?> recipe = findRecipeById(location);
        IPatternDetails pattern = PatternDetailsHelper.decodePattern(itemStack, level);
        HolderLookup.Provider registries = level.registryAccess();
        if (pattern != null) {
            List<GenericStack> stacks = pattern.getOutputs();
            if (stacks != null && recipe != null) {
                GenericStack primaryOutput = pattern.getPrimaryOutput();
                if (primaryOutput != null) {
                    if (primaryOutput.what() instanceof AEItemKey aeItemKey) {
                        Item patternItem = aeItemKey.getItem();
                        Item recipeItem = recipe.getResultItem(registries).getItem();
                        return patternItem.equals(recipeItem);
                    }
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public boolean isRecipeEqualToPattern(@Nullable IPatternDetails pattern, ResourceLocation location) {
        return false;
    }

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return ResourceLocation.fromNamespaceAndPath(PatternUploader.MODID, "jei_plugin");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        iRecipeManager = jeiRuntime.getRecipeManager();
    }
}

