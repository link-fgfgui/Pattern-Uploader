package io.github.linkfgfgui.pattern_uploader.utils;

import io.github.linkfgfgui.pattern_uploader.network.EncodeWithRecipeIdC2SPacket;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;

public class SendPackets {
    public static void send(Identifier id) {
        if (id == null) return;
        ClientPacketDistributor.sendToServer(new EncodeWithRecipeIdC2SPacket(id));
    }
}