package io.github.linkfgfgui.pattern_uploader.mixin;

import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

public class PatternUploaderMixinPlugin implements IMixinConfigPlugin {
    private static boolean isClassPresent(String className) {
        try {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            Class.forName(className, false, cl);
            return true;
        } catch (Throwable ignored) {
            return false;
        }
    }

    private static boolean isJeiPresent() {
        return isClassPresent("mezz.jei.api.IModPlugin");
    }

    private static boolean isEmiPresent() {
        return isClassPresent("dev.emi.emi.api.EmiPlugin");
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!isJeiPresent()) {
            return !mixinClassName.startsWith("io.github.linkfgfgui.pattern_uploader.mixin.jei");
        }
        if (!isEmiPresent()) {
            return !mixinClassName.startsWith("io.github.linkfgfgui.pattern_uploader.mixin.emi");
        }
        return true;
    }


    @Override
    public void onLoad(String mixinPackage) {

    }

    @Override
    public String getRefMapperConfig() {
        return "";
    }

    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {

    }

    @Override
    public List<String> getMixins() {
        return List.of();
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }
}
