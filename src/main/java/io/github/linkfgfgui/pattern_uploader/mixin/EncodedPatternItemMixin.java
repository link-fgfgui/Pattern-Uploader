package io.github.linkfgfgui.pattern_uploader.mixin;

import appeng.crafting.pattern.EncodedPatternItem;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

import static io.github.linkfgfgui.pattern_uploader.PatternUploader.recipeIdString;
import static io.github.linkfgfgui.pattern_uploader.Upload.recipeFinderUtil;

@Mixin(EncodedPatternItem.class)
public class EncodedPatternItemMixin {
    // 客户端：在 HoverText 显示样板的编码玩家 和 加工机器
    @Inject(method = "appendHoverText", at = @At("TAIL"))
    public void epp$appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> lines, TooltipFlag advancedTooltips, CallbackInfo ci) {
        if (true) {
            var customData = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
            var tag = customData.copyTag();
            if (tag.contains(recipeIdString)) {
                Component c = recipeFinderUtil.getWorkstationComponentByRecipeId(tag.getString(recipeIdString));
                if (c != null) {
                    lines.add(Component.translatable("pattern_uploader.pattern.hovertext.workstation", c).withStyle(ChatFormatting.GRAY));
                }
            }
        }
    }
}