package io.github.linkfgfgui.pattern_uploader;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

//    public static final ModConfigSpec.ConfigValue<List<? extends String>> BLACKLISTED_RECIPE_CATEGORIES = BUILDER
//            .defineList(
//                    "blacklistedCategories",
//                    List.of(), // 1. 默认值
//                    () -> "minecraft:overworld",
//                    obj -> {
//                        if (obj instanceof String s) {
//                            return ResourceLocation.tryParse(s) != null;
//                        }
//                        return false;
//                    }
//            );

    static final ModConfigSpec SPEC = BUILDER.build();
    private static boolean validateItemName(final Object obj) {
        return obj instanceof String itemName && BuiltInRegistries.ITEM.containsKey(ResourceLocation.parse(itemName));
    }

    static void onLoad(final ModConfigEvent event) {
//        items = ITEM_STRINGS.get().stream().map(itemName -> BuiltInRegistries.ITEM.get(ResourceLocation.parse(itemName))).collect(Collectors.toSet());
    }
}
