package io.github.linkfgfgui.pattern_uploader;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_RECIPE_CATEGORIES = BUILDER
            .defineList(
                    "blacklistedCategories",
                    List.of(),
                    () -> "minecraft:campfire_cooking",
                    obj -> {
                        if (obj instanceof String s) {
                            return ResourceLocation.tryParse(s) != null;
                        }
                        return false;
                    }
            );

    public static final ModConfigSpec.BooleanValue IS_CHECK_PATTERN_OUTPUT = BUILDER.define("isCheckPatternOutput", true);



    static final ModConfigSpec SPEC = BUILDER.build();
}
