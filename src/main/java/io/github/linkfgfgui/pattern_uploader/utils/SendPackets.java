package io.github.linkfgfgui.pattern_uploader.utils;

import io.github.linkfgfgui.pattern_uploader.network.EncodeWithCategoryIdC2SPacket;
import io.github.linkfgfgui.pattern_uploader.network.UploadCategoryWorkstationsC2SPacket;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.PacketDistributor;

import java.util.List;

public class SendPackets {
    public static void send(ResourceLocation categoryId) {
        if (categoryId == null) return;
        PacketDistributor.sendToServer(new EncodeWithCategoryIdC2SPacket(categoryId));
        List<ResourceLocation> workstationIds = RecipeFinderUtil.INSTANCE.get().getWorkstationIdsByCategoryId(categoryId);
        if (workstationIds == null) return;
        workstationIds.addFirst(categoryId);
        PacketDistributor.sendToServer(new UploadCategoryWorkstationsC2SPacket(workstationIds));
    }
}
