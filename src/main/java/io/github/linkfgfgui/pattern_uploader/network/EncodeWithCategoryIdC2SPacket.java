package io.github.linkfgfgui.pattern_uploader.network;

import appeng.menu.me.items.PatternEncodingTermMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import static io.github.linkfgfgui.pattern_uploader.PatternUploader.MODID;

public class EncodeWithCategoryIdC2SPacket implements CustomPacketPayload {

    public static final Type<EncodeWithCategoryIdC2SPacket> TYPE = new Type<>(
            ResourceLocation.fromNamespaceAndPath(MODID, "encode_with_cate"));

    public static final StreamCodec<FriendlyByteBuf, EncodeWithCategoryIdC2SPacket> STREAM_CODEC = StreamCodec.of(
            (buf, pkt) -> buf.writeResourceLocation(pkt.id),
            buf -> new EncodeWithCategoryIdC2SPacket(buf.readResourceLocation())
    );

    private final ResourceLocation id;

    public EncodeWithCategoryIdC2SPacket(ResourceLocation id) {
        this.id = id;
    }

    public static void handle(final EncodeWithCategoryIdC2SPacket msg, final IPayloadContext ctx) {
        ctx.enqueueWork(() -> {
            if (!(ctx.player() instanceof ServerPlayer player)) {
                return;
            }
            if (player.containerMenu instanceof PatternEncodingTermMenu menu && menu instanceof IPatternEncodingIdSync sync) {
                sync.eap$clientCategoryIdUpload(msg.id);
            }
        });
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

}
