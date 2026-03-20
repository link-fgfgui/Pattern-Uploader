package io.github.linkfgfgui.pattern_uploader;

import appeng.api.crafting.PatternDetailsHelper;
import appeng.api.inventories.InternalInventory;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.helpers.patternprovider.PatternProviderLogicHost;
import appeng.menu.me.items.PatternEncodingTermMenu;
import com.mojang.logging.LogUtils;
import io.github.linkfgfgui.pattern_uploader.utils.RecipeFinderUtilEMI;
import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.util.*;

import static io.github.linkfgfgui.pattern_uploader.PatternUploader.recipeIdString;

public class Upload {
    static Logger LOGGER = LogUtils.getLogger();
    static Map<ResourceLocation, Set<PatternProviderLogicHost>> workstation2ProvidersMap;
    static Map<ResourceLocation, Set<PatternProviderLogicHost>> recipe2ProvidersMap;

    public static void addToRecipeMap(ResourceLocation recipe, PatternProviderLogicHost host) {
        recipe2ProvidersMap.computeIfAbsent(recipe, key -> new HashSet<>()).add(host);
    }

    public static void addToWorkstationMap(ResourceLocation workstation, PatternProviderLogicHost host) {
        workstation2ProvidersMap.computeIfAbsent(workstation, key -> new HashSet<>()).add(host);
    }

    public static void upload(ServerPlayer player) {

        if (!(player.containerMenu instanceof PatternEncodingTermMenu encMenu)) return;

        IGridNode node = encMenu.getGridNode();
        if (node == null) {
            return;
        }
        recipe2ProvidersMap = new HashMap<>();
        workstation2ProvidersMap = new HashMap<>();
        IGrid grid = node.getGrid();
        Set<PatternProviderLogicHost> hosts = new HashSet<>();
        grid.getMachineClasses().forEach(clazz -> {
            if (PatternProviderLogicHost.class.isAssignableFrom(clazz)) {
                hosts.addAll((Collection<? extends PatternProviderLogicHost>) grid.getMachines(clazz));
            }
        });
        for (PatternProviderLogicHost host : hosts) {

            long s1 = recipe2ProvidersMap.size();
            InternalInventory patterns = host.getLogic().getPatternInv();
            for (ItemStack pattern : patterns) {
                CustomData customData = pattern.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                CompoundTag tag = customData.copyTag();
                ResourceLocation location = RecipeFinderUtilEMI.getRecipeCategoryIdByRecipeId(tag.getString(recipeIdString));
                if (location != null) {
                    addToRecipeMap(location, host);
                    break;
                }
            }
            if (recipe2ProvidersMap.size() > s1) {
                continue;
            }

            Level level = host.getBlockEntity().getLevel();
            BlockPos pos = host.getBlockEntity().getBlockPos();
            if (level != null) {
                host.getTargets().forEach(side -> {
                    BlockState bs = level.getBlockState(pos.relative(side));
                    if (bs.hasBlockEntity()) {
                        ResourceLocation targetBlockId = BuiltInRegistries.BLOCK.getKey(bs.getBlock());
                        addToWorkstationMap(targetBlockId, host);
                    }
                });
            }
        }

        Inventory inventory = player.getInventory();
        Item pattern = Item.byId(BuiltInRegistries.ITEM.getId(ResourceLocation.fromNamespaceAndPath("ae2", "processing_pattern")));
        for (int index = 0; index < inventory.items.size(); index++) {
            ItemStack is = inventory.getItem(index);
            if (is.is(pattern)) {
                PatternDetailsHelper.isEncodedPattern(is);
                var customData = is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
                var tag = customData.copyTag();
                if (tag.contains(recipeIdString)) {
                    if (tryInsert(recipe2ProvidersMap.get(RecipeFinderUtilEMI.getRecipeCategoryIdByRecipeId(tag.getString(recipeIdString))), is, inventory, index)) {
                        continue;
                    }
                    @Nullable List<ResourceLocation> locations = RecipeFinderUtilEMI.getWorkstationIdsByRecipeId(tag.getString(recipeIdString));
                    if (locations != null) {
                        for (ResourceLocation location : locations) {
                            if (is.isEmpty()) break;
                            Set<PatternProviderLogicHost> hosts2 = workstation2ProvidersMap.get(location);
                            if (tryInsert(hosts2, is, inventory, index)) {
                                break;
                            }
                        }
                    }
                }
                LOGGER.info("A Pattern Actioned");
            }
        }
    }

    static boolean tryInsert(@Nullable Set<PatternProviderLogicHost> hosts, ItemStack is, Inventory inventory, int index) {
        if (hosts != null) {
            for (PatternProviderLogicHost host : hosts) {
                InternalInventory patternInv = host.getLogic().getPatternInv();
                if (patternInv.addItems(is, true).equals(ItemStack.EMPTY)) {
                    inventory.setItem(index, patternInv.addItems(is));
                    return true;
                }
            }
        }
        return false;
    }

}
