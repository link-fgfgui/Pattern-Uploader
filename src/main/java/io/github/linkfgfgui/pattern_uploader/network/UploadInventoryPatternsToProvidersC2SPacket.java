package io.github.linkfgfgui.pattern_uploader.network;

import io.github.linkfgfgui.pattern_uploader.server.Upload;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static io.github.linkfgfgui.pattern_uploader.PatternUploader.MODID;


public class UploadInventoryPatternsToProvidersC2SPacket implements CustomPacketPayload {
    public static final Type<UploadInventoryPatternsToProvidersC2SPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(MODID, "upload_inventory_patterns_to_providers"));
    public static final StreamCodec<FriendlyByteBuf, UploadInventoryPatternsToProvidersC2SPacket> STREAM_CODEC = StreamCodec.of((buf, pkt) -> {
    }, buf -> new UploadInventoryPatternsToProvidersC2SPacket());

    public UploadInventoryPatternsToProvidersC2SPacket() {

    }

    public static void handle(final UploadInventoryPatternsToProvidersC2SPacket msg, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) return;
            Upload.upload(player);
        });
    }


    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
