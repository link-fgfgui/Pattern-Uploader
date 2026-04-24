package io.github.linkfgfgui.pattern_uploader;

import com.mojang.logging.LogUtils;
import io.github.linkfgfgui.pattern_uploader.network.EncodeWithRecipeIdC2SPacket;
import io.github.linkfgfgui.pattern_uploader.network.UploadInventoryPatternsToProvidersC2SPacket;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.slf4j.Logger;

@Mod(PatternUploader.MODID)
public class PatternUploader {
    public static final String MODID = "pattern_uploader";
    public static final String recipeIdString = "recipeId";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PatternUploader(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::registerPayloadHandlers);
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID);
        registrar.playToServer(EncodeWithRecipeIdC2SPacket.TYPE, EncodeWithRecipeIdC2SPacket.STREAM_CODEC, EncodeWithRecipeIdC2SPacket::handle);
        registrar.playToServer(UploadInventoryPatternsToProvidersC2SPacket.TYPE, UploadInventoryPatternsToProvidersC2SPacket.STREAM_CODEC, UploadInventoryPatternsToProvidersC2SPacket::handle);
//        registrar.playToServer(UploadCategoryWorkstationsC2SPacket.TYPE, UploadCategoryWorkstationsC2SPacket.STREAM_CODEC, UploadCategoryWorkstationsC2SPacket::handle);
    }
}
