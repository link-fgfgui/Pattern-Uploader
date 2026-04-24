package io.github.linkfgfgui.pattern_uploader.utils;

import dev.recipesync.RecipeSync;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplayContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class RecipeUtil {
    final Level level;

    public RecipeUtil(Level level) {
        this.level = level;
    }

    public @Nullable RecipeHolder<?> getRecipeHolder(String sid) {
        if (sid == null || sid.isEmpty()) return null;
        Identifier id = Identifier.tryParse(sid);
        if (id == null) return null;
        return RecipeSync.getMap().byKey(ResourceKey.create(Registries.RECIPE, id));
    }

    public List<ItemStack> getCraftStations(RecipeHolder<?> recipeHolder, boolean once) {
        List<RecipeDisplay> displays = recipeHolder.value().display();
        ContextMap ctx = SlotDisplayContext.fromLevel(level);
        List<ItemStack> itemStacks = new ArrayList<>();
        for (RecipeDisplay display : displays) {
            itemStacks.addAll(display.craftingStation().resolveForStacks(ctx));
            if (once && !itemStacks.isEmpty()) {
                return List.of(itemStacks.getFirst());
            }
        }
        return itemStacks;
    }

    public List<Identifier> getIdList(List<ItemStack> itemStacks) {
        return itemStacks.stream().map(ItemStack::getItem).map(BuiltInRegistries.ITEM::getKey).toList();
    }


    public RecipeType<?> getType(RecipeHolder<?> recipeHolder) {
        return recipeHolder.value().getType();
    }

    public Identifier getTypeId(RecipeType<?> type) {
        return BuiltInRegistries.RECIPE_TYPE.getKey(type);
    }

    public boolean hasCraftStation(RecipeHolder<?> recipeHolder) {
        return !getCraftStations(recipeHolder, true).isEmpty();
    }

    public @Nullable RecipeHolder<?> getRecipeHolder(RecipeType<?> type) {
        return (RecipeHolder) RecipeSync.getMap().byType((RecipeType) type).stream()
                .filter(r -> hasCraftStation((RecipeHolder) r))
                .findFirst().orElse(null);
    }

    public @Nullable List<Identifier> getCraftStationIdsByRecipeStringId(String sid) {
        RecipeHolder<?> recipe = getRecipeHolder(sid);
        if (recipe == null) return null;
        return getIdList(getCraftStations(recipe, false));
    }

    public @Nullable Component getWorkstationComponentByRecipeStringId(String sid) {
        RecipeHolder<?> recipe = getRecipeHolder(sid);
        if (recipe == null) return null;
        List<ItemStack> stackList = getCraftStations(recipe, true);
        if (stackList.isEmpty()) return null;
        ItemStack is = getCraftStations(recipe, true).getFirst();
        return is.getItemName();
    }


    public @Nullable Identifier getTypeIdFromRecipeStringId(String sid) {
        var recipe = getRecipeHolder(sid);
        if (recipe != null) {
            return getTypeId(getType(recipe));
        }
        return null;
    }
}
