package io.github.linkfgfgui.pattern_uploader.mixin;

import appeng.core.AppEng;
import appeng.crafting.pattern.EncodedPatternItem;
import io.github.linkfgfgui.pattern_uploader.utils.RecipeUtil;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Consumer;

import static io.github.linkfgfgui.pattern_uploader.PatternUploader.recipeIdString;

@Mixin(EncodedPatternItem.class)
public class EncodedPatternItemMixin {
    // 客户端：在 HoverText 显示样板的编码玩家 和 加工机器
    @Inject(method = "appendHoverText", at = @At("TAIL"))
    public void epp$appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay tooltipDisplay, Consumer<Component> lines, TooltipFlag flags, CallbackInfo ci) {
        if (true) {
            Level clientLevel = AppEng.instance().getClientLevel();
            RecipeUtil util = new RecipeUtil(clientLevel);
            var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            var tag = customData.copyTag();
            if (tag.contains(recipeIdString)) {
                Component c = util.getWorkstationComponentByRecipeStringId(tag.getString(recipeIdString).orElse(null));
                if (c != null) {
                    lines.accept(Component.translatable("pattern_uploader.pattern.hovertext.workstation", c).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }
}