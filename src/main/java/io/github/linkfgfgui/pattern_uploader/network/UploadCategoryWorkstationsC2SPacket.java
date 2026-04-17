package io.github.linkfgfgui.pattern_uploader.network;

import io.github.linkfgfgui.pattern_uploader.server.Upload;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.List;
import java.util.stream.Collectors;

import static io.github.linkfgfgui.pattern_uploader.PatternUploader.MODID;

public record UploadCategoryWorkstationsC2SPacket(List<ResourceLocation> ids) implements CustomPacketPayload {

    public static final Type<UploadCategoryWorkstationsC2SPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MODID, "upload_workstations"));

    public static final StreamCodec<ByteBuf, UploadCategoryWorkstationsC2SPacket> STREAM_CODEC = ResourceLocation.STREAM_CODEC
            .apply(ByteBufCodecs.list())
            .map(UploadCategoryWorkstationsC2SPacket::new, UploadCategoryWorkstationsC2SPacket::ids);

    public static void handle(final UploadCategoryWorkstationsC2SPacket msg, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }
            List<ResourceLocation> ids = msg.ids();
            Upload.addToCateStationMap(ids.getFirst(), ids.stream().skip(1).collect(Collectors.toSet()));
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
