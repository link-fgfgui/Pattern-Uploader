package io.github.linkfgfgui.pattern_uploader.utils;

import appeng.api.crafting.IPatternDetails;
import appeng.api.crafting.PatternDetailsHelper;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.fml.ModList;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface RecipeFinderUtil {
    static RecipeFinderUtil getApi() {
        if (INTEGRATED.EMI) {
            return new RecipeFinderUtilEMI();
        } else if (INTEGRATED.JEI) {
            return new RecipeFinderUtilJEI();
        } else {
            return null;
        }
    }

    @Nullable List<ResourceLocation> getWorkstationIdsByCategoryId(ResourceLocation id);

    @Nullable Component getWorkstationComponentByCategoryId(String id);

    default boolean isRecipeEqualToPattern(ItemStack itemStack, ResourceLocation location, Level level) {
        IPatternDetails pattern = PatternDetailsHelper.decodePattern(itemStack, level);
        return isRecipeEqualToPattern(pattern, location);
    }

    boolean isRecipeEqualToPattern(@Nullable IPatternDetails pattern, ResourceLocation location);

    class INTEGRATED {
        public static boolean JEI;
        public static boolean EMI;

        static {
            ModList list = ModList.get();
            if (list.isLoaded("emi")) {
                EMI = true;
            } else if (list.isLoaded("jei")) {
                JEI = true;
            }
        }
    }

    class INSTANCE {
        static RecipeFinderUtil api = null;

        public static RecipeFinderUtil get() {
            if (api == null) {
                api = getApi();
            }
            return api;
        }
    }
}
